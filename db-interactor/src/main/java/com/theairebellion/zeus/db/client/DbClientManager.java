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
        String urlKey = dbConfig.getDbType()
                            .protocol() + "://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + dbConfig.getDatabase();

        if (clientCache.containsKey(urlKey)) {
            return clientCache.get(urlKey);
        }

        DbClient newClient = initializeDbCLient(dbConfig);
        clientCache.put(urlKey, newClient);

        return newClient;
    }


    protected DbClient initializeDbCLient(DatabaseConfiguration dbConfig) {
        return new RelationalDbClient(connector, dbConfig);
    }

}

