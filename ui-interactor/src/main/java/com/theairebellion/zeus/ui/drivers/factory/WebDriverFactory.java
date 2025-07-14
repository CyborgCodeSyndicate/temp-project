package com.theairebellion.zeus.ui.drivers.factory;

import com.theairebellion.zeus.ui.drivers.base.DriverProvider;
import com.theairebellion.zeus.ui.drivers.config.WebDriverConfig;
import com.theairebellion.zeus.ui.drivers.providers.ChromeDriverProvider;
import com.theairebellion.zeus.ui.drivers.providers.EdgeDriverProvider;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.AbstractDriverOptions;

/**
 * Factory class for creating WebDriver instances based on registered driver providers.
 *
 * <p>This class manages WebDriver providers for different browser types and facilitates the
 * creation of WebDriver instances with appropriate configurations.
 *
 * <p>By default, Chrome and Edge drivers are registered. Additional drivers can be registered
 * dynamically using the {@link #registerDriver(String, DriverProvider)} method.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class WebDriverFactory {

   private WebDriverFactory() {
   }

   /**
    * Stores registered WebDriver providers, mapped by browser type (e.g., "CHROME", "EDGE").
    */
   private static final Map<String, DriverProvider<?>> DRIVER_PROVIDERS = new ConcurrentHashMap<>();

   static {
      registerDriver("CHROME", new ChromeDriverProvider());
      registerDriver("EDGE", new EdgeDriverProvider());
   }

   /**
    * Registers a new WebDriver provider for a specified browser type.
    *
    * @param type     The browser type (e.g., "CHROME", "EDGE").
    * @param provider The driver provider responsible for creating and managing the WebDriver instance.
    * @param <T>      The type parameter extending {@link DriverProvider}.
    * @throws IllegalArgumentException If a driver for the specified type is already registered.
    */
   public static <T extends DriverProvider<?>> void registerDriver(String type, T provider) {
      if (DRIVER_PROVIDERS.containsKey(type.trim().toUpperCase())) {
         throw new IllegalArgumentException("Driver type already registered: " + type);
      }
      DRIVER_PROVIDERS.put(type.trim().toUpperCase(), provider);
   }

   /**
    * Creates a WebDriver instance for the specified browser type, using the provided configuration.
    *
    * @param type   The browser type (e.g., "CHROME", "EDGE").
    * @param config The WebDriver configuration, specifying options such as version and headless mode.
    * @return A fully configured {@link WebDriver} instance.
    * @throws IllegalArgumentException If no driver is registered for the specified type.
    * @throws RuntimeException         If WebDriver creation fails due to an unexpected error.
    */
   @SuppressWarnings({"rawtypes", "unchecked"}) // Necessary for generic factory call
   public static WebDriver createDriver(String type, WebDriverConfig config) {
      DriverProvider<?> provider = Optional.ofNullable(DRIVER_PROVIDERS.get(type.trim().toUpperCase()))
            .orElseThrow(() -> new IllegalArgumentException("No driver registered for type: " + type));

      provider.setupDriver(config.getVersion());

      try {
         return new DriverCreator<>().createDriver(config,
               (DriverProvider<AbstractDriverOptions<?>>) provider);
      } catch (Exception e) {
         throw new WebDriverException("Failed to create WebDriver for type: " + type, e);
      }
   }

}
