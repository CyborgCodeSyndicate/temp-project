package com.theairebellion.zeus.ui.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UiFrameworkConfigTest {

   private UiFrameworkConfig config;

   @BeforeEach
   void setUp() {
      // Clear system property if set to ensure a clean test
      System.clearProperty("screenshot.on.passed.test");

      // Create a new instance using the ConfigFactory directly
      config = ConfigFactory.create(UiFrameworkConfig.class);
   }

   @Test
   void makeScreenshotOnPassedTest_DefaultValue_ShouldBeFalse() {
      // When using default values
      // Then
      assertFalse(config.makeScreenshotOnPassedTest(), "Default value should be false");
   }

   @Test
   void makeScreenshotOnPassedTest_SystemProperty_ShouldOverrideDefault() {
      // Given system property is set
      System.setProperty("screenshot.on.passed.test", "true");

      // When creating a new config instance
      UiFrameworkConfig overriddenConfig = ConfigFactory.create(UiFrameworkConfig.class);

      // Then
      assertTrue(overriddenConfig.makeScreenshotOnPassedTest(), "System property should override default");

      // Clean up
      System.clearProperty("screenshot.on.passed.test");
   }

   @Test
   void loadPolicy_ShouldBeMerge() {
      // Verify that the @Config.LoadPolicy annotation exists and is set to MERGE
      // This is a bit tricky to test directly, but we can check if the annotation is present
      assertTrue(UiFrameworkConfig.class.isAnnotationPresent(org.aeonbits.owner.Config.LoadPolicy.class),
            "Should have LoadPolicy annotation");

      org.aeonbits.owner.Config.LoadPolicy annotation =
            UiFrameworkConfig.class.getAnnotation(org.aeonbits.owner.Config.LoadPolicy.class);

      assertEquals(org.aeonbits.owner.Config.LoadType.MERGE, annotation.value(),
            "LoadPolicy should be set to MERGE");
   }

   @Test
   void sources_ShouldIncludeSystemPropertiesAndClasspath() {
      // Verify that the @Config.Sources annotation exists and contains expected values
      assertTrue(UiFrameworkConfig.class.isAnnotationPresent(org.aeonbits.owner.Config.Sources.class),
            "Should have Sources annotation");

      org.aeonbits.owner.Config.Sources annotation =
            UiFrameworkConfig.class.getAnnotation(org.aeonbits.owner.Config.Sources.class);

      String[] sources = annotation.value();
      assertEquals(2, sources.length, "Should have 2 sources");
      assertEquals("system:properties", sources[0], "First source should be system properties");
      assertEquals("classpath:${ui.config.file}.properties", sources[1],
            "Second source should be classpath properties file");
   }
}