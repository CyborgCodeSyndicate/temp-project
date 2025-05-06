package com.bakery.project.data.extractions;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.theairebellion.zeus.framework.storage.DataExtractor;
import com.theairebellion.zeus.framework.storage.DataExtractorImpl;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomDataExtractor {

    private CustomDataExtractor() {
    }

    public static <T> DataExtractor<T> responseBodyExtraction(String responsePrefix, String jsonPath, String jsonPrefix) {
        return new DataExtractorImpl<>(
                StorageKeysUi.UI,
                StorageKeysUi.RESPONSES,
                raw -> {
                    List<ApiResponse> responses = (List<ApiResponse>) raw;
                    List<ApiResponse> filteredResponses = responses.stream()
                            .filter(
                                    response -> response.getUrl().contains(responsePrefix))
                            .toList();

                    for (ApiResponse filteredResponse : filteredResponses) {
                        String jsonBody = removeJsonPrefix(filteredResponse.getBody(), jsonPrefix);
                        try {
                            Object result = JsonPath.read(jsonBody, jsonPath);
                            if (result instanceof List<?> list && list.isEmpty()) {
                                continue;
                            }
                            return (T) result;
                        } catch (PathNotFoundException ignored) {
                            //cant extract body
                        }
                    }
                    return null;
                }
        );
    }

    private static String removeJsonPrefix(String body, String jsonPrefix) {
        Pattern dynamicPrefixPattern = Pattern.compile("^" + Pattern.quote(jsonPrefix));
        Matcher matcher = dynamicPrefixPattern.matcher(body);
        if (matcher.find()) {
            return matcher.replaceFirst("");
        }
        return body;
    }
}
