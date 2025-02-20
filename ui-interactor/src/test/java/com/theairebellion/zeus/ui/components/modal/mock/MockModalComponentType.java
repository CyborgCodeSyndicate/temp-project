package com.theairebellion.zeus.ui.components.modal.mock;

import com.theairebellion.zeus.ui.components.modal.ModalComponentType;

public enum MockModalComponentType implements ModalComponentType {
    DUMMY;

    @Override
    public Enum<?> getType() {
        return this;
    }
}
