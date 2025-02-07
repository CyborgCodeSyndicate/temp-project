package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.validator.core.AssertionTarget;

public enum UiTablesAssertionTarget implements AssertionTarget {
    ROW,
    TABLE;


    @Override
    public Enum<?> target() {
        return this;
    }
}
