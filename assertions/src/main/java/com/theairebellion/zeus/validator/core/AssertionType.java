package com.theairebellion.zeus.validator.core;

/**
 * Represents a type of assertion used for validation.
 * <p>
 * Implementations of this interface define specific assertion types
 * that determine how validation is performed across different domains,
 * such as API responses, database queries, and UI components.
 * </p>
 *
 * <p>Each assertion type specifies:</p>
 * <ul>
 *     <li>A unique identifier for the assertion type.</li>
 *     <li>The expected data type that the assertion operates on.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate
 */

import com.theairebellion.zeus.ai.metadata.model.CreationType;
import com.theairebellion.zeus.annotations.InfoAIClass;

@InfoAIClass(
    description = "Interface representing what assertion function will be performed. Implemented in Enums",
    creationType = CreationType.ENUM)
public interface AssertionType {

    /**
     * Retrieves the unique identifier of the assertion type.
     *
     * @return The enum representing the assertion type.
     */
    Enum<?> type();

    /**
     * Retrieves the supported data type for this assertion.
     * <p>
     * Assertions are designed to validate specific data types.
     * This method ensures compatibility by defining which data type
     * the assertion operates on.
     * </p>
     *
     * @return The class representing the supported data type.
     */
    Class<?> getSupportedType();

}
