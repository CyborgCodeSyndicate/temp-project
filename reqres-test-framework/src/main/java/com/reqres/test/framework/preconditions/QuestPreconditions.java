package com.reqres.test.framework.preconditions;

import com.theairebellion.zeus.framework.parameters.PreQuestJourney;
import com.theairebellion.zeus.framework.quest.SuperQuest;

import java.util.function.BiConsumer;

public enum QuestPreconditions implements PreQuestJourney<QuestPreconditions> {

    CREATE_NEW_USER_FLOW(QuestPreconditionFunctions::createNewUser);

    public static final String CREATE_NEW_USER = "CREATE_NEW_USER_FLOW";

    private final BiConsumer<SuperQuest, Object[]> function;

    QuestPreconditions(final BiConsumer<SuperQuest, Object[]> function) {
        this.function = function;
    }

    @Override
    public BiConsumer<SuperQuest, Object[]> journey() {
        return function;
    }

    @Override
    public QuestPreconditions enumImpl() {
        return this;
    }

}
