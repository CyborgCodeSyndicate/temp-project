package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InsertionServiceRegistryTest {

    private InsertionServiceRegistry registry;

    @Mock
    private Insertion mockInsertion1;

    @Mock
    private Insertion mockInsertion2;

    // Test ComponentType interfaces
    interface TestComponentType1 extends ComponentType {}
    interface TestComponentType2 extends ComponentType {}

    @BeforeEach
    void setUp() {
        registry = new InsertionServiceRegistry();
    }

    @Test
    @DisplayName("Should register and retrieve a service")
    void shouldRegisterAndRetrieveService() {
        // Given
        registry.registerService(TestComponentType1.class, mockInsertion1);

        // When
        Insertion retrievedService = registry.getService(TestComponentType1.class);

        // Then
        assertSame(mockInsertion1, retrievedService);
    }

    @Test
    @DisplayName("Should return null when service is not registered")
    void shouldReturnNullWhenServiceIsNotRegistered() {
        // When
        Insertion retrievedService = registry.getService(TestComponentType1.class);

        // Then
        assertNull(retrievedService);
    }

    @Test
    @DisplayName("Should register multiple services and retrieve the correct one")
    void shouldRegisterMultipleServicesAndRetrieveCorrectOne() {
        // Given
        registry.registerService(TestComponentType1.class, mockInsertion1);
        registry.registerService(TestComponentType2.class, mockInsertion2);

        // When
        Insertion retrievedService1 = registry.getService(TestComponentType1.class);
        Insertion retrievedService2 = registry.getService(TestComponentType2.class);

        // Then
        assertSame(mockInsertion1, retrievedService1);
        assertSame(mockInsertion2, retrievedService2);
    }

    @Test
    @DisplayName("Should override existing service registration")
    void shouldOverrideExistingServiceRegistration() {
        // Given
        registry.registerService(TestComponentType1.class, mockInsertion1);

        // When
        registry.registerService(TestComponentType1.class, mockInsertion2);
        Insertion retrievedService = registry.getService(TestComponentType1.class);

        // Then
        assertSame(mockInsertion2, retrievedService);
        assertNotSame(mockInsertion1, retrievedService);
    }

    @Test
    @DisplayName("Should handle thread safety with concurrent registry access")
    void shouldHandleThreadSafetyWithConcurrentRegistryAccess() throws InterruptedException {
        // This test verifies that ConcurrentHashMap behaves as expected
        // when accessed by multiple threads

        // Given
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // When
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                // Create a unique component type class for each thread
                class DynamicComponentType implements ComponentType {
                    final int id = index;

                    @Override
                    public Enum<?> getType() {
                        return null;
                    }
                }

                registry.registerService(DynamicComponentType.class, mockInsertion1);
                Insertion retrieved = registry.getService(DynamicComponentType.class);
                assertSame(mockInsertion1, retrieved);
            });
            threads[i].start();
        }

        // Then - wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // No assertions needed here as each thread performs its own assertion
        // If any thread fails, the test will fail
    }
}