package com.example.project.preconditions;

import com.theairebellion.zeus.framework.parameters.DataIntercept;
import com.theairebellion.zeus.framework.quest.SuperQuest;

import java.util.function.Consumer;
import java.util.function.Supplier;


public enum BakeryInterceptRequests implements DataIntercept {
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

    @Override
    public Supplier<String> getEndpoint() {
        return endpoint;
    }

    @Override
    public Enum<?> enumImpl() {
        return this;
    }

}
