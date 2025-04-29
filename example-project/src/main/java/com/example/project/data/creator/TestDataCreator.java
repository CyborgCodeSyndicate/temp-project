package com.example.project.data.creator;

import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;

public enum TestDataCreator implements DataForge<TestDataCreator> {
    VALID_STUDENT(DataCreationFunctions::createValidStudent),
    DOG_PET(DataCreationFunctions::createDog),
    USERNAME_JOHN(DataCreationFunctions::usernameJohn),
    PASSWORD_JOHN(DataCreationFunctions::passwordJohn);


    public static final class Data {

        public static final String VALID_STUDENT = "VALID_STUDENT";
        public static final String DOG_PET = "DOG_PET";
        public static final String USERNAME_JOHN = "USERNAME_JOHN";
        public static final String PASSWORD_JOHN = "PASSWORD_JOHN";

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
    public TestDataCreator enumImpl() {
        return this;
    }
}
