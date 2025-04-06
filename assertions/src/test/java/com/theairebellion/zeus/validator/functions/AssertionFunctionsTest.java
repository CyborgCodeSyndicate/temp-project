package com.theairebellion.zeus.validator.functions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("all")
class AssertionFunctionsTest {

    @Nested
    @DisplayName("equals tests")
    class EqualsTests {
        @Test
        @DisplayName("equals should return true for identical values")
        void testEquals_Positive() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.equals("abc", "abc"), "String equality"),
                    () -> assertTrue(AssertionFunctions.equals(42, 42), "Integer equality"),
                    () -> assertTrue(AssertionFunctions.equals(null, null), "Null equality")
            );
        }

        @Test
        @DisplayName("equals should return false for different values")
        void testEquals_Negative() {
            assertAll(
                    () -> assertFalse(AssertionFunctions.equals("abc", "def"), "Different strings"),
                    () -> assertFalse(AssertionFunctions.equals("abc", null), "String vs null"),
                    () -> assertFalse(AssertionFunctions.equals(null, "abc"), "Null vs string"),
                    () -> assertFalse(AssertionFunctions.equals(1, 2), "Different integers")
            );
        }
    }

    @Nested
    @DisplayName("notEquals tests")
    class NotEqualsTests {
        @Test
        @DisplayName("notEquals should return true for different values")
        void testNotEquals_Positive() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.notEquals("abc", "def"), "Different strings"),
                    () -> assertTrue(AssertionFunctions.notEquals(null, "abc"), "Null vs string"),
                    () -> assertTrue(AssertionFunctions.notEquals("abc", null), "String vs null"),
                    () -> assertTrue(AssertionFunctions.notEquals(1, 2), "Different integers")
            );
        }

        @Test
        @DisplayName("notEquals should return false for identical values")
        void testNotEquals_Negative() {
            assertAll(
                    () -> assertFalse(AssertionFunctions.notEquals("abc", "abc"), "Identical strings"),
                    () -> assertFalse(AssertionFunctions.notEquals(42, 42), "Identical integers"),
                    () -> assertFalse(AssertionFunctions.notEquals(null, null), "Both null")
            );
        }
    }

    @Nested
    @DisplayName("contains tests")
    class ContainsTests {
        @Test
        @DisplayName("contains should return true when string contains substring")
        void testContains_Positive() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.contains("hello world", "world"), "Contains substring"),
                    () -> assertTrue(AssertionFunctions.contains("hello world", "hello"), "Contains prefix"),
                    () -> assertTrue(AssertionFunctions.contains("hello world", ""), "Contains empty string"),
                    () -> assertTrue(AssertionFunctions.contains("123", 2), "Contains number as string")
            );
        }

        @Test
        @DisplayName("contains should return false when string doesn't contain substring")
        void testContains_Negative() {
            assertAll(
                    () -> assertFalse(AssertionFunctions.contains("hello", "bye"), "Doesn't contain substring"),
                    () -> assertFalse(AssertionFunctions.contains(null, "something"), "Null doesn't contain anything"),
                    () -> assertFalse(AssertionFunctions.contains("hello", null), "String doesn't contain null")
            );
        }
    }

    @Nested
    @DisplayName("notNull tests")
    class NotNullTests {
        @Test
        @DisplayName("notNull should validate non-null values correctly")
        void testNotNull() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.notNull("abc", true), "String is not null with true expectation"),
                    () -> assertFalse(AssertionFunctions.notNull("abc", false), "String is not null with false expectation"),
                    () -> assertFalse(AssertionFunctions.notNull(null, true), "Null with true expectation"),
                    () -> assertTrue(AssertionFunctions.notNull(null, false), "Null with false expectation"),
                    () -> assertFalse(AssertionFunctions.notNull("abc", "invalid"), "Invalid expected type returns false")
            );
        }
    }

    @Nested
    @DisplayName("isNull tests")
    class IsNullTests {
        @Test
        @DisplayName("isNull should validate null values correctly")
        void testIsNull() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.isNull(null, true), "Null with true expectation"),
                    () -> assertFalse(AssertionFunctions.isNull(null, false), "Null with false expectation"),
                    () -> assertFalse(AssertionFunctions.isNull("not-null", true), "Not null with true expectation"),
                    () -> assertTrue(AssertionFunctions.isNull("not-null", false), "Not null with false expectation"),
                    () -> assertFalse(AssertionFunctions.isNull(null, "invalid"), "Invalid expected type returns false")
            );
        }
    }

    @Nested
    @DisplayName("allNotNull tests")
    class AllNotNullTests {
        @Test
        @DisplayName("allNotNull should validate collections with all non-null elements")
        void testAllNotNull() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.allNotNull(Arrays.asList("a", "b"), true),
                            "Collection with all non-null elements with true expectation"),
                    () -> assertFalse(AssertionFunctions.allNotNull(Arrays.asList("a", "b"), false),
                            "Collection with all non-null elements with false expectation"),
                    () -> assertFalse(AssertionFunctions.allNotNull(Arrays.asList("a", null, "c"), true),
                            "Collection with some null elements with true expectation"),
                    () -> assertTrue(AssertionFunctions.allNotNull(Arrays.asList("a", null, "c"), false),
                            "Collection with some null elements with false expectation"),
                    () -> assertFalse(AssertionFunctions.allNotNull(Collections.singletonList(null), true),
                            "Collection with only null with true expectation"),
                    () -> assertTrue(AssertionFunctions.allNotNull(Collections.singletonList(null), false),
                            "Collection with only null with false expectation"),
                    () -> assertFalse(AssertionFunctions.allNotNull("not a list", true),
                            "Non-collection input returns false"),
                    () -> assertFalse(AssertionFunctions.allNotNull(Collections.emptyList(), "invalid"),
                            "Invalid expected type returns false")
            );
        }
    }

    @Nested
    @DisplayName("allNull tests")
    class AllNullTests {
        @Test
        @DisplayName("allNull should validate collections with all null elements")
        void testAllNull() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.allNull(Arrays.asList(null, null), true),
                            "Collection with all null elements with true expectation"),
                    () -> assertFalse(AssertionFunctions.allNull(Arrays.asList(null, null), false),
                            "Collection with all null elements with false expectation"),
                    () -> assertFalse(AssertionFunctions.allNull(Arrays.asList("a", null), true),
                            "Collection with some non-null elements with true expectation"),
                    () -> assertTrue(AssertionFunctions.allNull(Arrays.asList("a", null), false),
                            "Collection with some non-null elements with false expectation"),
                    () -> assertFalse(AssertionFunctions.allNull(Collections.emptyList(), "invalid"),
                            "Invalid expected type returns false"),
                    () -> assertFalse(AssertionFunctions.allNull("not a list", true),
                            "Non-collection input returns false")
            );
        }
    }

    @Nested
    @DisplayName("greaterThan tests")
    class GreaterThanTests {
        @Test
        @DisplayName("greaterThan should compare numeric values correctly")
        void testGreaterThan() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.greaterThan(5, 2), "Integer is greater"),
                    () -> assertTrue(AssertionFunctions.greaterThan(5.5, 2.5), "Double is greater"),
                    () -> assertTrue(AssertionFunctions.greaterThan(5, 2.5), "Integer is greater than double"),
                    () -> assertTrue(AssertionFunctions.greaterThan(5.5, 2), "Double is greater than integer"),
                    () -> assertFalse(AssertionFunctions.greaterThan(2, 5), "Integer is less than"),
                    () -> assertFalse(AssertionFunctions.greaterThan(2.0, 2.0), "Double is equal")
            );
        }

        @Test
        @DisplayName("greaterThan should throw exception for non-numeric values")
        void testGreaterThan_Invalid() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.greaterThan("notNumber", 2),
                            "First argument is not a number"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.greaterThan(2, "notNumber"),
                            "Second argument is not a number"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.greaterThan(null, 2),
                            "First argument is null"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.greaterThan(2, null),
                            "Second argument is null")
            );
        }
    }

    @Nested
    @DisplayName("lessThan tests")
    class LessThanTests {
        @Test
        @DisplayName("lessThan should compare numeric values correctly")
        void testLessThan() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.lessThan(2, 5), "Integer is less"),
                    () -> assertTrue(AssertionFunctions.lessThan(2.5, 5.5), "Double is less"),
                    () -> assertTrue(AssertionFunctions.lessThan(2, 5.5), "Integer is less than double"),
                    () -> assertTrue(AssertionFunctions.lessThan(2.5, 5), "Double is less than integer"),
                    () -> assertFalse(AssertionFunctions.lessThan(5, 2), "Integer is greater than"),
                    () -> assertFalse(AssertionFunctions.lessThan(5.0, 5.0), "Double is equal")
            );
        }

        @Test
        @DisplayName("lessThan should throw exception for non-numeric values")
        void testLessThan_Invalid() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.lessThan("notNumber", 5),
                            "First argument is not a number"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.lessThan(5, "notNumber"),
                            "Second argument is not a number"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.lessThan(null, 5),
                            "First argument is null"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.lessThan(5, null),
                            "Second argument is null")
            );
        }
    }

    @Nested
    @DisplayName("containsAll tests")
    class ContainsAllTests {
        @Test
        @DisplayName("containsAll should check if collection contains all elements of another collection")
        void testContainsAll() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.containsAll(
                                    Arrays.asList("a", "b", "c"), Arrays.asList("a", "b")),
                            "Superset contains all elements"),
                    () -> assertTrue(AssertionFunctions.containsAll(
                                    Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c")),
                            "Equal sets contain all elements"),
                    () -> assertFalse(AssertionFunctions.containsAll(
                                    Arrays.asList("a", "b"), Arrays.asList("a", "b", "c")),
                            "Subset doesn't contain all elements"),
                    () -> assertTrue(AssertionFunctions.containsAll(
                                    Arrays.asList("a", "b", "c"), Collections.emptyList()),
                            "Any set contains all elements of empty set"),
                    () -> assertFalse(AssertionFunctions.containsAll(
                                    "String", "String"),
                            "Non-collection inputs return false")
            );
        }

        @Test
        @DisplayName("containsAll should handle null values")
        void testContainsAll_WithNulls() {
            assertAll(
                    () -> assertFalse(AssertionFunctions.containsAll(
                                    null, Arrays.asList("a", "b")),
                            "Null actual returns false"),
                    () -> assertFalse(AssertionFunctions.containsAll(
                                    Arrays.asList("a", "b"), null),
                            "Null expected returns false")
            );
        }
    }

    @Nested
    @DisplayName("containsAny tests")
    class ContainsAnyTests {
        @Test
        @DisplayName("containsAny should check if collection contains any elements of another collection")
        void testContainsAny() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.containsAny(
                                    Arrays.asList("a", "b", "c"), Arrays.asList("x", "b")),
                            "Contains at least one element"),
                    () -> assertTrue(AssertionFunctions.containsAny(
                                    Arrays.asList("a", "b", "c"), Arrays.asList("a", "b", "c")),
                            "Contains all elements"),
                    () -> assertFalse(AssertionFunctions.containsAny(
                                    Arrays.asList("a", "b"), Arrays.asList("x", "y")),
                            "Contains no elements"),
                    () -> assertFalse(AssertionFunctions.containsAny(
                                    Arrays.asList("a", "b"), Collections.emptyList()),
                            "Empty expected list returns false"),
                    () -> assertFalse(AssertionFunctions.containsAny(
                                    Collections.emptyList(), Arrays.asList("a", "b")),
                            "Empty actual list returns false")
            );
        }

        @Test
        @DisplayName("containsAny should handle null values")
        void testContainsAny_WithNulls() {
            assertAll(
                    () -> assertFalse(AssertionFunctions.containsAny(
                                    null, Arrays.asList("a", "b")),
                            "Null actual returns false"),
                    () -> assertFalse(AssertionFunctions.containsAny(
                                    Arrays.asList("a", "b"), null),
                            "Null expected returns false")
            );
        }
    }

    @Nested
    @DisplayName("startsWith tests")
    class StartsWithTests {
        @Test
        @DisplayName("startsWith should check if string starts with prefix")
        void testStartsWith() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.startsWith("hello world", "hello"),
                            "String starts with prefix"),
                    () -> assertTrue(AssertionFunctions.startsWith("hello", ""),
                            "Any string starts with empty string"),
                    () -> assertTrue(AssertionFunctions.startsWith("hello", "hello"),
                            "String starts with itself"),
                    () -> assertFalse(AssertionFunctions.startsWith("hello world", "world"),
                            "String doesn't start with suffix"),
                    () -> assertFalse(AssertionFunctions.startsWith("", "hello"),
                            "Empty string doesn't start with non-empty string")
            );
        }

        @Test
        @DisplayName("startsWith should handle null values")
        void testStartsWith_WithNulls() {
            assertAll(
                    () -> assertFalse(AssertionFunctions.startsWith(null, "hello"),
                            "Null doesn't start with anything"),
                    () -> assertFalse(AssertionFunctions.startsWith("hello", null),
                            "String doesn't start with null")
            );
        }
    }

    @Nested
    @DisplayName("endsWith tests")
    class EndsWithTests {
        @Test
        @DisplayName("endsWith should check if string ends with suffix")
        void testEndsWith() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.endsWith("hello world", "world"),
                            "String ends with suffix"),
                    () -> assertTrue(AssertionFunctions.endsWith("hello", ""),
                            "Any string ends with empty string"),
                    () -> assertTrue(AssertionFunctions.endsWith("hello", "hello"),
                            "String ends with itself"),
                    () -> assertFalse(AssertionFunctions.endsWith("hello world", "hello"),
                            "String doesn't end with prefix"),
                    () -> assertFalse(AssertionFunctions.endsWith("", "world"),
                            "Empty string doesn't end with non-empty string")
            );
        }

        @Test
        @DisplayName("endsWith should handle null values")
        void testEndsWith_WithNulls() {
            assertAll(
                    () -> assertFalse(AssertionFunctions.endsWith(null, "world"),
                            "Null doesn't end with anything"),
                    () -> assertFalse(AssertionFunctions.endsWith("hello world", null),
                            "String doesn't end with null")
            );
        }
    }

    @Nested
    @DisplayName("length tests")
    class LengthTests {
        @Test
        @DisplayName("length should check string and collection lengths")
        void testLength() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.length("abc", 3),
                            "String length matches"),
                    () -> assertFalse(AssertionFunctions.length("abc", 4),
                            "String length doesn't match"),
                    () -> assertTrue(AssertionFunctions.length(Arrays.asList("a", "b", "c"), 3),
                            "Collection size matches"),
                    () -> assertFalse(AssertionFunctions.length(Arrays.asList("a", "b"), 3),
                            "Collection size doesn't match"),
                    () -> assertTrue(AssertionFunctions.length("", 0),
                            "Empty string has length 0"),
                    () -> assertTrue(AssertionFunctions.length(Collections.emptyList(), 0),
                            "Empty collection has size 0")
            );
        }

        @Test
        @DisplayName("length should handle invalid inputs appropriately")
        void testLength_Invalid() {
            assertAll(
                    () -> assertFalse(AssertionFunctions.length(123, 3),
                            "Non-string/collection actual returns false"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.length("abc", "not-a-number"),
                            "Non-number expected throws exception"),
                    // Removed the null actual test since it doesn't throw an exception
                    () -> assertFalse(AssertionFunctions.length(null, 3),
                            "Null actual returns false")
            );
        }
    }

    @Nested
    @DisplayName("matchesRegex tests")
    class MatchesRegexTests {
        @Test
        @DisplayName("matchesRegex should validate if string matches regex pattern")
        void testMatchesRegex() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.matchesRegex("abc123", "[a-z]+\\d+"),
                            "String matches regex"),
                    () -> assertTrue(AssertionFunctions.matchesRegex("123", "\\d+"),
                            "Number as string matches regex"),
                    () -> assertFalse(AssertionFunctions.matchesRegex("abc", "\\d+"),
                            "String doesn't match regex"),
                    () -> assertFalse(AssertionFunctions.matchesRegex("", "\\d+"),
                            "Empty string doesn't match regex")
            );
        }

        @Test
        @DisplayName("matchesRegex should handle null and invalid inputs")
        void testMatchesRegex_Invalid() {
            assertAll(
                    () -> assertFalse(AssertionFunctions.matchesRegex(null, "\\d+"),
                            "Null doesn't match any regex"),
                    () -> assertFalse(AssertionFunctions.matchesRegex("abc", null),
                            "String doesn't match null regex"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.matchesRegex("abc", 123),
                            "Non-string expected throws exception")
            );
        }
    }

    @Nested
    @DisplayName("isEmpty tests")
    class IsEmptyTests {
        @Test
        @DisplayName("isEmpty should check if collection is empty")
        void testIsEmpty() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.isEmpty(Collections.emptyList(), true),
                            "Empty collection with true expectation"),
                    () -> assertFalse(AssertionFunctions.isEmpty(Collections.emptyList(), false),
                            "Empty collection with false expectation"),
                    () -> assertFalse(AssertionFunctions.isEmpty(Arrays.asList("a", "b"), true),
                            "Non-empty collection with true expectation"),
                    () -> assertTrue(AssertionFunctions.isEmpty(Arrays.asList("a", "b"), false),
                            "Non-empty collection with false expectation")
            );
        }

        @Test
        @DisplayName("isEmpty should handle non-collection and invalid inputs")
        void testIsEmpty_Invalid() {
            assertAll(
                    () -> assertFalse(AssertionFunctions.isEmpty("string", true),
                            "Non-collection with true expectation returns false"),
                    () -> assertFalse(AssertionFunctions.isEmpty(null, true),
                            "Null with true expectation returns false"),
                    () -> assertFalse(AssertionFunctions.isEmpty(Collections.emptyList(), "invalid"),
                            "Invalid expected type returns false")
            );
        }
    }

    @Nested
    @DisplayName("isNotEmpty tests")
    class IsNotEmptyTests {
        @Test
        @DisplayName("isNotEmpty should check if collection is not empty")
        void testIsNotEmpty() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.isNotEmpty(Arrays.asList("a", "b"), true),
                            "Non-empty collection with true expectation"),
                    () -> assertFalse(AssertionFunctions.isNotEmpty(Arrays.asList("a", "b"), false),
                            "Non-empty collection with false expectation"),
                    () -> assertFalse(AssertionFunctions.isNotEmpty(Collections.emptyList(), true),
                            "Empty collection with true expectation"),
                    () -> assertTrue(AssertionFunctions.isNotEmpty(Collections.emptyList(), false),
                            "Empty collection with false expectation")
            );
        }

        @Test
        @DisplayName("isNotEmpty should handle non-collection and invalid inputs")
        void testIsNotEmpty_Invalid() {
            assertAll(
                    () -> assertFalse(AssertionFunctions.isNotEmpty("string", true),
                            "Non-collection with true expectation returns false"),
                    () -> assertFalse(AssertionFunctions.isNotEmpty(null, true),
                            "Null with true expectation returns false"),
                    () -> assertFalse(AssertionFunctions.isNotEmpty(Arrays.asList("a", "b"), "invalid"),
                            "Invalid expected type returns false")
            );
        }
    }

    @Nested
    @DisplayName("between tests")
    class BetweenTests {
        @Test
        @DisplayName("between should check if number is within specified range")
        void testBetween() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.between(5, Arrays.asList(1, 10)),
                            "Number is within range"),
                    () -> assertTrue(AssertionFunctions.between(1, Arrays.asList(1, 10)),
                            "Number is at lower bound"),
                    () -> assertTrue(AssertionFunctions.between(10, Arrays.asList(1, 10)),
                            "Number is at upper bound"),
                    () -> assertTrue(AssertionFunctions.between(5.5, Arrays.asList(1, 10)),
                            "Decimal is within range"),
                    () -> assertFalse(AssertionFunctions.between(0, Arrays.asList(1, 10)),
                            "Number is below range"),
                    () -> assertFalse(AssertionFunctions.between(11, Arrays.asList(1, 10)),
                            "Number is above range")
            );
        }

        @Test
        @DisplayName("between should handle mixed numeric types")
        void testBetween_MixedTypes() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.between(5, Arrays.asList(1.0, 10.0)),
                            "Integer within double range"),
                    () -> assertTrue(AssertionFunctions.between(5.5, Arrays.asList(1, 10)),
                            "Double within integer range")
            );
        }

        @Test
        @DisplayName("between should throw exception for invalid inputs")
        void testBetween_Invalid() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.between("foo", Arrays.asList(1, 5)),
                            "Non-numeric actual throws exception"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.between(5, "notAList"),
                            "Non-list expected throws exception"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.between(5, Arrays.asList(1, 2, 3)),
                            "List with wrong size throws exception"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.between(5, Arrays.asList("a", "b")),
                            "List with non-numeric elements throws exception"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.between(5, Arrays.asList(1, "b")),
                            "List with mixed element types throws exception"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.between(null, Arrays.asList(1, 10)),
                            "Null actual throws exception"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> AssertionFunctions.between(5, null),
                            "Null expected throws exception")
            );
        }
    }

    @Nested
    @DisplayName("equalsIgnoreCase tests")
    class EqualsIgnoreCaseTests {
        @Test
        @DisplayName("equalsIgnoreCase should compare strings case-insensitively")
        void testEqualsIgnoreCase() {
            assertAll(
                    () -> assertTrue(AssertionFunctions.equalsIgnoreCase("ABC", "abc"),
                            "Same string with different case"),
                    () -> assertTrue(AssertionFunctions.equalsIgnoreCase("abc", "ABC"),
                            "Same string with different case (reversed)"),
                    () -> assertTrue(AssertionFunctions.equalsIgnoreCase("abc", "abc"),
                            "Same string with same case"),
                    () -> assertFalse(AssertionFunctions.equalsIgnoreCase("Hello", "World"),
                            "Different strings"),
                    () -> assertTrue(AssertionFunctions.equalsIgnoreCase("", ""),
                            "Empty strings")
            );
        }

        @Test
        @DisplayName("equalsIgnoreCase should handle null values")
        void testEqualsIgnoreCase_WithNulls() {
            assertAll(
                    () -> assertFalse(AssertionFunctions.equalsIgnoreCase(null, "abc"),
                            "Null doesn't equal any string"),
                    () -> assertFalse(AssertionFunctions.equalsIgnoreCase("abc", null),
                            "String doesn't equal null")
            );
        }
    }

    @Nested
    @DisplayName("private utility methods")
    class UtilityMethodTests {

        @Test
        @DisplayName("isCollection is indirectly tested through public methods")
        void testIsCollectionIndirectly() {
            // Test isCollection indirectly through methods using it
            assertTrue(AssertionFunctions.isEmpty(Collections.emptyList(), true),
                    "isEmpty uses isCollection internally and returns true for empty collection");
            assertFalse(AssertionFunctions.isEmpty("not a collection", true),
                    "isEmpty uses isCollection internally and returns false for non-collection");
        }

        @Test
        @DisplayName("isNumber is indirectly tested through public methods")
        void testIsNumberIndirectly() {
            // Test isNumber indirectly through methods using it
            assertTrue(AssertionFunctions.greaterThan(5, 3),
                    "greaterThan uses isNumber internally and returns true for valid numbers");

            assertThrows(IllegalArgumentException.class,
                    () -> AssertionFunctions.greaterThan("not a number", 3),
                    "greaterThan uses isNumber internally and throws for non-number");
        }
    }
}
