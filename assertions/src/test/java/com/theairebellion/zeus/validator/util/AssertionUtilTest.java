package com.theairebellion.zeus.validator.util;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.core.AssertionTypes;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AssertionUtilTest {

    private static final String NAME_KEY = "name";
    private static final String AGE_KEY = "age";
    private static final String SCORE_KEY = "score";
    private static final String ZEUS = "Zeus";

    @Test
    void validate_throwsIfDataIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> AssertionUtil.validate(null, Assertion.builder(String.class).build()));
    }

    @Test
    void validate_throwsIfNoAssertions() {
        assertThrows(IllegalArgumentException.class,
                () -> AssertionUtil.validate(Map.of("key", "value")));
    }

    @Test
    void validate_singleAssertionSuccess() {
        var data = Map.of(NAME_KEY, ZEUS);
        var assertion = Assertion.builder(String.class)
                .key(NAME_KEY)
                .type(AssertionTypes.IS)
                .expected(ZEUS)
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
        var data = Map.of(AGE_KEY, 25);
        var assertion = Assertion.builder(Integer.class)
                .key(AGE_KEY)
                .type(AssertionTypes.GREATER_THAN)
                .expected(30)
                .soft(false)
                .build();

        var results = AssertionUtil.validate(data, assertion);
        assertFalse(results.get(0).isPassed());
    }

    @Test
    void validate_throwsIfKeyNotExist() {
        var data = Map.of(AGE_KEY, 25);
        var assertion = Assertion.builder(Integer.class)
                .key("missingKey")
                .type(AssertionTypes.IS)
                .expected(25)
                .soft(false)
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> AssertionUtil.validate(data, assertion));
    }

    @Test
    void validate_throwsIfAssertionNull() {
        assertThrows(IllegalArgumentException.class,
                () -> AssertionUtil.validate(Map.of(), (Assertion<?>) null));
    }

    @Test
    void validate_throwsIfAssertionHasNoKey() {
        var data = Map.of(NAME_KEY, ZEUS);
        var assertion = Assertion.builder(String.class)
                .type(AssertionTypes.IS)
                .expected(ZEUS)
                .soft(false)
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> AssertionUtil.validate(data, assertion));
    }

    @Test
    void validate_throwsIfAssertionHasNoType() {
        var data = Map.of(NAME_KEY, ZEUS);
        var assertion = Assertion.builder(String.class)
                .key(NAME_KEY)
                .expected(ZEUS)
                .soft(false)
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> AssertionUtil.validate(data, assertion));
    }

    @Test
    void validate_supportedTypeMismatch() {
        var data = Map.of(AGE_KEY, "Not a number");
        var assertion = Assertion.builder(String.class)
                .key(AGE_KEY)
                .type(AssertionTypes.GREATER_THAN)
                .expected("anything")
                .soft(false)
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> AssertionUtil.validate(data, assertion));
    }

    @Test
    void validate_multipleAssertionsSuccess() {
        var data = Map.of(NAME_KEY, ZEUS, AGE_KEY, 1000);

        var assertion1 = Assertion.builder(String.class)
                .key(NAME_KEY)
                .type(AssertionTypes.IS)
                .expected(ZEUS)
                .soft(false)
                .build();

        var assertion2 = Assertion.builder(Integer.class)
                .key(AGE_KEY)
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
        var data = Map.of(SCORE_KEY, 50);
        var assertion = Assertion.builder(Integer.class)
                .key(SCORE_KEY)
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