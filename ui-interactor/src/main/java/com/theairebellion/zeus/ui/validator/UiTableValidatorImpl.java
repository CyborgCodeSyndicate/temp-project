package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.util.AssertionUtil;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UiTableValidator} responsible for validating table data and elements.
 * <p>
 * This class provides methods for extracting table row values, table elements, and performing
 * assertions on tables based on different validation targets.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
//todo check spring somehow as the others to add allure
@NoArgsConstructor
public class UiTableValidatorImpl implements UiTableValidator {

    /**
     * Logs the assertion target details for debugging purposes.
     *
     * @param data The assertion target data to be logged.
     */
    protected void printAssertionTarget(Map<String, Object> data) {
        LogUI.extended("Validation target: [{}]", data.toString());
    }

    /**
     * Validates the given table object against a set of assertions.
     * <p>
     * Extracts data based on the assertion target (row values, row elements, table values, or table elements),
     * logs the validation target, and executes assertions on the extracted data.
     * </p>
     *
     * @param object     The table object to validate.
     * @param assertions The assertions to apply.
     * @param <T>        The expected type of assertion results.
     * @return A list of assertion results indicating whether the validation passed or failed.
     */
    @Override
    public <T> List<AssertionResult<T>> validateTable(final Object object, final Assertion<?>... assertions) {
        LogUI.info("Starting response validation with {} assertion(s).", assertions.length);

        Map<String, T> data = new HashMap<>();

        for (Assertion<?> assertion : assertions) {
            switch ((UiTablesAssertionTarget) assertion.getTarget()) {
                case ROW_VALUES -> {
                    data.put("rowValues", (T) getRowValues(object));
                    assertion.setKey("rowValues");
                }
                case ROW_ELEMENTS -> {
                    data.put("rowElements", (T) getRowElements(object));
                    assertion.setKey("rowElements");
                }
                case TABLE_VALUES -> {
                    List<?> rows = (List<?>) object;
                    List<List<String>> rowList = rows.stream().map(UiTableValidatorImpl::getRowValues).toList();
                    data.put("tableValues", (T) rowList);
                    assertion.setKey("tableValues");
                }
                case TABLE_ELEMENTS -> {
                    List<?> rows = (List<?>) object;
                    List<List<SmartWebElement>> rowList = rows.stream().map(UiTableValidatorImpl::getRowElements).toList();
                    data.put("tableElements", (T) rowList);
                    assertion.setKey("tableElements");
                }
            }
        }

        printAssertionTarget((Map<String, Object>) data);
        return AssertionUtil.validate(data, assertions);
    }

    /**
     * Extracts the text values of all table cells within a row.
     *
     * @param row The row object containing table cells.
     * @return A list of extracted string values from the row.
     */
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

    /**
     * Extracts the web elements of all table cells within a row.
     *
     * @param row The row object containing table cells.
     * @return A list of extracted {@link SmartWebElement} instances.
     */
    private static List<SmartWebElement> getRowElements(Object row) {
        return Arrays.stream(row.getClass().getDeclaredFields())
                .filter(field -> TableCell.class.isAssignableFrom(field.getType()) || isListOfTableCell(field))
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(row);
                        if (value instanceof TableCell cell) {
                            return Collections.singletonList(cell.getElement());
                        } else if (value instanceof List<?> list) {
                            List<SmartWebElement> result = list.stream()
                                    .filter(TableCell.class::isInstance)
                                    .map(TableCell.class::cast)
                                    .map(TableCell::getElement)
                                    .collect(Collectors.toList());
                            return result;
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to access field value", e);
                    }
                    return Collections.<SmartWebElement>emptyList();
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Determines if a given field is a list of {@link TableCell} instances.
     *
     * @param field The field to check.
     * @return {@code true} if the field is a list of TableCell instances, otherwise {@code false}.
     */
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
