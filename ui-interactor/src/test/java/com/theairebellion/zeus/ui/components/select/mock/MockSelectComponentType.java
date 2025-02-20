package com.theairebellion.zeus.ui.components.select.mock;

import com.theairebellion.zeus.ui.components.select.SelectComponentType;

public enum MockSelectComponentType implements SelectComponentType {
    DUMMY;

    @Override
    public Enum<?> getType() {
        return this;
    }
}
