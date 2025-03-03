package com.theairebellion.zeus.ui.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${ui.config.file}.properties"})
public interface UIFrameworkConfig extends Config {

    @DefaultValue("false")
    @Key("screenshot.on.passed.test")
    boolean makeScreenshotOnPassedTest();

}
