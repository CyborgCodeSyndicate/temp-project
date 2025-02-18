package com.theairebellion.zeus.ui.components.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

public class ItemListServiceImplTest {

    private SmartWebDriver driver;
    private ItemListServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private Strategy strategy;
    private ItemList itemListMock;
    private DummyItemListComponentType dummyType;
    private MockedStatic<ComponentFactory> factoryMock;

    public enum DummyItemListComponentType implements ItemListComponentType {
        DUMMY;
        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    @BeforeEach
    public void setUp() {
        System.setProperty("project.package", "com.theairebellion.zeus");
        System.setProperty("list.default.type", "DUMMY");
        driver = mock(SmartWebDriver.class);
        service = new ItemListServiceImpl(driver);
        container = mock(SmartWebElement.class);
        locator = By.id("list");
        strategy = mock(Strategy.class);
        itemListMock = mock(ItemList.class);
        dummyType = DummyItemListComponentType.DUMMY;
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getListComponent(eq(dummyType), eq(driver)))
                .thenReturn(itemListMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testSelectContainerTextVarargs() {
        service.select(dummyType, container, "a", "b");
        verify(itemListMock).select(container, "a", "b");
    }

    @Test
    public void testSelectLocatorTextVarargs() {
        service.select(dummyType, locator, "a", "b");
        verify(itemListMock).select(locator, "a", "b");
    }

    @Test
    public void testSelectContainerStrategy() {
        when(itemListMock.select(container, strategy)).thenReturn("result");
        String result = service.select(dummyType, container, strategy);
        assertEquals("result", result);
        verify(itemListMock).select(container, strategy);
    }

    @Test
    public void testSelectLocatorStrategy() {
        when(itemListMock.select(locator, strategy)).thenReturn("result");
        String result = service.select(dummyType, locator, strategy);
        assertEquals("result", result);
        verify(itemListMock).select(locator, strategy);
    }

    @Test
    public void testSelectTextVarargs() {
        service.select(dummyType, "a", "b");
        verify(itemListMock).select("a", "b");
    }

    @Test
    public void testSelectByVarargs() {
        By by1 = By.id("1");
        By by2 = By.id("2");
        service.select(dummyType, by1, by2);
        verify(itemListMock).select(by1, by2);
    }

    @Test
    public void testDeSelectContainerTextVarargs() {
        service.deSelect(dummyType, container, "a", "b");
        verify(itemListMock).deSelect(container, "a", "b");
    }

    @Test
    public void testDeSelectLocatorTextVarargs() {
        service.deSelect(dummyType, locator, "a", "b");
        verify(itemListMock).deSelect(locator, "a", "b");
    }

    @Test
    public void testDeSelectContainerStrategy() {
        when(itemListMock.deSelect(container, strategy)).thenReturn("dresult");
        String result = service.deSelect(dummyType, container, strategy);
        assertEquals("dresult", result);
        verify(itemListMock).deSelect(container, strategy);
    }

    @Test
    public void testDeSelectLocatorStrategy() {
        when(itemListMock.deSelect(locator, strategy)).thenReturn("dresult");
        String result = service.deSelect(dummyType, locator, strategy);
        assertEquals("dresult", result);
        verify(itemListMock).deSelect(locator, strategy);
    }

    @Test
    public void testDeSelectTextVarargs() {
        service.deSelect(dummyType, "a", "b");
        verify(itemListMock).deSelect("a", "b");
    }

    @Test
    public void testDeSelectByVarargs() {
        By by1 = By.id("1");
        By by2 = By.id("2");
        service.deSelect(dummyType, by1, by2);
        verify(itemListMock).deSelect(by1, by2);
    }

    @Test
    public void testAreSelectedContainerText() {
        when(itemListMock.areSelected(container, "a", "b")).thenReturn(true);
        assertTrue(service.areSelected(dummyType, container, "a", "b"));
        verify(itemListMock).areSelected(container, "a", "b");
    }

    @Test
    public void testIsSelectedContainerText() {
        when(itemListMock.areSelected(container, "a")).thenReturn(true);
        assertTrue(service.isSelected(dummyType, container, "a"));
        verify(itemListMock).areSelected(container, "a");
    }

    @Test
    public void testAreSelectedLocatorText() {
        when(itemListMock.areSelected(locator, "a", "b")).thenReturn(true);
        assertTrue(service.areSelected(dummyType, locator, "a", "b"));
        verify(itemListMock).areSelected(locator, "a", "b");
    }

    @Test
    public void testIsSelectedLocatorText() {
        when(itemListMock.areSelected(locator, "a")).thenReturn(true);
        assertTrue(service.isSelected(dummyType, locator, "a"));
        verify(itemListMock).areSelected(locator, "a");
    }

    @Test
    public void testAreSelectedTextVarargs() {
        when(itemListMock.areSelected("a", "b")).thenReturn(true);
        assertTrue(service.areSelected(dummyType, "a", "b"));
        verify(itemListMock).areSelected("a", "b");
    }

    @Test
    public void testIsSelectedText() {
        when(itemListMock.areSelected("a")).thenReturn(true);
        assertTrue(service.isSelected(dummyType, "a"));
        verify(itemListMock).areSelected("a");
    }

    @Test
    public void testAreSelectedByVarargs() {
        By by1 = By.id("1");
        By by2 = By.id("2");
        when(itemListMock.areSelected(by1, by2)).thenReturn(true);
        assertTrue(service.areSelected(dummyType, by1, by2));
        verify(itemListMock).areSelected(by1, by2);
    }

    @Test
    public void testIsSelectedBy() {
        By by1 = By.id("1");
        when(itemListMock.areSelected(new By[]{by1})).thenReturn(true);
        assertTrue(service.isSelected(dummyType, by1));
        verify(itemListMock).areSelected(new By[]{by1});
    }

    @Test
    public void testAreEnabledContainerText() {
        when(itemListMock.areEnabled(container, "a", "b")).thenReturn(true);
        assertTrue(service.areEnabled(dummyType, container, "a", "b"));
        verify(itemListMock).areEnabled(container, "a", "b");
    }

    @Test
    public void testIsEnabledContainerText() {
        when(itemListMock.areEnabled(container, "a")).thenReturn(true);
        assertTrue(service.isEnabled(dummyType, container, "a"));
        verify(itemListMock).areEnabled(container, "a");
    }

    @Test
    public void testAreEnabledLocatorText() {
        when(itemListMock.areEnabled(locator, "a", "b")).thenReturn(true);
        assertTrue(service.areEnabled(dummyType, locator, "a", "b"));
        verify(itemListMock).areEnabled(locator, "a", "b");
    }

    @Test
    public void testIsEnabledLocatorText() {
        when(itemListMock.areEnabled(locator, "a")).thenReturn(true);
        assertTrue(service.isEnabled(dummyType, locator, "a"));
        verify(itemListMock).areEnabled(locator, "a");
    }

    @Test
    public void testAreEnabledTextVarargs() {
        when(itemListMock.areEnabled("a", "b")).thenReturn(true);
        assertTrue(service.areEnabled(dummyType, "a", "b"));
        verify(itemListMock).areEnabled("a", "b");
    }

    @Test
    public void testIsEnabledText() {
        when(itemListMock.areEnabled("a")).thenReturn(true);
        assertTrue(service.isEnabled(dummyType, "a"));
        verify(itemListMock).areEnabled("a");
    }

    @Test
    public void testAreEnabledByVarargs() {
        By by1 = By.id("1");
        By by2 = By.id("2");
        when(itemListMock.areEnabled(by1, by2)).thenReturn(true);
        assertTrue(service.areEnabled(dummyType, by1, by2));
        verify(itemListMock).areEnabled(by1, by2);
    }

    @Test
    public void testIsEnabledBy() {
        By by1 = By.id("1");
        when(itemListMock.areEnabled(new By[]{by1})).thenReturn(true);
        assertTrue(service.isEnabled(dummyType, by1));
        verify(itemListMock).areEnabled(new By[]{by1});
    }

    @Test
    public void testAreVisibleContainerText() {
        when(itemListMock.areVisible(container, "a", "b")).thenReturn(true);
        assertTrue(service.areVisible(dummyType, container, "a", "b"));
        verify(itemListMock).areVisible(container, "a", "b");
    }

    @Test
    public void testIsVisibleContainerText() {
        when(itemListMock.areVisible(container, "a")).thenReturn(true);
        assertTrue(service.isVisible(dummyType, container, "a"));
        verify(itemListMock).areVisible(container, "a");
    }

    @Test
    public void testAreVisibleLocatorText() {
        when(itemListMock.areVisible(locator, "a", "b")).thenReturn(true);
        assertTrue(service.areVisible(dummyType, locator, "a", "b"));
        verify(itemListMock).areVisible(locator, "a", "b");
    }

    @Test
    public void testIsVisibleLocatorText() {
        when(itemListMock.areVisible(locator, "a")).thenReturn(true);
        assertTrue(service.isVisible(dummyType, locator, "a"));
        verify(itemListMock).areVisible(locator, "a");
    }

    @Test
    public void testAreVisibleTextVarargs() {
        when(itemListMock.areVisible("a", "b")).thenReturn(true);
        assertTrue(service.areVisible(dummyType, "a", "b"));
        verify(itemListMock).areVisible("a", "b");
    }

    @Test
    public void testIsVisibleText() {
        when(itemListMock.areVisible("a")).thenReturn(true);
        assertTrue(service.isVisible(dummyType, "a"));
        verify(itemListMock).areVisible("a");
    }

    @Test
    public void testAreVisibleByVarargs() {
        By by1 = By.id("1");
        By by2 = By.id("2");
        when(itemListMock.areVisible(by1, by2)).thenReturn(true);
        assertTrue(service.areVisible(dummyType, by1, by2));
        verify(itemListMock).areVisible(by1, by2);
    }

    @Test
    public void testIsVisibleBy() {
        By by1 = By.id("1");
        when(itemListMock.areVisible(new By[]{by1})).thenReturn(true);
        assertTrue(service.isVisible(dummyType, by1));
        verify(itemListMock).areVisible(new By[]{by1});
    }

    @Test
    public void testGetSelectedContainer() {
        List<String> list = Arrays.asList("a", "b");
        when(itemListMock.getSelected(container)).thenReturn(list);
        assertEquals(list, service.getSelected(dummyType, container));
        verify(itemListMock).getSelected(container);
    }

    @Test
    public void testGetSelectedLocator() {
        List<String> list = Arrays.asList("a", "b");
        when(itemListMock.getSelected(locator)).thenReturn(list);
        assertEquals(list, service.getSelected(dummyType, locator));
        verify(itemListMock).getSelected(locator);
    }

    @Test
    public void testGetAllContainer() {
        List<String> list = Arrays.asList("a", "b", "c");
        when(itemListMock.getAll(container)).thenReturn(list);
        assertEquals(list, service.getAll(dummyType, container));
        verify(itemListMock).getAll(container);
    }

    @Test
    public void testGetAllLocator() {
        List<String> list = Arrays.asList("a", "b", "c");
        when(itemListMock.getAll(locator)).thenReturn(list);
        assertEquals(list, service.getAll(dummyType, locator));
        verify(itemListMock).getAll(locator);
    }

    @Test
    public void testInsertion() {
        service.insertion(dummyType, locator, "val1", "val2");
        verify(itemListMock).select(locator, "val1", "val2");
    }
}