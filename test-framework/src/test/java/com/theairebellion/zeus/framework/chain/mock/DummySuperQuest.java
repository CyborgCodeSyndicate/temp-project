package com.theairebellion.zeus.framework.chain.mock;

import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;

import java.util.concurrent.atomic.AtomicBoolean;

public class DummySuperQuest extends SuperQuest {

    private final AtomicBoolean completeCalled = new AtomicBoolean(false);

    public DummySuperQuest(Quest quest) {
        super(quest);
    }

    @Override
    public void complete() {
        completeCalled.set(true);
    }

    public boolean isCompleteCalled() {
        return completeCalled.get();
    }
}
