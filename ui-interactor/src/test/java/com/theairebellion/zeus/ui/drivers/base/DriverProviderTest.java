package com.theairebellion.zeus.ui.drivers.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverProviderTest {

    private static final String VERSION = "123.45";

    @Mock
    private DriverProvider<ChromeOptions> driverProvider;

    @Mock
    private ChromeOptions options;

    @Mock
    private WebDriver webDriver;

    @Test
    void testCreateOptions() {
        // Given
        when(driverProvider.createOptions()).thenReturn(options);

        // When
        ChromeOptions result = driverProvider.createOptions();

        // Then
        assertNotNull(result, "createOptions should return non-null options");
        assertSame(options, result, "createOptions should return the mocked options");
        verify(driverProvider, times(1)).createOptions();
    }

    @Test
    void testCreateDriver() {
        // Given
        when(driverProvider.createDriver(options)).thenReturn(webDriver);

        // When
        WebDriver result = driverProvider.createDriver(options);

        // Then
        assertNotNull(result, "createDriver should return non-null WebDriver");
        assertSame(webDriver, result, "createDriver should return the mocked WebDriver");
        verify(driverProvider, times(1)).createDriver(options);
    }

    @Test
    void testApplyDefaultArguments() {
        // When
        driverProvider.applyDefaultArguments(options);

        // Then
        verify(driverProvider, times(1)).applyDefaultArguments(options);
    }

    @Test
    void testApplyHeadlessArguments() {
        // When
        driverProvider.applyHeadlessArguments(options);

        // Then
        verify(driverProvider, times(1)).applyHeadlessArguments(options);
    }

    @Test
    void testSetupDriver() {
        // When
        driverProvider.setupDriver(VERSION);

        // Then
        verify(driverProvider, times(1)).setupDriver(VERSION);
    }

    @Test
    void testDownloadDriver() {
        // When
        driverProvider.downloadDriver(VERSION);

        // Then
        verify(driverProvider, times(1)).downloadDriver(VERSION);
    }

    @Test
    void testFullDriverCreationFlow() {
        // Given
        when(driverProvider.createOptions()).thenReturn(options);
        when(driverProvider.createDriver(options)).thenReturn(webDriver);

        // When creating and configuring a driver
        ChromeOptions createdOptions = driverProvider.createOptions();
        driverProvider.applyDefaultArguments(createdOptions);
        driverProvider.applyHeadlessArguments(createdOptions);
        WebDriver createdDriver = driverProvider.createDriver(createdOptions);

        // Then all methods should be called in the correct sequence
        verify(driverProvider, times(1)).createOptions();
        verify(driverProvider, times(1)).applyDefaultArguments(options);
        verify(driverProvider, times(1)).applyHeadlessArguments(options);
        verify(driverProvider, times(1)).createDriver(options);

        // And the driver should be returned
        assertSame(webDriver, createdDriver, "The flow should return the mocked WebDriver");
    }
}