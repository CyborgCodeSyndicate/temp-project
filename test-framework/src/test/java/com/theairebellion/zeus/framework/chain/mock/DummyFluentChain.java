package com.theairebellion.zeus.framework.chain.mock;

import com.theairebellion.zeus.framework.chain.FluentChain;
import com.theairebellion.zeus.framework.quest.Quest;

public class DummyFluentChain implements FluentChain {

    private final DummyQuest dummyQuest = new DummyQuest();
    private final DummySuperQuest dummySuperQuest = new DummySuperQuest(dummyQuest);

    @Override
    public Quest then() {
        return dummySuperQuest;
    }
}
