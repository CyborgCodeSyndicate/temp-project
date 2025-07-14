package com.theairebellion.zeus.db.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class DbAssertionTargetTest {

   @ParameterizedTest
   @EnumSource(DbAssertionTarget.class)
   @DisplayName("target() should return the enum instance")
   void testTarget_ShouldReturnSelf(DbAssertionTarget target) {
      // Act
      Enum<?> result = target.target();

      // Assert
      assertSame(target, result, "The target method should return the enum instance itself");
   }

   @Test
   @DisplayName("All required enum values should exist")
   void testEnumValues_ShouldHaveRequiredValues() {
      // Assert
      assertAll(
            "DbAssertionTarget should contain all required values",
            () -> assertNotNull(DbAssertionTarget.QUERY_RESULT, "QUERY_RESULT value should exist"),
            () -> assertNotNull(DbAssertionTarget.NUMBER_ROWS, "NUMBER_ROWS value should exist"),
            () -> assertNotNull(DbAssertionTarget.COLUMNS, "COLUMNS value should exist")
      );
   }

   @Test
   @DisplayName("Enum should have exactly 3 values")
   void testEnumCount_ShouldHaveExactlyThreeValues() {
      // Assert
      assertEquals(3, DbAssertionTarget.values().length,
            "DbAssertionTarget should have exactly 3 values");
   }
}