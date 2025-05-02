package com.theairebellion.zeus.db.connector;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbType;
import com.theairebellion.zeus.db.log.LogDb;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Manages database connections and driver registrations.
 *
 * <p>This service handles database connections by registering drivers,
 * creating new connections, and caching them for reuse. It supports
 * multiple database types and ensures efficient connection management.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@Component
public class BaseDbConnectorService {

   private static final Map<String, Connection> connectionMap = new ConcurrentHashMap<>();
   private static final Set<DbType<?>> registeredTypes = Collections.synchronizedSet(new HashSet<>());

   /**
    * Retrieves or creates a database connection based on the provided configuration.
    *
    * @param dbConfig The database configuration.
    * @return A {@link Connection} instance for the specified database.
    */
   public Connection getConnection(DatabaseConfiguration dbConfig) {
      DbType<?> dbType = dbConfig.getDbType();
      registerDriverIfNecessary(dbType);
      String url = buildConnectionUrl(dbConfig);
      return connectionMap.computeIfAbsent(url, u -> createConnection(u, dbConfig));
   }

   /**
    * Registers a database driver if it has not been registered before.
    *
    * @param dbType The database type.
    */
   private void registerDriverIfNecessary(DbType<?> dbType) {
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

   /**
    * Constructs the connection URL for the database.
    *
    * @param dbConfig The database configuration.
    * @return The constructed database connection URL.
    */
   private String buildConnectionUrl(DatabaseConfiguration dbConfig) {
      String url = dbConfig.getFullConnectionString() != null
            ? dbConfig.getFullConnectionString()
            : dbConfig.buildUrlKey();
      LogDb.debug("Built connection URL: {}", url);
      return url;
   }

   /**
    * Creates a new database connection.
    *
    * @param url      The connection URL.
    * @param dbConfig The database configuration containing credentials.
    * @return A new {@link Connection} instance.
    * @throws IllegalStateException If the connection fails to be established.
    */
   private Connection createConnection(String url, DatabaseConfiguration dbConfig) {
      try {
         Connection connection = DriverManager.getConnection(url, dbConfig.getDbUser(), dbConfig.getDbPassword());
         LogDb.info("Successfully created connection for URL: {}", url);
         return connection;
      } catch (SQLException e) {
         throw new IllegalStateException("Failed to create connection for URL: " + url, e);
      }
   }

   /**
    * Closes all active database connections and clears cached connections.
    */
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
