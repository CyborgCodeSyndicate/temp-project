package com.theairebellion.zeus.api.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("System Property Configuration Tests")
class SystemPropertyTest {

   private static final String TEST_PROPERTY_KEY = "test.property.key";
   private static final String TEST_PROPERTY_VALUE = "test.property.value";


   @BeforeEach
   void setUp() {
      // Make sure the property is not set initially
      System.clearProperty(TEST_PROPERTY_KEY);
   }


   @AfterEach
   void tearDown() {
      // Clean up after the test
      System.clearProperty(TEST_PROPERTY_KEY);
   }


   // Define a simple test config interface
   interface TestConfig extends ApiConfig {

      @Key(TEST_PROPERTY_KEY)
      String testProperty();

   }


   @Test
   @DisplayName("Owner ConfigFactory should recognize system properties")
   void testSystemPropertyIsRecognized() {
      // First verify the property is not set
      TestConfig initialConfig = ConfigFactory.create(TestConfig.class);
      assertNull(initialConfig.testProperty(), "Property should initially be null");

      // Set the system property
      System.setProperty(TEST_PROPERTY_KEY, TEST_PROPERTY_VALUE);

      // Create a new config instance
      TestConfig configAfterSet = ConfigFactory.create(TestConfig.class);

      // Verify the property is read correctly
      assertEquals(TEST_PROPERTY_VALUE, configAfterSet.testProperty(),
            "System property should be recognized by the config");
   }

}