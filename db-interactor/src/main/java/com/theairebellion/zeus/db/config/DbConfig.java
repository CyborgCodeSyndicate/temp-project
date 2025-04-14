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

   //todo: javaDocs - Check maybe not needed for all properties
   @Key("project.package")
   String projectPackage();

   @ConverterClass(DbTypeConverter.class)
   @Key("db.default.type")
   DbType type();

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

   @Key("db.full.connection.string")
   String fullConnectionString();

}
