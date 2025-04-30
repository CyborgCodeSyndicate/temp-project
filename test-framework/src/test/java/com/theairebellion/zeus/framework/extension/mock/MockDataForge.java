package com.theairebellion.zeus.framework.extension.mock;

import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;

public class MockDataForge implements DataForge<MockEnum> {

    private final Late<Object> late;

    public MockDataForge(Late<Object> late) {
        this.late = late;
    }

    @Override
    public Late<Object> dataCreator() {
        return late;
    }

    @Override
    public MockEnum enumImpl() {
        return MockEnum.VALUE;
    }
}
