package com.theairebellion.zeus.framework.spring;

import com.theairebellion.zeus.framework.spring.mock.AnnotatedTestClass;
import com.theairebellion.zeus.framework.spring.mock.NonAnnotatedTestClass;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class FrameworkAdapterContextCustomizerFactoryTest {

    @Test
    void testCreateContextCustomizerForAnnotatedClass() {
        ContextCustomizerFactory factory = new FrameworkAdapterContextCustomizerFactory();
        ContextCustomizer customizer = factory.createContextCustomizer(AnnotatedTestClass.class, null);
        assertNotNull(customizer);
        String[] basePackages = (String[]) ReflectionTestUtils.getField(customizer, "basePackages");
        assertNotNull(basePackages);
        assertArrayEquals(new String[] { "com.example.package1", "com.example.package2" }, basePackages);
    }

    @Test
    void testCreateContextCustomizerForNonAnnotatedClass() {
        ContextCustomizerFactory factory = new FrameworkAdapterContextCustomizerFactory();
        ContextCustomizer customizer = factory.createContextCustomizer(NonAnnotatedTestClass.class, null);
        assertNull(customizer);
    }
}