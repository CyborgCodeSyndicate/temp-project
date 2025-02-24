package com.theairebellion.zeus.ui.components.loader;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.loader.mock.MockLoaderComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

class LoaderServiceImplTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private LoaderServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private Loader loaderMock;
    private MockLoaderComponentType mockLoaderComponentType;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeEach
    public void setUp() {

        driver = mock(SmartWebDriver.class);
        service = new LoaderServiceImpl(driver);
        container = mock(SmartWebElement.class);
        locator = By.id("loader");
        loaderMock = mock(Loader.class);
        mockLoaderComponentType = MockLoaderComponentType.DUMMY;
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getLoaderComponent(eq(mockLoaderComponentType), eq(driver)))
                .thenReturn(loaderMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testIsVisibleContainer() {
        when(loaderMock.isVisible(container)).thenReturn(true);
        assertTrue(service.isVisible(mockLoaderComponentType, container));
        verify(loaderMock).isVisible(container);
    }

    @Test
    public void testIsVisibleLocator() {
        when(loaderMock.isVisible(locator)).thenReturn(true);
        assertTrue(service.isVisible(mockLoaderComponentType, locator));
        verify(loaderMock).isVisible(locator);
    }

    @Test
    public void testWaitToBeShownContainer() {
        service.waitToBeShown(mockLoaderComponentType, container, 5);
        verify(loaderMock).waitToBeShown(container, 5);
    }

    @Test
    public void testWaitToBeShownInt() {
        service.waitToBeShown(mockLoaderComponentType, 5);
        verify(loaderMock).waitToBeShown(5);
    }

    @Test
    public void testWaitToBeShownLocator() {
        service.waitToBeShown(mockLoaderComponentType, locator, 5);
        verify(loaderMock).waitToBeShown(locator, 5);
    }

    @Test
    public void testWaitToBeRemovedContainer() {
        service.waitToBeRemoved(mockLoaderComponentType, container, 10);
        verify(loaderMock).waitToBeRemoved(container, 10);
    }

    @Test
    public void testWaitToBeRemovedInt() {
        service.waitToBeRemoved(mockLoaderComponentType, 10);
        verify(loaderMock).waitToBeRemoved(10);
    }

    @Test
    public void testWaitToBeRemovedLocator() {
        service.waitToBeRemoved(mockLoaderComponentType, locator, 10);
        verify(loaderMock).waitToBeRemoved(locator, 10);
    }

    @Test
    public void testComponentCaching() {
        service.isVisible(mockLoaderComponentType, container);
        service.isVisible(mockLoaderComponentType, container);
        factoryMock.verify(() -> ComponentFactory.getLoaderComponent(eq(mockLoaderComponentType), eq(driver)), times(1));
    }
}