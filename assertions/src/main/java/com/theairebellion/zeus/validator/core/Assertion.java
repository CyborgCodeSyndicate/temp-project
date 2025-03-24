package com.theairebellion.zeus.validator.core;


import com.theairebellion.zeus.ai.metadata.model.classes.CreationType;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.InfoAIClass;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a validation rule used in test assertions.
 * <p>
 * This class defines the structure of an assertion, specifying what is being validated,
 * the type of validation, the expected value, and whether it should be treated as a soft assertion.
 * Assertions can be applied in various testing contexts, including API responses, UI elements,
 * and database records.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Getter
@Builder
@InfoAIClass(
        description = "Class representing assertion objects. Used to define validation logic in test automation, " +
                "specifying validation targets, assertion types, and expected values.",
        creationType = CreationType.BUILDER)
public class Assertion<T> {

    /**
     * The subject of the assertion, specifying what is being validated.
     */
    private final AssertionTarget target;

    /**
     * Provides a supplementary identifier for targeted validation.
     */
    @Setter
    @InfoAI(description = "The key used to specify what specific part of the target should be validated (e.g., JSON path, field name). Not mandatory.")
    private String key;

    /**
     * Indicates the logical operation for this validation.
     */
    private final AssertionType type;

    /**
     * The reference value expected by this assertion.
     */
    @InfoAI(description = "Defines the expected value that the assertion should compare against.")
    private final T expected;

    /**
     * Determines if the assertion is a soft assertion.
     */
    @InfoAI(description = "Specifies whether the assertion should be soft (non-blocking) or hard (test fails immediately on failure).")
    private final boolean soft;

}