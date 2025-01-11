package com.theairebellion.zeus.api.storage;

import com.theairebellion.zeus.framework.storage.DataExtractor;
import com.theairebellion.zeus.framework.storage.DataExtractorImpl;
import io.restassured.response.Response;

public class DataExtractorsApi {

    public static <T> DataExtractor<T> responseBodyExtraction(Enum<?> key, String jsonPath) {
        return new DataExtractorImpl<>(
            StorageKeysApi.API,
            key,
            raw -> {
                Response response = (Response) raw;
                return response.body().jsonPath().get(jsonPath);
            }
        );
    }


    public static DataExtractor<Integer> statusExtraction(Enum<?> key) {
        return new DataExtractorImpl<>(
            StorageKeysApi.API,
            key,
            raw -> {
                Response response = (Response) raw;
                return response.statusCode();
            }
        );
    }

}
