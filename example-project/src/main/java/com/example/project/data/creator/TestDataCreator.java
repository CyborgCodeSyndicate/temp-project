package com.example.project.data.creator;

import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;

public enum TestDataCreator implements DataForge {
    VALID_STUDENT(DataCreationFunctions::createValidStudent),
    DOG_PET(DataCreationFunctions::createDog);

    public static final class Data {
        public static final String VALID_STUDENT = "VALID_STUDENT";
        public static final String DOG_PET = "DOG_PET";

        private Data() {
        }
    }

    private final Late<Object> createDataFunction;


    TestDataCreator(final Late<Object> createDataFunction) {
        this.createDataFunction = createDataFunction;
    }


    @Override
    public Late<Object> dataCreator() {
        return createDataFunction;
    }


    @Override
    public Enum<?> enumImpl() {
        return this;
    }
}
