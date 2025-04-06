package com.theairebellion.zeus.ui.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeaturesTest {

    @Test
    void enumValues_ShouldHaveCorrectCount() {
        // Given/When
        Features[] values = Features.values();

        // Then
        assertEquals(16, values.length, "Should have 16 enum values");
    }

    @ParameterizedTest
    @MethodSource("provideEnumValuesAndFieldNames")
    void fieldName_ShouldMatchExpectedValue(Features feature, String expectedFieldName) {
        // When
        String actualFieldName = feature.getFieldName();

        // Then
        assertEquals(expectedFieldName, actualFieldName,
                "Enum " + feature.name() + " should have field name " + expectedFieldName);
    }

    @Test
    void fieldNames_ShouldBeUnique() {
        // Given
        Set<String> fieldNames = new HashSet<>();

        // When
        for (Features feature : Features.values()) {
            fieldNames.add(feature.getFieldName());
        }

        // Then
        assertEquals(Features.values().length, fieldNames.size(),
                "All field names should be unique");
    }

    @Test
    void constructor_ShouldSetFieldNameCorrectly() {
        // This verifies that the constructor properly sets the fieldName
        // by checking a sample enum value
        assertEquals("inputField", Features.INPUT_FIELDS.getFieldName(),
                "Constructor should set field name correctly");
    }

    @Test
    void valueOf_ShouldReturnCorrectEnum() {
        // When/Then - verify that valueOf works correctly for each enum
        assertEquals(Features.INPUT_FIELDS, Features.valueOf("INPUT_FIELDS"));
        assertEquals(Features.TABLE, Features.valueOf("TABLE"));
        assertEquals(Features.DATA_INSERTION, Features.valueOf("DATA_INSERTION"));
    }

    @Test
    void valueOf_WithInvalidName_ShouldThrowException() {
        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> Features.valueOf("NONEXISTENT_FEATURE"),
                "valueOf with invalid name should throw IllegalArgumentException");
    }

    // Method source for parameterized test
    private static Stream<Arguments> provideEnumValuesAndFieldNames() {
        return Stream.of(
                Arguments.of(Features.INPUT_FIELDS, "inputField"),
                Arguments.of(Features.TABLE, "table"),
                Arguments.of(Features.BUTTON_FIELDS, "buttonField"),
                Arguments.of(Features.RADIO_FIELDS, "radioField"),
                Arguments.of(Features.CHECKBOX_FIELDS, "checkboxField"),
                Arguments.of(Features.TOGGLE_FIELDS, "toggleField"),
                Arguments.of(Features.SELECT_FIELDS, "selectField"),
                Arguments.of(Features.LIST_FIELDS, "listField"),
                Arguments.of(Features.LOADER_FIELDS, "loaderField"),
                Arguments.of(Features.LINK_FIELDS, "linkField"),
                Arguments.of(Features.ALERT_FIELDS, "alertField"),
                Arguments.of(Features.TAB_FIELDS, "tabField"),
                Arguments.of(Features.REQUESTS_INTERCEPTOR, "interceptor"),
                Arguments.of(Features.VALIDATION, "validation"),
                Arguments.of(Features.NAVIGATION, "navigation"),
                Arguments.of(Features.DATA_INSERTION, "insertionService")
        );
    }
}