package com.theairebellion.zeus.validator.functions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AssertionFunctionsTest {

    @Test
    void testEquals_Positive() {
        assertAll(
                () -> assertTrue(AssertionFunctions.equals("abc", "abc")),
                () -> assertTrue(AssertionFunctions.equals(null, null))
        );
    }

    @Test
    void testEquals_Negative() {
        assertAll(
                () -> assertFalse(AssertionFunctions.equals("abc", "def")),
                () -> assertFalse(AssertionFunctions.equals("abc", null))
        );
    }

    @Test
    void testNotEquals() {
        assertAll(
                () -> assertTrue(AssertionFunctions.notEquals("abc", "def")),
                () -> assertFalse(AssertionFunctions.notEquals("abc", "abc"))
        );
    }

    @Test
    void testContains() {
        assertAll(
                () -> assertTrue(AssertionFunctions.contains("hello world", "world")),
                () -> assertFalse(AssertionFunctions.contains("hello", "bye")),
                () -> assertFalse(AssertionFunctions.contains(null, "something"))
        );
    }

    @Test
    void testNotNull() {
        assertAll(
                () -> assertTrue(AssertionFunctions.notNull("abc", null)),
                () -> assertFalse(AssertionFunctions.notNull(null, null))
        );
    }

    @Test
    void testIsNull() {
        assertAll(
                () -> assertTrue(AssertionFunctions.isNull(null, null)),
                () -> assertFalse(AssertionFunctions.isNull("not-null", null))
        );
    }

    @Test
    void testAllNotNull() {
        assertAll(
                () -> assertTrue(AssertionFunctions.allNotNull(Arrays.asList("a", "b"), null)),
                () -> assertFalse(AssertionFunctions.allNotNull(Arrays.asList("a", null, "c"), null)),
                () -> assertFalse(AssertionFunctions.allNotNull("not a list", null))
        );
    }

    @Test
    void testGreaterThan() {
        assertAll(
                () -> assertTrue(AssertionFunctions.greaterThan(5, 2)),
                () -> assertFalse(AssertionFunctions.greaterThan(2.0, 2.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> AssertionFunctions.greaterThan("notNumber", 2))
        );
    }

    @Test
    void testLessThan() {
        assertAll(
                () -> assertTrue(AssertionFunctions.lessThan(2, 5)),
                () -> assertFalse(AssertionFunctions.lessThan(5, 5)),
                () -> assertThrows(IllegalArgumentException.class, () -> AssertionFunctions.lessThan(5, "notNumber"))
        );
    }

    @Test
    void testContainsAll() {
        assertAll(
                () -> assertTrue(AssertionFunctions.containsAll(Arrays.asList("a", "b", "c"), Arrays.asList("a", "b"))),
                () -> assertFalse(AssertionFunctions.containsAll(Arrays.asList("a", "b"), Arrays.asList("a", "b", "c"))),
                () -> assertFalse(AssertionFunctions.containsAll("String", "String"))
        );
    }

    @Test
    void testContainsAny() {
        assertAll(
                () -> assertTrue(AssertionFunctions.containsAny(Arrays.asList("a", "b", "c"), Arrays.asList("x", "b"))),
                () -> assertFalse(AssertionFunctions.containsAny(Arrays.asList("a", "b"), Arrays.asList("x", "y")))
        );
    }

    @Test
    void testMatchesRegex() {
        assertAll(
                () -> assertTrue(AssertionFunctions.matchesRegex("abc123", "[a-z]+\\d+")),
                () -> assertFalse(AssertionFunctions.matchesRegex("abc", "\\d+")),
                () -> assertThrows(IllegalArgumentException.class, () -> AssertionFunctions.matchesRegex("abc", 123))
        );
    }

    @Test
    void testBetween() {
        assertAll(
                () -> assertTrue(AssertionFunctions.between(5, Arrays.asList(1, 10))),
                () -> assertFalse(AssertionFunctions.between(11, Arrays.asList(1, 10))),
                () -> assertThrows(IllegalArgumentException.class, () -> AssertionFunctions.between("foo", Arrays.asList(1, 5))),
                () -> assertThrows(IllegalArgumentException.class, () -> AssertionFunctions.between(5, "notAList")),
                () -> assertThrows(IllegalArgumentException.class, () -> AssertionFunctions.between(5, Arrays.asList(1, 2, 3)))
        );
    }

    @Test
    void testEqualsIgnoreCase() {
        assertAll(
                () -> assertTrue(AssertionFunctions.equalsIgnoreCase("ABC", "abc")),
                () -> assertFalse(AssertionFunctions.equalsIgnoreCase("Hello", "World")),
                () -> assertFalse(AssertionFunctions.equalsIgnoreCase(null, "abc"))
        );
    }
}