package com.theairebellion.zeus.validator.registry;

import com.theairebellion.zeus.validator.core.AssertionType;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

class AssertionRegistryTest {

    @Test
    void testGetValidator_BuiltInTypes() {
        var isValidator = AssertionRegistry.getValidator(AssertionTypes.IS);
        var notValidator = AssertionRegistry.getValidator(AssertionTypes.NOT);

        assertAll(
                () -> assertNotNull(isValidator),
                () -> assertTrue(isValidator.apply("abc", "abc")),
                () -> assertNotNull(notValidator),
                () -> assertTrue(notValidator.apply("abc", "xyz"))
        );
    }

    @Test
    void testGetValidator_ThrowsForUnknownType() {
        var unknownType = new AssertionType() {
            @Override public Enum<?> type() { return null; }
            @Override public Class<?> getSupportedType() { return Object.class; }
        };
        assertThrows(IllegalArgumentException.class, () ->
                AssertionRegistry.getValidator(unknownType));
    }

    @Test
    void testRegisterCustomAssertion() {
        var customType = new AssertionType() {
            @Override public Enum<?> type() { return null; }
            @Override public Class<?> getSupportedType() { return Object.class; }
        };

        var customFunc = (BiFunction<Object, Object, Boolean>) (a, e) -> true;
        AssertionRegistry.registerCustomAssertion(customType, customFunc);

        var retrieved = AssertionRegistry.getValidator(customType);
        assertSame(customFunc, retrieved);

        assertAll(
                () -> assertThrows(NullPointerException.class, () ->
                        AssertionRegistry.registerCustomAssertion(null, customFunc)),
                () -> assertThrows(NullPointerException.class, () ->
                        AssertionRegistry.registerCustomAssertion(customType, null))
        );
    }

    @Test
    void testAllAssertionTypesRegistered() {
        for (var type : AssertionTypes.values()) {
            assertNotNull(AssertionRegistry.getValidator(type), "Should have a validator for: " + type);
        }
    }

    @Test
    void testGreaterThanValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.GREATER_THAN);
        assertAll(
                () -> assertTrue(validator.apply(5, 3)),
                () -> assertFalse(validator.apply(3, 5)),
                () -> assertThrows(IllegalArgumentException.class, () -> validator.apply("string", 3))
        );
    }

    @Test
    void testLessThanValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.LESS_THAN);
        assertAll(
                () -> assertTrue(validator.apply(3, 5)),
                () -> assertFalse(validator.apply(5, 3)),
                () -> assertThrows(IllegalArgumentException.class, () -> validator.apply(3, "string"))
        );
    }

    @Test
    void testContainsValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.CONTAINS);
        assertAll(
                () -> assertTrue(validator.apply("hello world", "world")),
                () -> assertFalse(validator.apply("hello", "bye")),
                () -> assertFalse(validator.apply(null, "something"))
        );
    }

    @Test
    void testStartsWithValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.STARTS_WITH);
        assertAll(
                () -> assertTrue(validator.apply("hello world", "hello")),
                () -> assertFalse(validator.apply("hello world", "world"))
        );
    }

    @Test
    void testEndsWithValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.ENDS_WITH);
        assertAll(
                () -> assertTrue(validator.apply("hello world", "world")),
                () -> assertFalse(validator.apply("hello world", "hello"))
        );
    }

    @Test
    void testLengthValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.LENGTH);
        assertAll(
                () -> assertTrue(validator.apply("abc", 3)),
                () -> assertFalse(validator.apply("abc", 4)),
                () -> assertThrows(IllegalArgumentException.class, () -> validator.apply("abc", "not-a-number"))
        );
    }

    @Test
    void testMatchesRegexValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.MATCHES_REGEX);
        assertAll(
                () -> assertTrue(validator.apply("hello123", "\\w+\\d+")),
                () -> assertFalse(validator.apply("hello", "\\d+")),
                () -> assertThrows(IllegalArgumentException.class, () -> validator.apply("hello", 123))
        );
    }

    @Test
    void testBetweenValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.BETWEEN);
        assertAll(
                () -> assertTrue(validator.apply(5, Arrays.asList(3, 7))),
                () -> assertFalse(validator.apply(10, Arrays.asList(3, 7))),
                () -> assertThrows(IllegalArgumentException.class, () -> validator.apply(5, "not-a-list"))
        );
    }

    @Test
    void testCustomValidatorRegistration() {
        var customType = new AssertionType() {
            @Override public Enum<?> type() { return AssertionTypes.IS; }
            @Override public Class<?> getSupportedType() { return Object.class; }
        };

        AssertionRegistry.registerCustomAssertion(customType, (actual, expected) -> actual.equals("custom"));
        var validator = AssertionRegistry.getValidator(customType);

        assertAll(
                () -> assertTrue(validator.apply("custom", null)),
                () -> assertFalse(validator.apply("not-custom", null))
        );
    }
}