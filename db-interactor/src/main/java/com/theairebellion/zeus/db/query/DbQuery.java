package com.theairebellion.zeus.db.query;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbConfig;

import static com.theairebellion.zeus.db.config.DbConfigHolder.getDbConfig;

public interface DbQuery {

    String query();

    default DatabaseConfiguration config() {
        DbConfig dbConfig = getDbConfig();
        return DatabaseConfiguration.builder()
                .dbType(dbConfig.type())
                .host(dbConfig.host())
                .port(dbConfig.port())
                .database(dbConfig.name())
                .dbUser(dbConfig.username())
                .dbPassword(dbConfig.password())
                .fullConnectionString(dbConfig.fullConnectionString())
                .build();
    }

    Enum<?> enumImpl();

    default DbQuery withParam(String name, Object value) {
        return new ParametrizedQuery(this).withParam(name, value);
    }


}
