package com.theairebellion.zeus.framework.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FrameworkAdapter Annotation Tests")
class FrameworkAdapterTest {

    @Test
    @DisplayName("Should have correct meta annotations")
    void testFrameworkAdapterMetaAnnotations() {
        // When
        Retention retention = FrameworkAdapter.class.getAnnotation(Retention.class);
        Target target = FrameworkAdapter.class.getAnnotation(Target.class);
        Inherited inherited = FrameworkAdapter.class.getAnnotation(Inherited.class);

        // Then
        assertNotNull(retention, "Should have @Retention annotation");
        assertEquals(RetentionPolicy.RUNTIME, retention.value(), "Should have RUNTIME retention policy");

        assertNotNull(target, "Should have @Target annotation");
        assertArrayEquals(new ElementType[]{ElementType.TYPE}, target.value(),
                "Should target classes only");

        assertNotNull(inherited, "Should have @Inherited annotation");
    }

    // Mock class for testing
    @FrameworkAdapter(basePackages = {"com.test"})
    static class MockFrameworkAdapterTest {
    }

    @Test
    @DisplayName("Should be able to retrieve basePackages attribute")
    void testBasePackagesAttribute() {
        // When
        FrameworkAdapter adapter = MockFrameworkAdapterTest.class.getAnnotation(FrameworkAdapter.class);

        // Then
        assertNotNull(adapter, "Class should be annotated with @FrameworkAdapter");
        assertArrayEquals(new String[]{"com.test"}, adapter.basePackages(),
                "basePackages attribute should match expected value");
    }
}