package com.theairebellion.zeus.ui.components.table.annotations.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;

public enum MockComponentType implements ComponentType {
    DUMMY, FAIL, NON_EXISTENT;
    @Override
    public Enum<?> getType() {
        return this;
    }
}
