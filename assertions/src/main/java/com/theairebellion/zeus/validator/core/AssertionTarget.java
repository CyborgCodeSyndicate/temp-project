package com.theairebellion.zeus.validator.core;

/**
 * Defines a contract for specifying the target of an assertion.
 * <p>
 * This interface is implemented by enums that categorize assertions
 * for different validation contexts such as API responses, database queries,
 * and UI elements.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface AssertionTarget {

    /**
     * Retrieves the specific assertion target.
     *
     * @return The enum representing the assertion target.
     */
    Enum<?> target();

}
