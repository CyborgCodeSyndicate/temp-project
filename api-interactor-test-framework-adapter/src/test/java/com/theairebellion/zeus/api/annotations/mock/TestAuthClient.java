package com.theairebellion.zeus.api.annotations.mock;

import com.theairebellion.zeus.api.authentication.AuthenticationKey;
import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;

public class TestAuthClient extends BaseAuthenticationClient {

    @Override
    public AuthenticationKey authenticate(RestService restService, String user, String pass) {
        return null;
    }

    @Override
    protected Header authenticateImpl(RestService restService, String username, String password) {
        return null;
    }
}

