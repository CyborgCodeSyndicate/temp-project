package com.theairebellion.zeus.ui.components.base;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.base.mock.MockBaseComponent;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("BaseComponent Tests")
class BaseComponentTest extends BaseUnitUITest {

    @Mock
    private WebDriver webDriver;

    private SmartWebDriver smartWebDriver;
    private MockBaseComponent component;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        smartWebDriver = new SmartWebDriver(webDriver);
        component = new MockBaseComponent(smartWebDriver);
    }

    @Test
    @DisplayName("Should initialize with the correct WebDriver")
    void testDriverIsCorrectlySet() {
        // Then
        assertNotNull(component.driver, "Driver should not be null");
        assertSame(smartWebDriver, component.driver, "Component should use the provided SmartWebDriver");
    }
}