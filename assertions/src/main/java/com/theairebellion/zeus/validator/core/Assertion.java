package com.theairebellion.zeus.validator.core;


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
public class Assertion<T> {

    /**
     * The subject of the assertion, specifying what is being validated.
     */
    private final AssertionTarget target;

    /**
     * Provides a supplementary identifier for targeted validation.
     */
    @Setter
    private String key;

    /**
     * Indicates the logical operation for this validation.
     */
    private final AssertionType type;

    /**
     * The reference value expected by this assertion.
     */
    private final T expected;

    /**
     * Determines if the assertion is a soft assertion.
     */
    private final boolean soft;

}