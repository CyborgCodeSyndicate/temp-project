package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.MockJourneyTest;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("JourneyData Annotation Tests")
class JourneyDataTest {

   @Test
   @DisplayName("Should have correct meta annotations")
   void testJourneyDataMetaAnnotations() {
      // When
      Retention retention = JourneyData.class.getAnnotation(Retention.class);
      Target target = JourneyData.class.getAnnotation(Target.class);

      // Then
      assertNotNull(retention, "Should have @Retention annotation");
      assertEquals(RetentionPolicy.RUNTIME, retention.value(), "Should have RUNTIME retention policy");

      assertNotNull(target, "Should have @Target annotation");
      assertEquals(0, target.value().length, "Should have empty target array");
   }

   @Test
   @DisplayName("Should test JourneyData default values")
   void testJourneyDataDefaultValues() throws Exception {
      // Given - Using an existing class that already contains JourneyData
      Method method = MockJourneyTest.class.getMethod("mockJourneyMethod");
      Journey journey = method.getAnnotationsByType(Journey.class)[0];

      // When - Access the JourneyData from the Journey
      JourneyData[] journeyData = journey.journeyData();

      // Then - Verify one with late=true
      assertEquals(1, journeyData.length, "Should have one JourneyData item");
      assertEquals("data1", journeyData[0].value(), "Value should match");
      assertTrue(journeyData[0].late(), "Late should be true");

      // Create a new mock class to test default values
      class DefaultValueTest {
         @Journey(value = "test", journeyData = @JourneyData("default"))
         public void defaultValueMethod() {
         }
      }

      // Get the JourneyData with default values
      Method defaultMethod = DefaultValueTest.class.getMethod("defaultValueMethod");
      Journey defaultJourney = defaultMethod.getAnnotation(Journey.class);
      JourneyData defaultData = defaultJourney.journeyData()[0];

      // Verify default value
      assertEquals("default", defaultData.value(), "Value should match");
      assertFalse(defaultData.late(), "Default late should be false");
   }
}