package com.theairebellion.zeus.validator.core;

import com.theairebellion.zeus.ai.metadata.model.CreationType;
import com.theairebellion.zeus.annotations.InfoAIClass;

@InfoAIClass(
    description = "Interface representing what part of specific object should be asserted. Implemented in Enums",
    creationType = CreationType.ENUM)
public interface AssertionTarget {

    Enum<?> target();

}
