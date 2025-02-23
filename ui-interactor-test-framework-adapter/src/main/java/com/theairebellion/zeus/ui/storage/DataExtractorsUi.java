package com.theairebellion.zeus.ui.storage;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.DataExtractor;
import com.theairebellion.zeus.framework.storage.DataExtractorImpl;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.extensions.StorageKeysUi;
import net.minidev.json.JSONArray;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.RESPONSES;
import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class DataExtractorsUi {

    private static final Pattern FOR_LOOP_PATTERN = Pattern.compile("^for\\(;;\\);");


    public static <T> DataExtractor<T> responseBodyExtraction(String responsePrefix, String jsonPath) {
        return responseBodyExtraction(responsePrefix, jsonPath, 1);
    }


    public static <T> DataExtractor<T> responseBodyExtraction(String responsePrefix, String jsonPath, int index) {
        return new DataExtractorImpl<>(
                StorageKeysUi.UI,
                StorageKeysUi.RESPONSES,
                raw -> {
                    List<ApiResponse> responses = (List<ApiResponse>) raw;
                    List<ApiResponse> filteredResponses = responses.stream()
                            .filter(
                                    response -> response.getUrl().contains(responsePrefix))
                            .toList();

                    int adjustedIndex = filteredResponses.size() - index;
                    if (adjustedIndex < 0 || adjustedIndex >= filteredResponses.size()) {
                        throw new IllegalArgumentException("Invalid index for response list.");
                    }

                    for (ApiResponse filteredResponse : filteredResponses) {
                        String jsonBody = removeForLoopPrefix(filteredResponse.getBody());
                        try {
                            Object result = JsonPath.read(jsonBody, jsonPath);
                            if (result instanceof List<?> list && list.isEmpty()) {
                                continue;
                            }
                            return (T) result;
                        } catch (PathNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
        );
    }


    /*public static <T> DataExtractor<T> responseBodyExtraction(String responsePrefix, String jsonPath, int index) {
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
    }*/


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


    private static String removeForLoopPrefix(String body) {
        if (body.startsWith("for(;;);")) {
            return FOR_LOOP_PATTERN.matcher(body).replaceFirst("");
        }
        return body;
    }

}
