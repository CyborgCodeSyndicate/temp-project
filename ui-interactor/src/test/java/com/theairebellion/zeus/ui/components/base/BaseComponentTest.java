package com.theairebellion.zeus.ui.components.base;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.base.mock.MockBaseComponent;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

class BaseComponentTest extends BaseUnitUITest {

    private MockBaseComponent component;
    private SmartWebDriver driver;

    @BeforeEach
    public void setUp() {

        WebDriver original = Mockito.mock(WebDriver.class);
        driver = new SmartWebDriver(original);
        component = new MockBaseComponent(driver);
    }

    @Test
    public void testDriverIsSet() {
        assertNotNull(component.driver);
        assertEquals(driver, component.driver);
    }
}