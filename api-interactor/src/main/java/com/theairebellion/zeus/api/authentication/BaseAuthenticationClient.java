package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public abstract class BaseAuthenticationClient implements AuthenticationClient {

    public static final Map<AuthenticationKey, Header> userAuthenticationHeaderMap = new HashMap<>();


    @Override
    public AuthenticationKey authenticate(final RestService restService, final String username, final String password) {
        var authenticationKey = new AuthenticationKey(username, password, this.getClass());
        if (Objects.isNull(userAuthenticationHeaderMap.get(authenticationKey))) {
            userAuthenticationHeaderMap.put(authenticationKey, authenticateImpl(restService, username, password));
        }
        return authenticationKey;
    }


    public Header getAuthentication(final AuthenticationKey authenticationKey) {
        return userAuthenticationHeaderMap.get(authenticationKey);
    }


    protected abstract Header authenticateImpl(RestService restService, String username, String password);


}
