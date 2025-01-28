package com.theairebellion.zeus.validator.registry;

import com.theairebellion.zeus.validator.core.AssertionType;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionRegistryTest {

    @Test
    void testGetValidator_BuiltInTypes() {
        BiFunction<Object, Object, Boolean> isValidator =
                AssertionRegistry.getValidator(AssertionTypes.IS);
        assertNotNull(isValidator);
        assertTrue(isValidator.apply("abc", "abc"));

        BiFunction<Object, Object, Boolean> notValidator =
                AssertionRegistry.getValidator(AssertionTypes.NOT);
        assertNotNull(notValidator);
        assertTrue(notValidator.apply("abc", "xyz"));
    }

    @Test
    void testGetValidator_ThrowsForUnknownType() {
        AssertionType unknownType = new AssertionType() {
            @Override public Enum<?> type() { return null; }
            @Override public Class<?> getSupportedType() { return Object.class; }
        };
        assertThrows(IllegalArgumentException.class, () ->
                AssertionRegistry.getValidator(unknownType));
    }

    @Test
    void testRegisterCustomAssertion() {
        AssertionType customType = new AssertionType() {
            @Override public Enum<?> type() { return null; }
            @Override public Class<?> getSupportedType() { return Object.class; }
        };

        BiFunction<Object, Object, Boolean> customFunc = (a, e) -> true;

        AssertionRegistry.registerCustomAssertion(customType, customFunc);

        BiFunction<Object, Object, Boolean> retrieved =
                AssertionRegistry.getValidator(customType);
        assertSame(customFunc, retrieved);

        assertThrows(NullPointerException.class, () ->
                AssertionRegistry.registerCustomAssertion(null, customFunc));
        assertThrows(NullPointerException.class, () ->
                AssertionRegistry.registerCustomAssertion(customType, null));
    }

    @Test
    void testAllAssertionTypesRegistered() {
        for (AssertionTypes type : AssertionTypes.values()) {
            assertNotNull(
                    AssertionRegistry.getValidator(type),
                    "Should have a validator for: " + type
            );
        }
    }

    @Test
    void testIsValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.IS);
        assertTrue(validator.apply("abc", "abc"));
        assertFalse(validator.apply("abc", "def"));
    }

    @Test
    void testNotValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.NOT);
        assertTrue(validator.apply("abc", "def"));
        assertFalse(validator.apply("abc", "abc"));
    }

    @Test
    void testContainsValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.CONTAINS);
        assertTrue(validator.apply("hello world", "world"));
        assertFalse(validator.apply("hello", "bye"));
        assertFalse(validator.apply(null, "something"));
    }

    @Test
    void testNotNullValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.NOT_NULL);
        assertTrue(validator.apply("abc", null));
        assertFalse(validator.apply(null, null));
    }

    @Test
    void testIsNullValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.IS_NULL);
        assertTrue(validator.apply(null, null));
        assertFalse(validator.apply("not-null", null));
    }

    @Test
    void testAllNotNullValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.ALL_NOT_NULL);
        assertTrue(validator.apply(Arrays.asList("a", "b"), null));
        assertFalse(validator.apply(Arrays.asList("a", null), null));
        assertFalse(validator.apply("not-a-collection", null));
    }

    @Test
    void testAllNullValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.ALL_NULL);
        assertTrue(validator.apply(Arrays.asList(null, null), null));
        assertFalse(validator.apply(Arrays.asList("a", null), null));
    }

    @Test
    void testGreaterThanValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.GREATER_THAN);
        assertTrue(validator.apply(5, 3));
        assertFalse(validator.apply(3, 5));
        assertThrows(IllegalArgumentException.class, () -> validator.apply("string", 3));
    }

    @Test
    void testLessThanValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.LESS_THAN);
        assertTrue(validator.apply(3, 5));
        assertFalse(validator.apply(5, 3));
        assertThrows(IllegalArgumentException.class, () -> validator.apply(3, "string"));
    }

    @Test
    void testContainsAllValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.CONTAINS_ALL);
        assertTrue(validator.apply(Arrays.asList(1, 2, 3), Arrays.asList(2, 3)));
        assertFalse(validator.apply(Arrays.asList(1, 2, 3), Arrays.asList(4, 5)));
    }

    @Test
    void testContainsAnyValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.CONTAINS_ANY);
        assertTrue(validator.apply(Arrays.asList(1, 2, 3), Arrays.asList(3, 4)));
        assertFalse(validator.apply(Arrays.asList(1, 2, 3), Arrays.asList(4, 5)));
    }

    @Test
    void testStartsWithValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.STARTS_WITH);
        assertTrue(validator.apply("hello world", "hello"));
        assertFalse(validator.apply("hello world", "world"));
    }

    @Test
    void testEndsWithValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.ENDS_WITH);
        assertTrue(validator.apply("hello world", "world"));
        assertFalse(validator.apply("hello world", "hello"));
    }

    @Test
    void testLengthValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.LENGTH);
        assertTrue(validator.apply("abc", 3));
        assertFalse(validator.apply("abc", 4));
        assertThrows(IllegalArgumentException.class, () -> validator.apply("abc", "not-a-number"));
    }

    @Test
    void testMatchesRegexValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.MATCHES_REGEX);
        assertTrue(validator.apply("hello123", "\\w+\\d+"));
        assertFalse(validator.apply("hello", "\\d+"));
        assertThrows(IllegalArgumentException.class, () -> validator.apply("hello", 123));
    }

    @Test
    void testBetweenValidator() {
        var validator = AssertionRegistry.getValidator(AssertionTypes.BETWEEN);
        assertTrue(validator.apply(5, Arrays.asList(3, 7)));
        assertFalse(validator.apply(10, Arrays.asList(3, 7)));
        assertThrows(IllegalArgumentException.class, () -> validator.apply(5, "not-a-list"));
    }

    @Test
    void testCustomValidatorRegistration() {
        AssertionType customType = new AssertionType() {
            @Override
            public Enum<?> type() {
                return AssertionTypes.IS;
            }

            @Override
            public Class<?> getSupportedType() {
                return Object.class;
            }
        };

        AssertionRegistry.registerCustomAssertion(customType, (actual, expected) -> actual.equals("custom"));

        var validator = AssertionRegistry.getValidator(customType);
        assertTrue(validator.apply("custom", null));
        assertFalse(validator.apply("not-custom", null));
    }
}
