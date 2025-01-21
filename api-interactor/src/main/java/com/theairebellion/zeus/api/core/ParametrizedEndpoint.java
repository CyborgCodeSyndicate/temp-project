package com.theairebellion.zeus.api.core;

import com.theairebellion.zeus.api.log.LogAPI;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParametrizedEndpoint implements Endpoint {

    private final Endpoint original;
    private final Map<String, Object> pathParams = new HashMap<>();
    private final Map<String, Object> queryParams = new HashMap<>();
    private final Map<String, List<String>> additionalHeaders = new HashMap<>();


    ParametrizedEndpoint(Endpoint original) {
        this.original = original;
    }


    @Override
    public String baseUrl() {
        return original.baseUrl();
    }


    @Override
    public Method method() {
        return original.method();
    }


    @Override
    public String url() {
        return original.url();
    }


    @Override
    public Enum<?> enumImpl() {
        return original.enumImpl();
    }


    @Override
    public Map<String, List<String>> headers() {
        return original.headers();
    }


    @Override
    public RequestSpecification prepareRequestSpec(Object body) {
        RequestSpecification spec = original.prepareRequestSpec(body);

        // Apply path params
        for (Map.Entry<String, Object> entry : pathParams.entrySet()) {
            spec.pathParam(entry.getKey(), entry.getValue());
        }

        // Apply query params
        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            Object val = entry.getValue();
            if (val instanceof Collection) {
                spec.queryParams(entry.getKey(), ((Collection<?>) val).toArray());
            } else {
                spec.queryParam(entry.getKey(), val);
            }
        }

        for (Map.Entry<String, List<String>> entry : additionalHeaders.entrySet()) {
            spec.header(entry.getKey(), entry.getValue());
        }

        LogAPI.info("RequestSpecification prepared with pathParams={}, queryParams={}, headers={}",
                pathParams, queryParams, additionalHeaders);

        return spec;
    }


    public ParametrizedEndpoint withQueryParam(String key, Object value) {
        ParametrizedEndpoint copy = new ParametrizedEndpoint(this.original);
        copy.pathParams.putAll(this.pathParams);
        copy.queryParams.putAll(this.queryParams);
        copy.queryParams.put(key, value);
        return copy;
    }


    public ParametrizedEndpoint withPathParam(String key, Object value) {
        ParametrizedEndpoint copy = new ParametrizedEndpoint(this.original);
        copy.pathParams.putAll(this.pathParams);
        copy.queryParams.putAll(this.queryParams);
        copy.pathParams.put(key, value);
        return copy;
    }


    public ParametrizedEndpoint withHeader(String key, String value) {
        ParametrizedEndpoint copy = new ParametrizedEndpoint(this.original);
        copy.pathParams.putAll(this.pathParams);
        copy.queryParams.putAll(this.queryParams);
        copy.additionalHeaders.putAll(this.additionalHeaders);
        copy.additionalHeaders.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        return copy;
    }


    public ParametrizedEndpoint withHeaders(Map<String, String> headers) {
        ParametrizedEndpoint copy = new ParametrizedEndpoint(this.original);
        copy.pathParams.putAll(this.pathParams);
        copy.queryParams.putAll(this.queryParams);
        copy.additionalHeaders.putAll(this.additionalHeaders);
        headers.forEach((key, value) ->
                            copy.additionalHeaders.computeIfAbsent(key, k -> new ArrayList<>()).add(value)
        );
        return copy;
    }

}

