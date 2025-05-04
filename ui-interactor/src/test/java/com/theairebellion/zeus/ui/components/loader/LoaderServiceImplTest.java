package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.loader.mock.MockLoaderComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("LoaderServiceImpl Unit Tests")
class LoaderServiceImplTest extends BaseUnitUITest {

    @Mock private SmartWebDriver driver;
    @Mock private SmartWebElement container;
    @Mock private By locator;
    @Mock private Loader loaderMock;

    private LoaderServiceImpl service;
    private MockedStatic<ComponentFactory> factoryMock;
    private final MockLoaderComponentType componentType = MockLoaderComponentType.DUMMY_LOADER;

    private static final int DEFAULT_WAIT_SECONDS = 5;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new LoaderServiceImpl(driver);
        locator = By.id("loader");

        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getLoaderComponent(any(LoaderComponentType.class), eq(driver)))
                .thenReturn(loaderMock);
    }

    @AfterEach
    void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Nested
    @DisplayName("Method Delegation Tests")
    class MethodDelegationTests {

        @Test
        @DisplayName("isVisible with container delegates correctly")
        void isVisibleWithContainerDelegates() {
            // Given
            when(loaderMock.isVisible(container)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, container);

            // Then
            assertThat(result).isTrue();
            verify(loaderMock).isVisible(container);
            verifyNoMoreInteractions(loaderMock);
        }

        @Test
        @DisplayName("isVisible with locator delegates correctly")
        void isVisibleWithLocatorDelegates() {
            // Given
            when(loaderMock.isVisible(locator)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(loaderMock).isVisible(locator);
            verifyNoMoreInteractions(loaderMock);
        }

        @Test
        @DisplayName("waitToBeShown with container delegates correctly")
        void waitToBeShownWithContainerDelegates() {
            // Given - setup in @BeforeEach

            // When
            service.waitToBeShown(componentType, container, DEFAULT_WAIT_SECONDS);

            // Then
            verify(loaderMock).waitToBeShown(container, DEFAULT_WAIT_SECONDS);
            verifyNoMoreInteractions(loaderMock);
        }

        @Test
        @DisplayName("waitToBeShown with seconds only delegates correctly")
        void waitToBeShownWithSecondsOnlyDelegates() {
            // Given - setup in @BeforeEach

            // When
            service.waitToBeShown(componentType, DEFAULT_WAIT_SECONDS);

            // Then
            verify(loaderMock).waitToBeShown(DEFAULT_WAIT_SECONDS);
            verifyNoMoreInteractions(loaderMock);
        }

        @Test
        @DisplayName("waitToBeShown with locator delegates correctly")
        void waitToBeShownWithLocatorDelegates() {
            // Given - setup in @BeforeEach

            // When
            service.waitToBeShown(componentType, locator, DEFAULT_WAIT_SECONDS);

            // Then
            verify(loaderMock).waitToBeShown(locator, DEFAULT_WAIT_SECONDS);
            verifyNoMoreInteractions(loaderMock);
        }

        @Test
        @DisplayName("waitToBeRemoved with container delegates correctly")
        void waitToBeRemovedWithContainerDelegates() {
            // Given - setup in @BeforeEach

            // When
            service.waitToBeRemoved(componentType, container, DEFAULT_WAIT_SECONDS);

            // Then
            verify(loaderMock).waitToBeRemoved(container, DEFAULT_WAIT_SECONDS);
            verifyNoMoreInteractions(loaderMock);
        }

        @Test
        @DisplayName("waitToBeRemoved with seconds only delegates correctly")
        void waitToBeRemovedWithSecondsOnlyDelegates() {
            // Given - setup in @BeforeEach

            // When
            service.waitToBeRemoved(componentType, DEFAULT_WAIT_SECONDS);

            // Then
            verify(loaderMock).waitToBeRemoved(DEFAULT_WAIT_SECONDS);
            verifyNoMoreInteractions(loaderMock);
        }

        @Test
        @DisplayName("waitToBeRemoved with locator delegates correctly")
        void waitToBeRemovedWithLocatorDelegates() {
            // Given - setup in @BeforeEach

            // When
            service.waitToBeRemoved(componentType, locator, DEFAULT_WAIT_SECONDS);

            // Then
            verify(loaderMock).waitToBeRemoved(locator, DEFAULT_WAIT_SECONDS);
            verifyNoMoreInteractions(loaderMock);
        }
    }

    @Nested
    @DisplayName("Component Caching Tests")
    class ComponentCachingTests {

        @Test
        @DisplayName("Component is cached and reused")
        void componentCaching() {
            // Given - setup in @BeforeEach

            // When
            service.isVisible(componentType, container);
            service.waitToBeShown(componentType, DEFAULT_WAIT_SECONDS);

            // Then
            factoryMock.verify(() -> ComponentFactory.getLoaderComponent(eq(componentType), eq(driver)), times(1));
        }
    }
}