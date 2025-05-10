package com.theairebellion.zeus.framework.config;

import org.aeonbits.owner.Config;

/**
 * Configuration interface for loading framework settings.
 *
 * <p>This interface defines configuration properties for the framework,
 * allowing dynamic retrieval of essential settings from system properties
 * and external property files.
 *
 * <p>The configuration is loaded using a merge policy, which combines
 * multiple sources, including system properties and classpath properties.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${framework.config.file}.properties"})
public interface FrameworkConfig extends Config {

   /**
    * Retrieves the base package of the project.
    *
    * @return The base package name defined in the configuration.
    */
   @Key("project.package")
   String projectPackage();

   /**
    * Retrieves the default storage location used by the framework.
    *
    * @return The default storage configuration value.
    */
   @Key("default.storage")
   String defaultStorage();

   /**
    * Retrieves the test environment configuration.
    *
    * @return The configured test environment name.
    */
   @Key("test.env")
   String testEnv();

}
