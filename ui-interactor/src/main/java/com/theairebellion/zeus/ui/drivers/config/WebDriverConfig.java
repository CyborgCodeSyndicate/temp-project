package com.theairebellion.zeus.ui.drivers.config;

import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;

/**
 * Represents the configuration settings for a WebDriver instance.
 *
 * <p>This class encapsulates WebDriver-related settings such as browser version, execution mode (headless or remote),
 * custom driver option configurations, and event listeners.
 *
 * <p>The generic type {@code T} represents browser-specific configuration options that extend
 * {@link AbstractDriverOptions}. This allows flexibility in defining WebDriver settings for different browsers,
 * such as Chrome, Firefox, or Edge.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@AllArgsConstructor
@Getter
@Builder
public class WebDriverConfig<T extends AbstractDriverOptions<?>> {

   /**
    * The version of the WebDriver to use.
    */
   private final String version;

   /**
    * Indicates whether the WebDriver should run in headless mode.
    *
    * <p>If {@code true}, the browser will run without a visible UI.
    */
   private final boolean headless;

   /**
    * Indicates whether the WebDriver should connect to a remote Selenium Grid.
    *
    * <p>If {@code true}, the WebDriver instance will be created remotely using the specified {@link #remoteUrl}.
    */
   private final boolean remote;

   /**
    * The URL of the remote WebDriver server.
    *
    * <p>This is used when {@link #remote} is set to {@code true}.
    */
   private final String remoteUrl;

   /**
    * A customizer for modifying browser-specific driver options before instantiation.
    *
    * <p>This consumer allows for dynamic modifications to WebDriver options,
    * such as adding experimental flags or capabilities.
    */
   private final Consumer<T> optionsCustomizer;

   /**
    * An optional event-firing decorator for the WebDriver instance.
    *
    * <p>This allows event listeners to be attached to WebDriver operations for logging or debugging purposes.
    */
   private final EventFiringDecorator<WebDriver> eventFiringDecorator;

}
