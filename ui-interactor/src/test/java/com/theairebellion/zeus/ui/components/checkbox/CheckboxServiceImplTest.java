package com.theairebellion.zeus.ui.components.checkbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

public class CheckboxServiceImplTest {

    static {
        System.setProperty("project.package", "com.theairebellion.zeus");
        System.setProperty("checkbox.default.type", "DUMMY");
    }

    private SmartWebDriver driver;
    private CheckboxServiceImpl service;
    private SmartWebElement container;
    private Checkbox checkboxMock;
    private DummyCheckboxComponentType dummyType;
    private By locator;
    private Strategy strategy;
    private MockedStatic<ComponentFactory> factoryMock;

    private enum DummyCheckboxComponentType implements CheckboxComponentType {
        DUMMY;
        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    @BeforeEach
    public void setUp() {
        driver = mock(SmartWebDriver.class);
        service = new CheckboxServiceImpl(driver);
        container = mock(SmartWebElement.class);
        checkboxMock = mock(Checkbox.class);
        dummyType = DummyCheckboxComponentType.DUMMY;
        locator = By.id("checkbox");
        strategy = mock(Strategy.class);
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getCheckBoxComponent(eq(dummyType), eq(driver)))
                .thenReturn(checkboxMock);
    }

    @AfterEach
    public void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Test
    public void testSelectWithContainerAndText() {
        service.select(dummyType, container, "A", "B");
        verify(checkboxMock).select(container, "A", "B");
    }

    @Test
    public void testSelectWithContainerAndStrategy() {
        when(checkboxMock.select(container, strategy)).thenReturn("StrategySelected");
        String result = service.select(dummyType, container, strategy);
        assertEquals("StrategySelected", result);
        verify(checkboxMock).select(container, strategy);
    }

    @Test
    public void testSelectWithTextOnly() {
        service.select(dummyType, "A");
        verify(checkboxMock).select("A");
    }

    @Test
    public void testSelectWithLocator() {
        service.select(dummyType, locator);
        verify(checkboxMock).select(locator);
    }

    @Test
    public void testDeSelectWithContainerAndText() {
        service.deSelect(dummyType, container, "A", "B");
        verify(checkboxMock).deSelect(container, "A", "B");
    }

    @Test
    public void testDeSelectWithContainerAndStrategy() {
        when(checkboxMock.deSelect(container, strategy)).thenReturn("StrategyDeSelected");
        String result = service.deSelect(dummyType, container, strategy);
        assertEquals("StrategyDeSelected", result);
        verify(checkboxMock).deSelect(container, strategy);
    }

    @Test
    public void testDeSelectWithTextOnly() {
        service.deSelect(dummyType, "A");
        verify(checkboxMock).deSelect("A");
    }

    @Test
    public void testDeSelectWithLocator() {
        service.deSelect(dummyType, locator);
        verify(checkboxMock).deSelect(locator);
    }

    @Test
    public void testAreSelectedWithContainerAndText() {
        when(checkboxMock.areSelected(container, "A", "B")).thenReturn(true);
        boolean result = service.areSelected(dummyType, container, "A", "B");
        assertTrue(result);
        verify(checkboxMock).areSelected(container, "A", "B");
    }

    @Test
    public void testAreSelectedWithTextOnly() {
        when(checkboxMock.areSelected("A")).thenReturn(true);
        boolean result = service.areSelected(dummyType, "A");
        assertTrue(result);
        verify(checkboxMock).areSelected("A");
    }

    @Test
    public void testAreSelectedWithLocator() {
        when(checkboxMock.areSelected(locator)).thenReturn(true);
        boolean result = service.areSelected(dummyType, locator);
        assertTrue(result);
        verify(checkboxMock).areSelected(locator);
    }

    @Test
    public void testIsSelectedWithContainer() {
        when(checkboxMock.areSelected(container, "A")).thenReturn(true);
        boolean result = service.isSelected(dummyType, container, "A");
        assertTrue(result);
        verify(checkboxMock).areSelected(container, "A");
    }

