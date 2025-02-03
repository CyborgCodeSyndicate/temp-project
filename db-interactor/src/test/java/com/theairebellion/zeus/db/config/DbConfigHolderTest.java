package com.theairebellion.zeus.db.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class DbConfigHolderTest {

    @Test
    void testGetDbConfig_ShouldReturnConfig() {
        // Arrange
        DbConfig mockConfig = mock(DbConfig.class);
        ConfigFactory.setProperty("db.config.file", "test-db-config");

        // Act
        DbConfigHolder.getDbConfig(); // Triggers the initialization
        DbConfig config = DbConfigHolder.getDbConfig();

        // Assert
        assertNotNull(config, "Config should not be null");
    }
}