package com.theairebellion.zeus.api.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("RestAssertionTarget Enum Tests")
class RestAssertionTargetTest {

   @Nested
   @DisplayName("Enum Values and Validation")
   class EnumValidationTests {
      @Test
      @DisplayName("Verify enum values and count")
      void testEnumValues() {
         var values = RestAssertionTarget.values();
         assertAll(
               () -> assertEquals(3, values.length, "Should have exactly 3 enum values"),
               () -> assertNotNull(RestAssertionTarget.valueOf("STATUS"), "STATUS should be a valid enum value"),
               () -> assertNotNull(RestAssertionTarget.valueOf("BODY"), "BODY should be a valid enum value"),
               () -> assertNotNull(RestAssertionTarget.valueOf("HEADER"), "HEADER should be a valid enum value")
         );
      }
   }

   @Nested
   @DisplayName("Target Method Tests")
   class TargetMethodTests {
      @Test
      @DisplayName("Verify target() method returns correct enum value")
      void testTargetMethod() {
         assertAll(
               () -> assertEquals(RestAssertionTarget.STATUS, RestAssertionTarget.STATUS.target(), "STATUS target should return itself"),
               () -> assertEquals(RestAssertionTarget.BODY, RestAssertionTarget.BODY.target(), "BODY target should return itself"),
               () -> assertEquals(RestAssertionTarget.HEADER, RestAssertionTarget.HEADER.target(), "HEADER target should return itself")
         );
      }
   }
}