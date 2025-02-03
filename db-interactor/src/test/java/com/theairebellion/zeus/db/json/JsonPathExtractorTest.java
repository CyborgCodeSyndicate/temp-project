package com.theairebellion.zeus.db.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.theairebellion.zeus.db.exceptions.JsonPathExtractionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonPathExtractorTest {

    private JsonPathExtractor jsonPathExtractor;

    @BeforeEach
    void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        Configuration config = Configuration.builder()
                .options(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS)
                .build();
        jsonPathExtractor = new JsonPathExtractor(objectMapper, config);
    }

    @Test
    void testExtract_ShouldReturnExtractedValue() {
        // Arrange: Use a Map to represent the JSON structure
        Map<String, String> data = new HashMap<>();
        data.put("name", "John Doe");
        String jsonPath = "$.name";

        // Act: Extract using the JsonPath
        List<String> result = jsonPathExtractor.extract(data, jsonPath, List.class);

        // Assert: Check the extracted list contains the expected value
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0));
    }

    @Test
    void testExtract_ShouldThrowExceptionForInvalidJsonPath() {
        // Arrange: Valid data but invalid JSONPath
        Map<String, String> data = new HashMap<>();
        data.put("name", "John Doe");
        String invalidJsonPath = "$.nonExistentProperty";

        // Act & Assert: Expect exception due to conversion failure from empty list to String
        assertThrows(JsonPathExtractionException.class, () ->
                jsonPathExtractor.extract(data, invalidJsonPath, String.class)
        );
    }

    @Test
    void testExtract_ShouldHandleNonObjectInputGracefully() {
        // Arrange: Input is a plain string, not a JSON object
        String invalidJsonInput = "Just a string";
        String jsonPath = "$.name";

        // Act & Assert: Expect exception when applying JsonPath to a non-object
        assertThrows(JsonPathExtractionException.class, () ->
                jsonPathExtractor.extract(invalidJsonInput, jsonPath, String.class)
        );
    }
}