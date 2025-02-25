package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.radio.mock.MockRadioComponentType;
import com.theairebellion.zeus.ui.components.radio.mock.MockRadioService;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class RadioServiceTest extends BaseUnitUITest {

    private MockRadioService service;
    private MockSmartWebElement container;
    private By locator;
    private Strategy strategy;

    @BeforeEach
    void setUp() {
        service = new MockRadioService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testRadio");
        strategy = Strategy.FIRST;
    }

    @Test
    void testDefaultSelectContainerText() {
        service.reset();
        service.select(container, "RadioText");
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("RadioText", service.lastText);
    }

    @Test
    void testDefaultSelectContainerStrategy() {
        service.reset();
        service.returnSelected = "StrategyRadio";
        String result = service.select(container, strategy);
        assertEquals("StrategyRadio", result);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals(strategy, service.lastStrategy);
    }

    @Test
    void testDefaultSelectText() {
        service.reset();
        service.select("RadioLabel");
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals("RadioLabel", service.lastText);
    }

    @Test
    void testDefaultSelectLocator() {
        service.reset();
        service.select(locator);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultIsEnabledContainerText() {
        service.reset();
        service.returnBool = true;
        boolean enabled = service.isEnabled(container, "OptionA");
        assertTrue(enabled);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("OptionA", service.lastText);
    }

    @Test
    void testDefaultIsEnabledText() {
        service.reset();
        service.returnBool = true;
        boolean enabled = service.isEnabled("OptionB");
        assertTrue(enabled);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals("OptionB", service.lastText);
    }

    @Test
    void testDefaultIsEnabledLocator() {
        service.reset();
        service.returnBool = true;
        boolean enabled = service.isEnabled(locator);
        assertTrue(enabled);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultIsSelectedContainerText() {
        service.reset();
        service.returnBool = true;
        boolean selected = service.isSelected(container, "RA");
        assertTrue(selected);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("RA", service.lastText);
    }

    @Test
    void testDefaultIsSelectedText() {
        service.reset();
        service.returnBool = true;
        boolean selected = service.isSelected("RB");
        assertTrue(selected);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals("RB", service.lastText);
    }

    @Test
    void testDefaultIsSelectedLocator() {
        service.reset();
        service.returnBool = true;
        boolean selected = service.isSelected(locator);
        assertTrue(selected);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultIsVisibleContainerText() {
        service.reset();
        service.returnBool = true;
        boolean visible = service.isVisible(container, "RX");
        assertTrue(visible);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("RX", service.lastText);
    }

    @Test
    void testDefaultIsVisibleText() {
        service.reset();
        service.returnBool = true;
        boolean visible = service.isVisible("RY");
        assertTrue(visible);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals("RY", service.lastText);
    }

    @Test
    void testDefaultIsVisibleLocator() {
        service.reset();
        service.returnBool = true;
        boolean visible = service.isVisible(locator);
        assertTrue(visible);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultGetSelectedContainer() {
        service.reset();
        service.returnSelected = "RC";
        String sel = service.getSelected(container);
        assertEquals("RC", sel);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetSelectedLocator() {
        service.reset();
        service.returnSelected = "RD";
        String sel = service.getSelected(locator);
        assertEquals("RD", sel);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultGetAllContainer() {
        service.reset();
        service.returnAll = List.of("R1", "R2");
        List<String> all = service.getAll(container);
        assertEquals(List.of("R1", "R2"), all);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetAllLocator() {
        service.reset();
        service.returnAll = List.of("R3", "R4");
        List<String> all = service.getAll(locator);
        assertEquals(List.of("R3", "R4"), all);
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testInsertionMethod() {
        service.reset();
        service.insertion(MockRadioComponentType.DUMMY, locator, "InsertRadio");
        assertEquals(MockRadioComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
        assertEquals("InsertRadio", service.lastText);
    }
}