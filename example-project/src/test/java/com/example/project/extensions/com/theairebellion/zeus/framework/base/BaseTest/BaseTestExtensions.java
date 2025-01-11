package com.example.project.extensions.com.theairebellion.zeus.framework.base.BaseTest;

import com.theairebellion.zeus.framework.base.BaseTest;
import com.example.project.data.test.TestData;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.aeonbits.owner.ConfigCache;

@Extension
public class BaseTestExtensions {

    public static final TestData testData = ConfigCache.getOrCreate(TestData.class);


    public static TestData testData(@This BaseTest baseTest) {
        return testData;
    }


}
