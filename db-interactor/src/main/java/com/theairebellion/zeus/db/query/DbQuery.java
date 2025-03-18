package com.theairebellion.zeus.db.query;

import com.theairebellion.zeus.ai.metadata.model.CreationType;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbConfig;

import static com.theairebellion.zeus.db.config.DbConfigHolder.getDbConfig;

@InfoAIClass(
    description = "Interface representing database queries. Should be implemented as enum and methods to be overridden",
    creationType = CreationType.ENUM,
    useAsKeyInStorage = true)
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
                   .build();
    }

    Enum<?> enumImpl();

    default DbQuery withParam(String name, Object value) {
        return new ParametrizedQuery(this).withParam(name, value);
    }


}
