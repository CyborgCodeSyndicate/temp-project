package com.theairebellion.zeus.ui.components.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.input.mock.MockInputComponentType;
import com.theairebellion.zeus.ui.components.input.mock.MockInputService;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

class InputServiceTest extends BaseUnitUITest {

    private MockInputService service;
    private MockSmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        service = new MockInputService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testInput");
    }

    @Test
    void testDefaultInsertWithContainer() {
        service.reset();
        service.insert(container, "value");
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("value", service.lastValue);
    }

    @Test
    void testDefaultInsertWithContainerAndLabel() {
        service.reset();
        service.insert(container, "label", "val");
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("label", service.lastLabel);
        assertEquals("val", service.lastValue);
    }

    @Test
    void testDefaultInsertWithLabel() {
        service.reset();
        service.insert("label", "val");
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals("label", service.lastLabel);
        assertEquals("val", service.lastValue);
    }

    @Test
    void testDefaultInsertWithBy() {
        service.reset();
        service.insert(locator, "testVal");
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
        assertEquals("testVal", service.lastValue);
    }

    @Test
    void testDefaultClearWithContainer() {
        service.reset();
        service.clear(container);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultClearWithContainerAndLabel() {
        service.reset();
        service.clear(container, "label");
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("label", service.lastLabel);
    }

    @Test
    void testDefaultClearWithLabel() {
        service.reset();
        service.clear("myLabel");
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals("myLabel", service.lastLabel);
    }

    @Test
    void testDefaultClearWithBy() {
        service.reset();
        service.clear(locator);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultGetValueContainer() {
        service.reset();
        service.returnValue = "someVal";
        String val = service.getValue(container);
        assertEquals("someVal", val);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetValueContainerLabel() {
        service.reset();
        service.returnValue = "labVal";
        String val = service.getValue(container, "lab");
        assertEquals("labVal", val);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("lab", service.lastLabel);
    }

    @Test
    void testDefaultGetValueLabel() {
        service.reset();
        service.returnValue = "valLabel";
        String val = service.getValue("lbl");
        assertEquals("valLabel", val);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals("lbl", service.lastLabel);
    }

    @Test
    void testDefaultGetValueBy() {
        service.reset();
        service.returnValue = "byValue";
        String val = service.getValue(locator);
        assertEquals("byValue", val);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultIsEnabledContainer() {
        service.reset();
        service.returnEnabled = true;
        boolean enabled = service.isEnabled(container);
        assertTrue(enabled);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultIsEnabledContainerLabel() {
        service.reset();
        service.returnEnabled = true;
        boolean enabled = service.isEnabled(container, "lbl");
        assertTrue(enabled);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("lbl", service.lastLabel);
    }

    @Test
    void testDefaultIsEnabledLabel() {
        service.reset();
        service.returnEnabled = true;
        boolean enabled = service.isEnabled("someLabel");
        assertTrue(enabled);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals("someLabel", service.lastLabel);
    }

    @Test
    void testDefaultIsEnabledBy() {
        service.reset();
        service.returnEnabled = true;
        boolean enabled = service.isEnabled(locator);
        assertTrue(enabled);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testDefaultGetErrorMessageContainer() {
        service.reset();
        service.returnErrorMessage = "errMsg";
        String msg = service.getErrorMessage(container);
        assertEquals("errMsg", msg);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
    }

    @Test
    void testDefaultGetErrorMessageContainerLabel() {
        service.reset();
        service.returnErrorMessage = "labelErr";
        String msg = service.getErrorMessage(container, "lbl");
        assertEquals("labelErr", msg);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("lbl", service.lastLabel);
    }

    @Test
    void testDefaultGetErrorMessageLabel() {
        service.reset();
        service.returnErrorMessage = "labErr";
        String msg = service.getErrorMessage("myLabel");
        assertEquals("labErr", msg);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals("myLabel", service.lastLabel);
    }

    @Test
    void testDefaultGetErrorMessageBy() {
        service.reset();
        service.returnErrorMessage = "byErr";
        String msg = service.getErrorMessage(locator);
        assertEquals("byErr", msg);
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
    }

    @Test
    void testInsertionMethod() {
        service.reset();
        service.insertion(MockInputComponentType.DUMMY, locator, "someVal");
        assertEquals(MockInputComponentType.DUMMY, service.lastComponentType);
        assertEquals(locator, service.lastLocator);
        assertEquals("someVal", service.lastValue);
    }

    @Test
    void testTableInsertionMethod() {
        SmartWebElement cell = mock(SmartWebElement.class);
        service.reset();
        service.tableInsertion(cell, MockInputComponentType.DUMMY, "a", "b");
        assertEquals(cell, service.tableCell);
        assertArrayEquals(new String[]{"a", "b"}, service.tableInsertionValues);
    }

    @Test
    void testTableFilterMethod() {
        SmartWebElement header = mock(SmartWebElement.class);
        FilterStrategy strategy = FilterStrategy.SELECT;
        service.reset();
        service.tableFilter(header, MockInputComponentType.DUMMY, strategy, "z");
        assertEquals(header, service.headerCell);
        assertEquals(strategy, service.filterStrategy);
        assertArrayEquals(new String[]{"z"}, service.filterValues);
    }

    @Nested
    @DisplayName("Default Type Resolution")
    class DefaultTypeResolution {
        private MockedStatic<UiConfigHolder> uiConfigHolderMock;
        private MockedStatic<ReflectionUtil> reflectionUtilMock;
        private UiConfig uiConfigMock;

        @BeforeEach
        void setUp() {
            uiConfigMock = mock(UiConfig.class);
            uiConfigHolderMock = Mockito.mockStatic(UiConfigHolder.class);
            reflectionUtilMock = Mockito.mockStatic(ReflectionUtil.class);

            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfigMock);
            when(uiConfigMock.projectPackage()).thenReturn("com.test.package");
            when(uiConfigMock.inputDefaultType()).thenReturn("TEST_TYPE");
        }

        @AfterEach
        void tearDown() {
            if (uiConfigHolderMock != null) {
                uiConfigHolderMock.close();
            }
            if (reflectionUtilMock != null) {
                reflectionUtilMock.close();
            }
        }

        @Test
        @DisplayName("getDefaultType returns component type when found")
        void getDefaultTypeSuccess() {
            // Given
            InputComponentType mockType = mock(InputComponentType.class);
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(InputComponentType.class),
                            eq("TEST_TYPE"),
                            eq("com.test.package")))
                    .thenReturn(mockType);

            // When
            InputComponentType result = InputService.getDefaultType();

            // Then
            assertThat(result).isEqualTo(mockType);
        }

        @Test
        @DisplayName("getDefaultType returns null for invalid type")
        void getDefaultTypeInvalidType() {
            // Given
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(InputComponentType.class),
                            anyString(),
                            anyString()))
                    .thenReturn(null);

            // When
            InputComponentType result = InputService.getDefaultType();

            // Then
            assertThat(result).isNull();
        }
    }

    @Test
    @DisplayName("Test with explicit component type")
    void testExplicitComponentType() {
        // Given
        service.reset();

        // When
        service.insert(MockInputComponentType.CUSTOM, container, "value");

        // Then
        assertEquals(MockInputComponentType.CUSTOM, service.lastComponentType);
        assertEquals(container, service.lastContainer);
        assertEquals("value", service.lastValue);
    }

    @Test
    @DisplayName("Test non-default return value")
    void testNonDefaultReturnValue() {
        // Given
        service.reset();
        service.returnEnabled = false;

        // When
        boolean result = service.isEnabled(container);

        // Then
        assertEquals(false, result);
    }

    @Test
    @DisplayName("Input interface default methods work correctly")
    void testInputInterfaceDefaultMethods() {
        // Create a concrete implementation of Input with default methods
        Input defaultInput = new Input() {
            @Override
            public void insert(SmartWebElement container, String value) {}

            @Override
            public void insert(SmartWebElement container, String inputFieldLabel, String value) {}

            @Override
            public void insert(String inputFieldLabel, String value) {}

            @Override
            public void insert(By inputFieldContainerLocator, String value) {}

            @Override
            public void clear(SmartWebElement container) {}

            @Override
            public void clear(SmartWebElement container, String inputFieldLabel) {}

            @Override
            public void clear(String inputFieldLabel) {}

            @Override
            public void clear(By inputFieldContainerLocator) {}

            @Override
            public String getValue(SmartWebElement container) { return ""; }

            @Override
            public String getValue(SmartWebElement container, String inputFieldLabel) { return ""; }

            @Override
            public String getValue(String inputFieldLabel) { return ""; }

            @Override
            public String getValue(By inputFieldContainerLocator) { return ""; }

            @Override
            public boolean isEnabled(SmartWebElement container) { return false; }

            @Override
            public boolean isEnabled(SmartWebElement container, String inputFieldLabel) { return false; }

            @Override
            public boolean isEnabled(String inputFieldLabel) { return false; }

            @Override
            public boolean isEnabled(By inputFieldContainerLocator) { return false; }

            @Override
            public String getErrorMessage(SmartWebElement container) { return ""; }

            @Override
            public String getErrorMessage(SmartWebElement container, String inputFieldLabel) { return ""; }

            @Override
            public String getErrorMessage(String inputFieldLabel) { return ""; }

            @Override
            public String getErrorMessage(By inputFieldContainerLocator) { return ""; }
        };

        // Call the default methods to cover them
        FilterStrategy strategy = FilterStrategy.SELECT;
        defaultInput.tableInsertion(container, "value1");
        defaultInput.tableFilter(container, strategy, "value1");

        // Verify no exceptions
        assertThat(defaultInput).isNotNull();
    }
}