package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.data.DataProvider;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("TestStaticData Annotation Tests")
class TestStaticDataTest {

   // Mock DataProvider for testing
   static class MockDataProvider implements DataProvider {
      @Override
      public Map<String, Object> testStaticData() {
         return Map.of();
      }
   }

   @Test
   @DisplayName("Should have correct meta annotations")
   void testTestStaticDataMetaAnnotations() {
      // When
      Retention retention = TestStaticData.class.getAnnotation(Retention.class);
      Target target = TestStaticData.class.getAnnotation(Target.class);

      // Then
      assertNotNull(retention, "Should have @Retention annotation");
      assertEquals(RetentionPolicy.RUNTIME, retention.value(), "Should have RUNTIME retention policy");

      assertNotNull(target, "Should have @Target annotation");
      assertArrayEquals(
            new ElementType[] {ElementType.TYPE, ElementType.METHOD},
            target.value(),
            "Should target types and methods"
      );
   }

   @Test
   @DisplayName("Should be applicable to classes")
   void testTestStaticDataOnClass() {
      // Given
      @TestStaticData(MockDataProvider.class)
      class ClassWithStaticData {
      }

      // When
      TestStaticData testStaticData = ClassWithStaticData.class.getAnnotation(TestStaticData.class);

      // Then
      assertNotNull(testStaticData, "Class should be annotated with @TestStaticData");
      assertEquals(MockDataProvider.class, testStaticData.value(),
            "Value should match expected DataProvider class");
   }

   @Test
   @DisplayName("Should be applicable to methods")
   void testTestStaticDataOnMethod() {
      // Given
      class ClassWithMethodStaticData {
         @TestStaticData(MockDataProvider.class)
         public void testMethod() {
         }
      }

      try {
         // When
         var testStaticData = ClassWithMethodStaticData.class.getMethod("testMethod")
               .getAnnotation(TestStaticData.class);

         // Then
         assertNotNull(testStaticData, "Method should be annotated with @TestStaticData");
         assertEquals(MockDataProvider.class, testStaticData.value(),
               "Value should match expected DataProvider class");
      } catch (NoSuchMethodException e) {
         fail("Failed to find method", e);
      }
   }
}