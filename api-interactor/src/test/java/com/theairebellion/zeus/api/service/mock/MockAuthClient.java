package com.theairebellion.zeus.api.service.mock;

import com.theairebellion.zeus.api.authentication.AuthenticationKey;
import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;

public class MockAuthClient extends BaseAuthenticationClient {

    @Override
    public AuthenticationKey authenticate(RestService restService, String user, String pass, boolean cache) {
        return new AuthenticationKey("username", "password", null);
    }

    @Override
    protected Header authenticateImpl(RestService restService, String username, String password) {
        return null;
    }
}