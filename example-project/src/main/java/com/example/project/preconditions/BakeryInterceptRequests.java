package com.example.project.preconditions;

import java.util.function.Supplier;


public enum BakeryInterceptRequests {
    INTERCEPT_REQUEST_AUTH(() -> "?v-r=uidl"),
    INTERCEPT_REQUEST_SAVE(() -> "api/save");


    public static final class Data {

        public static final String INTERCEPT_REQUEST_AUTH = "INTERCEPT_REQUEST_AUTH";
        public static final String INTERCEPT_REQUEST_SAVE = "INTERCEPT_REQUEST_SAVE";

        private Data() {
        }
    }


    private final Supplier<String> endpoint;

    BakeryInterceptRequests(final Supplier<String> endpoint) {
        this.endpoint = endpoint;
    }

}
