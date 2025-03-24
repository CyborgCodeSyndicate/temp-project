package com.theairebellion.zeus.api.annotations;

import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.api.authentication.BaseAuthenticationClient;
import com.theairebellion.zeus.api.authentication.Credentials;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines API authentication parameters for test execution.
 * <p>
 * This annotation specifies the authentication credentials and client type
 * required for executing API tests.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@InfoAI(description = "Marks a test method that requires API authentication. " +
        "Automatically handles login using the provided credentials and authentication client before executing the test.")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthenticateViaApiAs {

    /**
     * Specifies the credentials class providing authentication details.
     *
     * @return The class implementing {@link Credentials}.
     */
    Class<? extends Credentials> credentials();

    /**
     * Specifies the authentication client responsible for handling authentication.
     *
     * @return The class extending {@link BaseAuthenticationClient}.
     */
    Class<? extends BaseAuthenticationClient> type();

    /**
     * Determines whether authentication credentials should be cached.
     *
     * @return {@code true} if credentials should be cached, otherwise {@code false}.
     */
    boolean cacheCredentials() default false;

}
