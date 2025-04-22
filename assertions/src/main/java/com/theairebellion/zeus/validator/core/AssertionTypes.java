package com.theairebellion.zeus.validator.core;

import java.util.Collection;

/**
 * Defines common assertion types for validation across different test domains.
 * <p>
 * This enum provides a predefined set of assertion types used for validating
 * API responses, database queries, and UI components. Each assertion type
 * specifies a supported data type that it operates on.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public enum AssertionTypes implements AssertionType<AssertionTypes> {
    /**
     * Checks if two values are equal.
     */
    IS(Object.class),

    /**
     * Checks if two values are not equal.
     */
    NOT(Object.class),

    /**
     * Validates if a string contains a specific substring.
     */
    CONTAINS(String.class),

    /**
     * Ensures a value is not null.
     */
    NOT_NULL(Object.class),

    /**
     * Ensures all values in a collection are not null.
     */
    ALL_NOT_NULL(Collection.class),

    /**
     * Ensures a value is null.
     */
    IS_NULL(Object.class),

    /**
     * Ensures all values in a collection are null.
     */
    ALL_NULL(Collection.class),

    /**
     * Checks if a numeric value is greater than the expected value.
     */
    GREATER_THAN(Number.class),

    /**
     * Checks if a numeric value is less than the expected value.
     */
    LESS_THAN(Number.class),

    /**
     * Ensures a collection contains all expected elements.
     */
    CONTAINS_ALL(Collection.class),

    /**
     * Ensures a collection contains at least one of the expected elements.
     */
    CONTAINS_ANY(Collection.class),

    /**
     * Checks if a string starts with a specific prefix.
     */
    STARTS_WITH(String.class),

    /**
     * Checks if a string ends with a specific suffix.
     */
    ENDS_WITH(String.class),

    /**
     * Validates the length of an object (string, list, etc.).
     */
    LENGTH(Object.class),

    /**
     * Checks if a string matches a given regular expression.
     */
    MATCHES_REGEX(String.class),

    /**
     * Ensures a collection is empty.
     */
    EMPTY(Collection.class),

    /**
     * Ensures a collection is not empty.
     */
    NOT_EMPTY(Collection.class),

    /**
     * Validates if a number falls within a specified range.
     */
    BETWEEN(Number.class),

    /**
     * Compares two strings while ignoring case sensitivity.
     */
    EQUALS_IGNORE_CASE(String.class);

    private final Class<?> supportedType;

    /**
     * Constructs an assertion type with the corresponding supported data type.
     *
     * @param supportedType The data type that the assertion operates on.
     * @param <T>           The generic type representing the supported data type.
     */
    <T> AssertionTypes(Class<T> supportedType) {
        this.supportedType = supportedType;
    }

    @Override
    public AssertionTypes type() {
        return this;
    }

    @Override
    public Class<?> getSupportedType() {
        return supportedType;
    }
}
