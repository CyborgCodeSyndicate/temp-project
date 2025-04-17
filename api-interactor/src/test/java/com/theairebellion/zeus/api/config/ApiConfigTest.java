package com.theairebellion.zeus.api.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("API Configuration Tests")
class ApiConfigTest {

    private static final String ALL = "ALL";

    @Test
    @DisplayName("Default values should be used when not configured")
    void testDefaultValues() {
        // Act
        var config = ConfigFactory.create(ApiConfig.class);

        // Assert
        assertAll(
                () -> assertTrue(config.restAssuredLoggingEnabled(), "Logging should be enabled by default"),
                () -> assertEquals(ALL, config.restAssuredLoggingLevel(), "Logging level should be ALL by default"),
                () -> assertTrue(config.logFullBody(), "Log full body should be enabled by default"),
                () -> assertEquals(1000, config.shortenBody(), "Shorten Body should be 1000 by default"),
                () -> assertNull(config.baseUrl(), "Base url should not have a default value")
        );
    }

}
