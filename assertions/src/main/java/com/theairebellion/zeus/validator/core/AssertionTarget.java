package com.theairebellion.zeus.validator.core;

import com.theairebellion.zeus.ai.metadata.model.CreationType;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.InfoAIClass;

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
@InfoAIClass(
        description = "Interface representing assertion targets. Defines which part of an " +
                "object (e.g., API response, UI element, database field) should be validated. Implemented in Enums.",
        creationType = CreationType.ENUM)
public interface AssertionTarget {

    /**
     * Retrieves the specific assertion target.
     *
     * @return The enum representing the assertion target.
     */
    Enum<?> target();

}
