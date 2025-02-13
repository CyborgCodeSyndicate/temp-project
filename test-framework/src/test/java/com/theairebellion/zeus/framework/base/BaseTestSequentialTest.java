package com.theairebellion.zeus.framework.base;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Lazy;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseTestSequentialTest {

    @InjectMocks
    private BaseTestSequential baseTestSequential;

    @Mock
    @Lazy
    private Services services;

    @BeforeAll
    static void setUp() {
        System.setProperty("spring.main.allow-bean-definition-overriding", "true");
    }

    @Test
    void testBeforeAll() {
        baseTestSequential.beforeAll();
        verifyNoInteractions(services);
    }

    @Test
    void testAfterAll() {
        baseTestSequential.afterAll();
        verifyNoInteractions(services);
    }

    @Test
    void testBeforeAllWithServices() {
        baseTestSequential.beforeAll(services);
        verifyNoInteractions(services);
    }

    @Test
    void testAfterAllWithServices() {
        baseTestSequential.afterAll(services);
        verifyNoInteractions(services);
    }
}
