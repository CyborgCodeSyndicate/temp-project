package com.theairebellion.zeus.ui.components.toggle.mock;

import com.theairebellion.zeus.ui.components.toggle.ToggleComponentType;

public enum MockToggleComponentType implements ToggleComponentType {
    DUMMY_TOGGLE;

    @Override
    public Enum<?> getType() {
        return this;
    }
}