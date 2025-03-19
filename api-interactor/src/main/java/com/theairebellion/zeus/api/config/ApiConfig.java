package com.theairebellion.zeus.api.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${api.config.file}.properties"})
public interface ApiConfig extends Config {

    @Key("project.package")
    String projectPackage();

    @DefaultValue("true")
    @Key("api.restassured.logging.enabled")
    boolean restAssuredLoggingEnabled();

    @DefaultValue("ALL")
    @Key("api.restassured.logging.level")
    String restAssuredLoggingLevel();

    @Key("api.base.url")
    String baseUrl();

}
