package com.theairebellion.zeus.validator.functions;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class AssertionFunctions {

    public static boolean equals(Object actual, Object expected) {
        return Objects.equals(actual, expected);
    }

    public static boolean notEquals(Object actual, Object expected) {
        return !Objects.equals(actual, expected);
    }

    public static boolean contains(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return false;
        }
        return actual.toString().contains(expected.toString());
    }

    public static boolean notNull(Object actual, Object expected) {
        boolean condition = Objects.nonNull(actual);
        if (!(expected instanceof Boolean)) {
            return false;
        }
        return condition == (Boolean) expected;
    }

    public static boolean isNull(Object actual, Object expected) {
        boolean condition = Objects.isNull(actual);
        if (!(expected instanceof Boolean)) {
            return false;
        }
        return condition == (Boolean) expected;
    }

    public static boolean allNotNull(Object actual, Object expected) {
        if (!isCollection(actual)) {
            return false;
        }
        boolean condition = ((Collection<?>) actual).stream().allMatch(Objects::nonNull);
        if (!(expected instanceof Boolean)) {
            return false;
        }
        return condition == (Boolean) expected;
    }

    public static boolean allNull(Object actual, Object expected) {
        if (!isCollection(actual)) {
            return false;
        }
        boolean condition = ((Collection<?>) actual).stream().allMatch(Objects::isNull);
        if (!(expected instanceof Boolean)) {
            return false;
        }
        return condition == (Boolean) expected;
    }

    public static boolean greaterThan(Object actual, Object expected) {
        if (!isNumber(actual) || !isNumber(expected)) {
            throw new IllegalArgumentException("Both actual and expected must be numbers.");
        }
        return ((Number) actual).doubleValue() > ((Number) expected).doubleValue();
    }

    public static boolean lessThan(Object actual, Object expected) {
        if (!isNumber(actual) || !isNumber(expected)) {
            throw new IllegalArgumentException("Both actual and expected must be numbers.");
        }
        return ((Number) actual).doubleValue() < ((Number) expected).doubleValue();
    }

    public static boolean containsAll(Object actual, Object expected) {
        if (!isCollection(actual) || !isCollection(expected)) {
            return false;
        }
        return ((Collection<?>) actual).containsAll((Collection<?>) expected);
    }

    public static boolean containsAny(Object actual, Object expected) {
        if (!isCollection(actual) || !isCollection(expected)) {
            return false;
        }
        Collection<?> actualCollection = (Collection<?>) actual;
        Collection<?> expectedCollection = (Collection<?>) expected;
        return expectedCollection.stream().anyMatch(actualCollection::contains);
    }

    public static boolean startsWith(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return false;
        }
        return actual.toString().startsWith(expected.toString());
    }

    public static boolean endsWith(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return false;
        }
        return actual.toString().endsWith(expected.toString());
    }

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

    public static boolean matchesRegex(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return false;
        }
        if (!(expected instanceof String)) {
            throw new IllegalArgumentException("Expected value must be a valid regex string.");
        }
        return actual.toString().matches(expected.toString());
    }

    public static boolean isEmpty(Object actual, Object expected) {
        boolean condition = (actual instanceof Collection) && ((Collection<?>) actual).isEmpty();
        if (!(expected instanceof Boolean)) {
            return false;
        }
        return condition == (Boolean) expected;
    }

    public static boolean isNotEmpty(Object actual, Object expected) {
        boolean condition = (actual instanceof Collection) && !((Collection<?>) actual).isEmpty();
        if (!(expected instanceof Boolean)) {
            return false;
        }
        return condition == (Boolean) expected;
    }

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

    public static boolean equalsIgnoreCase(Object actual, Object expected) {
        if (actual == null || expected == null) {
            return false;
        }
        return actual.toString().equalsIgnoreCase(expected.toString());
    }

    private static boolean isCollection(Object obj) {
        return obj instanceof Collection;
    }


    private static boolean isNumber(Object obj) {
        return obj instanceof Number;
    }

}
