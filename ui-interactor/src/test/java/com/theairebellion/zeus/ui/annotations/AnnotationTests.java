package com.theairebellion.zeus.ui.annotations;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.FindBy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class AnnotationTests {

   @Test
   void testHandleUIException() {
      // Create a test class with the annotation
      @SuppressWarnings("unused")
      class TestClass {
         @HandleUiException
         public void annotatedMethod() {
         }

         @HandleUiException(false)
         public void disabledMethod() {
         }
      }

      // Verify HandleUIException annotation properties
      try {
         var method1 = TestClass.class.getMethod("annotatedMethod");
         var method2 = TestClass.class.getMethod("disabledMethod");

         HandleUiException annotation1 = method1.getAnnotation(HandleUiException.class);
         HandleUiException annotation2 = method2.getAnnotation(HandleUiException.class);

         // Check annotation properties
         assertSame(HandleUiException.class, annotation1.annotationType());
         assertTrue(annotation1.value());
         assertFalse(annotation2.value());
      } catch (NoSuchMethodException e) {
         fail("Method not found", e);
      }

      // Verify annotation metadata
      assertEquals(RetentionPolicy.RUNTIME, HandleUiException.class.getAnnotation(Retention.class).value());
      assertEquals(ElementType.METHOD, HandleUiException.class.getAnnotation(Target.class).value()[0]);
   }

   @Test
   void testImplementationOfType() {
      // Create a test class with the annotation
      @ImplementationOfType("TestImplementation")
      class TestClass {
      }

      // Verify ImplementationOfType annotation properties
      ImplementationOfType annotation = TestClass.class.getAnnotation(ImplementationOfType.class);

      // Check annotation properties
      assertNotNull(annotation);
      assertEquals("TestImplementation", annotation.value());

      // Verify annotation metadata
      assertEquals(RetentionPolicy.RUNTIME, ImplementationOfType.class.getAnnotation(Retention.class).value());
      assertEquals(ElementType.TYPE, ImplementationOfType.class.getAnnotation(Target.class).value()[0]);
   }

   @Test
   void testInsertionField() {
      // Create a test class with the annotation
      @SuppressWarnings("unused")
      class TestClass {
         @InsertionField(
               locator = @FindBy(css = "test-locator"),
               type = MockInputComponentType.class,
               componentType = "TEST_COMPONENT",
               order = 1
         )
         private String testField;
      }

      // Verify InsertionField annotation properties
      try {
         Field field = TestClass.class.getDeclaredField("testField");
         InsertionField annotation = field.getAnnotation(InsertionField.class);

         // Check annotation properties
         assertNotNull(annotation);
         assertEquals("test-locator", annotation.locator().css());
         assertEquals(MockInputComponentType.class, annotation.type());
         assertEquals("TEST_COMPONENT", annotation.componentType());
         assertEquals(1, annotation.order());
      } catch (NoSuchFieldException e) {
         fail("Field not found", e);
      }

      // Verify annotation metadata
      assertEquals(RetentionPolicy.RUNTIME, InsertionField.class.getAnnotation(Retention.class).value());
      assertEquals(ElementType.FIELD, InsertionField.class.getAnnotation(Target.class).value()[0]);
   }

   // Dummy class for testing that implements ComponentType
   private interface MockInputComponentType extends ComponentType {
   }
}