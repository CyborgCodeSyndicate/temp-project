package com.theairebellion.zeus.framework.parameters.mock;

import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;

public class MockDataForge implements DataForge {

    final Object data;
    final Enum<?> enumValue;

    public MockDataForge(Object data, Enum<?> enumValue) {
        this.data = data;
        this.enumValue = enumValue;
    }

    @Override
    public Late<Object> dataCreator() {
        return () -> data;
    }

    @Override
    public Enum<?> enumImpl() {
        return enumValue;
    }
}
