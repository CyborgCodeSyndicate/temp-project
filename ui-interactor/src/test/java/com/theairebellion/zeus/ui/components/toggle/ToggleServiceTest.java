package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.components.toggle.mock.MockToggleComponentType;
import com.theairebellion.zeus.ui.components.toggle.mock.MockToggleService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.testutil.MockSmartWebElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("ToggleService Interface Default Methods")
class ToggleServiceTest extends BaseUnitUITest {

   private static final MockToggleComponentType DEFAULT_TYPE = MockToggleComponentType.DUMMY_TOGGLE;
   private static final String TOGGLE_TEXT_ON = "On";
   private static final String TOGGLE_TEXT_OFF = "Off";
   private MockToggleService service;
   private SmartWebElement container;
   private By locator;

   @BeforeEach
   void setUp() {
      // Given
      service = new MockToggleService();
      container = MockSmartWebElement.createMock();
      locator = By.id("testToggle");
      service.reset();
   }

   @Nested
   @DisplayName("Activate Default Methods Tests")
   class ActivateDefaultMethodsTests {

      @Test
      @DisplayName("activate with container and text delegates correctly")
      void activateWithContainerAndTextDefault() {
         // Given - setup in @BeforeEach

         // When
         service.activate(container, TOGGLE_TEXT_ON);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(TOGGLE_TEXT_ON);
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("activate with text only delegates correctly")
      void activateWithTextOnlyDefault() {
         // Given - setup in @BeforeEach

         // When
         service.activate(TOGGLE_TEXT_ON);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastText).isEqualTo(TOGGLE_TEXT_ON);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("activate with locator delegates correctly")
      void activateWithLocatorDefault() {
         // Given - setup in @BeforeEach

         // When
         service.activate(locator);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastText).isNull();
      }
   }

   @Nested
   @DisplayName("Deactivate Default Methods Tests")
   class DeactivateDefaultMethodsTests {

      @Test
      @DisplayName("deactivate with container and text delegates correctly")
      void deactivateWithContainerAndTextDefault() {
         // Given - setup in @BeforeEach

         // When
         service.deactivate(container, TOGGLE_TEXT_OFF);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(TOGGLE_TEXT_OFF);
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("deactivate with text only delegates correctly")
      void deactivateWithTextOnlyDefault() {
         // Given - setup in @BeforeEach

         // When
         service.deactivate(TOGGLE_TEXT_OFF);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastText).isEqualTo(TOGGLE_TEXT_OFF);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("deactivate with locator delegates correctly")
      void deactivateWithLocatorDefault() {
         // Given - setup in @BeforeEach

         // When
         service.deactivate(locator);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastText).isNull();
      }
   }

   @Nested
   @DisplayName("IsEnabled Default Methods Tests")
   class IsEnabledDefaultMethodsTests {

      @Test
      @DisplayName("isEnabled with container and text delegates correctly")
      void isEnabledWithContainerAndTextDefault() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isEnabled(container, TOGGLE_TEXT_ON);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(TOGGLE_TEXT_ON);
      }

      @Test
      @DisplayName("isEnabled with text only delegates correctly")
      void isEnabledWithTextOnlyDefault() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isEnabled(TOGGLE_TEXT_ON);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastText).isEqualTo(TOGGLE_TEXT_ON);
      }

      @Test
      @DisplayName("isEnabled with locator delegates correctly")
      void isEnabledWithLocatorDefault() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isEnabled(locator);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
      }
   }

   @Nested
   @DisplayName("IsActivated Default Methods Tests")
   class IsActivatedDefaultMethodsTests {

      @Test
      @DisplayName("isActivated with container and text delegates correctly")
      void isActivatedWithContainerAndTextDefault() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isActivated(container, TOGGLE_TEXT_ON);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(TOGGLE_TEXT_ON);
      }

      @Test
      @DisplayName("isActivated with text only delegates correctly")
      void isActivatedWithTextOnlyDefault() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isActivated(TOGGLE_TEXT_ON);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastText).isEqualTo(TOGGLE_TEXT_ON);
      }

      @Test
      @DisplayName("isActivated with locator delegates correctly")
      void isActivatedWithLocatorDefault() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isActivated(locator);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
      }
   }
}