package com.theairebellion.zeus.ui.components.button;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.mock.MockButtonComponentType;
import com.theairebellion.zeus.ui.components.button.mock.MockButtonService;
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

@DisplayName("ButtonService Test")
class ButtonServiceTest extends BaseUnitUITest {

    private MockButtonService service;
    private MockSmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        service = new MockButtonService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testButton");
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - Click")
    class DefaultMethodDelegationClickTests {

        @Test
        @DisplayName("click with container and text delegates to implementation")
        void testDefaultClickWithContainerAndText() {
            // Given
            service.reset();

            // When
            service.click(container, "ClickMe");

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isEqualTo("ClickMe");
        }

        @Test
        @DisplayName("click with container delegates to implementation")
        void testDefaultClickWithContainer() {
            // Given
            service.reset();

            // When
            service.click(container);

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isNull();
        }

        @Test
        @DisplayName("click with text delegates to implementation")
        void testDefaultClickWithString() {
            // Given
            service.reset();

            // When
            service.click("ClickString");

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
            assertThat(service.lastButtonText).isEqualTo("ClickString");
            assertThat(service.lastContainer).isNull();
        }

        @Test
        @DisplayName("click with locator delegates to implementation")
        void testDefaultClickWithLocator() {
            // Given
            service.reset();

            // When
            service.click(locator);

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
            assertThat(service.lastLocator).isEqualTo(locator);
            assertThat(service.lastContainer).isNull();
            assertThat(service.lastButtonText).isNull();
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - IsEnabled")
    class DefaultMethodDelegationIsEnabledTests {

        @Test
        @DisplayName("isEnabled with container and text delegates to implementation")
        void testDefaultIsEnabledWithContainerAndText() {
            // Given
            service.reset();
            service.returnEnabled = true;

            // When
            boolean enabled = service.isEnabled(container, "ButtonText");

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isEqualTo("ButtonText");
        }

        @Test
        @DisplayName("isEnabled with container delegates to implementation")
        void testDefaultIsEnabledWithContainer() {
            // Given
            service.reset();
            service.returnEnabled = true;

            // When
            boolean enabled = service.isEnabled(container);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isNull();
        }

        @Test
        @DisplayName("isEnabled with text delegates to implementation")
        void testDefaultIsEnabledWithString() {
            // Given
            service.reset();
            service.returnEnabled = true;

            // When
            boolean enabled = service.isEnabled("JustText");

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
            assertThat(service.lastButtonText).isEqualTo("JustText");
            assertThat(service.lastContainer).isNull();
        }

        @Test
        @DisplayName("isEnabled with locator delegates to implementation")
        void testDefaultIsEnabledWithLocator() {
            // Given
            service.reset();
            service.returnEnabled = true;

            // When
            boolean enabled = service.isEnabled(locator);

            // Then
            assertThat(enabled).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
            assertThat(service.lastLocator).isEqualTo(locator);
            assertThat(service.lastContainer).isNull();
            assertThat(service.lastButtonText).isNull();
        }
    }

    @Nested
    @DisplayName("Default Method Delegation Tests - IsVisible")
    class DefaultMethodDelegationIsVisibleTests {

        @Test
        @DisplayName("isVisible with container and text delegates to implementation")
        void testDefaultIsVisibleWithContainerAndText() {
            // Given
            service.reset();
            service.returnVisible = true;

            // When
            boolean visible = service.isVisible(container, "ButtonText");

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isEqualTo("ButtonText");
        }

        @Test
        @DisplayName("isVisible with container delegates to implementation")
        void testDefaultIsVisibleWithContainer() {
            // Given
            service.reset();
            service.returnVisible = true;

            // When
            boolean visible = service.isVisible(container);

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isNull();
        }

        @Test
        @DisplayName("isVisible with text delegates to implementation")
        void testDefaultIsVisibleWithString() {
            // Given
            service.reset();
            service.returnVisible = true;

            // When
            boolean visible = service.isVisible("JustText");

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
            assertThat(service.lastButtonText).isEqualTo("JustText");
            assertThat(service.lastContainer).isNull();
        }

        @Test
        @DisplayName("isVisible with locator delegates to implementation")
        void testDefaultIsVisibleWithLocator() {
            // Given
            service.reset();
            service.returnVisible = true;

            // When
            boolean visible = service.isVisible(locator);

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.DUMMY);
            assertThat(service.lastLocator).isEqualTo(locator);
            assertThat(service.lastContainer).isNull();
            assertThat(service.lastButtonText).isNull();
        }
    }

