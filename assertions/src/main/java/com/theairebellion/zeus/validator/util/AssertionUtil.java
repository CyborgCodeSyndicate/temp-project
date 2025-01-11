package com.theairebellion.zeus.validator.util;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.registry.AssertionRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AssertionUtil {

    public static <T> List<AssertionResult<T>> validate(Map<String, T> data, Assertion<?>... assertions) {
        return Arrays.stream(assertions)
            .map(assertion -> validateSingle(data, assertion))
            .collect(Collectors.toList());
    }


    private static <T> AssertionResult<T> validateSingle(Map<String, T> data, Assertion<?> assertion) {
        T actualValue = data.get(assertion.getKey());

        var validator = AssertionRegistry.getValidator(assertion.getType());
        boolean passed = validator.apply(actualValue, assertion.getExpected());

        return new AssertionResult(
            passed,
            assertion.getType().type().name(),
            assertion.getExpected(),
            actualValue,
            assertion.isSoft()
        );
    }

}
