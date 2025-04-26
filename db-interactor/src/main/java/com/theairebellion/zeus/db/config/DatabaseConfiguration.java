package com.theairebellion.zeus.db.config;

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
@Getter
@Builder
public class DatabaseConfiguration {

    /**
     * The type of database (e.g., PostgreSQL, MySQL, h2).
     */
    private DbType dbType;

    /**
     * The hostname or IP address of the database server.
     */
    private String host;

    /**
     * The port number used for database connections.
     */
    private Integer port;

    /**
     * The name of the database.
     */
    private String database;

    /**
     * The username for authenticating the database connection.
     */
    private String dbUser;

    /**
     * The password for authenticating the database connection.
     */
    private String dbPassword;

    /**
     * If set, this full JDBC URL will be used as the cache key instead
     * of the host/port/database combination.
     */
    private String fullConnectionString;

    /**
     * Builds a unique key from protocol, host, port and database name.
     * Used when {@link #fullConnectionString} is null.
     *
     * @return A string of the form "protocol://host:port/database"
     */
    public String buildUrlKey() {
        return String.format("%s://%s:%d/%s",
                dbType.protocol(),
                host,
                port,
                database);
    }
}
