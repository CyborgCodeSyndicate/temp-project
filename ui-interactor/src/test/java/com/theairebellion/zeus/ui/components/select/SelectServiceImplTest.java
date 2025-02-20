package com.theairebellion.zeus.ui.components.select;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.select.mock.MockSelectComponentType;
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

class SelectServiceImplTest {

    private SmartWebDriver driver;
    private SelectServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private Strategy strategy;
    private Select selectMock;
    private MockSelectComponentType mockSelectComponentType;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeEach
    public void setUp() {
        System.setProperty("project.package", "com.theairebellion.zeus");
        System.setProperty("select.default.type", "DUMMY");
        driver = mock(SmartWebDriver.class);
        service = new SelectServiceImpl(driver);
        container = mock(SmartWebElement.class);
        locator = By.id("select");
        strategy = mock(Strategy.class);
        selectMock = mock(Select.class);
        mockSelectComponentType = MockSelectComponentType.DUMMY;
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getSelectComponent(eq(mockSelectComponentType), eq(driver)))
                .thenReturn(selectMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testSelectOptionsContainerVarargs() {
        service.selectOptions(mockSelectComponentType, container, "val1", "val2");
        verify(selectMock).selectOptions(container, "val1", "val2");
    }

    @Test
    public void testSelectOptionContainer() {
        service.selectOption(mockSelectComponentType, container, "val1");
        verify(selectMock).selectOptions(container, "val1");
    }

    @Test
    public void testSelectOptionsLocatorVarargs() {
        service.selectOptions(mockSelectComponentType, locator, "val1", "val2");
        verify(selectMock).selectOptions(locator, "val1", "val2");
    }

    @Test
    public void testSelectOptionLocator() {
        service.selectOption(mockSelectComponentType, locator, "val1");
        verify(selectMock).selectOptions(locator, "val1");
    }

    @Test
    public void testSelectOptionsContainerStrategy() {
        List<String> expected = Arrays.asList("val1", "val2");
        when(selectMock.selectOptions(container, strategy)).thenReturn(expected);
        List<String> result = service.selectOptions(mockSelectComponentType, container, strategy);
        assertEquals(expected, result);
        verify(selectMock).selectOptions(container, strategy);
    }

    @Test
    public void testSelectOptionsLocatorStrategy() {
        List<String> expected = Arrays.asList("val1", "val2");
        when(selectMock.selectOptions(locator, strategy)).thenReturn(expected);
        List<String> result = service.selectOptions(mockSelectComponentType, locator, strategy);
        assertEquals(expected, result);
        verify(selectMock).selectOptions(locator, strategy);
    }

    @Test
    public void testGetAvailableOptionsContainer() {
        List<String> expected = Arrays.asList("opt1", "opt2");
        when(selectMock.getAvailableOptions(container)).thenReturn(expected);
        List<String> result = service.getAvailableOptions(mockSelectComponentType, container);
        assertEquals(expected, result);
        verify(selectMock).getAvailableOptions(container);
    }

    @Test
    public void testGetAvailableOptionsLocator() {
        List<String> expected = Arrays.asList("opt1", "opt2");
        when(selectMock.getAvailableOptions(locator)).thenReturn(expected);
        List<String> result = service.getAvailableOptions(mockSelectComponentType, locator);
        assertEquals(expected, result);
        verify(selectMock).getAvailableOptions(locator);
    }

    @Test
    public void testGetSelectedOptionsContainer() {
        List<String> expected = Arrays.asList("sel1", "sel2");
        when(selectMock.getSelectedOptions(container)).thenReturn(expected);
        List<String> result = service.getSelectedOptions(mockSelectComponentType, container);
        assertEquals(expected, result);
        verify(selectMock).getSelectedOptions(container);
    }

    @Test
    public void testGetSelectedOptionsLocator() {
        List<String> expected = Arrays.asList("sel1", "sel2");
        when(selectMock.getSelectedOptions(locator)).thenReturn(expected);
        List<String> result = service.getSelectedOptions(mockSelectComponentType, locator);
        assertEquals(expected, result);
        verify(selectMock).getSelectedOptions(locator);
    }

    @Test
    public void testIsOptionVisibleContainer() {
        when(selectMock.isOptionVisible(container, "val1")).thenReturn(true);
        assertTrue(service.isOptionVisible(mockSelectComponentType, container, "val1"));
        verify(selectMock).isOptionVisible(container, "val1");
    }

    @Test
    public void testIsOptionVisibleLocator() {
        when(selectMock.isOptionVisible(locator, "val1")).thenReturn(true);
        assertTrue(service.isOptionVisible(mockSelectComponentType, locator, "val1"));
        verify(selectMock).isOptionVisible(locator, "val1");
    }

    @Test
    public void testIsOptionEnabledContainer() {
        when(selectMock.isOptionEnabled(container, "val1")).thenReturn(true);
        assertTrue(service.isOptionEnabled(mockSelectComponentType, container, "val1"));
        verify(selectMock).isOptionEnabled(container, "val1");
    }

    @Test
    public void testIsOptionEnabledLocator() {
        when(selectMock.isOptionEnabled(locator, "val1")).thenReturn(true);
        assertTrue(service.isOptionEnabled(mockSelectComponentType, locator, "val1"));
        verify(selectMock).isOptionEnabled(locator, "val1");
    }

    @Test
    public void testSelectOptionsDefaultOverloadsContainer() {
        service.selectOptions(container, "val1", "val2");
        verify(selectMock).selectOptions(container, "val1", "val2");
    }

    @Test
    public void testSelectOptionDefaultOverloadsContainer() {
        service.selectOption(container, "val1");
        verify(selectMock).selectOptions(container, "val1");
    }

    @Test
    public void testSelectOptionsDefaultOverloadsLocator() {
        service.selectOptions(locator, "val1", "val2");
        verify(selectMock).selectOptions(locator, "val1", "val2");
    }

    @Test
    public void testSelectOptionDefaultOverloadsLocator() {
        service.selectOption(locator, "val1");
        verify(selectMock).selectOptions(locator, "val1");
    }

    @Test
    public void testGetAvailableOptionsDefaultOverloadsContainer() {
        List<String> expected = Arrays.asList("opt1", "opt2");
        when(selectMock.getAvailableOptions(container)).thenReturn(expected);
        assertEquals(expected, service.getAvailableOptions(container));
        verify(selectMock).getAvailableOptions(container);
    }

    @Test
    public void testGetAvailableOptionsDefaultOverloadsLocator() {
        List<String> expected = Arrays.asList("opt1", "opt2");
        when(selectMock.getAvailableOptions(locator)).thenReturn(expected);
        assertEquals(expected, service.getAvailableOptions(locator));
        verify(selectMock).getAvailableOptions(locator);
    }

    @Test
    public void testGetSelectedOptionsDefaultOverloadsContainer() {
        List<String> expected = Arrays.asList("sel1", "sel2");
        when(selectMock.getSelectedOptions(container)).thenReturn(expected);
        assertEquals(expected, service.getSelectedOptions(container));
        verify(selectMock).getSelectedOptions(container);
    }

    @Test
    public void testGetSelectedOptionsDefaultOverloadsLocator() {
        List<String> expected = Arrays.asList("sel1", "sel2");
        when(selectMock.getSelectedOptions(locator)).thenReturn(expected);
        assertEquals(expected, service.getSelectedOptions(locator));
        verify(selectMock).getSelectedOptions(locator);
    }

    @Test
    public void testIsOptionVisibleDefaultOverloadsContainer() {
        when(selectMock.isOptionVisible(container, "val1")).thenReturn(true);
        assertTrue(service.isOptionVisible(container, "val1"));
        verify(selectMock).isOptionVisible(container, "val1");
    }

    @Test
    public void testIsOptionVisibleDefaultOverloadsLocator() {
        when(selectMock.isOptionVisible(locator, "val1")).thenReturn(true);
        assertTrue(service.isOptionVisible(locator, "val1"));
        verify(selectMock).isOptionVisible(locator, "val1");
    }

    @Test
    public void testIsOptionEnabledDefaultOverloadsContainer() {
        when(selectMock.isOptionEnabled(container, "val1")).thenReturn(true);
        assertTrue(service.isOptionEnabled(container, "val1"));
        verify(selectMock).isOptionEnabled(container, "val1");
    }

    @Test
    public void testIsOptionEnabledDefaultOverloadsLocator() {
        when(selectMock.isOptionEnabled(locator, "val1")).thenReturn(true);
        assertTrue(service.isOptionEnabled(locator, "val1"));
        verify(selectMock).isOptionEnabled(locator, "val1");
    }

    @Test
    public void testInsertion() {
        service.insertion(mockSelectComponentType, locator, "val1", "val2");
        verify(selectMock).selectOptions(locator, "val1", "val2");
    }
}