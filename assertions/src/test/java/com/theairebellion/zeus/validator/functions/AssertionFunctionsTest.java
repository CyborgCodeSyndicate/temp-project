package com.theairebellion.zeus.validator.functions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionFunctionsTest {

    @Test
    void testEquals_Positive() {
        assertTrue(AssertionFunctions.equals("abc", "abc"));
        assertTrue(AssertionFunctions.equals(null, null));
    }

    @Test
    void testEquals_Negative() {
        assertFalse(AssertionFunctions.equals("abc", "def"));
        assertFalse(AssertionFunctions.equals("abc", null));
    }

    @Test
    void testNotEquals() {
        assertTrue(AssertionFunctions.notEquals("abc", "def"));
        assertFalse(AssertionFunctions.notEquals("abc", "abc"));
    }

    @Test
    void testContains() {
        assertTrue(AssertionFunctions.contains("hello world", "world"));
        assertFalse(AssertionFunctions.contains("hello", "bye"));
        assertFalse(AssertionFunctions.contains(null, "something"));
    }

    @Test
    void testNotNull() {
        assertTrue(AssertionFunctions.notNull("abc", null));
        assertFalse(AssertionFunctions.notNull(null, null));
    }

    @Test
    void testIsNull() {
        assertTrue(AssertionFunctions.isNull(null, null));
        assertFalse(AssertionFunctions.isNull("not-null", null));
    }

    @Test
    void testAllNotNull() {
        assertTrue(AssertionFunctions.allNotNull(Arrays.asList("a", "b"), null));
        assertFalse(AssertionFunctions.allNotNull(Arrays.asList("a", null, "c"), null));
        assertFalse(AssertionFunctions.allNotNull("not a list", null));
    }

    @Test
    void testAllNull() {
        assertTrue(AssertionFunctions.allNull(Arrays.asList(null, null, null), null));
        assertFalse(AssertionFunctions.allNull(Arrays.asList(null, "abc"), null));
        assertFalse(AssertionFunctions.allNull("not a list", null));
    }

    @Test
    void testLength_NeitherStringNorCollection_ReturnsFalse() {
        assertFalse(AssertionFunctions.length(123, 3));
    }

    @Test
    void testBetween_ReversedRange() {
        assertFalse(AssertionFunctions.between(7, List.of(10, 5)));
    }

    @Test
    void testIsEmpty_NullReturnsFalse() {
        assertFalse(AssertionFunctions.isEmpty(null, null));
    }

    @Test
    void testIsNotEmpty_NullReturnsFalse() {
        assertFalse(AssertionFunctions.isNotEmpty(null, null));
    }

    @Test
    void testGreaterThan_DoubleValues() {
        assertTrue(AssertionFunctions.greaterThan(3.14, 3.0));
        assertFalse(AssertionFunctions.greaterThan(3.14, 4.0));
    }

    @Test
    void testLessThan_NegativeNumbers() {
        assertTrue(AssertionFunctions.lessThan(-5, -1));
        assertFalse(AssertionFunctions.lessThan(-1, -5));
    }

    @Test
    void testGreaterThan() {
        assertTrue(AssertionFunctions.greaterThan(5, 2));
        assertFalse(AssertionFunctions.greaterThan(2.0, 2.0));
        assertThrows(IllegalArgumentException.class, () ->
                AssertionFunctions.greaterThan("notNumber", 2));
    }

    @Test
    void testLessThan() {
        assertTrue(AssertionFunctions.lessThan(2, 5));
        assertFalse(AssertionFunctions.lessThan(5, 5));
        assertThrows(IllegalArgumentException.class, () ->
                AssertionFunctions.lessThan(5, "notNumber"));
    }

    @Test
    void testContainsAll() {
        assertTrue(AssertionFunctions.containsAll(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("a", "b")));
        assertFalse(AssertionFunctions.containsAll(
                Arrays.asList("a", "b"),
                Arrays.asList("a", "b", "c")));
        assertFalse(AssertionFunctions.containsAll("String", "String"));
    }

    @Test
    void testContainsAny() {
        assertTrue(AssertionFunctions.containsAny(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("x", "b")));
        assertFalse(AssertionFunctions.containsAny(
                Arrays.asList("a", "b"),
                Arrays.asList("x", "y")));
    }

    @Test
    void testStartsWith() {
        assertTrue(AssertionFunctions.startsWith("abcde", "abc"));
        assertFalse(AssertionFunctions.startsWith("abc", "xyz"));
        assertFalse(AssertionFunctions.startsWith(null, "anything"));
    }

    @Test
    void testEndsWith() {
        assertTrue(AssertionFunctions.endsWith("abcde", "cde"));
        assertFalse(AssertionFunctions.endsWith("abc", "xyz"));
    }

    @Test
    void testLength_String() {
        assertTrue(AssertionFunctions.length("hello", 5));
        assertFalse(AssertionFunctions.length("hello", 4));
        assertThrows(IllegalArgumentException.class, () ->
                AssertionFunctions.length("hello", "notNumber"));
    }

    @Test
    void testLength_Collection() {
        assertTrue(AssertionFunctions.length(Arrays.asList(1,2), 2));
        assertFalse(AssertionFunctions.length(Arrays.asList(1,2,3), 2));
    }

    @Test
    void testMatchesRegex() {
        assertTrue(AssertionFunctions.matchesRegex("abc123", "[a-z]+\\d+"));
        assertFalse(AssertionFunctions.matchesRegex("abc", "\\d+"));
        assertThrows(IllegalArgumentException.class, () ->
                AssertionFunctions.matchesRegex("abc", 123)); // not a String
    }

    @Test
    void testIsEmpty() {
        assertTrue(AssertionFunctions.isEmpty(Collections.emptyList(), null));
        assertFalse(AssertionFunctions.isEmpty(List.of("a"), null));
        assertFalse(AssertionFunctions.isEmpty("string", null));
    }

    @Test
    void testIsNotEmpty() {
        assertTrue(AssertionFunctions.isNotEmpty(List.of("x"), null));
        assertFalse(AssertionFunctions.isNotEmpty(Collections.emptyList(), null));
        assertFalse(AssertionFunctions.isNotEmpty("string", null));
    }

    @Test
    void testBetween() {
        assertTrue(AssertionFunctions.between(5, Arrays.asList(1, 10)));
        assertFalse(AssertionFunctions.between(11, Arrays.asList(1, 10)));

        assertThrows(IllegalArgumentException.class, () ->
                AssertionFunctions.between("foo", Arrays.asList(1, 5)));
        assertThrows(IllegalArgumentException.class, () ->
                AssertionFunctions.between(5, "notAList"));
        assertThrows(IllegalArgumentException.class, () ->
                AssertionFunctions.between(5, Arrays.asList(1, 2, 3)));
    }

    @Test
    void testEqualsIgnoreCase() {
        assertTrue(AssertionFunctions.equalsIgnoreCase("ABC", "abc"));
        assertFalse(AssertionFunctions.equalsIgnoreCase("Hello", "World"));
        assertFalse(AssertionFunctions.equalsIgnoreCase(null, "abc"));
    }
}
