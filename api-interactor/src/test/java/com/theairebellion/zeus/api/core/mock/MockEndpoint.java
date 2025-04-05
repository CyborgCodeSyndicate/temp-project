package com.theairebellion.zeus.api.core.mock;

import io.restassured.http.Method; /**
 * Concrete implementation of MockTestableEndpoint for testing.
 * Can be configured with different method, URL, and enum values.
 */
public class MockEndpoint extends MockTestableEndpoint {
    private final Method method;
    private final String url;
    private final Enum<?> enumType;
    private final String baseUrlValue;

    /**
     * Creates a new MockEndpoint with the specified values.
     *
     * @param method the HTTP method
     * @param url the endpoint URL
     * @param enumType the enum implementation
     * @param baseUrlValue the base URL
     */
    public MockEndpoint(Method method, String url, Enum<?> enumType, String baseUrlValue) {
        this.method = method;
        this.url = url;
        this.enumType = enumType;
        this.baseUrlValue = baseUrlValue;
    }

    @Override
    public Method method() {
        return method;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public Enum<?> enumImpl() {
        return enumType;
    }

    @Override
    public String baseUrl() {
        return baseUrlValue;
    }
}
