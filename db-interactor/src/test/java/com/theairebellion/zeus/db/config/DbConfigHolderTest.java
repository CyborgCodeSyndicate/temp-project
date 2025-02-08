package com.theairebellion.zeus.db.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DbConfigHolderTest {

    private static final String DB_CONFIG_FILE_PROPERTY = "db.config.file";
    private static final String TEST_DB_CONFIG = "test-db-config";

    @Test
    void testGetDbConfig_ShouldReturnConfig() {
        ConfigFactory.setProperty(DB_CONFIG_FILE_PROPERTY, TEST_DB_CONFIG);

        DbConfigHolder.getDbConfig();
        DbConfig config = DbConfigHolder.getDbConfig();

        assertNotNull(config, "Config should not be null");
    }
}