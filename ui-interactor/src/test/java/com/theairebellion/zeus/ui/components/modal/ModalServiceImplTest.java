package com.theairebellion.zeus.ui.components.modal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.modal.mock.MockModalComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

class ModalServiceImplTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private ModalServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private Modal modalMock;
    private MockModalComponentType mockModalComponentType;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeEach
    public void setUp() {
        driver = mock(SmartWebDriver.class);
        service = new ModalServiceImpl(driver);
        container = mock(SmartWebElement.class);
        locator = By.id("modal");
        modalMock = mock(Modal.class);
        mockModalComponentType = MockModalComponentType.DUMMY;
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getModalComponent(eq(mockModalComponentType), eq(driver)))
                .thenReturn(modalMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testIsOpened() {
        when(modalMock.isOpened()).thenReturn(true);
        assertTrue(service.isOpened(mockModalComponentType));
        verify(modalMock).isOpened();
    }

    @Test
    public void testClickButtonContainerAndText() {
        service.clickButton(mockModalComponentType, container, "OK");
        verify(modalMock).clickButton(container, "OK");
    }

    @Test
    public void testClickButtonTextOnly() {
        service.clickButton(mockModalComponentType, "Submit");
        verify(modalMock).clickButton("Submit");
    }

    @Test
    public void testClickButtonByLocator() {
        service.clickButton(mockModalComponentType, locator);
        verify(modalMock).clickButton(locator);
    }

    @Test
    public void testGetTitle() {
        when(modalMock.getTitle()).thenReturn("Modal Title");
        assertEquals("Modal Title", service.getTitle(mockModalComponentType));
        verify(modalMock).getTitle();
    }

    @Test
    public void testGetBodyText() {
        when(modalMock.getBodyText()).thenReturn("Body Content");
        assertEquals("Body Content", service.getBodyText(mockModalComponentType));
        verify(modalMock).getBodyText();
    }

    @Test
    public void testGetContentTitle() {
        when(modalMock.getContentTitle()).thenReturn("Content Title");
        assertEquals("Content Title", service.getContentTitle(mockModalComponentType));
        verify(modalMock).getContentTitle();
    }

    @Test
    public void testClose() {
        service.close(mockModalComponentType);
        verify(modalMock).close();
    }

    @Test
    public void testComponentCaching() {
        service.isOpened(mockModalComponentType);
        service.getTitle(mockModalComponentType);
        factoryMock.verify(() -> ComponentFactory.getModalComponent(eq(mockModalComponentType), eq(driver)), Mockito.times(1));
    }
}