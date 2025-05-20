package com.theairebellion.zeus.api.authentication.mock;

import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;

public class AnotherAuthClient extends BaseAuthenticationClient {

    @Override
    protected Header authenticateImpl(RestService restService, String username, String password) {
        return null;
    }

}