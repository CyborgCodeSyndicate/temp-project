package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TableAssertionTypesTest {

    @Test
    void testEnumCreation() {
        // Verify all enum values are created
        TableAssertionTypes[] types = TableAssertionTypes.values();
        assertEquals(12, types.length);
    }

    @Test
    void testTypeMethod() {
        for (TableAssertionTypes type : TableAssertionTypes.values()) {
            assertEquals(type, type.type());
        }
    }

    @Test
    void testSupportedType() {
        for (TableAssertionTypes type : TableAssertionTypes.values()) {
            assertEquals(List.class, type.getSupportedType());
        }
    }

    @Test
    void testSpecificEnumValues() {
        // Verify specific enum values exist and have correct supported type
        TableAssertionTypes[] expectedTypes = {
                TableAssertionTypes.TABLE_NOT_EMPTY,
                TableAssertionTypes.TABLE_ROW_COUNT,
                TableAssertionTypes.EVERY_ROW_CONTAINS_VALUES,
                TableAssertionTypes.TABLE_DOES_NOT_CONTAIN_ROW,
                TableAssertionTypes.ALL_ROWS_ARE_UNIQUE,
                TableAssertionTypes.NO_EMPTY_CELLS,
                TableAssertionTypes.COLUMN_VALUES_ARE_UNIQUE,
                TableAssertionTypes.TABLE_DATA_MATCHES_EXPECTED,
                TableAssertionTypes.ROW_NOT_EMPTY,
                TableAssertionTypes.ROW_CONTAINS_VALUES,
                TableAssertionTypes.ALL_CELLS_ENABLED,
                TableAssertionTypes.ALL_CELLS_CLICKABLE
        };

        for (TableAssertionTypes type : expectedTypes) {
            assertNotNull(type);
            assertEquals(List.class, type.getSupportedType());
            assertEquals(type, type.type());
        }
    }
}

class UiTablesAssertionTargetTest {
    @Test
    void testEnumCreation() {
        // Verify all enum values are created
        UiTablesAssertionTarget[] targets = UiTablesAssertionTarget.values();
        assertEquals(4, targets.length);
    }

    @Test
    void testTargetMethod() {
        for (UiTablesAssertionTarget target : UiTablesAssertionTarget.values()) {
            assertEquals(target, target.target());
        }
    }

    @Test
    void testSpecificEnumValues() {
        // Verify specific enum values exist
        UiTablesAssertionTarget[] expectedTargets = {
                UiTablesAssertionTarget.ROW_VALUES,
                UiTablesAssertionTarget.TABLE_VALUES,
                UiTablesAssertionTarget.ROW_ELEMENTS,
                UiTablesAssertionTarget.TABLE_ELEMENTS
        };

        for (UiTablesAssertionTarget target : expectedTargets) {
            assertNotNull(target);
            assertEquals(target, target.target());
        }
    }
}

class UiTableValidatorTest {
    @Test
    void testUiTableValidatorInterfaceWithMockImplementation() {
        // Create a mock implementation of UiTableValidator
        UiTableValidator mockValidator = new UiTableValidator() {
            @Override
            public <T> List<AssertionResult<T>> validateTable(Object object, Assertion... assertions) {
                // Create mock AssertionResults
                AssertionResult<T> mockResult1 = new AssertionResult<>(
                        true,
                        "First assertion",
                        null,
                        null,
                        false
                );

                AssertionResult<T> mockResult2 = new AssertionResult<>(
                        false,
                        "Second assertion",
                        null,
                        null,
                        false
                );

                return List.of(mockResult1, mockResult2);
            }
        };

        // Create mock objects
        Object testObject = new Object();
        Assertion mockAssertion1 = Mockito.mock(Assertion.class);
        Assertion mockAssertion2 = Mockito.mock(Assertion.class);

        // Call validateTable
        List<AssertionResult<Object>> results = mockValidator.validateTable(testObject, mockAssertion1, mockAssertion2);

        // Verify results
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.get(0).isPassed());
        assertFalse(results.get(1).isPassed());
    }

    @Test
    void testUiTableValidatorWithNoAssertions() {
        // Create a mock implementation of UiTableValidator
        UiTableValidator mockValidator = new UiTableValidator() {
            @Override
            public <T> List<AssertionResult<T>> validateTable(Object object, Assertion... assertions) {
                // Return empty list when no assertions
                return List.of();
            }
        };

        // Create mock object
        Object testObject = new Object();

        // Call validateTable with no assertions
        List<AssertionResult<Object>> results = mockValidator.validateTable(testObject);

        // Verify results
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}