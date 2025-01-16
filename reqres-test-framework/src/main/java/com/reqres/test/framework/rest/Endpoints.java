package com.reqres.test.framework.rest;

import com.theairebellion.zeus.api.core.Endpoint;
import io.restassured.http.Method;

import java.util.List;
import java.util.Map;

public enum Endpoints implements Endpoint {
    GET_ALL_USERS(Method.GET, "/users?{page}"),
    GET_USER(Method.GET, "/users/{id}");

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
    public Map<String, List<String>> headers() {
        return Endpoint.super.headers();
    }
}
