package com.theairebellion.zeus.validator.util;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.core.AssertionType;
import com.theairebellion.zeus.validator.exceptions.InvalidAssertionException;
import com.theairebellion.zeus.validator.registry.AssertionRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AssertionUtil {

    public static <T> List<AssertionResult<T>> validate(Map<String, T> data, Assertion<?>... assertions) {
        if (data == null) {
            throw new IllegalArgumentException("The data map cannot be null.");
        }
        if (assertions == null || assertions.length == 0) {
            throw new IllegalArgumentException("At least one assertion must be provided.");
        }

        return Arrays.stream(assertions).map(assertion -> validateSingle(data, assertion)).toList();
    }


    private static <T> AssertionResult<T> validateSingle(Map<String, T> data, Assertion<?> assertion) {
        validateAssertion(assertion);

        String key = assertion.getKey();
        T actualValue = data.get(key);

        if (actualValue == null) {
            throw new IllegalArgumentException(
                String.format("Key '%s' in assertion does not exist or has a null value in the data map.", key)
            );
        }

        AssertionType type = assertion.getType();
        validateTypeCompatibility(type, actualValue);

        var validator = AssertionRegistry.getValidator(type);
        boolean passed = validator.apply(actualValue, assertion.getExpected());

        return new AssertionResult(
            passed,
            type.type().name(),
            assertion.getExpected(),
            actualValue,
            assertion.isSoft()
        );
    }


    private static void validateAssertion(Assertion<?> assertion) {
        if (assertion == null) {
            throw new InvalidAssertionException("Assertion cannot be null.");
        }

        if (assertion.getKey() == null || assertion.getKey().isEmpty()) {
            throw new InvalidAssertionException("Assertion must have a non-empty key.");
        }

        if (assertion.getType() == null) {
            throw new InvalidAssertionException("Assertion must have a non-null type.");
        }

        if (assertion.getTarget() == null) {
            throw new InvalidAssertionException("Assertion must have a non-null target.");
        }

        if (assertion.getExpected() == null) {
            throw new InvalidAssertionException("Assertion must have a non-null expected.");
        }
    }

    private static void validateTypeCompatibility(AssertionType type, Object actualValue) {
        Class<?> supportedType = type.getSupportedType();
        Class<?> actualValueClass = actualValue.getClass();

        if (!supportedType.isAssignableFrom(actualValueClass)) {
            throw new IllegalArgumentException(
                String.format(
                    "Assertion type '%s' is not compatible with the actual value type '%s'.",
                    type, actualValueClass.getName()
                )
            );
        }
    }

}
