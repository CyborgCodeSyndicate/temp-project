package com.theairebellion.zeus.ui.components.factory.mock;

import com.theairebellion.zeus.ui.components.input.InputComponentType;

public enum MockInputComponentType implements InputComponentType {
    DUMMY,
    FAIL,
    NON_EXISTENT;

    @Override
    public Enum<?> getType() {
        return this;
    }
}