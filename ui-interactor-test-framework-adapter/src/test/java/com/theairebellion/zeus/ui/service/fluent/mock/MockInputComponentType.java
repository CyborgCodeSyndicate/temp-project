package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.input.InputComponentType;

public enum MockInputComponentType implements InputComponentType {
    DUMMY;

    @Override
    public Enum<?> getType() {
        return this;
    }
}