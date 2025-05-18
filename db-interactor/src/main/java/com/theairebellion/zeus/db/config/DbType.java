package com.theairebellion.zeus.db.config;

/**
 * Defines the contract for database types.
 *
 * <p>This interface provides methods for retrieving database driver information,
 * connection protocol, and an enumeration representing the database type.
 * Implementing enums define specific database types (e.g., PostgreSQL, MySQL).
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public interface DbType<T extends Enum<T>> {

   /**
    * Retrieves the driver associated with the database type.
    *
    * @return The {@link java.sql.Driver} instance.
    */
   java.sql.Driver driver();

   /**
    * Retrieves the connection protocol for the database type.
    *
    * @return The database connection protocol as a string.
    */
   String protocol();

   /**
    * Retrieves the enum implementation representing this database type.
    *
    * @return An enumeration representing the database type.
    */
   T enumImpl();
}
