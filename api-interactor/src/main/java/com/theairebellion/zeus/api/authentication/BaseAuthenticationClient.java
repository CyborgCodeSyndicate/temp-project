package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public abstract class BaseAuthenticationClient implements AuthenticationClient {

    public static final Map<AuthenticationKey, Header> userAuthenticationHeaderMap = new ConcurrentHashMap<>();


    @Override
    public AuthenticationKey authenticate(final RestService restService, final String username, final String password,
                                          boolean cache) {
        var authenticationKey = new AuthenticationKey(username, password, this.getClass());
        if (!cache) {
            userAuthenticationHeaderMap.put(authenticationKey, authenticateImpl(restService, username, password));
        } else {
            synchronized (userAuthenticationHeaderMap) {
                if (Objects.isNull(userAuthenticationHeaderMap.get(authenticationKey))) {
                    userAuthenticationHeaderMap.put(authenticationKey,
                        authenticateImpl(restService, username, password));
                } else {
                    return authenticationKey;
                }
            }
        }
        return authenticationKey;
    }


    public Header getAuthentication(final AuthenticationKey authenticationKey) {
        return userAuthenticationHeaderMap.get(authenticationKey);
    }


    protected abstract Header authenticateImpl(RestService restService, String username, String password);


}
