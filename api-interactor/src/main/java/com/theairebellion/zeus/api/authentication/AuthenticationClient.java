package com.theairebellion.zeus.api.authentication;

import com.theairebellion.zeus.api.service.RestService;

/**
 * Defines the contract for authentication mechanisms.
 * <p>
 * Implementations of this interface handle user authentication and return an {@link AuthenticationKey}
 * that uniquely identifies authenticated sessions.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface AuthenticationClient {

    /**
     * Authenticates a user using the provided credentials.
     *
     * @param restService The {@link RestService} instance used to execute the authentication request.
     * @param username    The username of the user.
     * @param password    The password of the user.
     * @param cache       If {@code true}, the authentication result is cached for reuse.
     * @return The {@link AuthenticationKey} representing the authenticated session.
     */
    AuthenticationKey authenticate(RestService restService, String username, String password, boolean cache);
}
