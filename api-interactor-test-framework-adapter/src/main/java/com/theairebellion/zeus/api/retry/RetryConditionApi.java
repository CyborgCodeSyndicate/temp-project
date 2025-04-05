package com.theairebellion.zeus.api.retry;

import com.theairebellion.zeus.api.core.Endpoint;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.framework.retry.RetryConditionImpl;
import io.restassured.response.Response;

import java.util.Objects;

/**
 * Provides predefined retry conditions for API requests.
 * <p>
 * This utility class defines common conditions for retrying requests,
 * including status code checks and JSON response field validations.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class RetryConditionApi {

    /**
     * Creates a retry condition that checks if the response status matches the expected value.
     *
     * @param endpoint The API endpoint.
     * @param status   The expected status code.
     * @return A {@link RetryCondition} that retries until the expected status code is received.
     */
    public static RetryCondition<Integer> statusEquals(Endpoint endpoint, int status) {
        return new RetryConditionImpl<>(
                service -> {
                    RestService restService = (RestService) service;
                    Response response = restService.request(endpoint);
                    return response.getStatusCode();
                }, responseStatus -> responseStatus == status
        );
    }

    /**
     * Creates a retry condition that checks if the response status matches the expected value.
     *
     * @param endpoint The API endpoint.
     * @param body     The request body.
     * @param status   The expected status code.
     * @return A {@link RetryCondition} that retries until the expected status code is received.
     */
    public static RetryCondition<Integer> statusEquals(Endpoint endpoint, Object body, int status) {
        return new RetryConditionImpl<>(
                service -> {
                    RestService restService = (RestService) service;
                    Response response = restService.request(endpoint, body);
                    return response.getStatusCode();
                }, responseStatus -> responseStatus == status
        );
    }

    /**
     * Creates a retry condition that checks if a specific JSON response field equals the expected value.
     *
     * @param endpoint The API endpoint.
     * @param jsonPath The JSON path to extract the field.
     * @param obj      The expected value.
     * @return A {@link RetryCondition} that retries until the field value matches the expected value.
     */
    public static RetryCondition<Object> responseFieldEqualsTo(Endpoint endpoint, String jsonPath, Object obj) {
        return new RetryConditionImpl<>(
                service -> {
                    RestService restService = (RestService) service;
                    Response response = restService.request(endpoint);
                    return response.getBody().jsonPath().get(jsonPath);
                }, field -> field.equals(obj)
        );
    }

    /**
     * Creates a retry condition that checks if a specific JSON response field equals the expected value.
     *
     * @param endpoint The API endpoint.
     * @param body     The request body.
     * @param jsonPath The JSON path to extract the field.
     * @param obj      The expected value.
     * @return A {@link RetryCondition} that retries until the field value matches the expected value.
     */
    public static RetryCondition<Object> responseFieldEqualsTo(Endpoint endpoint, Object body, String jsonPath,
                                                               Object obj) {
        return new RetryConditionImpl<>(
                service -> {
                    RestService restService = (RestService) service;
                    Response response = restService.request(endpoint, body);
                    return response.getBody().jsonPath().get(jsonPath);
                }, field -> field.equals(obj)
        );
    }

    /**
     * Creates a retry condition that checks if a specific JSON response field is non-null.
     *
     * @param endpoint The API endpoint.
     * @param jsonPath The JSON path to extract the field.
     * @return A {@link RetryCondition} that retries until the field contains a non-null value.
     */
    public static RetryCondition<Object> responseFieldNonNull(Endpoint endpoint, String jsonPath) {
        return new RetryConditionImpl<>(
                service -> {
                    RestService restService = (RestService) service;
                    Response response = restService.request(endpoint);
                    return response.getBody().jsonPath().get(jsonPath);
                }, Objects::nonNull
        );
    }

    /**
     * Creates a retry condition that checks if a specific JSON response field is non-null.
     *
     * @param endpoint The API endpoint.
     * @param body     The request body.
     * @param jsonPath The JSON path to extract the field.
     * @return A {@link RetryCondition} that retries until the field contains a non-null value.
     */
    public static RetryCondition<Object> responseFieldNonNull(Endpoint endpoint, Object body, String jsonPath) {
        return new RetryConditionImpl<>(
                service -> {
                    RestService restService = (RestService) service;
                    Response response = restService.request(endpoint, body);
                    return response.getBody().jsonPath().get(jsonPath);
                }, Objects::nonNull
        );
    }

}
