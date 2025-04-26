package com.theairebellion.zeus.db.query;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.query.mock.TestEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParametrizedQueryTest {

    private static final String QUERY_SELECT_USERS = "SELECT * FROM users";
    private static final String QUERY_SELECT_USERS_WITH_ID = "SELECT * FROM users WHERE id = {id}";
    private static final String QUERY_SELECT_USERS_WITH_ID_AND_NAME = "SELECT * FROM users WHERE id = {id} AND name = {name}";
    private static final String QUERY_UPDATE_USERS = "UPDATE users SET name = {name}, age = {age} WHERE id = {id}";
    private static final String EXPECTED_QUERY_WITH_ID_AND_NAME = "SELECT * FROM users WHERE id = 1 AND name = John";
    private static final String EXPECTED_QUERY_WITH_NULL_ID = "SELECT * FROM users WHERE id = null AND name = John";
    private static final String EXPECTED_QUERY_WITH_UPDATED_ID = "SELECT * FROM users WHERE id = 2";
    private static final String EXPECTED_QUERY_UPDATE_USERS = "UPDATE users SET name = John, age = 30 WHERE id = 1";
    private static final String PARAM_ID = "id";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_AGE = "age";
    private static final String VALUE_NAME = "John";
    private static final Integer VALUE_ID_1 = 1;
    private static final Integer VALUE_ID_2 = 2;
    private static final Integer VALUE_AGE = 30;

    @Mock
    private DatabaseConfiguration mockDbConfig;

    @Test
    @DisplayName("Should replace placeholders with actual parameter values")
    void testParametrizedQuery_ReplacesPlaceholders() {
        // Arrange
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS_WITH_ID_AND_NAME);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        // Act
        DbQuery parametrizedQuery = query.withParam(PARAM_ID, VALUE_ID_1).withParam(PARAM_NAME, VALUE_NAME);

        // Assert
        assertEquals(EXPECTED_QUERY_WITH_ID_AND_NAME, parametrizedQuery.query(),
                "Placeholder replacement should produce expected query string");
    }

    @Test
    @DisplayName("Should return original query when no parameters are provided")
    void testParametrizedQuery_NoParameters() {
        // Arrange
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        // Act & Assert
        assertEquals(QUERY_SELECT_USERS, query.query(),
                "Query with no parameters should match original query string");
    }

    @Test
    @DisplayName("Should replace placeholder with 'null' string when parameter value is null")
    void testParametrizedQuery_NullParameterValue() {
        // Arrange
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS_WITH_ID);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        // Act
        DbQuery parametrizedQuery = query.withParam(PARAM_ID, null);

        // Assert
        assertEquals("SELECT * FROM users WHERE id = null", parametrizedQuery.query(),
                "Null parameter value should be replaced with string 'null'");
    }

    @Test
    @DisplayName("Should overwrite parameter value when same parameter is set twice")
    void testParametrizedQuery_OverwriteParameter() {
        // Arrange
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS_WITH_ID);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        // Act
        DbQuery parametrizedQuery = query.withParam(PARAM_ID, VALUE_ID_1).withParam(PARAM_ID, VALUE_ID_2);

        // Assert
        assertEquals(EXPECTED_QUERY_WITH_UPDATED_ID, parametrizedQuery.query(),
                "Parameter value should be overwritten when same parameter is set twice");
    }

    @Test
    @DisplayName("Should replace multiple placeholders with their corresponding values")
    void testParametrizedQuery_MultipleParameters() {
        // Arrange
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS_WITH_ID_AND_NAME);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        // Act
        DbQuery parametrizedQuery = query.withParam(PARAM_ID, VALUE_ID_1).withParam(PARAM_NAME, VALUE_NAME);

        // Assert
        assertEquals(EXPECTED_QUERY_WITH_ID_AND_NAME, parametrizedQuery.query(),
                "Multiple placeholders should be replaced with corresponding values");
    }

    @Test
    @DisplayName("Should replace placeholders with null string when parameter values are null")
    void testParametrizedQuery_ReplacesNullPlaceholders() {
        // Arrange
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS_WITH_ID_AND_NAME);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        // Act
        DbQuery parametrizedQuery = query.withParam(PARAM_ID, null).withParam(PARAM_NAME, VALUE_NAME);

        // Assert
        assertEquals(EXPECTED_QUERY_WITH_NULL_ID, parametrizedQuery.query(),
                "Null parameter values should be replaced with string 'null'");
    }

    @Test
    @DisplayName("Should return original query when it contains no placeholders")
    void testParametrizedQuery_NoPlaceholders() {
        // Arrange
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        // Act & Assert
        assertEquals(QUERY_SELECT_USERS, query.query(),
                "Query with no placeholders should remain unchanged");
    }

    @Test
    @DisplayName("Should replace multiple placeholders in complex query")
    void testParametrizedQuery_MultipleReplacements() {
        // Arrange
        DbQuery originalQuery = createQuery(QUERY_UPDATE_USERS);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        // Act
        DbQuery parametrizedQuery = query
                .withParam(PARAM_NAME, VALUE_NAME)
                .withParam(PARAM_AGE, VALUE_AGE)
                .withParam(PARAM_ID, VALUE_ID_1);

        // Assert
        assertEquals(EXPECTED_QUERY_UPDATE_USERS, parametrizedQuery.query(),
                "Multiple placeholders in complex query should be replaced correctly");
    }

    @Test
    @DisplayName("Should delegate config() to original query")
    void testConfig_DelegatesToOriginalQuery() {
        // Arrange
        DbQuery originalQuery = mock(DbQuery.class);
        when(originalQuery.config()).thenReturn(mockDbConfig);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        // Act & Assert
        assertSame(mockDbConfig, query.config(),
                "config() should delegate to original query");
    }

    @Test
    @DisplayName("Should delegate enumImpl() to original query")
    void testEnumImpl_DelegatesToOriginalQuery() {
        // Arrange
        TestEnum testEnum = TestEnum.VALUE;
        DbQuery originalQuery = mock(DbQuery.class);
        doReturn(testEnum).when(originalQuery).enumImpl();
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        // Act & Assert
        assertEquals(testEnum, query.enumImpl(),
                "enumImpl() should delegate to original query");
    }

    @Test
    @DisplayName("Should create new instance when withParam is called")
    void testWithParam_CreatesNewInstance() {
        // Arrange
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        // Act
        DbQuery result = query.withParam(PARAM_NAME, VALUE_NAME);

        // Assert
        assertNotSame(query, result,
                "withParam() should create a new instance");
    }

    @Test
    @DisplayName("Should leave placeholder unreplaced if parameter not provided")
    void testUnprovidedPlaceholderRemains() {
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS_WITH_ID_AND_NAME);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        DbQuery parametrizedQuery = query.withParam(PARAM_ID, VALUE_ID_1);

        assertEquals("SELECT * FROM users WHERE id = 1 AND name = {name}", parametrizedQuery.query(),
                "Unprovided placeholder should remain unchanged");
    }

    private DbQuery createQuery(String query) {
        return new DbQuery() {
            @Override
            public String query() {
                return query;
            }

            @Override
            public DatabaseConfiguration config() {
                return null;
            }

            @Override
            public Enum<?> enumImpl() {
                return null;
            }
        };
    }
}