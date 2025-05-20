package com.theairebellion.zeus.ui.annotations;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.UiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class InsertionElementTest {

   @Test
   void shouldHaveCorrectRetentionPolicy() {
      Retention retention = InsertionElement.class.getAnnotation(Retention.class);
      assertNotNull(retention, "InsertionElement should have Retention annotation");
      assertEquals(RetentionPolicy.RUNTIME, retention.value(),
            "InsertionElement should have RUNTIME retention policy");
   }

   @Test
   void shouldHaveCorrectTarget() {
      Target target = InsertionElement.class.getAnnotation(Target.class);
      assertNotNull(target, "InsertionElement should have Target annotation");
      assertArrayEquals(new ElementType[] {ElementType.FIELD}, target.value(),
            "InsertionElement should target FIELD elements only");
   }

   @Test
   void shouldDeclareRequiredElements() {
      try {
         var locatorClassMethod = InsertionElement.class.getDeclaredMethod("locatorClass");
         assertNotNull(locatorClassMethod, "locatorClass element should be declared");
         assertEquals(Class.class, locatorClassMethod.getReturnType(),
               "locatorClass should return Class type");
         // Annotations don't have default values unless explicitly set
         // No need to check default value as it's required
      } catch (NoSuchMethodException e) {
         fail("locatorClass element should be defined in InsertionElement annotation", e);
      }

      try {
         var elementEnumMethod = InsertionElement.class.getDeclaredMethod("elementEnum");
         assertNotNull(elementEnumMethod, "elementEnum element should be declared");
         assertEquals(String.class, elementEnumMethod.getReturnType(),
               "elementEnum should return String type");
      } catch (NoSuchMethodException e) {
         fail("elementEnum element should be defined in InsertionElement annotation", e);
      }

      try {
         var orderMethod = InsertionElement.class.getDeclaredMethod("order");
         assertNotNull(orderMethod, "order element should be declared");
         assertEquals(int.class, orderMethod.getReturnType(),
               "order should return int type");
      } catch (NoSuchMethodException e) {
         fail("order element should be defined in InsertionElement annotation", e);
      }
   }

   // Mock enum for testing
   private enum MockElementEnum {
      BUTTON
   }

   // Mock implementation of UIElement
   private static class MockUiElement implements UiElement {
      @Override
      public By locator() {
         return By.id("mockId");
      }

      @Override
      public <T extends ComponentType> T componentType() {
         return null; // Mocked implementation
      }

      @Override
      public Enum<?> enumImpl() {
         return MockElementEnum.BUTTON;
      }

      @Override
      public Consumer<SmartWebDriver> before() {
         return UiElement.super.before();
      }

      @Override
      public Consumer<SmartWebDriver> after() {
         return UiElement.super.after();
      }
   }

   @Test
   void shouldBeUsableOnField() {
      try {
         // Get the annotated field
         Field field = AnnotatedClass.class.getDeclaredField("annotatedField");

         // Check if our annotation is present
         InsertionElement annotation = field.getAnnotation(InsertionElement.class);
         assertNotNull(annotation, "Field should be annotated with InsertionElement");

         // Test annotation attributes
         assertEquals(MockUiElement.class, annotation.locatorClass(),
               "locatorClass attribute should match");
         assertEquals("BUTTON", annotation.elementEnum(),
               "elementEnum attribute should match");
         assertEquals(1, annotation.order(),
               "order attribute should match");
      } catch (NoSuchFieldException e) {
         fail("Failed to find annotated field", e);
      }
   }

   static class AnnotatedClass {
      @InsertionElement(
            locatorClass = MockUiElement.class,
            elementEnum = "BUTTON",
            order = 1
      )
      private String annotatedField;
   }
}