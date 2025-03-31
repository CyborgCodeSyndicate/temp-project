package com.theairebellion.zeus.validator.util;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionTarget;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import com.theairebellion.zeus.validator.exceptions.InvalidAssertionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssertionUtilTest {

    private static final String NAME_KEY = "name";
    private static final String AGE_KEY = "age";
    private static final String SCORE_KEY = "score";
    private static final String ITEMS_KEY = "items";
    private static final String ZEUS = "Zeus";

    // Mock target for testing
    private static final AssertionTarget TEST_TARGET = () -> TestEnum.TEST;

    @Nested
    @DisplayName("Input validation tests")
    class InputValidationTests {
        @Test
        @DisplayName("validate should throw if data is null")
        void validate_throwsIfDataIsNull() {
            var assertion = Assertion.builder()
                    .key(NAME_KEY)
                    .type(AssertionTypes.IS)
                    .expected(ZEUS)
                    .target(TEST_TARGET)
                    .build();

            var exception = assertThrows(IllegalArgumentException.class,
                    () -> AssertionUtil.validate(null, List.of(assertion)),
                    "Should throw when data is null");

            assertTrue(exception.getMessage().contains("data map cannot be null"),
                    "Exception message should indicate data is null");
        }

        @Test
        @DisplayName("validate should throw if empty list of assertions are provided")
        void validate_throwsIfEmptyListOfAssertions() {
            var data = Map.of(NAME_KEY, ZEUS);

            var exception = assertThrows(IllegalArgumentException.class,
                    () -> AssertionUtil.validate(data, List.of()),
                    "Should throw when empty list of assertions are provided");

            assertTrue(exception.getMessage().contains("At least one assertion"),
                    "Exception message should indicate no assertions");
        }

        @Test
        @DisplayName("validate should throw if assertions list is null")
        void validate_throwsIfAssertionsListIsNull() {
            var data = Map.of(NAME_KEY, ZEUS);

            List<Assertion> nullList = null;
            var exception = assertThrows(IllegalArgumentException.class,
                    () -> AssertionUtil.validate(data, nullList),
                    "Should throw when assertions list is null");

            assertTrue(exception.getMessage().contains("At least one assertion"),
                    "Exception message should indicate problem with assertions");
        }

        @Test
        @DisplayName("validate should throw if assertions is null")
        void validate_throwsIfAssertionsIsNull() {
            var data = Map.of(NAME_KEY, ZEUS);

            var exception = assertThrows(IllegalArgumentException.class,
                    () -> AssertionUtil.validate(data,  null),
                    "Should throw when assertion is null");

            assertTrue(exception.getMessage().contains("At least one assertion must be provided."),
                    "Exception message should indicate that at least single assertion needs to be provided");
        }

        @Test
        @DisplayName("validate should throw if assertion in list is null")
        void validate_throwsIfAssertionInListIsNull() {
            var data = Map.of(NAME_KEY, ZEUS);

            var assertion = Assertion.builder()
                                .key(NAME_KEY)
                                .type(AssertionTypes.IS)
                                .expected(ZEUS)
                                .target(TEST_TARGET)
                                .build();

            Assertion nullAssertion = null;
            List<Assertion> assertionList = new ArrayList<>();
            assertionList.add(assertion);
            assertionList.add(nullAssertion);

            var exception = assertThrows(InvalidAssertionException.class,
                () -> AssertionUtil.validate(data,  assertionList),
                "Should throw when there is null assertion in assertion list");

            assertTrue(exception.getMessage().contains("Assertion cannot be null."),
                "Exception message should indicate that null as assertion is not acceptable");
        }

        @Test
        @DisplayName("validate should throw if assertion has no key")
        void validate_throwsIfAssertionHasNoKey() {
            var data = Map.of(NAME_KEY, ZEUS);

            var assertion = Assertion.builder()
                    .type(AssertionTypes.IS)
                    .expected(ZEUS)
                    .target(TEST_TARGET)
                    .build();

            var exception = assertThrows(InvalidAssertionException.class,
                    () -> AssertionUtil.validate(data, List.of(assertion)),
                    "Should throw when assertion has no key");

            assertTrue(exception.getMessage().contains("non-empty key"),
                    "Exception message should indicate key problem");
        }

        @Test
        @DisplayName("validate should throw if assertion has empty key")
        void validate_throwsIfAssertionHasEmptyKey() {
            var data = Map.of(NAME_KEY, ZEUS);

            var assertion = Assertion.builder()
                    .key("")
                    .type(AssertionTypes.IS)
                    .expected(ZEUS)
                    .target(TEST_TARGET)
                    .build();

            var exception = assertThrows(InvalidAssertionException.class,
                    () -> AssertionUtil.validate(data, List.of(assertion)),
                    "Should throw when assertion has empty key");

            assertTrue(exception.getMessage().contains("non-empty key"),
                    "Exception message should indicate key problem");
        }

        // @Test
        // @DisplayName("validate should throw if assertion has no type")
        // void validate_throwsIfAssertionHasNoType() {
        //     var data = Map.of(NAME_KEY, ZEUS);
        //
        //     var assertion = Assertion.builder()
        //             .key(NAME_KEY)
        //             .expected(ZEUS)
        //             .target(TEST_TARGET)
        //             .build();
        //
        //     var exception = assertThrows(InvalidAssertionException.class,
        //             () -> AssertionUtil.validate(data, assertion),
        //             "Should throw when assertion has no type");
        //
        //     assertTrue(exception.getMessage().contains("non-null type"),
        //             "Exception message should indicate type problem");
        // }

        // @Test
        // @DisplayName("validate should throw if assertion has no target")
        // void validate_throwsIfAssertionHasNoTarget() {
        //     var data = Map.of(NAME_KEY, ZEUS);
        //
        //     var assertion = Assertion.builder()
        //             .key(NAME_KEY)
        //             .type(AssertionTypes.IS)
        //             .expected(ZEUS)
        //             .build();
        //
        //     var exception = assertThrows(InvalidAssertionException.class,
        //             () -> AssertionUtil.validate(data, assertion),
        //             "Should throw when assertion has no target");
        //
        //     assertTrue(exception.getMessage().contains("non-null target"),
        //             "Exception message should indicate target problem");
        // }

        // @Test
        // @DisplayName("validate should throw if assertion has no expected value")
        // void validate_throwsIfAssertionHasNoExpected() {
        //     var data = Map.of(NAME_KEY, ZEUS);
        //
        //     var assertion = Assertion.builder()
        //             .key(NAME_KEY)
        //             .type(AssertionTypes.IS)
        //             .target(TEST_TARGET)
        //             .build();
        //
        //     var exception = assertThrows(InvalidAssertionException.class,
        //             () -> AssertionUtil.validate(data, assertion),
        //             "Should throw when assertion has no expected value");
        //
        //     assertTrue(exception.getMessage().contains("non-null expected"),
        //             "Exception message should indicate expected value problem");
        // }

        @Test
        @DisplayName("validate should throw if key not found in data")
        void validate_throwsIfKeyNotExist() {
            var data = Map.of(AGE_KEY, 25);

            var assertion = Assertion.builder()
                    .key("missingKey")
                    .type(AssertionTypes.IS)
                    .expected(25)
                    .target(TEST_TARGET)
                    .build();

            var exception = assertThrows(IllegalArgumentException.class,
                    () -> AssertionUtil.validate(data, List.of(assertion)),
                    "Should throw when key not found in data");

            assertTrue(exception.getMessage().contains("does not exist"),
                    "Exception message should indicate key not found");
            assertTrue(exception.getMessage().contains(assertion.getKey()),
                "Exception message should have the missing key in the message");
        }

        @Test
        @DisplayName("validate should throw if type is incompatible with actual value")
        void validate_supportedTypeMismatch() {
            var data = Map.of(AGE_KEY, "Not a number");

            var assertion = Assertion.builder()
                    .key(AGE_KEY)
                    .type(AssertionTypes.GREATER_THAN)
                    .expected(100)
                    .target(TEST_TARGET)
                    .build();

            var exception = assertThrows(IllegalArgumentException.class,
                    () -> AssertionUtil.validate(data, List.of(assertion)),
                    "Should throw when type is incompatible");

            assertTrue(exception.getMessage().contains("not compatible"),
                    "Exception message should indicate type compatibility issue");
            assertTrue(exception.getMessage().contains(AssertionTypes.GREATER_THAN.name()),
                "Exception message should contain details for the Assertion Type");
            assertTrue(exception.getMessage().contains(String.class.getName()),
                "Exception message should contain the actual value type");
        }
    }

    @Nested
    @DisplayName("Validation functionality tests")
    class ValidationFunctionalityTests {
        @Test
        @DisplayName("validate should handle single successful assertion")
        void validate_singleAssertionSuccess() {
            // Given
            var data = Map.of(NAME_KEY, ZEUS);
            var assertion = Assertion.builder()
                    .key(NAME_KEY)
                    .type(AssertionTypes.IS)
                    .expected(ZEUS)
                    .target(TEST_TARGET)
                    .build();

            // When
            var results = AssertionUtil.validate(data, List.of(assertion));

            // Then
            assertEquals(1, results.size(), "Should return one result");
            var result = results.get(0);

            assertAll(
                    () -> assertTrue(result.isPassed(), "Validation should pass"),
                    () -> assertEquals("IS", result.getDescription(), "Description should match type name"),
                    () -> assertEquals(ZEUS, result.getExpectedValue(), "Expected value should match"),
                    () -> assertEquals(ZEUS, result.getActualValue(), "Actual value should match"),
                    () -> assertFalse(result.isSoft(), "Should not be soft assertion by default")
            );
        }

        @Test
        @DisplayName("validate should handle single failed assertion")
        void validate_singleAssertionFail() {
            // Given
            var data = Map.of(AGE_KEY, 25);
            var assertion = Assertion.builder()
                    .key(AGE_KEY)
                    .type(AssertionTypes.GREATER_THAN)
                    .expected(30)
                    .target(TEST_TARGET)
                    .build();

            // When
            var results = AssertionUtil.validate(data, List.of(assertion));

            // Then
            assertEquals(1, results.size(), "Should return one result");
            var result = results.get(0);

            assertAll(
                    () -> assertFalse(result.isPassed(), "Validation should fail"),
                    () -> assertEquals("GREATER_THAN", result.getDescription(), "Description should match type name"),
                    () -> assertEquals(30, result.getExpectedValue(), "Expected value should match"),
                    () -> assertEquals(25, result.getActualValue(), "Actual value should match")
            );
        }

        @Test
        @DisplayName("validate should handle multiple assertions")
        void validate_multipleAssertionsSuccess() {
            // Given
            var data = Map.of(NAME_KEY, ZEUS, AGE_KEY, 1000);

            var assertion1 = Assertion.builder()
                    .key(NAME_KEY)
                    .type(AssertionTypes.IS)
                    .expected(ZEUS)
                    .target(TEST_TARGET)
                    .build();

            var assertion2 = Assertion.builder()
                    .key(AGE_KEY)
                    .type(AssertionTypes.GREATER_THAN)
                    .expected(500)
                    .target(TEST_TARGET)
                    .soft(true)
                    .build();

            // When
            var results = AssertionUtil.validate(data, List.of(assertion1, assertion2));

            // Then
            assertEquals(2, results.size(), "Should return two results");

            assertAll(
                    () -> assertTrue(results.get(0).isPassed(), "First validation should pass"),
                    () -> assertFalse(results.get(0).isSoft(), "First validation should not be soft"),
                    () -> assertTrue(results.get(1).isPassed(), "Second validation should pass"),
                    () -> assertTrue(results.get(1).isSoft(), "Second validation should be soft")
            );
        }

        @Test
        @DisplayName("validate should handle mixed success/failure assertions")
        void validate_mixedResults() {
            // Given
            var data = new HashMap<String, Object>();
            data.put(NAME_KEY, ZEUS);
            data.put(AGE_KEY, 25);
            data.put(SCORE_KEY, 80);

            var assertion1 = Assertion.builder()
                    .key(NAME_KEY)
                    .type(AssertionTypes.IS)
                    .expected(ZEUS)
                    .target(TEST_TARGET)
                    .build();

            var assertion2 = Assertion.builder()
                    .key(AGE_KEY)
                    .type(AssertionTypes.GREATER_THAN)
                    .expected(100)
                    .target(TEST_TARGET)
                    .build();

            var assertion3 = Assertion.builder()
                    .key(SCORE_KEY)
                    .type(AssertionTypes.BETWEEN)
                    .expected(Arrays.asList(50, 100))
                    .target(TEST_TARGET)
                    .soft(true)
                    .build();

            // When
            var results = AssertionUtil.validate(data, List.of(assertion1, assertion2, assertion3));

            // Then
            assertEquals(3, results.size(), "Should return three results");

            assertAll(
                    () -> assertTrue(results.get(0).isPassed(), "First validation should pass"),
                    () -> assertFalse(results.get(1).isPassed(), "Second validation should fail"),
                    () -> assertTrue(results.get(2).isPassed(), "Third validation should pass"),
                    () -> assertTrue(results.get(2).isSoft(), "Third validation should be soft")
            );
        }

        @Test
        @DisplayName("validate should handle soft assertions")
        void validate_softAssertionFail() {
            // Given
            var data = Map.of(SCORE_KEY, 50);
            var assertion = Assertion.builder()
                    .key(SCORE_KEY)
                    .type(AssertionTypes.GREATER_THAN)
                    .expected(100)
                    .target(TEST_TARGET)
                    .soft(true)
                    .build();

            // When
            var results = AssertionUtil.validate(data, List.of(assertion));

            // Then
            assertEquals(1, results.size(), "Should return one result");
            var result = results.get(0);

            assertAll(
                    () -> assertFalse(result.isPassed(), "Validation should fail"),
                    () -> assertTrue(result.isSoft(), "Should be soft assertion"),
                    () -> assertEquals(100, result.getExpectedValue(), "Expected value should match"),
                    () -> assertEquals(50, result.getActualValue(), "Actual value should match")
            );
        }

        @Test
        @DisplayName("validate should handle collection type assertions")
        void validate_collectionTypeAssertions() {
            // Given
            var data = new HashMap<String, Object>();
            data.put(ITEMS_KEY, Arrays.asList("apple", "banana", "cherry"));

            var assertion1 = Assertion.builder()
                    .key(ITEMS_KEY)
                    .type(AssertionTypes.CONTAINS_ALL)
                    .expected(Arrays.asList("apple", "banana"))
                    .target(TEST_TARGET)
                    .build();

            var assertion2 = Assertion.builder()
                    .key(ITEMS_KEY)
                    .type(AssertionTypes.NOT_EMPTY)
                    .expected(true)
                    .target(TEST_TARGET)
                    .build();

            // When
            var results = AssertionUtil.validate(data, List.of(assertion1, assertion2));

            // Then
            assertEquals(2, results.size(), "Should return two results");

            assertAll(
                    () -> assertTrue(results.get(0).isPassed(), "CONTAINS_ALL validation should pass"),
                    () -> assertTrue(results.get(1).isPassed(), "NOT_EMPTY validation should pass")
            );
        }

        @Test
        @DisplayName("validate should handle string-specific assertions")
        void validate_stringAssertions() {
            // Given
            var data = Map.of(NAME_KEY, "Hello World");

            var assertion1 = Assertion.builder()
                    .key(NAME_KEY)
                    .type(AssertionTypes.STARTS_WITH)
                    .expected("Hello")
                    .target(TEST_TARGET)
                    .build();

            var assertion2 = Assertion.builder()
                    .key(NAME_KEY)
                    .type(AssertionTypes.ENDS_WITH)
                    .expected("World")
                    .target(TEST_TARGET)
                    .build();

            var assertion3 = Assertion.builder()
                    .key(NAME_KEY)
                    .type(AssertionTypes.CONTAINS)
                    .expected("lo Wo")
                    .target(TEST_TARGET)
                    .build();

            // When
            var results = AssertionUtil.validate(data, List.of(assertion1, assertion2, assertion3));

            // Then
            assertEquals(3, results.size(), "Should return three results");

            assertAll(
                    () -> assertTrue(results.get(0).isPassed(), "STARTS_WITH validation should pass"),
                    () -> assertTrue(results.get(1).isPassed(), "ENDS_WITH validation should pass"),
                    () -> assertTrue(results.get(2).isPassed(), "CONTAINS validation should pass")
            );
        }

        @Test
        @DisplayName("validate should handle regex assertions")
        void validate_regexAssertions() {
            // Given
            var data = Map.of(
                    "email", "user@example.com",
                    "phone", "123-456-7890"
            );

            var emailAssertion = Assertion.builder()
                    .key("email")
                    .type(AssertionTypes.MATCHES_REGEX)
                    .expected("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$")
                    .target(TEST_TARGET)
                    .build();

            var phoneAssertion = Assertion.builder()
                    .key("phone")
                    .type(AssertionTypes.MATCHES_REGEX)
                    .expected("\\d{3}-\\d{3}-\\d{4}")
                    .target(TEST_TARGET)
                    .build();

            // When
            var results = AssertionUtil.validate(data, List.of(emailAssertion, phoneAssertion));

            // Then
            assertEquals(2, results.size(), "Should return two results");

            assertAll(
                    () -> assertTrue(results.get(0).isPassed(), "Email regex validation should pass"),
                    () -> assertTrue(results.get(1).isPassed(), "Phone regex validation should pass")
            );
        }

        @Test
        @DisplayName("validate should handle numeric assertions")
        void validate_numericAssertions() {
            // Given
            var data = Map.of(
                    "score", 75,
                    "temperature", 98.6
            );

            var betweenAssertion = Assertion.builder()
                    .key("score")
                    .type(AssertionTypes.BETWEEN)
                    .expected(Arrays.asList(0, 100))
                    .target(TEST_TARGET)
                    .build();

            var greaterThanAssertion = Assertion.builder()
                    .key("temperature")
                    .type(AssertionTypes.GREATER_THAN)
                    .expected(98.0)
                    .target(TEST_TARGET)
                    .build();

            // When
            var results = AssertionUtil.validate(data, List.of(betweenAssertion, greaterThanAssertion));

            // Then
            assertEquals(2, results.size(), "Should return two results");

            assertAll(
                    () -> assertTrue(results.get(0).isPassed(), "BETWEEN validation should pass"),
                    () -> assertTrue(results.get(1).isPassed(), "GREATER_THAN validation should pass")
            );
        }

        @Test
        @DisplayName("validate should handle equals ignore case assertions")
        void validate_equalsIgnoreCaseAssertions() {
            // Given
            var data = Map.of("category", "Electronics");

            var assertion = Assertion.builder()
                    .key("category")
                    .type(AssertionTypes.EQUALS_IGNORE_CASE)
                    .expected("electronics")
                    .target(TEST_TARGET)
                    .build();

            // When
            var results = AssertionUtil.validate(data, List.of(assertion));

            // Then
            assertEquals(1, results.size(), "Should return one result");
            assertTrue(results.get(0).isPassed(), "EQUALS_IGNORE_CASE validation should pass");
        }

        @Test
        @DisplayName("validate should handle null or empty collection assertions")
        void validate_nullEmptyCollectionAssertions() {
            // Given
            var data = new HashMap<String, Object>();
            data.put("emptyList", Collections.emptyList());
            data.put("nonEmptyList", Arrays.asList("item"));
            data.put("nullValues", Arrays.asList(null, null));
            data.put("nonNullValues", Arrays.asList("a", "b"));

            var emptyAssertion = Assertion.builder()
                    .key("emptyList")
                    .type(AssertionTypes.EMPTY)
                    .expected(true)
                    .target(TEST_TARGET)
                    .build();

            var notEmptyAssertion = Assertion.builder()
                    .key("nonEmptyList")
                    .type(AssertionTypes.NOT_EMPTY)
                    .expected(true)
                    .target(TEST_TARGET)
                    .build();

            var allNullAssertion = Assertion.builder()
                    .key("nullValues")
                    .type(AssertionTypes.ALL_NULL)
                    .expected(true)
                    .target(TEST_TARGET)
                    .build();

            var allNotNullAssertion = Assertion.builder()
                    .key("nonNullValues")
                    .type(AssertionTypes.ALL_NOT_NULL)
                    .expected(true)
                    .target(TEST_TARGET)
                    .build();

            // When
            var results = AssertionUtil.validate(data, List.of(emptyAssertion, notEmptyAssertion,
                    allNullAssertion, allNotNullAssertion));

            // Then
            assertEquals(4, results.size(), "Should return four results");

            assertAll(
                    () -> assertTrue(results.get(0).isPassed(), "EMPTY validation should pass"),
                    () -> assertTrue(results.get(1).isPassed(), "NOT_EMPTY validation should pass"),
                    () -> assertTrue(results.get(2).isPassed(), "ALL_NULL validation should pass"),
                    () -> assertTrue(results.get(3).isPassed(), "ALL_NOT_NULL validation should pass")
            );
        }
    }

    // Test enum for custom target
    private enum TestEnum {
        TEST
    }
}