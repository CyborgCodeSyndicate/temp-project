package com.theairebellion.zeus.validator.util;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionUtilTest {

    @Test
    void testValidate_ThrowsIfDataIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(null, Assertion.builder(String.class).build()));
    }

    @Test
    void testValidate_ThrowsIfNoAssertions() {
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(Collections.singletonMap("key", "value")));
    }

    @Test
    void testValidate_SingleAssertionSuccess() {
        Map<String, Object> data = Collections.singletonMap("name", "Zeus");
        Assertion<String> assertion = Assertion.builder(String.class)
                .key("name")
                .type(AssertionTypes.IS)
                .expected("Zeus")
                .soft(false)
                .build();

        var results = AssertionUtil.validate(data, assertion);
        assertEquals(1, results.size());
        AssertionResult<?> result = results.get(0);
        assertTrue(result.isPassed());
        assertEquals("IS", result.getDescription());
    }

    @Test
    void testValidate_SingleAssertionFail() {
        Map<String, Object> data = Collections.singletonMap("age", 25);
        Assertion<Integer> assertion = Assertion.builder(Integer.class)
                .key("age")
                .type(AssertionTypes.GREATER_THAN)
                .expected(30)
                .soft(false)
                .build();

        var results = AssertionUtil.validate(data, assertion);
        assertFalse(results.get(0).isPassed());
    }

    @Test
    void testValidate_ThrowsIfKeyNotExist() {
        Map<String, Object> data = Collections.singletonMap("age", 25);
        Assertion<Integer> assertion = Assertion.builder(Integer.class)
                .key("missingKey")
                .type(AssertionTypes.IS)
                .expected(25)
                .soft(false)
                .build();
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(data, assertion));
    }

    @Test
    void testValidate_ThrowsIfAssertionNull() {
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(Collections.emptyMap(), (Assertion<?>) null));
    }

    @Test
    void testValidate_ThrowsIfAssertionHasNoKey() {
        Map<String, Object> data = Collections.singletonMap("name", "Zeus");
        Assertion<String> assertion = Assertion.builder(String.class)
                .type(AssertionTypes.IS)
                .expected("Zeus")
                .soft(false)
                .build();
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(data, assertion));
    }

    @Test
    void testValidate_ThrowsIfAssertionHasNoType() {
        Map<String, Object> data = Collections.singletonMap("name", "Zeus");
        Assertion<String> assertion = Assertion.builder(String.class)
                .key("name")
                .expected("Zeus")
                .soft(false)
                .build();
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(data, assertion));
    }

    @Test
    void testValidate_SupportedTypeMismatch() {
        Map<String, Object> data = Collections.singletonMap("age", "Not a number");
        Assertion<String> assertion = Assertion.builder(String.class)
                .key("age")
                .type(AssertionTypes.GREATER_THAN)
                .expected("anything")
                .soft(false)
                .build();
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(data, assertion));
    }

    @Test
    void testValidate_MultipleAssertionsSuccess() {
        Map<String, Object> data = Map.of(
                "name", "Zeus",
                "age", 1000
        );

        var assertion1 = Assertion.builder(String.class)
                .key("name")
                .type(AssertionTypes.IS)
                .expected("Zeus")
                .soft(false)
                .build();

        var assertion2 = Assertion.builder(Integer.class)
                .key("age")
                .type(AssertionTypes.GREATER_THAN)
                .expected(500)
                .soft(true)
                .build();

        var results = AssertionUtil.validate(data, assertion1, assertion2);
        assertEquals(2, results.size());
        assertTrue(results.get(0).isPassed());
        assertTrue(results.get(1).isPassed());
        assertTrue(results.get(1).isSoft());
    }

    @Test
    void testValidate_SoftAssertionFail() {
        Map<String, Object> data = Map.of("score", 50);
        var assertion = Assertion.builder(Integer.class)
                .key("score")
                .type(AssertionTypes.GREATER_THAN)
                .expected(100)
                .soft(true)
                .build();

        var results = AssertionUtil.validate(data, assertion);
        assertEquals(1, results.size());
        assertFalse(results.get(0).isPassed());
        assertTrue(results.get(0).isSoft());
    }
}
