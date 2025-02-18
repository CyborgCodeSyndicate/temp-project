package com.theairebellion.zeus.ui.components.loader;

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

public class LoaderServiceImplTest {

    private SmartWebDriver driver;
    private LoaderServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private Loader loaderMock;
    private DummyLoaderComponentType dummyType;
    private MockedStatic<ComponentFactory> factoryMock;

    public enum DummyLoaderComponentType implements LoaderComponentType {
        DUMMY;
        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    @BeforeEach
    public void setUp() {
        System.setProperty("project.package", "com.theairebellion.zeus");
        System.setProperty("loader.default.type", "DUMMY");
        driver = mock(SmartWebDriver.class);
        service = new LoaderServiceImpl(driver);
        container = mock(SmartWebElement.class);
        locator = By.id("loader");
        loaderMock = mock(Loader.class);
        dummyType = DummyLoaderComponentType.DUMMY;
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getLoaderComponent(eq(dummyType), eq(driver)))
                .thenReturn(loaderMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testIsVisibleContainer() {
        when(loaderMock.isVisible(container)).thenReturn(true);
        assertTrue(service.isVisible(dummyType, container));
        verify(loaderMock).isVisible(container);
    }

    @Test
    public void testIsVisibleLocator() {
        when(loaderMock.isVisible(locator)).thenReturn(true);
        assertTrue(service.isVisible(dummyType, locator));
        verify(loaderMock).isVisible(locator);
    }

    @Test
    public void testWaitToBeShownContainer() {
        service.waitToBeShown(dummyType, container, 5);
        verify(loaderMock).waitToBeShown(container, 5);
    }

    @Test
    public void testWaitToBeShownInt() {
        service.waitToBeShown(dummyType, 5);
        verify(loaderMock).waitToBeShown(5);
    }

    @Test
    public void testWaitToBeShownLocator() {
        service.waitToBeShown(dummyType, locator, 5);
        verify(loaderMock).waitToBeShown(locator, 5);
    }

    @Test
    public void testWaitToBeRemovedContainer() {
        service.waitToBeRemoved(dummyType, container, 10);
        verify(loaderMock).waitToBeRemoved(container, 10);
    }

    @Test
    public void testWaitToBeRemovedInt() {
        service.waitToBeRemoved(dummyType, 10);
        verify(loaderMock).waitToBeRemoved(10);
    }

    @Test
    public void testWaitToBeRemovedLocator() {
        service.waitToBeRemoved(dummyType, locator, 10);
        verify(loaderMock).waitToBeRemoved(locator, 10);
    }

    @Test
    public void testComponentCaching() {
        service.isVisible(dummyType, container);
        service.isVisible(dummyType, container);
        factoryMock.verify(() -> ComponentFactory.getLoaderComponent(eq(dummyType), eq(driver)), times(1));
    }
}