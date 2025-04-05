package com.theairebellion.zeus.ui.extensions;

import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StorageKeysUiTest {

    @Test
    void enumValues_ShouldHaveCorrectCount() {
        // Given/When
        StorageKeysUi[] values = StorageKeysUi.values();

        // Then
        assertEquals(4, values.length, "Should have 3 enum values");
    }

    @Test
    void enumValues_ShouldContainExpectedValues() {
        // Given/When/Then
        assertEquals(StorageKeysUi.UI, StorageKeysUi.valueOf("UI"),
                "Should have UI enum value");
        assertEquals(StorageKeysUi.RESPONSES, StorageKeysUi.valueOf("RESPONSES"),
                "Should have RESPONSES enum value");
        assertEquals(StorageKeysUi.USERNAME, StorageKeysUi.valueOf("USERNAME"),
                "Should have USERNAME enum value");
        assertEquals(StorageKeysUi.PASSWORD, StorageKeysUi.valueOf("PASSWORD"),
                "Should have PASSWORD enum value");
    }

    @Test
    void valueOf_WithInvalidName_ShouldThrowException() {
        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> StorageKeysUi.valueOf("NONEXISTENT_KEY"),
                "valueOf with invalid name should throw IllegalArgumentException");
    }

    @Test
    void ordinal_ShouldReturnCorrectPosition() {
        // Given/When/Then
        assertEquals(0, StorageKeysUi.UI.ordinal(), "UI should have ordinal 0");
        assertEquals(1, StorageKeysUi.RESPONSES.ordinal(), "RESPONSES should have ordinal 1");
        assertEquals(2, StorageKeysUi.USERNAME.ordinal(), "USERNAME should have ordinal 2");
        assertEquals(3, StorageKeysUi.PASSWORD.ordinal(), "PASSWORD should have ordinal 3");
    }
}