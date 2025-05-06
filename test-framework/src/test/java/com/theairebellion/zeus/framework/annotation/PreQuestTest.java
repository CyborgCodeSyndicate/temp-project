package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.MockPreQuestTest;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("PreQuest Annotation Tests")
class PreQuestTest {

   @Test
   @DisplayName("Should retrieve PreQuest annotation with nested Journey")
   void testPreQuestAnnotation() throws Exception {
      // When
      Method method = MockPreQuestTest.class.getMethod("mockPreQuestMethod");
      PreQuest preQuest = method.getAnnotation(PreQuest.class);

      // Then
      assertNotNull(preQuest, "Method should be annotated with @PreQuest");

      Journey[] journeys = preQuest.value();
      assertEquals(1, journeys.length, "Should have one Journey");

      Journey journey = journeys[0];
      assertEquals("prequest", journey.value(), "Journey value should match");
      assertEquals(5, journey.order(), "Journey order should match");
   }

   @Test
   @DisplayName("Should have correct meta annotations")
   void testPreQuestMetaAnnotations() {
      // When
      Retention retention = PreQuest.class.getAnnotation(Retention.class);
      Target target = PreQuest.class.getAnnotation(Target.class);

      // Then
      assertNotNull(retention, "Should have @Retention annotation");
      assertEquals(RetentionPolicy.RUNTIME, retention.value(), "Should have RUNTIME retention policy");

      assertNotNull(target, "Should have @Target annotation");
      assertArrayEquals(new ElementType[] {ElementType.METHOD}, target.value(),
            "Should target methods only");
   }

   @Test
   @DisplayName("Should handle multiple nested Journey annotations")
   void testMultipleNestedJourneys() {
      // Given
      class MultipleJourneysTest {
         @PreQuest({
               @Journey(value = "first", order = 1),
               @Journey(value = "second", order = 2)
         })
         public void mockMethod() {
         }
      }

      try {
         // When
         Method method = MultipleJourneysTest.class.getMethod("mockMethod");
         PreQuest preQuest = method.getAnnotation(PreQuest.class);

         // Then
         assertNotNull(preQuest, "Method should be annotated with @PreQuest");
         assertEquals(2, preQuest.value().length, "Should have two journeys");
         assertEquals("first", preQuest.value()[0].value(), "First journey value should match");
         assertEquals("second", preQuest.value()[1].value(), "Second journey value should match");
      } catch (NoSuchMethodException e) {
         fail("Failed to find method", e);
      }
   }
}