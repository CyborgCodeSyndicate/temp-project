package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ToggleServiceImplTest {

    private SmartWebDriver driver;
    private ToggleServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private Toggle toggleMock;
    private DummyToggleComponentType dummyType;
    private MockedStatic<ComponentFactory> factoryMock;

    public enum DummyToggleComponentType implements ToggleComponentType {
        DUMMY;
        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    @BeforeEach
    public void setUp() {
        System.setProperty("project.package", "com.theairebellion.zeus");
        System.setProperty("toggle.default.type", "DUMMY");
        driver = mock(SmartWebDriver.class);
        service = new ToggleServiceImpl(driver);
        container = mock(SmartWebElement.class);
        locator = By.id("toggle");
        toggleMock = mock(Toggle.class);
        dummyType = DummyToggleComponentType.DUMMY;
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getToggleComponent(eq(dummyType), eq(driver)))
                .thenReturn(toggleMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testActivateContainerAndText() {
        service.activate(dummyType, container, "On");
        verify(toggleMock).activate(container, "On");
    }

    @Test
    public void testActivateTextOnly() {
        service.activate(dummyType, "On");
        verify(toggleMock).activate("On");
    }

    @Test
    public void testActivateLocator() {
        service.activate(dummyType, locator);
        verify(toggleMock).activate(locator);
    }

    @Test
    public void testDeactivateContainerAndText() {
        service.deactivate(dummyType, container, "Off");
        verify(toggleMock).deactivate(container, "Off");
    }

    @Test
    public void testDeactivateTextOnly() {
        service.deactivate(dummyType, "Off");
        verify(toggleMock).deactivate("Off");
    }

    @Test
    public void testDeactivateLocator() {
        service.deactivate(dummyType, locator);
        verify(toggleMock).deactivate(locator);
    }

    @Test
    public void testIsEnabledContainerAndText() {
        when(toggleMock.isEnabled(container, "On")).thenReturn(true);
        assertTrue(service.isEnabled(dummyType, container, "On"));
        verify(toggleMock).isEnabled(container, "On");
    }

    @Test
    public void testIsEnabledTextOnly() {
        when(toggleMock.isEnabled("On")).thenReturn(true);
        assertTrue(service.isEnabled(dummyType, "On"));
        verify(toggleMock).isEnabled("On");
    }

    @Test
    public void testIsEnabledLocator() {
        when(toggleMock.isEnabled(locator)).thenReturn(true);
        assertTrue(service.isEnabled(dummyType, locator));
        verify(toggleMock).isEnabled(locator);
    }

    @Test
    public void testIsActivatedContainerAndText() {
        when(toggleMock.isActivated(container, "On")).thenReturn(true);
        assertTrue(service.isActivated(dummyType, container, "On"));
        verify(toggleMock).isActivated(container, "On");
    }

    @Test
    public void testIsActivatedTextOnly() {
        when(toggleMock.isActivated("On")).thenReturn(true);
        assertTrue(service.isActivated(dummyType, "On"));
        verify(toggleMock).isActivated("On");
    }

    @Test
    public void testIsActivatedLocator() {
        when(toggleMock.isActivated(locator)).thenReturn(true);
        assertTrue(service.isActivated(dummyType, locator));
        verify(toggleMock).isActivated(locator);
    }

    @Test
    public void testComponentCaching() {
        service.activate(dummyType, container, "On");
        service.deactivate(dummyType, container, "Off");
        service.isEnabled(dummyType, container, "On");
        factoryMock.verify(() -> ComponentFactory.getToggleComponent(eq(dummyType), eq(driver)), times(1));
    }
}