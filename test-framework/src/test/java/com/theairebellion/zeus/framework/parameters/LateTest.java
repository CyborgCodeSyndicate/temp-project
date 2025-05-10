package com.theairebellion.zeus.framework.parameters;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LateTest {

   private static final String TEST_STRING = "testString";
   private static final Integer TEST_INTEGER = 100;

   @Nested
   @DisplayName("Late.join() tests")
   class JoinTests {

      @Test
      @DisplayName("Should return the correct string value")
      void testJoinWithString() {
         // Given
         Late<String> late = () -> TEST_STRING;

         // When
         String result = late.join();

         // Then
         assertEquals(TEST_STRING, result, "Late.join() should return the correct string value");
      }

      @Test
      @DisplayName("Should return the correct integer value")
      void testJoinWithInteger() {
         // Given
         Late<Integer> late = () -> TEST_INTEGER;

         // When
         Integer result = late.join();

         // Then
         assertEquals(TEST_INTEGER, result, "Late.join() should return the correct integer value");
      }

      @ParameterizedTest(name = "Should join with {0} of type {1}")
      @MethodSource("provideTypesForJoin")
      @DisplayName("Should join with various data types")
      <T> void testJoinWithVariousTypes(T value, Class<T> type) {
         // Given
         Late<T> late = () -> value;

         // When
         T result = late.join();

         // Then
         assertNotNull(result, "Result should not be null");
         assertEquals(value, result, "Late.join() should return the correct value");
      }

      private static Stream<Arguments> provideTypesForJoin() {
         return Stream.of(
               Arguments.of(Boolean.TRUE, Boolean.class),
               Arguments.of(42L, Long.class),
               Arguments.of(3.14, Double.class),
               Arguments.of("Hello, world!", String.class)
         );
      }
   }
}