package com.theairebellion.zeus.db.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StorageKeysDb enum tests")
class StorageKeysDbTest {

    @Test
    @DisplayName("DB enum constant should have the correct name")
    void dbEnumShouldHaveCorrectName() {
        // When
        String enumName = StorageKeysDb.DB.name();

        // Then
        assertThat(enumName)
                .as("StorageKeysDb.DB should have name 'DB'")
                .isEqualTo("DB");
    }

    @ParameterizedTest
    @EnumSource(StorageKeysDb.class)
    @DisplayName("All enum constants should return non-null toString values")
    void allEnumConstantsShouldHaveValidToString(StorageKeysDb key) {
        // When
        String stringValue = key.toString();

        // Then
        assertThat(stringValue)
                .as("Enum toString() should not return null")
                .isNotNull();
    }

    @Test
    @DisplayName("valueOf should correctly map string to enum constant")
    void valueOfShouldMapStringToEnumConstant() {
        // When
        StorageKeysDb enumValue = StorageKeysDb.valueOf("DB");

        // Then
        assertThat(enumValue)
                .as("valueOf('DB') should return StorageKeysDb.DB")
                .isEqualTo(StorageKeysDb.DB);
    }
}