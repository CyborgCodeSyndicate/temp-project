package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.link.mock.MockLinkComponentType;
import com.theairebellion.zeus.ui.components.link.mock.MockLinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class LinkServiceTest extends BaseUnitUITest {

    private MockLinkService service;
    private MockSmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        service = new MockLinkService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testLink");
    }

    @Test
    void testDoubleClickWithContainerAndTextDefault() {
        service.reset();
        service.doubleClick(container, "LinkTxt");
        assertEquals(MockLinkComponentType.DUMMY, service.lastLinkType);
        assertEquals(container, service.lastContainer);
        assertEquals("LinkTxt", service.lastButtonText);
    }

    @Test
    void testDoubleClickWithContainerDefault() {
        service.reset();
        service.doubleClick(container);
        assertEquals(MockLinkComponentType.DUMMY, service.lastLinkType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDoubleClickWithTextOnlyDefault() {
        service.reset();
        service.doubleClick("someLink");
        assertEquals(MockLinkComponentType.DUMMY, service.lastLinkType);
        assertEquals("someLink", service.lastButtonText);
    }

    @Test
    void testDoubleClickWithLocatorDefault() {
        service.reset();
        service.doubleClick(locator);
        assertEquals(MockLinkComponentType.DUMMY, service.lastLinkType);
        assertEquals(locator, service.lastLocator);
    }
}