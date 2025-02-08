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

    private static final String NAME_KEY = "name";
    private static final String NAME_VALUE = "John Doe";
    private static final String VALID_JSON_PATH = "$.name";
    private static final String INVALID_JSON_PATH = "$.nonExistentProperty";
    private static final String NON_OBJECT_INPUT = "Just a string";

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
        Map<String, String> data = new HashMap<>();
        data.put(NAME_KEY, NAME_VALUE);

        List<String> result = jsonPathExtractor.extract(data, VALID_JSON_PATH, List.class);

        assertEquals(1, result.size());
        assertEquals(NAME_VALUE, result.get(0));
    }

    @Test
    void testExtract_ShouldThrowExceptionForInvalidJsonPath() {
        Map<String, String> data = new HashMap<>();
        data.put(NAME_KEY, NAME_VALUE);

        assertThrows(JsonPathExtractionException.class, () ->
                jsonPathExtractor.extract(data, INVALID_JSON_PATH, String.class)
        );
    }

    @Test
    void testExtract_ShouldHandleNonObjectInputGracefully() {
        assertThrows(JsonPathExtractionException.class, () ->
                jsonPathExtractor.extract(NON_OBJECT_INPUT, VALID_JSON_PATH, String.class)
        );
    }
}