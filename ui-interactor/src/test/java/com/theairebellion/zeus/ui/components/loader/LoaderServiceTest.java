package com.theairebellion.zeus.ui.components.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.loader.mock.MockLoaderComponentType;
import com.theairebellion.zeus.ui.components.loader.mock.MockLoaderService;
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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@DisplayName("LoaderService Interface Default Methods")
class LoaderServiceTest extends BaseUnitUITest {

    private MockLoaderService service;
    private SmartWebElement container;
    private By locator;

    @BeforeEach
    void setUp() {
        service = new MockLoaderService();
        WebElement webElement = mock(WebElement.class);
        WebDriver driver = mock(WebDriver.class);
        container = new MockSmartWebElement(webElement, driver);
        locator = By.id("testLoader");
    }

    @Nested
    @DisplayName("Default Methods Tests")
    class DefaultMethodsTests {

        @Test
        @DisplayName("isVisible with container delegates correctly")
        void isVisibleWithContainerDelegates() {
            // Given
            service.reset();
            service.returnVisible = true;

            // When
            var result = service.isVisible(container);

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockLoaderComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
        }

        @Test
        @DisplayName("isVisible with locator delegates correctly")
        void isVisibleWithLocatorDelegates() {
            // Given
            service.reset();
            service.returnVisible = true;

            // When
            var result = service.isVisible(locator);

            // Then
            assertThat(result).isTrue();
            assertThat(service.lastComponentType).isEqualTo(MockLoaderComponentType.DUMMY);
            assertThat(service.lastLocator).isEqualTo(locator);
        }

        @Test
        @DisplayName("waitToBeShown with container delegates correctly")
        void waitToBeShownWithContainerDelegates() {
            // Given
            service.reset();
            var seconds = 5;

            // When
            service.waitToBeShown(container, seconds);

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockLoaderComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastSeconds).isEqualTo(seconds);
        }

