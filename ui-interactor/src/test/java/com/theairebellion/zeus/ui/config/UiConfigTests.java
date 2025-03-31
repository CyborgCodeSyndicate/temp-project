package com.theairebellion.zeus.ui.config;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UiConfigTests extends BaseUnitUITest {

    @Nested
    class UiConfigDefaultValuesTests {
        private UiConfig config;

        @BeforeEach
        void setUp() {
            // Clear any existing cached configurations
            ConfigCache.clear();
            config = ConfigCache.getOrCreate(UiConfig.class);
        }

        @Test
        void testDefaultBrowserType() {
            assertEquals("CHROME", config.browserType());
        }

        @Test
        void testDefaultBrowserVersion() {
            assertEquals("", config.browserVersion());
        }

        @Test
        void testDefaultHeadless() {
            assertFalse(config.headless());
        }

        @Test
        void testDefaultUseWrappedSeleniumFunctions() {
            assertTrue(config.useWrappedSeleniumFunctions());
        }

        @Test
        void testDefaultUseShadowRoot() {
            assertFalse(config.useShadowRoot());
        }

        @Test
        void testDefaultRemoteDriverUrl() {
            assertEquals("",config.remoteDriverUrl());
        }
    }

    @Nested
    class UiConfigCustomPropertiesTests {
        private UiConfig config;
        private Properties testProperties;

        @BeforeEach
        void setUp() {
            // Clear any existing cached configurations
            ConfigCache.clear();

            // Prepare custom properties
            testProperties = new Properties();
            testProperties.setProperty("browser.type", "FIREFOX");
            testProperties.setProperty("browser.version", "90.0");
            testProperties.setProperty("headless", "true");
            testProperties.setProperty("remote.driver.url", "http://localhost:4444");
            testProperties.setProperty("wait.duration.in.seconds", "30");
            testProperties.setProperty("project.package", "com.example.test");
            testProperties.setProperty("use.wrap.selenium.function", "false");
            testProperties.setProperty("use.shadow.root", "true");

            // Create config with custom properties
            config = ConfigCache.getOrCreate(UiConfig.class, testProperties);
        }

        @Test
        void testCustomBrowserType() {
            assertEquals("FIREFOX", config.browserType());
        }

        @Test
        void testCustomBrowserVersion() {
            assertEquals("90.0", config.browserVersion());
        }

        @Test
        void testCustomHeadless() {
            assertTrue(config.headless());
        }

        @Test
        void testCustomRemoteDriverUrl() {
            assertEquals("http://localhost:4444", config.remoteDriverUrl());
        }

        @Test
        void testCustomWaitDuration() {
            assertEquals(30, config.waitDuration());
        }

        @Test
        void testCustomProjectPackage() {
            assertEquals("com.example.test", config.projectPackage());
        }

        @Test
        void testCustomUseWrappedSeleniumFunctions() {
            assertFalse(config.useWrappedSeleniumFunctions());
        }

        @Test
        void testCustomUseShadowRoot() {
            assertTrue(config.useShadowRoot());
        }
    }

    @Nested
    class UiConfigHolderTests {
        @BeforeEach
        void setUp() {
            // Clear any existing cached configurations
            ConfigCache.clear();
        }

        @Test
        void testGetUiConfigFirstCall() {
            UiConfig firstConfig = UiConfigHolder.getUiConfig();
            assertNotNull(firstConfig);
        }

        @Test
        void testGetUiConfigSubsequentCalls() {
            UiConfig firstConfig = UiConfigHolder.getUiConfig();
            UiConfig secondConfig = UiConfigHolder.getUiConfig();
            assertSame(firstConfig, secondConfig);
        }

        @Test
        void testAllConfigMethodsAvailable() {
            UiConfig config = UiConfigHolder.getUiConfig();

            // Verify that all methods can be called without throwing exceptions
            assertDoesNotThrow(() -> {
                config.browserType();
                config.browserVersion();
                config.headless();
                config.waitDuration();
                config.projectPackage();
                config.inputDefaultType();
                config.useWrappedSeleniumFunctions();
            });
        }
    }
}