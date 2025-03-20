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
import com.theairebellion.zeus.ai.metadata.model.classes.CreationType;
import com.theairebellion.zeus.annotations.InfoAIClass;

@InfoAIClass(
    description = "Interface representing what part of specific object should be asserted. Implemented in Enums",
    creationType = CreationType.ENUM)
public interface AssertionTarget {

    /**
     * Retrieves the specific assertion target.
     *
     * @return The enum representing the assertion target.
     */
    Enum<?> target();

}
