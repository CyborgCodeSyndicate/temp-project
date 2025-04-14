package com.theairebellion.zeus.ui.drivers.base;

import java.util.concurrent.ConcurrentHashMap;
import org.openqa.selenium.remote.AbstractDriverOptions;

/**
 * Abstract base class for managing WebDriver setup and configuration.
 *
 * <p>This class provides a mechanism to ensure that WebDriver binaries are downloaded only once per driver type,
 * avoiding redundant downloads across multiple test sessions.
 *
 * <p>It utilizes a thread-safe {@link ConcurrentHashMap} to track which drivers have been downloaded.
 *
 * <p>The generic type {@code T} represents a specific browser configuration extending {@link AbstractDriverOptions}.
 * Implementations of this class must define the WebDriver options corresponding to a particular browser.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public abstract class BaseDriverProvider<T extends AbstractDriverOptions<?>> implements DriverProvider<T> {

   /**
    * Thread-safe map tracking the download status of different WebDriver binaries.
    *
    * <p>The keys represent the driver types (e.g., Chrome, Firefox),
    * and the values indicate whether the corresponding driver binary has already been downloaded.
    */
   private static final ConcurrentHashMap<String, Boolean> DRIVER_DOWNLOAD_STATUS = new ConcurrentHashMap<>();

   /**
    * Retrieves the type of WebDriver that this provider supports.
    *
    * @return A {@link String} representing the WebDriver type (e.g., "CHROME", "FIREFOX").
    */
   protected abstract String getDriverType();

   /**
    * Sets up the WebDriver by downloading the necessary driver binary if it has not already been downloaded.
    *
    * <p>This method ensures that each driver type is downloaded only once by utilizing
    * {@link ConcurrentHashMap#computeIfAbsent(Object, java.util.function.Function)} to prevent duplicate downloads.
    *
    * @param version The specific WebDriver version to download.
    */
   @Override
   public void setupDriver(final String version) {
      String driverType = getDriverType();
      DRIVER_DOWNLOAD_STATUS.computeIfAbsent(driverType, key -> {
         downloadDriver(version);
         return true;
      });
   }

}