    @Nested
    @DisplayName("Explicit Component Type Tests")
    class ExplicitComponentTypeTests {

        @Test
        @DisplayName("Different component type is correctly passed to implementation")
        void testDifferentComponentType() {
            // Given
            service.reset();

            // When
            service.click(MockButtonComponentType.CUSTOM, container, "ClickMe");

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockButtonComponentType.CUSTOM);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastButtonText).isEqualTo("ClickMe");
        }

        @Test
        @DisplayName("Return value is correctly passed from implementation")
        void testReturnValueFromImplementation() {
            // Given
            service.reset();
            service.returnEnabled = false;

            // When
            boolean result = service.isEnabled(container);

            // Then
            assertThat(result).isFalse();
        }
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
            when(uiConfigMock.buttonDefaultType()).thenReturn("TEST_TYPE");
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
        void getDefaultTypeSuccess() throws Exception {
            // Given
            ButtonComponentType mockType = mock(ButtonComponentType.class);
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(ButtonComponentType.class),
                            eq("TEST_TYPE"),
                            eq("com.test.package")))
                    .thenReturn(mockType);

            // When - access the private method using reflection
            java.lang.reflect.Method getDefaultTypeMethod = ButtonService.class.getDeclaredMethod("getDefaultType");
            getDefaultTypeMethod.setAccessible(true);
            ButtonComponentType result = (ButtonComponentType) getDefaultTypeMethod.invoke(null);

            // Then
            assertThat(result).isEqualTo(mockType);
        }

        @Test
        @DisplayName("getDefaultType returns null when exception occurs")
        void getDefaultTypeWithException() throws Exception {
            // Given
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(ButtonComponentType.class),
                            anyString(),
                            anyString()))
                    .thenThrow(new RuntimeException("Test exception"));

            // When - access the private method using reflection
            java.lang.reflect.Method getDefaultTypeMethod = ButtonService.class.getDeclaredMethod("getDefaultType");
            getDefaultTypeMethod.setAccessible(true);
            ButtonComponentType result = (ButtonComponentType) getDefaultTypeMethod.invoke(null);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("getDefaultType handles reflection errors correctly")
        void getDefaultTypeWithReflectionError() throws Exception {
            // Given
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(ButtonComponentType.class),
                            anyString(),
                            anyString()))
                    .thenAnswer(invocation -> {
                        // Simulating error without actually throwing NoClassDefFoundError
                        throw new RuntimeException("Simulated reflection error");
                    });

            // When - access the private method using reflection
            java.lang.reflect.Method getDefaultTypeMethod = ButtonService.class.getDeclaredMethod("getDefaultType");
            getDefaultTypeMethod.setAccessible(true);
            ButtonComponentType result = (ButtonComponentType) getDefaultTypeMethod.invoke(null);

            // Then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("TableInsertion Integration Tests")
    class TableInsertionTests {

        @Test
        @DisplayName("Table insertion delegates to implementation")
        void testTableInsertion() {
            // Given
            service.reset();
            SmartWebElement cellElement = mock(SmartWebElement.class);
            ComponentType componentType = MockButtonComponentType.DUMMY;

            // When
            service.tableInsertion(cellElement, componentType, "value1", "value2");

            // Then
            assertThat(service.lastCellElement).isEqualTo(cellElement);
            assertThat(service.lastComponentType).isEqualTo(componentType);
        }
    }
}