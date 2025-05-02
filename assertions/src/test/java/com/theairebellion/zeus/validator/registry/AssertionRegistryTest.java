package com.theairebellion.zeus.validator.registry;

import com.theairebellion.zeus.validator.core.AssertionType;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.BiPredicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssertionRegistryTest {

   @Nested
   @DisplayName("Built-in validators tests")
   class BuiltInValidatorsTests {
      @Test
      @DisplayName("Registry should provide validators for basic types")
      void testGetValidator_BasicTypes() {
         var isValidator = AssertionRegistry.getValidator(AssertionTypes.IS);
         var notValidator = AssertionRegistry.getValidator(AssertionTypes.NOT);
         var containsValidator = AssertionRegistry.getValidator(AssertionTypes.CONTAINS);

         assertAll(
               () -> assertNotNull(isValidator, "IS validator should exist"),
               () -> assertTrue(isValidator.test("abc", "abc"), "IS validator should work correctly"),
               () -> assertFalse(isValidator.test("abc", "xyz"), "IS validator should reject unequal values"),

               () -> assertNotNull(notValidator, "NOT validator should exist"),
               () -> assertTrue(notValidator.test("abc", "xyz"), "NOT validator should work correctly"),
               () -> assertFalse(notValidator.test("abc", "abc"), "NOT validator should reject equal values"),

               () -> assertNotNull(containsValidator, "CONTAINS validator should exist"),
               () -> assertTrue(containsValidator.test("hello world", "world"), "CONTAINS validator should work correctly"),
               () -> assertFalse(containsValidator.test("hello", "world"), "CONTAINS validator should reject non-containing values")
         );
      }

      @ParameterizedTest
      @EnumSource(AssertionTypes.class)
      @DisplayName("Registry should have validators for all AssertionTypes")
      void testAllAssertionTypesRegistered(AssertionTypes type) {
         var validator = AssertionRegistry.getValidator(type);
         assertNotNull(validator, "Should have a validator for: " + type);
      }
   }

   @Nested
   @DisplayName("Custom validators tests")
   class CustomValidatorsTests {
      @Test
      @DisplayName("Registry should throw exception for unknown types")
      void testGetValidator_ThrowsForUnknownType() {
         var unknownType = new AssertionType() {
            @Override
            public Enum<?> type() {
               return TestEnum.UNKNOWN;
            }

            @Override
            public Class<?> getSupportedType() {
               return Object.class;
            }
         };

         var exception = assertThrows(IllegalArgumentException.class,
               () -> AssertionRegistry.getValidator(unknownType),
               "Should throw exception for unknown type");

         assertTrue(exception.getMessage().contains("No validator registered"),
               "Exception message should indicate the problem");

         assertTrue(exception.getMessage().contains(unknownType.type().name()),
               "Exception message should contain the not registered assertion");
      }

      @Test
      @DisplayName("Registry should allow registration of custom assertions")
      void testRegisterCustomAssertion() {
         // Given
         var customType = new AssertionType() {
            @Override
            public Enum<?> type() {
               return TestEnum.CUSTOM;
            }

            @Override
            public Class<?> getSupportedType() {
               return Object.class;
            }
         };
         BiPredicate<Object, Object> customFunc = (a, e) -> "custom".equals(a);

         // When
         AssertionRegistry.registerCustomAssertion(customType, customFunc);
         var retrieved = AssertionRegistry.getValidator(customType);

         // Then
         assertSame(customFunc, retrieved, "Retrieved validator should be the same as registered");
         assertTrue(retrieved.test("custom", null), "Custom validator should work as expected");
         assertFalse(retrieved.test("not-custom", null), "Custom validator should reject non-matching values");
      }

      @Test
      @DisplayName("Registry should reject null parameters for custom assertions")
      void testRegisterCustomAssertion_RejectsNulls() {
         // Given
         var customType = new AssertionType() {
            @Override
            public Enum<?> type() {
               return TestEnum.CUSTOM;
            }

            @Override
            public Class<?> getSupportedType() {
               return Object.class;
            }
         };
         BiPredicate<Object, Object> customFunc = (a, e) -> true;

         // Then
         assertAll(
               () -> assertThrows(NullPointerException.class,
                     () -> AssertionRegistry.registerCustomAssertion(null, customFunc),
                     "Should reject null type"),
               () -> assertThrows(NullPointerException.class,
                     () -> AssertionRegistry.registerCustomAssertion(customType, null),
                     "Should reject null validator function")
         );
      }

      @Test
      @DisplayName("Registry should allow overriding built-in validators")
      void testOverrideBuiltInValidator() {
         // Given
         BiPredicate<Object, Object> customIsValidator = (a, e) -> "override".equals(a);

         // When
         AssertionRegistry.registerCustomAssertion(AssertionTypes.IS, customIsValidator);
         var validator = AssertionRegistry.getValidator(AssertionTypes.IS);

         // Then
         assertTrue(validator.test("override", null), "Overridden validator should work as expected");
         assertFalse(validator.test("abc", "abc"), "Original behavior should be replaced");

         // Reset for other tests
         AssertionRegistry.registerCustomAssertion(AssertionTypes.IS, (a, e) -> a != null && a.equals(e));
      }
   }

   @Nested
   @DisplayName("Validator behavior tests")
   class ValidatorBehaviorTests {
      @Test
      @DisplayName("Null checking validators should work correctly")
      void testNullValidators() {
         var notNullValidator = AssertionRegistry.getValidator(AssertionTypes.NOT_NULL);
         var isNullValidator = AssertionRegistry.getValidator(AssertionTypes.IS_NULL);

         assertAll(
               () -> assertTrue(notNullValidator.test("not-null", true), "NOT_NULL with non-null value and true expectation"),
               () -> assertFalse(notNullValidator.test(null, true), "NOT_NULL with null value and true expectation"),

               () -> assertTrue(isNullValidator.test(null, true), "IS_NULL with null value and true expectation"),
               () -> assertFalse(isNullValidator.test("not-null", true), "IS_NULL with non-null value and true expectation")
         );
      }

      @Test
      @DisplayName("Collection validators should work correctly")
      void testCollectionValidators() {
         var emptyValidator = AssertionRegistry.getValidator(AssertionTypes.EMPTY);
         var notEmptyValidator = AssertionRegistry.getValidator(AssertionTypes.NOT_EMPTY);
         var containsAllValidator = AssertionRegistry.getValidator(AssertionTypes.CONTAINS_ALL);
         var containsAnyValidator = AssertionRegistry.getValidator(AssertionTypes.CONTAINS_ANY);

         assertAll(
               () -> assertTrue(emptyValidator.test(Collections.emptyList(), true),
                     "EMPTY with empty collection and true expectation"),
               () -> assertFalse(emptyValidator.test(Arrays.asList(1, 2), true),
                     "EMPTY with non-empty collection and true expectation"),

               () -> assertTrue(notEmptyValidator.test(Arrays.asList(1, 2), true),
                     "NOT_EMPTY with non-empty collection and true expectation"),
               () -> assertFalse(notEmptyValidator.test(Collections.emptyList(), true),
                     "NOT_EMPTY with empty collection and true expectation"),

               () -> assertTrue(containsAllValidator.test(Arrays.asList(1, 2, 3), Arrays.asList(1, 2)),
                     "CONTAINS_ALL with superset and subset"),
               () -> assertFalse(containsAllValidator.test(Arrays.asList(1, 2), Arrays.asList(1, 2, 3)),
                     "CONTAINS_ALL with subset and superset"),

               () -> assertTrue(containsAnyValidator.test(Arrays.asList(1, 2, 3), Arrays.asList(3, 4, 5)),
                     "CONTAINS_ANY with overlapping sets"),
               () -> assertFalse(containsAnyValidator.test(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6)),
                     "CONTAINS_ANY with disjoint sets")
         );
      }

      @Test
      @DisplayName("String validators should work correctly")
      void testStringValidators() {
         var startsWithValidator = AssertionRegistry.getValidator(AssertionTypes.STARTS_WITH);
         var endsWithValidator = AssertionRegistry.getValidator(AssertionTypes.ENDS_WITH);
         var equalsIgnoreCaseValidator = AssertionRegistry.getValidator(AssertionTypes.EQUALS_IGNORE_CASE);
         var matchesRegexValidator = AssertionRegistry.getValidator(AssertionTypes.MATCHES_REGEX);

         assertAll(
               () -> assertTrue(startsWithValidator.test("hello world", "hello"),
                     "STARTS_WITH with matching prefix"),
               () -> assertFalse(startsWithValidator.test("hello world", "world"),
                     "STARTS_WITH with non-matching prefix"),

               () -> assertTrue(endsWithValidator.test("hello world", "world"),
                     "ENDS_WITH with matching suffix"),
               () -> assertFalse(endsWithValidator.test("hello world", "hello"),
                     "ENDS_WITH with non-matching suffix"),

               () -> assertTrue(equalsIgnoreCaseValidator.test("Hello", "hello"),
                     "EQUALS_IGNORE_CASE with case difference"),
               () -> assertFalse(equalsIgnoreCaseValidator.test("hello", "world"),
                     "EQUALS_IGNORE_CASE with different strings"),

               () -> assertTrue(matchesRegexValidator.test("abc123", "\\w+\\d+"),
                     "MATCHES_REGEX with matching pattern"),
               () -> assertFalse(matchesRegexValidator.test("abc", "\\d+"),
                     "MATCHES_REGEX with non-matching pattern")
         );
      }

      @Test
      @DisplayName("Numeric validators should work correctly")
      void testNumericValidators() {
         var greaterThanValidator = AssertionRegistry.getValidator(AssertionTypes.GREATER_THAN);
         var lessThanValidator = AssertionRegistry.getValidator(AssertionTypes.LESS_THAN);
         var betweenValidator = AssertionRegistry.getValidator(AssertionTypes.BETWEEN);

         assertAll(
               () -> assertTrue(greaterThanValidator.test(5, 3),
                     "GREATER_THAN with larger actual"),
               () -> assertFalse(greaterThanValidator.test(3, 5),
                     "GREATER_THAN with smaller actual"),

               () -> assertTrue(lessThanValidator.test(3, 5),
                     "LESS_THAN with smaller actual"),
               () -> assertFalse(lessThanValidator.test(5, 3),
                     "LESS_THAN with larger actual"),

               () -> assertTrue(betweenValidator.test(5, Arrays.asList(1, 10)),
                     "BETWEEN with value in range"),
               () -> assertFalse(betweenValidator.test(15, Arrays.asList(1, 10)),
                     "BETWEEN with value outside range")
         );
      }
   }

   // Test enum for custom types
   private enum TestEnum {
      UNKNOWN,
      CUSTOM
   }
}