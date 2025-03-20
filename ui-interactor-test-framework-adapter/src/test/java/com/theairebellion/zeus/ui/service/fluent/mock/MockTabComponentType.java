package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.tab.TabComponentType;

public enum MockTabComponentType implements TabComponentType {
    DUMMY;

    @Override
    public Enum<?> getType() {
        return this;
    }
}