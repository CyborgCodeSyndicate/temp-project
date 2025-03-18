package com.theairebellion.zeus.validator.core;


import com.theairebellion.zeus.ai.metadata.model.CreationType;
import com.theairebellion.zeus.annotations.InfoAIClass;

@InfoAIClass(
    description = "Interface representing what assertion function will be performed. Implemented in Enums",
    creationType = CreationType.ENUM)
public interface AssertionType {

    Enum<?> type();

    Class<?> getSupportedType();

}
