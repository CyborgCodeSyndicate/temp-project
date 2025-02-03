package com.theairebellion.zeus.db.annotations;

import com.theairebellion.zeus.db.extensions.DbTestExtension;
import com.theairebellion.zeus.framework.annotation.FrameworkAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;

public class DBAnnotationTest {

    @DB
    private static class DummyClass {}

    @Test
    void testDBAnnotationMetaAnnotations() {
        // Get the DB annotation from the test class
        DB dbAnnotation = DummyClass.class.getAnnotation(DB.class);
        assertNotNull(dbAnnotation, "@DB annotation should be present");

        // Check for meta-annotations on the DB annotation itself
        Annotation[] metaAnnotations = dbAnnotation.annotationType().getAnnotations();

        // Verify ExtendWith meta-annotation
        ExtendWith extendWith = dbAnnotation.annotationType().getAnnotation(ExtendWith.class);
        assertNotNull(extendWith, "@ExtendWith meta-annotation should be present on @DB");
        assertEquals(DbTestExtension.class, extendWith.value()[0]);

        // Verify FrameworkAdapter meta-annotation
        FrameworkAdapter frameworkAdapter = dbAnnotation.annotationType().getAnnotation(FrameworkAdapter.class);
        assertNotNull(frameworkAdapter, "@FrameworkAdapter meta-annotation should be present");
        assertArrayEquals(new String[]{"com.theairebellion.zeus.db"}, frameworkAdapter.basePackages());
    }
}