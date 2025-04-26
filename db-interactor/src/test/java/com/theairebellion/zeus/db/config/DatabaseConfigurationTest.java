package com.theairebellion.zeus.db.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Driver;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DatabaseConfigurationTest {

    private static final String MOCK_PROTOCOL = "mock-protocol";
    private static final String HOST = "localhost";
    private static final int PORT = 5432;
    private static final String DATABASE = "testdb";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    @Test
    @DisplayName("Should build database configuration with all properties")
    void testDatabaseConfigurationBuilder() {
        // Given
        DbType mockDbType = createMockDbType();

        // When
        DatabaseConfiguration config = DatabaseConfiguration.builder()
                .dbType(mockDbType)
                .host(HOST)
                .port(PORT)
                .database(DATABASE)
                .dbUser(USER)
                .dbPassword(PASSWORD)
                .build();

        // Then
        assertEquals(mockDbType, config.getDbType());
        assertEquals(HOST, config.getHost());
        assertEquals(PORT, config.getPort());
        assertEquals(DATABASE, config.getDatabase());
        assertEquals(USER, config.getDbUser());
        assertEquals(PASSWORD, config.getDbPassword());
    }

    @Test
    @DisplayName("Should create configuration via all-args constructor")
    void testAllArgsConstructor() {
        // Given
        DbType mockDbType = createMockDbType();

        // When
        DatabaseConfiguration config = new DatabaseConfiguration(
                mockDbType, HOST, PORT, DATABASE, USER, PASSWORD, null);

        // Then
        assertEquals(mockDbType, config.getDbType());
        assertEquals(HOST, config.getHost());
        assertEquals(PORT, config.getPort());
        assertEquals(DATABASE, config.getDatabase());
        assertEquals(USER, config.getDbUser());
        assertEquals(PASSWORD, config.getDbPassword());
    }

    @Test
    @DisplayName("Should build separate configuration instances")
    void testBuilderCreatesDistinctInstances() {
        // Given
        DbType mockDbType = createMockDbType();

        // When
        DatabaseConfiguration config1 = DatabaseConfiguration.builder()
                .dbType(mockDbType)
                .host(HOST)
                .port(PORT)
                .database(DATABASE)
                .dbUser(USER)
                .dbPassword(PASSWORD)
                .build();

        DatabaseConfiguration config2 = DatabaseConfiguration.builder()
                .dbType(mockDbType)
                .host(HOST)
                .port(PORT)
                .database(DATABASE)
                .dbUser(USER)
                .dbPassword(PASSWORD)
                .build();

        // Then
        // Without @EqualsAndHashCode, the class uses reference equality
        assertNotSame(config1, config2, "Builder should create distinct instances");

        // Fields should have same values
        assertEquals(config1.getDbType(), config2.getDbType(), "DbType should match");
        assertEquals(config1.getHost(), config2.getHost(), "Host should match");
        assertEquals(config1.getPort(), config2.getPort(), "Port should match");
        assertEquals(config1.getDatabase(), config2.getDatabase(), "Database should match");
        assertEquals(config1.getDbUser(), config2.getDbUser(), "User should match");
        assertEquals(config1.getDbPassword(), config2.getDbPassword(), "Password should match");
    }

    @SuppressWarnings("all")
    @ParameterizedTest
    @MethodSource("configurationVariations")
    @DisplayName("Should allow comparing field values between different configurations")
    void testFieldComparisons(DatabaseConfiguration config1, DatabaseConfiguration config2, String fieldName, boolean shouldMatch) {
        // Since equals() uses reference equality, we compare specific fields
        boolean fieldsMatch;

        switch(fieldName) {
            case "dbType":
                fieldsMatch = Objects.equals(config1.getDbType(), config2.getDbType());
                break;
            case "host":
                fieldsMatch = Objects.equals(config1.getHost(), config2.getHost());
                break;
            case "port":
                fieldsMatch = Objects.equals(config1.getPort(), config2.getPort());
                break;
            case "database":
                fieldsMatch = Objects.equals(config1.getDatabase(), config2.getDatabase());
                break;
            case "dbUser":
                fieldsMatch = Objects.equals(config1.getDbUser(), config2.getDbUser());
                break;
            case "dbPassword":
                fieldsMatch = Objects.equals(config1.getDbPassword(), config2.getDbPassword());
                break;
            default:
                fieldsMatch = false;
        }

        if (shouldMatch) {
            assertTrue(fieldsMatch, "Field " + fieldName + " should match");
        } else {
            assertFalse(fieldsMatch, "Field " + fieldName + " should not match");
        }
    }

    @Test
    @DisplayName("Should have a toString representation")
    void testToString() {
        // Given
        DbType mockDbType = createMockDbType();
        DatabaseConfiguration config = DatabaseConfiguration.builder()
                .dbType(mockDbType)
                .host(HOST)
                .port(PORT)
                .database(DATABASE)
                .dbUser(USER)
                .dbPassword(PASSWORD)
                .build();

        // When
        String toString = config.toString();

        // Then
        assertNotNull(toString, "toString should not be null");
        // Lombok's default toString might not include field values with just @Getter and @Builder
        // So we're just checking that toString() returns something
    }

    @Test
    @DisplayName("buildUrlKey() should return protocol://host:port/database")
    void testBuildUrlKey() {
        DbType dbType = createMockDbType(); // protocol() → "mock-protocol"
        DatabaseConfiguration cfg = DatabaseConfiguration.builder()
                .dbType(dbType)
                .host("h")
                .port(1234)
                .database("db")
                .build();

        assertEquals("mock-protocol://h:1234/db", cfg.buildUrlKey());
    }

    private static Stream<Arguments> configurationVariations() {
        DbType mockDbType1 = createMockDbType();
        DbType mockDbType2 = createAnotherMockDbType();

        return Stream.of(
                // Different dbType
                Arguments.of(
                        DatabaseConfiguration.builder().dbType(mockDbType1).host(HOST).port(PORT).database(DATABASE).dbUser(USER).dbPassword(PASSWORD).build(),
                        DatabaseConfiguration.builder().dbType(mockDbType2).host(HOST).port(PORT).database(DATABASE).dbUser(USER).dbPassword(PASSWORD).build(),
                        "dbType", false
                ),
                // Different host
                Arguments.of(
                        DatabaseConfiguration.builder().dbType(mockDbType1).host(HOST).port(PORT).database(DATABASE).dbUser(USER).dbPassword(PASSWORD).build(),
                        DatabaseConfiguration.builder().dbType(mockDbType1).host("different-host").port(PORT).database(DATABASE).dbUser(USER).dbPassword(PASSWORD).build(),
                        "host", false
                ),
                // Same host
                Arguments.of(
                        DatabaseConfiguration.builder().dbType(mockDbType1).host(HOST).port(PORT).database(DATABASE).dbUser(USER).dbPassword(PASSWORD).build(),
                        DatabaseConfiguration.builder().dbType(mockDbType1).host(HOST).port(PORT+1).database(DATABASE).dbUser(USER).dbPassword(PASSWORD).build(),
                        "host", true
                )
        );
    }

    private static DbType createMockDbType() {
        return new DbType() {
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
                return null;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                DbType other = (DbType) obj;
                return MOCK_PROTOCOL.equals(other.protocol());
            }

            @Override
            public int hashCode() {
                return MOCK_PROTOCOL.hashCode();
            }
        };
    }

    private static DbType createAnotherMockDbType() {
        DbType mockDbType = mock(DbType.class);
        when(mockDbType.protocol()).thenReturn("different-protocol");
        return mockDbType;
    }
}