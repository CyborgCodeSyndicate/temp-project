package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public abstract class BaseAuthenticationClient implements AuthenticationClient {

    public static final Map<String, Header> userAuthenticationHeaderMap = new HashMap<>();


    @Override
    public void authenticate(final RestService restService, final String username, final String password) {
        if (Objects.isNull(userAuthenticationHeaderMap.get(username))) {
            userAuthenticationHeaderMap.put(username, authenticateImpl(restService, username, password));
        }
    }


    public Header getAuthentication(String username) {
        return userAuthenticationHeaderMap.get(username);
    }


    protected abstract Header authenticateImpl(RestService restService, String username, String password);



}
