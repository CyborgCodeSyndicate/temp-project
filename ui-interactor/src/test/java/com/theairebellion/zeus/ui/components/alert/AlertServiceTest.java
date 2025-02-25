package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.alert.mock.MockAlertComponentType;
import com.theairebellion.zeus.ui.components.alert.mock.MockAlertService;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class AlertServiceTest extends BaseUnitUITest {

    private MockAlertService service;
    private MockSmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        service = new MockAlertService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("alertLocator");
    }

    @Test
    void testGetValueWithContainerDefault() {
        service.reset();
        String result = service.getValue(container);
        assertEquals(MockAlertService.VALUE_CONTAINER, result);
        assertEquals(MockAlertComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testGetValueWithLocatorDefault() {
        service.reset();
        String result = service.getValue(locator);
        assertEquals(MockAlertService.VALUE_LOCATOR, result);
        assertEquals(MockAlertComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testIsVisibleWithContainerDefault() {
        service.reset();
        boolean visible = service.isVisible(container);
        assertTrue(visible);
        assertEquals(MockAlertComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testIsVisibleWithLocatorDefault() {
        service.reset();
        boolean visible = service.isVisible(locator);
        assertTrue(visible);
        assertEquals(MockAlertComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testGetValueWithContainerExplicitType() {
        service.reset();
        String result = service.getValue(MockAlertComponentType.DUMMY, container);
        assertEquals(MockAlertService.VALUE_CONTAINER, result);
        assertEquals(MockAlertComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testGetValueWithLocatorExplicitType() {
        service.reset();
        String result = service.getValue(MockAlertComponentType.DUMMY, locator);
        assertEquals(MockAlertService.VALUE_LOCATOR, result);
        assertEquals(MockAlertComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testIsVisibleWithContainerExplicitType() {
        service.reset();
        boolean visible = service.isVisible(MockAlertComponentType.DUMMY, container);
        assertTrue(visible);
        assertEquals(MockAlertComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testIsVisibleWithLocatorExplicitType() {
        service.reset();
        boolean visible = service.isVisible(MockAlertComponentType.DUMMY, locator);
        assertTrue(visible);
        assertEquals(MockAlertComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }
}