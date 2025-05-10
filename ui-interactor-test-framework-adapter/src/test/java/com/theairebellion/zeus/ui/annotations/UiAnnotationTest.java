package com.theairebellion.zeus.ui.annotations;

import com.theairebellion.zeus.framework.annotation.FrameworkAdapter;
import com.theairebellion.zeus.ui.extensions.UiTestExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UiAnnotationTest {

   @Test
   void shouldHaveCorrectRetentionPolicy() {
      Retention retention = UI.class.getAnnotation(Retention.class);
      assertNotNull(retention, "UI annotation should have Retention annotation");
      assertEquals(RetentionPolicy.RUNTIME, retention.value(),
            "UI annotation should have RUNTIME retention policy");
   }

   @Test
   void shouldHaveCorrectTarget() {
      Target target = UI.class.getAnnotation(Target.class);
      assertNotNull(target, "UI annotation should have Target annotation");
      assertArrayEquals(new ElementType[] {ElementType.TYPE}, target.value(),
            "UI annotation should target TYPE elements only");
   }

   @Test
   void shouldHaveFrameworkAdapterAnnotation() {
      FrameworkAdapter frameworkAdapter = UI.class.getAnnotation(FrameworkAdapter.class);
      assertNotNull(frameworkAdapter, "UI annotation should have FrameworkAdapter annotation");
      assertArrayEquals(new String[] {"com.theairebellion.zeus.ui"}, frameworkAdapter.basePackages(),
            "UI annotation should have correct basePackages");
   }

   @Test
   void shouldHaveExtendWithAnnotation() {
      ExtendWith extendWith = UI.class.getAnnotation(ExtendWith.class);
      assertNotNull(extendWith, "UI annotation should have ExtendWith annotation");
      assertArrayEquals(new Class[] {UiTestExtension.class}, extendWith.value(),
            "UI annotation should extend UiTestExtension");
   }

   @Test
   void shouldBeUsableOnClass() {
      UI annotation = AnnotatedClass.class.getAnnotation(UI.class);
      assertNotNull(annotation, "Class should be annotated with UI");

      // Meta-annotations (like ExtendWith) are not inherited to the annotated class in reflection
      // Instead, we should verify the UI annotation itself has these annotations
      ExtendWith extendWith = UI.class.getAnnotation(ExtendWith.class);
      assertNotNull(extendWith, "UI annotation should have ExtendWith annotation");
      assertArrayEquals(new Class[] {UiTestExtension.class}, extendWith.value(),
            "UI annotation's ExtendWith should have UiTestExtension");

      FrameworkAdapter frameworkAdapter = UI.class.getAnnotation(FrameworkAdapter.class);
      assertNotNull(frameworkAdapter, "UI annotation should have FrameworkAdapter annotation");
      assertArrayEquals(new String[] {"com.theairebellion.zeus.ui"}, frameworkAdapter.basePackages(),
            "UI annotation's FrameworkAdapter should have correct basePackages");
   }

   @UI
   static class AnnotatedClass {
      // Class body is irrelevant for the test
   }
}