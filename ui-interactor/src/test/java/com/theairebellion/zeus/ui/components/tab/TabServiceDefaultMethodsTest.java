package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class TabServiceDefaultMethodsTest extends BaseUnitUITest {

    private TabService service;
    private SmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        container = mock(SmartWebElement.class);
        locator = By.id("testTab");
        service = new TabService() {
            @Override
            public void tableInsertion(SmartWebElement cellElement, ComponentType componentType, String... values) {}
            @Override
            public boolean isSelected(TabComponentType componentType, SmartWebElement c, String text) {
                return true;
            }
            @Override
            public boolean isSelected(TabComponentType componentType, SmartWebElement c) {
                return true;
            }
            @Override
            public boolean isSelected(TabComponentType componentType, String text) {
                return true;
            }
            @Override
            public boolean isSelected(TabComponentType componentType, By by) {
                return true;
            }
            @Override
            public <T extends ButtonComponentType> void click(T componentType, SmartWebElement c, String text) {
            }
            @Override
            public <T extends ButtonComponentType> void click(T componentType, SmartWebElement c) {
            }
            @Override
            public <T extends ButtonComponentType> void click(T componentType, String text) {
            }
            @Override
            public <T extends ButtonComponentType> void click(T componentType, By by) {
            }
            @Override
            public <T extends ButtonComponentType> boolean isEnabled(T componentType, SmartWebElement c, String text) {
                return true;
            }
            @Override
            public <T extends ButtonComponentType> boolean isEnabled(T componentType, SmartWebElement c) {
                return true;
            }
            @Override
            public <T extends ButtonComponentType> boolean isEnabled(T componentType, String text) {
                return true;
            }
            @Override
            public <T extends ButtonComponentType> boolean isEnabled(T componentType, By by) {
                return true;
            }
            @Override
            public <T extends ButtonComponentType> boolean isVisible(T componentType, SmartWebElement c, String text) {
                return true;
            }
            @Override
            public <T extends ButtonComponentType> boolean isVisible(T componentType, SmartWebElement c) {
                return true;
            }
            @Override
            public <T extends ButtonComponentType> boolean isVisible(T componentType, String text) {
                return true;
            }
            @Override
            public <T extends ButtonComponentType> boolean isVisible(T componentType, By by) {
                return true;
            }
        };
    }

    @Test
    void testIsSelectedContainerAndText() {
        assertTrue(service.isSelected(container, "TabA"));
    }

    @Test
    void testIsSelectedContainerOnly() {
        assertTrue(service.isSelected(container));
    }

    @Test
    void testIsSelectedTextOnly() {
        assertTrue(service.isSelected("TabA"));
    }

    @Test
    void testIsSelectedLocator() {
        assertTrue(service.isSelected(locator));
    }

    @Test
    void testIsEnabledContainerAndText() {
        assertTrue(service.isEnabled(container, "BtnA"));
    }

    @Test
    void testIsEnabledContainerOnly() {
        assertTrue(service.isEnabled(container));
    }

    @Test
    void testIsEnabledTextOnly() {
        assertTrue(service.isEnabled("BtnA"));
    }

    @Test
    void testIsEnabledLocator() {
        assertTrue(service.isEnabled(locator));
    }

    @Test
    void testIsVisibleContainerAndText() {
        assertTrue(service.isVisible(container, "BtnVis"));
    }

    @Test
    void testIsVisibleContainerOnly() {
        assertTrue(service.isVisible(container));
    }

    @Test
    void testIsVisibleTextOnly() {
        assertTrue(service.isVisible("BtnVis"));
    }

    @Test
    void testIsVisibleLocator() {
        assertTrue(service.isVisible(locator));
    }
}