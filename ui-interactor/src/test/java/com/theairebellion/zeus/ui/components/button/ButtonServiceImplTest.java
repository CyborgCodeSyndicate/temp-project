package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.button.mock.MockButtonComponentType;
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

class ButtonServiceImplTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private ButtonServiceImpl service;
    private SmartWebElement container;
    private Button buttonMock;
    private MockButtonComponentType mockButtonComponentType;
    private By locator;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeEach
    public void setUp() {
        driver = mock(SmartWebDriver.class);
        service = new ButtonServiceImpl(driver);
        container = mock(SmartWebElement.class);
        buttonMock = mock(Button.class);
        mockButtonComponentType = MockButtonComponentType.DUMMY;
        locator = By.id("button");
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getButtonComponent(eq(mockButtonComponentType), eq(driver)))
                .thenReturn(buttonMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testClickContainerAndText() {
        service.click(mockButtonComponentType, container, "ClickMe");
        verify(buttonMock).click(container, "ClickMe");
    }

    @Test
    public void testClickContainer() {
        service.click(mockButtonComponentType, container);
        verify(buttonMock).click(container);
    }

    @Test
    public void testClickString() {
        service.click(mockButtonComponentType, "ClickMe");
        verify(buttonMock).click("ClickMe");
    }

    @Test
    public void testClickLocator() {
        service.click(mockButtonComponentType, locator);
        verify(buttonMock).click(locator);
    }

    @Test
    public void testIsEnabledContainerAndText() {
        when(buttonMock.isEnabled(container, "EnableMe")).thenReturn(true);
        boolean result = service.isEnabled(mockButtonComponentType, container, "EnableMe");
        assertTrue(result);
        verify(buttonMock).isEnabled(container, "EnableMe");
    }

    @Test
    public void testIsEnabledContainer() {
        when(buttonMock.isEnabled(container)).thenReturn(true);
        boolean result = service.isEnabled(mockButtonComponentType, container);
        assertTrue(result);
        verify(buttonMock).isEnabled(container);
    }

    @Test
    public void testIsEnabledString() {
        when(buttonMock.isEnabled("EnableMe")).thenReturn(true);
        boolean result = service.isEnabled(mockButtonComponentType, "EnableMe");
        assertTrue(result);
        verify(buttonMock).isEnabled("EnableMe");
    }

    @Test
    public void testIsEnabledLocator() {
        when(buttonMock.isEnabled(locator)).thenReturn(true);
        boolean result = service.isEnabled(mockButtonComponentType, locator);
        assertTrue(result);
        verify(buttonMock).isEnabled(locator);
    }

    @Test
    public void testIsVisibleContainerAndText() {
        when(buttonMock.isVisible(container, "VisibleMe")).thenReturn(true);
        boolean result = service.isVisible(mockButtonComponentType, container, "VisibleMe");
        assertTrue(result);
        verify(buttonMock).isVisible(container, "VisibleMe");
    }

    @Test
    public void testIsVisibleContainer() {
        when(buttonMock.isVisible(container)).thenReturn(true);
        boolean result = service.isVisible(mockButtonComponentType, container);
        assertTrue(result);
        verify(buttonMock).isVisible(container);
    }

    @Test
    public void testIsVisibleString() {
        when(buttonMock.isVisible("VisibleMe")).thenReturn(true);
        boolean result = service.isVisible(mockButtonComponentType, "VisibleMe");
        assertTrue(result);
        verify(buttonMock).isVisible("VisibleMe");
    }

    @Test
    public void testIsVisibleLocator() {
        when(buttonMock.isVisible(locator)).thenReturn(true);
        boolean result = service.isVisible(mockButtonComponentType, locator);
        assertTrue(result);
        verify(buttonMock).isVisible(locator);
    }

    @Test
    public void testClickDefaultOverloadString() {
        service.click("DefaultClick");
        verify(buttonMock).click("DefaultClick");
    }

    @Test
    public void testClickDefaultOverloadContainer() {
        service.click(container);
        verify(buttonMock).click(container);
    }

    @Test
    public void testClickDefaultOverloadLocator() {
        service.click(locator);
        verify(buttonMock).click(locator);
    }

    @Test
    public void testIsEnabledDefaultOverloadString() {
        when(buttonMock.isEnabled("DefaultEnable")).thenReturn(true);
        boolean result = service.isEnabled("DefaultEnable");
        assertTrue(result);
        verify(buttonMock).isEnabled("DefaultEnable");
    }

    @Test
    public void testIsEnabledDefaultOverloadContainer() {
        when(buttonMock.isEnabled(container)).thenReturn(true);
        boolean result = service.isEnabled(container);
        assertTrue(result);
        verify(buttonMock).isEnabled(container);
    }

    @Test
    public void testIsEnabledDefaultOverloadLocator() {
        when(buttonMock.isEnabled(locator)).thenReturn(true);
        boolean result = service.isEnabled(locator);
        assertTrue(result);
        verify(buttonMock).isEnabled(locator);
    }

    @Test
    public void testIsVisibleDefaultOverloadString() {
        when(buttonMock.isVisible("DefaultVisible")).thenReturn(true);
        boolean result = service.isVisible("DefaultVisible");
        assertTrue(result);
        verify(buttonMock).isVisible("DefaultVisible");
    }

    @Test
    public void testIsVisibleDefaultOverloadContainer() {
        when(buttonMock.isVisible(container)).thenReturn(true);
        boolean result = service.isVisible(container);
        assertTrue(result);
        verify(buttonMock).isVisible(container);
    }

    @Test
    public void testIsVisibleDefaultOverloadLocator() {
        when(buttonMock.isVisible(locator)).thenReturn(true);
        boolean result = service.isVisible(locator);
        assertTrue(result);
        verify(buttonMock).isVisible(locator);
    }

    @Test
    public void testComponentCaching() {
        service.click(mockButtonComponentType, container, "ClickMe");
        service.isEnabled(mockButtonComponentType, container, "EnableMe");
        service.isVisible(mockButtonComponentType, container, "VisibleMe");
        factoryMock.verify(() -> ComponentFactory.getButtonComponent(eq(mockButtonComponentType), eq(driver)), times(1));
    }
}