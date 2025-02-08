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

    @Key("radio.default.type")
    String radioDefaultType();

    @Key("table.default.type")
    String tableDefaultType();

    @DefaultValue("true")
    @Key("use.wrap.selenium.function")
    boolean useWrappedSeleniumFunctions();

    @DefaultValue("false")
    @Key("use.shadow.root")
    boolean useShadowRoot();



}
