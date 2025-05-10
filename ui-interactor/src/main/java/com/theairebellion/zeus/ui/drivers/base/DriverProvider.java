package com.theairebellion.zeus.ui.drivers.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;

/**
 * Defines a contract for WebDriver providers responsible for managing browser-specific configurations and instances.
 *
 * <p>Implementations of this interface should provide methods to:
 * <ul>
 *     <li>Create browser-specific {@link AbstractDriverOptions}.</li>
 *     <li>Apply default and headless arguments to WebDriver options.</li>
 *     <li>Handle driver setup and download logic.</li>
 *     <li>Instantiate WebDriver instances with configured options.</li>
 * </ul>
 *
 * <p>The generic type {@code T} represents a browser-specific configuration that extends {@link AbstractDriverOptions}.
 * Implementations must define the specific WebDriver options required for a particular browser.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface DriverProvider<T extends AbstractDriverOptions<?>> {

   /**
    * Creates a new instance of browser-specific driver options.
    *
    * <p>This method should return an instance of the browser options configured with necessary defaults.
    *
    * @return A {@link AbstractDriverOptions} instance containing browser-specific configurations.
    */
   T createOptions();

   /**
    * Creates and returns a new WebDriver instance using the provided options.
    *
    * <p>This method initializes a new WebDriver based on the configured browser options and settings.
    *
    * @param options The browser-specific driver options.
    * @return A configured instance of {@link WebDriver}.
    */
   WebDriver createDriver(T options);

   /**
    * Applies default arguments to the provided driver options.
    *
    * <p>This method configures default settings such as window size, timeouts, and other browser-specific capabilities.
    *
    * @param options The driver options to which default arguments should be applied.
    */
   void applyDefaultArguments(T options);

   /**
    * Applies headless mode arguments to the provided driver options.
    *
    * <p>This method ensures the WebDriver runs in headless mode if required.
    *
    * @param options The driver options to modify for headless execution.
    */
   void applyHeadlessArguments(T options);

   /**
    * Sets up the WebDriver by downloading the necessary binary if it has not already been downloaded.
    *
    * <p>Implementations should ensure that the correct WebDriver version is downloaded and ready for execution.
    *
    * @param version The version of the WebDriver to download.
    */
   void setupDriver(String version);

   /**
    * Downloads the WebDriver binary for the specified version.
    *
    * <p>This method should contain logic for fetching and installing the WebDriver for a given browser.
    *
    * @param version The WebDriver version to download.
    */
   void downloadDriver(String version);

}
