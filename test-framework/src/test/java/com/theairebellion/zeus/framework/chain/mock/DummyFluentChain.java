package com.theairebellion.zeus.framework.chain.mock;

import com.theairebellion.zeus.framework.chain.FluentChain;
import com.theairebellion.zeus.framework.quest.Quest;

public class DummyFluentChain implements FluentChain {

    private final DummyQuest quest = new DummyQuest();

    @Override
    public Quest then() {
        return quest;
    }
}
