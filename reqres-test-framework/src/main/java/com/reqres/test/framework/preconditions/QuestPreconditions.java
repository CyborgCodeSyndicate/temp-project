package com.reqres.test.framework.preconditions;

import com.theairebellion.zeus.framework.parameters.PreQuestJourney;
import com.theairebellion.zeus.framework.quest.SuperQuest;

import java.util.function.BiConsumer;

public enum QuestPreconditions implements PreQuestJourney {

    CREATE_NEW_USER(QuestPreconditionFunctions::createNewUser);

    public static final class Data {

        public static final String CREATE_NEW_USER = "CREATE_NEW_USER";

    }


    private final BiConsumer<SuperQuest, Object[]> function;


    QuestPreconditions(final BiConsumer<SuperQuest, Object[]> function) {
        this.function = function;
    }


    @Override
    public BiConsumer<SuperQuest, Object[]> journey() {
        return function;
    }


    @Override
    public Enum<?> enumImpl() {
        return this;
    }

}
