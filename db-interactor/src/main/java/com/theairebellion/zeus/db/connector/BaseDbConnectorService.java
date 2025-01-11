package com.theairebellion.zeus.db.connector;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbType;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Component
public class BaseDbConnectorService {

    private final Map<String, Connection> connectionMap = new HashMap<>();
    private final Set<DbType> registeredTypes = new HashSet<>();


    public Connection getConnection(DatabaseConfiguration dbConfig) {
        synchronized (this) {
            DbType dbType = dbConfig.getDbType();

            if (!registeredTypes.contains(dbType)) {
                try {
                    DriverManager.registerDriver(dbType.driver());
                    registeredTypes.add(dbType);
                } catch (Exception e) {
                    throw new RuntimeException("Database driver registration failed for type: " + dbType, e);
                }
            }

            String url = dbType.protocol() + "://" + dbConfig.getHost() + ":" + dbConfig.getPort() + "/" + dbConfig.getDatabase();
            if (!connectionMap.containsKey(url)) {
                try {
                    connectionMap.put(url,
                        DriverManager.getConnection(url, dbConfig.getDbUser(), dbConfig.getDbPassword()));
                } catch (SQLException e) {
                    throw new RuntimeException("Database connection failed for url: " + url, e);
                }
            }
            return connectionMap.get(url);
        }
    }


    public void closeConnections() {
        connectionMap.forEach((s, connection) -> {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        connectionMap.clear();
        registeredTypes.clear();
    }

}
