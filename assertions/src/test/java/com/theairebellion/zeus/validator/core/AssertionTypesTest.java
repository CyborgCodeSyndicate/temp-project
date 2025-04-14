package com.theairebellion.zeus.validator.core;

import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssertionTypesTest {

   @ParameterizedTest
   @EnumSource(AssertionTypes.class)
   @DisplayName("Each enum value should return itself as type and have a non-null supported type")
   void testEachEnum(AssertionTypes type) {
      assertAll(
            () -> assertSame(type, type.type(), "type() should return the enum itself"),
            () -> assertNotNull(type.getSupportedType(), "supportedType should not be null")
      );
   }


   @Test
   @DisplayName("Verify all enum values exist and count matches")
   void testEnumValues() {
      // Given
      AssertionTypes[] values = AssertionTypes.values();

      // Then
      assertEquals(19, values.length, "Should have 19 enum values");

      // Verify each enum exists
      assertAll(
            () -> assertTrue(containsEnumValue(values, "IS"), "Should contain IS"),
            () -> assertTrue(containsEnumValue(values, "NOT"), "Should contain NOT"),
            () -> assertTrue(containsEnumValue(values, "CONTAINS"), "Should contain CONTAINS"),
            () -> assertTrue(containsEnumValue(values, "NOT_NULL"), "Should contain NOT_NULL"),
            () -> assertTrue(containsEnumValue(values, "ALL_NOT_NULL"), "Should contain ALL_NOT_NULL"),
            () -> assertTrue(containsEnumValue(values, "IS_NULL"), "Should contain IS_NULL"),
            () -> assertTrue(containsEnumValue(values, "ALL_NULL"), "Should contain ALL_NULL"),
            () -> assertTrue(containsEnumValue(values, "GREATER_THAN"), "Should contain GREATER_THAN"),
            () -> assertTrue(containsEnumValue(values, "LESS_THAN"), "Should contain LESS_THAN"),
            () -> assertTrue(containsEnumValue(values, "CONTAINS_ALL"), "Should contain CONTAINS_ALL"),
            () -> assertTrue(containsEnumValue(values, "CONTAINS_ANY"), "Should contain CONTAINS_ANY"),
            () -> assertTrue(containsEnumValue(values, "STARTS_WITH"), "Should contain STARTS_WITH"),
            () -> assertTrue(containsEnumValue(values, "ENDS_WITH"), "Should contain ENDS_WITH"),
            () -> assertTrue(containsEnumValue(values, "LENGTH"), "Should contain LENGTH"),
            () -> assertTrue(containsEnumValue(values, "MATCHES_REGEX"), "Should contain MATCHES_REGEX"),
            () -> assertTrue(containsEnumValue(values, "EMPTY"), "Should contain EMPTY"),
            () -> assertTrue(containsEnumValue(values, "NOT_EMPTY"), "Should contain NOT_EMPTY"),
            () -> assertTrue(containsEnumValue(values, "BETWEEN"), "Should contain BETWEEN"),
            () -> assertTrue(containsEnumValue(values, "EQUALS_IGNORE_CASE"), "Should contain EQUALS_IGNORE_CASE")
      );
   }


   @Test
   @DisplayName("Object-related assertion types should have correct supported types")
   void testObjectTypes() {
      assertAll(
            () -> assertEquals(Object.class, AssertionTypes.IS.getSupportedType()),
            () -> assertEquals(Object.class, AssertionTypes.NOT.getSupportedType()),
            () -> assertEquals(Object.class, AssertionTypes.NOT_NULL.getSupportedType()),
            () -> assertEquals(Object.class, AssertionTypes.IS_NULL.getSupportedType()),
            () -> assertEquals(Object.class, AssertionTypes.LENGTH.getSupportedType())
      );
   }


   @Test
   @DisplayName("String-related assertion types should have correct supported types")
   void testStringTypes() {
      assertAll(
            () -> assertEquals(String.class, AssertionTypes.CONTAINS.getSupportedType()),
            () -> assertEquals(String.class, AssertionTypes.STARTS_WITH.getSupportedType()),
            () -> assertEquals(String.class, AssertionTypes.ENDS_WITH.getSupportedType()),
            () -> assertEquals(String.class, AssertionTypes.MATCHES_REGEX.getSupportedType()),
            () -> assertEquals(String.class, AssertionTypes.EQUALS_IGNORE_CASE.getSupportedType())
      );
   }


   @Test
   @DisplayName("Number-related assertion types should have correct supported types")
   void testNumberTypes() {
      assertAll(
            () -> assertEquals(Number.class, AssertionTypes.GREATER_THAN.getSupportedType()),
            () -> assertEquals(Number.class, AssertionTypes.LESS_THAN.getSupportedType()),
            () -> assertEquals(Number.class, AssertionTypes.BETWEEN.getSupportedType())
      );
   }


   @Test
   @DisplayName("Collection-related assertion types should have correct supported types")
   void testCollectionTypes() {
      assertAll(
            () -> assertEquals(Collection.class, AssertionTypes.ALL_NOT_NULL.getSupportedType()),
            () -> assertEquals(Collection.class, AssertionTypes.ALL_NULL.getSupportedType()),
            () -> assertEquals(Collection.class, AssertionTypes.CONTAINS_ALL.getSupportedType()),
            () -> assertEquals(Collection.class, AssertionTypes.CONTAINS_ANY.getSupportedType()),
            () -> assertEquals(Collection.class, AssertionTypes.EMPTY.getSupportedType()),
            () -> assertEquals(Collection.class, AssertionTypes.NOT_EMPTY.getSupportedType())
      );
   }


   // Helper method to check if an enum value exists by name
   private boolean containsEnumValue(AssertionTypes[] values, String name) {
      for (AssertionTypes value : values) {
         if (value.name().equals(name)) {
            return true;
         }
      }
      return false;
   }

}