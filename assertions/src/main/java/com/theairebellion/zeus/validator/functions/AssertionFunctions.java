package com.theairebellion.zeus.validator.functions;

import java.util.Collection;
import java.util.Objects;

public class AssertionFunctions {


    public static boolean equals(Object actual, Object expected) {
        return actual.equals(expected);
    }


    public static boolean notEquals(Object actual, Object expected) {
        return !Objects.equals(actual, expected);
    }


    public static boolean contains(Object actual, Object expected) {
        return actual != null && actual.toString().contains(expected.toString());
    }


    public static boolean notNull(Object actual, Object expected) {
        return Objects.nonNull(actual);
    }


    public static boolean allNotNull(Object actual, Object expected) {
        if (!(actual instanceof Collection)) {
            return false;
        }
        return ((Collection<?>) actual).stream().allMatch(Objects::nonNull);
    }


    public static boolean isNull(Object actual, Object expected) {
        return Objects.isNull(actual);
    }

    public static boolean allNull(Object actual, Object expected) {
        if (!(actual instanceof Collection)) {
            return false;
        }
        return ((Collection<?>) actual).stream().allMatch(Objects::isNull);
    }


    public static boolean greaterThan(Object actual, Object expected) {
        return actual != null && ((Number) actual).doubleValue() > ((Number) expected).doubleValue();
    }


    public static boolean lessThan(Object actual, Object expected) {
        return actual != null && ((Number) actual).doubleValue() < ((Number) expected).doubleValue();
    }


    public static boolean containsAll(Object actual, Object expected) {
        if (!(actual instanceof Collection) || !(expected instanceof Collection)) {
            return false;
        }
        return ((Collection<?>) actual).containsAll((Collection<?>) expected);
    }


    public static boolean containsAny(Object actual, Object expected) {
        if (!(actual instanceof final Collection<?> actualCollection) || !(expected instanceof final Collection<?> expectedCollection)) {
            return false;
        }
        return expectedCollection.stream().anyMatch(actualCollection::contains);
    }

}
