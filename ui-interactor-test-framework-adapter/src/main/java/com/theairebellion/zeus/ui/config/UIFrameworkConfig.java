package com.theairebellion.zeus.ui.config;

import com.theairebellion.zeus.config.ConfigSource;
import com.theairebellion.zeus.config.PropertyConfig;
import org.aeonbits.owner.Config;

/**
 * Configuration interface for UI framework settings.
 * <p>
 * This interface defines configurable properties for the UI testing framework,
 * allowing users to manage settings such as screenshot behavior upon test success.
 * The configurations are loaded from system properties and a specified properties file.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@ConfigSource("ui-framework-config")
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${ui.config.file}.properties"})
public interface UIFrameworkConfig extends PropertyConfig {

    /**
     * Determines whether a screenshot should be taken when a test passes.
     * <p>
     * Default value: {@code false}.
     * </p>
     *
     * @return {@code true} if a screenshot should be taken on a passed test, {@code false} otherwise.
     */
    @DefaultValue("false")
    @Key("screenshot.on.passed.test")
    boolean makeScreenshotOnPassedTest();

}
