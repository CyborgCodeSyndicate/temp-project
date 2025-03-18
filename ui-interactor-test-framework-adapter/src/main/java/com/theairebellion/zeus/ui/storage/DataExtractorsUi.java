package com.theairebellion.zeus.ui.storage;

import com.jayway.jsonpath.JsonPath;
import com.theairebellion.zeus.framework.storage.DataExtractor;
import com.theairebellion.zeus.framework.storage.DataExtractorImpl;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.extensions.StorageKeysUi;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class providing data extraction mechanisms for UI-related data storage and retrieval.
 *
 * <p>This class contains static methods for extracting data from API responses and table elements.
 * It provides functionalities to:
 * <ul>
 *   <li>Extract JSON data from stored API responses.</li>
 *   <li>Retrieve specific table rows based on search criteria or index.</li>
 *   <li>Process and filter data for validation and assertions.</li>
 * </ul>
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class DataExtractorsUi {

    /**
     * Creates a {@link DataExtractor} to retrieve a value from a stored API response body using a JSONPath expression.
     *
     * @param responsePrefix The prefix of the API response URL used to filter relevant responses.
     * @param jsonPath       The JSONPath expression used to extract data from the response body.
     * @param <T>            The expected return type of the extracted data.
     * @return A {@link DataExtractor} that retrieves the specified value from the API response.
     */
    public static <T> DataExtractor<T> responseBodyExtraction(String responsePrefix, String jsonPath) {
        return responseBodyExtraction(responsePrefix, jsonPath, 1);
    }

    /**
     * Creates a {@link DataExtractor} to retrieve a value from a stored API response body using a JSONPath expression.
     *
     * @param responsePrefix The prefix of the API response URL used to filter relevant responses.
     * @param jsonPath       The JSONPath expression used to extract data from the response body.
     * @param index          The index (from the most recent) of the filtered API responses to extract data from.
     * @param <T>            The expected return type of the extracted data.
     * @return A {@link DataExtractor} that retrieves the specified value from the API response.
     * @throws IllegalArgumentException If the provided index is out of range.
     */
    public static <T> DataExtractor<T> responseBodyExtraction(String responsePrefix, String jsonPath, int index) {
        return new DataExtractorImpl<>(
                StorageKeysUi.UI,
                StorageKeysUi.RESPONSES,
                raw -> {
                    List<ApiResponse> responses = (List<ApiResponse>) raw;

                    List<ApiResponse> filteredResponses = responses.stream()
                            .filter(
                                    response -> response.getUrl().startsWith(responsePrefix))
                            .toList();

                    int adjustedIndex = filteredResponses.size() - index;
                    if (adjustedIndex < 0 || adjustedIndex >= filteredResponses.size()) {
                        throw new IllegalArgumentException("Invalid index for response list.");
                    }

                    ApiResponse selectedResponse = filteredResponses.get(adjustedIndex);

                    return JsonPath.read(selectedResponse.getBody(), jsonPath);
                }
        );
    }

    /**
     * Creates a {@link DataExtractor} to retrieve a specific table row from storage based on search criteria.
     *
     * <p>This method searches stored table data for a row containing all specified indicator values.
     * It compares row values case-insensitively after trimming whitespace.</p>
     *
     * @param key        The key used to retrieve the stored table data.
     * @param indicators The values used to search for a matching row.
     * @param <T>        The expected return type of the extracted table row.
     * @return A {@link DataExtractor} that retrieves the matching row from the table.
     */
    public static <T> DataExtractor<T> tableRowExtractor(Enum<?> key, String... indicators) {
        return new DataExtractorImpl<>(
                StorageKeysUi.UI,
                key,
                raw -> {
                    List<T> rows = new ArrayList<>();
                    if (List.class.isAssignableFrom(raw.getClass())) {
                        rows = (List<T>) raw;
                    } else {
                        rows.add((T) raw);
                    }

                    return rows.stream().filter(row -> {
                        List<String> rowValues = getRowValues(row);
                        List<String> modifiedRowValues = lowerCaseAndTrimListOfStrings(rowValues);
                        List<String> modifiedIndicators = lowerCaseAndTrimListOfStrings(List.of(indicators));
                        return new HashSet<>(modifiedRowValues).containsAll(modifiedIndicators);
                    }).findFirst().orElse(null);
                }
        );
    }

    /**
     * Creates a {@link DataExtractor} to retrieve a specific table row from storage based on its index.
     *
     * @param key   The key used to retrieve the stored table data.
     * @param index The zero-based index of the row to retrieve.
     * @param <T>   The expected return type of the extracted table row.
     * @return A {@link DataExtractor} that retrieves the row at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public static <T> DataExtractor<T> tableRowExtractor(Enum<?> key, int index) {
        return new DataExtractorImpl<>(
                StorageKeysUi.UI,
                key,
                raw -> {
                    List<T> rows = new ArrayList<>();
                    if (List.class.isAssignableFrom(raw.getClass())) {
                        rows = (List<T>) raw;
                    } else {
                        rows.add((T) raw);
                    }

                    return rows.get(index);
                }
        );
    }

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
