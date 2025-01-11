package com.theairebellion.zeus.framework.parameters;

import com.theairebellion.zeus.framework.quest.Quest;
import manifold.ext.rt.api.Jailbreak;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface PreQuestJourney {

    BiConsumer<@Jailbreak Quest, Object[]> journey();

    Enum<?> enumImpl();

}
