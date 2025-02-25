package com.theairebellion.zeus.ui.components.modal;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.modal.mock.MockModalComponentType;
import com.theairebellion.zeus.ui.components.modal.mock.MockModalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ModalServiceTest extends BaseUnitUITest {

    private MockModalService service;
    private MockSmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        service = new MockModalService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testModal");
    }

    @Test
    void testDefaultIsOpened() {
        service.reset();
        service.returnOpened = true;
        boolean opened = service.isOpened();
        assertTrue(opened);
        assertEquals(MockModalComponentType.DUMMY, service.lastComponentType);
    }

    @Test
    void testDefaultClickButtonContainerAndText() {
        service.reset();
        service.clickButton(container, "OK");
        assertEquals(MockModalComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("OK", service.lastButtonText);
    }

    @Test
    void testDefaultClickButtonTextOnly() {
        service.reset();
        service.clickButton("Proceed");
        assertEquals(MockModalComponentType.DUMMY, service.lastComponentType);
        assertEquals("Proceed", service.lastButtonText);
    }

    @Test
    void testDefaultClickButtonByLocator() {
        service.reset();
        service.clickButton(locator);
        assertEquals(MockModalComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastButtonLocator);
    }

    @Test
    void testDefaultGetTitle() {
        service.reset();
        service.returnTitle = "Modal Title Here";
        String title = service.getTitle();
        assertEquals("Modal Title Here", title);
        assertEquals(MockModalComponentType.DUMMY, service.lastComponentType);
    }

    @Test
    void testDefaultGetBodyText() {
        service.reset();
        service.returnBodyText = "Body content sample";
        String body = service.getBodyText();
        assertEquals("Body content sample", body);
        assertEquals(MockModalComponentType.DUMMY, service.lastComponentType);
    }

    @Test
    void testDefaultGetContentTitle() {
        service.reset();
        service.returnContentTitle = "Content Title Sample";
        String ctitle = service.getContentTitle();
        assertEquals("Content Title Sample", ctitle);
        assertEquals(MockModalComponentType.DUMMY, service.lastComponentType);
    }

    @Test
    void testDefaultClose() {
        service.reset();
        service.close();
        assertEquals(MockModalComponentType.DUMMY, service.lastComponentType);
    }
}