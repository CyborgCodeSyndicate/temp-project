package com.theairebellion.zeus.validator.core;

import java.util.Collection;

public enum AssertionTypes implements AssertionType {
    IS(Object.class),
    NOT(Object.class),
    CONTAINS(String.class),
    NOT_NULL(Object.class),
    ALL_NOT_NULL(Collection.class),
    IS_NULL(Object.class),

    ALL_NULL(Collection.class),
    GREATER_THAN(Number.class),
    LESS_THAN(Number.class),
    CONTAINS_ALL(Collection.class),
    CONTAINS_ANY(Collection.class);


    private final Class<?> supportedType;


    <T> AssertionTypes(Class<T> supportedType) {
        this.supportedType = supportedType;
    }


    @Override
    public Enum type() {
        return this;
    }


    @Override
    public Class getSupportedType() {
        return supportedType;
    }
}
