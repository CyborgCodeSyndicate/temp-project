package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ToggleServiceDefaultMethodsTest extends BaseUnitUITest {

    private ToggleService service;
    private SmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        container = mock(SmartWebElement.class);
        locator = By.id("testToggle");
        service = new ToggleService() {
            @Override
            public void activate(ToggleComponentType componentType, SmartWebElement c, String toggleText) {
            }
            @Override
            public void activate(ToggleComponentType componentType, String toggleText) {
            }
            @Override
            public void activate(ToggleComponentType componentType, By toggleLocator) {
            }
            @Override
            public void deactivate(ToggleComponentType componentType, SmartWebElement c, String toggleText) {
            }
            @Override
            public void deactivate(ToggleComponentType componentType, String toggleText) {
            }
            @Override
            public void deactivate(ToggleComponentType componentType, By toggleLocator) {
            }
            @Override
            public boolean isEnabled(ToggleComponentType componentType, SmartWebElement c, String toggleText) {
                return true;
            }
            @Override
            public boolean isEnabled(ToggleComponentType componentType, String toggleText) {
                return true;
            }
            @Override
            public boolean isEnabled(ToggleComponentType componentType, By toggleLocator) {
                return true;
            }
            @Override
            public boolean isActivated(ToggleComponentType componentType, SmartWebElement c, String toggleText) {
                return true;
            }
            @Override
            public boolean isActivated(ToggleComponentType componentType, String toggleText) {
                return true;
            }
            @Override
            public boolean isActivated(ToggleComponentType componentType, By toggleLocator) {
                return true;
            }
        };
    }

    @Test
    void testActivateContainerAndText() {
        service.activate(container, "On");
    }

    @Test
    void testActivateTextOnly() {
        service.activate("On");
    }

    @Test
    void testActivateLocator() {
        service.activate(locator);
    }

    @Test
    void testDeactivateContainerAndText() {
        service.deactivate(container, "Off");
    }

    @Test
    void testDeactivateTextOnly() {
        service.deactivate("Off");
    }

    @Test
    void testDeactivateLocator() {
        service.deactivate(locator);
    }

    @Test
    void testIsEnabledContainerAndText() {
        assertTrue(service.isEnabled(container, "On"));
    }

    @Test
    void testIsEnabledTextOnly() {
        assertTrue(service.isEnabled("On"));
    }

    @Test
    void testIsEnabledLocator() {
        assertTrue(service.isEnabled(locator));
    }

    @Test
    void testIsActivatedContainerAndText() {
        assertTrue(service.isActivated(container, "On"));
    }

    @Test
    void testIsActivatedTextOnly() {
        assertTrue(service.isActivated("On"));
    }

    @Test
    void testIsActivatedLocator() {
        assertTrue(service.isActivated(locator));
    }
}