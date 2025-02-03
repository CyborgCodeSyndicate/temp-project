package com.theairebellion.zeus.db.connector;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BaseDbConnectorServiceTest {

    private BaseDbConnectorService dbConnectorService;
    private DatabaseConfiguration mockConfig;
    private DbType mockDbType;
    private Connection mockConnection;
    private Driver mockDriver;

    @BeforeEach
    void setup() throws SQLException {
        dbConnectorService = spy(new BaseDbConnectorService());
        mockConfig = mock(DatabaseConfiguration.class);
        mockDbType = mock(DbType.class);
        mockConnection = mock(Connection.class);
        mockDriver = mock(Driver.class);

        // Mock DatabaseConfiguration behavior
        when(mockConfig.getDbType()).thenReturn(mockDbType);
        when(mockConfig.getHost()).thenReturn("localhost");
        when(mockConfig.getPort()).thenReturn(5432);
        when(mockConfig.getDatabase()).thenReturn("testdb");
        when(mockConfig.getDbUser()).thenReturn("user");
        when(mockConfig.getDbPassword()).thenReturn("password");

        // Mock DbType behavior
        when(mockDbType.protocol()).thenReturn("jdbc:mock");
        when(mockDbType.driver()).thenReturn(mockDriver);

        // Mock Driver behavior
        when(mockDriver.acceptsURL("jdbc:mock://localhost:5432/testdb")).thenReturn(true);
        when(mockDriver.connect(eq("jdbc:mock://localhost:5432/testdb"), any())).thenReturn(mockConnection);

        // Register the mocked Driver
        DriverManager.registerDriver(mockDriver);
    }

    @AfterEach
    void teardown() throws SQLException {
        // Unregister the mocked Driver to avoid affecting other tests
        DriverManager.deregisterDriver(mockDriver);
    }

    @Test
    void testGetConnection_ShouldReturnSameConnectionForSameURL() throws SQLException {
        // Act
        Connection firstConnection = dbConnectorService.getConnection(mockConfig);
        Connection secondConnection = dbConnectorService.getConnection(mockConfig);

        // Assert
        assertNotNull(firstConnection);
        assertSame(firstConnection, secondConnection, "Connections should be the same for the same URL");
        verify(mockConnection, never()).close();
    }

    @Test
    void testRegisterDriverIfNecessary_ShouldNotRegisterDriverIfAlreadyRegistered() throws SQLException {
        // Act
        dbConnectorService.getConnection(mockConfig);
        dbConnectorService.getConnection(mockConfig); // Call again to ensure no duplicate registration

        // Assert
        verify(mockDbType, times(1)).driver();
    }

    @Test
    void testCloseConnections_ShouldHandleEmptyConnectionsGracefully() throws SQLException {
        // Act
        dbConnectorService.closeConnections();

        // Assert
        verify(mockConnection, never()).close(); // No connection to close
    }

    @Test
    void testCloseConnections_ShouldHandleAlreadyClosedConnections() throws SQLException {
        // Arrange
        when(mockConnection.isClosed()).thenReturn(true);

        // Act
        dbConnectorService.getConnection(mockConfig);
        dbConnectorService.closeConnections();

        // Assert
        verify(mockConnection, never()).close(); // Connection is already closed
    }

    @Test
    void testCreateConnection_ShouldThrowExceptionForInvalidUrl() {
        // Arrange
        when(mockConfig.getDatabase()).thenReturn("invalid-db");
        when(mockDbType.protocol()).thenReturn("jdbc:invalid");

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> dbConnectorService.getConnection(mockConfig));
    }

    @Test
    void testRegisterDriverIfNecessary_ShouldThrowExceptionOnDriverRegistrationFailure() throws SQLException {
        // Arrange
        DbType faultyDbType = mock(DbType.class);
        Driver mockDriver = mock(Driver.class);
        when(faultyDbType.driver()).thenReturn(mockDriver);
        doThrow(new SQLException("Driver registration failed")).when(mockDriver).acceptsURL(anyString());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            dbConnectorService.getConnection(
                    DatabaseConfiguration.builder()
                            .dbType(faultyDbType)
                            .host("localhost")
                            .port(5432)
                            .database("testdb")
                            .dbUser("user")
                            .dbPassword("password")
                            .build()
            );
        });

        verify(faultyDbType, times(1)).driver(); // Ensure the driver is fetched
    }

    @Test
    void testRegisterDriverIfNecessary_ShouldNotRegisterAlreadyRegisteredDriver() {
        // Act: Call getConnection multiple times
        dbConnectorService.getConnection(mockConfig);
        dbConnectorService.getConnection(mockConfig);

        // Assert: Verify the driver was only registered once
        verify(mockDbType, times(1)).driver();
    }

    @Test
    void testRegisterDriverIfNecessary_ShouldThrowExceptionOnDriverRegistrationFailure_StaticMock() {
        // Create a new DbType and a new Driver that have not been registered before.
        DbType failingDbType = mock(DbType.class);
        Driver failingDriver = mock(Driver.class);
        when(failingDbType.protocol()).thenReturn("jdbc:fail");
        when(failingDbType.driver()).thenReturn(failingDriver);

        // Create a new DatabaseConfiguration using the failing DbType.
        DatabaseConfiguration failingConfig = DatabaseConfiguration.builder()
                .dbType(failingDbType)
                .host("localhost")
                .port(5432)
                .database("faildb")
                .dbUser("user")
                .dbPassword("password")
                .build();

        // Create a default answer that intercepts static calls.
        Answer<Object> defaultAnswer = new Answer<>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String methodName = invocation.getMethod().getName();
                // Stub getLogWriter() to always return null.
                if ("getLogWriter".equals(methodName)) {
                    return null;
                }
                // For registerDriver(Driver): if the argument is our failingDriver, throw SQLException.
                if ("registerDriver".equals(methodName)) {
                    Object arg0 = invocation.getArgument(0);
                    if (failingDriver.equals(arg0)) {
                        throw new SQLException("Simulated registration failure");
                    }
                }
                // For any other static method call on DriverManager, return null.
                return null;
            }
        };

        // Use static mocking with the default answer.
        try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class, defaultAnswer)) {
            // Act & Assert: When getConnection is called, driver registration should fail.
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> dbConnectorService.getConnection(failingConfig));
            assertTrue(exception.getMessage().contains("Failed to register database driver for type:"));
        }
    }

    @Test
    void testCloseConnections_ShouldHandleSQLExceptionOnClose() throws SQLException {
        // Arrange: Set up the connection to appear open and then throw an exception when close() is called.
        when(mockConnection.isClosed()).thenReturn(false);
        doThrow(new SQLException("Simulated close failure")).when(mockConnection).close();

        // Ensure the connection is created (and added to the internal map).
        dbConnectorService.getConnection(mockConfig);

        // Act & Assert: Calling closeConnections() should not propagate the SQLException.
        assertDoesNotThrow(() -> dbConnectorService.closeConnections());
    }
}