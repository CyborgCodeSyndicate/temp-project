package com.theairebellion.zeus.framework.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataExtractorsTest tests")
class DataExtractorsTestUnitTest {

    @Nested
    @DisplayName("staticTestData extractor tests")
    class StaticTestDataTests {

        @Test
        @DisplayName("Should extract existing value from test data")
        void testStaticTestDataExtractionReturnsValue() {
            // Given
            Map<String, Object> testData = new HashMap<>();
            testData.put("myKey", "expectedValue");

            // When
            DataExtractor<String> extractor = DataExtractorsTest.staticTestData("myKey");
            String result = extractor.extract(testData);

            // Then
            assertNull(extractor.getSubKey(), "SubKey should be null");
            assertEquals(StorageKeysTest.STATIC_DATA, extractor.getKey(),
                    "Key should be StorageKeysTest.STATIC_DATA");
            assertEquals("expectedValue", result, "Extracted value should match expected");
        }

        @Test
        @DisplayName("Should return null when key doesn't exist")
        void testStaticTestDataExtractionReturnsNullWhenMissing() {
            // Given
            Map<String, Object> testData = new HashMap<>();

            // When
            DataExtractor<String> extractor = DataExtractorsTest.staticTestData("nonExistentKey");
            String result = extractor.extract(testData);

            // Then
            assertNull(result, "Should return null for non-existent key");
        }

        @Test
        @DisplayName("Should handle null input data")
        void testStaticTestDataExtractionWithNullData() {
            // When
            DataExtractor<String> extractor = DataExtractorsTest.staticTestData("anyKey");

            // Then
            assertThrows(NullPointerException.class,
                    () -> extractor.extract(null),
                    "Should throw NullPointerException for null input data");
        }

        @Test
        @DisplayName("Should handle different value types")
        void testStaticTestDataExtractionWithDifferentTypes() {
            // Given
            Map<String, Object> testData = new HashMap<>();
            testData.put("stringKey", "string");
            testData.put("intKey", 42);
            testData.put("boolKey", true);

            // When/Then
            DataExtractor<String> stringExtractor = DataExtractorsTest.staticTestData("stringKey");
            assertEquals("string", stringExtractor.extract(testData), "Should extract string value");

            DataExtractor<Integer> intExtractor = DataExtractorsTest.staticTestData("intKey");
            assertEquals(42, intExtractor.extract(testData), "Should extract integer value");

            DataExtractor<Boolean> boolExtractor = DataExtractorsTest.staticTestData("boolKey");
            assertEquals(true, boolExtractor.extract(testData), "Should extract boolean value");
        }
    }
}