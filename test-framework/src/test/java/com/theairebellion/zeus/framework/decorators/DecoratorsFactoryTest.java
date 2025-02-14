package com.theairebellion.zeus.framework.decorators;

import com.theairebellion.zeus.framework.decorators.mock.MockDecoratorNoMatchingConstructor;
import com.theairebellion.zeus.framework.decorators.mock.MockTarget;
import com.theairebellion.zeus.framework.decorators.mock.SubMockTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorsFactoryTest {

    DecoratorsFactory factory;

    @BeforeEach
    void setUp() {
        factory = new DecoratorsFactory();
    }

    @Test
    void testNullTargetReturnsNull() {
        Object result = factory.decorate(null, MockDecorator.class);
        assertNull(result);
    }

    @Test
    void testDecoratesInstanceWithDirectConstructor() {
        MockTarget target = new MockTarget();
        MockDecorator decorator = factory.decorate(target, MockDecorator.class);
        assertNotNull(decorator);
        assertSame(target, decorator.target);
    }

    @Test
    void testCachesDecoratedInstance() {
        MockTarget target = new MockTarget();
        MockDecorator first = factory.decorate(target, MockDecorator.class);
        MockDecorator second = factory.decorate(target, MockDecorator.class);
        assertSame(first, second);
    }

    @Test
    void testDecoratesInstanceWithSuperclassConstructor() {
        SubMockTarget target = new SubMockTarget();
        MockDecoratorForSuperclass decorator = factory.decorate(target, MockDecoratorForSuperclass.class);
        assertNotNull(decorator);
        assertSame(target, decorator.target);
    }

    @Test
    void testNoMatchingConstructorThrowsException() {
        MockTarget target = new MockTarget();
        assertThrows(
                IllegalStateException.class,
                () -> factory.decorate(target, MockDecoratorNoMatchingConstructor.class)
        );
    }

    record MockDecorator(MockTarget target) {
    }

    record MockDecoratorForSuperclass(MockTarget target) {
    }
}