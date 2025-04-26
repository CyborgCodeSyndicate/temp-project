package com.theairebellion.zeus.db.query;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbConfig;
import com.theairebellion.zeus.db.config.DbConfigHolder;
import com.theairebellion.zeus.db.config.DbType;
import com.theairebellion.zeus.db.query.mock.DummyDbQuery;
import com.theairebellion.zeus.db.query.mock.TestEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DbQueryTest {

    private static final String MOCK_HOST = "mockHost";
    private static final String MOCK_DATABASE = "mockDatabase";
    private static final String MOCK_USER = "mockUser";
    private static final String MOCK_PASSWORD = "mockPassword";
    private static final int MOCK_PORT = 1234;
    private static final String QUERY_RESULT = "SELECT * FROM dummy";
    private static final TestEnum EXPECTED_ENUM = TestEnum.VALUE;
    private static final String PARAM_KEY = "key";
    private static final String PARAM_VALUE = "value";
    private static final String MOCK_CONNECTION_STRING = "jdbc:override";

    @Mock
    private DbType mockDbType;

    @Mock
    private DbConfig mockDbConfig;

    @Test
    @DisplayName("Should return the query string")
    void testQuery() {
        DummyDbQuery dummy = new DummyDbQuery();
        assertEquals(QUERY_RESULT, dummy.query(), "Query string should match expected value");
    }

    @Test
    @DisplayName("Should return the enum implementation")
    void testEnumImpl() {
        DummyDbQuery dummy = new DummyDbQuery();
        assertEquals(EXPECTED_ENUM, dummy.enumImpl(), "Enum implementation should match expected value");
    }

    @Test
    @DisplayName("Should build and return database configuration from DbConfig")
    void testConfig() {
        // Arrange
        when(mockDbConfig.type()).thenReturn(mockDbType);
        when(mockDbConfig.host()).thenReturn(MOCK_HOST);
        when(mockDbConfig.port()).thenReturn(MOCK_PORT);
        when(mockDbConfig.name()).thenReturn(MOCK_DATABASE);
        when(mockDbConfig.username()).thenReturn(MOCK_USER);
        when(mockDbConfig.password()).thenReturn(MOCK_PASSWORD);
        when(mockDbConfig.fullConnectionString()).thenReturn(MOCK_CONNECTION_STRING);

        try (MockedStatic<DbConfigHolder> mockedHolder = mockStatic(DbConfigHolder.class)) {
            mockedHolder.when(DbConfigHolder::getDbConfig).thenReturn(mockDbConfig);

            // Act
            DummyDbQuery dummy = new DummyDbQuery();
            DatabaseConfiguration config = dummy.config();

            // Assert
            assertAll(
                    "Database configuration properties should match expected values",
                    () -> assertEquals(mockDbType, config.getDbType(), "DbType should match"),
                    () -> assertEquals(MOCK_HOST, config.getHost(), "Host should match"),
                    () -> assertEquals(MOCK_PORT, config.getPort(), "Port should match"),
                    () -> assertEquals(MOCK_DATABASE, config.getDatabase(), "Database name should match"),
                    () -> assertEquals(MOCK_USER, config.getDbUser(), "Username should match"),
                    () -> assertEquals(MOCK_PASSWORD, config.getDbPassword(), "Password should match"),
                    () -> assertEquals(MOCK_CONNECTION_STRING, config.getFullConnectionString())
            );
        }
    }

    @Test
    @DisplayName("Should create a ParametrizedQuery when withParam is called")
    void testWithParam() {
        // Arrange
        DummyDbQuery dummy = new DummyDbQuery();

        // Act
        DbQuery paramQuery = dummy.withParam(PARAM_KEY, PARAM_VALUE);

        // Assert
        assertAll(
                "withParam should return a properly initialized ParametrizedQuery",
                () -> assertNotNull(paramQuery, "Result should not be null"),
                () -> assertNotSame(dummy, paramQuery, "Result should be a different instance"),
                () -> assertInstanceOf(ParametrizedQuery.class, paramQuery, "Result should be a ParametrizedQuery")
        );
    }
}