package com.theairebellion.zeus.ui.util.strategy;

import com.theairebellion.zeus.ui.components.base.ComponentType;

/**
 * Enum representing different selection strategies for UI components.
 * <p>
 * These strategies define how elements should be selected or interacted with
 * when multiple options are available within a UI component.
 * </p>
 *
 * This enum is used in conjunction with UI components such as lists and checkboxes
 * to define a selection approach dynamically.
 *
 * @author Cyborg Code Syndicate
 */
public enum Strategy implements ComponentType {

    /**
     * Selects a random element from the available options.
     */
    RANDOM,

    /**
     * Selects the first element from the available options.
     */
    FIRST,

    /**
     * Selects the last element from the available options.
     */
    LAST,

    /**
     * Selects all available elements.
     */
    ALL;

    /**
     * Retrieves the type of the current strategy.
     *
     * @return The enum value representing the selection strategy.
     */
    @Override
    public Enum<?> getType() {
        return this;
    }
}
