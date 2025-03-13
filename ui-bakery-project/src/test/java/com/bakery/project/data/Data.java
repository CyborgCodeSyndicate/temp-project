package com.bakery.project.data;

import com.bakery.project.data.test.TestData;
import org.aeonbits.owner.ConfigCache;

public class Data {


    public static TestData testData() {
        return getTestDataConfig();
    }


    private static TestData getTestDataConfig() {
        return ConfigCache.getOrCreate(TestData.class);
    }


}
