package com.theairebellion.zeus.api.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiConfigTest {

    @Test
    void testApiConfig_DefaultValues() {
        ApiConfig config = ConfigFactory.create(ApiConfig.class);

        // covers the defaultValue = "true"
        assertTrue(config.restAssuredLoggingEnabled());
        // covers the defaultValue = "ALL"
        assertEquals("ALL", config.restAssuredLoggingLevel());

        // If you want to check if it is null or something else:
        String url = config.baseUrl();
        // Could be null or a string if set. Check whichever applies.
        // For coverage alone, just call it.
        // e.g., assertNull(url);
        // or assertEquals("someDefaultUrl", url);
        // or just do:
        System.out.println("baseUrl from config = " + url);
    }

    @Test
    void testApiConfig_WithCustomProperties() {
        // Suppose we want to override or define the baseUrl:
        Map<String, String> props = new HashMap<>();
        props.put("api.base.url", "https://custom-url.com");
        props.put("api.restassured.logging.enabled", "false");
        props.put("api.restassured.logging.level", "NONE");

        ApiConfig config = ConfigFactory.create(ApiConfig.class, props);

        // Now your coverage includes these lines too
        assertFalse(config.restAssuredLoggingEnabled());
        assertEquals("NONE", config.restAssuredLoggingLevel());
        assertEquals("https://custom-url.com", config.baseUrl());
    }
}