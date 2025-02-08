package com.example.project.extensions.com.theairebellion.zeus.framework.base.BaseTest;

import com.example.project.data.test.TestData;
import com.theairebellion.zeus.framework.base.BaseTest;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.aeonbits.owner.ConfigCache;

@Extension
public class BaseTestExtensions {


    public static TestData testData(@This BaseTest baseTest) {
        return getTestDataConfig();
    }


    private static TestData getTestDataConfig() {
        return ConfigCache.getOrCreate(TestData.class);
    }


}
