package com.theairebellion.zeus.db.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseConfigurationTest {

    private static final String MOCK_PROTOCOL = "mock-protocol";
    private static final String HOST = "localhost";
    private static final int PORT = 5432;
    private static final String DATABASE = "testdb";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    @Test
    void testDatabaseConfigurationBuilder() {
        DbType mockDbType = new DbType() {
            @Override
            public java.sql.Driver driver() {
                return null;
            }

            @Override
            public String protocol() {
                return MOCK_PROTOCOL;
            }

            @Override
            public Enum<?> enumImpl() {
                return null;
            }
        };

        DatabaseConfiguration config = DatabaseConfiguration.builder()
                .dbType(mockDbType)
                .host(HOST)
                .port(PORT)
                .database(DATABASE)
                .dbUser(USER)
                .dbPassword(PASSWORD)
                .build();

        assertEquals(mockDbType, config.getDbType());
        assertEquals(HOST, config.getHost());
        assertEquals(PORT, config.getPort());
        assertEquals(DATABASE, config.getDatabase());
        assertEquals(USER, config.getDbUser());
        assertEquals(PASSWORD, config.getDbPassword());
    }
}