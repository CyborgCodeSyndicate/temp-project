package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.util.AssertionUtil;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//todo check spring somehow as the others to add allure
@NoArgsConstructor
public class UiTableValidatorImpl implements UiTableValidator {


    protected void printAssertionTarget(Map<String, Object> data) {
        LogUI.extended("Validation target: [{}]", data.toString());
    }


    @Override
    public <T> List<AssertionResult<T>> validateTable(final Object object, final Assertion<?>... assertions) {
        LogUI.info("Starting response validation with {} assertion(s).", assertions.length);

        Map<String, T> data = new HashMap<>();

        for (Assertion<?> assertion : assertions) {
            switch ((UiTablesAssertionTarget) assertion.getTarget()) {
                case ROW -> {
                    data.put("row", (T) getRowValues(object));
                    assertion.setKey("row");
                }
                case TABLE -> {
                    List<?> rows = (List<?>) object;
                    List<List<String>> rowList = rows.stream().map(UiTableValidatorImpl::getRowValues).toList();
                    data.put("table", (T) rowList);
                    assertion.setKey("table");
                }
            }
        }

        printAssertionTarget((Map<String, Object>) data);
        return AssertionUtil.validate(data, assertions);
    }


    //todo handle duplicate code
    private static List<String> getRowValues(Object row) {
        return Arrays.stream(row.getClass().getDeclaredFields())
                   .filter(field -> TableCell.class.isAssignableFrom(field.getType()) || isListOfTableCell(field))
                   .map(field -> {
                       field.setAccessible(true);
                       try {
                           Object value = field.get(row);
                           if (value instanceof TableCell cell) {
                               return Collections.singletonList(cell.getText());
                           } else if (value instanceof List<?> list) {
                               List<String> result = list.stream()
                                                         .filter(TableCell.class::isInstance)
                                                         .map(TableCell.class::cast)
                                                         .map(TableCell::getText)
                                                         .collect(Collectors.toList());
                               return result;
                           }
                       } catch (IllegalAccessException e) {
                           throw new RuntimeException("Failed to access field value", e);
                       }
                       return Collections.<String>emptyList();
                   })
                   .flatMap(List::stream)
                   .collect(Collectors.toList());
    }


    //todo handle duplicate code
    private static boolean isListOfTableCell(final Field field) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }
        final Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType parameterizedType) {
            final Type[] typeArguments = parameterizedType.getActualTypeArguments();
            return (typeArguments.length == 1) && (typeArguments[0] == TableCell.class);
        }
        return false;
    }


    private static List<String> lowerCaseAndTrimListOfStrings(List<String> list) {
        return list.stream()
                   .map(s -> s.trim().toLowerCase())
                   .toList();
    }

}
