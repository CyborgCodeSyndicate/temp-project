package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.loader.mock.MockLoaderComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("LoaderServiceImpl Unit Tests")
class LoaderServiceImplTest extends BaseUnitUITest {

   private SmartWebDriver driver;
   private LoaderServiceImpl service;
   private SmartWebElement container;
   private By locator;
   private Loader loaderMock;
   private MockLoaderComponentType componentType;
   private MockedStatic<ComponentFactory> factoryMock;

   @BeforeEach
   void setUp() {
      driver = mock(SmartWebDriver.class);
      service = new LoaderServiceImpl(driver);
      container = MockSmartWebElement.createMock();
      locator = By.id("loader");
      loaderMock = mock(Loader.class);
      componentType = MockLoaderComponentType.DUMMY;

      // Configure static mock for ComponentFactory
      factoryMock = mockStatic(ComponentFactory.class);
      factoryMock.when(() -> ComponentFactory.getLoaderComponent(eq(componentType), eq(driver)))
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
      }

      @Test
      @DisplayName("waitToBeShown with container delegates correctly")
      void waitToBeShownWithContainerDelegates() {
         // Given
         var seconds = 5;

         // When
         service.waitToBeShown(componentType, container, seconds);

         // Then
         verify(loaderMock).waitToBeShown(container, seconds);
      }

      @Test
      @DisplayName("waitToBeShown with seconds only delegates correctly")
      void waitToBeShownWithSecondsOnlyDelegates() {
         // Given
         var seconds = 5;

         // When
         service.waitToBeShown(componentType, seconds);

         // Then
         verify(loaderMock).waitToBeShown(seconds);
      }

      @Test
      @DisplayName("waitToBeShown with locator delegates correctly")
      void waitToBeShownWithLocatorDelegates() {
         // Given
         var seconds = 5;

         // When
         service.waitToBeShown(componentType, locator, seconds);

         // Then
         verify(loaderMock).waitToBeShown(locator, seconds);
      }

      @Test
      @DisplayName("waitToBeRemoved with container delegates correctly")
      void waitToBeRemovedWithContainerDelegates() {
         // Given
         var seconds = 10;

         // When
         service.waitToBeRemoved(componentType, container, seconds);

         // Then
         verify(loaderMock).waitToBeRemoved(container, seconds);
      }

      @Test
      @DisplayName("waitToBeRemoved with seconds only delegates correctly")
      void waitToBeRemovedWithSecondsOnlyDelegates() {
         // Given
         var seconds = 10;

         // When
         service.waitToBeRemoved(componentType, seconds);

         // Then
         verify(loaderMock).waitToBeRemoved(seconds);
      }

