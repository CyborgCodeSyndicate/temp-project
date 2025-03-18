package com.theairebellion.zeus.ui.components.base;

/**
 * Represents a UI component type.
 * <p>
 * This interface is used to define various types of UI components such as
 * buttons, checkboxes, dropdowns, accordions, and more.
 * </p>
 * <p>
 * Each specific component type extends this interface and provides
 * its own enumeration values representing available component variations.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface ComponentType {

    /**
     * Retrieves the enum representation of the component type.
     * <p>
     * This method is used to determine the specific type of a UI component,
     * allowing for dynamic handling within the framework.
     * </p>
     *
     * @return The enum value representing the component type.
     */
    Enum<?> getType();
}
