package com.example.project.data.cleaner;

import com.theairebellion.zeus.framework.parameters.DataRipper;
import com.theairebellion.zeus.framework.quest.Quest;
import manifold.ext.rt.api.Jailbreak;

import java.util.function.Consumer;

public enum TestDataCleaner implements DataRipper {
    ALL_CREATED_STUDENTS(DataCleanUpFunctions::cleanAllStudents);

    public static final class Data {
        public static final String ALL_CREATED_STUDENTS = "ALL_CREATED_STUDENTS";

        private Data() {
        }
    }


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