      @Test
      @DisplayName("waitToBeRemoved with locator delegates correctly")
      void waitToBeRemovedWithLocatorDelegates() {
         // Given
         var seconds = 10;

         // When
         service.waitToBeRemoved(componentType, locator, seconds);

         // Then
         verify(loaderMock).waitToBeRemoved(locator, seconds);
      }
   }

   @Nested
   @DisplayName("Default Method Overloads Tests")
   class DefaultMethodOverloadsTests {

      @BeforeEach
      void setUpDefaultTypeMock() {
         // Additional setup for testing default method overloads
         factoryMock.when(() -> ComponentFactory.getLoaderComponent(any(LoaderComponentType.class), eq(driver)))
               .thenReturn(loaderMock);
      }

      @Test
      @DisplayName("isVisible with container default overload delegates correctly")
      void isVisibleWithContainerDefaultOverload() {
         // Given
         when(loaderMock.isVisible(container)).thenReturn(true);

         // When
         var result = service.isVisible(container);

         // Then
         assertThat(result).isTrue();
         verify(loaderMock).isVisible(container);
      }

      @Test
      @DisplayName("isVisible with locator default overload delegates correctly")
      void isVisibleWithLocatorDefaultOverload() {
         // Given
         when(loaderMock.isVisible(locator)).thenReturn(true);

         // When
         var result = service.isVisible(locator);

         // Then
         assertThat(result).isTrue();
         verify(loaderMock).isVisible(locator);
      }

      @Test
      @DisplayName("waitToBeShown with container default overload delegates correctly")
      void waitToBeShownWithContainerDefaultOverload() {
         // Given
         var seconds = 5;

         // When
         service.waitToBeShown(container, seconds);

         // Then
         verify(loaderMock).waitToBeShown(container, seconds);
      }

      @Test
      @DisplayName("waitToBeShown with seconds only default overload delegates correctly")
      void waitToBeShownWithSecondsOnlyDefaultOverload() {
         // Given
         var seconds = 5;

         // When
         service.waitToBeShown(seconds);

         // Then
         verify(loaderMock).waitToBeShown(seconds);
      }

      @Test
      @DisplayName("waitToBeShown with locator default overload delegates correctly")
      void waitToBeShownWithLocatorDefaultOverload() {
         // Given
         var seconds = 5;

         // When
         service.waitToBeShown(locator, seconds);

         // Then
         verify(loaderMock).waitToBeShown(locator, seconds);
      }

      @Test
      @DisplayName("waitToBeRemoved with container default overload delegates correctly")
      void waitToBeRemovedWithContainerDefaultOverload() {
         // Given
         var seconds = 10;

         // When
         service.waitToBeRemoved(container, seconds);

         // Then
         verify(loaderMock).waitToBeRemoved(container, seconds);
      }

      @Test
      @DisplayName("waitToBeRemoved with seconds only default overload delegates correctly")
      void waitToBeRemovedWithSecondsOnlyDefaultOverload() {
         // Given
         var seconds = 10;

         // When
         service.waitToBeRemoved(seconds);

         // Then
         verify(loaderMock).waitToBeRemoved(seconds);
      }

      @Test
      @DisplayName("waitToBeRemoved with locator default overload delegates correctly")
      void waitToBeRemovedWithLocatorDefaultOverload() {
         // Given
         var seconds = 10;

         // When
         service.waitToBeRemoved(locator, seconds);

         // Then
         verify(loaderMock).waitToBeRemoved(locator, seconds);
      }
   }

   @Nested
   @DisplayName("Component Caching Tests")
   class ComponentCachingTests {

      @Test
      @DisplayName("Component is cached and reused")
      void componentCaching() {
         // When
         service.isVisible(componentType, container);
         service.waitToBeShown(componentType, 5);
         service.waitToBeRemoved(componentType, 10);

         // Then
         factoryMock.verify(() -> ComponentFactory.getLoaderComponent(eq(componentType), eq(driver)), times(1));
      }

      @Test
      @DisplayName("Different component types create different instances")
      void differentComponentTypes() {
         // Setup mock component types
         componentType = MockLoaderComponentType.DUMMY;
         var componentType2 = MockLoaderComponentType.TEST;

         // Create mock components
         var loader1 = mock(Loader.class);
         var loader2 = mock(Loader.class);

         // Configure behavior
         when(loader1.isVisible(container)).thenReturn(false);
         when(loader2.isVisible(container)).thenReturn(true);

         // Configure factory mock
         factoryMock.reset();
         factoryMock.when(() -> ComponentFactory.getLoaderComponent(eq(componentType), eq(driver)))
               .thenReturn(loader1);
         factoryMock.when(() -> ComponentFactory.getLoaderComponent(eq(componentType2), eq(driver)))
               .thenReturn(loader2);

         // First component type operation
         var result1 = service.isVisible(componentType, container);
         assertThat(result1).isFalse();
         verify(loader1).isVisible(container);

         // Second component type operation
         var result2 = service.isVisible(componentType2, container);
         assertThat(result2).isTrue();
         verify(loader2).isVisible(container);

         // Verify factory calls
         factoryMock.verify(() -> ComponentFactory.getLoaderComponent(eq(componentType), eq(driver)), times(1));
         factoryMock.verify(() -> ComponentFactory.getLoaderComponent(eq(componentType2), eq(driver)), times(1));
      }
   }
}