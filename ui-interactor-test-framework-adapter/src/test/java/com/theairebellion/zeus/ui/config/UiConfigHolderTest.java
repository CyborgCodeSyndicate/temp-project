package com.theairebellion.zeus.ui.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class UiConfigHolderTest {

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // Note: If the class has a private static field "config", we need to reset it
        // If your UiConfigHolder is similar to UiFrameworkConfigHolder, we can use this
        try {
            Field configField = UiConfigHolder.class.getDeclaredField("config");
            configField.setAccessible(true);
            configField.set(null, null);
        } catch (NoSuchFieldException e) {
            // It's okay if the field doesn't exist, we'll just assume it's different
        }
    }

    @Test
    void getUiConfig_ShouldReturnNonNullInstance() {
        // When
        UiConfig result = UiConfigHolder.getUiConfig();

        // Then
        assertNotNull(result, "Should return a UiConfig instance");
    }

    @Test
    void getUiConfig_MultipleCalls_ShouldReturnSameInstance() {
        // When
        UiConfig result1 = UiConfigHolder.getUiConfig();
        UiConfig result2 = UiConfigHolder.getUiConfig();

        // Then
        assertSame(result1, result2, "Multiple calls should return the same instance");
    }

    @Test
    void getUiConfig_Concurrently_ShouldReturnSameInstance() throws InterruptedException {
        // Create a reference to store the results from threads
        final UiConfig[] threadResult1 = new UiConfig[1];
        final UiConfig[] threadResult2 = new UiConfig[1];

        // Create two threads that will call getUiConfig() concurrently
        Thread thread1 = new Thread(() -> threadResult1[0] = UiConfigHolder.getUiConfig());
        Thread thread2 = new Thread(() -> threadResult2[0] = UiConfigHolder.getUiConfig());

        // Start both threads
        thread1.start();
        thread2.start();

        // Wait for both threads to complete
        thread1.join();
        thread2.join();

        // Then both threads should have the same instance
        assertNotNull(threadResult1[0], "Thread 1 should have received a config instance");
        assertNotNull(threadResult2[0], "Thread 2 should have received a config instance");
        assertSame(threadResult1[0], threadResult2[0], "Both threads should receive the same instance");
    }
}