        @Test
        @DisplayName("waitToBeShown with seconds only delegates correctly")
        void waitToBeShownWithSecondsOnlyDelegates() {
            // Given
            service.reset();
            var seconds = 7;

            // When
            service.waitToBeShown(seconds);

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockLoaderComponentType.DUMMY);
            assertThat(service.lastSeconds).isEqualTo(seconds);
        }

        @Test
        @DisplayName("waitToBeShown with locator delegates correctly")
        void waitToBeShownWithLocatorDelegates() {
            // Given
            service.reset();
            var seconds = 10;

            // When
            service.waitToBeShown(locator, seconds);

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockLoaderComponentType.DUMMY);
            assertThat(service.lastLocator).isEqualTo(locator);
            assertThat(service.lastSeconds).isEqualTo(seconds);
        }

        @Test
        @DisplayName("waitToBeRemoved with container delegates correctly")
        void waitToBeRemovedWithContainerDelegates() {
            // Given
            service.reset();
            var seconds = 3;

            // When
            service.waitToBeRemoved(container, seconds);

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockLoaderComponentType.DUMMY);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastSeconds).isEqualTo(seconds);
        }

        @Test
        @DisplayName("waitToBeRemoved with seconds only delegates correctly")
        void waitToBeRemovedWithSecondsOnlyDelegates() {
            // Given
            service.reset();
            var seconds = 9;

            // When
            service.waitToBeRemoved(seconds);

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockLoaderComponentType.DUMMY);
            assertThat(service.lastSeconds).isEqualTo(seconds);
        }

        @Test
        @DisplayName("waitToBeRemoved with locator delegates correctly")
        void waitToBeRemovedWithLocatorDelegates() {
            // Given
            service.reset();
            var seconds = 11;

            // When
            service.waitToBeRemoved(locator, seconds);

            // Then
            assertThat(service.lastComponentType).isEqualTo(MockLoaderComponentType.DUMMY);
            assertThat(service.lastLocator).isEqualTo(locator);
            assertThat(service.lastSeconds).isEqualTo(seconds);
        }
    }

    @Nested
    @DisplayName("WaitToBeShownAndRemoved Methods Tests")
    class WaitToBeShownAndRemovedTests {

        @Test
        @DisplayName("waitToBeShownAndRemoved with container delegates correctly")
        void waitToBeShownAndRemovedWithContainerDelegates() {
            // Given
            service.reset();
            var showSeconds = 2;
            var removeSeconds = 4;
            var componentType = MockLoaderComponentType.DUMMY;

            // When
            service.waitToBeShownAndRemoved(componentType, container, showSeconds, removeSeconds);

            // Then
            assertThat(service.lastComponentType).isEqualTo(componentType);
            assertThat(service.lastContainer).isEqualTo(container);
            assertThat(service.lastSeconds).isEqualTo(removeSeconds);
        }

        @Test
        @DisplayName("waitToBeShownAndRemoved with seconds only delegates correctly")
        void waitToBeShownAndRemovedWithSecondsOnlyDelegates() {
            // Given
            service.reset();
            var showSeconds = 3;
            var removeSeconds = 5;
            var componentType = MockLoaderComponentType.DUMMY;

            // When
            service.waitToBeShownAndRemoved(componentType, showSeconds, removeSeconds);

            // Then
            assertThat(service.lastComponentType).isEqualTo(componentType);
            assertThat(service.lastSeconds).isEqualTo(removeSeconds);
        }

        @Test
        @DisplayName("waitToBeShownAndRemoved with locator delegates correctly")
        void waitToBeShownAndRemovedWithLocatorDelegates() {
            // Given
            service.reset();
            var showSeconds = 1;
            var removeSeconds = 6;
            var componentType = MockLoaderComponentType.DUMMY;

            // When
            service.waitToBeShownAndRemoved(componentType, locator, showSeconds, removeSeconds);

            // Then
            assertThat(service.lastComponentType).isEqualTo(componentType);
            assertThat(service.lastLocator).isEqualTo(locator);
            assertThat(service.lastSeconds).isEqualTo(removeSeconds);
        }
    }

    @Nested
    @DisplayName("Default Type Resolution Tests")
    class DefaultTypeResolutionTests {
        private MockedStatic<UiConfigHolder> uiConfigHolderMock;
        private MockedStatic<ReflectionUtil> reflectionUtilMock;
        private UiConfig uiConfigMock;

        @BeforeEach
        void setUp() {
            uiConfigMock = mock(UiConfig.class);
            uiConfigHolderMock = mockStatic(UiConfigHolder.class);
            reflectionUtilMock = mockStatic(ReflectionUtil.class);

            uiConfigHolderMock.when(UiConfigHolder::getUiConfig)
                    .thenReturn(uiConfigMock);
            when(uiConfigMock.loaderDefaultType()).thenReturn("TEST_TYPE");
            when(uiConfigMock.projectPackage()).thenReturn("com.test.package");
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
            var mockType = MockLoaderComponentType.DUMMY;
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(LoaderComponentType.class),
                            eq("TEST_TYPE"),
                            eq("com.test.package")))
                    .thenReturn(mockType);

            // When - access the private method using reflection
            var getDefaultTypeMethod = LoaderService.class.getDeclaredMethod("getDefaultType");
            getDefaultTypeMethod.setAccessible(true);
            var result = (LoaderComponentType) getDefaultTypeMethod.invoke(null);

            // Then
            assertThat(result).isEqualTo(mockType);
        }

        @Test
        @DisplayName("getDefaultType returns null when exception occurs")
        void getDefaultTypeWithException() throws Exception {
            // Given - ReflectionUtil throws exception when called
            reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                            eq(LoaderComponentType.class),
                            anyString(),
                            anyString()))
                    .thenThrow(new RuntimeException("Test exception"));

            // When - access private method via reflection
            var getDefaultTypeMethod = LoaderService.class.getDeclaredMethod("getDefaultType");
            getDefaultTypeMethod.setAccessible(true);
            var result = (LoaderComponentType) getDefaultTypeMethod.invoke(null);

            // Then - verify null is returned when exception occurs
            assertThat(result).isNull();
        }
    }
}