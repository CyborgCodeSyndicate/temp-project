package com.theairebellion.zeus.ui.annotations;

import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class InterceptRequestsTest {

    @Test
    void shouldHaveCorrectRetentionPolicy() {
        Retention retention = InterceptRequests.class.getAnnotation(Retention.class);
        assertNotNull(retention, "InterceptRequests should have Retention annotation");
        assertEquals(RetentionPolicy.RUNTIME, retention.value(),
                "InterceptRequests should have RUNTIME retention policy");
    }

    @Test
    void shouldHaveCorrectTarget() {
        Target target = InterceptRequests.class.getAnnotation(Target.class);
        assertNotNull(target, "InterceptRequests should have Target annotation");
        assertArrayEquals(new ElementType[]{ElementType.METHOD}, target.value(),
                "InterceptRequests should target METHOD elements only");
    }

    @Test
    void shouldDeclareRequiredElements() {
        try {
            Method requestUrlSubStringsMethod = InterceptRequests.class.getDeclaredMethod("requestUrlSubStrings");
            assertNotNull(requestUrlSubStringsMethod, "requestUrlSubStrings element should be declared");
            assertEquals(String[].class, requestUrlSubStringsMethod.getReturnType(),
                    "requestUrlSubStrings should return String[] type");
        } catch (NoSuchMethodException e) {
            fail("requestUrlSubStrings element should be defined in InterceptRequests annotation", e);
        }
    }

    @Test
    void shouldBeUsableOnMethod() {
        try {
            // Get the annotated method
            Method method = AnnotatedClass.class.getDeclaredMethod("annotatedMethod");

            // Check if our annotation is present
            InterceptRequests annotation = method.getAnnotation(InterceptRequests.class);
            assertNotNull(annotation, "Method should be annotated with InterceptRequests");

            // Test annotation attributes
            assertArrayEquals(
                    new String[]{"api/users", "api/products"},
                    annotation.requestUrlSubStrings(),
                    "requestUrlSubStrings attribute should match"
            );
        } catch (NoSuchMethodException e) {
            fail("Failed to find annotated method", e);
        }
    }

    @Test
    void shouldHandleEmptyUrlSubStrings() {
        try {
            // Get the annotated method
            Method method = AnnotatedClass.class.getDeclaredMethod("emptyUrlSubStrings");

            // Check if our annotation is present
            InterceptRequests annotation = method.getAnnotation(InterceptRequests.class);
            assertNotNull(annotation, "Method should be annotated with InterceptRequests");

            // Test annotation attributes
            assertArrayEquals(
                    new String[]{},
                    annotation.requestUrlSubStrings(),
                    "Empty requestUrlSubStrings should be handled"
            );
        } catch (NoSuchMethodException e) {
            fail("Failed to find annotated method", e);
        }
    }

    static class AnnotatedClass {
        @InterceptRequests(
                requestUrlSubStrings = {"api/users", "api/products"}
        )
        void annotatedMethod() {
            // Method body is irrelevant for the test
        }

        @InterceptRequests(
                requestUrlSubStrings = {}
        )
        void emptyUrlSubStrings() {
            // Method body is irrelevant for the test
        }
    }
}