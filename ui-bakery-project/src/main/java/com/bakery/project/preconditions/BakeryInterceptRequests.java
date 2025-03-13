package com.bakery.project.preconditions;

import com.theairebellion.zeus.framework.parameters.DataIntercept;

import java.util.function.Supplier;


public enum BakeryInterceptRequests implements DataIntercept {
    INTERCEPT_REQUEST_AUTH(() -> "?v-r=uidl"),
    INTERCEPT_REQUEST_LOGIN(() -> "/login");


    public static final class Data {

        public static final String INTERCEPT_REQUEST_AUTH = "INTERCEPT_REQUEST_AUTH";
        public static final String INTERCEPT_REQUEST_LOGIN = "INTERCEPT_REQUEST_LOGIN";

        private Data() {
        }
    }


    private final Supplier<String> endpoint;

    BakeryInterceptRequests(final Supplier<String> endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public Supplier<String> getEndpoint() {
        return endpoint;
    }

    @Override
    public Enum<?> enumImpl() {
        return this;
    }

}
