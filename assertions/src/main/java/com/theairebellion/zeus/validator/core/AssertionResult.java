package com.theairebellion.zeus.validator.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents the result of an assertion evaluation.
 * <p>
 * This class stores details about an assertion's outcome, including whether it passed,
 * the expected and actual values, and an optional description. It also supports soft assertions,
 * allowing validations to continue even when failures occur.
 * </p>
 *
 * <p>The expected and actual values in the assertion are represented by the generic type {@code T},
 * which allows this class to handle various data types in validation checks.</p>
 *
 * @author Cyborg Code Syndicate
 */
@Getter
@AllArgsConstructor
public class AssertionResult<T> {

    /**
     * Indicates whether the assertion passed ({@code true}) or failed ({@code false}).
     */
    private final boolean passed;

    /**
     * A brief description of the assertion being performed.
     */
    @NonNull
    private final String description;

    /**
     * The expected value in the assertion.
     */
    private final Object expectedValue;

    /**
     * The actual value retrieved during validation.
     */
    private final T actualValue;

    /**
     * Specifies if the assertion is a soft assertion.
     */
    private boolean soft;


    /**
     * Returns a formatted string representing the assertion outcome.
     *
     * @return A descriptive summary of the assertion result.
     */
    @Override
    public String toString() {
        return passed
                   ?
                   String.format("✔ Validation passed: %s (Expected: %s, Actual: %s)", description, expectedValue,
                       actualValue)
                   : String.format("✘ Validation failed: %s (Expected: %s, Actual: %s)", description, expectedValue,
            actualValue);
    }

}
