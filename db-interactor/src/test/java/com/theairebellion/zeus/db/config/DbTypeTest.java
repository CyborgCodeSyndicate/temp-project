package com.theairebellion.zeus.db.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DbTypeTest {

    private static final String MOCK_PROTOCOL = "mock-protocol";

    @Test
    void testMockDbTypeImplementation() {
        DbType mockDbType = new DbType() {
            @Override
            public java.sql.Driver driver() {
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
        };

        assertEquals(MOCK_PROTOCOL, mockDbType.protocol());
    }
}