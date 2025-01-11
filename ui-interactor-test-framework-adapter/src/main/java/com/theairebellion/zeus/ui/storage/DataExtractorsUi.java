package com.theairebellion.zeus.ui.storage;

import com.jayway.jsonpath.JsonPath;
import com.theairebellion.zeus.framework.storage.DataExtractor;
import com.theairebellion.zeus.framework.storage.DataExtractorImpl;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.extensions.StorageKeysUi;

import java.util.List;

public class DataExtractorsUi {

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

}
