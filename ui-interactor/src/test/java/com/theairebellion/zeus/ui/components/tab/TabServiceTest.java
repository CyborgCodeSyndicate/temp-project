package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.tab.mock.MockTabComponentType;
import com.theairebellion.zeus.ui.components.tab.mock.MockTabService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TabServiceTest extends BaseUnitUITest {

    private MockTabService service;
    private MockTabComponentType mockType;
    private MockSmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        service = new MockTabService();
        mockType = MockTabComponentType.DUMMY;
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testTab");
    }

    @Test
    void testDefaultIsSelectedContainerAndText() {
        service.reset();
        service.returnBool = true;
        boolean selected = service.isSelected(mockType, container, "TabX");
        assertTrue(selected);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("TabX", service.lastText);
    }

    @Test
    void testDefaultIsSelectedContainerOnly() {
        service.reset();
        service.returnBool = true;
        boolean selected = service.isSelected(mockType, container);
        assertTrue(selected);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultIsSelectedTextOnly() {
        service.reset();
        service.returnBool = true;
        boolean selected = service.isSelected(mockType, "TabZ");
        assertTrue(selected);
        assertEquals(mockType, service.lastComponentType);
        assertEquals("TabZ", service.lastText);
    }

    @Test
    void testDefaultIsSelectedLocator() {
        service.reset();
        service.returnBool = true;
        boolean selected = service.isSelected(mockType, locator);
        assertTrue(selected);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultClickContainerAndText() {
        service.reset();
        service.click(mockType, container, "ClickTab");
        assertEquals(mockType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("ClickTab", service.lastText);
    }

    @Test
    void testDefaultClickContainerOnly() {
        service.reset();
        service.click(mockType, container);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultClickTextOnly() {
        service.reset();
        service.click(mockType, "CText");
        assertEquals(mockType, service.lastComponentType);
        assertEquals("CText", service.lastText);
    }

    @Test
    void testDefaultClickLocator() {
        service.reset();
        service.click(mockType, locator);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultIsEnabledContainerAndText() {
        service.reset();
        service.returnBool = true;
        boolean enabled = service.isEnabled(mockType, container, "TabA");
        assertTrue(enabled);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("TabA", service.lastText);
    }

    @Test
    void testDefaultIsEnabledContainer() {
        service.reset();
        service.returnBool = true;
        boolean enabled = service.isEnabled(mockType, container);
        assertTrue(enabled);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultIsEnabledText() {
        service.reset();
        service.returnBool = true;
        boolean enabled = service.isEnabled(mockType, "TT");
        assertTrue(enabled);
        assertEquals(mockType, service.lastComponentType);
        assertEquals("TT", service.lastText);
    }

    @Test
    void testDefaultIsEnabledLocator() {
        service.reset();
        service.returnBool = true;
        boolean enabled = service.isEnabled(mockType, locator);
        assertTrue(enabled);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultIsVisibleContainerAndText() {
        service.reset();
        service.returnBool = true;
        boolean visible = service.isVisible(mockType, container, "TabView");
        assertTrue(visible);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("TabView", service.lastText);
    }

    @Test
    void testDefaultIsVisibleContainer() {
        service.reset();
        service.returnBool = true;
        boolean visible = service.isVisible(mockType, container);
        assertTrue(visible);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultIsVisibleText() {
        service.reset();
        service.returnBool = true;
        boolean visible = service.isVisible(mockType, "TCheck");
        assertTrue(visible);
        assertEquals(mockType, service.lastComponentType);
        assertEquals("TCheck", service.lastText);
    }

    @Test
    void testDefaultIsVisibleLocator() {
        service.reset();
        service.returnBool = true;
        boolean visible = service.isVisible(mockType, locator);
        assertTrue(visible);
        assertEquals(mockType, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }
}