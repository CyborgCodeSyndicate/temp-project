package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.loader.mock.MockLoaderComponentType;
import com.theairebellion.zeus.ui.components.loader.mock.MockLoaderService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class LoaderServiceTest extends BaseUnitUITest {

    private MockLoaderService service;
    private MockLoaderComponentType mockLoaderType;
    private SmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        service = new MockLoaderService();
        mockLoaderType = MockLoaderComponentType.DUMMY;
        container = mock(SmartWebElement.class);
        locator = By.id("testLoader");
    }

    @Test
    void testDefaultIsVisibleContainer() {
        service.reset();
        service.returnVisible = true;
        boolean visible = service.isVisible(container);
        assertTrue(visible);
        assertEquals(MockLoaderComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultIsVisibleLocator() {
        service.reset();
        service.returnVisible = true;
        boolean visible = service.isVisible(locator);
        assertTrue(visible);
        assertEquals(MockLoaderComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultWaitToBeShownContainer() {
        service.reset();
        service.waitToBeShown(container, 5);
        assertEquals(MockLoaderComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals(5, service.lastSeconds);
    }

    @Test
    void testDefaultWaitToBeShownInt() {
        service.reset();
        service.waitToBeShown(7);
        assertEquals(MockLoaderComponentType.DUMMY, service.lastComponentType);
        assertEquals(7, service.lastSeconds);
    }

    @Test
    void testDefaultWaitToBeShownLocator() {
        service.reset();
        service.waitToBeShown(locator, 10);
        assertEquals(MockLoaderComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
        assertEquals(10, service.lastSeconds);
    }

    @Test
    void testDefaultWaitToBeRemovedContainer() {
        service.reset();
        service.waitToBeRemoved(container, 3);
        assertEquals(MockLoaderComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals(3, service.lastSeconds);
    }

    @Test
    void testDefaultWaitToBeRemovedInt() {
        service.reset();
        service.waitToBeRemoved(9);
        assertEquals(MockLoaderComponentType.DUMMY, service.lastComponentType);
        assertEquals(9, service.lastSeconds);
    }

    @Test
    void testDefaultWaitToBeRemovedLocator() {
        service.reset();
        service.waitToBeRemoved(locator, 11);
        assertEquals(MockLoaderComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
        assertEquals(11, service.lastSeconds);
    }

    @Test
    void testWaitToBeShownAndRemovedContainer() {
        service.reset();
        service.waitToBeShownAndRemoved(mockLoaderType, container, 2, 4);
        assertEquals(mockLoaderType, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals(4, service.lastSeconds);
    }

    @Test
    void testWaitToBeShownAndRemovedInt() {
        service.reset();
        service.waitToBeShownAndRemoved(mockLoaderType, 3, 5);
        assertEquals(mockLoaderType, service.lastComponentType);
        assertEquals(5, service.lastSeconds);
    }

    @Test
    void testWaitToBeShownAndRemovedLocator() {
        service.reset();
        service.waitToBeShownAndRemoved(mockLoaderType, locator, 1, 6);
        assertEquals(mockLoaderType, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
        assertEquals(6, service.lastSeconds);
    }
}