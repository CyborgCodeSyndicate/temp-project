package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.client.DbClient;
import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class AllureDbClientManagerTest {

    // We assume that DatabaseConfiguration has a builder method.
    private DatabaseConfiguration createDummyConfig() {
        return DatabaseConfiguration.builder()
                // For testing purposes, passing null for DbType is acceptable if not used.
                .dbType(null)
                .host("localhost")
                .port(1234)
                .database("testdb")
                .dbUser("user")
                .dbPassword("pass")
                .build();
    }

    @Test
    void testInitializeDbClient() {
        BaseDbConnectorService connector = mock(BaseDbConnectorService.class);
        AllureDbClientManager manager = new AllureDbClientManager(connector);
        DatabaseConfiguration config = createDummyConfig();

        // Call the (protected) method initializeDbClient.
        // (Because the test is in the same package, we can call it directly.)
        DbClient client = manager.initializeDbClient(config);
        assertNotNull(client);
        // Verify that the returned client is an instance of RelationalDbClientAllure.
        assertInstanceOf(RelationalDbClientAllure.class, client);
    }
}