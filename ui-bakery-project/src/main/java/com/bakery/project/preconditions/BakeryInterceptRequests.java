package com.bakery.project.preconditions;

import com.theairebellion.zeus.ui.parameters.DataIntercept;


public enum BakeryInterceptRequests implements DataIntercept {
    INTERCEPT_REQUEST_AUTH( "?v-r=uidl"),
    INTERCEPT_REQUEST_LOGIN("/login");


    public static final class Data {

        public static final String INTERCEPT_REQUEST_AUTH = "INTERCEPT_REQUEST_AUTH";
        public static final String INTERCEPT_REQUEST_LOGIN = "INTERCEPT_REQUEST_LOGIN";

        private Data() {
        }
    }


    private final String endpointSubString;

    BakeryInterceptRequests(final String endpointSubString) {
        this.endpointSubString = endpointSubString;
    }

    @Override
    public String getEndpointSubString() {
        return endpointSubString;
    }

    @Override
    public Enum<?> enumImpl() {
        return this;
    }

}
