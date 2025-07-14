package com.theairebellion.zeus.ui.config;

import org.aeonbits.owner.ConfigCache;

/**
 * Singleton holder for {@link UiFrameworkConfig}, ensuring a single instance is used throughout the application.
 *
 * <p>This class provides a globally accessible configuration instance for UI framework settings.
 * It utilizes the {@link ConfigCache} library to efficiently manage configuration loading and retrieval.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class UiFrameworkConfigHolder {

   private UiFrameworkConfigHolder() {
   }

   /**
    * Cached instance of the UI framework configuration.
    */
   private static UiFrameworkConfig config;

   /**
    * Retrieves the singleton instance of {@link UiFrameworkConfig}.
    *
    * <p>If the configuration has not been initialized, it is created and cached using {@link ConfigCache}.
    *
    * @return The UI framework configuration instance.
    */
   public static UiFrameworkConfig getUiFrameworkConfig() {
      if (config == null) {
         config = ConfigCache.getOrCreate(UiFrameworkConfig.class);
      }
      return config;
   }

}
