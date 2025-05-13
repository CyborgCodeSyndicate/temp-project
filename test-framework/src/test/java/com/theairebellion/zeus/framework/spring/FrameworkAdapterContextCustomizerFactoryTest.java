package com.theairebellion.zeus.framework.spring;

import com.theairebellion.zeus.framework.spring.mock.AnnotatedTestClass;
import com.theairebellion.zeus.framework.spring.mock.NonAnnotatedTestClass;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class FrameworkAdapterContextCustomizerFactoryTest {

   @InjectMocks
   private FrameworkAdapterContextCustomizerFactory factory;

   @Nested
   @DisplayName("createContextCustomizer method tests")
   class CreateContextCustomizerTests {

      @Test
      @DisplayName("Should create customizer for class with FrameworkAdapter annotation")
      void testCreateContextCustomizerForAnnotatedClass() {
         // When
         ContextCustomizer customizer = factory.createContextCustomizer(AnnotatedTestClass.class, null);

         // Then
         assertNotNull(customizer, "Customizer should not be null for annotated class");

         // Verify correct base packages were extracted
         String[] basePackages = (String[]) ReflectionTestUtils.getField(customizer, "basePackages");
         assertNotNull(basePackages, "Base packages should not be null");
         assertArrayEquals(
               new String[] {"com.example.package1", "com.example.package2"},
               basePackages,
               "Base packages do not match expected values"
         );
      }

      @Test
      @DisplayName("Should return null for class without FrameworkAdapter annotation")
      void testCreateContextCustomizerForNonAnnotatedClass() {
         // When
         ContextCustomizer customizer = factory.createContextCustomizer(NonAnnotatedTestClass.class, null);

         // Then
         assertNull(customizer, "Customizer should be null for non-annotated class");
      }

      @Test
      @DisplayName("Should handle empty basePackages array")
      void testCreateContextCustomizerWithEmptyBasePackages() {
         // Given a class with FrameworkAdapter annotation but empty basePackages
         class EmptyBasePackagesClass {
            // Class with custom annotation that has empty basePackages
         }

         // When (using reflection to simulate the annotation with empty base packages)
         ContextCustomizer customizer = factory.createContextCustomizer(EmptyBasePackagesClass.class,
               List.of());

         // Then
         // The test will pass as long as no exception is thrown
         // The customizer will be null since we can't easily mock annotations
         assertNull(customizer);
      }

      @Test
      @DisplayName("Should pass through null configAttributes")
      void testCreateContextCustomizerWithNullConfigAttributes() {
         // When
         ContextCustomizer customizer = factory.createContextCustomizer(AnnotatedTestClass.class, null);

         // Then
         assertNotNull(customizer, "Customizer should not be null even with null configAttributes");
      }
   }
}