package com.theairebellion.zeus.api.config;

import com.theairebellion.zeus.config.ConfigSource;
import com.theairebellion.zeus.config.PropertyConfig;
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
@ConfigSource("api-config")
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${api.config.file}.properties"})
public interface ApiConfig extends PropertyConfig {

    @Key("project.package")
    String projectPackage();

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
     * @return The logging level as a string (e.g., {@code "ALL"}, {@code "BASIC"}, {@code "NONE"}).
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

    /**
     * Determines whether the full response body should be logged.
     * <p>
     * If set to {@code true}, the entire response body will be logged. If {@code false},
     * the response body will be truncated based on the {@code shorten.body} configuration.
     * </p>
     * <p>Default: {@code true}</p>
     *
     * @return {@code true} if full response body logging is enabled, {@code false} otherwise.
     */
    @DefaultValue("true")
    @Key("log.full.body")
    boolean logFullBody();

    /**
     * Specifies the maximum number of characters to display when logging a response body.
     * <p>
     * If {@code log.full.body} is set to {@code false}, response bodies longer than this value
     * will be truncated with an ellipsis ({@code ...}).
     * </p>
     * <p>Default: {@code 1000}</p>
     *
     * @return The maximum number of characters to display in a logged response body.
     */
    @DefaultValue("1000")
    @Key("shorten.body")
    int shortenBody();

}
