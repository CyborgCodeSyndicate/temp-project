package com.theairebellion.zeus.ui.drivers.config;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebDriverConfigTest {

    private static final String VERSION = "123.45";
    private static final String REMOTE_URL = "http://example.com:4444/wd/hub";

    @SuppressWarnings("unchecked")
    private final Consumer<ChromeOptions> optionsCustomizer = mock(Consumer.class);

    @SuppressWarnings("unchecked")
    private final EventFiringDecorator<WebDriver> eventFiringDecorator = mock(EventFiringDecorator.class);

    @Test
    void testAllArgsConstructor() {
        // When creating a config with all arguments
        WebDriverConfig<ChromeOptions> config = new WebDriverConfig<>(
                VERSION,
                true,
                true,
                REMOTE_URL,
                optionsCustomizer,
                eventFiringDecorator
        );

        // Then all properties should match the inputs
        assertEquals(VERSION, config.getVersion(), "Version should match input");
        assertTrue(config.isHeadless(), "Headless should be true");
        assertTrue(config.isRemote(), "Remote should be true");
        assertEquals(REMOTE_URL, config.getRemoteUrl(), "Remote URL should match input");
        assertSame(optionsCustomizer, config.getOptionsCustomizer(), "Options customizer should match input");
        assertSame(eventFiringDecorator, config.getEventFiringDecorator(), "Event firing decorator should match input");
    }


    @Test
    void testBuilder() {
        // When creating a config using the builder
        WebDriverConfig<ChromeOptions> config = WebDriverConfig.<ChromeOptions>builder()
                .version(VERSION)
                .headless(true)
                .remote(true)
                .remoteUrl(REMOTE_URL)
                .optionsCustomizer(optionsCustomizer)
                .eventFiringDecorator(eventFiringDecorator)
                .build();

        // Then all properties should match the inputs
        assertEquals(VERSION, config.getVersion(), "Version should match input");
        assertTrue(config.isHeadless(), "Headless should be true");
        assertTrue(config.isRemote(), "Remote should be true");
        assertEquals(REMOTE_URL, config.getRemoteUrl(), "Remote URL should match input");
        assertSame(optionsCustomizer, config.getOptionsCustomizer(), "Options customizer should match input");
        assertSame(eventFiringDecorator, config.getEventFiringDecorator(), "Event firing decorator should match input");
    }

    @Test
    void testEmptyBuilderWithDefaults() {
        // When creating a config with only some properties
        WebDriverConfig<ChromeOptions> config = WebDriverConfig.<ChromeOptions>builder()
                .build();

        // Then specified properties should match and unspecified should be default values
        assertNull(config.getVersion(), "Version should be null");
        assertFalse(config.isHeadless(), "Headless should default to false");
        assertFalse(config.isRemote(), "Remote should default to false");
        assertNull(config.getRemoteUrl(), "Remote URL should default to null");
        assertNull(config.getOptionsCustomizer(), "Options customizer should default to null");
        assertNull(config.getEventFiringDecorator(), "Event firing decorator should default to null");
    }
}