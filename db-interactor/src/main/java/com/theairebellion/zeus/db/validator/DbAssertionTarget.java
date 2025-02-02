package com.theairebellion.zeus.db.validator;

import com.theairebellion.zeus.validator.core.AssertionTarget;

public enum DbAssertionTarget implements AssertionTarget {

    QUERY_RESULT,
    NUMBER_ROWS,
    COLUMNS;


    @Override
    public Enum<?> target() {
        return this;
    }
}
