package com.theairebellion.zeus.db.query;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbConfig;
import org.aeonbits.owner.ConfigCache;

public interface DbQuery {

    DbConfig dbConfig = ConfigCache.getOrCreate(DbConfig.class);


    String query();

    default DatabaseConfiguration config() {
        return DatabaseConfiguration.builder()
                   .dbType(dbConfig.type())
                   .host(dbConfig.host())
                   .port(dbConfig.port())
                   .database(dbConfig.name())
                   .dbUser(dbConfig.username())
                   .dbPassword(dbConfig.password())
                   .build();
    }

    Enum<?> enumImpl();


    default DbQuery withParam(String name, Object value) {
        return new ParametrizedQuery(this).withParam(name, value);
    }


}
