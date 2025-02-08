package com.theairebellion.zeus.db.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theairebellion.zeus.db.json.JsonPathExtractor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DbInteractionAutoConfigurationTest {

    private final DbInteractionAutoConfiguration configuration = new DbInteractionAutoConfiguration();

    @Test
    void testDefaultObjectMapper() {
        ObjectMapper objectMapper = configuration.defaultObjectMapper();
        assertNotNull(objectMapper, "ObjectMapper should not be null");
    }

    @Test
    void testJsonPathExtractor() {
        JsonPathExtractor jsonPathExtractor = configuration.jsonPathExtractor(configuration.defaultObjectMapper());
        assertNotNull(jsonPathExtractor, "JsonPathExtractor should not be null");
    }
}