package com.theairebellion.zeus.framework.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

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
    void testBeforeAll_ShouldCallOverridableBeforeAll() {
        // When
        baseTestSequential.beforeAll();

        // Then
        verify(baseTestSequential).beforeAll(services);
    }

    @Test
    @DisplayName("afterAll() should call overridable afterAll(Services)")
    void testAfterAll_ShouldCallOverridableAfterAll() {
        // When
        baseTestSequential.afterAll();

        // Then
        verify(baseTestSequential).afterAll(services);
    }
}