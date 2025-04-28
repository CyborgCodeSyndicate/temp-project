package com.theairebellion.zeus.db.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Driver;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DbTypeTest {

    private static final String MOCK_PROTOCOL = "mock-protocol";
    private static final String MOCK_DB_URL = MOCK_PROTOCOL + "://localhost:5432/testdb";

    @Test
    @DisplayName("Should return protocol from DbType implementation")
    void testDbTypeProtocol() {
        // Given
        DbType<?> mockDbType = createMockDbType();

        // When
        String protocol = mockDbType.protocol();

        // Then
        assertEquals(MOCK_PROTOCOL, protocol, "Should return the configured protocol");
    }

    @Test
    @DisplayName("Should return driver from DbType implementation")
    void testDbTypeDriver() {
        // Given
        Driver mockDriver = mock(Driver.class);
        DbType<?> mockDbType = createMockDbType(mockDriver);

        // When
        Driver driver = mockDbType.driver();

        // Then
        assertSame(mockDriver, driver, "Should return the configured driver");
    }

    @Test
    @DisplayName("Should return enum implementation from DbType")
    void testDbTypeEnumImpl() {
        // Given
        enum TestEnum { POSTGRES, MYSQL, ORACLE }
        TestEnum enumValue = TestEnum.POSTGRES;

        DbType<?> mockDbType = new DbType() {
            @Override
            public Driver driver() {
                return null;
            }

            @Override
            public String protocol() {
                return MOCK_PROTOCOL;
            }

            @Override
            public Enum<?> enumImpl() {
                return enumValue;
            }
        };

        // When
        Enum<?> result = mockDbType.enumImpl();

        // Then
        assertSame(enumValue, result, "Should return the configured enum value");
    }

    @Test
    @DisplayName("Driver from DbType should be usable with JDBC")
    void testDbTypeDriverWithJdbc() throws SQLException {
        // Given
        Driver mockDriver = mock(Driver.class);
        when(mockDriver.acceptsURL(MOCK_DB_URL)).thenReturn(true);

        DbType<?> mockDbType = createMockDbType(mockDriver);

        // When
        Driver driver = mockDbType.driver();
        boolean acceptsUrl = driver.acceptsURL(MOCK_DB_URL);

        // Then
        assertTrue(acceptsUrl, "Driver should accept URLs with the protocol");
        verify(mockDriver).acceptsURL(MOCK_DB_URL);
    }

    @Test
    @DisplayName("Protocol should be used for building database URLs")
    void testProtocolForUrlBuilding() {
        // Given
        DbType<?> mockDbType = createMockDbType();
        String host = "localhost";
        int port = 5432;
        String database = "testdb";

        // When
        String url = String.format("%s://%s:%d/%s",
                mockDbType.protocol(), host, port, database);

        // Then
        assertEquals(MOCK_DB_URL, url, "URL should be correctly formatted with protocol");
    }

    private DbType<?> createMockDbType() {
        return createMockDbType(null);
    }

    private DbType<?> createMockDbType(Driver driver) {
        return new DbType() {
            @Override
            public Driver driver() {
                return driver;
            }

            @Override
            public String protocol() {
                return MOCK_PROTOCOL;
            }

            @Override
            public Enum<?> enumImpl() {
                return null;
            }
        };
    }
}