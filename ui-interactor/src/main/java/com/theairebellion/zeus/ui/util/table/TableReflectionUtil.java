package com.theairebellion.zeus.ui.util.table;

import com.theairebellion.zeus.ui.components.table.exceptions.TableException;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for extracting data from UI table row objects using Java reflection.
 * <p>
 * Supports operations like retrieving text values and web elements from fields of type {@link TableCell}
 * or {@code List<TableCell>}. Also includes helpers for checking type compatibility and normalizing string lists.
 * </p>
 */
@SuppressWarnings("java:S3011")
public final class TableReflectionUtil {

    private TableReflectionUtil() {
    }


    /**
     * Extracts all textual content from fields of type {@link TableCell} or {@code List<TableCell>} within a given row object.
     *
     * @param row The row object containing {@link TableCell} fields.
     * @return A list of strings representing the text content of the table cells in the row.
     */
    public static List<String> extractTextsFromRow(Object row) {
        return Arrays.stream(row.getClass().getDeclaredFields())
                .filter(field -> TableCell.class.isAssignableFrom(field.getType()) || isListOfTableCell(field))
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(row);
                        if (value instanceof TableCell cell) {
                            return Collections.singletonList(cell.getText());
                        } else if (value instanceof List<?> list) {
                            return list.stream()
                                    .filter(TableCell.class::isInstance)
                                    .map(TableCell.class::cast)
                                    .map(TableCell::getText)
                                    .toList();
                        }
                    } catch (IllegalAccessException e) {
                        throw new TableException("Failed to access field value", e);
                    }
                    return Collections.<String>emptyList();
                })
                .flatMap(List::stream)
                .toList();
    }


    /**
     * Extracts all {@link SmartWebElement} instances from fields of type {@link TableCell} or {@code List<TableCell>} within a given row object.
     *
     * @param row The row object containing {@link TableCell} fields.
     * @return A list of {@link SmartWebElement} objects extracted from the row.
     */
    public static List<SmartWebElement> extractElementsFromRow(Object row) {
        return Arrays.stream(row.getClass().getDeclaredFields())
                .filter(field -> TableCell.class.isAssignableFrom(field.getType()) || isListOfTableCell(field))
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(row);
                        if (value instanceof TableCell cell) {
                            return Collections.singletonList(cell.getElement());
                        } else if (value instanceof List<?> list) {
                            return list.stream()
                                    .filter(TableCell.class::isInstance)
                                    .map(TableCell.class::cast)
                                    .map(TableCell::getElement)
                                    .toList();
                        }
                    } catch (IllegalAccessException e) {
                        throw new TableException("Failed to access field value", e);
                    }
                    return Collections.<SmartWebElement>emptyList();
                })
                .flatMap(List::stream)
                .toList();
    }


    /**
     * Checks whether a field is a {@code List<TableCell>} type.
     *
     * @param field The field to inspect.
     * @return {@code true} if the field is a parameterized list of {@link TableCell}; {@code false} otherwise.
     */
    public static boolean isListOfTableCell(Field field) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType parameterizedType) {
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            return typeArguments.length == 1 && typeArguments[0] == TableCell.class;
        }
        return false;
    }


    /**
     * Converts a list of strings to lowercase and trims whitespace from each element.
     *
     * @param list The input list of strings.
     * @return A new list with trimmed and lowercased strings.
     */
    public static List<String> lowerCaseAndTrim(List<String> list) {
        return list.stream()
                .map(s -> s.trim().toLowerCase())
                .toList();
    }

}