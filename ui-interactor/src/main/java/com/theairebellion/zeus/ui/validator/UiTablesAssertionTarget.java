package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.validator.core.AssertionTarget;

public enum UiTablesAssertionTarget implements AssertionTarget {
    ROW_VALUES,
    TABLE_VALUES,
    ROW_ELEMENTS,
    TABLE_ELEMENTS;


    @Override
    public Enum<?> target() {
        return this;
    }
}
