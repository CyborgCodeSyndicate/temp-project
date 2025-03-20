package com.theairebellion.zeus.db.config;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DatabaseConfiguration {

    private DbType dbType;
    private String host;
    private Integer port;
    private String database;
    private String dbUser;
    private String dbPassword;
    private String fullConnectionString;


    public String buildUrlKey() {
        return String.format("%s://%s:%d/%s",
                dbType.protocol(),
                host,
                port,
                database);
    }
}
