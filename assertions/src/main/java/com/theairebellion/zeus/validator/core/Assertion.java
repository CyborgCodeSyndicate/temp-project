package com.theairebellion.zeus.validator.core;


import com.theairebellion.zeus.ai.metadata.model.CreationType;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.InfoAIClass;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@InfoAIClass(
    description = "Class representing assertion objects. Based on how the class is built different assertion will be performed",
    creationType = CreationType.BUILDER)
public class Assertion<T> {

    private final AssertionTarget target;
    @Setter
    @InfoAI(description = "The key used to specify what from the target should be extracted. Not mandatory.")
    private String key;
    private final AssertionType type;
    @InfoAI(description = "The expected value for assertion")
    private final T expected;
    @InfoAI(description = "Specify if the assertion should be soft or hard")
    private final boolean soft;

}