package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.button.mock.MockButtonComponentType;
import com.theairebellion.zeus.ui.components.button.mock.MockButtonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ButtonServiceTest extends BaseUnitUITest {

    private MockButtonService service;
    private MockSmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        service = new MockButtonService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testButton");
    }

    @Test
    void testDefaultClickWithContainerAndText() {
        service.reset();
        service.click(container, "ClickMe");
        assertEquals(MockButtonComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("ClickMe", service.lastButtonText);
    }

    @Test
    void testDefaultClickWithContainer() {
        service.reset();
        service.click(container);
        assertEquals(MockButtonComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultClickWithString() {
        service.reset();
        service.click("ClickString");
        assertEquals(MockButtonComponentType.DUMMY, service.lastComponentType);
        assertEquals("ClickString", service.lastButtonText);
    }

    @Test
    void testDefaultClickWithLocator() {
        service.reset();
        service.click(locator);
        assertEquals(MockButtonComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultIsEnabledWithContainerAndText() {
        service.reset();
        boolean enabled = service.isEnabled(container, "ButtonText");
        assertTrue(enabled);
        assertEquals(MockButtonComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("ButtonText", service.lastButtonText);
    }

    @Test
    void testDefaultIsEnabledWithContainer() {
        service.reset();
        boolean enabled = service.isEnabled(container);
        assertTrue(enabled);
        assertEquals(MockButtonComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultIsEnabledWithString() {
        service.reset();
        boolean enabled = service.isEnabled("JustText");
        assertTrue(enabled);
        assertEquals(MockButtonComponentType.DUMMY, service.lastComponentType);
        assertEquals("JustText", service.lastButtonText);
    }

    @Test
    void testDefaultIsEnabledWithLocator() {
        service.reset();
        boolean enabled = service.isEnabled(locator);
        assertTrue(enabled);
        assertEquals(MockButtonComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultIsVisibleWithContainerAndText() {
        service.reset();
        boolean visible = service.isVisible(container, "ButtonText");
        assertTrue(visible);
        assertEquals(MockButtonComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("ButtonText", service.lastButtonText);
    }

    @Test
    void testDefaultIsVisibleWithContainer() {
        service.reset();
        boolean visible = service.isVisible(container);
        assertTrue(visible);
        assertEquals(MockButtonComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultIsVisibleWithString() {
        service.reset();
        boolean visible = service.isVisible("JustText");
        assertTrue(visible);
        assertEquals(MockButtonComponentType.DUMMY, service.lastComponentType);
        assertEquals("JustText", service.lastButtonText);
    }

    @Test
    void testDefaultIsVisibleWithLocator() {
        service.reset();
        boolean visible = service.isVisible(locator);
        assertTrue(visible);
        assertEquals(MockButtonComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }
}