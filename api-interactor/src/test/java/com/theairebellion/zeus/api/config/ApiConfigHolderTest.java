package com.theairebellion.zeus.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("ApiConfigHolder Basic Tests")
class ApiConfigHolderTest {

    @BeforeEach
    void resetSingleton() {
        // Clear any cached instance before each test
        ApiConfigHolder.resetForTest();
    }

    @Test
    @DisplayName("getApiConfig should return non-null config")
    void getApiConfigShouldReturnNonNullConfig() {
        // Act
        ApiConfig config = ApiConfigHolder.getApiConfig();

        // Assert
        assertNotNull(config, "ApiConfig should not be null");
    }

    @Test
    @DisplayName("getApiConfig should return the same instance on multiple calls")
    void getApiConfigShouldReturnSameInstance() {
        // Act
        ApiConfig first = ApiConfigHolder.getApiConfig();
        ApiConfig second = ApiConfigHolder.getApiConfig();

        // Assert
        assertSame(first, second, "Multiple calls to getApiConfig should return the same instance");
    }

}
