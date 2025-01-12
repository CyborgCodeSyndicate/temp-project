package com.example.project.data.creator;

import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;

public enum TestDataCreator implements DataForge {
    VALID_STUDENT_FLOW(DataCreationFunctions::createValidStudent),
    DOG_PET_FLOW(DataCreationFunctions::createDog);

    public static final String VALID_STUDENT = "VALID_STUDENT_FLOW";
    public static final String DOG_PET = "DOG_PET_FLOW";

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
