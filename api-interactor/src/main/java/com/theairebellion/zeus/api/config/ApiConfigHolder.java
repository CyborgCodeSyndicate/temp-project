package com.theairebellion.zeus.api.config;

import org.aeonbits.owner.ConfigCache;

/**
 * Singleton holder for API configuration settings.
 *
 * <p>This class provides a centralized mechanism to retrieve and cache
 * the API configuration using the {@code Owner} library.
 * It ensures that the configuration is only loaded once and reused throughout the application.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class ApiConfigHolder {

   private ApiConfigHolder() {
   }


   private static ApiConfig config;

   /**
    * Retrieves the API configuration instance.
    *
    * <p>If the configuration is not already loaded, it is initialized
    * using {@code ConfigCache.getOrCreate(ApiConfig.class)}.
    *
    * @return The {@code ApiConfig} instance.
    */
   public static ApiConfig getApiConfig() {
      if (config == null) {
         config = ConfigCache.getOrCreate(ApiConfig.class);
      }
      return config;
   }

}
