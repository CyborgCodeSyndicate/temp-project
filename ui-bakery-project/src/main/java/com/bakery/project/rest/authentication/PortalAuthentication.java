package com.bakery.project.rest.authentication;

import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;

public class PortalAuthentication extends BaseAuthenticationClient {

    @Override
    protected Header authenticateImpl(final RestService restService, final String username, final String password) {

        return new Header("header", "portal");
    }

}
