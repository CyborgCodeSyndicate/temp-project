package com.theairebellion.zeus.validator.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssertionTargetTest {

   @Test
   @DisplayName("AssertionTarget implementation should return the enum itself")
   void testAssertionTargetImplementation() {
      // Given
      AssertionTarget target = new AssertionTarget() {
         @Override
         public Enum<?> target() {
            return TestEnum.TEST_VALUE;
         }
      };

      // When
      Enum<?> result = target.target();

      // Then
      assertEquals(TestEnum.TEST_VALUE, result, "target() should return the enum");
   }

   @Test
   @DisplayName("AssertionTarget can be implemented with lambda")
   void testAssertionTargetWithLambda() {
      // Given
      AssertionTarget target = () -> TestEnum.ANOTHER_VALUE;

      // When
      Enum<?> result = target.target();

      // Then
      assertEquals(TestEnum.ANOTHER_VALUE, result, "Lambda implementation should work");
   }

   @Test
   @DisplayName("AssertionTarget can be implemented in an enum")
   void testAssertionTargetInEnum() {
      // Given
      AssertionTarget target = TargetEnum.TARGET_1;

      // When
      Enum<?> result = target.target();

      // Then
      assertEquals(TargetEnum.TARGET_1, result, "Enum implementation should return itself");
   }

   // Test enums for testing
   private enum TestEnum {
      TEST_VALUE,
      ANOTHER_VALUE
   }

   private enum TargetEnum implements AssertionTarget {
      TARGET_1,
      TARGET_2;

      @Override
      public Enum<?> target() {
         return this;
      }
   }
}