package com.example.project.data.test;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${test.data.file}.properties"})
public interface TestData extends Config {

    @Key("username")
    String username();

}
