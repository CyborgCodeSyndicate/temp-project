package com.reqres.test.framework.preconditions;

import com.theairebellion.zeus.framework.parameters.PreQuestJourney;
import com.theairebellion.zeus.framework.quest.Quest;
import manifold.ext.rt.api.Jailbreak;

import java.util.function.BiConsumer;

public enum QuestPreconditions implements PreQuestJourney {

    CREATE_NEW_LEADER_USER_FLOW(QuestPreconditionFunctions::createNewLeaderUser);

    public static final String CREATE_NEW_LEADER_USER = "CREATE_NEW_LEADER_USER_FLOW";

    private final BiConsumer<@Jailbreak Quest, Object[]> function;

    QuestPreconditions(final BiConsumer<@Jailbreak Quest, Object[]> function) {
        this.function = function;
    }

    @Override
    public BiConsumer<@Jailbreak Quest, Object[]> journey() {
        return function;
    }

    @Override
    public Enum<?> enumImpl() {
        return this;
    }
}
