package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.AccordionMockComponentType;
import com.theairebellion.zeus.ui.components.accordion.mock.MockAccordionService;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class AccordionServiceTest extends BaseUnitUITest {

    private MockAccordionService service;
    private MockSmartWebElement container;
    private By locator;
    private Strategy strategy;

    @BeforeEach
    void setUp() {
        service = new MockAccordionService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("test");
        strategy = Strategy.FIRST;
    }

    @Test
    void testDefaultExpandWithContainerAndText() {
        service.reset();
        service.expand(container, "Panel1", "Panel2");
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"Panel1", "Panel2"}, service.lastAccordionText);
    }

    @Test
    void testDefaultExpandWithContainerAndStrategy() {
        service.reset();
        String result = service.expand(container, strategy);
        assertEquals(MockAccordionService.EXPAND_STRATEGY_RESULT, result);
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals(strategy, service.lastStrategy);
    }

    @Test
    void testDefaultExpandWithTextOnly() {
        service.reset();
        service.expand("Panel1");
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"Panel1"}, service.lastAccordionText);
    }

    @Test
    void testDefaultExpandWithByLocator() {
        service.reset();
        service.expand(locator);
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastAccordionLocators);
    }

    @Test
    void testDefaultCollapseWithContainerAndText() {
        service.reset();
        service.collapse(container, "Panel1");
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"Panel1"}, service.lastAccordionText);
    }

    @Test
    void testDefaultCollapseWithContainerAndStrategy() {
        service.reset();
        String result = service.collapse(container, strategy);
        assertEquals(MockAccordionService.COLLAPSE_STRATEGY_RESULT, result);
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals(strategy, service.lastStrategy);
    }

    @Test
    void testDefaultCollapseWithTextOnly() {
        service.reset();
        service.collapse("Panel1", "Panel2");
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"Panel1", "Panel2"}, service.lastAccordionText);
    }

    @Test
    void testDefaultCollapseWithByLocator() {
        service.reset();
        service.collapse(locator);
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastAccordionLocators);
    }

    @Test
    void testDefaultAreEnabledWithContainerAndText() {
        service.reset();
        boolean result = service.areEnabled(container, "Panel1", "Panel2");
        assertTrue(result);
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"Panel1", "Panel2"}, service.lastAccordionText);
    }

    @Test
    void testDefaultAreEnabledWithTextOnly() {
        service.reset();
        boolean result = service.areEnabled("Panel1");
        assertTrue(result);
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"Panel1"}, service.lastAccordionText);
    }

    @Test
    void testDefaultAreEnabledWithByLocator() {
        service.reset();
        boolean result = service.areEnabled(locator);
        assertTrue(result);
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastAccordionLocators);
    }

    @Test
    void testDefaultGetExpanded() {
        service.reset();
        List<String> list = service.getExpanded(container);
        assertEquals(MockAccordionService.EXPANDED_LIST, list);
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetCollapsed() {
        service.reset();
        List<String> list = service.getCollapsed(container);
        assertEquals(MockAccordionService.COLLAPSED_LIST, list);
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetAll() {
        service.reset();
        List<String> list = service.getAll(container);
        assertEquals(MockAccordionService.ALL_LIST, list);
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetTitle() {
        service.reset();
        String title = service.getTitle(locator);
        assertEquals(MockAccordionService.TITLE, title);
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastAccordionLocators);
    }

    @Test
    void testDefaultGetText() {
        service.reset();
        String text = service.getText(locator);
        assertEquals(MockAccordionService.TEXT, text);
        assertEquals(AccordionMockComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastAccordionLocators);
    }
}