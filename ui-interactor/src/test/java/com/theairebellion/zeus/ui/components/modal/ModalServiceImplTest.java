package com.theairebellion.zeus.ui.components.modal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

public class ModalServiceImplTest {

    private SmartWebDriver driver;
    private ModalServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private Modal modalMock;
    private DummyModalComponentType dummyType;
    private MockedStatic<ComponentFactory> factoryMock;

    public enum DummyModalComponentType implements ModalComponentType {
        DUMMY;
        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    @BeforeEach
    public void setUp() {
        System.setProperty("project.package", "com.theairebellion.zeus");
        System.setProperty("modal.default.type", "DUMMY");
        driver = mock(SmartWebDriver.class);
        service = new ModalServiceImpl(driver);
        container = mock(SmartWebElement.class);
        locator = By.id("modal");
        modalMock = mock(Modal.class);
        dummyType = DummyModalComponentType.DUMMY;
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getModalComponent(eq(dummyType), eq(driver)))
                .thenReturn(modalMock);
    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testIsOpened() {
        when(modalMock.isOpened()).thenReturn(true);
        assertTrue(service.isOpened(dummyType));
        verify(modalMock).isOpened();
    }

    @Test
    public void testClickButtonContainerAndText() {
        service.clickButton(dummyType, container, "OK");
        verify(modalMock).clickButton(container, "OK");
    }

    @Test
    public void testClickButtonTextOnly() {
        service.clickButton(dummyType, "Submit");
        verify(modalMock).clickButton("Submit");
    }

    @Test
    public void testClickButtonByLocator() {
        service.clickButton(dummyType, locator);
        verify(modalMock).clickButton(locator);
    }

    @Test
    public void testGetTitle() {
        when(modalMock.getTitle()).thenReturn("Modal Title");
        assertEquals("Modal Title", service.getTitle(dummyType));
        verify(modalMock).getTitle();
    }

    @Test
    public void testGetBodyText() {
        when(modalMock.getBodyText()).thenReturn("Body Content");
        assertEquals("Body Content", service.getBodyText(dummyType));
        verify(modalMock).getBodyText();
    }

    @Test
    public void testGetContentTitle() {
        when(modalMock.getContentTitle()).thenReturn("Content Title");
        assertEquals("Content Title", service.getContentTitle(dummyType));
        verify(modalMock).getContentTitle();
    }

    @Test
    public void testClose() {
        service.close(dummyType);
        verify(modalMock).close();
    }

    @Test
    public void testComponentCaching() {
        service.isOpened(dummyType);
        service.getTitle(dummyType);
        factoryMock.verify(() -> ComponentFactory.getModalComponent(eq(dummyType), eq(driver)), Mockito.times(1));
    }
}