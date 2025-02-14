package com.theairebellion.zeus.framework.parameters.mock;

import com.theairebellion.zeus.framework.parameters.DataRipper;
import com.theairebellion.zeus.framework.quest.SuperQuest;

import java.util.function.Consumer;

public class MockDataRipper implements DataRipper {

    private final Consumer<SuperQuest> consumer;

    public MockDataRipper(Consumer<SuperQuest> consumer) {
        this.consumer = consumer;
    }

    @Override
    public Consumer<SuperQuest> eliminate() {
        return consumer;
    }

    @Override
    public Enum<?> enumImpl() {
        return MockEnum.RIPPED;
    }
}
