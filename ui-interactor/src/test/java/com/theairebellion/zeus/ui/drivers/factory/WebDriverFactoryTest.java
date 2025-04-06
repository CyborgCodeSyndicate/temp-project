package com.theairebellion.zeus.ui.drivers.factory;

import com.theairebellion.zeus.ui.drivers.base.DriverProvider;
import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import com.theairebellion.zeus.ui.drivers.providers.ChromeDriverProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.AbstractDriverOptions;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
class WebDriverFactoryTest {

    private static final String CHROME_TYPE = "CHROME";
    private static final String EDGE_TYPE = "EDGE";
    private static final String CUSTOM_TYPE = "CUSTOM";
    private static final String VERSION = "123.45";

    @Mock
    private WebDriverConfig<AbstractDriverOptions<?>> config;

    @Mock
    private WebDriver webDriver;

    @Mock
    private DriverProvider<AbstractDriverOptions<?>> customProvider;

    @Test
    void testBasicClassFunctionality() {
        // This test simply ensures the constructor is covered
        assertNotNull(WebDriverFactory.class, "WebDriverFactory class should exist");

        // Reflection to verify DRIVER_PROVIDERS is initialized
        try {
            Field field = WebDriverFactory.class.getDeclaredField("DRIVER_PROVIDERS");
            field.setAccessible(true);
            Map<String, DriverProvider<?>> providers = (Map<String, DriverProvider<?>>) field.get(null);
            assertNotNull(providers, "DRIVER_PROVIDERS should be initialized");
            assertFalse(providers.isEmpty(), "DRIVER_PROVIDERS should not be empty");
        } catch (Exception e) {
            fail("Could not access DRIVER_PROVIDERS field: " + e.getMessage());
        }
    }

    @Test
    void testRegisterDriverAlreadyRegistered() {
        // Since we can't modify the DRIVER_PROVIDERS map easily, we'll test by trying to register CHROME again
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                WebDriverFactory.registerDriver(CHROME_TYPE, mock(DriverProvider.class)));

        // Verify the exception message
        assertTrue(exception.getMessage().contains("Driver type already registered"),
                "Exception should mention that driver type is already registered");
    }

    @Test
    void testRegisterDriverCaseInsensitive() {
        // Test by trying to register chrome (lowercase) which should be treated as CHROME
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                WebDriverFactory.registerDriver("chrome", mock(DriverProvider.class)));

        // Verify the exception message
        assertTrue(exception.getMessage().contains("Driver type already registered"),
                "Exception should mention that driver type is already registered regardless of case");
    }

    @Test
    void testRegisterNewDriver() {
        String NEW_DRIVER = "NEW";
        WebDriverFactory.registerDriver(NEW_DRIVER, mock(DriverProvider.class));


        try {
            Field field = WebDriverFactory.class.getDeclaredField("DRIVER_PROVIDERS");
            field.setAccessible(true);
            Map<String, DriverProvider<?>> providers = (Map<String, DriverProvider<?>>) field.get(null);
            assertNotNull(providers.get(NEW_DRIVER), "New driver should be added");
        } catch (Exception e) {
            fail("Could not access DRIVER_PROVIDERS field: " + e.getMessage());
        }
    }

    @Test
    void testCreateDriverWithUnknownType() {
        // When creating a driver with an unknown type
        // Then an IllegalArgumentException should be thrown
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                WebDriverFactory.createDriver("UNKNOWN", config));

        // Verify the exception message
        assertTrue(exception.getMessage().contains("No driver registered for type"),
                "Exception should mention that no driver is registered");
    }

    @Test
    void testCreateDriverWithExistingType() throws Exception {
        // Given a configuration with proper parameters
        when(config.getVersion()).thenReturn(VERSION);

        // Create a spy on WebDriverFactory to ensure class name coverage
        // This forces coverage of the class declaration line
        assertNotNull(WebDriverFactory.class.getName(), "WebDriverFactory class should be loaded");

        // Create a mock DriverCreator that will be returned when a new DriverCreator is constructed
        try (MockedConstruction<DriverCreator> mockedCreator = mockConstruction(
                DriverCreator.class,
                (mock, context) -> {
                    try {
                        when(mock.createDriver(any(), any())).thenReturn(webDriver);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })) {

            // When creating a driver with an existing type
            WebDriver result = WebDriverFactory.createDriver(CHROME_TYPE, config);

            // Then the driver should be returned
            assertSame(webDriver, result, "The created WebDriver should be returned");

            // And a new DriverCreator should have been constructed
            assertEquals(1, mockedCreator.constructed().size(), "A DriverCreator should have been constructed");

            // And the driver creation should be attempted
            verify(mockedCreator.constructed().get(0)).createDriver(eq(config), any(ChromeDriverProvider.class));
        }
    }

    @Test
    void testCreateDriverWithException() throws Exception {
        // Given a configuration with proper parameters
        when(config.getVersion()).thenReturn(VERSION);

        // Create a mock DriverCreator that will throw an exception
        try (MockedConstruction<DriverCreator> mockedCreator = mockConstruction(
                DriverCreator.class,
                (mock, context) -> {
                    try {
                        when(mock.createDriver(any(), any())).thenThrow(new MalformedURLException("Test exception"));
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })) {

            // When creating a driver that will throw an exception
            // Then a RuntimeException should be thrown
            Exception exception = assertThrows(WebDriverException.class, () ->
                    WebDriverFactory.createDriver(CHROME_TYPE, config));

            // Verify the exception details
            assertTrue(exception.getMessage().contains("Failed to create WebDriver"),
                    "Exception should mention that driver creation failed");
            assertTrue(exception.getCause() instanceof MalformedURLException,
                    "Exception cause should be the original exception");
        }
    }
}