package com.theairebellion.zeus.framework.annotation;

import com.theairebellion.zeus.framework.annotation.mock.MockRipperTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ripper Annotation Tests")
class RipperTest {

    @Test
    @DisplayName("Should retrieve Ripper annotation with correct targets")
    void testRipperAnnotation() throws Exception {
        // When
        Method method = MockRipperTest.class.getMethod("mockRipperMethod");
        Ripper ripper = method.getAnnotation(Ripper.class);

        // Then
        assertNotNull(ripper, "Method should be annotated with @Ripper");
        assertArrayEquals(new String[]{"target1", "target2"}, ripper.targets(),
                "Targets should match expected values");
    }

    @Test
    @DisplayName("Should have correct meta annotations")
    void testRipperMetaAnnotations() {
        // When
        Retention retention = Ripper.class.getAnnotation(Retention.class);
        Target target = Ripper.class.getAnnotation(Target.class);

        // Then
        assertNotNull(retention, "Should have @Retention annotation");
        assertEquals(RetentionPolicy.RUNTIME, retention.value(), "Should have RUNTIME retention policy");

        assertNotNull(target, "Should have @Target annotation");
        assertArrayEquals(
                new ElementType[]{ElementType.TYPE, ElementType.METHOD},
                target.value(),
                "Should target types and methods"
        );
    }

    @Test
    @DisplayName("Should be applicable to classes")
    void testRipperOnClass() {
        // Given
        @Ripper(targets = {"classTarget"})
        class ClassRipperTest {}

        // When
        Ripper ripper = ClassRipperTest.class.getAnnotation(Ripper.class);

        // Then
        assertNotNull(ripper, "Class should be annotated with @Ripper");
        assertArrayEquals(new String[]{"classTarget"}, ripper.targets(),
                "Target should match expected value");
    }
}