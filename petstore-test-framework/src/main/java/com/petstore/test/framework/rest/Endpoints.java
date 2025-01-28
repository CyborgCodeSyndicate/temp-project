package com.petstore.test.framework.rest;

import com.theairebellion.zeus.api.core.Endpoint;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;

public enum Endpoints implements Endpoint {
    GET_PET_BY_ID(Method.GET, "/pet/{petId}"),
    GET_PET_BY_STATUS(Method.GET, "/pet/findByStatus?status={status}");

    private final Method method;
    private final String url;

    Endpoints(final Method method, final String url) {
        this.method = method;
        this.url = url;
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
        return this;
    }

    @Override
    public RequestSpecification defaultConfiguration() {
        RequestSpecification spec = Endpoint.super.defaultConfiguration();
        spec.contentType(ContentType.JSON);
        return spec;
    }
}
