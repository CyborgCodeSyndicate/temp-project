package com.reqres.test.framework.rest.authentication;

import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;

import static com.reqres.test.framework.rest.Endpoints.LOGIN_USER;

public class ReqResAuthentication extends BaseAuthenticationClient {

    @Override
    protected Header authenticateImpl(final RestService restService, final String username, final String password) {
        String token = restService
                .request(LOGIN_USER, new LoginUser(username, password))
                .getBody()
                .jsonPath()
                .getString("token");
        return new Header("Authorization", "Bearer " + token);
    }

}
