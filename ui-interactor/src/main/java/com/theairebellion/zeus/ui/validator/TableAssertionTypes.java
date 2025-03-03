package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.validator.core.AssertionType;

import java.util.List;

public enum TableAssertionTypes implements AssertionType {

    TABLE_NOT_EMPTY(List.class),
    TABLE_ROW_COUNT(List.class),
    EVERY_ROW_CONTAINS_VALUES(List.class),
    TABLE_DOES_NOT_CONTAIN_ROW(List.class),
    ALL_ROWS_ARE_UNIQUE(List.class),
    NO_EMPTY_CELLS(List.class),
    COLUMN_VALUES_ARE_UNIQUE(List.class),
    TABLE_DATA_MATCHES_EXPECTED(List.class),
    ROW_NOT_EMPTY(List.class),
    ROW_CONTAINS_VALUES(List.class),
    ALL_CELLS_ENABLED(List.class),
    ALL_CELLS_CLICKABLE(List.class);

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
