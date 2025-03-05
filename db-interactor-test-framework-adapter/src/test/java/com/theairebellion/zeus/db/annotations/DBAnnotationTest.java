package com.theairebellion.zeus.db.annotations;

import com.theairebellion.zeus.db.extensions.DbTestExtension;
import com.theairebellion.zeus.framework.annotation.FrameworkAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

class DBAnnotationTest {

    @DB
    private static class DummyClass {}

    @Test
    @DisplayName("@DB annotation should have proper meta-annotations")
    void testDBAnnotationMetaAnnotations() {
        // Given
        Class<DB> annotationType = DB.class;

        // When
        ExtendWith extendWith = annotationType.getAnnotation(ExtendWith.class);
        FrameworkAdapter frameworkAdapter = annotationType.getAnnotation(FrameworkAdapter.class);
        Retention retention = annotationType.getAnnotation(Retention.class);
        Target target = annotationType.getAnnotation(Target.class);

        // Then
        assertThat(extendWith)
                .as("@ExtendWith meta-annotation should be present")
                .isNotNull();
        assertThat(extendWith.value())
                .as("@ExtendWith should specify DbTestExtension")
                .containsExactly(DbTestExtension.class);

        assertThat(frameworkAdapter)
                .as("@FrameworkAdapter meta-annotation should be present")
                .isNotNull();
        assertThat(frameworkAdapter.basePackages())
                .as("@FrameworkAdapter should have correct base package")
                .containsExactly("com.theairebellion.zeus.db");

        assertThat(retention)
                .as("@Retention meta-annotation should be present")
                .isNotNull();
        assertThat(retention.value())
                .as("@Retention should be RUNTIME")
                .isEqualTo(RetentionPolicy.RUNTIME);

        assertThat(target)
                .as("@Target meta-annotation should be present")
                .isNotNull();
        assertThat(target.value())
                .as("@Target should only include TYPE")
                .containsExactly(ElementType.TYPE);
    }

    @Test
    @DisplayName("@DB annotation should be applied to annotated class")
    void testDBAnnotationIsApplied() {
        // When
        DB dbAnnotation = DummyClass.class.getAnnotation(DB.class);

        // Then
        assertThat(dbAnnotation)
                .as("@DB annotation should be present on the test class")
                .isNotNull();
    }
}