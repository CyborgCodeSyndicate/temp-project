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
    public void testClickContainerAndText() {
        service.click(mockTabComponentType, container, "Tab1");
        verify(tabMock).click(container, "Tab1");
    }

    @Test
    public void testClickContainer() {
        service.click(mockTabComponentType, container);
        verify(tabMock).click(container);
    }

    @Test
    public void testClickString() {
        service.click(mockTabComponentType, "Tab1");
        verify(tabMock).click("Tab1");
    }

    @Test
    public void testClickLocator() {
        service.click(mockTabComponentType, locator);
        verify(tabMock).click(locator);
    }

    @Test
    public void testIsSelectedContainerAndText() {
        when(tabMock.isSelected(container, "Tab1")).thenReturn(true);
        assertTrue(service.isSelected(mockTabComponentType, container, "Tab1"));
        verify(tabMock).isSelected(container, "Tab1");
    }

    @Test
    public void testIsSelectedContainer() {
        when(tabMock.isSelected(container)).thenReturn(true);
        assertTrue(service.isSelected(mockTabComponentType, container));
        verify(tabMock).isSelected(container);
    }

    @Test
    public void testIsSelectedString() {
        when(tabMock.isSelected("Tab1")).thenReturn(true);
        assertTrue(service.isSelected(mockTabComponentType, "Tab1"));
        verify(tabMock).isSelected("Tab1");
    }

    @Test
    public void testIsSelectedLocator() {
        when(tabMock.isSelected(locator)).thenReturn(true);
        assertTrue(service.isSelected(mockTabComponentType, locator));
        verify(tabMock).isSelected(locator);
    }

    @Test
    public void testComponentCaching() {
        service.isSelected(mockTabComponentType, container, "Tab1");
        service.isSelected(mockTabComponentType, container, "Tab1");
        factoryMock.verify(() -> ComponentFactory.getTabComponent(eq(mockTabComponentType), eq(driver)), times(1));
    }
}