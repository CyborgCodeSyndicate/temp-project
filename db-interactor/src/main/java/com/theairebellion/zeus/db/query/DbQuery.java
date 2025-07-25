package com.theairebellion.zeus.db.query;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbConfig;
import com.theairebellion.zeus.db.config.DbConfigHolder;

import static com.theairebellion.zeus.db.config.DbConfigHolder.getDbConfig;

/**
 * Represents a database query and provides configuration details.
 *
 * <p>This interface defines a structured way to execute database queries,
 * retrieve their configurations, and apply parameterized values dynamically.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface DbQuery<T extends Enum<T>> {

   /**
    * Retrieves the raw query string.
    *
    * @return The SQL query as a {@code String}.
    */
   String query();

   /**
    * Provides the database configuration associated with this query.
    *
    * <p>The configuration is fetched from {@link DbConfigHolder}, ensuring
    * consistency across database queries.
    *
    * @return The {@link DatabaseConfiguration} for the query.
    */
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

   /**
    * Retrieves the enum representation of the query implementation.
    *
    * @return The query's enum representation.
    */
   T enumImpl();

   /**
    * Applies a parameter to the query dynamically.
    *
    * <p>This method replaces placeholders within the query with actual values,
    * allowing parameterized SQL execution.
    *
    * @param name  The name of the parameter to replace.
    * @param value The value to assign to the parameter.
    * @return A new {@code DbQuery} instance with the applied parameter.
    */
   default DbQuery<T> withParam(String name, Object value) {
      return new ParametrizedQuery<>(this).withParam(name, value);
   }
}
