package com.theairebellion.zeus.ui.components.table.insertion.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;

public enum MockComponentType implements ComponentType {
    DUMMY;

    @Override
    public Enum<?> getType() {
        return this;
    }
}
