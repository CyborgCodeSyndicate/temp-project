package com.theairebellion.zeus.framework.chain.mock;

import com.theairebellion.zeus.framework.quest.Quest;

import java.util.concurrent.atomic.AtomicBoolean;

public class DummyQuest extends Quest {

    private final AtomicBoolean completeCalled = new AtomicBoolean(false);

    @Override
    public void complete() {
        completeCalled.set(true);
    }

    public boolean isCompleteCalled() {
        return completeCalled.get();
    }
}
