package com.theairebellion.zeus.api.service.fluent;

import com.theairebellion.zeus.api.service.RestService;
import com.theairebellion.zeus.framework.annotation.AIDisableUsage;
import groovy.lang.Delegate;

@AIDisableUsage
public class SuperRestServiceFluent extends RestServiceFluent {

    @Delegate
    private final RestServiceFluent original;

    public SuperRestServiceFluent(RestServiceFluent original) {
        super(original.getRestService());
        this.original = original;
    }

    public RestService getRestService() {
        return original.getRestService();
    }
}
