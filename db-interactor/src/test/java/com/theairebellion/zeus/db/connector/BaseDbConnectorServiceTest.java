package com.theairebellion.zeus.db.connector;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseDbConnectorServiceTest {

    private static final String HOST = "localhost";
    private static final int PORT = 5432;
    private static final String DATABASE = "testdb";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "password";
    private static final String MOCK_PROTOCOL = "jdbc:mock";
    private static final String CONNECTION_URL = MOCK_PROTOCOL + "://" + HOST + ":" + PORT + "/" + DATABASE;

    @Spy
    private BaseDbConnectorService dbConnectorService;

    @Mock
    private DatabaseConfiguration mockConfig;

    @Mock
    private DbType mockDbType;

    @Mock
    private Connection mockConnection;

    @Mock
    private Driver mockDriver;

    @BeforeEach
    void setup() {
        // Configure mocks - use lenient for setup that might not be used in every test
        lenient().when(mockConfig.getDbType()).thenReturn(mockDbType);
        lenient().when(mockConfig.getHost()).thenReturn(HOST);
        lenient().when(mockConfig.getPort()).thenReturn(PORT);
        lenient().when(mockConfig.getDatabase()).thenReturn(DATABASE);
        lenient().when(mockConfig.getDbUser()).thenReturn(DB_USER);
        lenient().when(mockConfig.getDbPassword()).thenReturn(DB_PASSWORD);
        lenient().when(mockDbType.protocol()).thenReturn(MOCK_PROTOCOL);
        lenient().when(mockDbType.driver()).thenReturn(mockDriver);
    }

    @Nested
    @DisplayName("Connection Management Tests")
    class ConnectionManagementTests {

        @Test
        @DisplayName("Should return the same connection for the same URL")
        void testGetConnection_ShouldReturnSameConnectionForSameURL() throws SQLException {
            // Given
            // Set up driver acceptance
            lenient().when(mockDriver.acceptsURL(anyString())).thenReturn(true);

            try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
                // Use doNothing for void methods
                driverManagerMock.when(() -> DriverManager.registerDriver(any(Driver.class)))
                        .then(invocation -> null); // Mockito needs an Answer but it's ignored for void methods

                driverManagerMock.when(() -> DriverManager.getConnection(
                                anyString(), anyString(), anyString()))
                        .thenReturn(mockConnection);

                // When
                Connection firstConnection = dbConnectorService.getConnection(mockConfig);
                Connection secondConnection = dbConnectorService.getConnection(mockConfig);

                // Then
                assertNotNull(firstConnection, "Connection should not be null");
                assertSame(firstConnection, secondConnection, "Same connection should be returned for same URL");

                // Verify DriverManager.getConnection was called only once
                driverManagerMock.verify(() -> DriverManager.getConnection(anyString(), anyString(), anyString()), times(1));
            }
        }

        @Test
        @DisplayName("Should create different connections for different URLs")
        void testGetConnection_ShouldCreateDifferentConnectionsForDifferentURLs() throws SQLException {
            // Given
            DatabaseConfiguration otherConfig = mock(DatabaseConfiguration.class);
            String otherDatabase = "otherdb";
            Connection otherConnection = mock(Connection.class);

            // Configure other config mock
            when(otherConfig.getDbType()).thenReturn(mockDbType);
            when(otherConfig.getHost()).thenReturn(HOST);
            when(otherConfig.getPort()).thenReturn(PORT);
            when(otherConfig.getDatabase()).thenReturn(otherDatabase);
            when(otherConfig.getDbUser()).thenReturn(DB_USER);
            when(otherConfig.getDbPassword()).thenReturn(DB_PASSWORD);

            lenient().when(mockDriver.acceptsURL(anyString())).thenReturn(true);

            try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
                // Use then(invocation -> null) for void methods
                driverManagerMock.when(() -> DriverManager.registerDriver(any(Driver.class)))
                        .then(invocation -> null);

                // Use URL capture to return different connections
                driverManagerMock.when(() -> DriverManager.getConnection(
                                contains(DATABASE), anyString(), anyString()))
                        .thenReturn(mockConnection);

                driverManagerMock.when(() -> DriverManager.getConnection(
                                contains(otherDatabase), anyString(), anyString()))
                        .thenReturn(otherConnection);

                // When
                Connection firstConnection = dbConnectorService.getConnection(mockConfig);
                Connection secondConnection = dbConnectorService.getConnection(otherConfig);

                // Then
                assertNotNull(firstConnection, "First connection should not be null");
                assertNotNull(secondConnection, "Second connection should not be null");
                assertNotSame(firstConnection, secondConnection, "Different connections should be returned for different URLs");
            }
        }
    }

    @Nested
    @DisplayName("Driver Registration Tests")
    class DriverRegistrationTests {

        @Test
        @DisplayName("Should not register driver if already registered")
        void testRegisterDriverIfNecessary_ShouldNotRegisterDriverIfAlreadyRegistered() {
            // Given
            try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
                // Use proper void method stubbing
                driverManagerMock.when(() -> DriverManager.registerDriver(any(Driver.class)))
                        .then(invocation -> null);

                driverManagerMock.when(() -> DriverManager.getConnection(
                                anyString(), anyString(), anyString()))
                        .thenReturn(mockConnection);

                // When
                dbConnectorService.getConnection(mockConfig);
                dbConnectorService.getConnection(mockConfig);

                // Then
                // Verify driver registration happens only once
                driverManagerMock.verify(() -> DriverManager.registerDriver(any(Driver.class)), times(1));
            }
        }

        @Test
        @DisplayName("Should throw exception when driver registration fails")
        void testRegisterDriverIfNecessary_ShouldThrowExceptionOnDriverRegistrationFailure() {
            // Given
            SQLException registrationException = new SQLException("Simulated registration failure");

            try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
                // Use doThrow for void methods with exceptions
                driverManagerMock.when(() -> DriverManager.registerDriver(any(Driver.class)))
                        .thenThrow(registrationException);

                // When/Then
                IllegalStateException exception = assertThrows(
                        IllegalStateException.class,
                        () -> dbConnectorService.getConnection(mockConfig)
                );

                // Verify message contains expected text
                assertTrue(exception.getMessage().contains("Failed to register database driver"),
                        "Exception message should indicate driver registration failure");
                assertInstanceOf(SQLException.class, exception.getCause(),
                        "Exception cause should be SQLException");
            }
        }
    }

    @Nested
    @DisplayName("Connection Closing Tests")
    class ConnectionClosingTests {

        @Test
        @DisplayName("Should gracefully handle empty connection map when closing")
        void testCloseConnections_ShouldHandleEmptyConnectionsGracefully() {
            // When/Then - no setup needed, the map starts empty
            assertDoesNotThrow(() -> dbConnectorService.closeConnections(),
                    "Closing connections with empty map should not throw");
        }

        @Test
        @DisplayName("Should not close connections that are already closed")
        void testCloseConnections_ShouldHandleAlreadyClosedConnections() throws SQLException {
            // Given
            when(mockConnection.isClosed()).thenReturn(true);

            try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
                // Setup required mocks with proper void handling
                driverManagerMock.when(() -> DriverManager.registerDriver(any(Driver.class)))
                        .then(invocation -> null);

                driverManagerMock.when(() -> DriverManager.getConnection(
                                anyString(), anyString(), anyString()))
                        .thenReturn(mockConnection);

                // First get a connection to populate the map
                dbConnectorService.getConnection(mockConfig);

                // When
                dbConnectorService.closeConnections();

                // Then
                verify(mockConnection, never()).close();
            }
        }

        @Test
        @DisplayName("Should handle SQLException when closing connections")
        void testCloseConnections_ShouldHandleSQLExceptionOnClose() throws SQLException {
            // Given
            when(mockConnection.isClosed()).thenReturn(false);
            doThrow(new SQLException("Simulated close failure")).when(mockConnection).close();

            try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
                // Setup required mocks with proper void handling
                driverManagerMock.when(() -> DriverManager.registerDriver(any(Driver.class)))
                        .then(invocation -> null);

                driverManagerMock.when(() -> DriverManager.getConnection(
                                anyString(), anyString(), anyString()))
                        .thenReturn(mockConnection);

                // First get a connection to populate the map
                dbConnectorService.getConnection(mockConfig);

                // When/Then
                assertDoesNotThrow(() -> dbConnectorService.closeConnections(),
                        "Closing connections should handle SQLException gracefully");

                // Verify close was called even though it threw exception
                verify(mockConnection).close();
            }
        }

        @Test
        @DisplayName("Should clear connection map after closing")
        void testCloseConnections_ShouldClearConnectionMap() {
            // Given
            try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
                // Setup required mocks with proper void handling
                driverManagerMock.when(() -> DriverManager.registerDriver(any(Driver.class)))
                        .then(invocation -> null);

                driverManagerMock.when(() -> DriverManager.getConnection(
                                anyString(), anyString(), anyString()))
                        .thenReturn(mockConnection);

                // First get a connection to populate the map
                dbConnectorService.getConnection(mockConfig);

                // When
                dbConnectorService.closeConnections();

                // Then - clearInvocations doesn't work well with static mocks
                // So we'll verify the collection was cleared by checking a second call
                // creates a new connection (it will call getConnection again if the map was cleared)

                // Reset the static mock to verify the next call
                driverManagerMock.reset();

                // Setup for verification
                driverManagerMock.when(() -> DriverManager.registerDriver(any(Driver.class)))
                        .then(invocation -> null);

                driverManagerMock.when(() -> DriverManager.getConnection(
                                anyString(), anyString(), anyString()))
                        .thenReturn(mockConnection);

                // Call again and verify connection is fetched again (map was cleared)
                dbConnectorService.getConnection(mockConfig);

                // Verify DriverManager.getConnection was called again
                driverManagerMock.verify(() -> DriverManager.getConnection(
                        anyString(), anyString(), anyString()), times(1));
            }
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw exception when connection creation fails")
        void testCreateConnection_ShouldThrowExceptionForConnectionFailure() {
            // Given
            SQLException connectionException = new SQLException("Connection failed");

            try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
                // Set up all mocks before using them, with proper void handling
                driverManagerMock.when(() -> DriverManager.registerDriver(any(Driver.class)))
                        .then(invocation -> null);

                driverManagerMock.when(() -> DriverManager.getConnection(
                                anyString(), anyString(), anyString()))
                        .thenThrow(connectionException);

                // When/Then
                IllegalStateException exception = assertThrows(
                        IllegalStateException.class,
                        () -> dbConnectorService.getConnection(mockConfig)
                );

                // Verify message contains expected text
                assertTrue(exception.getMessage().contains("Failed to create connection"),
                        "Exception message should indicate connection failure");
                assertSame(connectionException, exception.getCause(),
                        "Exception cause should be the SQLException we threw");
            }
        }

        @Test
        @DisplayName("Should build correct connection URL")
        void testBuildConnectionUrl_ShouldFormatUrlCorrectly() {
            // Given

            // Use reflection to access private method
            try {
                java.lang.reflect.Method method = BaseDbConnectorService.class.getDeclaredMethod(
                        "buildConnectionUrl", DbType.class, DatabaseConfiguration.class);
                method.setAccessible(true);

                // When
                String actualUrl = (String) method.invoke(dbConnectorService, mockDbType, mockConfig);

                // Then
                assertEquals(CONNECTION_URL, actualUrl, "URL should be formatted correctly");
            } catch (Exception e) {
                fail("Failed to invoke buildConnectionUrl method: " + e.getMessage());
            }
        }
    }
}