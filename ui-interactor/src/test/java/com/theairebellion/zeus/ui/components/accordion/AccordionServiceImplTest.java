package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockAccordionComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccordionServiceImplTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private AccordionServiceImpl service;
    private SmartWebElement container;
    private Accordion accordionMock;
    private MockAccordionComponentType mockAccordionComponentType;
    private Strategy strategy;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeEach
    public void setUp() {
        driver = Mockito.mock(SmartWebDriver.class);
        service = new AccordionServiceImpl(driver);
        container = Mockito.mock(SmartWebElement.class);
        accordionMock = Mockito.mock(Accordion.class);
        mockAccordionComponentType = MockAccordionComponentType.DUMMY;
        strategy = Mockito.mock(Strategy.class);
        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getAccordionComponent(eq(mockAccordionComponentType), eq(driver)))
                .thenReturn(accordionMock);

    }

    @AfterEach
    public void tearDown() {
        factoryMock.close();
    }

    @Test
    public void testExpandWithContainerAndText() {
        service.expand(mockAccordionComponentType, container, "Panel1", "Panel2");
        verify(accordionMock).expand(container, "Panel1", "Panel2");
    }

    @Test
    public void testExpandWithContainerAndStrategy() {
        when(accordionMock.expand(container, strategy)).thenReturn("Expanded");
        String result = service.expand(mockAccordionComponentType, container, strategy);
        assertEquals("Expanded", result);
        verify(accordionMock).expand(container, strategy);
    }

    @Test
    public void testExpandWithTextOnly() {
        service.expand(mockAccordionComponentType, "Panel1");
        verify(accordionMock).expand("Panel1");
    }

    @Test
    public void testExpandWithByLocator() {
        By locator = By.id("accordion");
        service.expand(mockAccordionComponentType, locator);
        verify(accordionMock).expand(locator);
    }

    @Test
    public void testCollapseWithContainerAndText() {
        service.collapse(mockAccordionComponentType, container, "Panel1");
        verify(accordionMock).collapse(container, "Panel1");
    }

    @Test
    public void testCollapseWithContainerAndStrategy() {
        when(accordionMock.collapse(container, strategy)).thenReturn("Collapsed");
        String result = service.collapse(mockAccordionComponentType, container, strategy);
        assertEquals("Collapsed", result);
        verify(accordionMock).collapse(container, strategy);
    }

    @Test
    public void testCollapseWithTextOnly() {
        service.collapse(mockAccordionComponentType, "Panel1");
        verify(accordionMock).collapse("Panel1");
    }

    @Test
    public void testCollapseWithByLocator() {
        By locator = By.id("accordion");
        service.collapse(mockAccordionComponentType, locator);
        verify(accordionMock).collapse(locator);
    }

    @Test
    public void testAreEnabledWithContainerAndText() {
        when(accordionMock.areEnabled(container, "Panel1", "Panel2")).thenReturn(true);
        boolean result = service.areEnabled(mockAccordionComponentType, container, "Panel1", "Panel2");
        assertTrue(result);
        verify(accordionMock).areEnabled(container, "Panel1", "Panel2");
    }

    @Test
    public void testAreEnabledWithTextOnly() {
        when(accordionMock.areEnabled("Panel1")).thenReturn(true);
        boolean result = service.areEnabled(mockAccordionComponentType, "Panel1");
        assertTrue(result);
        verify(accordionMock).areEnabled("Panel1");
    }

    @Test
    public void testAreEnabledWithByLocator() {
        By locator = By.id("accordion");
        when(accordionMock.areEnabled(locator)).thenReturn(true);
        boolean result = service.areEnabled(mockAccordionComponentType, locator);
        assertTrue(result);
        verify(accordionMock).areEnabled(locator);
    }

    @Test
    public void testGetExpanded() {
        List<String> expandedList = Collections.singletonList("Panel1");
        when(accordionMock.getExpanded(container)).thenReturn(expandedList);
        List<String> result = service.getExpanded(mockAccordionComponentType, container);
        assertEquals(expandedList, result);
        verify(accordionMock).getExpanded(container);
    }

    @Test
    public void testGetCollapsed() {
        List<String> collapsedList = Arrays.asList("Panel2", "Panel3");
        when(accordionMock.getCollapsed(container)).thenReturn(collapsedList);
        List<String> result = service.getCollapsed(mockAccordionComponentType, container);
        assertEquals(collapsedList, result);
        verify(accordionMock).getCollapsed(container);
    }

    @Test
    public void testGetAll() {
        List<String> allPanels = Arrays.asList("Panel1", "Panel2", "Panel3");
        when(accordionMock.getAll(container)).thenReturn(allPanels);
        List<String> result = service.getAll(mockAccordionComponentType, container);
        assertEquals(allPanels, result);
        verify(accordionMock).getAll(container);
    }

    @Test
    public void testGetTitle() {
        By locator = By.id("title");
        when(accordionMock.getTitle(locator)).thenReturn("Title");
        String result = service.getTitle(mockAccordionComponentType, locator);
        assertEquals("Title", result);
        verify(accordionMock).getTitle(locator);
    }

    @Test
    public void testGetText() {
        By locator = By.id("text");
        when(accordionMock.getText(locator)).thenReturn("Text");
        String result = service.getText(mockAccordionComponentType, locator);
        assertEquals("Text", result);
        verify(accordionMock).getText(locator);
    }

    @Test
    public void testComponentCaching() {
        service.expand(mockAccordionComponentType, container, "Panel1");
        service.collapse(mockAccordionComponentType, container, "Panel1");
        factoryMock.verify(() -> ComponentFactory.getAccordionComponent(eq(mockAccordionComponentType), eq(driver)), Mockito.times(1));
    }
}
