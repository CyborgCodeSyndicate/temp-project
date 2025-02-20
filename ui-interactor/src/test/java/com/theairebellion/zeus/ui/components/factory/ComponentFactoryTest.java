package com.theairebellion.zeus.ui.components.factory;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.mock.MockComponentType;
import com.theairebellion.zeus.ui.components.factory.mock.MockImpl;
import com.theairebellion.zeus.ui.components.factory.mock.MockInterface;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ComponentFactoryTest {

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
        Object instance = method.invoke(null, MockInterface.class, MockComponentType.DUMMY, pkg, driver);
        assertInstanceOf(MockImpl.class, instance);
        MockImpl dummy = (MockImpl) instance;
        assertEquals(driver, dummy.driver());
    }

    @Test
    public void testComponentNotFound() {
        AtomicReference<Method> methodRef = new AtomicReference<>();
        assertThrows(InvocationTargetException.class, () -> {
            methodRef.set(ComponentFactory.class.getDeclaredMethod("getComponent", Class.class, ComponentType.class, String.class, SmartWebDriver.class));
            methodRef.get().setAccessible(true);
            methodRef.get().invoke(null, MockInterface.class, MockComponentType.NON_EXISTENT, pkg, driver);
        });
    }

    @Test
    public void testCreateInstanceFailure() {
        AtomicReference<Method> methodRef = new AtomicReference<>();
        assertThrows(InvocationTargetException.class, () -> {
            methodRef.set(ComponentFactory.class.getDeclaredMethod("getComponent", Class.class, ComponentType.class, String.class, SmartWebDriver.class));
            methodRef.get().setAccessible(true);
            methodRef.get().invoke(null, MockInterface.class, MockComponentType.FAIL, pkg, driver);
        });
    }
}