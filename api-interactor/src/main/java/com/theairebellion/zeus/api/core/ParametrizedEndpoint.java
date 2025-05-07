package com.theairebellion.zeus.api.core;

import com.theairebellion.zeus.api.log.LogApi;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;

import java.util.*;

/**
 * A wrapper for {@link Endpoint} that allows adding query parameters, path parameters, and headers dynamically.
 * <p>
 * This class enables the creation of new endpoint instances with modified parameters while maintaining immutability.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class ParametrizedEndpoint<T extends Enum<T>> implements Endpoint<T> {

    private final Endpoint<T> original;
    private final Map<String, Object> pathParams;
    private final Map<String, Object> queryParams;
    private final Map<String, List<String>> additionalHeaders;

    /**
     * Constructs a new {@code ParametrizedEndpoint} with no additional parameters.
     *
     * @param original The original endpoint being wrapped.
     */
    ParametrizedEndpoint(Endpoint<T> original) {
        this(original, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    private ParametrizedEndpoint(Endpoint<T> original, Map<String, Object> pathParams, Map<String, Object> queryParams,
                                 Map<String, List<String>> additionalHeaders) {
        this.original = original;
        this.pathParams = Collections.unmodifiableMap(new HashMap<>(pathParams));
        this.queryParams = Collections.unmodifiableMap(new HashMap<>(queryParams));
        this.additionalHeaders = Collections.unmodifiableMap(new HashMap<>(additionalHeaders));
    }

    /**
     * Retrieves the HTTP method of the endpoint.
     *
     * @return The HTTP method.
     */
    @Override
    public Method method() {
        return original.method();
    }

    /**
     * Retrieves the URL path of the endpoint.
     *
     * @return The endpoint URL.
     */
    @Override
    public String url() {
        return original.url();
    }

    /**
     * Retrieves the enum representation of the endpoint.
     *
     * @return The enum representing this endpoint.
     */
    @Override
    public T enumImpl() {
        return original.enumImpl();
    }

    /**
     * Retrieves the base URL from the API configuration.
     *
     * @return The base URL.
     */
    @Override
    public String baseUrl() {
        return original.baseUrl();
    }

    /**
     * Retrieves the headers, including any additional headers added dynamically.
     *
     * @return A map containing merged headers.
     */
    @Override
    public Map<String, List<String>> headers() {
        Map<String, List<String>> mergedHeaders = new HashMap<>(original.headers());
        additionalHeaders.forEach(
                (key, value) -> mergedHeaders.computeIfAbsent(key, k -> new ArrayList<>()).addAll(value));
        return Collections.unmodifiableMap(mergedHeaders);
    }

    /**
     * Prepares a {@link RequestSpecification} with path parameters, query parameters, and headers.
     *
     * @param body The request body (optional).
     * @return The configured {@link RequestSpecification} instance.
     */
    @Override
    public RequestSpecification prepareRequestSpec(Object body) {
        RequestSpecification spec = original.prepareRequestSpec(body);
        spec.pathParams(pathParams);
        spec.queryParams(queryParams);
        headers().forEach((key, values) -> spec.header(key, String.join(",", values)));

        LogApi.info("Prepared RequestSpecification with pathParams: {}, queryParams: {}, headers: {}",
                pathParams, queryParams, headers());

        return spec;
    }

    /**
     * Creates a new {@code ParametrizedEndpoint} with an added query parameter.
     *
     * @param key   The query parameter name.
     * @param value The query parameter value.
     * @return A new instance with the query parameter added.
     * @throws IllegalArgumentException if the key or value is invalid.
     */
    @Override
    public ParametrizedEndpoint<T> withQueryParam(String key, Object value) {
        validateParam(key, value);
        Map<String, Object> newQueryParams = new HashMap<>(this.queryParams);
        newQueryParams.put(key, value);
        return new ParametrizedEndpoint<>(this.original, this.pathParams, newQueryParams, this.additionalHeaders);
    }

    /**
     * Creates a new {@code ParametrizedEndpoint} with an added path parameter.
     *
     * @param key   The path parameter name.
     * @param value The path parameter value.
     * @return A new instance with the path parameter added.
     * @throws IllegalArgumentException if the key or value is invalid.
     */
    @Override
    public ParametrizedEndpoint<T> withPathParam(String key, Object value) {
        validateParam(key, value);
        Map<String, Object> newPathParams = new HashMap<>(this.pathParams);
        newPathParams.put(key, value);
        return new ParametrizedEndpoint<>(this.original, newPathParams, this.queryParams, this.additionalHeaders);
    }

    /**
     * Creates a new {@code ParametrizedEndpoint} with an added request header.
     *
     * @param key   The header name.
     * @param value The header value.
     * @return A new instance with the header added.
     * @throws IllegalArgumentException if the key or value is invalid.
     */
    @Override
    public ParametrizedEndpoint<T> withHeader(String key, String value) {
        validateParam(key, value);
        Map<String, List<String>> newHeaders = new HashMap<>(this.additionalHeaders);
        newHeaders.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        return new ParametrizedEndpoint<>(this.original, this.pathParams, this.queryParams, newHeaders);
    }

    /**
     * Creates a new {@code ParametrizedEndpoint} with a multi-value request header.
     *
     * @param key    The header name.
     * @param values A list of values for the header.
     * @return A new instance with the header added.
     * @throws IllegalArgumentException if the key or values are invalid.
     */
    @Override
    public ParametrizedEndpoint<T> withHeader(String key, List<String> values) {
        validateParam(key, values);
        Map<String, List<String>> newHeaders = new HashMap<>(this.additionalHeaders);
        newHeaders.computeIfAbsent(key, k -> new ArrayList<>()).addAll(values);
        return new ParametrizedEndpoint<>(this.original, this.pathParams, this.queryParams, newHeaders);
    }

    /**
     * Validates a parameter key and value.
     *
     * @param key   The parameter key.
     * @param value The parameter value.
     * @throws IllegalArgumentException if the key is null/empty or if the value is invalid.
     */
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
