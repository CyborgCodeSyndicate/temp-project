package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.toggle.mock.MockToggleComponentType;
import com.theairebellion.zeus.ui.components.toggle.mock.MockToggleService;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ToggleServiceTest extends BaseUnitUITest {

    private MockToggleService service;
    private MockToggleComponentType mockType;
    private MockSmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        service = new MockToggleService();
        mockType = MockToggleComponentType.DUMMY;
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testToggle");
    }

    @Test
    void testActivateContainerAndText() {
        service.reset();
        service.activate(mockType, container, "On");
        assertEquals(mockType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("On", service.lastText);
    }

    @Test
    void testActivateTextOnly() {
        service.reset();
        service.activate(mockType, "On");
        assertEquals(mockType, service.lastComponentType);
        assertEquals("On", service.lastText);
    }

    @Test
    void testActivateLocator() {
        service.reset();
        service.activate(mockType, locator);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDeactivateContainerAndText() {
        service.reset();
        service.deactivate(mockType, container, "Off");
        assertEquals(mockType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("Off", service.lastText);
    }

    @Test
    void testDeactivateTextOnly() {
        service.reset();
        service.deactivate(mockType, "Off");
        assertEquals(mockType, service.lastComponentType);
        assertEquals("Off", service.lastText);
    }

    @Test
    void testDeactivateLocator() {
        service.reset();
        service.deactivate(mockType, locator);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testIsEnabledContainerAndText() {
        service.reset();
        service.returnBool = true;
        boolean result = service.isEnabled(mockType, container, "On");
        assertTrue(result);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("On", service.lastText);
    }

    @Test
    void testIsEnabledTextOnly() {
        service.reset();
        service.returnBool = true;
        boolean result = service.isEnabled(mockType, "On");
        assertTrue(result);
        assertEquals(mockType, service.lastComponentType);
        assertEquals("On", service.lastText);
    }

    @Test
    void testIsEnabledLocator() {
        service.reset();
        service.returnBool = true;
        boolean result = service.isEnabled(mockType, locator);
        assertTrue(result);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testIsActivatedContainerAndText() {
        service.reset();
        service.returnBool = true;
        boolean result = service.isActivated(mockType, container, "On");
        assertTrue(result);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("On", service.lastText);
    }

    @Test
    void testIsActivatedTextOnly() {
        service.reset();
        service.returnBool = true;
        boolean result = service.isActivated(mockType, "On");
        assertTrue(result);
        assertEquals(mockType, service.lastComponentType);
        assertEquals("On", service.lastText);
    }

    @Test
    void testIsActivatedLocator() {
        service.reset();
        service.returnBool = true;
        boolean result = service.isActivated(mockType, locator);
        assertTrue(result);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }
}