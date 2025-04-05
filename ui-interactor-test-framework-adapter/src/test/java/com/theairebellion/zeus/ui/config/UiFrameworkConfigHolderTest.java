package com.theairebellion.zeus.ui.config;

import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UiFrameworkConfigHolderTest {

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // Reset the config field to null before each test
        Field configField = UiFrameworkConfigHolder.class.getDeclaredField("config");
        configField.setAccessible(true);
        configField.set(null, null);
    }

    @Test
    void getUiFrameworkConfig_FirstCall_ShouldCreateNewInstance() {
        // Given
        UIFrameworkConfig mockConfig = mock(UIFrameworkConfig.class);

        try (MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class)) {
            // Setup the mocked static method
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(UIFrameworkConfig.class))
                    .thenReturn(mockConfig);

            // When
            UIFrameworkConfig result = UiFrameworkConfigHolder.getUiFrameworkConfig();

            // Then
            assertSame(mockConfig, result, "Should return the mocked config");

            // Verify the ConfigCache was called
            mockedConfigCache.verify(() -> ConfigCache.getOrCreate(UIFrameworkConfig.class));
        }
    }

    @Test
    void getUiFrameworkConfig_SubsequentCalls_ShouldReturnCachedInstance() throws Exception {
        // Given
        UIFrameworkConfig mockConfig = mock(UIFrameworkConfig.class);

        try (MockedStatic<ConfigCache> mockedConfigCache = mockStatic(ConfigCache.class)) {
            // Setup the mocked static method to return our mock
            mockedConfigCache.when(() -> ConfigCache.getOrCreate(UIFrameworkConfig.class))
                    .thenReturn(mockConfig);

            // When - first call should use ConfigCache
            UIFrameworkConfig firstResult = UiFrameworkConfigHolder.getUiFrameworkConfig();

            // Reset the mock counting to verify the second call doesn't use ConfigCache
            mockedConfigCache.reset();

            // When - second call should use cached instance
            UIFrameworkConfig secondResult = UiFrameworkConfigHolder.getUiFrameworkConfig();

            // Then
            assertSame(firstResult, secondResult, "Subsequent calls should return the same instance");

            // Verify ConfigCache was not called on the second invocation
            mockedConfigCache.verify(() -> ConfigCache.getOrCreate(UIFrameworkConfig.class), never());
        }
    }

    @Test
    void getUiFrameworkConfig_MultithreadedAccess_ShouldReturnSameInstance() throws Exception {
        // This tests that even when called from multiple threads, we get the same instance

        // Create a reference to store the results from threads
        final UIFrameworkConfig[] threadResult1 = new UIFrameworkConfig[1];
        final UIFrameworkConfig[] threadResult2 = new UIFrameworkConfig[1];

        // Create two threads that will call getUiFrameworkConfig() concurrently
        Thread thread1 = new Thread(() -> threadResult1[0] = UiFrameworkConfigHolder.getUiFrameworkConfig());
        Thread thread2 = new Thread(() -> threadResult2[0] = UiFrameworkConfigHolder.getUiFrameworkConfig());

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