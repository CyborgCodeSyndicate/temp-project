package com.theairebellion.zeus.framework.spring;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;

class FrameworkAdapterContextCustomizerTest {

    @Getter
    static class DummyAnnotationConfigApplicationContext extends AnnotationConfigApplicationContext {
        private String[] scannedPackages;

        @Override
        public void scan(String... basePackages) {
            this.scannedPackages = basePackages;
        }

    }

    @Test
    void testConstructorThrowsForNullBasePackages() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new FrameworkAdapterContextCustomizer(null));
        assertEquals("Base packages must not be null", ex.getMessage());
    }

    @Test
    void testCustomizeContextWithAnnotationConfigApplicationContext() {
        String[] basePackages = new String[] { "com.example.package1", "com.example.package2" };
        FrameworkAdapterContextCustomizer customizer = new FrameworkAdapterContextCustomizer(basePackages);
        DummyAnnotationConfigApplicationContext context = new DummyAnnotationConfigApplicationContext();
        MergedContextConfiguration mergedConfig = Mockito.mock(MergedContextConfiguration.class);
        customizer.customizeContext(context, mergedConfig);
        assertArrayEquals(basePackages, context.getScannedPackages());
    }

    @Test
    void testCustomizeContextWithNonAnnotationConfigApplicationContext() {
        String[] basePackages = new String[] { "com.example.package" };
        FrameworkAdapterContextCustomizer customizer = new FrameworkAdapterContextCustomizer(basePackages);
        ConfigurableApplicationContext context = Mockito.mock(ConfigurableApplicationContext.class);
        MergedContextConfiguration mergedConfig = Mockito.mock(MergedContextConfiguration.class);
        customizer.customizeContext(context, mergedConfig);
    }

    @Test
    void testEqualsHashCodeToString() {
        String[] packages1 = new String[] { "com.example.a", "com.example.b" };
        String[] packages2 = new String[] { "com.example.a", "com.example.b" };
        String[] packages3 = new String[] { "com.example.x" };
        FrameworkAdapterContextCustomizer customizer1 = new FrameworkAdapterContextCustomizer(packages1);
        FrameworkAdapterContextCustomizer customizer2 = new FrameworkAdapterContextCustomizer(packages2);
        FrameworkAdapterContextCustomizer customizer3 = new FrameworkAdapterContextCustomizer(packages3);
        assertEquals(customizer1, customizer2);
        assertEquals(customizer1.hashCode(), customizer2.hashCode());
        assertNotEquals(customizer1, customizer3);
        String expected = "FrameworkAdapterContextCustomizer{basePackages=" + Arrays.toString(packages1) + "}";
        assertEquals(expected, customizer1.toString());
    }
}