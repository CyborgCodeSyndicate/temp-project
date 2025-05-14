package com.theairebellion.zeus.ui.components.loader;

import com.theairebellion.zeus.ui.components.loader.mock.MockLoaderComponentType;
import com.theairebellion.zeus.ui.components.loader.mock.MockLoaderService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.testutil.MockSmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("LoaderService Interface Default Methods")
class LoaderServiceTest extends BaseUnitUITest {

   private static final MockLoaderComponentType DEFAULT_TYPE = MockLoaderComponentType.DUMMY_LOADER;
   private static final int WAIT_SECONDS_5 = 5;
   private static final int WAIT_SECONDS_10 = 10;
   private MockLoaderService service;
   private SmartWebElement container;
   private By locator;

   @BeforeEach
   void setUp() {
      // Given
      service = new MockLoaderService();
      container = MockSmartWebElement.createMock();
      locator = By.id("testLoader");
      service.reset();
   }

   @Nested
   @DisplayName("Default Methods Tests")
   class DefaultMethodsTests {

      @Test
      @DisplayName("isVisible with container delegates correctly")
      void isVisibleWithContainerDelegates() {
         // Given
         service.returnVisible = true;

         // When
         var result = service.isVisible(container);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("isVisible with locator delegates correctly")
      void isVisibleWithLocatorDelegates() {
         // Given
         service.returnVisible = true;

         // When
         var result = service.isVisible(locator);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastContainer).isNull();
      }

      @Test
      @DisplayName("waitToBeShown with container delegates correctly")
      void waitToBeShownWithContainerDelegates() {
         // Given - setup in @BeforeEach

         // When
         service.waitToBeShown(container, WAIT_SECONDS_5);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastSeconds).isEqualTo(WAIT_SECONDS_5);
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("waitToBeShown with seconds only delegates correctly")
      void waitToBeShownWithSecondsOnlyDelegates() {
         // Given - setup in @BeforeEach

         // When
         service.waitToBeShown(WAIT_SECONDS_5);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastSeconds).isEqualTo(WAIT_SECONDS_5);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("waitToBeShown with locator delegates correctly")
      void waitToBeShownWithLocatorDelegates() {
         // Given - setup in @BeforeEach

         // When
         service.waitToBeShown(locator, WAIT_SECONDS_5);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastSeconds).isEqualTo(WAIT_SECONDS_5);
         assertThat(service.lastContainer).isNull();
      }

      @Test
      @DisplayName("waitToBeRemoved with container delegates correctly")
      void waitToBeRemovedWithContainerDelegates() {
         // Given - setup in @BeforeEach

         // When
         service.waitToBeRemoved(container, WAIT_SECONDS_10);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastSeconds).isEqualTo(WAIT_SECONDS_10);
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("waitToBeRemoved with seconds only delegates correctly")
      void waitToBeRemovedWithSecondsOnlyDelegates() {
         // Given - setup in @BeforeEach

         // When
         service.waitToBeRemoved(WAIT_SECONDS_10);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastSeconds).isEqualTo(WAIT_SECONDS_10);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("waitToBeRemoved with locator delegates correctly")
      void waitToBeRemovedWithLocatorDelegates() {
         // Given - setup in @BeforeEach

         // When
         service.waitToBeRemoved(locator, WAIT_SECONDS_10);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastSeconds).isEqualTo(WAIT_SECONDS_10);
         assertThat(service.lastContainer).isNull();
      }
   }

   @Nested
   @DisplayName("WaitToBeShownAndRemoved Methods Tests")
   class WaitToBeShownAndRemovedTests {

      @Test
      @DisplayName("waitToBeShownAndRemoved with container delegates correctly")
      void waitToBeShownAndRemovedWithContainerDelegates() {
         // Given - service spy setup could be used here, but manual mock checks last call
         var componentType = MockLoaderComponentType.DUMMY_LOADER; // Use explicit type for clarity

         // When
         service.waitToBeShownAndRemoved(componentType, container, WAIT_SECONDS_5, WAIT_SECONDS_10);

         // Then
         // Check the state after the *last* call (waitToBeRemoved)
         assertThat(service.explicitComponentType).isEqualTo(componentType);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastSeconds).isEqualTo(WAIT_SECONDS_10); // last call was waitToBeRemoved
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("waitToBeShownAndRemoved with seconds only delegates correctly")
      void waitToBeShownAndRemovedWithSecondsOnlyDelegates() {
         // Given
         var componentType = MockLoaderComponentType.DUMMY_LOADER;

         // When
         service.waitToBeShownAndRemoved(componentType, WAIT_SECONDS_5, WAIT_SECONDS_10);

         // Then
         assertThat(service.explicitComponentType).isEqualTo(componentType);
         assertThat(service.lastSeconds).isEqualTo(WAIT_SECONDS_10);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("waitToBeShownAndRemoved with locator delegates correctly")
      void waitToBeShownAndRemovedWithLocatorDelegates() {
         // Given
         var componentType = MockLoaderComponentType.DUMMY_LOADER;

         // When
         service.waitToBeShownAndRemoved(componentType, locator, WAIT_SECONDS_5, WAIT_SECONDS_10);

         // Then
         assertThat(service.explicitComponentType).isEqualTo(componentType);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastSeconds).isEqualTo(WAIT_SECONDS_10);
         assertThat(service.lastContainer).isNull();
      }
   }
}