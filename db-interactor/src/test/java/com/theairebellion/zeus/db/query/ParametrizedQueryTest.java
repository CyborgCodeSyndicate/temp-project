package com.theairebellion.zeus.db.query;

import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParametrizedQueryTest {

    @Test
    void testParametrizedQuery_ReplacesPlaceholders() {
        DbQuery originalQuery = new DbQuery() {
            @Override
            public String query() {
                return "SELECT * FROM users WHERE id = {id} AND name = {name}";
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

        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        DbQuery parametrizedQuery = query.withParam("id", 1).withParam("name", "John");

        assertEquals("SELECT * FROM users WHERE id = 1 AND name = John", parametrizedQuery.query());
    }

    @Test
    void testParametrizedQuery_NoParameters() {
        DbQuery originalQuery = new DbQuery() {
            @Override
            public String query() {
                return "SELECT * FROM users";
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

        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        assertEquals("SELECT * FROM users", query.query());
    }

    @Test
    void testParametrizedQuery_NullParameterValue() {
        DbQuery originalQuery = new DbQuery() {
            @Override
            public String query() {
                return "SELECT * FROM users WHERE id = {id}";
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

        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        DbQuery parametrizedQuery = query.withParam("id", null);

        assertEquals("SELECT * FROM users WHERE id = null", parametrizedQuery.query());
    }

    @Test
    void testParametrizedQuery_OverwriteParameter() {
        DbQuery originalQuery = new DbQuery() {
            @Override
            public String query() {
                return "SELECT * FROM users WHERE id = {id}";
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

        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        DbQuery parametrizedQuery = query.withParam("id", 1).withParam("id", 2);

        assertEquals("SELECT * FROM users WHERE id = 2", parametrizedQuery.query());
    }

    @Test
    void testParametrizedQuery_MultipleParameters() {
        DbQuery originalQuery = new DbQuery() {
            @Override
            public String query() {
                return "SELECT * FROM users WHERE id = {id} AND name = {name}";
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

        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        DbQuery parametrizedQuery = query.withParam("id", 1).withParam("name", "John");

        assertEquals("SELECT * FROM users WHERE id = 1 AND name = John", parametrizedQuery.query());
    }

    @Test
    void testParametrizedQuery_ReplacesNullPlaceholders() {
        DbQuery originalQuery = new DbQuery() {
            @Override
            public String query() {
                return "SELECT * FROM users WHERE id = {id} AND name = {name}";
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

        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        DbQuery parametrizedQuery = query.withParam("id", null).withParam("name", "John");

        assertEquals("SELECT * FROM users WHERE id = null AND name = John", parametrizedQuery.query());
    }

    @Test
    void testParametrizedQuery_NoPlaceholders() {
        DbQuery originalQuery = new DbQuery() {
            @Override
            public String query() {
                return "SELECT * FROM users";
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

        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        assertEquals("SELECT * FROM users", query.query());
    }

    @Test
    void testParametrizedQuery_MultipleReplacements() {
        DbQuery originalQuery = new DbQuery() {
            @Override
            public String query() {
                return "UPDATE users SET name = {name}, age = {age} WHERE id = {id}";
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

        ParametrizedQuery query = new ParametrizedQuery(originalQuery);

        DbQuery parametrizedQuery = query.withParam("name", "John")
                .withParam("age", 30)
                .withParam("id", 1);

        assertEquals("UPDATE users SET name = John, age = 30 WHERE id = 1", parametrizedQuery.query());
    }
}