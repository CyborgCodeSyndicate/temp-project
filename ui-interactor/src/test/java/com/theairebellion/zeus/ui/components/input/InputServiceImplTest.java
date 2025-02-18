package com.theairebellion.zeus.ui.components.input;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class InputServiceImplTest {

    static {
        System.setProperty("project.package", "com.theairebellion.zeus");
        System.setProperty("input.default.type", "DUMMY");
    }

    private SmartWebDriver driver;
    private InputServiceImpl service;
    private SmartWebElement container;
    private Input inputMock;
    private DummyInputComponentType dummyType;
    private By locator;
    private SmartWebElement cell;
    private SmartWebElement headerCell;
    private FilterStrategy filterStrategy;
    private MockedStatic<ComponentFactory> factoryMock;

    private enum DummyInputComponentType implements InputComponentType {
        DUMMY;
        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    @BeforeEach
    public void setUp() {
        driver = mock(SmartWebDriver.class);
        service = new InputServiceImpl(driver);
        container = mock(SmartWebElement.class);
        inputMock = mock(Input.class);
        dummyType = DummyInputComponentType.DUMMY;
        locator = By.id("input");
        cell = mock(SmartWebElement.class);
        headerCell = mock(SmartWebElement.class);
        filterStrategy = mock(FilterStrategy.class);
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getInputComponent(eq(dummyType), eq(driver)))
                .thenReturn(inputMock);
    }

    @AfterEach
    public void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Test
    public void testInsertContainer() {
        service.insert(dummyType, container, "value");
        verify(inputMock).insert(container, "value");
    }

    @Test
    public void testInsertContainerLabel() {
        service.insert(dummyType, container, "label", "value");
        verify(inputMock).insert(container, "label", "value");
    }

    @Test
    public void testInsertLabel() {
        service.insert(dummyType, "label", "value");
        verify(inputMock).insert("label", "value");
    }

    @Test
    public void testInsertBy() {
        service.insert(dummyType, locator, "value");
        verify(inputMock).insert(locator, "value");
    }

    @Test
    public void testClearContainer() {
        service.clear(dummyType, container);
        verify(inputMock).clear(container);
    }

    @Test
    public void testClearContainerLabel() {
        service.clear(dummyType, container, "label");
        verify(inputMock).clear(container, "label");
    }

    @Test
    public void testClearLabel() {
        service.clear(dummyType, "label");
        verify(inputMock).clear("label");
    }

    @Test
    public void testClearBy() {
        service.clear(dummyType, locator);
        verify(inputMock).clear(locator);
    }

    @Test
    public void testGetValueContainer() {
        when(inputMock.getValue(container)).thenReturn("val");
        String result = service.getValue(dummyType, container);
        assertEquals("val", result);
        verify(inputMock).getValue(container);
    }

    @Test
    public void testGetValueContainerLabel() {
        when(inputMock.getValue(container)).thenReturn("val");
        String result = service.getValue(dummyType, container, "label");
        assertEquals("val", result);
        verify(inputMock).getValue(container);
    }

    @Test
    public void testGetValueLabel() {
        when(inputMock.getValue("label")).thenReturn("val");
        String result = service.getValue(dummyType, "label");
        assertEquals("val", result);
        verify(inputMock).getValue("label");
    }

    @Test
    public void testGetValueBy() {
        when(inputMock.getValue(locator)).thenReturn("val");
        String result = service.getValue(dummyType, locator);
        assertEquals("val", result);
        verify(inputMock).getValue(locator);
    }

    @Test
    public void testIsEnabledContainer() {
        when(inputMock.isEnabled(container)).thenReturn(true);
        boolean result = service.isEnabled(dummyType, container);
        assertTrue(result);
        verify(inputMock).isEnabled(container);
    }

    @Test
    public void testIsEnabledContainerLabel() {
        when(inputMock.isEnabled(container, "label")).thenReturn(true);
        boolean result = service.isEnabled(dummyType, container, "label");
        assertTrue(result);
        verify(inputMock).isEnabled(container, "label");
    }

    @Test
    public void testIsEnabledLabel() {
        when(inputMock.isEnabled("label")).thenReturn(true);
        boolean result = service.isEnabled(dummyType, "label");
        assertTrue(result);
        verify(inputMock).isEnabled("label");
    }

    @Test
    public void testIsEnabledBy() {
        when(inputMock.isEnabled(locator)).thenReturn(true);
        boolean result = service.isEnabled(dummyType, locator);
        assertTrue(result);
        verify(inputMock).isEnabled(locator);
    }

    @Test
    public void testGetErrorMessageContainer() {
        when(inputMock.getErrorMessage(container)).thenReturn("err");
        String result = service.getErrorMessage(dummyType, container);
        assertEquals("err", result);
        verify(inputMock).getErrorMessage(container);
    }

    @Test
    public void testGetErrorMessageContainerLabel() {
        when(inputMock.getErrorMessage(container, "label")).thenReturn("err");
        String result = service.getErrorMessage(dummyType, container, "label");
        assertEquals("err", result);
        verify(inputMock).getErrorMessage(container, "label");
    }

    @Test
    public void testGetErrorMessageLabel() {
        when(inputMock.getErrorMessage("label")).thenReturn("err");
        String result = service.getErrorMessage(dummyType, "label");
        assertEquals("err", result);
        verify(inputMock).getErrorMessage("label");
    }

    @Test
    public void testGetErrorMessageBy() {
        when(inputMock.getErrorMessage(locator)).thenReturn("err");
        String result = service.getErrorMessage(dummyType, locator);
        assertEquals("err", result);
        verify(inputMock).getErrorMessage(locator);
    }

    @Test
    public void testTableInsertion() {
        service.tableInsertion(cell, dummyType, "val1", "val2");
        verify(inputMock).tableInsertion(cell, "val1", "val2");
    }

    @Test
    public void testTableFilter() {
        service.tableFilter(headerCell, dummyType, filterStrategy, "val1");
        verify(inputMock).tableFilter(headerCell, filterStrategy, "val1");
    }

    @Test
    public void testInsertionMethod() {
        service.insertion(dummyType, locator, "insertionVal");
        verify(inputMock).insert(locator, "insertionVal");
    }
}