package com.theairebellion.zeus.api.config;

import org.aeonbits.owner.Config;

/**
 * Configuration interface for API-related settings.
 * <p>
 * This interface utilizes the {@code Owner} library to load API configurations
 * from system properties or a specified properties file. It supports customizable
 * settings for logging, API base URLs, and more.
 * </p>
 *
 * <p>
 * Configuration sources:
 * <ul>
 *     <li>System properties</li>
 *     <li>Classpath properties file (defined via {@code api.config.file})</li>
 * </ul>
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${api.config.file}.properties"})
public interface ApiConfig extends Config {

    /**
     * Determines whether RestAssured request/response logging is enabled.
     * <p>Default: {@code true}</p>
     *
     * @return {@code true} if logging is enabled, {@code false} otherwise.
     */
    @DefaultValue("true")
    @Key("api.restassured.logging.enabled")
    boolean restAssuredLoggingEnabled();

    /**
     * Specifies the logging level for RestAssured.
     * <p>Default: {@code "ALL"}</p>
     *
     * @return The logging level as a string (e.g., {@code "ALL"}, {@code "BODY"}, {@code "HEADERS"}).
     */
    @DefaultValue("ALL")
    @Key("api.restassured.logging.level")
    String restAssuredLoggingLevel();

    /**
     * Retrieves the base URL for API requests.
     *
     * @return The API base URL.
     */
    @Key("api.base.url")
    String baseUrl();

}
