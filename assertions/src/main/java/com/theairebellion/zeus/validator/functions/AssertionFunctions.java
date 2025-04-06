package com.theairebellion.zeus.validator.functions;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Provides utility functions for validating assertions.
 * <p>
 * This class contains static methods that perform various validation operations,
 * including equality checks, containment checks, numeric comparisons, string operations,
 * and collection validations. These functions are used to implement different
 * assertion types in the framework.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class AssertionFunctions {

    private AssertionFunctions() {
    }


    /**
     * Checks if the actual value is equal to the expected value.
     */
    public static boolean equals(Object actual, Object expected) {
        return Objects.equals(actual, expected);
    }

    /**
     * Checks if the actual value is not equal to the expected value.
     */
    public static boolean notEquals(Object actual, Object expected) {
        return !Objects.equals(actual, expected);
    }

    /**
     * Checks if the actual value contains the expected value as a substring.
     */
    public static boolean contains(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return false;
        }
        return actual.toString().contains(expected.toString());
    }

    /**
     * Checks if the actual value is not null.
     */
    public static boolean notNull(Object actual, Object expected) {
        boolean condition = Objects.nonNull(actual);
        return expected instanceof Boolean && condition == (Boolean) expected;
    }

    /**
     * Checks if the actual value is null.
     */
    public static boolean isNull(Object actual, Object expected) {
        boolean condition = Objects.isNull(actual);
        return expected instanceof Boolean && condition == (Boolean) expected;
    }

    /**
     * Checks if all elements in a collection are not null.
     */
    public static boolean allNotNull(Object actual, Object expected) {
        if (!isCollection(actual)) {
            return false;
        }
        boolean condition = ((Collection<?>) actual).stream().allMatch(Objects::nonNull);
        return expected instanceof Boolean && condition == (Boolean) expected;
    }

    /**
     * Checks if all elements in a collection are null.
     */
    public static boolean allNull(Object actual, Object expected) {
        if (!isCollection(actual)) {
            return false;
        }
        boolean condition = ((Collection<?>) actual).stream().allMatch(Objects::isNull);
        return expected instanceof Boolean && condition == (Boolean) expected;
    }

    /**
     * Checks if the actual numeric value is greater than the expected numeric value.
     */
    public static boolean greaterThan(Object actual, Object expected) {
        if (!isNumber(actual) || !isNumber(expected)) {
            throw new IllegalArgumentException("Both actual and expected must be numbers.");
        }
        return ((Number) actual).doubleValue() > ((Number) expected).doubleValue();
    }

    /**
     * Checks if the actual numeric value is less than the expected numeric value.
     */
    public static boolean lessThan(Object actual, Object expected) {
        if (!isNumber(actual) || !isNumber(expected)) {
            throw new IllegalArgumentException("Both actual and expected must be numbers.");
        }
        return ((Number) actual).doubleValue() < ((Number) expected).doubleValue();
    }

    /**
     * Checks if all elements in the expected collection exist in the actual collection.
     */
    public static boolean containsAll(Object actual, Object expected) {
        if (!isCollection(actual) || !isCollection(expected)) {
            return false;
        }
        return ((Collection<?>) actual).containsAll((Collection<?>) expected);
    }

    /**
     * Checks if any element in the expected collection exists in the actual collection.
     */
    public static boolean containsAny(Object actual, Object expected) {
        if (!isCollection(actual) || !isCollection(expected)) {
            return false;
        }
        Collection<?> actualCollection = (Collection<?>) actual;
        Collection<?> expectedCollection = (Collection<?>) expected;
        return expectedCollection.stream().anyMatch(actualCollection::contains);
    }

    /**
     * Checks if the actual string starts with the expected string.
     */
    public static boolean startsWith(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return false;
        }
        return actual.toString().startsWith(expected.toString());
    }

    /**
     * Checks if the actual string ends with the expected string.
     */
    public static boolean endsWith(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return false;
        }
        return actual.toString().endsWith(expected.toString());
    }

    /**
     * Checks if the actual value has the expected length.
     */
    public static boolean length(Object actual, Object expected) {
        if (!(expected instanceof Number)) {
            throw new IllegalArgumentException("Expected value must be a number.");
        }
        int expectedLength = ((Number) expected).intValue();

        if (actual instanceof String) {
            return ((String) actual).length() == expectedLength;
        } else if (actual instanceof Collection) {
            return ((Collection<?>) actual).size() == expectedLength;
        }
        return false;
    }

    /**
     * Checks if the actual string matches the expected regex pattern.
     */
    public static boolean matchesRegex(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return false;
        }
        if (!(expected instanceof String)) {
            throw new IllegalArgumentException("Expected value must be a valid regex string.");
        }
        return actual.toString().matches(expected.toString());
    }

    /**
     * Checks if the actual collection is empty.
     */
    public static boolean isEmpty(Object actual, Object expected) {
        boolean condition = (actual instanceof Collection) && ((Collection<?>) actual).isEmpty();
        return expected instanceof Boolean && condition == (Boolean) expected;
    }

    /**
     * Checks if the actual collection is not empty.
     */
    public static boolean isNotEmpty(Object actual, Object expected) {
        boolean condition = (actual instanceof Collection) && !((Collection<?>) actual).isEmpty();
        return expected instanceof Boolean && condition == (Boolean) expected;
    }

    /**
     * Checks if the actual numeric value falls within a given range.
     */
    public static boolean between(Object actual, Object expected) {
        if (!isNumber(actual) || !(expected instanceof List<?>)) {
            throw new IllegalArgumentException("Expected must be a list with two numeric values.");
        }
        List<?> range = (List<?>) expected;
        if (range.size() != 2 || !(range.get(0) instanceof Number) || !(range.get(1) instanceof Number)) {
            throw new IllegalArgumentException("Expected list must contain exactly two numeric values.");
        }

        double min = ((Number) range.get(0)).doubleValue();
        double max = ((Number) range.get(1)).doubleValue();
        double value = ((Number) actual).doubleValue();

        return value >= min && value <= max;
    }

    /**
     * Checks if the actual string is equal to the expected string, ignoring case.
     */
    public static boolean equalsIgnoreCase(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return false;
        }
        return actual.toString().equalsIgnoreCase(expected.toString());
    }

    /**
     * Checks if the given object is a collection.
     */
    private static boolean isCollection(Object obj) {
        return obj instanceof Collection;
    }

    /**
     * Checks if the given object is a number.
     */
    private static boolean isNumber(Object obj) {
        return obj instanceof Number;
    }

}
