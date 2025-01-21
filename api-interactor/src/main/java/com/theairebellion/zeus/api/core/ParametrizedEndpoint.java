package com.theairebellion.zeus.api.core;

import com.theairebellion.zeus.api.log.LogAPI;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParametrizedEndpoint implements Endpoint {

    private final Endpoint original;
    private final Map<String, Object> pathParams;
    private final Map<String, Object> queryParams;
    private final Map<String, List<String>> additionalHeaders;


    ParametrizedEndpoint(Endpoint original) {
        this(original, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }


    private ParametrizedEndpoint(Endpoint original, Map<String, Object> pathParams, Map<String, Object> queryParams,
                                 Map<String, List<String>> additionalHeaders) {
        this.original = original;
        this.pathParams = Collections.unmodifiableMap(new HashMap<>(pathParams));
        this.queryParams = Collections.unmodifiableMap(new HashMap<>(queryParams));
        this.additionalHeaders = Collections.unmodifiableMap(new HashMap<>(additionalHeaders));
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
    public String baseUrl() {
        return original.baseUrl();
    }


    @Override
    public Map<String, List<String>> headers() {
        Map<String, List<String>> mergedHeaders = new HashMap<>(original.headers());
        additionalHeaders.forEach(
            (key, value) -> mergedHeaders.computeIfAbsent(key, k -> new ArrayList<>()).addAll(value));
        return Collections.unmodifiableMap(mergedHeaders);
    }


    @Override
    public RequestSpecification prepareRequestSpec(Object body) {
        RequestSpecification spec = original.prepareRequestSpec(body);
        spec.pathParams(pathParams);
        spec.queryParams(queryParams);
        headers().forEach((key, values) -> spec.header(key, String.join(",", values)));
        return spec;
    }


    public ParametrizedEndpoint withQueryParam(String key, Object value) {
        validateParam(key, value);
        Map<String, Object> newQueryParams = new HashMap<>(this.queryParams);
        newQueryParams.put(key, value);
        return new ParametrizedEndpoint(this.original, this.pathParams, newQueryParams, this.additionalHeaders);
    }


    public ParametrizedEndpoint withPathParam(String key, Object value) {
        validateParam(key, value);
        Map<String, Object> newPathParams = new HashMap<>(this.pathParams);
        newPathParams.put(key, value);
        return new ParametrizedEndpoint(this.original, newPathParams, this.queryParams, this.additionalHeaders);
    }


    public ParametrizedEndpoint withHeader(String key, String value) {
        validateParam(key, value);
        Map<String, List<String>> newHeaders = new HashMap<>(this.additionalHeaders);
        newHeaders.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        return new ParametrizedEndpoint(this.original, this.pathParams, this.queryParams, newHeaders);
    }


    public ParametrizedEndpoint withHeader(String key, List<String> values) {
        validateParam(key, values);
        Map<String, List<String>> newHeaders = new HashMap<>(this.additionalHeaders);
        newHeaders.computeIfAbsent(key, k -> new ArrayList<>()).addAll(values);
        return new ParametrizedEndpoint(this.original, this.pathParams, this.queryParams, newHeaders);
    }


    private void validateParam(String key, Object value) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Header key must not be null or empty");
        }
        if (value == null) {
            throw new IllegalArgumentException("Header value must not be null for key: " + key);
        }
        if (value instanceof Collection && ((Collection<?>) value).isEmpty()) {
            throw new IllegalArgumentException("Header value list must not be empty for key: " + key);
        }
    }

}

