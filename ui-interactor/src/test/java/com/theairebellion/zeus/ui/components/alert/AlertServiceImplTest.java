package com.theairebellion.zeus.ui.components.alert;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

public class AlertServiceImplTest {

    private SmartWebDriver driver;
    private AlertServiceImpl service;
    private SmartWebElement container;
    private Alert alertMock;
    private DummyAlertComponentType dummyType;
    private By locator;
    private MockedStatic<ComponentFactory> factoryMock;

    private enum DummyAlertComponentType implements AlertComponentType {
        DUMMY;
        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    @BeforeEach
    public void setUp() {
        driver = mock(SmartWebDriver.class);
        service = new AlertServiceImpl(driver);
        container = mock(SmartWebElement.class);
        alertMock = mock(Alert.class);
        dummyType = DummyAlertComponentType.DUMMY;
        locator = By.id("alert");
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getAlertComponent(eq(dummyType), eq(driver)))
                .thenReturn(alertMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testGetValueWithContainer() {
        when(alertMock.getValue(container)).thenReturn("Alert Value");
        String result = service.getValue(dummyType, container);
        assertEquals("Alert Value", result);
        verify(alertMock).getValue(container);
    }

    @Test
    public void testGetValueWithLocator() {
        when(alertMock.getValue(locator)).thenReturn("Locator Alert Value");
        String result = service.getValue(dummyType, locator);
        assertEquals("Locator Alert Value", result);
        verify(alertMock).getValue(locator);
    }

    @Test
    public void testIsVisibleWithContainer() {
        when(alertMock.isVisible(container)).thenReturn(true);
        boolean result = service.isVisible(dummyType, container);
        assertTrue(result);
        verify(alertMock).isVisible(container);
    }

    @Test
    public void testIsVisibleWithLocator() {
        when(alertMock.isVisible(locator)).thenReturn(true);
        boolean result = service.isVisible(dummyType, locator);
        assertTrue(result);
        verify(alertMock).isVisible(locator);
    }

    @Test
    public void testComponentCaching() {
        service.getValue(dummyType, container);
        service.isVisible(dummyType, container);
        factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(dummyType), eq(driver)), times(1));
    }
}