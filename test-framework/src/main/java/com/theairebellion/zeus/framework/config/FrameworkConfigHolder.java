package com.theairebellion.zeus.framework.config;

import org.aeonbits.owner.ConfigCache;

/**
 * Singleton holder for accessing the framework configuration.
 *
 * <p>This class ensures a single instance of {@code FrameworkConfig} is created
 * and provides a global access point for retrieving configuration values.
 *
 * <p>The configuration is lazily initialized upon first access and cached for
 * subsequent use, optimizing performance by avoiding redundant object creation.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class FrameworkConfigHolder {

   /**
    * Cached instance of the framework configuration.
    */
   private static FrameworkConfig config;

   /**
    * Retrieves the singleton instance of {@code FrameworkConfig}.
    *
    * <p>If the configuration is not yet initialized, it is created and cached
    * using {@code ConfigCache}.
    *
    * @return The {@code FrameworkConfig} instance.
    */
   public static FrameworkConfig getFrameworkConfig() {
      if (config == null) {
         config = ConfigCache.getOrCreate(FrameworkConfig.class);
      }
      return config;
   }

}
