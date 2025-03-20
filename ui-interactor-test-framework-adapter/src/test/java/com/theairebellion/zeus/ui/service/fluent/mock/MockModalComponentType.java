package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.modal.ModalComponentType;

public enum MockModalComponentType implements ModalComponentType {
    DUMMY;

    @Override
    public Enum<?> getType() {
        return this;
    }
}
