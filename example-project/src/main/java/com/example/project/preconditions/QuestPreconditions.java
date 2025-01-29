package com.example.project.preconditions;

import com.example.project.model.Student;
import com.theairebellion.zeus.framework.parameters.Late;
import com.theairebellion.zeus.framework.parameters.PreQuestJourney;
import com.theairebellion.zeus.framework.quest.Quest;
import manifold.ext.rt.api.Jailbreak;

import java.util.function.BiConsumer;

import static com.example.project.preconditions.QuestPreconditionFunctions.login;
import static com.example.project.preconditions.QuestPreconditionFunctions.validStudentsSetup;

public enum QuestPreconditions implements PreQuestJourney {

    STUDENT_PRECONDITION(
        (quest, objects) -> validStudentsSetup(quest, (Student) objects[0])),
    STUDENT_PRECONDITION_LATE(
        (quest, objects) -> validStudentsSetup(quest,
            (Late<Student>) objects[0])),
    LOGIN((quest, objects) -> login(quest, (String) objects[0], (String) objects[1]));


    public static final class Data {

        public static final String STUDENT_PRECONDITION = "STUDENT_PRECONDITION";
        public static final String STUDENT_PRECONDITION_LATE = "STUDENT_PRECONDITION_LATE";
        public static final String LOGIN = "LOGIN";


        private Data() {
        }

    }


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
