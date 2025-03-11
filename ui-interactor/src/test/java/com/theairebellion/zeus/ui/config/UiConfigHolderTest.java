package com.theairebellion.zeus.ui.config;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class UiConfigHolderTest extends BaseUnitUITest {

    @BeforeEach
    void setUp() {
        // Clear ConfigCache
        ConfigCache.clear();

        // Reset the static config field using reflection
        try {
            Field configField = UiConfigHolder.class.getDeclaredField("config");
            configField.setAccessible(true);

            // Use reflection to set the field to null
            configField.set(null, null);
        } catch (Exception e) {
            // Log or handle the exception if needed
            e.printStackTrace();
        }
    }

    @Test
    void testConstructor() {
        // Get the class
        Class<UiConfigHolder> holderClass = UiConfigHolder.class;

        // Get all declared constructors
        Constructor<?>[] constructors = holderClass.getDeclaredConstructors();

        // Check that at least one constructor exists
        assertTrue(constructors.length > 0, "Class should have at least one constructor");

        // Try to instantiate the constructor
        try {
            Constructor<UiConfigHolder> constructor = holderClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            UiConfigHolder instance = constructor.newInstance();
            assertNotNull(instance, "Constructor should create an instance");
        } catch (Exception e) {
            fail("Failed to instantiate constructor: " + e.getMessage());
        }
    }

    @Test
    void testConstructorInstantiation() {
        // Attempt to create an instance using the public constructor
        UiConfigHolder instance = new UiConfigHolder();
        assertNotNull(instance, "Public constructor should create an instance");
    }

    @Test
    void testGetUiConfigFirstCall() {
        UiConfig firstConfig = UiConfigHolder.getUiConfig();
        assertNotNull(firstConfig, "First call should return a non-null config");
    }

    @Test
    void testGetUiConfigSubsequentCalls() {
        UiConfig firstConfig = UiConfigHolder.getUiConfig();
        UiConfig secondConfig = UiConfigHolder.getUiConfig();

        // Verify same instance is returned
        assertSame(firstConfig, secondConfig,
                "Subsequent calls should return the same config instance");
    }

    @Test
    void testGetUiConfigAfterReset() {
        // Create a new config with custom properties
        Properties customProperties = new Properties();
        customProperties.setProperty("browser.type", "FIREFOX");

        // Reset and recreate config
        ConfigCache.clear();
        UiConfig secondConfig = ConfigCache.getOrCreate(UiConfig.class, customProperties);

        // Manually set the config in the holder (to simulate reset)
        try {
            Field configField = UiConfigHolder.class.getDeclaredField("config");
            configField.setAccessible(true);
            configField.set(null, secondConfig);
        } catch (Exception e) {
            fail("Failed to set config field: " + e.getMessage());
        }

        // Get config again
        UiConfig retrievedConfig = UiConfigHolder.getUiConfig();

        // Verify config properties
        assertEquals("FIREFOX", retrievedConfig.browserType(),
                "Browser type should reflect custom properties");
    }

    @Test
    void testConfigCacheIntegration() {
        UiConfig config = UiConfigHolder.getUiConfig();

        // Verify some basic config properties to ensure integration works
        assertNotNull(config.browserType(), "Browser type should not be null");
        assertEquals("CHROME", config.browserType(), "Default browser type should be CHROME");
    }

    @Test
    void testThreadSafeConfigRetrieval() throws InterruptedException {
        final int THREAD_COUNT = 100;
        final UiConfig[] configs = new UiConfig[THREAD_COUNT];

        Thread[] threads = new Thread[THREAD_COUNT];

        // Create threads to retrieve config concurrently
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                configs[index] = UiConfigHolder.getUiConfig();
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Verify all threads got the same config instance
        UiConfig firstConfig = configs[0];
        assertNotNull(firstConfig, "First config should not be null");

        for (int i = 1; i < THREAD_COUNT; i++) {
            assertSame(firstConfig, configs[i],
                    "All threads should get the same config instance");
        }
    }
}