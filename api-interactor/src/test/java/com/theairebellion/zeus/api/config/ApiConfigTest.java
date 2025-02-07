package com.theairebellion.zeus.api.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiConfigTest {

    @Test
    void testApiConfig_DefaultValues() {
        var config = ConfigFactory.create(ApiConfig.class);

        assertAll(
                () -> assertTrue(config.restAssuredLoggingEnabled()),
                () -> assertEquals("ALL", config.restAssuredLoggingLevel()),
                () -> System.out.println("baseUrl from config = " + config.baseUrl())
        );
    }

    @Test
    void testApiConfig_WithCustomProperties() {
        var config = ConfigFactory.create(ApiConfig.class, Map.of(
                "api.base.url", "https://custom-url.com",
                "api.restassured.logging.enabled", "false",
                "api.restassured.logging.level", "NONE"
        ));

        assertAll(
                () -> assertFalse(config.restAssuredLoggingEnabled()),
                () -> assertEquals("NONE", config.restAssuredLoggingLevel()),
                () -> assertEquals("https://custom-url.com", config.baseUrl())
        );
    }
}