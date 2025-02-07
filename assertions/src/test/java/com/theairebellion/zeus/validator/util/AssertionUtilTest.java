package com.theairebellion.zeus.validator.util;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AssertionUtilTest {

    @Test
    void validate_throwsIfDataIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(null, Assertion.builder(String.class).build()));
    }

    @Test
    void validate_throwsIfNoAssertions() {
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(Map.of("key", "value")));
    }

    @Test
    void validate_singleAssertionSuccess() {
        var data = Map.of("name", "Zeus");
        var assertion = Assertion.builder(String.class)
                .key("name")
                .type(AssertionTypes.IS)
                .expected("Zeus")
                .soft(false)
                .build();

        var results = AssertionUtil.validate(data, assertion);
        assertEquals(1, results.size());
        var result = results.get(0);
        assertTrue(result.isPassed());
        assertEquals("IS", result.getDescription());
    }

    @Test
    void validate_singleAssertionFail() {
        var data = Map.of("age", 25);
        var assertion = Assertion.builder(Integer.class)
                .key("age")
                .type(AssertionTypes.GREATER_THAN)
                .expected(30)
                .soft(false)
                .build();

        var results = AssertionUtil.validate(data, assertion);
        assertFalse(results.get(0).isPassed());
    }

    @Test
    void validate_throwsIfKeyNotExist() {
        var data = Map.of("age", 25);
        var assertion = Assertion.builder(Integer.class)
                .key("missingKey")
                .type(AssertionTypes.IS)
                .expected(25)
                .soft(false)
                .build();
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(data, assertion));
    }

    @Test
    void validate_throwsIfAssertionNull() {
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(Map.of(), (Assertion<?>) null));
    }

    @Test
    void validate_throwsIfAssertionHasNoKey() {
        var data = Map.of("name", "Zeus");
        var assertion = Assertion.builder(String.class)
                .type(AssertionTypes.IS)
                .expected("Zeus")
                .soft(false)
                .build();
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(data, assertion));
    }

    @Test
    void validate_throwsIfAssertionHasNoType() {
        var data = Map.of("name", "Zeus");
        var assertion = Assertion.builder(String.class)
                .key("name")
                .expected("Zeus")
                .soft(false)
                .build();
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(data, assertion));
    }

    @Test
    void validate_supportedTypeMismatch() {
        var data = Map.of("age", "Not a number");
        var assertion = Assertion.builder(String.class)
                .key("age")
                .type(AssertionTypes.GREATER_THAN)
                .expected("anything")
                .soft(false)
                .build();
        assertThrows(IllegalArgumentException.class, () ->
                AssertionUtil.validate(data, assertion));
    }

    @Test
    void validate_multipleAssertionsSuccess() {
        var data = Map.of("name", "Zeus", "age", 1000);

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
        assertTrue(results.stream().allMatch(AssertionResult::isPassed));
        assertTrue(results.get(1).isSoft());
    }

    @Test
    void validate_softAssertionFail() {
        var data = Map.of("score", 50);
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
