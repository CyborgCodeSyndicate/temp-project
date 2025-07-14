package com.theairebellion.zeus.api.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("StorageKeysApi Tests")
class StorageKeysApiTest {

   @Test
   @DisplayName("Should have expected number of enum values")
   void shouldHaveExpectedNumberOfEnumValues() {
      // Act & Assert
      assertThat(StorageKeysApi.values()).hasSize(3);
   }

   @ParameterizedTest
   @EnumSource(StorageKeysApi.class)
   @DisplayName("Should be able to get enum value by name")
   void shouldBeAbleToGetEnumValueByName(StorageKeysApi key) {
      // Act & Assert
      assertThatCode(() -> StorageKeysApi.valueOf(key.name()))
            .doesNotThrowAnyException();

      assertThat(StorageKeysApi.valueOf(key.name())).isEqualTo(key);
   }

   @Test
   @DisplayName("Should contain API, USERNAME, and PASSWORD values")
   void shouldContainExpectedValues() {
      // Act & Assert
      assertThat(StorageKeysApi.values())
            .contains(StorageKeysApi.API)
            .contains(StorageKeysApi.USERNAME)
            .contains(StorageKeysApi.PASSWORD);
   }

   @Test
   @DisplayName("Should not throw exception when using valueOf with valid names")
   void shouldNotThrowExceptionWhenUsingValidNames() {
      // Act & Assert
      assertThatCode(() -> {
         StorageKeysApi.valueOf("API");
         StorageKeysApi.valueOf("USERNAME");
         StorageKeysApi.valueOf("PASSWORD");
      }).doesNotThrowAnyException();
   }
}