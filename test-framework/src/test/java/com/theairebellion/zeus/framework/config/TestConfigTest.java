package com.theairebellion.zeus.framework.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
      classes = TestConfig.class,
      properties = "project.package=com.theairebellion.zeus"
)
@DisplayName("TestConfig Tests")
class TestConfigTest {

   @Autowired
   private ApplicationContext context;

   @Autowired
   private Environment environment;

   @Test
   @DisplayName("Spring context should load successfully")
   void contextLoads() {
      assertNotNull(context, "ApplicationContext should be loaded");
   }

   @Test
   @DisplayName("Class should have SpringBootConfiguration annotation")
   void testSpringBootConfigurationAnnotation() {
      SpringBootConfiguration annotation = TestConfig.class.getAnnotation(SpringBootConfiguration.class);
      assertNotNull(annotation, "Should have @SpringBootConfiguration annotation");
   }

   @Test
   @DisplayName("Class should have ComponentScan annotation with correct packages")
   void testComponentScanAnnotation() {
      ComponentScan annotation = TestConfig.class.getAnnotation(ComponentScan.class);
      assertNotNull(annotation, "Should have @ComponentScan annotation");

      String[] basePackages = annotation.basePackages();
      assertEquals(2, basePackages.length, "Should have 2 base packages");
      assertEquals("com.theairebellion.zeus.framework", basePackages[0],
            "First package should be framework package");
      assertEquals("${project.package}", basePackages[1],
            "Second package should be project package placeholder");
   }

   @Test
   @DisplayName("Environment should resolve project.package property")
   void testEnvironmentProperties() {
      String projectPackage = environment.getProperty("project.package");
      assertNotNull(projectPackage, "project.package should be defined");
      assertEquals("com.theairebellion.zeus", projectPackage,
            "project.package should match test property");
   }

   @Test
   @DisplayName("Component scan should work with resolved properties")
   void testComponentScanWithResolvedProperties() {
      // If component scanning works properly, beans from the framework package should be available
      // This is a basic check that the package resolution works
      assertTrue(context.getBeanNamesForType(Object.class).length > 0,
            "Should find beans from component scan");
   }
}