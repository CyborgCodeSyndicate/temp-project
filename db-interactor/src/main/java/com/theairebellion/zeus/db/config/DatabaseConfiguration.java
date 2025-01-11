package com.theairebellion.zeus.db.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class DatabaseConfiguration {

    private DbType dbType;
    private final String host;
    private final int port;
    private final String database;
    private final String dbUser;
    private final String dbPassword;

}
