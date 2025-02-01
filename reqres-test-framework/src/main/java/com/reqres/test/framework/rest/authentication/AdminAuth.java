package com.reqres.test.framework.rest.authentication;

import com.theairebellion.zeus.api.authentication.Credentials;

public class AdminAuth implements Credentials {

    @Override
    public String username() {
        return "eve.holt@reqres.in";
    }

    @Override
    public String password() {
        return "cityslicka";
    }

}
