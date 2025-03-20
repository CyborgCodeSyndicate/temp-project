package com.theairebellion.zeus.db.client;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DbClientManager {

    private final Map<String, DbClient> clientCache = new ConcurrentHashMap<>();
    @Getter
    private final BaseDbConnectorService connector;

    @Autowired
    public DbClientManager(final BaseDbConnectorService connector) {
        this.connector = connector;
    }

    public DbClient getClient(DatabaseConfiguration dbConfig) {
        String urlKey = dbConfig.getFullConnectionString() != null
                ? dbConfig.getFullConnectionString()
                : dbConfig.buildUrlKey();

        return clientCache.computeIfAbsent(urlKey, key -> initializeDbClient(dbConfig));
    }

    protected DbClient initializeDbClient(final DatabaseConfiguration dbConfig) {
        return new RelationalDbClient(connector, dbConfig);
    }

}

