package com.theairebellion.zeus.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Handles UI-related exceptions automatically.
 * <p>
 * This annotation is used on methods that interact with web elements
 * to ensure exceptions are properly managed during UI interactions.
 * When applied, it allows the framework to handle known exceptions
 * gracefully instead of letting them fail immediately.
 * </p>
 *
 * <p>
 * Typically used in `SmartWebDriver` and `SmartWebElement` classes to wrap
 * interactions with Selenium WebDriver elements.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HandleUIException {

    /**
     * Enables or disables exception handling for the annotated method.
     *
     * @return {@code true} if exception handling is enabled, {@code false} otherwise.
     */
    boolean value() default true;
}
