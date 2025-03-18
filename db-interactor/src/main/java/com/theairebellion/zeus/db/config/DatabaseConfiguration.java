package com.theairebellion.zeus.db.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents the configuration settings for database connections.
 * <p>
 * This class holds essential details such as the database type, host, port,
 * credentials, and database name required to establish a connection.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@AllArgsConstructor
@Getter
@Builder
public class DatabaseConfiguration {

    /**
     * The type of database (e.g., PostgreSQL, MySQL).
     */
    private DbType dbType;

    /**
     * The hostname or IP address of the database server.
     */
    private final String host;

    /**
     * The port number used for database connections.
     */
    private final int port;

    /**
     * The name of the database.
     */
    private final String database;

    /**
     * The username for authenticating the database connection.
     */
    private final String dbUser;

    /**
     * The password for authenticating the database connection.
     */
    private final String dbPassword;
}
