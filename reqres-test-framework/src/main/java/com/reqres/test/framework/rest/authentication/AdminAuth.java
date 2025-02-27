package com.reqres.test.framework.rest.authentication;

import com.reqres.test.framework.data.test.TestData;
import com.theairebellion.zeus.api.authentication.Credentials;
import org.aeonbits.owner.ConfigCache;

public class AdminAuth implements Credentials {

    private static final TestData testData = ConfigCache.getOrCreate(TestData.class);

    @Override
    public String username() {
        return testData.username();
    }

    @Override
    public String password() {
        return testData.password();
    }

}
