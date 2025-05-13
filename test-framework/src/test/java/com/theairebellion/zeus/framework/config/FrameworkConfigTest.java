package com.theairebellion.zeus.framework.config;

import org.aeonbits.owner.Config;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FrameworkConfig Tests")
class FrameworkConfigTest {

   @Mock
   private FrameworkConfig mockConfig;

   @Test
   @DisplayName("projectPackage() should return configured package")
   void testProjectPackage() {
      // Given
      when(mockConfig.projectPackage()).thenReturn("com.theairebellion.test");

      // When
      String result = mockConfig.projectPackage();

      // Then
      assertEquals("com.theairebellion.test", result);
      verify(mockConfig).projectPackage();
   }

   @Test
   @DisplayName("defaultStorage() should return configured storage")
   void testDefaultStorage() {
      // Given
      when(mockConfig.defaultStorage()).thenReturn("testStorage");

      // When
      String result = mockConfig.defaultStorage();

      // Then
      assertEquals("testStorage", result);
      verify(mockConfig).defaultStorage();
   }

   @Test
   @DisplayName("Interface should have correct Config annotations")
   void testInterfaceAnnotations() {
      // When
      Config.LoadPolicy loadPolicy = FrameworkConfig.class.getAnnotation(Config.LoadPolicy.class);
      Config.Sources sources = FrameworkConfig.class.getAnnotation(Config.Sources.class);

      // Then
      assertNotNull(loadPolicy, "Should have @Config.LoadPolicy annotation");
      assertEquals(Config.LoadType.MERGE, loadPolicy.value(), "Should use MERGE load type");

      assertNotNull(sources, "Should have @Config.Sources annotation");
      assertArrayEquals(
            new String[] {"system:properties", "classpath:${framework.config.file}.properties"},
            sources.value(),
            "Should have correct source paths"
      );
   }

   @Test
   @DisplayName("Methods should have correct Key annotations")
   void testMethodAnnotations() throws NoSuchMethodException {
      // When
      Config.Key projectPackageKey = FrameworkConfig.class
            .getMethod("projectPackage")
            .getAnnotation(Config.Key.class);

      Config.Key defaultStorageKey = FrameworkConfig.class
            .getMethod("defaultStorage")
            .getAnnotation(Config.Key.class);

      // Then
      assertNotNull(projectPackageKey, "projectPackage should have @Key annotation");
      assertEquals("project.package", projectPackageKey.value(), "projectPackage key should be correct");

      assertNotNull(defaultStorageKey, "defaultStorage should have @Key annotation");
      assertEquals("default.storage", defaultStorageKey.value(), "defaultStorage key should be correct");
   }
}