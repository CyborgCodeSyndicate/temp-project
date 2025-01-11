package com.example.project.preconditions;

import com.theairebellion.zeus.framework.parameters.PreQuestJourney;
import com.theairebellion.zeus.framework.quest.Quest;
import manifold.ext.rt.api.Jailbreak;

import java.util.function.BiConsumer;

public enum QuestPreconditions implements PreQuestJourney {

    STUDENT_PRECONDITION_FLOW(QuestPreconditionFunctions::validStudentsSetup),
    STUDENT_PRECONDITION_FLOW_LATE(QuestPreconditionFunctions::validStudentsSetup);


    public static final String STUDENT_PRECONDITION = "STUDENT_PRECONDITION_FLOW";
    public static final String STUDENT_PRECONDITION_LATE = "STUDENT_PRECONDITION_FLOW_LATE";


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
