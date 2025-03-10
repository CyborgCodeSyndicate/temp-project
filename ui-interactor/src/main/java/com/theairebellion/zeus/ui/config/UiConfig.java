package com.theairebellion.zeus.ui.config;

import org.aeonbits.owner.Config;

/**
 * Configuration interface for UI automation settings.
 * <p>
 * This interface is used to define and retrieve configuration properties
 * for the UI automation framework. It uses the {@link Config} library
 * to load values from system properties or a properties file.
 * </p>
 *
 * <p>Properties are loaded from:</p>
 * <ul>
 *     <li>System properties (e.g., `-Dbrowser.type=CHROME`)</li>
 *     <li>Classpath properties file, dynamically determined by `${ui.config.file}.properties`</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${ui.config.file}.properties"})
public interface UiConfig extends Config {

    /**
     * Retrieves the browser type used for UI testing.
     * <p>Default: "CHROME"</p>
     *
     * @return The browser type (e.g., CHROME, FIREFOX, EDGE).
     */
    @DefaultValue("CHROME")
    @Key("browser.type")
    String browserType();

    /**
     * Retrieves the browser version.
     * <p>Default: "" (empty string, meaning latest version will be used)</p>
     *
     * @return The browser version (e.g., "114.0.5735.199").
     */
    @DefaultValue("")
    @Key("browser.version")
    String browserVersion();

    /**
     * Determines whether the browser should run in headless mode.
     * <p>Default: false</p>
     *
     * @return {@code true} if headless mode is enabled, otherwise {@code false}.
     */
    @DefaultValue("false")
    @Key("headless")
    boolean headless();

    /**
     * Retrieves the URL for a remote WebDriver instance.
     *
     * @return The Selenium Grid or remote WebDriver URL.
     */
    @Key("remote.driver.url")
    String remoteDriverUrl();

    /**
     * Retrieves the default wait duration for element interactions.
     *
     * @return The wait duration in seconds.
     */
    @Key("wait.duration.in.seconds")
    int waitDuration();

    /**
     * Retrieves the base package of the project for class scanning.
     *
     * @return The project's root package.
     */
    @Key("project.package")
    String projectPackage();

    /**
     * Retrieves the default type for input fields.
     *
     * @return The default input component type.
     */
    @Key("input.default.type")
    String inputDefaultType();

    /**
     * Retrieves the default type for buttons.
     *
     * @return The default button component type.
     */
    @Key("button.default.type")
    String buttonDefaultType();

    /**
     * Retrieves the default type for checkboxes.
     *
     * @return The default checkbox component type.
     */
    @Key("checkbox.default.type")
    String checkboxDefaultType();

    /**
     * Retrieves the default type for toggle buttons.
     *
     * @return The default toggle component type.
     */
    @Key("toggle.default.type")
    String toggleDefaultType();

    /**
     * Retrieves the default type for radio buttons.
     *
     * @return The default radio button component type.
     */
    @Key("radio.default.type")
    String radioDefaultType();

    /**
     * Retrieves the default type for select (dropdown) components.
     *
     * @return The default select component type.
     */
    @Key("select.default.type")
    String selectDefaultType();

    /**
     * Retrieves the default type for list components.
     *
     * @return The default list component type.
     */
    @Key("list.default.type")
    String listDefaultType();

    /**
     * Retrieves the default type for loader/spinner components.
     *
     * @return The default loader component type.
     */
    @Key("loader.default.type")
    String loaderDefaultType();

    /**
     * Retrieves the default type for link components.
     *
     * @return The default link component type.
     */
    @Key("link.default.type")
    String linkDefaultType();

    /**
     * Retrieves the default type for alert components.
     *
     * @return The default alert component type.
     */
    @Key("alert.default.type")
    String alertDefaultType();

    /**
     * Retrieves the default type for tab components.
     *
     * @return The default tab component type.
     */
    @Key("tab.default.type")
    String tabDefaultType();

    /**
     * Retrieves the default type for modal components.
     *
     * @return The default modal component type.
     */
    @Key("modal.default.type")
    String modalDefaultType();

    /**
     * Retrieves the default type for accordion components.
     *
     * @return The default accordion component type.
     */
    @Key("accordion.default.type")
    String accordionDefaultType();

    /**
     * Retrieves the default type for table components.
     *
     * @return The default table component type.
     */
    @Key("table.default.type")
    String tableDefaultType();

    /**
     * Determines whether wrapped Selenium functions should be used.
     * <p>Default: true</p>
     *
     * @return {@code true} if wrapped Selenium functions should be used, otherwise {@code false}.
     */
    @DefaultValue("true")
    @Key("use.wrap.selenium.function")
    boolean useWrappedSeleniumFunctions();

    /**
     * Determines whether Shadow DOM elements should be used.
     * <p>Default: false</p>
     *
     * @return {@code true} if Shadow DOM elements should be used, otherwise {@code false}.
     */
    @DefaultValue("false")
    @Key("use.shadow.root")
    boolean useShadowRoot();

}
