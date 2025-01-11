package com.theairebellion.zeus.framework.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${framework.config.file}.properties"})
public interface FrameworkConfig extends Config {

    @Key("project.package")
    String projectPackage();

}
