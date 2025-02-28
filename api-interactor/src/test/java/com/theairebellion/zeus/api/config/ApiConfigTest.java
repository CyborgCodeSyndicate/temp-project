package com.theairebellion.zeus.api.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("API Configuration Tests")
class ApiConfigTest {

    private static final String CUSTOM_URL = "https://custom-url.com";
    private static final String LOGGING_ENABLED_KEY = "api.restassured.logging.enabled";
    private static final String LOGGING_LEVEL_KEY = "api.restassured.logging.level";
    private static final String ALL = "ALL";
    private static final String NONE = "NONE";

    @Test
    @DisplayName("Default values should be used when not configured")
    void testDefaultValues() {
        // Act
        var config = ConfigFactory.create(ApiConfig.class);

        // Assert
        assertAll(
                () -> assertTrue(config.restAssuredLoggingEnabled(), "Logging should be enabled by default"),
                () -> assertEquals(ALL, config.restAssuredLoggingLevel(), "Logging level should be ALL by default")
        );
    }

    @Test
    @DisplayName("Custom properties should override defaults when passed directly")
    void testCustomProperties() {
        // Arrange
        Map<String, String> props = Map.of(
                LOGGING_ENABLED_KEY, "false",
                LOGGING_LEVEL_KEY, NONE
        );

        // Act
        var config = ConfigFactory.create(ApiConfig.class, props);

        // Assert
        assertAll(
                () -> assertFalse(config.restAssuredLoggingEnabled(), "Logging should be disabled with custom property"),
                () -> assertEquals(NONE, config.restAssuredLoggingLevel(), "Logging level should match custom property")
        );
    }
}