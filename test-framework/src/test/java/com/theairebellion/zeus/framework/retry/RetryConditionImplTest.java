package com.theairebellion.zeus.framework.retry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

class RetryConditionImplTest {

    @Test
    void testFunctionAndCondition() {
        Function<Object, Integer> func = obj -> 42;
        Predicate<Integer> pred = value -> value == 42;
        RetryCondition<Integer> conditionImpl = new RetryConditionImpl<>(func, pred);
        Integer result = conditionImpl.function().apply(new Object());
        assertEquals(42, result);
        assertTrue(conditionImpl.condition().test(42));
        assertFalse(conditionImpl.condition().test(41));
    }
}