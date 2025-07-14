package com.theairebellion.zeus.db.allure;

import com.theairebellion.zeus.db.client.DbClient;
import com.theairebellion.zeus.db.config.DatabaseConfiguration;
import com.theairebellion.zeus.db.config.DbType;
import com.theairebellion.zeus.db.connector.BaseDbConnectorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AllureDbClientManagerTest {

   private static final String HOST = "localhost";
   private static final int PORT = 1234;
   private static final String DATABASE = "testdb";
   private static final String USER = "user";
   private static final String PASSWORD = "pass";

   @Mock
   private BaseDbConnectorService connector;

   @InjectMocks
   private AllureDbClientManager manager;

   @Mock
   private DbType dbType;

   private DatabaseConfiguration createDummyConfig() {
      when(dbType.protocol()).thenReturn("jdbc");

      return DatabaseConfiguration.builder()
            .dbType(dbType)
            .host(HOST)
            .port(PORT)
            .database(DATABASE)
            .dbUser(USER)
            .dbPassword(PASSWORD)
            .build();
   }

   @Test
   @DisplayName("initializeDbClient should return RelationalDbClientAllure instance")
   void testInitializeDbClient() {
      // Arrange
      DatabaseConfiguration config = createDummyConfig();

      // Act
      DbClient client = manager.initializeDbClient(config);

      // Assert
      assertAll(
            "Client should be initialized correctly",
            () -> assertNotNull(client, "Client should not be null"),
            () -> assertInstanceOf(RelationalDbClientAllure.class, client,
                  "Client should be an instance of RelationalDbClientAllure")
      );
   }

   @Test
   @DisplayName("constructor should create instance correctly")
   void testConstructor() {
      // Arrange
      BaseDbConnectorService anotherConnector = mock(BaseDbConnectorService.class);

      // Act
      AllureDbClientManager managerInstance = new AllureDbClientManager(anotherConnector);

      // Assert
      assertNotNull(managerInstance, "Manager instance should not be null");
   }

   @Test
   @DisplayName("initializeDbClient should create a new instance for each call")
   void testInitializeDbClientCreatesNewInstance() {
      // Arrange
      DatabaseConfiguration config = createDummyConfig();

      // Act
      DbClient client1 = manager.initializeDbClient(config);
      DbClient client2 = manager.initializeDbClient(config);

      // Assert
      assertAll(
            "Each call should return a new instance",
            () -> assertNotNull(client1, "First client should not be null"),
            () -> assertNotNull(client2, "Second client should not be null"),
            () -> assertNotSame(client1, client2, "Clients should be different instances")
      );
   }
}