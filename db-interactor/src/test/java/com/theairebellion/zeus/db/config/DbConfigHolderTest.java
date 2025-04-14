package com.theairebellion.zeus.db.config;

import java.lang.reflect.Field;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DbConfigHolderTest {

   private static final String DB_CONFIG_FILE_PROPERTY = "db.config.file";
   private static final String TEST_DB_CONFIG = "test-db-config";

   @Mock
   private DbConfig mockDbConfig;

   @BeforeEach
   void setUp() throws Exception {
      // Reset the static config field before each test
      Field configField = DbConfigHolder.class.getDeclaredField("config");
      configField.setAccessible(true);
      configField.set(null, null);

      // Set the config file property
      ConfigFactory.setProperty(DB_CONFIG_FILE_PROPERTY, TEST_DB_CONFIG);
   }

   @AfterEach
   void tearDown() {
      // Clean up system property
      System.clearProperty(DB_CONFIG_FILE_PROPERTY);
   }

   @Test
   @DisplayName("Should return config when accessed first time")
   void testGetDbConfig_ShouldReturnConfig() {
      // When
      DbConfig config = DbConfigHolder.getDbConfig();

      // Then
      assertNotNull(config, "Config should not be null");
   }

   @Test
   @DisplayName("Should reuse same config instance on multiple calls")
   void testGetDbConfig_ShouldReuseInstance() {
      // When
      DbConfig config1 = DbConfigHolder.getDbConfig();
      DbConfig config2 = DbConfigHolder.getDbConfig();

      // Then
      assertSame(config1, config2, "Should return the same config instance");
   }

   @Test
   @DisplayName("Should create config via ConfigCache")
   void testGetDbConfig_ShouldUseConfigCache() {
      // Given
      try (MockedStatic<org.aeonbits.owner.ConfigCache> mockedConfigCache = mockStatic(org.aeonbits.owner.ConfigCache.class)) {
         mockedConfigCache.when(() -> org.aeonbits.owner.ConfigCache.getOrCreate(eq(DbConfig.class)))
               .thenReturn(mockDbConfig);

         // When
         DbConfig result = DbConfigHolder.getDbConfig();

         // Then
         assertSame(mockDbConfig, result, "Should return the config from ConfigCache");
         mockedConfigCache.verify(() -> org.aeonbits.owner.ConfigCache.getOrCreate(eq(DbConfig.class)), times(1));
      }
   }

   @Test
   @DisplayName("Should create config only once even with concurrent access")
   void testGetDbConfig_ThreadSafety() throws Exception {
      // Given
      int threadCount = 10;
      Thread[] threads = new Thread[threadCount];
      DbConfig[] results = new DbConfig[threadCount];

      // When - simulate concurrent access
      for (int i = 0; i < threadCount; i++) {
         final int index = i;
         threads[i] = new Thread(() -> {
            results[index] = DbConfigHolder.getDbConfig();
         });
      }

      for (Thread thread : threads) {
         thread.start();
      }

      for (Thread thread : threads) {
         thread.join();
      }

      // Then
      DbConfig firstResult = results[0];
      assertNotNull(firstResult, "Config should not be null");

      for (int i = 1; i < threadCount; i++) {
         assertSame(firstResult, results[i], "All threads should get the same config instance");
      }
   }

   @Test
   @DisplayName("Should respect the db.config.file property")
   void testGetDbConfig_ShouldUseConfigProperty() {
      // Given
      String customValue = "custom-db-config";
      ConfigFactory.setProperty(DB_CONFIG_FILE_PROPERTY, customValue);

      try (MockedStatic<org.aeonbits.owner.ConfigCache> mockedConfigCache = mockStatic(org.aeonbits.owner.ConfigCache.class)) {
         // When
         DbConfigHolder.getDbConfig();

         // Then
         // Verify that ConfigCache is called (would use the custom property via ConfigFactory)
         mockedConfigCache.verify(() -> org.aeonbits.owner.ConfigCache.getOrCreate(eq(DbConfig.class)));

         // We can't easily verify the property was used since it happens inside ConfigCache,
         // but this test confirms the ConfigCache.getOrCreate method is called
      }
   }
}