    @Test
    public void testIsSelectedWithTextOnly() {
        when(checkboxMock.areSelected("A")).thenReturn(true);
        boolean result = service.isSelected(dummyType, "A");
        assertTrue(result);
        verify(checkboxMock).areSelected("A");
    }

    @Test
    public void testIsSelectedWithLocator() {
        when(checkboxMock.areSelected(locator)).thenReturn(true);
        boolean result = service.isSelected(dummyType, locator);
        assertTrue(result);
        verify(checkboxMock).areSelected(locator);
    }

    @Test
    public void testAreEnabledWithContainerAndText() {
        when(checkboxMock.areEnabled(container, "A", "B")).thenReturn(true);
        boolean result = service.areEnabled(dummyType, container, "A", "B");
        assertTrue(result);
        verify(checkboxMock).areEnabled(container, "A", "B");
    }

    @Test
    public void testAreEnabledWithTextOnly() {
        when(checkboxMock.areEnabled("A")).thenReturn(true);
        boolean result = service.areEnabled(dummyType, "A");
        assertTrue(result);
        verify(checkboxMock).areEnabled("A");
    }

    @Test
    public void testAreEnabledWithLocator() {
        when(checkboxMock.areEnabled(locator)).thenReturn(true);
        boolean result = service.areEnabled(dummyType, locator);
        assertTrue(result);
        verify(checkboxMock).areEnabled(locator);
    }

    @Test
    public void testIsEnabledWithContainer() {
        when(checkboxMock.areEnabled(container, "A")).thenReturn(true);
        boolean result = service.isEnabled(dummyType, container, "A");
        assertTrue(result);
        verify(checkboxMock).areEnabled(container, "A");
    }

    @Test
    public void testIsEnabledWithTextOnly() {
        when(checkboxMock.areEnabled("A")).thenReturn(true);
        boolean result = service.isEnabled(dummyType, "A");
        assertTrue(result);
        verify(checkboxMock).areEnabled("A");
    }

    @Test
    public void testIsEnabledWithLocator() {
        when(checkboxMock.areEnabled(locator)).thenReturn(true);
        boolean result = service.isEnabled(dummyType, locator);
        assertTrue(result);
        verify(checkboxMock).areEnabled(locator);
    }

    @Test
    public void testGetSelectedWithContainer() {
        List<String> list = Collections.singletonList("A");
        when(checkboxMock.getSelected(container)).thenReturn(list);
        List<String> result = service.getSelected(dummyType, container);
        assertEquals(list, result);
        verify(checkboxMock).getSelected(container);
    }

    @Test
    public void testGetSelectedWithLocator() {
        List<String> list = Arrays.asList("A", "B");
        when(checkboxMock.getSelected(locator)).thenReturn(list);
        List<String> result = service.getSelected(dummyType, locator);
        assertEquals(list, result);
        verify(checkboxMock).getSelected(locator);
    }

    @Test
    public void testGetAllWithContainer() {
        List<String> list = Arrays.asList("A", "B", "C");
        when(checkboxMock.getAll(container)).thenReturn(list);
        List<String> result = service.getAll(dummyType, container);
        assertEquals(list, result);
        verify(checkboxMock).getAll(container);
    }

    @Test
    public void testGetAllWithLocator() {
        List<String> list = Arrays.asList("A", "B", "C", "D");
        when(checkboxMock.getAll(locator)).thenReturn(list);
        List<String> result = service.getAll(dummyType, locator);
        assertEquals(list, result);
        verify(checkboxMock).getAll(locator);
    }

    @Test
    public void testInsertion() {
        service.insertion(dummyType, locator, "InsertedValue");
        verify(checkboxMock).select("InsertedValue");
    }

    @Test
    public void testComponentCaching() {
        service.select(dummyType, container, "A");
        service.deSelect(dummyType, container, "B");
        service.areSelected(dummyType, container, "C");
        factoryMock.verify(() -> ComponentFactory.getCheckBoxComponent(eq(dummyType), eq(driver)), times(1));
    }
}