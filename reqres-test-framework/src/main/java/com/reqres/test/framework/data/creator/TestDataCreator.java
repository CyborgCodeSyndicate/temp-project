package com.reqres.test.framework.data.creator;

import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;

public enum TestDataCreator implements DataForge {
    USER_LEADER_FLOW(DataCreationFunctions::createLeaderUser);

    public static final String USER_LEADER = "USER_LEADER_FLOW";

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
