package com.theairebellion.zeus.db.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DbTypeTest {

    @Test
    void testMockDbTypeImplementation() {
        // Arrange
        DbType mockDbType = new DbType() {
            @Override
            public java.sql.Driver driver() {
                return null;
            }

            @Override
            public String protocol() {
                return "mock-protocol";
            }

            @Override
            public Enum<?> enumImpl() {
                return null;
            }
        };

        // Act & Assert
        assertEquals("mock-protocol", mockDbType.protocol());
    }
}