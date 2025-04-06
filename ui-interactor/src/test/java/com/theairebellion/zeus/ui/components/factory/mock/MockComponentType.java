package com.theairebellion.zeus.ui.components.factory.mock;

public enum MockComponentType implements MockInterface {
    DUMMY, FAIL, NON_EXISTENT;

    @Override
    public Enum<?> getType() {
        return this;
    }
}
