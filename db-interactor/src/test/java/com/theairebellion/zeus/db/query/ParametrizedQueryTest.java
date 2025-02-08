package com.theairebellion.zeus.db.query;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void testParametrizedQuery_ReplacesPlaceholders() {
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS_WITH_ID_AND_NAME);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);
        DbQuery parametrizedQuery = query.withParam(PARAM_ID, VALUE_ID_1).withParam(PARAM_NAME, VALUE_NAME);
        assertEquals(EXPECTED_QUERY_WITH_ID_AND_NAME, parametrizedQuery.query());
    }

    @Test
    void testParametrizedQuery_NoParameters() {
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);
        assertEquals(QUERY_SELECT_USERS, query.query());
    }

    @Test
    void testParametrizedQuery_NullParameterValue() {
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS_WITH_ID);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);
        DbQuery parametrizedQuery = query.withParam(PARAM_ID, null);
        assertEquals("SELECT * FROM users WHERE id = null", parametrizedQuery.query());
    }

    @Test
    void testParametrizedQuery_OverwriteParameter() {
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS_WITH_ID);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);
        DbQuery parametrizedQuery = query.withParam(PARAM_ID, VALUE_ID_1).withParam(PARAM_ID, VALUE_ID_2);
        assertEquals(EXPECTED_QUERY_WITH_UPDATED_ID, parametrizedQuery.query());
    }

    @Test
    void testParametrizedQuery_MultipleParameters() {
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS_WITH_ID_AND_NAME);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);
        DbQuery parametrizedQuery = query.withParam(PARAM_ID, VALUE_ID_1).withParam(PARAM_NAME, VALUE_NAME);
        assertEquals(EXPECTED_QUERY_WITH_ID_AND_NAME, parametrizedQuery.query());
    }

    @Test
    void testParametrizedQuery_ReplacesNullPlaceholders() {
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS_WITH_ID_AND_NAME);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);
        DbQuery parametrizedQuery = query.withParam(PARAM_ID, null).withParam(PARAM_NAME, VALUE_NAME);
        assertEquals(EXPECTED_QUERY_WITH_NULL_ID, parametrizedQuery.query());
    }

    @Test
    void testParametrizedQuery_NoPlaceholders() {
        DbQuery originalQuery = createQuery(QUERY_SELECT_USERS);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);
        assertEquals(QUERY_SELECT_USERS, query.query());
    }

    @Test
    void testParametrizedQuery_MultipleReplacements() {
        DbQuery originalQuery = createQuery(QUERY_UPDATE_USERS);
        ParametrizedQuery query = new ParametrizedQuery(originalQuery);
        DbQuery parametrizedQuery = query
                .withParam(PARAM_NAME, VALUE_NAME)
                .withParam(PARAM_AGE, VALUE_AGE)
                .withParam(PARAM_ID, VALUE_ID_1);
        assertEquals(EXPECTED_QUERY_UPDATE_USERS, parametrizedQuery.query());
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