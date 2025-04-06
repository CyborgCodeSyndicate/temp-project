package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.select.SelectComponentType;

public enum MockSelectComponentType implements SelectComponentType {
    DUMMY;

    @Override
    public Enum<?> getType() {
        return this;
    }
}
