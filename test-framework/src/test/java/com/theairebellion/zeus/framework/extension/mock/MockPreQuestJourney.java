package com.theairebellion.zeus.framework.extension.mock;

import com.theairebellion.zeus.framework.parameters.PreQuestJourney;
import com.theairebellion.zeus.framework.quest.SuperQuest;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class MockPreQuestJourney implements PreQuestJourney {

    public final AtomicBoolean invoked = new AtomicBoolean(false);

    @Override
    public BiConsumer<SuperQuest, Object[]> journey() {
        return (sq, args) -> invoked.set(true);
    }

    @Override
    public Enum<?> enumImpl() {
        return null;
    }
}
