package com.theairebellion.zeus.api.retry;

import com.theairebellion.zeus.api.core.Endpoint;
import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.framework.retry.RetryCondition;
import com.theairebellion.zeus.framework.retry.RetryConditionImpl;
import io.restassured.response.Response;

import java.util.Objects;

public class RetryConditionApi {

    public static RetryCondition<Integer> statusEquals(Endpoint endpoint, int status) {
        return new RetryConditionImpl<>(
            fluentService -> {
                RestService restService = (RestService) fluentService;
                Response response = restService.request(endpoint);
                return response.getStatusCode();
            }, responseStatus -> responseStatus == status
        );
    }


    public static RetryCondition<Integer> statusEquals(Endpoint endpoint, Object body, int status) {
        return new RetryConditionImpl<>(
            fluentService -> {
                RestService restService = (RestService) fluentService;
                Response response = restService.request(endpoint, body);
                return response.getStatusCode();
            }, responseStatus -> responseStatus == status
        );
    }


    public static RetryCondition<Object> responseFieldEqualsTo(Endpoint endpoint, String jsonPath, Object obj) {
        return new RetryConditionImpl<>(
            service -> {
                RestService restService = (RestService) service;
                Response response = restService.request(endpoint);
                return response.getBody().jsonPath().get(jsonPath);
            }, field -> field.equals(obj)
        );
    }


    public static RetryCondition<Object> responseFieldEqualsTo(Endpoint endpoint, Object body, String jsonPath,
                                                               Object obj) {
        return new RetryConditionImpl<>(
            fluentService -> {
                RestService restService = (RestService) fluentService;
                Response response = restService.request(endpoint, body);
                return response.getBody().jsonPath().get(jsonPath);
            }, field -> field.equals(obj)
        );
    }


    public static RetryCondition<Object> responseFieldNonNull(Endpoint endpoint, String jsonPath) {
        return new RetryConditionImpl<>(
            service -> {
                RestService restService = (RestService) service;
                Response response = restService.request(endpoint);
                return response.getBody().jsonPath().get(jsonPath);
            }, Objects::nonNull
        );
    }


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
