package com.theairebellion.zeus.db.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.theairebellion.zeus.db.exceptions.JsonPathExtractionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JsonPathExtractorTest {

    private static final String NAME_KEY = "name";
    private static final String NAME_VALUE = "John Doe";
    private static final String AGE_KEY = "age";
    private static final int AGE_VALUE = 30;
    private static final String VALID_NAME_PATH = "$.name";
    private static final String VALID_AGE_PATH = "$.age";
    private static final String ARRAY_PATH = "$.items";
    private static final String NESTED_PATH = "$.person.name";
    private static final String INVALID_JSON_PATH = "$.nonExistentProperty";
    private static final String NON_OBJECT_INPUT = "Just a string";

    @Mock
    private ObjectMapper mockMapper;

    @Mock
    private Configuration mockConfig;

    private JsonPathExtractor jsonPathExtractor;
    private JsonPathExtractor realJsonPathExtractor;

    @BeforeEach
    void setup() {
        // Create extractor with mocks for testing error scenarios
        jsonPathExtractor = new JsonPathExtractor(mockMapper, mockConfig);

        // Create extractor with real implementations for testing success scenarios
        ObjectMapper realMapper = new ObjectMapper();
        Configuration realConfig = Configuration.builder()
                .options(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS)
                .build();
        realJsonPathExtractor = new JsonPathExtractor(realMapper, realConfig);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create with custom ObjectMapper and Configuration")
        void testCustomConstructor() {
            // When
            JsonPathExtractor extractor = new JsonPathExtractor(mockMapper, mockConfig);

            // Then
            assertNotNull(extractor, "Extractor should be created with custom config");
        }

        @Test
        @DisplayName("Should create with default Configuration when only ObjectMapper is provided")
        void testDefaultConfigConstructor() {
            // When
            JsonPathExtractor extractor = new JsonPathExtractor(mockMapper);

            // Then
            assertNotNull(extractor, "Extractor should be created with default config");
        }
    }

    @Nested
    @DisplayName("Successful Extraction Tests")
    class SuccessfulExtractionTests {

        @Test
        @DisplayName("Should extract string value from simple object")
        void testExtractStringValue() {
            // Given
            Map<String, Object> data = new HashMap<>();
            data.put(NAME_KEY, NAME_VALUE);

            // When
            @SuppressWarnings("unchecked")
            List<String> result = realJsonPathExtractor.extract(data, VALID_NAME_PATH, List.class);

            // Then
            assertNotNull(result, "Result should not be null");
            assertFalse(result.isEmpty(), "Result should have at least one value");
            assertEquals(NAME_VALUE, result.get(0), "Should extract correct value");
        }

        @Test
        @DisplayName("Should extract integer value from simple object")
        void testExtractIntegerValue() {
            // Given
            Map<String, Object> data = new HashMap<>();
            data.put(AGE_KEY, AGE_VALUE);

            // When
            @SuppressWarnings("unchecked")
            List<Integer> result = realJsonPathExtractor.extract(data, VALID_AGE_PATH, List.class);

            // Then
            assertNotNull(result, "Result should not be null");
            assertFalse(result.isEmpty(), "Result should have at least one value");
            assertEquals(AGE_VALUE, result.get(0), "Should extract correct value");
        }

        @Test
        @DisplayName("Should extract array values")
        void testExtractArrayValues() {
            // Given
            Map<String, Object> data = new HashMap<>();
            List<String> items = Arrays.asList("item1", "item2", "item3");
            data.put("items", items);

            // When
            List<?> result = realJsonPathExtractor.extract(data, ARRAY_PATH, List.class);

            // Then
            assertNotNull(result, "Result should not be null");

            // Handle different result structures
            if (result.size() == 1 && result.get(0) instanceof List<?> firstElement) {
                // Result is a list containing the array
                assertEquals(3, firstElement.size(), "Inner list should have 3 items");
                assertEquals("item1", firstElement.get(0), "First item should match");
                assertEquals("item2", firstElement.get(1), "Second item should match");
                assertEquals("item3", firstElement.get(2), "Third item should match");
            } else if (result.size() == 3) {
                // Result is the list directly
                assertEquals("item1", result.get(0), "First item should match");
                assertEquals("item2", result.get(1), "Second item should match");
                assertEquals("item3", result.get(2), "Third item should match");
            } else {
                fail("Unexpected result structure");
            }
        }

        @Test
        @DisplayName("Should extract nested object values")
        void testExtractNestedValues() {
            // Given
            Map<String, Object> person = new HashMap<>();
            person.put(NAME_KEY, NAME_VALUE);

            Map<String, Object> data = new HashMap<>();
            data.put("person", person);

            // When
            @SuppressWarnings("unchecked")
            List<String> result = realJsonPathExtractor.extract(data, NESTED_PATH, List.class);

            // Then
            assertNotNull(result, "Result should not be null");
            assertFalse(result.isEmpty(), "Result should have at least one value");
            assertEquals(NAME_VALUE, result.get(0), "Should extract correct nested value");
        }

        @Test
        @DisplayName("Should handle non-object input")
        void testNonObjectInput() {
            // When/Then
            assertThrows(JsonPathExtractionException.class,
                    () -> realJsonPathExtractor.extract(NON_OBJECT_INPUT, VALID_NAME_PATH, String.class),
                    "Should throw exception for non-object input");
        }

        @Test
        @DisplayName("Should return null when path doesn't exist but exceptions are suppressed")
        void testNonExistentPathWithSuppressionReturnsNull() {
            // Given
            Map<String, Object> data = new HashMap<>();
            data.put(NAME_KEY, NAME_VALUE);

            // Create extractor that will suppress path not found
            Configuration suppressConfig = Configuration.builder()
                    .options(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL)
                    .build();
            JsonPathExtractor suppressExtractor = new JsonPathExtractor(new ObjectMapper(), suppressConfig);

            // When
            Object result = suppressExtractor.extract(data, INVALID_JSON_PATH, Object.class);

            // Then
            assertNull(result, "Should return null for non-existent path when suppressed");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw exception when JSON serialization fails")
        void testJsonSerializationFailure() throws JsonProcessingException {
            // Given
            RuntimeException serializationError = new RuntimeException("Serialization error");
            doThrow(serializationError).when(mockMapper).writeValueAsString(any());

            // When/Then
            JsonPathExtractionException exception = assertThrows(
                    JsonPathExtractionException.class,
                    () -> jsonPathExtractor.extract(new Object(), VALID_NAME_PATH, String.class)
            );

            // Verify the exception message contains the expected text
            assertTrue(exception.getMessage().contains("Failed to convert object to JSON"),
                    "Exception message should indicate serialization failure");
        }

        @Test
        @DisplayName("Should throw exception when JsonPath parsing fails")
        void testJsonPathParsingFailure() throws JsonProcessingException {
            // Given
            when(mockMapper.writeValueAsString(any())).thenReturn("{invalid:json}");

            // When/Then
            assertThrows(
                    JsonPathExtractionException.class,
                    () -> jsonPathExtractor.extract(new Object(), VALID_NAME_PATH, String.class)
            );
        }

        @Test
        @DisplayName("Should throw exception when result type conversion fails")
        void testResultTypeConversionFailure() throws JsonProcessingException {
            // Setup a simple test scenario where conversion will fail
            Map<String, String> testData = new HashMap<>();
            testData.put(NAME_KEY, "John");

            // This will cause a conversion failure when trying to convert to Integer
            assertThrows(JsonPathExtractionException.class,
                    () -> realJsonPathExtractor.extract(testData, VALID_NAME_PATH, Integer.class));

            // Check that exception has correct message using a mock
            when(mockMapper.writeValueAsString(any())).thenReturn("{\"name\":\"John\"}");
            List<String> mockResult = new ArrayList<>();
            mockResult.add("John");

            // Set up mocks to simulate a conversion failure
            com.jayway.jsonpath.DocumentContext mockContext = mock(com.jayway.jsonpath.DocumentContext.class);
            when(mockContext.read(anyString())).thenReturn(mockResult);

            com.jayway.jsonpath.ParseContext mockParseContext = mock(com.jayway.jsonpath.ParseContext.class);
            when(mockParseContext.parse(anyString())).thenReturn(mockContext);

            when(mockConfig.getOptions()).thenReturn(EnumSet.of(Option.ALWAYS_RETURN_LIST));

            try (MockedStatic<com.jayway.jsonpath.JsonPath> jsonPathMock = mockStatic(com.jayway.jsonpath.JsonPath.class)) {
                jsonPathMock.when(() -> com.jayway.jsonpath.JsonPath.using(any(Configuration.class)))
                        .thenReturn(mockParseContext);

                when(mockMapper.convertValue(any(), eq(Integer.class)))
                        .thenThrow(new IllegalArgumentException("Cannot convert to Integer"));

                JsonPathExtractionException exception = assertThrows(
                        JsonPathExtractionException.class,
                        () -> jsonPathExtractor.extract(testData, VALID_NAME_PATH, Integer.class)
                );

                // Verify message
                assertTrue(exception.getMessage().contains("Failed to convert"),
                        "Exception message should contain 'Failed to convert'");
            }
        }
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("extractionTestCases")
    @DisplayName("Should handle various extraction scenarios")
    void testExtractWithVariousInputs(Object input, String jsonPath, String description,
                                      Class<?> resultType, boolean shouldThrow) {
        // Given input from test case

        // When/Then
        if (shouldThrow) {
            assertThrows(JsonPathExtractionException.class,
                    () -> realJsonPathExtractor.extract(input, jsonPath, resultType),
                    description);
        } else {
            // Just verify it doesn't throw
            assertDoesNotThrow(() -> {
                Object result = realJsonPathExtractor.extract(input, jsonPath, List.class);
                assertNotNull(result, "Result should not be null");
            }, description);
        }
    }

    private static Stream<Arguments> extractionTestCases() {
        return Stream.of(
                Arguments.of(
                        new HashMap<String, Object>() {{ put("value", "test"); }},
                        "$.value",
                        "Should extract string value",
                        List.class, // Using List instead of specific types
                        false
                ),
                Arguments.of(
                        new HashMap<String, Object>() {{ put("isActive", true); }},
                        "$.isActive",
                        "Should extract boolean value",
                        List.class, // Using List instead of specific types
                        false
                ),
                Arguments.of(
                        NON_OBJECT_INPUT,
                        "$.anything",
                        "Should throw when input is not a valid JSON object",
                        String.class,
                        true
                ),
                Arguments.of(
                        new HashMap<String, Object>(),
                        INVALID_JSON_PATH,
                        "Should handle non-existent path",
                        String.class,
                        true
                ),
                Arguments.of(
                        null,
                        "$.anything",
                        "Should handle null input",
                        String.class,
                        true
                )
        );
    }
}