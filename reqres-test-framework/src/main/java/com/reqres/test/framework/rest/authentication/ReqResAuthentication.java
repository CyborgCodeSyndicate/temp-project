package com.reqres.test.framework.rest.authentication;

import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;

import static com.reqres.test.framework.rest.ApiResponsesJsonPaths.TOKEN;
import static com.reqres.test.framework.rest.Endpoints.POST_LOGIN_USER;
import static com.reqres.test.framework.utils.Headers.AUTHORIZATION_HEADER_KEY;
import static com.reqres.test.framework.utils.Headers.AUTHORIZATION_HEADER_VALUE;

public class ReqResAuthentication extends BaseAuthenticationClient {

    @Override
    protected Header authenticateImpl(final RestService restService, final String username, final String password) {
        String token = restService
                .request(POST_LOGIN_USER, new LoginUser(username, password))
                .getBody()
                .jsonPath()
                .getString(TOKEN.getJsonPath());
        return new Header(AUTHORIZATION_HEADER_KEY, AUTHORIZATION_HEADER_VALUE + token);
    }

}
