package com.theairebellion.zeus.db.client;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages database clients for handling database interactions.
 * <p>
 * This class caches and retrieves database clients based on the provided
 * database configuration, ensuring efficient reuse of connections.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Component
public class DbClientManager {

    private final Map<String, DbClient> clientCache = new ConcurrentHashMap<>();
    @Getter
    private final BaseDbConnectorService connector;

    /**
     * Constructs a new {@code DbClientManager} with the given database connector service.
     *
     * @param connector The service responsible for establishing database connections.
     */
    @Autowired
    public DbClientManager(final BaseDbConnectorService connector) {
        this.connector = connector;
    }

    /**
     * Retrieves a database client based on the given configuration.
     * <p>
     * If a client is already cached for the given configuration, it is reused.
     * Otherwise, a new client is initialized.
     * </p>
     *
     * @param dbConfig The database configuration for which a client is required.
     * @return A {@code DbClient} instance corresponding to the configuration.
     */
    public DbClient getClient(DatabaseConfiguration dbConfig) {
        String urlKey = buildUrlKey(dbConfig);
        return clientCache.computeIfAbsent(urlKey, key -> initializeDbClient(dbConfig));
    }

    /**
     * Initializes a new database client instance.
     *
     * @param dbConfig The configuration used to initialize the client.
     * @return A new instance of {@code DbClient}.
     */
    protected DbClient initializeDbClient(final DatabaseConfiguration dbConfig) {
        return new RelationalDbClient(connector, dbConfig);
    }

    /**
     * Builds a unique key for the database configuration based on connection details.
     *
     * @param dbConfig The database configuration.
     * @return A unique string representing the database connection.
     */
    private String buildUrlKey(DatabaseConfiguration dbConfig) {
        return String.format("%s://%s:%d/%s",
                dbConfig.getDbType().protocol(),
                dbConfig.getHost(),
                dbConfig.getPort(),
                dbConfig.getDatabase());
    }

}
