package com.theairebellion.zeus.framework.decorators;

import com.theairebellion.zeus.framework.decorators.mock.MockDecoratorNoMatchingConstructor;
import com.theairebellion.zeus.framework.decorators.mock.MockTarget;
import com.theairebellion.zeus.framework.decorators.mock.SubMockTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DecoratorsFactory Tests")
class DecoratorsFactoryTest {

    private DecoratorsFactory factory;

    @BeforeEach
    void setUp() {
        factory = new DecoratorsFactory();
    }

    @Test
    @DisplayName("decorate() should return null when target is null")
    void testNullTargetReturnsNull() {
        // When
        Object result = factory.decorate(null, MockDecorator.class);

        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("decorate() should create decorator when target class matches constructor parameter")
    void testDecoratesInstanceWithDirectConstructor() {
        // Given
        MockTarget target = new MockTarget();

        // When
        MockDecorator decorator = factory.decorate(target, MockDecorator.class);

        // Then
        assertNotNull(decorator);
        assertSame(target, decorator.target());
    }

    @Test
    @DisplayName("decorate() should return cached decorator for same target")
    void testCachesDecoratedInstance() {
        // Given
        MockTarget target = new MockTarget();

        // When
        MockDecorator first = factory.decorate(target, MockDecorator.class);
        MockDecorator second = factory.decorate(target, MockDecorator.class);

        // Then
        assertSame(first, second);
    }

    @Test
    @DisplayName("decorate() should create new decorator for different target")
    void testDifferentTargetsGetDifferentDecorators() {
        // Given
        MockTarget target1 = new MockTarget();
        MockTarget target2 = new MockTarget();

        // When
        MockDecorator decorator1 = factory.decorate(target1, MockDecorator.class);
        MockDecorator decorator2 = factory.decorate(target2, MockDecorator.class);

        // Then
        assertNotNull(decorator1);
        assertNotNull(decorator2);
        assertNotSame(decorator1, decorator2);
    }

    @Test
    @DisplayName("decorate() should use constructor with superclass parameter when direct constructor not found")
    void testDecoratesInstanceWithSuperclassConstructor() {
        // Given
        SubMockTarget target = new SubMockTarget();

        // When
        MockDecoratorForSuperclass decorator = factory.decorate(target, MockDecoratorForSuperclass.class);

        // Then
        assertNotNull(decorator);
        assertSame(target, decorator.target());
    }

    @Test
    @DisplayName("decorate() should throw IllegalStateException when no matching constructor found")
    void testNoMatchingConstructorThrowsException() {
        // Given
        MockTarget target = new MockTarget();

        // When/Then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> factory.decorate(target, MockDecoratorNoMatchingConstructor.class)
        );

        // Verify exception message
        assertTrue(exception.getMessage().contains("Failed to create decorator"));
        assertTrue(exception.getMessage().contains("No matching constructor found"));
    }

    @Test
    @DisplayName("decorate() should throw IllegalStateException when constructor throws exception")
    void testConstructorExceptionThrowsIllegalStateException() {
        // Given
        MockTarget target = new MockTarget();

        // When/Then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> factory.decorate(target, FailingConstructorDecorator.class)
        );

        // Verify exception message
        assertTrue(exception.getMessage().contains("Failed to create decorator"));
    }

    @Test
    @DisplayName("decorate() should use correct cache for different decorator types")
    void testCacheForDifferentDecoratorTypes() {
        // Given
        MockTarget target = new MockTarget();

        // When
        MockDecorator decorator1 = factory.decorate(target, MockDecorator.class);
        AnotherMockDecorator decorator2 = factory.decorate(target, AnotherMockDecorator.class);

        // The second decoration should create a new instance since it's a different type

        // Then - we can examine the cache directly using reflection
        try {
            Field cacheField = DecoratorsFactory.class.getDeclaredField("cache");
            cacheField.setAccessible(true);
            Map<?, ?> cache = (Map<?, ?>) cacheField.get(factory);

            // Cache should only have one entry now (the latest one)
            assertEquals(1, cache.size());

            // The cached value should be the last decorator
            assertSame(decorator2, cache.get(target));

            // Check that retrieving the first decorator type again creates a new instance
            MockDecorator decorator1Again = factory.decorate(target, MockDecorator.class);
            assertNotSame(decorator1, decorator1Again);

        } catch (Exception e) {
            fail("Exception examining cache: " + e.getMessage());
        }
    }

    // Test decorator types
    record MockDecorator(MockTarget target) {}

    record MockDecoratorForSuperclass(MockTarget target) {}

    record AnotherMockDecorator(MockTarget target) {}

    static class FailingConstructorDecorator {
        public FailingConstructorDecorator(MockTarget target) {
            throw new RuntimeException("Simulated constructor failure");
        }
    }
}