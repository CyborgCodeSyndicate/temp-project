package com.theairebellion.zeus.framework.retry;

import com.theairebellion.zeus.framework.retry.mock.MockRetryCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RetryConditionTest {

    @Test
    void testFunctionReturnsExpectedValue() {
        RetryCondition<Integer> condition = new MockRetryCondition<>(obj -> 42, val -> val == 42);
        Integer value = condition.function().apply(new Object());
        assertEquals(42, value);
    }

    @Test
    void testConditionReturnsTrueForExpectedValue() {
        RetryCondition<Integer> condition = new MockRetryCondition<>(obj -> 42, val -> val == 42);
        assertTrue(condition.condition().test(42));
    }

    @Test
    void testConditionReturnsFalseForUnexpectedValue() {
        RetryCondition<Integer> condition = new MockRetryCondition<>(obj -> 42, val -> val == 42);
        assertFalse(condition.condition().test(41));
    }
}