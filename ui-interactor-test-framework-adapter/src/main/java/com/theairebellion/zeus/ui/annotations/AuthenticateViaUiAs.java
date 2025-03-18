package com.theairebellion.zeus.ui.annotations;

import com.theairebellion.zeus.ui.authentication.BaseLoginClient;
import com.theairebellion.zeus.ui.authentication.LoginCredentials;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to authenticate a user via the UI in test scenarios.
 * <p>
 * This annotation is used to perform UI-based authentication by specifying
 * the required credentials and the login client responsible for handling the authentication process.
 * It supports caching credentials to optimize performance by reducing redundant logins.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthenticateViaUiAs {

    /**
     * Specifies the credentials class required for authentication.
     *
     * @return The class that provides login credentials.
     */
    Class<? extends LoginCredentials> credentials();

    /**
     * Specifies the UI login client class responsible for handling authentication.
     *
     * @return The class implementing the authentication logic.
     */
    Class<? extends BaseLoginClient> type();

    /**
     * Determines whether the credentials should be cached.
     * <p>
     * If set to {@code true}, the framework will store the credentials and reuse them
     * for subsequent tests to avoid redundant logins.
     * </p>
     *
     * @return {@code true} if credentials should be cached, {@code false} otherwise.
     */
    boolean cacheCredentials() default false;

}
