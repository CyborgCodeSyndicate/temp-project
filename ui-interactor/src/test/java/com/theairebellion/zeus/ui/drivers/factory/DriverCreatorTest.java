package com.theairebellion.zeus.ui.drivers.factory;

import com.theairebellion.zeus.ui.drivers.base.DriverProvider;
import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.net.MalformedURLException;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverCreatorTest {

    private static final String REMOTE_URL = "http://example.com:4444/wd/hub";

    @InjectMocks
    private DriverCreator<ChromeOptions> driverCreator;

    @Mock
    private WebDriverConfig<ChromeOptions> config;

    @Mock
    private DriverProvider<ChromeOptions> provider;

    @Mock
    private ChromeOptions options;

    @Mock
    private WebDriver webDriver;

    @Mock
    private WebDriver decoratedWebDriver;

    @Mock
    private Consumer<ChromeOptions> optionsCustomizer;

    @Mock
    private EventFiringDecorator<WebDriver> eventFiringDecorator;

    @Test
    void testCreateLocalDriver() throws MalformedURLException {
        // Given a configuration for a local driver
        when(config.isHeadless()).thenReturn(false);
        when(config.isRemote()).thenReturn(false);
        when(config.getOptionsCustomizer()).thenReturn(null);
        when(config.getEventFiringDecorator()).thenReturn(null);

        // Also need to configure provider behavior for this specific test
        when(provider.createOptions()).thenReturn(options);
        when(provider.createDriver(options)).thenReturn(webDriver);

        // When creating a driver
        WebDriver result = driverCreator.createDriver(config, provider);

        // Then the local driver should be created
        assertNotNull(result, "createDriver should return non-null WebDriver");
        assertSame(webDriver, result, "The local driver should be returned");

        // And the provider methods should be called correctly
        verify(provider, times(1)).createOptions();
        verify(provider, times(1)).applyDefaultArguments(options);
        verify(provider, times(1)).createDriver(options);
        verify(provider, never()).applyHeadlessArguments(any());
    }

    @Test
    void testCreateHeadlessDriver() throws MalformedURLException {
        // Given a configuration for a headless driver
        when(config.isHeadless()).thenReturn(true);
        when(config.isRemote()).thenReturn(false);
        when(config.getOptionsCustomizer()).thenReturn(null);
        when(config.getEventFiringDecorator()).thenReturn(null);

        // Also need to configure provider behavior for this specific test
        when(provider.createOptions()).thenReturn(options);
        when(provider.createDriver(options)).thenReturn(webDriver);

        // When creating a driver
        WebDriver result = driverCreator.createDriver(config, provider);

        // Then the headless driver should be created
        assertNotNull(result, "createDriver should return non-null WebDriver");
        assertSame(webDriver, result, "The headless driver should be returned");

        // And the headless arguments should be applied
        verify(provider, times(1)).createOptions();
        verify(provider, times(1)).applyDefaultArguments(options);
        verify(provider, times(1)).applyHeadlessArguments(options);
        verify(provider, times(1)).createDriver(options);
    }

    @Test
    void testCreateRemoteDriver() throws MalformedURLException {
        // Given a configuration for a remote driver
        when(config.isHeadless()).thenReturn(false);
        when(config.isRemote()).thenReturn(true);
        when(config.getRemoteUrl()).thenReturn(REMOTE_URL);
        when(config.getOptionsCustomizer()).thenReturn(null);
        when(config.getEventFiringDecorator()).thenReturn(null);

        // Configure provider options
        when(provider.createOptions()).thenReturn(options);

        // Mock RemoteWebDriver construction
        try (MockedConstruction<RemoteWebDriver> mockedConstruction =
                     mockConstruction(RemoteWebDriver.class, (mock, context) -> {
                         // This will return a mocked RemoteWebDriver
                     })) {

            // When creating a driver
            WebDriver result = driverCreator.createDriver(config, provider);

            // Then a RemoteWebDriver should have been constructed
            assertEquals(1, mockedConstruction.constructed().size(),
                    "A RemoteWebDriver should have been constructed");

            // Verify the provider methods were called
            verify(provider).createOptions();
            verify(provider).applyDefaultArguments(options);
            verify(provider, never()).createDriver(any());
        }
    }

    @Test
    void testOptionsCustomizer() throws MalformedURLException {
        // Given a configuration with an options customizer
        when(config.isHeadless()).thenReturn(false);
        when(config.isRemote()).thenReturn(false);
        when(config.getOptionsCustomizer()).thenReturn(optionsCustomizer);
        when(config.getEventFiringDecorator()).thenReturn(null);

        // Also need to configure provider behavior for this specific test
        when(provider.createOptions()).thenReturn(options);
        when(provider.createDriver(options)).thenReturn(webDriver);

        // When creating a driver
        WebDriver result = driverCreator.createDriver(config, provider);

        // Then the options customizer should be applied
        verify(optionsCustomizer, times(1)).accept(options);
    }

    @Test
    void testEventFiringDecorator() throws MalformedURLException {
        // Given a configuration with an event firing decorator
        when(config.isHeadless()).thenReturn(false);
        when(config.isRemote()).thenReturn(false);
        when(config.getOptionsCustomizer()).thenReturn(null);
        when(config.getEventFiringDecorator()).thenReturn(eventFiringDecorator);

        // Also need to configure provider and decorator behavior for this specific test
        when(provider.createOptions()).thenReturn(options);
        when(provider.createDriver(options)).thenReturn(webDriver);
        when(eventFiringDecorator.decorate(webDriver)).thenReturn(decoratedWebDriver);

        // When creating a driver
        WebDriver result = driverCreator.createDriver(config, provider);

        // Then the driver should be decorated
        assertNotNull(result, "createDriver should return non-null WebDriver");
        assertSame(decoratedWebDriver, result, "The decorated driver should be returned");
        verify(eventFiringDecorator, times(1)).decorate(webDriver);
    }

    @Test
    void testNullOptionsCustomizerAndDecorator() throws MalformedURLException {
        // Given a configuration with null customizer and decorator
        when(config.isHeadless()).thenReturn(false);
        when(config.isRemote()).thenReturn(false);
        when(config.getOptionsCustomizer()).thenReturn(null);
        when(config.getEventFiringDecorator()).thenReturn(null);

        // Also need to configure provider behavior for this specific test
        when(provider.createOptions()).thenReturn(options);
        when(provider.createDriver(options)).thenReturn(webDriver);

        // When creating a driver
        WebDriver result = driverCreator.createDriver(config, provider);

        // Then the driver should be created without customization or decoration
        assertNotNull(result, "createDriver should return non-null WebDriver");
        assertSame(webDriver, result, "The original driver should be returned");
    }

    @Test
    void testMalformedUrlException() {
        // Given a configuration with an invalid URL
        when(config.isHeadless()).thenReturn(false);
        when(config.isRemote()).thenReturn(true);
        when(config.getRemoteUrl()).thenReturn("invalid-url");

        // Configure provider options
        when(provider.createOptions()).thenReturn(options);

        // When creating a driver, then a MalformedURLException should be thrown
        assertThrows(MalformedURLException.class, () -> driverCreator.createDriver(config, provider));
    }
}