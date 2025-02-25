package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.select.mock.MockSelectComponentType;
import com.theairebellion.zeus.ui.components.select.mock.MockSelectService;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SelectServiceTest extends BaseUnitUITest {

    private MockSelectService service;
    private MockSmartWebElement container;
    private By locator;
    private Strategy strategy;

    @BeforeEach
    void setUp() {
        service = new MockSelectService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testSelect");
        strategy = Strategy.RANDOM;
    }

    @Test
    void testDefaultSelectOptionsContainerVarargs() {
        service.reset();
        service.selectOptions(container, "val1", "val2");
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"val1", "val2"}, service.lastValues);
    }

    @Test
    void testDefaultSelectOptionContainer() {
        service.reset();
        service.selectOption(container, "valSingle");
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"valSingle"}, service.lastValues);
    }

    @Test
    void testDefaultSelectOptionsLocatorVarargs() {
        service.reset();
        service.selectOptions(locator, "v1", "v2");
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
        assertArrayEquals(new String[]{"v1", "v2"}, service.lastValues);
    }

    @Test
    void testDefaultSelectOptionLocator() {
        service.reset();
        service.selectOption(locator, "single");
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
        assertArrayEquals(new String[]{"single"}, service.lastValues);
    }

    @Test
    void testDefaultSelectOptionsContainerStrategy() {
        service.reset();
        service.returnOptions = List.of("res1", "res2");
        List<String> result = service.selectOptions(container, strategy);
        assertEquals(List.of("res1", "res2"), result);
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals(strategy, service.lastStrategy);
    }

    @Test
    void testDefaultSelectOptionsLocatorStrategy() {
        service.reset();
        service.returnOptions = List.of("op1", "op2");
        List<String> result = service.selectOptions(locator, strategy);
        assertEquals(List.of("op1", "op2"), result);
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
        assertEquals(strategy, service.lastStrategy);
    }

    @Test
    void testDefaultGetAvailableOptionsContainer() {
        service.reset();
        service.returnOptions = List.of("avail1", "avail2");
        List<String> opts = service.getAvailableOptions(container);
        assertEquals(List.of("avail1", "avail2"), opts);
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetAvailableOptionsLocator() {
        service.reset();
        service.returnOptions = List.of("x", "y");
        List<String> opts = service.getAvailableOptions(locator);
        assertEquals(List.of("x", "y"), opts);
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultGetSelectedOptionsContainer() {
        service.reset();
        service.returnOptions = List.of("s1", "s2");
        List<String> selected = service.getSelectedOptions(container);
        assertEquals(List.of("s1", "s2"), selected);
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetSelectedOptionsLocator() {
        service.reset();
        service.returnOptions = List.of("chosenA", "chosenB");
        List<String> selected = service.getSelectedOptions(locator);
        assertEquals(List.of("chosenA", "chosenB"), selected);
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultIsOptionVisibleContainer() {
        service.reset();
        service.returnBool = true;
        boolean visible = service.isOptionVisible(container, "valueA");
        assertTrue(visible);
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"valueA"}, service.lastValues);
    }

    @Test
    void testDefaultIsOptionVisibleLocator() {
        service.reset();
        service.returnBool = true;
        boolean visible = service.isOptionVisible(locator, "valZ");
        assertTrue(visible);
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
        assertArrayEquals(new String[]{"valZ"}, service.lastValues);
    }

    @Test
    void testDefaultIsOptionEnabledContainer() {
        service.reset();
        service.returnBool = true;
        boolean enabled = service.isOptionEnabled(container, "enableVal");
        assertTrue(enabled);
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"enableVal"}, service.lastValues);
    }

    @Test
    void testDefaultIsOptionEnabledLocator() {
        service.reset();
        service.returnBool = true;
        boolean enabled = service.isOptionEnabled(locator, "maybe");
        assertTrue(enabled);
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
        assertArrayEquals(new String[]{"maybe"}, service.lastValues);
    }

    @Test
    void testInsertionMethod() {
        service.reset();
        service.insertion(MockSelectComponentType.DUMMY, locator, "ins1", "ins2");
        assertEquals(MockSelectComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
        assertArrayEquals(new String[]{"ins1", "ins2"}, service.lastValues);
    }
}