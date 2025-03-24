package com.theairebellion.zeus.validator.core;

import com.theairebellion.zeus.ai.metadata.model.classes.CreationType;
import com.theairebellion.zeus.annotations.InfoAIClass;

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
