package com.theairebellion.zeus.ui.components.alert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.alert.mock.MockAlertComponentType;
import com.theairebellion.zeus.ui.components.alert.mock.MockAlertService;
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

@DisplayName("AlertService Test")
class AlertServiceTest extends BaseUnitUITest {

    private MockAlertService service;
    private SmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        service = new MockAlertService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("alertLocator");
    }

    @Nested
    @DisplayName("Default Method Delegation Tests")
    class DefaultMethodDelegationTests {

        @Test
        @DisplayName("getValue with container delegates to implementation")
        void testGetValueWithContainerDefault() {
            // Given
            service.reset();

            // When
            String result = service.getValue(container);

            // Then
            assertThat(result).isEqualTo(MockAlertService.VALUE_CONTAINER);
            assertThat(service.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("getValue with locator delegates to implementation")
        void testGetValueWithLocatorDefault() {
            // Given
            service.reset();

            // When
            String result = service.getValue(locator);

            // Then
            assertThat(result).isEqualTo(MockAlertService.VALUE_LOCATOR);
            assertThat(service.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
            assertThat(service.lastLocator).isEqualTo(locator);
        }

        @Test
        @DisplayName("isVisible with container delegates to implementation")
        void testIsVisibleWithContainerDefault() {
            // Given
            service.reset();

            // When
            boolean visible = service.isVisible(container);

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("isVisible with locator delegates to implementation")
        void testIsVisibleWithLocatorDefault() {
            // Given
            service.reset();

            // When
            boolean visible = service.isVisible(locator);

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
            assertThat(service.lastLocator).isEqualTo(locator);
        }
    }

    @Nested
    @DisplayName("Explicit Component Type Tests")
    class ExplicitComponentTypeTests {

        @Test
        @DisplayName("getValue with container and explicit type delegates correctly")
        void testGetValueWithContainerExplicitType() {
            // Given
            service.reset();

            // When
            String result = service.getValue(MockAlertComponentType.DUMMY, container);

            // Then
            assertThat(result).isEqualTo(MockAlertService.VALUE_CONTAINER);
            assertThat(service.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("getValue with locator and explicit type delegates correctly")
        void testGetValueWithLocatorExplicitType() {
            // Given
            service.reset();

            // When
            String result = service.getValue(MockAlertComponentType.DUMMY, locator);

            // Then
            assertThat(result).isEqualTo(MockAlertService.VALUE_LOCATOR);
            assertThat(service.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
            assertThat(service.lastLocator).isEqualTo(locator);
        }

        @Test
        @DisplayName("isVisible with container and explicit type delegates correctly")
        void testIsVisibleWithContainerExplicitType() {
            // Given
            service.reset();

            // When
            boolean visible = service.isVisible(MockAlertComponentType.DUMMY, container);

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("isVisible with locator and explicit type delegates correctly")
        void testIsVisibleWithLocatorExplicitType() {
            // Given
            service.reset();

            // When
            boolean visible = service.isVisible(MockAlertComponentType.DUMMY, locator);

            // Then
            assertThat(visible).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockAlertComponentType.DUMMY);
            assertThat(service.lastLocator).isEqualTo(locator);
        }

        @Test
        @DisplayName("Different component type is correctly passed to implementation")
        void testDifferentComponentType() {
            // Given
            service.reset();

            // When
            service.getValue(MockAlertComponentType.ANOTHER, container);

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockAlertComponentType.ANOTHER);
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
            when(uiConfigMock.alertDefaultType()).thenReturn("TEST_TYPE");
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
            AlertComponentType mockType = mock(AlertComponentType.class);
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(AlertComponentType.class),
                            eq("TEST_TYPE"),
                            eq("com.test.package")))
                    .thenReturn(mockType);

            // When - access the private method using reflection
            java.lang.reflect.Method getDefaultTypeMethod = AlertService.class.getDeclaredMethod("getDefaultType");
            getDefaultTypeMethod.setAccessible(true);
            AlertComponentType result = (AlertComponentType) getDefaultTypeMethod.invoke(null);

            // Then
            assertThat(result).isEqualTo(mockType);
        }

        @Test
        @DisplayName("getDefaultType returns null when exception occurs")
        void getDefaultTypeWithException() throws Exception {
            // Given
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(AlertComponentType.class),
                            anyString(),
                            anyString()))
                    .thenThrow(new RuntimeException("Test exception"));

            // When - access the private method using reflection
            java.lang.reflect.Method getDefaultTypeMethod = AlertService.class.getDeclaredMethod("getDefaultType");
            getDefaultTypeMethod.setAccessible(true);
            AlertComponentType result = (AlertComponentType) getDefaultTypeMethod.invoke(null);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("getDefaultType handles reflection errors correctly")
        void getDefaultTypeWithReflectionError() throws Exception {
            // Given
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(AlertComponentType.class),
                            anyString(),
                            anyString()))
                    .thenAnswer(invocation -> {
                        // Simulating error without actually throwing NoClassDefFoundError
                        // which can't be mocked properly
                        throw new RuntimeException("Simulated reflection error");
                    });

            // When - access the private method using reflection
            java.lang.reflect.Method getDefaultTypeMethod = AlertService.class.getDeclaredMethod("getDefaultType");
            getDefaultTypeMethod.setAccessible(true);
            AlertComponentType result = (AlertComponentType) getDefaultTypeMethod.invoke(null);

            // Then
            assertThat(result).isNull();
        }
    }
}