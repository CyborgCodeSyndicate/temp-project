package com.theairebellion.zeus.validator.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssertionTypeTest {

   @Test
   @DisplayName("AssertionType implementation should return the enum itself and supported type")
   void testAssertionTypeImplementation() {
      // Given
      AssertionType<?> type = new AssertionType() {
         @Override
         public Enum<?> type() {
            return TestEnum.TEST_TYPE;
         }

         @Override
         public Class<?> getSupportedType() {
            return String.class;
         }
      };

      // When
      Enum<?> typeResult = type.type();
      Class<?> supportedType = type.getSupportedType();

      // Then
      assertAll(
            () -> assertEquals(TestEnum.TEST_TYPE, typeResult, "type() should return the enum"),
            () -> assertEquals(String.class, supportedType, "getSupportedType() should return the class")
      );
   }

   @Test
   @DisplayName("AssertionType can be implemented in an enum")
   void testAssertionTypeInEnum() {
      // Given
      AssertionType type = TypeEnum.STRING_TYPE;

      // When
      Enum<?> typeResult = type.type();
      Class<?> supportedType = type.getSupportedType();

      // Then
      assertAll(
            () -> assertEquals(TypeEnum.STRING_TYPE, typeResult, "Enum implementation should return itself"),
            () -> assertEquals(String.class, supportedType, "getSupportedType() should return the correct class")
      );
   }

   // Test enums for testing
   private enum TestEnum {
      TEST_TYPE,
      ANOTHER_TYPE
   }

   private enum TypeEnum implements AssertionType<TypeEnum> {
      STRING_TYPE(String.class),
      NUMBER_TYPE(Number.class);

      private final Class<?> supportedType;

      TypeEnum(Class<?> supportedType) {
         this.supportedType = supportedType;
      }

      @Override
      public TypeEnum type() {
         return this;
      }

      @Override
      public Class<?> getSupportedType() {
         return supportedType;
      }
   }
}