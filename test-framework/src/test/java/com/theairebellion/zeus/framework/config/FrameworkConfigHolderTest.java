package com.theairebellion.zeus.framework.config;

import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FrameworkConfigHolder Tests")
class FrameworkConfigHolderTest {

    @BeforeEach
    void clearConfigBeforeTests() throws Exception {
        // Reset the static config field before each test
        var field = FrameworkConfigHolder.class.getDeclaredField("config");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    @DisplayName("getFrameworkConfig should retrieve config from cache on first call")
    void testGetFrameworkConfig_FirstCall() {
        try (MockedStatic<ConfigCache> configCacheMock = mockStatic(ConfigCache.class)) {
            // Given
            FrameworkConfig mockConfig = mock(FrameworkConfig.class);
            configCacheMock.when(() -> ConfigCache.getOrCreate(FrameworkConfig.class))
                    .thenReturn(mockConfig);

            // When
            FrameworkConfig result = FrameworkConfigHolder.getFrameworkConfig();

            // Then
            assertNotNull(result, "Should return a config instance");
            assertSame(mockConfig, result, "Should return the mock from ConfigCache");
            configCacheMock.verify(() -> ConfigCache.getOrCreate(FrameworkConfig.class), times(1));
        }
    }

    @Test
    @DisplayName("getFrameworkConfig should return cached config on subsequent calls")
    void testGetFrameworkConfig_CachedResult() {
        try (MockedStatic<ConfigCache> configCacheMock = mockStatic(ConfigCache.class)) {
            // Given
            FrameworkConfig mockConfig = mock(FrameworkConfig.class);
            configCacheMock.when(() -> ConfigCache.getOrCreate(FrameworkConfig.class))
                    .thenReturn(mockConfig);

            // When - Call twice
            FrameworkConfig result1 = FrameworkConfigHolder.getFrameworkConfig();
            FrameworkConfig result2 = FrameworkConfigHolder.getFrameworkConfig();

            // Then
            assertNotNull(result1, "Should return a config instance on first call");
            assertNotNull(result2, "Should return a config instance on second call");
            assertSame(mockConfig, result1, "First result should be the mock from ConfigCache");
            assertSame(result1, result2, "Second result should be same as first (cached)");

            // Should only call ConfigCache once regardless of how many times we call the method
            configCacheMock.verify(() -> ConfigCache.getOrCreate(FrameworkConfig.class), times(1));
        }
    }

    @Test
    @DisplayName("getFrameworkConfig should return real config instance when not mocked")
    void testGetFrameworkConfig_RealConfig() {
        // Given - System property for testing
        System.setProperty("project.package", "com.test.package");
        System.setProperty("default.storage", "test.storage");

        try {
            // When
            FrameworkConfig config = FrameworkConfigHolder.getFrameworkConfig();

            // Then
            assertNotNull(config, "Should return a real config instance");
            // Test real config behavior with system properties
            assertEquals("com.test.package", config.projectPackage(),
                    "Should read project.package from system properties");
            assertEquals("test.storage", config.defaultStorage(),
                    "Should read default.storage from system properties");
        } finally {
            // Clean up system properties
            System.clearProperty("project.package");
            System.clearProperty("default.storage");

            // Reset static field for other tests
            try {
                var field = FrameworkConfigHolder.class.getDeclaredField("config");
                field.setAccessible(true);
                field.set(null, null);
            } catch (Exception e) {
                fail("Failed to reset config field: " + e.getMessage());
            }
        }
    }
}