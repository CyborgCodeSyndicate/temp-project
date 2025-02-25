package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.checkbox.mock.MockCheckboxComponentType;
import com.theairebellion.zeus.ui.components.checkbox.mock.MockCheckboxService;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CheckboxServiceTest extends BaseUnitUITest {

    private MockCheckboxService service;
    private MockSmartWebElement container;
    private By locator;
    private Strategy strategy;

    @BeforeEach
    void setUp() {
        service = new MockCheckboxService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testCheckbox");
        strategy = Strategy.FIRST;
    }

    @Test
    void testDefaultSelectWithContainerAndText() {
        service.reset();
        service.select(container, "A", "B");
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"A", "B"}, service.lastCheckboxText);
    }

    @Test
    void testDefaultSelectWithContainerAndStrategy() {
        service.reset();
        String result = service.select(container, strategy);
        assertEquals("selectStrategyMock", result);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals(strategy, service.lastStrategy);
    }

    @Test
    void testDefaultSelectWithTextOnly() {
        service.reset();
        service.select("A");
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"A"}, service.lastCheckboxText);
    }

    @Test
    void testDefaultSelectWithLocator() {
        service.reset();
        service.select(locator);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastCheckboxLocators);
    }

    @Test
    void testDefaultDeSelectWithContainerAndText() {
        service.reset();
        service.deSelect(container, "C", "D");
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"C", "D"}, service.lastCheckboxText);
    }

    @Test
    void testDefaultDeSelectWithContainerAndStrategy() {
        service.reset();
        String result = service.deSelect(container, strategy);
        assertEquals("deSelectStrategyMock", result);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals(strategy, service.lastStrategy);
    }

    @Test
    void testDefaultDeSelectWithTextOnly() {
        service.reset();
        service.deSelect("X");
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"X"}, service.lastCheckboxText);
    }

    @Test
    void testDefaultDeSelectWithLocator() {
        service.reset();
        service.deSelect(locator);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastCheckboxLocators);
    }

    @Test
    void testDefaultAreSelectedWithContainerAndText() {
        service.reset();
        boolean selected = service.areSelected(container, "A", "B");
        assertTrue(selected);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"A", "B"}, service.lastCheckboxText);
    }

    @Test
    void testDefaultAreSelectedWithTextOnly() {
        service.reset();
        boolean selected = service.areSelected("A");
        assertTrue(selected);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"A"}, service.lastCheckboxText);
    }

    @Test
    void testDefaultAreSelectedWithLocator() {
        service.reset();
        boolean selected = service.areSelected(locator);
        assertTrue(selected);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastCheckboxLocators);
    }

    @Test
    void testDefaultIsSelectedWithContainer() {
        service.reset();
        boolean selected = service.isSelected(container, "A");
        assertTrue(selected);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"A"}, service.lastCheckboxText);
    }

    @Test
    void testDefaultIsSelectedWithTextOnly() {
        service.reset();
        boolean selected = service.isSelected("A");
        assertTrue(selected);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"A"}, service.lastCheckboxText);
    }

    @Test
    void testDefaultIsSelectedWithLocator() {
        service.reset();
        boolean selected = service.isSelected(locator);
        assertTrue(selected);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastCheckboxLocators);
    }

    @Test
    void testDefaultAreEnabledWithContainerAndText() {
        service.reset();
        boolean enabled = service.areEnabled(container, "X");
        assertTrue(enabled);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"X"}, service.lastCheckboxText);
    }

    @Test
    void testDefaultAreEnabledWithTextOnly() {
        service.reset();
        boolean enabled = service.areEnabled("Y");
        assertTrue(enabled);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"Y"}, service.lastCheckboxText);
    }

    @Test
    void testDefaultAreEnabledWithLocator() {
        service.reset();
        boolean enabled = service.areEnabled(locator);
        assertTrue(enabled);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastCheckboxLocators);
    }

    @Test
    void testDefaultIsEnabledWithContainer() {
        service.reset();
        boolean enabled = service.isEnabled(container, "Z");
        assertTrue(enabled);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertArrayEquals(new String[]{"Z"}, service.lastCheckboxText);
    }

    @Test
    void testDefaultIsEnabledWithTextOnly() {
        service.reset();
        boolean enabled = service.isEnabled("Z");
        assertTrue(enabled);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new String[]{"Z"}, service.lastCheckboxText);
    }

    @Test
    void testDefaultIsEnabledWithLocator() {
        service.reset();
        boolean enabled = service.isEnabled(locator);
        assertTrue(enabled);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastCheckboxLocators);
    }

    @Test
    void testDefaultGetSelectedWithContainer() {
        service.reset();
        service.returnSelectedList = List.of("A");
        List<String> result = service.getSelected(container);
        assertEquals(List.of("A"), result);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetSelectedWithLocator() {
        service.reset();
        service.returnSelectedList = List.of("X", "Y");
        List<String> result = service.getSelected(locator);
        assertEquals(List.of("X", "Y"), result);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastCheckboxLocators);
    }

    @Test
    void testDefaultGetAllWithContainer() {
        service.reset();
        service.returnAllList = List.of("A", "B", "C");
        List<String> result = service.getAll(container);
        assertEquals(List.of("A", "B", "C"), result);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetAllWithLocator() {
        service.reset();
        service.returnAllList = List.of("D", "E");
        List<String> result = service.getAll(locator);
        assertEquals(List.of("D", "E"), result);
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastCheckboxLocators);
    }

    @Test
    void testInsertionMethod() {
        service.reset();
        service.insertion(MockCheckboxComponentType.DUMMY, locator, "Inserted");
        assertEquals(MockCheckboxComponentType.DUMMY, service.lastComponentType);
        assertArrayEquals(new By[]{locator}, service.lastCheckboxLocators);
        assertArrayEquals(new String[]{"Inserted"}, service.lastCheckboxText);
    }
}