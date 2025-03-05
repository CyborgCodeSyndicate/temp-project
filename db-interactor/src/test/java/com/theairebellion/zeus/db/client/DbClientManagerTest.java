package com.theairebellion.zeus.db.client;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbType;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DbClientManagerTest {

    private static final String JDBC_PROTOCOL = "jdbc";
    private static final String LOCALHOST = "localhost";
    private static final int DEFAULT_PORT = 3306;
    private static final String TEST_DATABASE = "testdb";
    private static final String SAME_CLIENT_ERROR = "Expected the same client instance for the same configuration";
    private static final String DIFFERENT_CLIENT_ERROR = "Expected different client instances for different configurations";
    private static final String CLIENT_NOT_NULL = "Expected a non-null client to be created";
    private static final String CLIENT_INSTANCE_TYPE = "Expected client to be an instance of RelationalDbClient";
    private static final String CONNECTOR_GETTER = "Connector should be accessible via getter";

    @Mock
    private BaseDbConnectorService connector;

    @Spy
    @InjectMocks
    private DbClientManager manager;

    @Nested
    @DisplayName("Client Caching Tests")
    class ClientCachingTests {

        @Test
        @DisplayName("Should return cached client for same configuration")
        void testGetClient_ShouldReturnCachedClient() {
            // Given
            var dbConfig = mock(DatabaseConfiguration.class);
            var dbType = mock(DbType.class);

            when(dbConfig.getDbType()).thenReturn(dbType);
            when(dbType.protocol()).thenReturn(JDBC_PROTOCOL);
            when(dbConfig.getHost()).thenReturn(LOCALHOST);
            when(dbConfig.getPort()).thenReturn(DEFAULT_PORT);
            when(dbConfig.getDatabase()).thenReturn(TEST_DATABASE);

            // When
            var client1 = manager.getClient(dbConfig);
            var client2 = manager.getClient(dbConfig);

            // Then
            assertSame(client1, client2, SAME_CLIENT_ERROR);
            verify(manager, times(1)).initializeDbClient(dbConfig);
        }

        @Test
        @DisplayName("Should return different clients for different configurations")
        void testGetClient_ShouldReturnDifferentClientsForDifferentConfigs() {
            // Given
            var dbConfig1 = mock(DatabaseConfiguration.class);
            var dbConfig2 = mock(DatabaseConfiguration.class);
            var dbType = mock(DbType.class);

            when(dbConfig1.getDbType()).thenReturn(dbType);
            when(dbConfig2.getDbType()).thenReturn(dbType);
            when(dbType.protocol()).thenReturn(JDBC_PROTOCOL);

            when(dbConfig1.getHost()).thenReturn(LOCALHOST);
            when(dbConfig2.getHost()).thenReturn("other-host");

            when(dbConfig1.getPort()).thenReturn(DEFAULT_PORT);
            when(dbConfig2.getPort()).thenReturn(DEFAULT_PORT);

            when(dbConfig1.getDatabase()).thenReturn(TEST_DATABASE);
            when(dbConfig2.getDatabase()).thenReturn(TEST_DATABASE);

            // When
            var client1 = manager.getClient(dbConfig1);
            var client2 = manager.getClient(dbConfig2);

            // Then
            assertNotSame(client1, client2, DIFFERENT_CLIENT_ERROR);
            verify(manager, times(1)).initializeDbClient(dbConfig1);
            verify(manager, times(1)).initializeDbClient(dbConfig2);
        }

        @Test
        @DisplayName("Should return different clients when port is different")
        void testGetClient_ShouldReturnDifferentClientsForDifferentPorts() {
            // Given
            var dbConfig1 = mock(DatabaseConfiguration.class);
            var dbConfig2 = mock(DatabaseConfiguration.class);
            var dbType = mock(DbType.class);

            when(dbConfig1.getDbType()).thenReturn(dbType);
            when(dbConfig2.getDbType()).thenReturn(dbType);
            when(dbType.protocol()).thenReturn(JDBC_PROTOCOL);

            when(dbConfig1.getHost()).thenReturn(LOCALHOST);
            when(dbConfig2.getHost()).thenReturn(LOCALHOST);

            when(dbConfig1.getPort()).thenReturn(DEFAULT_PORT);
            when(dbConfig2.getPort()).thenReturn(DEFAULT_PORT + 1);

            when(dbConfig1.getDatabase()).thenReturn(TEST_DATABASE);
            when(dbConfig2.getDatabase()).thenReturn(TEST_DATABASE);

            // When
            var client1 = manager.getClient(dbConfig1);
            var client2 = manager.getClient(dbConfig2);

            // Then
            assertNotSame(client1, client2, DIFFERENT_CLIENT_ERROR);
        }

        @Test
        @DisplayName("Should return different clients when database is different")
        void testGetClient_ShouldReturnDifferentClientsForDifferentDatabases() {
            // Given
            var dbConfig1 = mock(DatabaseConfiguration.class);
            var dbConfig2 = mock(DatabaseConfiguration.class);
            var dbType = mock(DbType.class);

            when(dbConfig1.getDbType()).thenReturn(dbType);
            when(dbConfig2.getDbType()).thenReturn(dbType);
            when(dbType.protocol()).thenReturn(JDBC_PROTOCOL);

            when(dbConfig1.getHost()).thenReturn(LOCALHOST);
            when(dbConfig2.getHost()).thenReturn(LOCALHOST);

            when(dbConfig1.getPort()).thenReturn(DEFAULT_PORT);
            when(dbConfig2.getPort()).thenReturn(DEFAULT_PORT);

            when(dbConfig1.getDatabase()).thenReturn(TEST_DATABASE);
            when(dbConfig2.getDatabase()).thenReturn("other-db");

            // When
            var client1 = manager.getClient(dbConfig1);
            var client2 = manager.getClient(dbConfig2);

            // Then
            assertNotSame(client1, client2, DIFFERENT_CLIENT_ERROR);
        }
    }

    @Nested
    @DisplayName("Client Initialization Tests")
    class ClientInitializationTests {

        @Test
        @DisplayName("Should create new RelationalDbClient")
        void testInitializeDbClient_ShouldCreateNewClient() {
            // Given
            var dbConfig = mock(DatabaseConfiguration.class);

            // When
            var client = manager.initializeDbClient(dbConfig);

            // Then
            assertNotNull(client, CLIENT_NOT_NULL);
            assertInstanceOf(RelationalDbClient.class, client, CLIENT_INSTANCE_TYPE);
        }
    }

    @Test
    @DisplayName("Should return connector via getter")
    void testGetConnector() {
        // When
        var returnedConnector = manager.getConnector();

        // Then
        assertSame(connector, returnedConnector, CONNECTOR_GETTER);
    }
}