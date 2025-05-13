package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.MockCraftTest;
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

@DisplayName("Craft Annotation Tests")
class CraftTest {

   @Test
   @DisplayName("Should retrieve Craft annotation with correct model value")
   void testCraftAnnotation() throws Exception {
      // When
      Method method = MockCraftTest.class.getMethod("mockMethod", String.class);
      Craft craft = method.getParameters()[0].getAnnotation(Craft.class);

      // Then
      assertNotNull(craft, "Parameter should be annotated with @Craft");
      assertEquals("testModel", craft.model(), "Model attribute should match expected value");
   }

   @Test
   @DisplayName("Should have correct retention policy and target")
   void testCraftMetaAnnotations() {
      // When
      Retention retention = Craft.class.getAnnotation(Retention.class);
      Target target = Craft.class.getAnnotation(Target.class);

      // Then
      assertNotNull(retention, "Should have @Retention annotation");
      assertEquals(RetentionPolicy.RUNTIME, retention.value(), "Should have RUNTIME retention policy");

      assertNotNull(target, "Should have @Target annotation");
      assertArrayEquals(new ElementType[] {ElementType.PARAMETER}, target.value(),
            "Should target parameters only");
   }
}