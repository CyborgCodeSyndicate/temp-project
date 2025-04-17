package com.theairebellion.zeus.api.mock;

import com.theairebellion.zeus.api.authentication.Credentials;

public class TestCreds implements Credentials {

    @Override
    public String username() {
        return "testUser";
    }

    @Override
    public String password() {
        return "testPass";
    }
}
