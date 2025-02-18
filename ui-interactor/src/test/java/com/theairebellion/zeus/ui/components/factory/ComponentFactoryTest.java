package com.theairebellion.zeus.ui.components.factory;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ComponentFactoryTest {

    public interface DummyInterface extends ComponentType {
    }

    @ImplementationOfType("DUMMY")
    public record DummyImpl(SmartWebDriver driver) implements DummyInterface {
        @Override
        public Enum<?> getType() {
            return DummyComponentType.DUMMY;
        }
    }

    public enum DummyComponentType implements DummyInterface {
        DUMMY, FAIL, NON_EXISTENT;

        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    private SmartWebDriver driver;
    private String pkg;

    @BeforeEach
    public void setUp() {
        System.setProperty("ui.config.file", "ui-config");
        System.setProperty("wait.duration.in.seconds", "2");
        WebDriver original = mock(WebDriver.class);
        driver = new SmartWebDriver(original);
        pkg = ComponentFactoryTest.class.getPackage().getName();
    }

    @Test
    public void testSuccessfulInstanceCreation() throws Exception {
        Method method = ComponentFactory.class.getDeclaredMethod("getComponent", Class.class, ComponentType.class, String.class, SmartWebDriver.class);
        method.setAccessible(true);
        Object instance = method.invoke(null, DummyInterface.class, DummyComponentType.DUMMY, pkg, driver);
        assertInstanceOf(DummyImpl.class, instance);
        DummyImpl dummy = (DummyImpl) instance;
        assertEquals(driver, dummy.driver());
    }

    @Test
    public void testComponentNotFound() {
        AtomicReference<Method> methodRef = new AtomicReference<>();
        assertThrows(InvocationTargetException.class, () -> {
            methodRef.set(ComponentFactory.class.getDeclaredMethod("getComponent", Class.class, ComponentType.class, String.class, SmartWebDriver.class));
            methodRef.get().setAccessible(true);
            methodRef.get().invoke(null, DummyInterface.class, DummyComponentType.NON_EXISTENT, pkg, driver);
        });
    }

    @Test
    public void testCreateInstanceFailure() {
        AtomicReference<Method> methodRef = new AtomicReference<>();
        assertThrows(InvocationTargetException.class, () -> {
            methodRef.set(ComponentFactory.class.getDeclaredMethod("getComponent", Class.class, ComponentType.class, String.class, SmartWebDriver.class));
            methodRef.get().setAccessible(true);
            methodRef.get().invoke(null, DummyInterface.class, DummyComponentType.FAIL, pkg, driver);
        });
    }
}