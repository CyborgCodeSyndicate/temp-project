package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.tab.mock.MockTabComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TabServiceImplTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private TabServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private MockTabComponentType mockTabComponentType;
    private Tab tabMock;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeEach
    public void setUp() {
        driver = mock(SmartWebDriver.class);
        service = new TabServiceImpl(driver);
        container = mock(SmartWebElement.class);
        locator = By.id("tab");
        mockTabComponentType = MockTabComponentType.DUMMY;
        tabMock = mock(Tab.class);
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getTabComponent(eq(mockTabComponentType), eq(driver)))
                .thenReturn(tabMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    void testClickContainerAndText() {
        service.click(mockTabComponentType, container, "Tab1");
        verify(tabMock).click(container, "Tab1");
    }

    @Test
    void testClickContainerOnly() {
        service.click(mockTabComponentType, container);
        verify(tabMock).click(container);
    }

    @Test
    void testClickTextOnly() {
        service.click(mockTabComponentType, "Tab1");
        verify(tabMock).click("Tab1");
    }

    @Test
    void testClickByLocator() {
        service.click(mockTabComponentType, locator);
        verify(tabMock).click(locator);
    }

    @Test
    void testIsSelectedContainerAndText() {
        when(tabMock.isSelected(container, "Tab1")).thenReturn(true);
        assertTrue(service.isSelected(mockTabComponentType, container, "Tab1"));
        verify(tabMock).isSelected(container, "Tab1");
    }

    @Test
    void testIsSelectedContainerOnly() {
        when(tabMock.isSelected(container)).thenReturn(true);
        assertTrue(service.isSelected(mockTabComponentType, container));
        verify(tabMock).isSelected(container);
    }

    @Test
    void testIsSelectedTextOnly() {
        when(tabMock.isSelected("Tab1")).thenReturn(true);
        assertTrue(service.isSelected(mockTabComponentType, "Tab1"));
        verify(tabMock).isSelected("Tab1");
    }

    @Test
    void testIsSelectedByLocator() {
        when(tabMock.isSelected(locator)).thenReturn(true);
        assertTrue(service.isSelected(mockTabComponentType, locator));
        verify(tabMock).isSelected(locator);
    }

    @Test
    void testIsEnabledContainerAndText() {
        when(tabMock.isEnabled(container, "Tab2")).thenReturn(true);
        assertTrue(service.isEnabled(mockTabComponentType, container, "Tab2"));
        verify(tabMock).isEnabled(container, "Tab2");
    }

    @Test
    void testIsEnabledContainerOnly() {
        when(tabMock.isEnabled(container)).thenReturn(true);
        assertTrue(service.isEnabled(mockTabComponentType, container));
        verify(tabMock).isEnabled(container);
    }

    @Test
    void testIsEnabledTextOnly() {
        when(tabMock.isEnabled("Tab2")).thenReturn(true);
        assertTrue(service.isEnabled(mockTabComponentType, "Tab2"));
        verify(tabMock).isEnabled("Tab2");
    }

    @Test
    void testIsEnabledByLocator() {
        when(tabMock.isEnabled(locator)).thenReturn(true);
        assertTrue(service.isEnabled(mockTabComponentType, locator));
        verify(tabMock).isEnabled(locator);
    }

    @Test
    void testIsVisibleContainerAndText() {
        when(tabMock.isVisible(container, "Tab3")).thenReturn(true);
        assertTrue(service.isVisible(mockTabComponentType, container, "Tab3"));
        verify(tabMock).isVisible(container, "Tab3");
    }

    @Test
    void testIsVisibleContainerOnly() {
        when(tabMock.isVisible(container)).thenReturn(true);
        assertTrue(service.isVisible(mockTabComponentType, container));
        verify(tabMock).isVisible(container);
    }

    @Test
    void testIsVisibleTextOnly() {
        when(tabMock.isVisible("Tab3")).thenReturn(true);
        assertTrue(service.isVisible(mockTabComponentType, "Tab3"));
        verify(tabMock).isVisible("Tab3");
    }

    @Test
    void testIsVisibleByLocator() {
        when(tabMock.isVisible(locator)).thenReturn(true);
        assertTrue(service.isVisible(mockTabComponentType, locator));
        verify(tabMock).isVisible(locator);
    }

    @Test
    void testComponentCaching() {
        service.isSelected(mockTabComponentType, container, "Tab1");
        service.isSelected(mockTabComponentType, container, "Tab1");
        factoryMock.verify(() -> ComponentFactory.getTabComponent(eq(mockTabComponentType), eq(driver)), times(1));
    }
}