package com.theairebellion.zeus.api.service.fluent;

import com.theairebellion.zeus.api.service.RestService;
import groovy.lang.Delegate;

/**
 * Extends {@link RestServiceFluent} to provide additional capabilities.
 * <p>
 * This class acts as a wrapper around {@code RestServiceFluent}, delegating its behavior
 * while allowing further customization if needed.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class SuperRestServiceFluent extends RestServiceFluent {

    @Delegate
    private final RestServiceFluent original;

    /**
     * Constructs a {@code SuperRestServiceFluent} instance.
     *
     * @param original The original {@link RestServiceFluent} instance to delegate.
     */
    public SuperRestServiceFluent(RestServiceFluent original) {
        super(original.getRestService());
        this.original = original;
    }

    /**
     * Retrieves the underlying {@link RestService} instance.
     *
     * @return The {@code RestService} instance.
     */
    public RestService getRestService() {
        return original.getRestService();
    }
}
