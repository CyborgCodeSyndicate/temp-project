package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;

public enum MockCheckboxComponentType implements CheckboxComponentType {
    DUMMY;

    @Override
    public Enum<?> getType() {
        return this;
    }
}