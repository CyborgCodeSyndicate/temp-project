package com.example.project.data.cleaner;

import com.theairebellion.zeus.framework.parameters.DataRipper;
import com.theairebellion.zeus.framework.quest.Quest;
import manifold.ext.rt.api.Jailbreak;

import java.util.function.Consumer;

public enum TestDataCleaner implements DataRipper {
    ALL_CREATED_STUDENTS_FLOW(DataCleanUpFunctions::cleanAllStudents);

    public static final String ALL_CREATED_STUDENTS = "ALL_CREATED_STUDENTS_FLOW";

    private final Consumer<@Jailbreak Quest> cleanUpFunction;


    TestDataCleaner(final Consumer<@Jailbreak Quest> cleanUpFunction) {
        this.cleanUpFunction = cleanUpFunction;
    }


    @Override
    public Consumer<@Jailbreak Quest> eliminate() {
        return cleanUpFunction;
    }


    @Override
    public Enum<?> enumImpl() {
        return this;
    }
}
