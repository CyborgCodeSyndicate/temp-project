package com.theairebellion.zeus.framework.retry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class RetryConditionImplTest {

    private static final int EXPECTED_VALUE = 42;
    private final Function<Object, Integer> testFunction = obj -> EXPECTED_VALUE;
    private final Predicate<Integer> testPredicate = value -> value == EXPECTED_VALUE;
    private final RetryConditionImpl<Integer> retryCondition = new RetryConditionImpl<>(testFunction, testPredicate);

    @Nested
    @DisplayName("Function tests")
    class FunctionTests {
        @Test
        @DisplayName("Should return expected value from function")
        void testFunction() {
            // When
            Integer result = retryCondition.function().apply(new Object());

            // Then
            assertEquals(EXPECTED_VALUE, result,
                    "Function should return the expected value");
        }

        @Test
        @DisplayName("Should return expected value with null input")
        void testFunctionWithNullInput() {
            // When
            Integer result = retryCondition.function().apply(null);

            // Then
            assertEquals(EXPECTED_VALUE, result,
                    "Function should handle null input and return the expected value");
        }
    }

    @Nested
    @DisplayName("Condition tests")
    class ConditionTests {
        @Test
        @DisplayName("Should return true for matching value")
        void testConditionWithMatchingValue() {
            // When
            boolean result = retryCondition.condition().test(EXPECTED_VALUE);

            // Then
            assertTrue(result, "Condition should return true for the expected value");
        }

        @ParameterizedTest
        @ValueSource(ints = {41, 43, 0, -1})
        @DisplayName("Should return false for non-matching values")
        void testConditionWithNonMatchingValues(int nonMatchingValue) {
            // When
            boolean result = retryCondition.condition().test(nonMatchingValue);

            // Then
            assertFalse(result,
                    "Condition should return false for values other than the expected value");
        }
    }

    @Test
    @DisplayName("Should construct and function correctly with complex predicates")
    void testWithComplexPredicates() {
        // Given
        Function<Object, String> stringFunction = obj -> "test";
        Predicate<String> complexPredicate = str -> str != null && str.length() > 2 && str.startsWith("t");
        RetryConditionImpl<String> complexCondition = new RetryConditionImpl<>(stringFunction, complexPredicate);

        // When
        String functionResult = complexCondition.function().apply(new Object());
        boolean predicateResult = complexCondition.condition().test(functionResult);

        // Then
        assertEquals("test", functionResult, "Function should return the expected string");
        assertTrue(predicateResult, "Complex predicate should evaluate to true for the function result");
    }
}