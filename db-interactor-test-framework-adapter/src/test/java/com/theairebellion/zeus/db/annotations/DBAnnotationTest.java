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

    private static final String DB_ANNOTATION_ERROR = "@DB annotation should be present";
    private static final String EXTEND_WITH_ERROR = "@ExtendWith meta-annotation should be present on @DB";
    private static final String FRAMEWORK_ADAPTER_ERROR = "@FrameworkAdapter meta-annotation should be present";

    private static final String[] EXPECTED_BASE_PACKAGES = {"com.theairebellion.zeus.db"};

    @Test
    void testDBAnnotationMetaAnnotations() {
        DB dbAnnotation = DummyClass.class.getAnnotation(DB.class);
        assertNotNull(dbAnnotation, DB_ANNOTATION_ERROR);

        Annotation[] metaAnnotations = dbAnnotation.annotationType().getAnnotations();

        ExtendWith extendWith = dbAnnotation.annotationType().getAnnotation(ExtendWith.class);
        assertNotNull(extendWith, EXTEND_WITH_ERROR);
        assertEquals(DbTestExtension.class, extendWith.value()[0]);

        FrameworkAdapter frameworkAdapter = dbAnnotation.annotationType().getAnnotation(FrameworkAdapter.class);
        assertNotNull(frameworkAdapter, FRAMEWORK_ADAPTER_ERROR);
        assertArrayEquals(EXPECTED_BASE_PACKAGES, frameworkAdapter.basePackages());
    }
}