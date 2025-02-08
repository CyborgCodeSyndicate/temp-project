package com.theairebellion.zeus.db.client;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbType;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DbClientManagerTest {

    private static final String JDBC_PROTOCOL = "jdbc";
    private static final String LOCALHOST = "localhost";
    private static final int DEFAULT_PORT = 3306;
    private static final String TEST_DATABASE = "testdb";
    private static final String SAME_CLIENT_ERROR = "Expected the same client instance for the same configuration";
    private static final String CLIENT_NOT_NULL = "Expected a non-null client to be created";
    private static final String CLIENT_INSTANCE_TYPE = "Expected client to be an instance of RelationalDbClient";

    private DbClientManager manager;

    @BeforeEach
    void setUp() {
        var connector = mock(BaseDbConnectorService.class);
        manager = new DbClientManager(connector);
    }

    @Test
    void testGetClient_ShouldReturnCachedClient() {
        var dbConfig = mock(DatabaseConfiguration.class);
        var dbType = mock(DbType.class);

        when(dbConfig.getDbType()).thenReturn(dbType);
        when(dbType.protocol()).thenReturn(JDBC_PROTOCOL);
        when(dbConfig.getHost()).thenReturn(LOCALHOST);
        when(dbConfig.getPort()).thenReturn(DEFAULT_PORT);
        when(dbConfig.getDatabase()).thenReturn(TEST_DATABASE);

        var client1 = manager.getClient(dbConfig);
        var client2 = manager.getClient(dbConfig);

        assertSame(client1, client2, SAME_CLIENT_ERROR);
    }

    @Test
    void testInitializeDbClient_ShouldCreateNewClient() {
        var dbConfig = mock(DatabaseConfiguration.class);

        var client = manager.initializeDbClient(dbConfig);

        assertNotNull(client, CLIENT_NOT_NULL);
        assertInstanceOf(RelationalDbClient.class, client, CLIENT_INSTANCE_TYPE);
    }
}