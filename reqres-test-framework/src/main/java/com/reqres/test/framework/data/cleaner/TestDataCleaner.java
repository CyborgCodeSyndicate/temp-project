package com.reqres.test.framework.data.cleaner;

import com.theairebellion.zeus.framework.parameters.DataRipper;
import com.theairebellion.zeus.framework.quest.Quest;
import manifold.ext.rt.api.Jailbreak;

import java.util.function.Consumer;

public enum TestDataCleaner implements DataRipper {
    DELETE_ADMIN_USER_FLOW(DataCleanUpFunctions::deleteAdminUser);

    public static final String DELETE_ADMIN_USER = "DELETE_ADMIN_USER_FLOW";

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
