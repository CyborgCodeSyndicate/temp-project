package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.log.LogApi;
import com.theairebellion.zeus.api.service.RestService;
import io.restassured.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract base class for authentication clients.
 * <p>
 * Provides a caching mechanism for authentication headers and defines
 * a template method for implementing authentication logic.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public abstract class BaseAuthenticationClient implements AuthenticationClient {

    /** Stores authentication headers mapped by their corresponding authentication keys. */
    public static final Map<AuthenticationKey, Header> userAuthenticationHeaderMap = new HashMap<>();

    /**
     * Authenticates a user and caches the authentication header if caching is enabled.
     *
     * @param restService The {@code RestService} instance handling the authentication request.
     * @param username    The username for authentication.
     * @param password    The password for authentication.
     * @param cache       Whether to cache the authentication header.
     * @return The generated {@code AuthenticationKey}.
     */
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

    /**
     * Retrieves the authentication header associated with the given authentication key.
     *
     * @param authenticationKey The authentication key identifying the session.
     * @return The corresponding authentication header, or {@code null} if not found.
     */
    public Header getAuthentication(final AuthenticationKey authenticationKey) {
        if (authenticationKey == null) {
            LogApi.error("AuthenticationKey is null. Cannot retrieve authentication header.");
            throw new IllegalArgumentException("AuthenticationKey cannot be null.");
        }
        LogApi.debug("Retrieving authentication header for user: {}", authenticationKey.getUsername());
        return userAuthenticationHeaderMap.get(authenticationKey);
    }

    /**
     * Performs the actual authentication process and returns the authentication header.
     *
     * @param restService The {@code RestService} instance handling the request.
     * @param username    The username for authentication.
     * @param password    The password for authentication.
     * @return The authentication header containing credentials.
     */
    protected abstract Header authenticateImpl(RestService restService, String username, String password);
}
