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

    private static final String HOST = "localhost";
    private static final int PORT = 5432;
    private static final String DATABASE = "testdb";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "password";
    private static final String MOCK_PROTOCOL = "jdbc:mock";
    private static final String FAILING_PROTOCOL = "jdbc:fail";
    private static final String FAILING_DATABASE = "faildb";

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

        when(mockConfig.getDbType()).thenReturn(mockDbType);
        when(mockConfig.getHost()).thenReturn(HOST);
        when(mockConfig.getPort()).thenReturn(PORT);
        when(mockConfig.getDatabase()).thenReturn(DATABASE);
        when(mockConfig.getDbUser()).thenReturn(DB_USER);
        when(mockConfig.getDbPassword()).thenReturn(DB_PASSWORD);

        when(mockDbType.protocol()).thenReturn(MOCK_PROTOCOL);
        when(mockDbType.driver()).thenReturn(mockDriver);

        when(mockDriver.acceptsURL(MOCK_PROTOCOL + "://" + HOST + ":" + PORT + "/" + DATABASE)).thenReturn(true);
        when(mockDriver.connect(eq(MOCK_PROTOCOL + "://" + HOST + ":" + PORT + "/" + DATABASE), any())).thenReturn(mockConnection);

        DriverManager.registerDriver(mockDriver);
    }

    @AfterEach
    void teardown() throws SQLException {
        DriverManager.deregisterDriver(mockDriver);
    }

    @Test
    void testGetConnection_ShouldReturnSameConnectionForSameURL() throws SQLException {
        Connection firstConnection = dbConnectorService.getConnection(mockConfig);
        Connection secondConnection = dbConnectorService.getConnection(mockConfig);

        assertNotNull(firstConnection);
        assertSame(firstConnection, secondConnection);
        verify(mockConnection, never()).close();
    }

    @Test
    void testRegisterDriverIfNecessary_ShouldNotRegisterDriverIfAlreadyRegistered() throws SQLException {
        dbConnectorService.getConnection(mockConfig);
        dbConnectorService.getConnection(mockConfig);

        verify(mockDbType, times(1)).driver();
    }

    @Test
    void testCloseConnections_ShouldHandleEmptyConnectionsGracefully() throws SQLException {
        dbConnectorService.closeConnections();

        verify(mockConnection, never()).close();
    }

    @Test
    void testCloseConnections_ShouldHandleAlreadyClosedConnections() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);

        dbConnectorService.getConnection(mockConfig);
        dbConnectorService.closeConnections();

        verify(mockConnection, never()).close();
    }

    @Test
    void testCreateConnection_ShouldThrowExceptionForInvalidUrl() {
        when(mockConfig.getDatabase()).thenReturn("invalid-db");
        when(mockDbType.protocol()).thenReturn("jdbc:invalid");

        assertThrows(IllegalStateException.class, () -> dbConnectorService.getConnection(mockConfig));
    }

    @Test
    void testRegisterDriverIfNecessary_ShouldThrowExceptionOnDriverRegistrationFailure() throws SQLException {
        DbType faultyDbType = mock(DbType.class);
        Driver mockDriver = mock(Driver.class);
        when(faultyDbType.driver()).thenReturn(mockDriver);
        doThrow(new SQLException("Driver registration failed")).when(mockDriver).acceptsURL(anyString());

        assertThrows(IllegalStateException.class, () -> {
            dbConnectorService.getConnection(
                    DatabaseConfiguration.builder()
                            .dbType(faultyDbType)
                            .host(HOST)
                            .port(PORT)
                            .database(DATABASE)
                            .dbUser(DB_USER)
                            .dbPassword(DB_PASSWORD)
                            .build()
            );
        });

        verify(faultyDbType, times(1)).driver();
    }

    @Test
    void testRegisterDriverIfNecessary_ShouldNotRegisterAlreadyRegisteredDriver() {
        dbConnectorService.getConnection(mockConfig);
        dbConnectorService.getConnection(mockConfig);

        verify(mockDbType, times(1)).driver();
    }

    @Test
    void testRegisterDriverIfNecessary_ShouldThrowExceptionOnDriverRegistrationFailure_StaticMock() {
        DbType failingDbType = mock(DbType.class);
        Driver failingDriver = mock(Driver.class);
        when(failingDbType.protocol()).thenReturn(FAILING_PROTOCOL);
        when(failingDbType.driver()).thenReturn(failingDriver);

        DatabaseConfiguration failingConfig = DatabaseConfiguration.builder()
                .dbType(failingDbType)
                .host(HOST)
                .port(PORT)
                .database(FAILING_DATABASE)
                .dbUser(DB_USER)
                .dbPassword(DB_PASSWORD)
                .build();

        Answer<Object> defaultAnswer = new Answer<>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String methodName = invocation.getMethod().getName();
                if ("getLogWriter".equals(methodName)) {
                    return null;
                }
                if ("registerDriver".equals(methodName)) {
                    Object arg0 = invocation.getArgument(0);
                    if (failingDriver.equals(arg0)) {
                        throw new SQLException("Simulated registration failure");
                    }
                }
                return null;
            }
        };

        try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class, defaultAnswer)) {
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> dbConnectorService.getConnection(failingConfig));
            assertTrue(exception.getMessage().contains("Failed to register database driver for type:"));
        }
    }

    @Test
    void testCloseConnections_ShouldHandleSQLExceptionOnClose() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        doThrow(new SQLException("Simulated close failure")).when(mockConnection).close();

        dbConnectorService.getConnection(mockConfig);

        assertDoesNotThrow(() -> dbConnectorService.closeConnections());
    }
}