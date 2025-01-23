package com.theairebellion.zeus.db.connector;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbType;
import com.theairebellion.zeus.db.log.LogDb;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BaseDbConnectorService {

    private final Map<String, Connection> connectionMap = new ConcurrentHashMap<>();
    private final Set<DbType> registeredTypes = Collections.synchronizedSet(new HashSet<>());

    public Connection getConnection(DatabaseConfiguration dbConfig) {
        DbType dbType = dbConfig.getDbType();

        registerDriverIfNecessary(dbType);

        String url = buildConnectionUrl(dbType, dbConfig);

        return connectionMap.computeIfAbsent(url, u -> createConnection(u, dbConfig));
    }

    private void registerDriverIfNecessary(DbType dbType) {
        if (!registeredTypes.contains(dbType)) {
            synchronized (registeredTypes) {
                if (!registeredTypes.contains(dbType)) {
                    try {
                        DriverManager.registerDriver(dbType.driver());
                        registeredTypes.add(dbType);
                        LogDb.info("Registered database driver for type: {}", dbType);
                    } catch (SQLException e) {
                        throw new IllegalStateException("Failed to register database driver for type: " + dbType, e);
                    }
                }
            }
        }
    }

    private String buildConnectionUrl(DbType dbType, DatabaseConfiguration dbConfig) {
        String url = String.format("%s://%s:%d/%s",
                dbType.protocol(),
                dbConfig.getHost(),
                dbConfig.getPort(),
                dbConfig.getDatabase());
        LogDb.debug("Built connection URL: {}", url);
        return url;
    }

    private Connection createConnection(String url, DatabaseConfiguration dbConfig) {
        try {
            Connection connection = DriverManager.getConnection(url, dbConfig.getDbUser(), dbConfig.getDbPassword());
            LogDb.info("Successfully created connection for URL: {}", url);
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to create connection for URL: " + url, e);
        }
    }

    public void closeConnections() {
        connectionMap.forEach((url, connection) -> {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    LogDb.info("Closed connection for URL: {}", url);
                }
            } catch (SQLException e) {
                LogDb.warn("Failed to close connection for URL: {}", url, e);
            }
        });
        connectionMap.clear();
        registeredTypes.clear();
        LogDb.info("Cleared all connections and registered types.");
    }
}
