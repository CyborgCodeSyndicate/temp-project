package com.theairebellion.zeus.api.config;

import java.lang.reflect.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("ApiConfigHolder Basic Tests")
class ApiConfigHolderTest {

   @Test
   @DisplayName("getApiConfig should return non-null config")
   void getApiConfigShouldReturnNonNullConfig() {
      // Act
      ApiConfig config = ApiConfigHolder.getApiConfig();

      // Assert
      assertNotNull(config, "ApiConfig should not be null");
   }

   @Test
   @DisplayName("getApiConfig should return the same instance on multiple calls")
   void getApiConfigShouldReturnSameInstance() throws Exception {
      // Reset the singleton first to ensure test isolation
      resetConfigHolder();

      // Act
      ApiConfig config1 = ApiConfigHolder.getApiConfig();
      ApiConfig config2 = ApiConfigHolder.getApiConfig();

      // Assert
      assertSame(config1, config2, "Multiple calls to getApiConfig should return the same instance");
   }

   /**
    * Helper method to reset the singleton for testing
    */
   private void resetConfigHolder() throws Exception {
      Field configField = ApiConfigHolder.class.getDeclaredField("config");
      configField.setAccessible(true);
      configField.set(null, null);
   }
}