package com.theairebellion.zeus.ui.service.tables;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class DefaultTableTypesTest {

   @Test
   void defaultEnumValue_shouldBePresent() {
      // Act
      DefaultTableTypes type = DefaultTableTypes.DEFAULT;

      // Assert
      assertNotNull(type, "DEFAULT enum value should not be null");
      assertEquals("DEFAULT", type.name(), "Enum name should be 'DEFAULT'");
   }

   @Test
   void getType_shouldReturnItself() {
      // Arrange
      DefaultTableTypes type = DefaultTableTypes.DEFAULT;

      // Act
      Enum<?> result = type.getType();

      // Assert
      assertSame(type, result, "getType() should return the enum instance itself");
   }
}