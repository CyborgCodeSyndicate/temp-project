package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.validator.core.AssertionType;

import java.util.List;

public enum TableAssertionTypes implements AssertionType {

    TABLE_NOT_EMPTY(List.class),
    TABLE_ROW_COUNT(List.class),
    TABLE_COLUMN_COUNT(List.class),
    ALL_ROWS_CONTAIN_VALUES(List.class),
    VALUES_PRESENT_IN_ALL_ROWS(List.class),
    TABLE_CONTAINS_ROW(List.class),
    TABLE_DOES_NOT_CONTAIN_ROW(List.class),
    UNIQUE_ROWS(List.class),
    NO_EMPTY_CELLS(List.class),
    COLUMN_VALUE_UNIQUENESS(List.class),
    TABLE_DATA_MATCHES_EXPECTED(List.class),
    ROW_NOT_EMPTY(List.class),
    ROW_CONTAIN_VALUES(List.class),
    ROW_DATA_MATCHES_EXPECTED(List.class),
    ALL_CELLS_ENABLED(List.class),
    CELL_HAS_ATTRIBUTE(SmartWebElement.class),
    CELL_IS_CLICKABLE(SmartWebElement.class),
    ALL_CELLS_ARE_CLICKABLE(List.class);

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
