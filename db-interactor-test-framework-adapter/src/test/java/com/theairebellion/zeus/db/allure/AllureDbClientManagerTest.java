package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.client.DbClient;
import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class AllureDbClientManagerTest {

    private static final String HOST = "localhost";
    private static final int PORT = 1234;
    private static final String DATABASE = "testdb";
    private static final String USER = "user";
    private static final String PASSWORD = "pass";

    private DatabaseConfiguration createDummyConfig() {
        return DatabaseConfiguration.builder()
                .dbType(null)
                .host(HOST)
                .port(PORT)
                .database(DATABASE)
                .dbUser(USER)
                .dbPassword(PASSWORD)
                .build();
    }

    @Test
    void testInitializeDbClient() {
        BaseDbConnectorService connector = mock(BaseDbConnectorService.class);
        AllureDbClientManager manager = new AllureDbClientManager(connector);
        DatabaseConfiguration config = createDummyConfig();

        DbClient client = manager.initializeDbClient(config);

        assertNotNull(client);
        assertInstanceOf(RelationalDbClientAllure.class, client);
    }
}