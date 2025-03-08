package com.theairebellion.zeus.ui.components.base;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.base.mock.MockComponent;
import com.theairebellion.zeus.ui.components.base.mock.MockComponentService;
import com.theairebellion.zeus.ui.components.base.mock.MockType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AbstractComponentService Tests")
class AbstractComponentServiceTest extends BaseUnitUITest {

    @Mock
    private WebDriver webDriver;

    private SmartWebDriver smartWebDriver;
    private MockComponentService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        smartWebDriver = new SmartWebDriver(webDriver);
        service = new MockComponentService(smartWebDriver);
    }

    @Nested
    @DisplayName("Component caching tests")
    class ComponentCachingTests {

        @Test
        @DisplayName("Should return the same component instance for the same type")
        void testComponentCaching() {
            // When
            MockComponent compA1 = service.getOrCreateComponent(MockType.A);
            MockComponent compA2 = service.getOrCreateComponent(MockType.A);

            // Then
            assertNotNull(compA1);
            assertSame(compA1, compA2, "Should return the same instance for the same type");
        }

        @Test
        @DisplayName("Should return different component instances for different types")
        void testDifferentComponentTypes() {
            // When
            MockComponent compA = service.getOrCreateComponent(MockType.A);
            MockComponent compB = service.getOrCreateComponent(MockType.B);

            // Then
            assertNotSame(compA, compB, "Should return different instances for different types");
        }
    }

    @ParameterizedTest
    @EnumSource(MockType.class)
    @DisplayName("Should create components with correct values for each type")
    void testComponentValues(MockType type) {
        // When
        MockComponent component = service.getOrCreateComponent(type);

        // Then
        assertNotNull(component);
        assertEquals(type.name(), component.getValue(),
                "Component value should match type name");
    }
}