package com.theairebellion.zeus.ui.components.base;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.base.mock.MockComponent;
import com.theairebellion.zeus.ui.components.base.mock.MockComponentService;
import com.theairebellion.zeus.ui.components.base.mock.MockType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

class AbstractComponentServiceTest extends BaseUnitUITest {

    private MockComponentService service;

    @BeforeEach
    public void setUp() {

        WebDriver original = Mockito.mock(WebDriver.class);
        SmartWebDriver driver = new SmartWebDriver(original);
        service = new MockComponentService(driver);
    }

    @Test
    public void testGetOrCreateComponentCaching() {
        MockComponent compA1 = service.getOrCreateComponent(MockType.A);
        MockComponent compA2 = service.getOrCreateComponent(MockType.A);
        MockComponent compB = service.getOrCreateComponent(MockType.B);
        assertNotNull(compA1);
        assertSame(compA1, compA2);
        assertNotSame(compA1, compB);
        assertEquals("A", compA1.getValue());
        assertEquals("B", compB.getValue());
    }
}