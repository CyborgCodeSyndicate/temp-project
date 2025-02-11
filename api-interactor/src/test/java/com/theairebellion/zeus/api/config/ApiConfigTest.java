package com.theairebellion.zeus.api.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiConfigTest {

    private static final String CUSTOM_URL = "https://custom-url.com";
    private static final String BASE_URL_KEY = "api.base.url";
    private static final String LOGGING_ENABLED_KEY = "api.restassured.logging.enabled";
    private static final String LOGGING_LEVEL_KEY = "api.restassured.logging.level";
    public static final String ALL = "ALL";
    public static final String NONE = "NONE";

    @Test
    void testApiConfig_DefaultValues() {
        var config = ConfigFactory.create(ApiConfig.class);
        assertAll(
                () -> assertTrue(config.restAssuredLoggingEnabled()),
                () -> assertEquals(ALL, config.restAssuredLoggingLevel()),
                () -> System.out.println("baseUrl from config = " + config.baseUrl())
        );
    }

    @Test
    void testApiConfig_WithCustomProperties() {
        var config = ConfigFactory.create(ApiConfig.class, Map.of(
                BASE_URL_KEY, CUSTOM_URL,
                LOGGING_ENABLED_KEY, "false",
                LOGGING_LEVEL_KEY, NONE
        ));
        assertAll(
                () -> assertFalse(config.restAssuredLoggingEnabled()),
                () -> assertEquals(NONE, config.restAssuredLoggingLevel()),
                () -> assertEquals(CUSTOM_URL, config.baseUrl())
        );
    }
}
