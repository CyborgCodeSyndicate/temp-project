package com.theairebellion.zeus.db.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theairebellion.zeus.db.json.JsonPathExtractor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DbInteractionAutoConfigurationTest {

    private final DbInteractionAutoConfiguration configuration = new DbInteractionAutoConfiguration();

    @Test
    void testDefaultObjectMapper() {
        // Act
        ObjectMapper objectMapper = configuration.defaultObjectMapper();

        // Assert
        assertNotNull(objectMapper, "ObjectMapper should not be null");
    }

    @Test
    void testJsonPathExtractor() {
        // Act
        JsonPathExtractor jsonPathExtractor = configuration.jsonPathExtractor(configuration.defaultObjectMapper());

        // Assert
        assertNotNull(jsonPathExtractor, "JsonPathExtractor should not be null");
    }
}