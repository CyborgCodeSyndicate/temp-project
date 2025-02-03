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

public class DbQueryTest {

    @Test
    void testQuery() {
        DummyDbQuery dummy = new DummyDbQuery();
        // Verify that the abstract query() method returns the expected string.
        assertEquals("SELECT * FROM dummy", dummy.query());
    }

    @Test
    void testEnumImpl() {
        DummyDbQuery dummy = new DummyDbQuery();
        // Verify that the abstract enumImpl() returns the expected enum value.
        assertEquals(TestEnum.VALUE, dummy.enumImpl());
    }

    @Test
    void testConfig() {
        DbType mockDbType = mock(DbType.class);

        // Create a mock DbConfig and stub its methods.
        DbConfig mockDbConfig = mock(DbConfig.class);
        when(mockDbConfig.type()).thenReturn(mockDbType);
        when(mockDbConfig.host()).thenReturn("mockHost");
        when(mockDbConfig.port()).thenReturn(1234);
        when(mockDbConfig.name()).thenReturn("mockDatabase");
        when(mockDbConfig.username()).thenReturn("mockUser");
        when(mockDbConfig.password()).thenReturn("mockPassword");

        // Use static mocking for DbConfigHolder to control its getDbConfig() return value.
        try (MockedStatic<DbConfigHolder> mockedHolder = mockStatic(DbConfigHolder.class)) {
            mockedHolder.when(DbConfigHolder::getDbConfig).thenReturn(mockDbConfig);

            DummyDbQuery dummy = new DummyDbQuery();
            DatabaseConfiguration config = dummy.config();

            // Assert that the configuration built from the mock DbConfig has the expected values.
            assertEquals(mockDbType, config.getDbType());
            assertEquals("mockHost", config.getHost());
            assertEquals(1234, config.getPort());
            assertEquals("mockDatabase", config.getDatabase());
            assertEquals("mockUser", config.getDbUser());
            assertEquals("mockPassword", config.getDbPassword());
        }
    }

    @Test
    void testWithParam() {
        DummyDbQuery dummy = new DummyDbQuery();
        // Call withParam; it should return a new instance wrapping the original query.
        DbQuery paramQuery = dummy.withParam("key", "value");

        // Check that the returned value is not null and is not the same instance as dummy.
        assertNotNull(paramQuery);
        assertNotSame(dummy, paramQuery);

        // Optionally, verify that it is an instance of ParametrizedQuery.
        // This assumes that ParametrizedQuery is a public class implementing DbQuery.
        assertInstanceOf(ParametrizedQuery.class, paramQuery);
    }
}