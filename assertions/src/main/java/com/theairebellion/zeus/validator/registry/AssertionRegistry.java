package com.theairebellion.zeus.validator.registry;

import com.theairebellion.zeus.validator.functions.AssertionFunctions;
import com.theairebellion.zeus.validator.core.AssertionType;
import com.theairebellion.zeus.validator.core.AssertionTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class AssertionRegistry {

    private static final Map<AssertionType, BiFunction<Object, Object, Boolean>> VALIDATORS = new HashMap<>();

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
    }

    public static void registerCustomAssertion(AssertionType type, BiFunction<Object, Object, Boolean> validator) {
        VALIDATORS.put(type, validator);
    }


    public static BiFunction<Object, Object, Boolean> getValidator(AssertionType type) {
        return VALIDATORS.get(type);
    }

}
