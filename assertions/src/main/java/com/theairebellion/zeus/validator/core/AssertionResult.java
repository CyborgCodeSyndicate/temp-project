package com.theairebellion.zeus.validator.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssertionResult<T> {

    private final boolean passed;
    private final String description;
    private final T expectedValue;
    private final T actualValue;
    private boolean soft;


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
