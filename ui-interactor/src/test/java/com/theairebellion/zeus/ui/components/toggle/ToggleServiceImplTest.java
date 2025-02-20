package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.toggle.mock.MockToggleComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.aeonbits.owner.ConfigCache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ToggleServiceImplTest {

    private SmartWebDriver driver;
    private ToggleServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private Toggle toggleMock;
    private MockToggleComponentType mockToggleComponentType;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeAll
    static void globalSetup() {
        System.setProperty("project.package", "com.theairebellion.zeus");
        System.setProperty("toggle.default.type", "DUMMY");
        System.setProperty("wait.duration.in.seconds", "2");

        ConfigCache.clear();
    }

    @BeforeEach
    public void setUp() {
        driver = mock(SmartWebDriver.class);
        service = new ToggleServiceImpl(driver);
        container = mock(SmartWebElement.class);
        locator = By.id("toggle");
        toggleMock = mock(Toggle.class);
        mockToggleComponentType = MockToggleComponentType.DUMMY;
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getToggleComponent(eq(mockToggleComponentType), eq(driver)))
                .thenReturn(toggleMock);
    }

    @AfterEach
    public void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Test
    public void testActivateContainerAndText() {
        service.activate(mockToggleComponentType, container, "On");
        verify(toggleMock).activate(container, "On");
    }

    @Test
    public void testActivateTextOnly() {
        service.activate(mockToggleComponentType, "On");
        verify(toggleMock).activate("On");
    }

    @Test
    public void testActivateLocator() {
        service.activate(mockToggleComponentType, locator);
        verify(toggleMock).activate(locator);
    }

    @Test
    public void testDeactivateContainerAndText() {
        service.deactivate(mockToggleComponentType, container, "Off");
        verify(toggleMock).deactivate(container, "Off");
    }

    @Test
    public void testDeactivateTextOnly() {
        service.deactivate(mockToggleComponentType, "Off");
        verify(toggleMock).deactivate("Off");
    }

    @Test
    public void testDeactivateLocator() {
        service.deactivate(mockToggleComponentType, locator);
        verify(toggleMock).deactivate(locator);
    }

    @Test
    public void testIsEnabledContainerAndText() {
        when(toggleMock.isEnabled(container, "On")).thenReturn(true);
        assertTrue(service.isEnabled(mockToggleComponentType, container, "On"));
        verify(toggleMock).isEnabled(container, "On");
    }

    @Test
    public void testIsEnabledTextOnly() {
        when(toggleMock.isEnabled("On")).thenReturn(true);
        assertTrue(service.isEnabled(mockToggleComponentType, "On"));
        verify(toggleMock).isEnabled("On");
    }

    @Test
    public void testIsEnabledLocator() {
        when(toggleMock.isEnabled(locator)).thenReturn(true);
        assertTrue(service.isEnabled(mockToggleComponentType, locator));
        verify(toggleMock).isEnabled(locator);
    }

    @Test
    public void testIsActivatedContainerAndText() {
        when(toggleMock.isActivated(container, "On")).thenReturn(true);
        assertTrue(service.isActivated(mockToggleComponentType, container, "On"));
        verify(toggleMock).isActivated(container, "On");
    }

    @Test
    public void testIsActivatedTextOnly() {
        when(toggleMock.isActivated("On")).thenReturn(true);
        assertTrue(service.isActivated(mockToggleComponentType, "On"));
        verify(toggleMock).isActivated("On");
    }

    @Test
    public void testIsActivatedLocator() {
        when(toggleMock.isActivated(locator)).thenReturn(true);
        assertTrue(service.isActivated(mockToggleComponentType, locator));
        verify(toggleMock).isActivated(locator);
    }

    @Test
    public void testComponentCaching() {
        service.activate(mockToggleComponentType, container, "On");
        service.deactivate(mockToggleComponentType, container, "Off");
        service.isEnabled(mockToggleComponentType, container, "On");
        factoryMock.verify(() -> ComponentFactory.getToggleComponent(eq(mockToggleComponentType), eq(driver)), times(1));
    }
}