package com.bakery.project.data.cleaner;

import com.theairebellion.zeus.framework.parameters.DataRipper;
import com.theairebellion.zeus.framework.quest.SuperQuest;

import java.util.function.Consumer;

public enum TestDataCleaner implements DataRipper<TestDataCleaner> {
    DELETE_CREATED_ORDERS(DataCleanUpFunctions::cleanAllOrders);

    public static final class Data {
        public static final String DELETE_CREATED_ORDERS = "DELETE_CREATED_ORDERS";

        private Data() {
        }
    }


    private final Consumer<SuperQuest> cleanUpFunction;


    TestDataCleaner(final Consumer<SuperQuest> cleanUpFunction) {
        this.cleanUpFunction = cleanUpFunction;
    }


    @Override
    public Consumer<SuperQuest> eliminate() {
        return cleanUpFunction;
    }


    @Override
    public TestDataCleaner enumImpl() {
        return this;
    }
}
