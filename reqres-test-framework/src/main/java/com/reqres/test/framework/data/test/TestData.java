package com.reqres.test.framework.data.test;

import com.theairebellion.zeus.config.ConfigSource;
import com.theairebellion.zeus.config.PropertyConfig;
import org.aeonbits.owner.Config;

@ConfigSource("test-config")
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${test.data.file}.properties"})
public interface TestData extends PropertyConfig {

    @Key("username")
    String username();

    @Key("password")
    String password();

}
