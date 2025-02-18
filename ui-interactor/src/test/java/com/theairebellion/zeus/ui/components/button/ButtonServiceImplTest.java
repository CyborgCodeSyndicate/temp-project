package com.theairebellion.zeus.ui.components.button;

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

public class ButtonServiceImplTest {

    static {
        System.setProperty("project.package", "com.theairebellion.zeus");
        System.setProperty("button.default.type", "DUMMY");
    }

    private SmartWebDriver driver;
    private ButtonServiceImpl service;
    private SmartWebElement container;
    private Button buttonMock;
    private DummyButtonComponentType dummyType;
    private By locator;
    private MockedStatic<ComponentFactory> factoryMock;

    public enum DummyButtonComponentType implements ButtonComponentType {
        DUMMY;
        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    @BeforeEach
    public void setUp() {
        driver = mock(SmartWebDriver.class);
        service = new ButtonServiceImpl(driver);
        container = mock(SmartWebElement.class);
        buttonMock = mock(Button.class);
        dummyType = DummyButtonComponentType.DUMMY;
        locator = By.id("button");
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getButtonComponent(eq(dummyType), eq(driver)))
                .thenReturn(buttonMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testClickContainerAndText() {
        service.click(dummyType, container, "ClickMe");
        verify(buttonMock).click(container, "ClickMe");
    }

    @Test
    public void testClickContainer() {
        service.click(dummyType, container);
        verify(buttonMock).click(container);
    }

    @Test
    public void testClickString() {
        service.click(dummyType, "ClickMe");
        verify(buttonMock).click("ClickMe");
    }

    @Test
    public void testClickLocator() {
        service.click(dummyType, locator);
        verify(buttonMock).click(locator);
    }

    @Test
    public void testIsEnabledContainerAndText() {
        when(buttonMock.isEnabled(container, "EnableMe")).thenReturn(true);
        boolean result = service.isEnabled(dummyType, container, "EnableMe");
        assertTrue(result);
        verify(buttonMock).isEnabled(container, "EnableMe");
    }

    @Test
    public void testIsEnabledContainer() {
        when(buttonMock.isEnabled(container)).thenReturn(true);
        boolean result = service.isEnabled(dummyType, container);
        assertTrue(result);
        verify(buttonMock).isEnabled(container);
    }

    @Test
    public void testIsEnabledString() {
        when(buttonMock.isEnabled("EnableMe")).thenReturn(true);
        boolean result = service.isEnabled(dummyType, "EnableMe");
        assertTrue(result);
        verify(buttonMock).isEnabled("EnableMe");
    }

    @Test
    public void testIsEnabledLocator() {
        when(buttonMock.isEnabled(locator)).thenReturn(true);
        boolean result = service.isEnabled(dummyType, locator);
        assertTrue(result);
        verify(buttonMock).isEnabled(locator);
    }

    @Test
    public void testIsVisibleContainerAndText() {
        when(buttonMock.isVisible(container, "VisibleMe")).thenReturn(true);
        boolean result = service.isVisible(dummyType, container, "VisibleMe");
        assertTrue(result);
        verify(buttonMock).isVisible(container, "VisibleMe");
    }

    @Test
    public void testIsVisibleContainer() {
        when(buttonMock.isVisible(container)).thenReturn(true);
        boolean result = service.isVisible(dummyType, container);
        assertTrue(result);
        verify(buttonMock).isVisible(container);
    }

    @Test
    public void testIsVisibleString() {
        when(buttonMock.isVisible("VisibleMe")).thenReturn(true);
        boolean result = service.isVisible(dummyType, "VisibleMe");
        assertTrue(result);
        verify(buttonMock).isVisible("VisibleMe");
    }

    @Test
    public void testIsVisibleLocator() {
        when(buttonMock.isVisible(locator)).thenReturn(true);
        boolean result = service.isVisible(dummyType, locator);
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
        service.click(dummyType, container, "ClickMe");
        service.isEnabled(dummyType, container, "EnableMe");
        service.isVisible(dummyType, container, "VisibleMe");
        factoryMock.verify(() -> ComponentFactory.getButtonComponent(eq(dummyType), eq(driver)), times(1));
    }
}