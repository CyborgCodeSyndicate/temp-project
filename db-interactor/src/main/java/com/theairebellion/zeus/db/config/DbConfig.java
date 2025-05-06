package com.theairebellion.zeus.db.config;

import com.theairebellion.zeus.config.ConfigSource;
import com.theairebellion.zeus.config.PropertyConfig;
import org.aeonbits.owner.Config;

/**
 * Configuration interface for database connection settings.
 *
 * <p>This interface uses the OWNER library to load database configuration properties
 * from system properties or a specified configuration file.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@ConfigSource("db-config")
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:${db.config.file}.properties"})
public interface DbConfig extends PropertyConfig {

    /**
     * Retrieves the base Java package for your project.
     * <p>
     * Used by reflection utilities to scan for entity classes and database hooks
     * under the specified root package.
     * </p>
     *
     * @return the root package name of the application code
     */
    @Key("project.package")
    String projectPackage();

    /**
     * Retrieves the default database type.
     * <p>
     * Determines which {@code DbType} implementation (e.g., MySQL, PostgreSQL)
     * is used to configure connections and dialect behavior.
     * </p>
     *
     * @return the configured {@code DbType} for the default database
     */
    @SuppressWarnings("squid:S1452")
    @ConverterClass(DbTypeConverter.class)
    @Key("db.default.type")
    DbType<?> type();

   /**
    * Retrieves the default database host.
    *
    * @return The hostname or IP address of the database.
    */
   @Key("db.default.host")
   String host();

   /**
    * Retrieves the default database port.
    *
    * @return The port number used for database connections.
    */
   @Key("db.default.port")
   Integer port();

   /**
    * Retrieves the default database name.
    *
    * @return The name of the database.
    */
   @Key("db.default.name")
   String name();

   /**
    * Retrieves the default database username.
    *
    * @return The username used for authentication.
    */
   @Key("db.default.username")
   String username();

   /**
    * Retrieves the default database password.
    *
    * @return The password used for authentication.
    */
   @Key("db.default.password")
   String password();

    /**
     * Retrieves the full JDBC connection string for the database.
     * <p>
     * If provided, this URL will be used directly to establish connections,
     * overriding the individual host, port, and name properties.
     * </p>
     *
     * @return the complete JDBC URL, or {@code null} if not set
     */
    @Key("db.full.connection.string")
    String fullConnectionString();

}
