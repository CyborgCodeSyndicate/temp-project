package com.theairebellion.zeus.db.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${db.config.file}.properties"})
public interface DbConfig extends Config {

    @Key("project.package")
    String projectPackage();

    @ConverterClass(DbTypeConverter.class)
    @Key("db.default.type")
    DbType type();

    @Key("db.default.host")
    String host();

    @Key("db.default.port")
    Integer port();

    @Key("db.default.name")
    String name();

    @Key("db.default.username")
    String username();

    @Key("db.default.password")
    String password();

    @Key("db.full.connection.string")
    String fullConnectionString();

}
