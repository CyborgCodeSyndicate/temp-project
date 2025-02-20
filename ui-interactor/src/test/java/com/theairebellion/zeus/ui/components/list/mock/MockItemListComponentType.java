package com.theairebellion.zeus.ui.components.list.mock;

import com.theairebellion.zeus.ui.components.list.ItemListComponentType;

public enum MockItemListComponentType implements ItemListComponentType {
    DUMMY;

    @Override
    public Enum<?> getType() {
        return this;
    }
}
