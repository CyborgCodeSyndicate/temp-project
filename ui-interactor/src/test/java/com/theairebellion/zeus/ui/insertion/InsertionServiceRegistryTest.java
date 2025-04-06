package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InsertionServiceRegistryTest {

    private InsertionServiceRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new InsertionServiceRegistry();
    }

    @Nested
    @DisplayName("Method: registerService(Class<? extends ComponentType>, Insertion)")
    class RegisterServiceMethodTest {

        @Test
        @DisplayName("Given a new service is registered, then getService returns it")
        void whenNewServiceRegistered_thenItIsAvailable() {
            // Arrange
            Class<? extends ComponentType> typeKey = MockedComponentType.class;
            Insertion mockedService = Mockito.mock(Insertion.class);

            // Act
            registry.registerService(typeKey, mockedService);

            // Assert
            assertSame(mockedService, registry.getService(typeKey),
                "Expected the registry to return the same instance that was registered");
        }

        @Test
        @DisplayName("Given a service is re-registered, then the latest registration overwrites the old one")
        void whenServiceIsReRegistered_thenItOverwrites() {
            // Arrange
            Class<? extends ComponentType> typeKey = MockedComponentType.class;
            Insertion firstService = Mockito.mock(Insertion.class);
            Insertion secondService = Mockito.mock(Insertion.class);

            // Register first
            registry.registerService(typeKey, firstService);
            // Register second (same key)
            registry.registerService(typeKey, secondService);

            // Assert
            assertSame(secondService, registry.getService(typeKey),
                "Expected the second registration to overwrite the first one");
        }

        @Test
        @DisplayName("Given a null service is registered, then a NullPointerException is thrown")
        void whenNullServiceRegistered_thenThrowsNullPointerException() {
            // Arrange
            Class<? extends ComponentType> typeKey = MockedComponentType.class;

            // Act & Assert
            assertThrows(NullPointerException.class, () -> registry.registerService(typeKey, null));
        }
    }

    @Nested
    @DisplayName("Method: getService(Class<? extends ComponentType>)")
    class GetServiceMethodTest {

        @Test
        @DisplayName("Given no service was registered for a type, then getService returns null")
        void whenNoServiceFound_thenReturnNull() {
            // Arrange
            Class<? extends ComponentType> unknownType = AnotherMockedComponentType.class;

            // Act
            Insertion result = registry.getService(unknownType);

            // Assert
            assertNull(result, "Expected null for an unregistered type");
        }
    }

    @Nested
    @DisplayName("Concurrency Tests")
    class ConcurrencyTests {

        @Test
        @DisplayName("Multiple threads can register and retrieve services without errors")
        void testConcurrentRegistration() throws InterruptedException {
            // We have an array of 10 unique component type classes
            Class<? extends ComponentType>[] uniqueTypes = new Class[]{
                UniqueType1.class,
                UniqueType2.class,
                UniqueType3.class,
                UniqueType4.class,
                UniqueType5.class,
                UniqueType6.class,
                UniqueType7.class,
                UniqueType8.class,
                UniqueType9.class,
                UniqueType10.class
            };

            int threadCount = uniqueTypes.length;
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(threadCount);

            // To store the insertion services that each thread registers
            AtomicReferenceArray<Insertion> insertedServices = new AtomicReferenceArray<>(threadCount);

            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                new Thread(() -> {
                    try {
                        startLatch.await(); // Wait for all threads to be ready
                        Insertion insertionService = Mockito.mock(Insertion.class);
                        insertedServices.set(index, insertionService);
                        registry.registerService(uniqueTypes[index], insertionService);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        doneLatch.countDown();
                    }
                }).start();
            }

            startLatch.countDown(); // Release all threads
            doneLatch.await(); // Wait for all threads to finish

            // Verify all registrations exist and match what was inserted
            for (int i = 0; i < threadCount; i++) {
                assertSame(insertedServices.get(i), registry.getService(uniqueTypes[i]),
                    "Expected non-null insertion after concurrent registration for " + uniqueTypes[i].getSimpleName());
            }
        }
    }

    // Simple fake classes or interfaces for the test
    interface MockedComponentType extends ComponentType {
    }

    interface AnotherMockedComponentType extends ComponentType {
    }

    // A dynamic class that pretends to be a ComponentType
    static class DynamicMockedComponentType implements ComponentType {

        private final int id;

        DynamicMockedComponentType(int id) {
            this.id = id;
        }

        @Override
        public Enum<?> getType() {
            return null; // Not important for this test
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DynamicMockedComponentType)) return false;
            DynamicMockedComponentType that = (DynamicMockedComponentType) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }
    }

    static class UniqueType1 implements ComponentType {
        @Override
        public Enum<?> getType() {
            return null;
        }
    }

    static class UniqueType2 implements ComponentType {
        @Override
        public Enum<?> getType() {
            return null;
        }
    }

    static class UniqueType3 implements ComponentType {
        @Override
        public Enum<?> getType() {
            return null;
        }
    }

    static class UniqueType4 implements ComponentType {
        @Override
        public Enum<?> getType() {
            return null;
        }
    }

    static class UniqueType5 implements ComponentType {
        @Override
        public Enum<?> getType() {
            return null;
        }
    }

    static class UniqueType6 implements ComponentType {
        @Override
        public Enum<?> getType() {
            return null;
        }
    }

    static class UniqueType7 implements ComponentType {
        @Override
        public Enum<?> getType() {
            return null;
        }
    }

    static class UniqueType8 implements ComponentType {
        @Override
        public Enum<?> getType() {
            return null;
        }
    }

    static class UniqueType9 implements ComponentType {
        @Override
        public Enum<?> getType() {
            return null;
        }
    }

    static class UniqueType10 implements ComponentType {
        @Override
        public Enum<?> getType() {
            return null;
        }
    }
}