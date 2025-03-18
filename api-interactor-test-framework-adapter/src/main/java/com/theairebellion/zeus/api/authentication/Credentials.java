package com.theairebellion.zeus.api.authentication;

/**
 * Represents authentication credentials for API authentication.
 * <p>
 * Implementing classes provide username and password details used for authentication
 * in API requests.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface Credentials {

    /**
     * Retrieves the username associated with the credentials.
     *
     * @return The username.
     */
    String username();

    /**
     * Retrieves the password associated with the credentials.
     *
     * @return The password.
     */
    String password();
}
