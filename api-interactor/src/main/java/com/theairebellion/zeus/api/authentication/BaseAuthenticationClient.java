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
    public AuthenticationKey authenticate(final RestService restService, final String username, final String password) {
        var authenticationKey = new AuthenticationKey(username, password, this.getClass());
        if (Objects.isNull(userAuthenticationHeaderMap.get(authenticationKey))) {
            try {
                userAuthenticationHeaderMap.put(authenticationKey, authenticateImpl(restService, username, password));
                LogApi.info("Successfully authenticated user: {}", username);
            } catch (Exception e) {
                LogApi.error("Authentication failed for user: {}. Error: {}", username, e.getMessage(), e);
                throw e;
            }
        } else {
            LogApi.debug("User already authenticated: {}", username);
        }

        return authenticationKey;
    }


    public Header getAuthentication(final AuthenticationKey authenticationKey) {
        LogApi.debug("Retrieving authentication header for user: {}", authenticationKey.getUsername());
        return userAuthenticationHeaderMap.get(authenticationKey);
    }


    protected abstract Header authenticateImpl(RestService restService, String username, String password);


}
