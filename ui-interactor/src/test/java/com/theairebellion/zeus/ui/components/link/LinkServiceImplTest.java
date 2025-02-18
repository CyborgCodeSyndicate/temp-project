package com.theairebellion.zeus.ui.components.link;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

public class LinkServiceImplTest {

    private SmartWebDriver driver;
    private LinkServiceImpl service;
    private SmartWebElement container;
    private Link linkMock;
    private DummyLinkComponentType dummyType;
    private By locator;
    private MockedStatic<ComponentFactory> factoryMock;

    public enum DummyLinkComponentType implements LinkComponentType {
        DUMMY;
        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    @BeforeEach
    public void setUp() {
        System.setProperty("project.package", "com.theairebellion.zeus");
        System.setProperty("link.default.type", "DUMMY");
        driver = mock(SmartWebDriver.class);
        service = new LinkServiceImpl(driver);
        container = mock(SmartWebElement.class);
        linkMock = mock(Link.class);
        dummyType = DummyLinkComponentType.DUMMY;
        locator = By.id("link");
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getLinkComponent(eq(dummyType), eq(driver)))
                .thenReturn(linkMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testClickContainerAndText() {
        service.click(dummyType, container, "LinkText");
        verify(linkMock).click(container, "LinkText");
    }

    @Test
    public void testClickContainer() {
        service.click(dummyType, container);
        verify(linkMock).click(container);
    }

    @Test
    public void testClickString() {
        service.click(dummyType, "LinkText");
        verify(linkMock).click("LinkText");
    }

    @Test
    public void testClickLocator() {
        service.click(dummyType, locator);
        verify(linkMock).click(locator);
    }

    @Test
    public void testDoubleClickContainerAndText() {
        service.doubleClick(dummyType, container, "LinkText");
        verify(linkMock).doubleClick(container, "LinkText");
    }

    @Test
    public void testDoubleClickContainer() {
        service.doubleClick(dummyType, container);
        verify(linkMock).doubleClick(container);
    }

    @Test
    public void testDoubleClickString() {
        service.doubleClick(dummyType, "LinkText");
        verify(linkMock).doubleClick("LinkText");
    }

    @Test
    public void testDoubleClickLocator() {
        service.doubleClick(dummyType, locator);
        verify(linkMock).doubleClick(locator);
    }

    @Test
    public void testIsEnabledContainerAndText() {
        when(linkMock.isEnabled(container, "LinkText")).thenReturn(true);
        assertTrue(service.isEnabled(dummyType, container, "LinkText"));
        verify(linkMock).isEnabled(container, "LinkText");
    }

    @Test
    public void testIsEnabledContainer() {
        when(linkMock.isEnabled(container)).thenReturn(true);
        assertTrue(service.isEnabled(dummyType, container));
        verify(linkMock).isEnabled(container);
    }

    @Test
    public void testIsEnabledString() {
        when(linkMock.isEnabled("LinkText")).thenReturn(true);
        assertTrue(service.isEnabled(dummyType, "LinkText"));
        verify(linkMock).isEnabled("LinkText");
    }

    @Test
    public void testIsEnabledLocator() {
        when(linkMock.isEnabled(locator)).thenReturn(true);
        assertTrue(service.isEnabled(dummyType, locator));
        verify(linkMock).isEnabled(locator);
    }

    @Test
    public void testIsVisibleContainerAndText() {
        when(linkMock.isVisible(container, "LinkText")).thenReturn(true);
        assertTrue(service.isVisible(dummyType, container, "LinkText"));
        verify(linkMock).isVisible(container, "LinkText");
    }

    @Test
    public void testIsVisibleContainer() {
        when(linkMock.isVisible(container)).thenReturn(true);
        assertTrue(service.isVisible(dummyType, container));
        verify(linkMock).isVisible(container);
    }

    @Test
    public void testIsVisibleString() {
        when(linkMock.isVisible("LinkText")).thenReturn(true);
        assertTrue(service.isVisible(dummyType, "LinkText"));
        verify(linkMock).isVisible("LinkText");
    }

    @Test
    public void testIsVisibleLocator() {
        when(linkMock.isVisible(locator)).thenReturn(true);
        assertTrue(service.isVisible(dummyType, locator));
        verify(linkMock).isVisible(locator);
    }

    @Test
    public void testComponentCaching() {
        service.click(dummyType, container, "ClickMe");
        service.isEnabled(dummyType, container, "EnableMe");
        service.isVisible(dummyType, container, "VisibleMe");
        factoryMock.verify(() -> ComponentFactory.getLinkComponent(eq(dummyType), eq(driver)), times(1));
    }
}