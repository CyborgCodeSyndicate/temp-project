package com.theairebellion.zeus.ui.util.strategy;

import com.theairebellion.zeus.ui.components.base.ComponentType;

public enum Strategy implements ComponentType {

    RANDOM,
    FIRST,
    LAST,
    ALL;

    @Override
    public Enum getType() {
        return this;
    }
}