package com.theairebellion.zeus.api.validator;

import com.theairebellion.zeus.validator.core.AssertionTarget;

public enum RestAssertionTarget implements AssertionTarget {
    STATUS,
    BODY,
    HEADER;


    @Override
    public Enum<?> target() {
        return this;
    }
}
