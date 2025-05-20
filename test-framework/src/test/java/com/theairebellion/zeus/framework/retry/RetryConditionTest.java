package com.theairebellion.zeus.framework.retry;

import com.theairebellion.zeus.framework.retry.mock.MockRetryCondition;
import java.util.function.Function;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RetryConditionTest {

   private static final int EXPECTED_VALUE = 42;
   private Function<Object, Integer> testFunction;
   private Predicate<Integer> testPredicate;
   private RetryCondition<Integer> retryCondition;

   @BeforeEach
   void setUp() {
      testFunction = obj -> EXPECTED_VALUE;
      testPredicate = val -> val == EXPECTED_VALUE;
      retryCondition = new MockRetryCondition<>(testFunction, testPredicate);
   }

   @Nested
   @DisplayName("RetryCondition.function() tests")
   class FunctionTests {
      @Test
      @DisplayName("Should return the expected value")
      void testFunctionReturnsExpectedValue() {
         // When
         Integer value = retryCondition.function().apply(new Object());

         // Then
         assertEquals(EXPECTED_VALUE, value,
               "Function should return the expected value");
      }

      @Test
      @DisplayName("Should return the expected value with null input")
      void testFunctionWithNullInput() {
         // When
         Integer value = retryCondition.function().apply(null);

         // Then
         assertEquals(EXPECTED_VALUE, value,
               "Function should handle null input and return the expected value");
      }

      @Test
      @DisplayName("Should return the function provided in constructor")
      void testFunctionIdentity() {
         // When
         Function<Object, Integer> returnedFunction = retryCondition.function();

         // Then
         assertNotNull(returnedFunction, "Returned function should not be null");
         assertSame(testFunction, returnedFunction,
               "Returned function should be the same instance provided to constructor");
      }
   }

   @Nested
   @DisplayName("RetryCondition.condition() tests")
   class ConditionTests {
      @Test
      @DisplayName("Should return true for matching value")
      void testConditionReturnsTrueForExpectedValue() {
         // When
         boolean result = retryCondition.condition().test(EXPECTED_VALUE);

         // Then
         assertTrue(result,
               "Condition should return true for the expected value");
      }

      @ParameterizedTest
      @ValueSource(ints = {41, 43, 0, -1})
      @DisplayName("Should return false for non-matching values")
      void testConditionReturnsFalseForUnexpectedValue(int nonMatchingValue) {
         // When
         boolean result = retryCondition.condition().test(nonMatchingValue);

         // Then
         assertFalse(result,
               "Condition should return false for values other than the expected value");
      }

      @Test
      @DisplayName("Should return the predicate provided in constructor")
      void testPredicateIdentity() {
         // When
         Predicate<Integer> returnedPredicate = retryCondition.condition();

         // Then
         assertNotNull(returnedPredicate, "Returned predicate should not be null");
         assertSame(testPredicate, returnedPredicate,
               "Returned predicate should be the same instance provided to constructor");
      }
   }

   @Test
   @DisplayName("Should work with different generic types")
   void testWithDifferentGenericTypes() {
      // Given
      Function<Object, String> stringFunction = obj -> "test";
      Predicate<String> stringPredicate = s -> s.equals("test");
      RetryCondition<String> stringCondition = new MockRetryCondition<>(stringFunction, stringPredicate);

      // When
      String result = stringCondition.function().apply(new Object());
      boolean predicateResult = stringCondition.condition().test(result);

      // Then
      assertEquals("test", result, "String function should return expected value");
      assertTrue(predicateResult, "String predicate should evaluate to true for the function result");
   }
}