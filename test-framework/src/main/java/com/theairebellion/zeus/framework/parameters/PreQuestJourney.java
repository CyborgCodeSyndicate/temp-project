package com.theairebellion.zeus.framework.parameters;

import com.theairebellion.zeus.framework.quest.SuperQuest;

import java.util.function.BiConsumer;

public interface PreQuestJourney {

    BiConsumer<SuperQuest, Object[]> journey();

    Enum<?> enumImpl();

}
