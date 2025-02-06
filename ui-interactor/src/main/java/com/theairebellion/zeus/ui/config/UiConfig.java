package com.theairebellion.zeus.ui.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${ui.config.file}.properties"})
public interface UiConfig extends Config {

    @DefaultValue("CHROME")
    @Key("browser.type")
    String browserType();

    @DefaultValue("")
    @Key("browser.version")
    String browserVersion();

    @DefaultValue("false")
    @Key("headless")
    boolean headless();

    @Key("wait.duration.in.seconds")
    int waitDuration();

    @Key("project.package")
    String projectPackage();

    @Key("input.default.type")
    String inputDefaultType();

    @Key("button.default.type")
    String buttonDefaultType();

    @Key("checkbox.default.type")
    String checkboxDefaultType();

    @Key("toggle.default.type")
    String toggleDefaultType();

    @Key("radio.default.type")
    String radioDefaultType();

    @Key("select.default.type")
    String selectDefaultType();

    @Key("list.default.type")
    String listDefaultType();

    @Key("loader.default.type")
    String loaderDefaultType();

    @Key("link.default.type")
    String linkDefaultType();

    @Key("alert.default.type")
    String alertDefaultType();

    @Key("tab.default.type")
    String tabDefaultType();

    @Key("modal.default.type")
    String modalDefaultType();

    @Key("accordion.default.type")
    String accordionDefaultType();

    @DefaultValue("true")
    @Key("use.wrap.selenium.function")
    boolean useWrappedSeleniumFunctions();

}
