package com.theairebellion.zeus.ui.components.table.service.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;

public enum MockComponentType implements ComponentType {
    TEST_INSERT, TEST_FILTER;
    @Override
    public Enum<?> getType() {
        return this;
    }
}
