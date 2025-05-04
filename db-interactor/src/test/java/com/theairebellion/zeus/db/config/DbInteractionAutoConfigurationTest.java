package com.theairebellion.zeus.db.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theairebellion.zeus.db.json.JsonPathExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DbInteractionAutoConfigurationTest {

    @InjectMocks
    private DbInteractionAutoConfiguration configuration;

    @Test
    @DisplayName("Should create default ObjectMapper")
    void testDefaultObjectMapper() {
        // When
        ObjectMapper objectMapper = configuration.defaultObjectMapper();

        // Then
        assertNotNull(objectMapper, "ObjectMapper should not be null");
    }

    @Test
    @DisplayName("Should create JsonPathExtractor with copied ObjectMapper")
    void testJsonPathExtractor() {
        // Given
        ObjectMapper originalMapper = new ObjectMapper();

        // When
        JsonPathExtractor jsonPathExtractor = configuration.jsonPathExtractor(originalMapper);

        // Then
        assertNotNull(jsonPathExtractor, "JsonPathExtractor should not be null");

        try {
            java.lang.reflect.Field field = JsonPathExtractor.class.getDeclaredField("objectMapper");
            field.setAccessible(true);
            Object copiedMapper = field.get(jsonPathExtractor);

            assertNotNull(copiedMapper, "Copied ObjectMapper should not be null");
            assertNotSame(originalMapper, copiedMapper, "ObjectMapper should be a different instance (copied)");
            assertEquals(ObjectMapper.class, copiedMapper.getClass(), "Should be an instance of ObjectMapper");
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should create ObjectMapper with appropriate configuration")
    void testObjectMapperConfiguration() {
        // When
        ObjectMapper objectMapper = configuration.defaultObjectMapper();

        // Then
        assertNotNull(objectMapper, "ObjectMapper should not be null");

        try {
            TestData testData = new TestData("test", 123);
            String json = objectMapper.writeValueAsString(testData);
            assertNotNull(json, "Should be able to serialize objects");
            assertTrue(json.contains("test"), "JSON should contain field values");
        } catch (Exception e) {
            fail("ObjectMapper should be able to serialize objects: " + e.getMessage());
        }
    }

    // Simple test class for serialization
    private static class TestData {
        private final String name;
        private final int value;

        public TestData(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }

    @Test
    @DisplayName("Full auto-configuration should create properly configured beans")
    void testFullConfiguration() {
        // When
        ObjectMapper objectMapper = configuration.defaultObjectMapper();
        JsonPathExtractor jsonPathExtractor = configuration.jsonPathExtractor(objectMapper);

        // Then
        assertNotNull(objectMapper, "ObjectMapper should not be null");
        assertNotNull(jsonPathExtractor, "JsonPathExtractor should not be null");
    }
}