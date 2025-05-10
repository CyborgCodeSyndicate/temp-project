package com.theairebellion.zeus.framework.storage;

import com.theairebellion.zeus.framework.storage.mock.MockEnum;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataExtractorImpl Unit Tests")
class DataExtractorImplUnitTest {

   @Nested
   @DisplayName("Constructor tests")
   class ConstructorTests {

      @Test
      @DisplayName("Should properly initialize with subKey")
      void testConstructorWithSubKey() {
         // Given
         Function<Object, String> extractionLogic = raw -> ((String) raw).toUpperCase();

         // When
         DataExtractorImpl<String> extractor = new DataExtractorImpl<>(
               MockEnum.KEY2, MockEnum.KEY1, extractionLogic);

         // Then
         assertEquals(MockEnum.KEY2, extractor.getSubKey(),
               "SubKey should match the value provided in constructor");
         assertEquals(MockEnum.KEY1, extractor.getKey(),
               "Key should match the value provided in constructor");

         // Verify extraction logic works
         String result = extractor.extract("hello");
         assertEquals("HELLO", result,
               "Extraction logic should transform the input as expected");
      }

      @Test
      @DisplayName("Should properly initialize without subKey")
      void testConstructorWithoutSubKey() {
         // Given
         Function<Object, Integer> extractionLogic = raw -> ((String) raw).length();

         // When
         DataExtractorImpl<Integer> extractor = new DataExtractorImpl<>(
               MockEnum.KEY1, extractionLogic);

         // Then
         assertNull(extractor.getSubKey(),
               "SubKey should be null when constructor without subKey is used");
         assertEquals(MockEnum.KEY1, extractor.getKey(),
               "Key should match the value provided in constructor");

         // Verify extraction logic works
         Integer result = extractor.extract("test");
         assertEquals(4, result,
               "Extraction logic should calculate the length of the string");
      }
   }

   @Nested
   @DisplayName("Extraction function tests")
   class ExtractionFunctionTests {

      @Test
      @DisplayName("Should apply extraction function to various input types")
      void testExtractionFunctionWithVariousInputs() {
         // Given
         Function<Object, String> stringExtractor = obj -> obj != null ? obj.toString() : "null";
         DataExtractorImpl<String> extractor = new DataExtractorImpl<>(
               MockEnum.KEY1, stringExtractor);

         // When/Then
         assertEquals("42", extractor.extract(42),
               "Should extract integer value as string");
         assertEquals("true", extractor.extract(true),
               "Should extract boolean value as string");

         // For null handling, we need an extraction function that handles null explicitly
         assertEquals("null", extractor.extract(null),
               "Should handle null input with our null-safe extractor");
      }

      @Test
      @DisplayName("Should support complex extraction transformations")
      void testComplexExtractionFunction() {
         // Given - A function that processes different types differently
         Function<Object, Object> complexExtractor = obj -> {
            if (obj instanceof String) {
               return ((String) obj).length();
            } else if (obj instanceof Integer) {
               return (Integer) obj * 2;
            }
            return "unknown";
         };

         DataExtractorImpl<Object> extractor = new DataExtractorImpl<>(
               MockEnum.KEY1, complexExtractor);

         // When/Then
         assertEquals(5, extractor.extract("hello"),
               "Should extract string length for string input");
         assertEquals(84, extractor.extract(42),
               "Should double the value for integer input");
         assertEquals("unknown", extractor.extract(true),
               "Should return default for other types");
      }
   }
}