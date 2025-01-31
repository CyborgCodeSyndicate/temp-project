package com.reqres.test.framework.data.creator;

import com.theairebellion.zeus.framework.parameters.DataForge;
import com.theairebellion.zeus.framework.parameters.Late;

public enum TestDataCreator implements DataForge {
    USER_LEADER_FLOW(DataCreationFunctions::createLeaderUser),
    LOGIN_ADMIN_USER_FLOW(DataCreationFunctions::createAdminLoginUser),
    USER_SUFFIX_FLOW(DataCreationFunctions::createSuffixUser),
    USER_PREFIX_FLOW(DataCreationFunctions::createPrefixUser);

    public static final String USER_LEADER = "USER_LEADER_FLOW";
    public static final String LOGIN_ADMIN_USER = "LOGIN_ADMIN_USER_FLOW";
    public static final String USER_SUFFIX = "USER_SUFFIX_FLOW";
    public static final String USER_PREFIX = "USER_PREFIX_FLOW";

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
