package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.list.mock.MockItemListComponentType;
import com.theairebellion.zeus.ui.components.list.mock.MockItemListService;
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

class ItemListServiceTest extends BaseUnitUITest {

    private MockItemListService service;
    private MockSmartWebElement container;
    private By locator;
    private Strategy strategy;

    @BeforeEach
    void setUp() {
        service = new MockItemListService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testList");
        strategy = Strategy.FIRST;
    }

    @Test
    void testDefaultSelectContainer() {
        service.reset();
        service.select(container, "a", "b");
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"a", "b"}, service.lastText);
    }

    @Test
    void testDefaultSelectLocator() {
        service.reset();
        service.select(locator, "a");
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
        assertArrayEquals(new String[]{"a"}, service.lastText);
    }

    @Test
    void testDefaultSelectContainerStrategy() {
        service.reset();
        String result = service.select(container, strategy);
        assertEquals("mockSelectStrategy", result);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals(strategy, service.lastStrategy);
    }

    @Test
    void testDefaultSelectLocatorStrategy() {
        service.reset();
        String result = service.select(locator, strategy);
        assertEquals("mockSelectStrategy", result);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
        assertEquals(strategy, service.lastStrategy);
    }

    @Test
    void testDefaultSelectTextVarargs() {
        service.reset();
        service.select("x", "y");
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"x", "y"}, service.lastText);
    }

    @Test
    void testDefaultSelectByVarargs() {
        service.reset();
        By l1 = By.id("l1");
        By l2 = By.id("l2");
        service.select(l1, l2);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{l1, l2}, service.lastLocators);
    }

    @Test
    void testDefaultDeSelectContainer() {
        service.reset();
        service.deSelect(container, "p", "q");
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"p", "q"}, service.lastText);
    }

    @Test
    void testDefaultDeSelectLocator() {
        service.reset();
        service.deSelect(locator, "p");
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
        assertArrayEquals(new String[]{"p"}, service.lastText);
    }

    @Test
    void testDefaultDeSelectContainerStrategy() {
        service.reset();
        String result = service.deSelect(container, strategy);
        assertEquals("mockDeSelectStrategy", result);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals(strategy, service.lastStrategy);
    }

    @Test
    void testDefaultDeSelectLocatorStrategy() {
        service.reset();
        String result = service.deSelect(locator, strategy);
        assertEquals("mockDeSelectStrategy", result);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
        assertEquals(strategy, service.lastStrategy);
    }

    @Test
    void testDefaultDeSelectTextVarargs() {
        service.reset();
        service.deSelect("p", "q");
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"p", "q"}, service.lastText);
    }

    @Test
    void testDefaultDeSelectByVarargs() {
        service.reset();
        By l1 = By.id("l1");
        By l2 = By.id("l2");
        service.deSelect(l1, l2);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{l1, l2}, service.lastLocators);
    }

    @Test
    void testDefaultAreSelectedContainer() {
        service.reset();
        service.returnBool = true;
        boolean sel = service.areSelected(container, "a", "b");
        assertTrue(sel);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"a", "b"}, service.lastText);
    }

    @Test
    void testDefaultIsSelectedContainer() {
        service.reset();
        service.returnBool = true;
        boolean sel = service.isSelected(container, "single");
        assertTrue(sel);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"single"}, service.lastText);
    }

    @Test
    void testDefaultAreSelectedLocator() {
        service.reset();
        service.returnBool = true;
        boolean sel = service.areSelected(locator, "a", "b");
        assertTrue(sel);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
        assertArrayEquals(new String[]{"a", "b"}, service.lastText);
    }

    @Test
    void testDefaultIsSelectedLocator() {
        service.reset();
        service.returnBool = true;
        boolean sel = service.isSelected(locator, "one");
        assertTrue(sel);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
        assertArrayEquals(new String[]{"one"}, service.lastText);
    }

    @Test
    void testDefaultAreSelectedTextVarargs() {
        service.reset();
        service.returnBool = true;
        boolean sel = service.areSelected("x", "y");
        assertTrue(sel);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"x", "y"}, service.lastText);
    }

    @Test
    void testDefaultIsSelectedText() {
        service.reset();
        service.returnBool = true;
        boolean sel = service.isSelected("z");
        assertTrue(sel);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"z"}, service.lastText);
    }

    @Test
    void testDefaultAreSelectedByVarargs() {
        service.reset();
        By l1 = By.id("1");
        By l2 = By.id("2");
        service.returnBool = true;
        boolean sel = service.areSelected(l1, l2);
        assertTrue(sel);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{l1, l2}, service.lastLocators);
    }

    @Test
    void testDefaultIsSelectedBy() {
        service.reset();
        By l1 = By.id("3");
        service.returnBool = true;
        boolean sel = service.isSelected(l1, "someTxt");
        // This calls isSelected(DEFAULT_TYPE, l1, "someTxt") => isSelected(<<component>>, By l, "someTxt")
        // The interface calls are slightly different. We'll just verify last state.
        assertTrue(sel);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{l1}, service.lastLocators);
        assertArrayEquals(new String[]{"someTxt"}, service.lastText);
    }

    @Test
    void testDefaultAreEnabledContainer() {
        service.reset();
        service.returnBool = true;
        boolean en = service.areEnabled(container, "1", "2");
        assertTrue(en);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"1", "2"}, service.lastText);
    }

    @Test
    void testDefaultIsEnabledContainer() {
        service.reset();
        service.returnBool = true;
        boolean en = service.isEnabled(container, "one");
        assertTrue(en);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"one"}, service.lastText);
    }

    @Test
    void testDefaultAreEnabledLocator() {
        service.reset();
        service.returnBool = true;
        boolean en = service.areEnabled(locator, "xxx", "yyy");
        assertTrue(en);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
        assertArrayEquals(new String[]{"xxx", "yyy"}, service.lastText);
    }

    @Test
    void testDefaultIsEnabledLocator() {
        service.reset();
        service.returnBool = true;
        boolean en = service.isEnabled(locator, "solo");
        assertTrue(en);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
        assertArrayEquals(new String[]{"solo"}, service.lastText);
    }

    @Test
    void testDefaultAreEnabledTextVarargs() {
        service.reset();
        service.returnBool = true;
        boolean en = service.areEnabled("a", "b");
        assertTrue(en);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"a", "b"}, service.lastText);
    }

    @Test
    void testDefaultIsEnabledText() {
        service.reset();
        service.returnBool = true;
        boolean en = service.isEnabled("something");
        assertTrue(en);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"something"}, service.lastText);
    }

    @Test
    void testDefaultAreEnabledByVarargs() {
        service.reset();
        By l1 = By.id("l1");
        By l2 = By.id("l2");
        service.returnBool = true;
        boolean en = service.areEnabled(l1, l2);
        assertTrue(en);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{l1, l2}, service.lastLocators);
    }

    @Test
    void testDefaultIsEnabledBy() {
        service.reset();
        By l1 = By.id("xyz");
        service.returnBool = true;
        boolean en = service.isEnabled(l1);
        assertTrue(en);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{l1}, service.lastLocators);
    }

    @Test
    void testDefaultAreVisibleContainer() {
        service.reset();
        service.returnBool = true;
        boolean vis = service.areVisible(container, "x", "y");
        assertTrue(vis);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"x", "y"}, service.lastText);
    }

    @Test
    void testDefaultIsVisibleContainer() {
        service.reset();
        service.returnBool = true;
        boolean vis = service.isVisible(container, "X");
        assertTrue(vis);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"X"}, service.lastText);
    }

    @Test
    void testDefaultAreVisibleLocator() {
        service.reset();
        service.returnBool = true;
        boolean vis = service.areVisible(locator, "a", "b");
        assertTrue(vis);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
        assertArrayEquals(new String[]{"a", "b"}, service.lastText);
    }

    @Test
    void testDefaultIsVisibleLocator() {
        service.reset();
        service.returnBool = true;
        boolean vis = service.isVisible(locator, "zz");
        assertTrue(vis);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
        assertArrayEquals(new String[]{"zz"}, service.lastText);
    }

    @Test
    void testDefaultAreVisibleTextVarargs() {
        service.reset();
        service.returnBool = true;
        boolean vis = service.areVisible("cc", "dd");
        assertTrue(vis);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"cc", "dd"}, service.lastText);
    }

    @Test
    void testDefaultIsVisibleText() {
        service.reset();
        service.returnBool = true;
        boolean vis = service.isVisible("gg");
        assertTrue(vis);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"gg"}, service.lastText);
    }

    @Test
    void testDefaultAreVisibleByVarargs() {
        service.reset();
        By l1 = By.id("aa");
        By l2 = By.id("bb");
        service.returnBool = true;
        boolean vis = service.areVisible(l1, l2);
        assertTrue(vis);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{l1, l2}, service.lastLocators);
    }

    @Test
    void testDefaultIsVisibleBy() {
        service.reset();
        By l1 = By.id("cc");
        service.returnBool = true;
        boolean vis = service.isVisible(l1);
        assertTrue(vis);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{l1}, service.lastLocators);
    }

    @Test
    void testDefaultGetSelectedContainer() {
        service.reset();
        service.returnSelectedList = List.of("x");
        List<String> sel = service.getSelected(container);
        assertEquals(List.of("x"), sel);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetSelectedLocator() {
        service.reset();
        service.returnSelectedList = List.of("a", "b");
        List<String> sel = service.getSelected(locator);
        assertEquals(List.of("a", "b"), sel);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
    }

    @Test
    void testDefaultGetAllContainer() {
        service.reset();
        service.returnAllList = List.of("q", "r");
        List<String> all = service.getAll(container);
        assertEquals(List.of("q", "r"), all);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetAllLocator() {
        service.reset();
        service.returnAllList = List.of("u", "v");
        List<String> all = service.getAll(locator);
        assertEquals(List.of("u", "v"), all);
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
    }

    @Test
    void testInsertionMethod() {
        service.reset();
        service.insertion(MockItemListComponentType.DUMMY, locator, "arrOne", "arrTwo");
        assertEquals(MockItemListComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastLocators);
        assertArrayEquals(new String[]{"arrOne", "arrTwo"}, service.lastText);
    }
}