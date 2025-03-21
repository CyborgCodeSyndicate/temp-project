package com.theairebellion.zeus.api.storage;

import com.theairebellion.zeus.framework.storage.DataExtractor;
import com.theairebellion.zeus.framework.storage.DataExtractorImpl;
import io.restassured.response.Response;

/**
 * Provides utility methods for extracting data from API responses.
 * <p>
 * This class defines reusable {@link DataExtractor} implementations for extracting
 * response body values and HTTP status codes.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class DataExtractorsApi {

    /**
     * Creates a {@link DataExtractor} to extract a value from a response body using a JSON path.
     *
     * @param key      The storage key associated with the extraction.
     * @param jsonPath The JSON path expression to locate the value in the response body.
     * @param <T>      The type of the extracted data.
     * @return A {@code DataExtractor} configured for response body extraction.
     */
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

    /**
     * Creates a {@link DataExtractor} to extract the HTTP status code from a response.
     *
     * @param key The storage key associated with the extraction.
     * @return A {@code DataExtractor} configured for status code extraction.
     */
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
