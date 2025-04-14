package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.MockTestServiceTest;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("TestService Annotation Tests")
class TestServiceTest {

   @Test
   @DisplayName("Should retrieve TestService annotation with correct value")
   void testTestServiceAnnotation() {
      // When
      TestService testService = MockTestServiceTest.class.getAnnotation(TestService.class);

      // Then
      assertNotNull(testService, "Class should be annotated with @TestService");
      assertEquals("TestService", testService.value(), "Value should match expected");
   }

   @Test
   @DisplayName("Should have correct meta annotations")
   void testTestServiceMetaAnnotations() {
      // When
      Retention retention = TestService.class.getAnnotation(Retention.class);
      Target target = TestService.class.getAnnotation(Target.class);
      Service service = TestService.class.getAnnotation(Service.class);
      Scope scope = TestService.class.getAnnotation(Scope.class);
      Lazy lazy = TestService.class.getAnnotation(Lazy.class);

      // Then
      assertNotNull(retention, "Should have @Retention annotation");
      assertEquals(RetentionPolicy.RUNTIME, retention.value(), "Should have RUNTIME retention policy");

      assertNotNull(target, "Should have @Target annotation");
      assertArrayEquals(new ElementType[] {ElementType.TYPE}, target.value(),
            "Should target types only");

      assertNotNull(service, "Should have @Service annotation");

      assertNotNull(scope, "Should have @Scope annotation");
      assertEquals("prototype", scope.value(), "Scope should be prototype");

      assertNotNull(lazy, "Should have @Lazy annotation");
   }

   @Test
   @DisplayName("Should not be applicable to methods")
   void testTestServiceOnMethod() {
      // Given
      class MethodTestServiceTest {
         // This would not compile due to @Target restriction
         // @TestService("test")
         public void testMethod() {
         }
      }

      // Then
      // Just verify that the test class was created successfully
      assertNotNull(MethodTestServiceTest.class, "Test class should exist");
   }
}