package com.theairebellion.zeus.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an implementation type for UI components.
 * <p>
 * This annotation is used to associate a specific implementation class
 * with a predefined UI component type. It allows the framework to
 * dynamically instantiate the correct component implementation based
 * on its type definition.
 * </p>
 *
 * <p>
 * Commonly used in UI component implementations such as alerts, checkboxes,
 * dropdowns, and other interactive elements.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ImplementationOfType {

    /**
     * Specifies the component type this implementation corresponds to.
     *
     * @return The type identifier for the UI component.
     */
    String value();
}
