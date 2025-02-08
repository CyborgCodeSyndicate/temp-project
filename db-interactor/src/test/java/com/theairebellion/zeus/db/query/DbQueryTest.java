package com.theairebellion.zeus.db.query;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbConfig;
import com.theairebellion.zeus.db.config.DbConfigHolder;
import com.theairebellion.zeus.db.config.DbType;
import com.theairebellion.zeus.db.query.mock.DummyDbQuery;
import com.theairebellion.zeus.db.query.mock.TestEnum;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

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

    @Test
    void testQuery() {
        DummyDbQuery dummy = new DummyDbQuery();
        assertEquals(QUERY_RESULT, dummy.query());
    }

    @Test
    void testEnumImpl() {
        DummyDbQuery dummy = new DummyDbQuery();
        assertEquals(EXPECTED_ENUM, dummy.enumImpl());
    }

    @Test
    void testConfig() {
        DbType mockDbType = mock(DbType.class);
        DbConfig mockDbConfig = mock(DbConfig.class);

        when(mockDbConfig.type()).thenReturn(mockDbType);
        when(mockDbConfig.host()).thenReturn(MOCK_HOST);
        when(mockDbConfig.port()).thenReturn(MOCK_PORT);
        when(mockDbConfig.name()).thenReturn(MOCK_DATABASE);
        when(mockDbConfig.username()).thenReturn(MOCK_USER);
        when(mockDbConfig.password()).thenReturn(MOCK_PASSWORD);

        try (MockedStatic<DbConfigHolder> mockedHolder = mockStatic(DbConfigHolder.class)) {
            mockedHolder.when(DbConfigHolder::getDbConfig).thenReturn(mockDbConfig);

            DummyDbQuery dummy = new DummyDbQuery();
            DatabaseConfiguration config = dummy.config();

            assertEquals(mockDbType, config.getDbType());
            assertEquals(MOCK_HOST, config.getHost());
            assertEquals(MOCK_PORT, config.getPort());
            assertEquals(MOCK_DATABASE, config.getDatabase());
            assertEquals(MOCK_USER, config.getDbUser());
            assertEquals(MOCK_PASSWORD, config.getDbPassword());
        }
    }

    @Test
    void testWithParam() {
        DummyDbQuery dummy = new DummyDbQuery();
        DbQuery paramQuery = dummy.withParam(PARAM_KEY, PARAM_VALUE);

        assertNotNull(paramQuery);
        assertNotSame(dummy, paramQuery);
        assertInstanceOf(ParametrizedQuery.class, paramQuery);
    }
}