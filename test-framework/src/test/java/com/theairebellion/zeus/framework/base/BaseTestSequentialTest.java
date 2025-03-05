package com.theairebellion.zeus.framework.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.jupiter.api.TestInstance;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BaseTestSequential Tests")
class BaseTestSequentialTest {

    @Spy
    @InjectMocks
    private BaseTestSequential baseTestSequential;

    @Mock
    private Services services;

    @Test
    @DisplayName("beforeAll() should call overridable beforeAll(Services)")
    void testBeforeAll() {
        // When
        baseTestSequential.beforeAll();

        // Then
        verify(baseTestSequential).beforeAll(services);
    }

    @Test
    @DisplayName("afterAll() should call overridable afterAll(Services)")
    void testAfterAll() {
        // When
        baseTestSequential.afterAll();

        // Then
        verify(baseTestSequential).afterAll(services);
    }

    @Test
    @DisplayName("Default beforeAll(Services) implementation should do nothing")
    void testBeforeAllWithServices() {
        // Create a real instance to test the default implementation
        BaseTestSequential realInstance = new BaseTestSequential();

        // When
        realInstance.beforeAll(services);

        // Then
        verifyNoInteractions(services);
    }

    @Test
    @DisplayName("Default afterAll(Services) implementation should do nothing")
    void testAfterAllWithServices() {
        // Create a real instance to test the default implementation
        BaseTestSequential realInstance = new BaseTestSequential();

        // When
        realInstance.afterAll(services);

        // Then
        verifyNoInteractions(services);
    }

    @Test
    @DisplayName("Should have @Component annotation")
    void shouldHaveComponentAnnotation() {
        // When
        Component annotation = BaseTestSequential.class.getAnnotation(Component.class);

        // Then
        assertNotNull(annotation, "Should have @Component annotation");
    }

    @Test
    @DisplayName("Should have @TestInstance(PER_CLASS) annotation")
    void shouldHaveTestInstanceAnnotation() {
        // When
        TestInstance annotation = BaseTestSequential.class.getAnnotation(TestInstance.class);

        // Then
        assertNotNull(annotation, "Should have @TestInstance annotation");
        assertEquals(TestInstance.Lifecycle.PER_CLASS, annotation.value(),
                "Should use PER_CLASS lifecycle");
    }

    @Test
    @DisplayName("Should have @DirtiesContext(AFTER_CLASS) annotation")
    void shouldHaveDirtiesContextAnnotation() {
        // When
        DirtiesContext annotation = BaseTestSequential.class.getAnnotation(DirtiesContext.class);

        // Then
        assertNotNull(annotation, "Should have @DirtiesContext annotation");
        assertEquals(DirtiesContext.ClassMode.AFTER_CLASS, annotation.classMode(),
                "Should use AFTER_CLASS mode");
    }

    @Test
    @DisplayName("Should extend BaseTest")
    void shouldExtendBaseTest() {
        // Then
        assertTrue(BaseTest.class.isAssignableFrom(BaseTestSequential.class),
                "Should extend BaseTest");
    }

    @Test
    @DisplayName("beforeAll and afterAll methods should be correctly annotated with @AfterAll")
    void methodsShouldHaveCorrectAnnotations() throws NoSuchMethodException {
        // When
        Annotation beforeAllAnnotation = BaseTestSequential.class
                .getDeclaredMethod("beforeAll")
                .getAnnotation(org.junit.jupiter.api.AfterAll.class);

        Annotation afterAllAnnotation = BaseTestSequential.class
                .getDeclaredMethod("afterAll")
                .getAnnotation(org.junit.jupiter.api.AfterAll.class);

        // Then
        assertNotNull(beforeAllAnnotation, "beforeAll should have @AfterAll annotation");
        assertNotNull(afterAllAnnotation, "afterAll should have @AfterAll annotation");
    }

    @Test
    @DisplayName("Base implementations should have final modifier")
    void baseMethods_ShouldBeFinal() throws NoSuchMethodException {
        // When/Then
        assertTrue(java.lang.reflect.Modifier.isFinal(
                        BaseTestSequential.class.getDeclaredMethod("beforeAll").getModifiers()),
                "beforeAll() should be final");

        assertTrue(java.lang.reflect.Modifier.isFinal(
                        BaseTestSequential.class.getDeclaredMethod("afterAll").getModifiers()),
                "afterAll() should be final");
    }
}