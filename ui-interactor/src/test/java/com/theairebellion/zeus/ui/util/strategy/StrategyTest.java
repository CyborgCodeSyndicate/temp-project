package com.theairebellion.zeus.ui.util.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Strategy Tests")
class StrategyTest {

    @ParameterizedTest
    @EnumSource(Strategy.class)
    @DisplayName("getType should return the enum instance itself")
    void getTypeShouldReturnEnumInstance(Strategy strategy) {
        // When
        Enum<?> result = strategy.getType();

        // Then
        assertSame(strategy, result, "getType should return the enum instance itself");
    }

    @Test
    @DisplayName("Should have expected enum values")
    void shouldHaveExpectedEnumValues() {
        // Verify all expected enum values exist
        assertNotNull(Strategy.RANDOM);
        assertNotNull(Strategy.FIRST);
        assertNotNull(Strategy.LAST);
        assertNotNull(Strategy.ALL);

        // Verify the expected number of enum values
        assertEquals(4, Strategy.values().length);
    }
}