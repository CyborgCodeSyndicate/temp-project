package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.validator.core.AssertionType;

import java.util.List;

public enum TableAssertionTypes implements AssertionType {
    ALL_FIELD_VISIBLE(Object.class),
    VALUES_PRESENT_IN_ALL_ROWS(List.class)
    ;



    private final Class<?> supportedType;


    <T> TableAssertionTypes(Class<T> supportedType) {
        this.supportedType = supportedType;
    }


    @Override
    public Enum<?> type() {
        return this;
    }


    @Override
    public Class<?> getSupportedType() {
        return supportedType;
    }
    }
