package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.toggle.mock.MockToggleComponentType;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("ToggleServiceImpl Unit Tests")
class ToggleServiceImplTest extends BaseUnitUITest {

   private SmartWebDriver driver;
   private ToggleServiceImpl service;
   private SmartWebElement container;
   private Toggle toggleMock;
   private MockToggleComponentType componentType;
   private By locator;
   private MockedStatic<ComponentFactory> factoryMock;

   @BeforeEach
   void setUp() {
      driver = mock(SmartWebDriver.class);
      service = new ToggleServiceImpl(driver);
      container = MockSmartWebElement.createMock();
      toggleMock = mock(Toggle.class);
      componentType = MockToggleComponentType.DUMMY;
      locator = By.id("toggle");

      // Configure static mock for ComponentFactory
      factoryMock = mockStatic(ComponentFactory.class);
      factoryMock.when(() -> ComponentFactory.getToggleComponent(eq(componentType), eq(driver)))
            .thenReturn(toggleMock);
   }

   @AfterEach
   void tearDown() {
      if (factoryMock != null) {
         factoryMock.close();
      }
   }

   @Nested
   @DisplayName("Activate Method Tests")
   class ActivateMethodTests {

      @Test
      @DisplayName("activate with container and text delegates correctly")
      void activateWithContainerAndText() {
         // Given
         var toggleText = "On";

         // When
         service.activate(componentType, container, toggleText);

         // Then
         verify(toggleMock).activate(container, toggleText);
      }

      @Test
      @DisplayName("activate with text only delegates correctly")
      void activateWithTextOnly() {
         // Given
         var toggleText = "On";

         // When
         service.activate(componentType, toggleText);

         // Then
         verify(toggleMock).activate(toggleText);
      }

      @Test
      @DisplayName("activate with locator delegates correctly")
      void activateWithLocator() {
         // When
         service.activate(componentType, locator);

         // Then
         verify(toggleMock).activate(locator);
      }
   }

   @Nested
   @DisplayName("Deactivate Method Tests")
   class DeactivateMethodTests {

      @Test
      @DisplayName("deactivate with container and text delegates correctly")
      void deactivateWithContainerAndText() {
         // Given
         var toggleText = "Off";

         // When
         service.deactivate(componentType, container, toggleText);

         // Then
         verify(toggleMock).deactivate(container, toggleText);
      }

      @Test
      @DisplayName("deactivate with text only delegates correctly")
      void deactivateWithTextOnly() {
         // Given
         var toggleText = "Off";

         // When
         service.deactivate(componentType, toggleText);

         // Then
         verify(toggleMock).deactivate(toggleText);
      }

      @Test
      @DisplayName("deactivate with locator delegates correctly")
      void deactivateWithLocator() {
         // When
         service.deactivate(componentType, locator);

         // Then
         verify(toggleMock).deactivate(locator);
      }
   }

   @Nested
   @DisplayName("IsEnabled Method Tests")
   class IsEnabledMethodTests {

      @Test
      @DisplayName("isEnabled with container and text delegates correctly")
      void isEnabledWithContainerAndText() {
         // Given
         var toggleText = "On";
         when(toggleMock.isEnabled(container, toggleText)).thenReturn(true);

         // When
         var result = service.isEnabled(componentType, container, toggleText);

         // Then
         assertThat(result).isTrue();
         verify(toggleMock).isEnabled(container, toggleText);
      }

      @Test
      @DisplayName("isEnabled with text only delegates correctly")
      void isEnabledWithTextOnly() {
         // Given
         var toggleText = "On";
         when(toggleMock.isEnabled(toggleText)).thenReturn(true);

         // When
         var result = service.isEnabled(componentType, toggleText);

         // Then
         assertThat(result).isTrue();
         verify(toggleMock).isEnabled(toggleText);
      }

      @Test
      @DisplayName("isEnabled with locator delegates correctly")
      void isEnabledWithLocator() {
         // Given
         when(toggleMock.isEnabled(locator)).thenReturn(true);

         // When
         var result = service.isEnabled(componentType, locator);

         // Then
         assertThat(result).isTrue();
         verify(toggleMock).isEnabled(locator);
      }
   }

   @Nested
   @DisplayName("IsActivated Method Tests")
   class IsActivatedMethodTests {

      @Test
      @DisplayName("isActivated with container and text delegates correctly")
      void isActivatedWithContainerAndText() {
         // Given
         var toggleText = "On";
         when(toggleMock.isActivated(container, toggleText)).thenReturn(true);

         // When
         var result = service.isActivated(componentType, container, toggleText);

         // Then
         assertThat(result).isTrue();
         verify(toggleMock).isActivated(container, toggleText);
      }

      @Test
      @DisplayName("isActivated with text only delegates correctly")
      void isActivatedWithTextOnly() {
         // Given
         var toggleText = "On";
         when(toggleMock.isActivated(toggleText)).thenReturn(true);

         // When
         var result = service.isActivated(componentType, toggleText);

         // Then
         assertThat(result).isTrue();
         verify(toggleMock).isActivated(toggleText);
      }

      @Test
      @DisplayName("isActivated with locator delegates correctly")
      void isActivatedWithLocator() {
         // Given
         when(toggleMock.isActivated(locator)).thenReturn(true);

         // When
         var result = service.isActivated(componentType, locator);

         // Then
         assertThat(result).isTrue();
         verify(toggleMock).isActivated(locator);
      }
   }

   @Nested
   @DisplayName("Component Caching Tests")
   class ComponentCachingTests {

      @Test
      @DisplayName("Component is cached and reused")
      void componentCaching() {
         // When
         service.activate(componentType, container, "On");
         service.deactivate(componentType, container, "Off");
         service.isEnabled(componentType, container, "On");

         // Then
         factoryMock.verify(() -> ComponentFactory.getToggleComponent(eq(componentType), eq(driver)), times(1));
      }

      @Test
      @DisplayName("Different component types create different instances")
      void differentComponentTypes() {
         // Setup mock component types
         componentType = MockToggleComponentType.DUMMY;
         var componentType2 = MockToggleComponentType.TEST;

         // Create mock components
         var toggle1 = mock(Toggle.class);
         var toggle2 = mock(Toggle.class);

         // Configure behavior
         when(toggle1.isActivated(container, "On")).thenReturn(false);
         when(toggle2.isActivated(container, "On")).thenReturn(true);

         // Configure factory mock
         factoryMock.reset();
         factoryMock.when(() -> ComponentFactory.getToggleComponent(eq(componentType), eq(driver)))
               .thenReturn(toggle1);
         factoryMock.when(() -> ComponentFactory.getToggleComponent(eq(componentType2), eq(driver)))
               .thenReturn(toggle2);

         // First component type operation
         var result1 = service.isActivated(componentType, container, "On");
         assertThat(result1).isFalse();
         verify(toggle1).isActivated(container, "On");

         // Second component type operation
         var result2 = service.isActivated(componentType2, container, "On");
         assertThat(result2).isTrue();
         verify(toggle2).isActivated(container, "On");

         // Verify factory calls
         factoryMock.verify(() -> ComponentFactory.getToggleComponent(eq(componentType), eq(driver)), times(1));
         factoryMock.verify(() -> ComponentFactory.getToggleComponent(eq(componentType2), eq(driver)), times(1));
      }
   }
}