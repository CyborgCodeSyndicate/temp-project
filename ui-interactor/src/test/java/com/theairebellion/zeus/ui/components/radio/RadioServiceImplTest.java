package com.theairebellion.zeus.ui.components.radio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.radio.mock.MockRadioComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

class RadioServiceImplTest {

    private SmartWebDriver driver;
    private RadioServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private Strategy strategy;
    private Radio radioMock;
    private MockRadioComponentType mockRadioComponentType;
    private MockedStatic<ComponentFactory> factoryMock;



    @BeforeEach
    public void setUp() {
        System.setProperty("project.package", "com.theairebellion.zeus");
        System.setProperty("radio.default.type", "DUMMY");
        driver = mock(SmartWebDriver.class);
        service = new RadioServiceImpl(driver);
        container = mock(SmartWebElement.class);
        locator = By.id("radio");
        strategy = mock(Strategy.class);
        radioMock = mock(Radio.class);
        mockRadioComponentType = MockRadioComponentType.DUMMY;
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getRadioComponent(eq(mockRadioComponentType), eq(driver)))
                .thenReturn(radioMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testSelectContainerText() {
        service.select(mockRadioComponentType, container, "Option1");
        verify(radioMock).select(container, "Option1");
    }

    @Test
    public void testSelectContainerStrategy() {
        when(radioMock.select(container, strategy)).thenReturn("ResultOption");
        String result = service.select(mockRadioComponentType, container, strategy);
        assertEquals("ResultOption", result);
        verify(radioMock).select(container, strategy);
    }

    @Test
    public void testSelectText() {
        service.select(mockRadioComponentType, "Option1");
        verify(radioMock).select("Option1");
    }

    @Test
    public void testSelectLocator() {
        service.select(mockRadioComponentType, locator);
        verify(radioMock).select(locator);
    }

    @Test
    public void testIsEnabledContainerText() {
        when(radioMock.isEnabled(container, "Option1")).thenReturn(true);
        assertTrue(service.isEnabled(mockRadioComponentType, container, "Option1"));
        verify(radioMock).isEnabled(container, "Option1");
    }

    @Test
    public void testIsEnabledText() {
        when(radioMock.isEnabled("Option1")).thenReturn(true);
        assertTrue(service.isEnabled(mockRadioComponentType, "Option1"));
        verify(radioMock).isEnabled("Option1");
    }

    @Test
    public void testIsEnabledLocator() {
        when(radioMock.isEnabled(locator)).thenReturn(true);
        assertTrue(service.isEnabled(mockRadioComponentType, locator));
        verify(radioMock).isEnabled(locator);
    }

    @Test
    public void testIsSelectedContainerText() {
        when(radioMock.isSelected(container, "Option1")).thenReturn(true);
        assertTrue(service.isSelected(mockRadioComponentType, container, "Option1"));
        verify(radioMock).isSelected(container, "Option1");
    }

    @Test
    public void testIsSelectedText() {
        when(radioMock.isSelected("Option1")).thenReturn(true);
        assertTrue(service.isSelected(mockRadioComponentType, "Option1"));
        verify(radioMock).isSelected("Option1");
    }

    @Test
    public void testIsSelectedLocator() {
        when(radioMock.isSelected(locator)).thenReturn(true);
        assertTrue(service.isSelected(mockRadioComponentType, locator));
        verify(radioMock).isSelected(locator);
    }

    @Test
    public void testIsVisibleContainerText() {
        when(radioMock.isVisible(container, "Option1")).thenReturn(true);
        assertTrue(service.isVisible(mockRadioComponentType, container, "Option1"));
        verify(radioMock).isVisible(container, "Option1");
    }

    @Test
    public void testIsVisibleText() {
        when(radioMock.isVisible("Option1")).thenReturn(true);
        assertTrue(service.isVisible(mockRadioComponentType, "Option1"));
        verify(radioMock).isVisible("Option1");
    }

    @Test
    public void testIsVisibleLocator() {
        when(radioMock.isVisible(locator)).thenReturn(true);
        assertTrue(service.isVisible(mockRadioComponentType, locator));
        verify(radioMock).isVisible(locator);
    }

    @Test
    public void testGetSelectedContainer() {
        when(radioMock.getSelected(container)).thenReturn("Option1");
        assertEquals("Option1", service.getSelected(mockRadioComponentType, container));
        verify(radioMock).getSelected(container);
    }

    @Test
    public void testGetSelectedLocator() {
        when(radioMock.getSelected(locator)).thenReturn("Option1");
        assertEquals("Option1", service.getSelected(mockRadioComponentType, locator));
        verify(radioMock).getSelected(locator);
    }

    @Test
    public void testGetAllContainer() {
        List<String> options = Arrays.asList("Option1", "Option2");
        when(radioMock.getAll(container)).thenReturn(options);
        assertEquals(options, service.getAll(mockRadioComponentType, container));
        verify(radioMock).getAll(container);
    }

    @Test
    public void testGetAllLocator() {
        List<String> options = Arrays.asList("Option1", "Option2");
        when(radioMock.getAll(locator)).thenReturn(options);
        assertEquals(options, service.getAll(mockRadioComponentType, locator));
        verify(radioMock).getAll(locator);
    }

    @Test
    public void testInsertion() {
        service.insertion(mockRadioComponentType, locator, "Option1");
        verify(radioMock).select("Option1");
    }
}