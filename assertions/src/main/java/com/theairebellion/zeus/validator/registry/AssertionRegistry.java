package com.theairebellion.zeus.validator.registry;

import com.theairebellion.zeus.validator.core.AssertionType;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import com.theairebellion.zeus.validator.functions.AssertionFunctions;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class AssertionRegistry {

    private static final Map<AssertionType, BiFunction<Object, Object, Boolean>> VALIDATORS = new ConcurrentHashMap<>();

    static {
        VALIDATORS.put(AssertionTypes.IS, AssertionFunctions::equals);
        VALIDATORS.put(AssertionTypes.NOT, AssertionFunctions::notEquals);
        VALIDATORS.put(AssertionTypes.CONTAINS, AssertionFunctions::contains);
        VALIDATORS.put(AssertionTypes.NOT_NULL, AssertionFunctions::notNull);
        VALIDATORS.put(AssertionTypes.ALL_NOT_NULL, AssertionFunctions::allNotNull);
        VALIDATORS.put(AssertionTypes.IS_NULL, AssertionFunctions::isNull);
        VALIDATORS.put(AssertionTypes.ALL_NULL, AssertionFunctions::allNull);
        VALIDATORS.put(AssertionTypes.GREATER_THAN, AssertionFunctions::greaterThan);
        VALIDATORS.put(AssertionTypes.LESS_THAN, AssertionFunctions::lessThan);
        VALIDATORS.put(AssertionTypes.CONTAINS_ALL, AssertionFunctions::containsAll);
        VALIDATORS.put(AssertionTypes.CONTAINS_ANY, AssertionFunctions::containsAny);
        VALIDATORS.put(AssertionTypes.STARTS_WITH, AssertionFunctions::startsWith);
        VALIDATORS.put(AssertionTypes.ENDS_WITH, AssertionFunctions::endsWith);
        VALIDATORS.put(AssertionTypes.LENGTH, AssertionFunctions::length);
        VALIDATORS.put(AssertionTypes.MATCHES_REGEX, AssertionFunctions::matchesRegex);
        VALIDATORS.put(AssertionTypes.EMPTY, AssertionFunctions::isEmpty);
        VALIDATORS.put(AssertionTypes.NOT_EMPTY, AssertionFunctions::isNotEmpty);
        VALIDATORS.put(AssertionTypes.BETWEEN, AssertionFunctions::between);
        VALIDATORS.put(AssertionTypes.EQUALS_IGNORE_CASE, AssertionFunctions::equalsIgnoreCase);
    }

    public static void registerCustomAssertion(AssertionType type, BiFunction<Object, Object, Boolean> validator) {
        Objects.requireNonNull(type, "AssertionType must not be null");
        Objects.requireNonNull(validator, "Validator must not be null");

        VALIDATORS.put(type, validator);
    }


    public static BiFunction<Object, Object, Boolean> getValidator(AssertionType type) {
        Objects.requireNonNull(type, "AssertionType must not be null");
        BiFunction<Object, Object, Boolean> validator = VALIDATORS.get(type);

        if (validator == null) {
            throw new IllegalArgumentException("No validator registered for AssertionType: " + type);
        }

        return validator;
    }

}
