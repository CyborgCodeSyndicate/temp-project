package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.MockOdysseyTest;
import com.theairebellion.zeus.framework.extension.Craftsman;
import com.theairebellion.zeus.framework.extension.Epilogue;
import com.theairebellion.zeus.framework.extension.Initiator;
import com.theairebellion.zeus.framework.extension.Oracle;
import com.theairebellion.zeus.framework.extension.Prologue;
import com.theairebellion.zeus.framework.extension.RipperMan;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Odyssey Annotation Tests")
class OdysseyTest {

   @Test
   @DisplayName("Should include all required extensions")
   void odysseyAnnotation_ShouldContainCorrectExtensions() {
      // When
      Odyssey odyssey = MockOdysseyTest.class.getAnnotation(Odyssey.class);
      ExtendWith extendWith = odyssey.annotationType().getAnnotation(ExtendWith.class);

      // Then
      assertNotNull(extendWith, "Should have @ExtendWith annotation");
      assertEquals(6, extendWith.value().length, "Should have 6 extensions");

      List<Class<?>> extensions = Arrays.asList(extendWith.value());
      assertTrue(extensions.contains(Oracle.class), "Should include Oracle extension");
      assertTrue(extensions.contains(Prologue.class), "Should include Prologue extension");
      assertTrue(extensions.contains(Epilogue.class), "Should include Epilogue extension");
      assertTrue(extensions.contains(Craftsman.class), "Should include Craftsman extension");
      assertTrue(extensions.contains(RipperMan.class), "Should include RipperMan extension");
      assertTrue(extensions.contains(Initiator.class), "Should include Initiator extension");
   }

   @Test
   @DisplayName("Should have correct meta annotations")
   void testOdysseyMetaAnnotations() {
      // When
      Retention retention = Odyssey.class.getAnnotation(Retention.class);
      Target target = Odyssey.class.getAnnotation(Target.class);

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
   @DisplayName("Should be applicable to methods")
   void testOdysseyOnMethod() {
      // Given
      class MethodOdysseyTest {
         @Odyssey
         public void odysseyMethod() {
         }
      }

      try {
         // When
         var odyssey = MethodOdysseyTest.class.getMethod("odysseyMethod")
               .getAnnotation(Odyssey.class);

         // Then
         assertNotNull(odyssey, "Method should be annotated with @Odyssey");
      } catch (NoSuchMethodException e) {
         fail("Failed to find method", e);
      }
   }
}