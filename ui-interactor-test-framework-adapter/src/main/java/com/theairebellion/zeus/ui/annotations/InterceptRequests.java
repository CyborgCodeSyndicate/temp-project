package com.theairebellion.zeus.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for intercepting network requests during UI automation tests.
 * <p>
 * This annotation is used on test methods to specify a list of request URL substrings
 * that should be intercepted and monitored during execution. It is particularly useful
 * for verifying API calls made by the UI, debugging network interactions, and applying
 * request modifications.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InterceptRequests {

    /**
     * Specifies an array of request URL substrings to intercept.
     * <p>
     * Any request made by the UI that contains one of these substrings
     * will be intercepted during the test execution.
     * </p>
     *
     * @return An array of substrings representing parts of request URLs to intercept.
     */
    String[] requestUrlSubStrings() default {};
}
