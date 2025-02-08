package com.theairebellion.zeus.db.client;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbType;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DbClientManagerTest {

    private DbClientManager manager;

    @BeforeEach
    void setUp() {
        BaseDbConnectorService connector = mock(BaseDbConnectorService.class);
        manager = new DbClientManager(connector);
    }

    @Test
    void testGetClient_ShouldReturnCachedClient() {
        DatabaseConfiguration dbConfig = mock(DatabaseConfiguration.class);
        DbType dbType = mock(DbType.class);

        when(dbConfig.getDbType()).thenReturn(dbType);
        when(dbType.protocol()).thenReturn("jdbc");
        when(dbConfig.getHost()).thenReturn("localhost");
        when(dbConfig.getPort()).thenReturn(3306);
        when(dbConfig.getDatabase()).thenReturn("testdb");

        DbClient client1 = manager.getClient(dbConfig);
        DbClient client2 = manager.getClient(dbConfig);

        assertSame(client1, client2, "Expected the same client instance for the same configuration");
    }

    @Test
    void testInitializeDbClient_ShouldCreateNewClient() {
        DatabaseConfiguration dbConfig = mock(DatabaseConfiguration.class);

        DbClient client = manager.initializeDbClient(dbConfig);

        assertNotNull(client, "Expected a non-null client to be created");
        assertInstanceOf(RelationalDbClient.class, client, "Expected client to be an instance of RelationalDbClient");
    }
}