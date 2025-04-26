package com.theairebellion.zeus.db.connector;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbType;
import com.theairebellion.zeus.db.log.LogDb;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
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

    private DatabaseConfiguration databaseConfiguration;

    @Mock
    private DbType mockDbType;

    @Mock
    private Connection mockConnection;

    @Mock
    private Driver mockDriver;

    @BeforeEach
    void setup() {
        databaseConfiguration = DatabaseConfiguration.builder()
                .dbType(mockDbType)
                .host(HOST)
                .port(PORT)
                .database(DATABASE)
                .dbUser(DB_USER)
                .dbPassword(DB_PASSWORD)
                .build();
        lenient().when(mockDbType.protocol()).thenReturn(MOCK_PROTOCOL);
        lenient().when(mockDbType.driver()).thenReturn(mockDriver);
    }

    @AfterEach
    void afterAll() {
        dbConnectorService.closeConnections();
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
                Connection firstConnection = dbConnectorService.getConnection(databaseConfiguration);
                Connection secondConnection = dbConnectorService.getConnection(databaseConfiguration);

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
            String otherDatabase = "otherdb";
            Connection otherConnection = mock(Connection.class);
            DatabaseConfiguration otherConfig = DatabaseConfiguration.builder()
                    .dbType(mockDbType)
                    .host(HOST)
                    .port(PORT)
                    .database(otherDatabase)
                    .dbUser(DB_USER)
                    .dbPassword(DB_PASSWORD)
                    .build();

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
                Connection firstConnection = dbConnectorService.getConnection(databaseConfiguration);
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
                dbConnectorService.getConnection(databaseConfiguration);
                dbConnectorService.getConnection(databaseConfiguration);

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
                        () -> dbConnectorService.getConnection(databaseConfiguration)
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
                dbConnectorService.getConnection(databaseConfiguration);

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
                dbConnectorService.getConnection(databaseConfiguration);

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
                dbConnectorService.getConnection(databaseConfiguration);

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
                dbConnectorService.getConnection(databaseConfiguration);

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
                        () -> dbConnectorService.getConnection(databaseConfiguration)
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
                        "buildConnectionUrl", DatabaseConfiguration.class);
                method.setAccessible(true);

                // When
                String actualUrl = (String) method.invoke(dbConnectorService, databaseConfiguration);

                // Then
                assertEquals(CONNECTION_URL, actualUrl, "URL should be formatted correctly");
            } catch (Exception e) {
                fail("Failed to invoke buildConnectionUrl method: " + e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Private Method Coverage")
    class PrivateMethodCoverage {
        @Spy
        BaseDbConnectorService svc;
        @Mock
        DbType mockType;

        @Test
        @DisplayName("buildConnectionUrl uses fullConnectionString when non-null")
        void buildConnectionUrl_prefersFullConnectionString() throws Exception {
            var dbConfig = spy(DatabaseConfiguration.builder()
                    .dbType(mockType)
                    .host("x").port(1).database("y")
                    .build());
            doReturn("jdbc:foo/bar").when(dbConfig).getFullConnectionString();

            Method m = BaseDbConnectorService.class
                    .getDeclaredMethod("buildConnectionUrl", DatabaseConfiguration.class);
            m.setAccessible(true);

            try (MockedStatic<LogDb> logs = mockStatic(LogDb.class)) {
                String url = (String) m.invoke(svc, dbConfig);
                assertEquals("jdbc:foo/bar", url);
                logs.verify(() -> LogDb.debug("Built connection URL: {}", "jdbc:foo/bar"), times(1));
            }
        }

        @Test
        @DisplayName("registerDriverIfNecessary skips registration on second call")
        void innerIfFalsePath() throws Exception {
            // 1) Arrange: have our DbType return a fake Driver
            java.sql.Driver fakeDriver = mock(java.sql.Driver.class);
            when(mockType.driver()).thenReturn(fakeDriver);

            // 2) Reflectively grab the private registerDriverIfNecessary(DbType) method
            Method register = BaseDbConnectorService.class
                    .getDeclaredMethod("registerDriverIfNecessary", DbType.class);
            register.setAccessible(true);

            try (
                    MockedStatic<DriverManager> dm = mockStatic(DriverManager.class);
                    MockedStatic<LogDb> logs = mockStatic(LogDb.class)
            ) {
                // 3) Stub out DriverManager.registerDriver so it doesn't actually register
                dm.when(() -> DriverManager.registerDriver(fakeDriver)).then(inv -> null);

                // --- First invocation: should register & log once ---
                register.invoke(dbConnectorService, mockType);
                logs.verify(() ->
                                LogDb.info("Registered database driver for type: {}", mockType),
                        times(1)
                );

                // --- Second invocation: because it's now “already registered,” it should do nothing ---
                register.invoke(dbConnectorService, mockType);

                // verify that registerDriver was still only called once
                dm.verify(() -> DriverManager.registerDriver(fakeDriver), times(1));

                // and no further logs
                logs.verifyNoMoreInteractions();
            }
        }

        @Test
        @DisplayName("registerDriverIfNecessary logs info on first registration")
        void registerDriver_logsOnFirstCall() throws Exception {
            // clear out any previously registered types
            svc.closeConnections();

            // stub mockType.driver() to return a fake Driver
            Driver fakeDriver = mock(Driver.class);
            when(mockType.driver()).thenReturn(fakeDriver);

            try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class);
                 MockedStatic<LogDb> logs = mockStatic(LogDb.class)) {

                // stub the void registerDriver call
                dm.when(() -> DriverManager.registerDriver(fakeDriver))
                        .then(invocation -> null);

                // reflectively grab the private method
                Method reg = BaseDbConnectorService.class
                        .getDeclaredMethod("registerDriverIfNecessary", DbType.class);
                reg.setAccessible(true);

                // first invocation: should register and log once
                reg.invoke(svc, mockType);
                logs.verify(() -> LogDb.info("Registered database driver for type: {}", mockType), times(1));

                // second invocation: already registered, so no more logs
                reg.invoke(svc, mockType);
                logs.verifyNoMoreInteractions();
            }
        }
    }
}