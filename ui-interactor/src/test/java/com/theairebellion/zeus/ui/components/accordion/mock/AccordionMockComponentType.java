package com.theairebellion.zeus.ui.components.accordion.mock;

import com.theairebellion.zeus.ui.components.accordion.AccordionComponentType;

public enum AccordionMockComponentType implements AccordionComponentType {

    DUMMY;
    @Override
    public Enum<?> getType() {
        return this;
    }
}
