package com.theairebellion.zeus.framework.storage;

import com.theairebellion.zeus.framework.storage.mock.MockEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataExtractorImpl tests")
class DataExtractorImplTest {

    @Nested
    @DisplayName("Constructor and field access tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should initialize correctly with sub-key")
        void testExtractorWithSubKey() {
            // Given
            Function<Object, String> extractionLogic = raw -> raw.toString().toUpperCase();

            // When
            DataExtractorImpl<String> extractor = new DataExtractorImpl<>(
                    MockEnum.KEY2, MockEnum.KEY1, extractionLogic);

            // Then
            assertEquals(MockEnum.KEY2, extractor.getSubKey(), "SubKey should match the provided value");
            assertEquals(MockEnum.KEY1, extractor.getKey(), "Key should match the provided value");

            // Verify extraction works
            String result = extractor.extract("hello");
            assertEquals("HELLO", result, "Extraction logic should be applied correctly");
        }

        @Test
        @DisplayName("Should initialize correctly without sub-key")
        void testExtractorWithoutSubKey() {
            // Given
            Function<Object, Integer> extractionLogic = raw -> ((String) raw).length();

            // When
            DataExtractorImpl<Integer> extractor = new DataExtractorImpl<>(MockEnum.KEY1, extractionLogic);

            // Then
            assertNull(extractor.getSubKey(), "SubKey should be null when not provided");
            assertEquals(MockEnum.KEY1, extractor.getKey(), "Key should match the provided value");

            // Verify extraction works
            Integer result = extractor.extract("abcd");
            assertEquals(4, result, "Extraction logic should be applied correctly");
        }
    }

    @Nested
    @DisplayName("Extraction functionality tests")
    class ExtractionTests {

        @Test
        @DisplayName("Should handle various input types")
        void testExtractionWithVariousInputs() {
            // Given
            DataExtractorImpl<String> extractor = new DataExtractorImpl<>(
                    MockEnum.KEY1, obj -> (obj == null) ? "NULL" : obj.toString());

            // When/Then
            assertEquals("123", extractor.extract(123), "Should handle numeric input");
            assertEquals("true", extractor.extract(true), "Should handle boolean input");
            assertEquals("NULL", extractor.extract(null), "Should handle null input");

            // For array input, use Arrays.toString instead of direct toString
            Object[] emptyArray = new Object[0];
            assertTrue(extractor.extract(emptyArray).contains("Object"),
                    "Should contain class name for array");
        }

        @Test
        @DisplayName("Should apply complex transformation")
        void testComplexExtraction() {
            // Given
            DataExtractorImpl<Integer> extractor = new DataExtractorImpl<>(
                    MockEnum.KEY1,
                    obj -> {
                        if (obj instanceof String) {
                            return ((String) obj).length();
                        } else if (obj instanceof Number) {
                            return ((Number) obj).intValue() * 2;
                        }
                        return -1;
                    }
            );

            // When/Then
            assertEquals(5, extractor.extract("hello"), "Should calculate string length");
            assertEquals(20, extractor.extract(10), "Should multiply number by 2");
            assertEquals(-1, extractor.extract(new Object()), "Should return default for other types");
        }
    }
}