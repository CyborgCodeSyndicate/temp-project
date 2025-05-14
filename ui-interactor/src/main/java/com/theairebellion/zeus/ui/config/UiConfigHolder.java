package com.theairebellion.zeus.ui.config;

import org.aeonbits.owner.ConfigCache;

/**
 * Singleton holder for managing the {@link UiConfig} instance.
 *
 * <p>This class ensures a single shared instance of the UI configuration throughout the application.
 * The configuration is lazily initialized using {@link ConfigCache} for efficient retrieval.
 *
 * <p>Benefits:
 * <ul>
 *     <li>Prevents multiple instantiations of the {@link UiConfig} interface.</li>
 *     <li>Ensures consistent configuration values throughout the test execution.</li>
 *     <li>Leverages OWNER library caching for improved performance.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class UiConfigHolder {

   /**
    * Singleton instance of the UI configuration
    */
   private static UiConfig config;


   private UiConfigHolder() {
   }

   /**
    * Retrieves the global {@link UiConfig} instance.
    *
    * <p>If the configuration has not been initialized, it will be created using {@link ConfigCache}.
    *
    * @return The singleton instance of {@link UiConfig}.
    */
   public static UiConfig getUiConfig() {
      if (config == null) {
         config = ConfigCache.getOrCreate(UiConfig.class);
      }
      return config;
   }

}
