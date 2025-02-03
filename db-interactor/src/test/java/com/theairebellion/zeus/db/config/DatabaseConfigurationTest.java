package com.theairebellion.zeus.db.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseConfigurationTest {

    @Test
    void testDatabaseConfigurationBuilder() {
        // Arrange
        DbType mockDbType = new DbType() {
            @Override
            public java.sql.Driver driver() {
                return null;
            }

            @Override
            public String protocol() {
                return "mock-protocol";
            }

            @Override
            public Enum<?> enumImpl() {
                return null;
            }
        };

        String host = "localhost";
        int port = 5432;
        String database = "testdb";
        String dbUser = "user";
        String dbPassword = "password";

        // Act
        DatabaseConfiguration config = DatabaseConfiguration.builder()
                .dbType(mockDbType)
                .host(host)
                .port(port)
                .database(database)
                .dbUser(dbUser)
                .dbPassword(dbPassword)
                .build();

        // Assert
        assertEquals(mockDbType, config.getDbType());
        assertEquals(host, config.getHost());
        assertEquals(port, config.getPort());
        assertEquals(database, config.getDatabase());
        assertEquals(dbUser, config.getDbUser());
        assertEquals(dbPassword, config.getDbPassword());
    }
}