package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.log.LogApi;
import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public abstract class BaseAuthenticationClient implements AuthenticationClient {

    public static final Map<AuthenticationKey, Header> userAuthenticationHeaderMap = new HashMap<>();


    @Override
    public AuthenticationKey authenticate(final RestService restService, final String username, final String password,
                                          boolean cache) {
        var authenticationKey = new AuthenticationKey(username, password, this.getClass());
        if (!cache) {
            userAuthenticationHeaderMap.put(authenticationKey, authenticateImpl(restService, username, password));
            LogApi.info("Successfully authenticated user: {}", username);
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
        LogApi.debug("Retrieving authentication header for user: {}", authenticationKey.getUsername());
        return userAuthenticationHeaderMap.get(authenticationKey);
    }


    protected abstract Header authenticateImpl(RestService restService, String username, String password);


}
