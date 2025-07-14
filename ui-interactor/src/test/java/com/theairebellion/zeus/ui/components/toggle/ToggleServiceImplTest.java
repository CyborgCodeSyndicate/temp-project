package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.toggle.mock.MockToggleComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("ToggleServiceImpl Unit Tests")
class ToggleServiceImplTest extends BaseUnitUITest {

   private static final String TOGGLE_TEXT_ON = "On";
   private static final String TOGGLE_TEXT_OFF = "Off";
   private final MockToggleComponentType componentType = MockToggleComponentType.DUMMY_TOGGLE;
   @Mock
   private SmartWebDriver driver;
   @Mock
   private SmartWebElement container;
   @Mock
   private Toggle toggleMock;
   @Mock
   private By locator;
   private ToggleServiceImpl service;
   private MockedStatic<ComponentFactory> factoryMock;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this);
      service = new ToggleServiceImpl(driver);
      locator = By.id("toggle");

      factoryMock = Mockito.mockStatic(ComponentFactory.class);
      factoryMock.when(() -> ComponentFactory.getToggleComponent(any(ToggleComponentType.class), eq(driver)))
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

         // When
         service.activate(componentType, container, TOGGLE_TEXT_ON);

         // Then
         verify(toggleMock).activate(container, TOGGLE_TEXT_ON);
         verifyNoMoreInteractions(toggleMock);
      }

      @Test
      @DisplayName("activate with text only delegates correctly")
      void activateWithTextOnly() {
         // Given

         // When
         service.activate(componentType, TOGGLE_TEXT_ON);

         // Then
         verify(toggleMock).activate(TOGGLE_TEXT_ON);
         verifyNoMoreInteractions(toggleMock);
      }

      @Test
      @DisplayName("activate with locator delegates correctly")
      void activateWithLocator() {
         // Given

         // When
         service.activate(componentType, locator);

         // Then
         verify(toggleMock).activate(locator);
         verifyNoMoreInteractions(toggleMock);
      }
   }

   @Nested
   @DisplayName("Deactivate Method Tests")
   class DeactivateMethodTests {

      @Test
      @DisplayName("deactivate with container and text delegates correctly")
      void deactivateWithContainerAndText() {
         // Given

         // When
         service.deactivate(componentType, container, TOGGLE_TEXT_OFF);

         // Then
         verify(toggleMock).deactivate(container, TOGGLE_TEXT_OFF);
         verifyNoMoreInteractions(toggleMock);
      }

      @Test
      @DisplayName("deactivate with text only delegates correctly")
      void deactivateWithTextOnly() {
         // Given

         // When
         service.deactivate(componentType, TOGGLE_TEXT_OFF);

         // Then
         verify(toggleMock).deactivate(TOGGLE_TEXT_OFF);
         verifyNoMoreInteractions(toggleMock);
      }

      @Test
      @DisplayName("deactivate with locator delegates correctly")
      void deactivateWithLocator() {
         // Given

         // When
         service.deactivate(componentType, locator);

         // Then
         verify(toggleMock).deactivate(locator);
         verifyNoMoreInteractions(toggleMock);
      }
   }

   @Nested
   @DisplayName("IsEnabled Method Tests")
   class IsEnabledMethodTests {

      @Test
      @DisplayName("isEnabled with container and text delegates correctly")
      void isEnabledWithContainerAndText() {
         // Given
         when(toggleMock.isEnabled(container, TOGGLE_TEXT_ON)).thenReturn(true);

         // When
         var result = service.isEnabled(componentType, container, TOGGLE_TEXT_ON);

         // Then
         assertThat(result).isTrue();
         verify(toggleMock).isEnabled(container, TOGGLE_TEXT_ON);
         verifyNoMoreInteractions(toggleMock);
      }

      @Test
      @DisplayName("isEnabled with text only delegates correctly")
      void isEnabledWithTextOnly() {
         // Given
         when(toggleMock.isEnabled(TOGGLE_TEXT_ON)).thenReturn(true);

         // When
         var result = service.isEnabled(componentType, TOGGLE_TEXT_ON);

         // Then
         assertThat(result).isTrue();
         verify(toggleMock).isEnabled(TOGGLE_TEXT_ON);
         verifyNoMoreInteractions(toggleMock);
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
         verifyNoMoreInteractions(toggleMock);
      }
   }

   @Nested
   @DisplayName("IsActivated Method Tests")
   class IsActivatedMethodTests {

      @Test
      @DisplayName("isActivated with container and text delegates correctly")
      void isActivatedWithContainerAndText() {
         // Given
         when(toggleMock.isActivated(container, TOGGLE_TEXT_ON)).thenReturn(true);

         // When
         var result = service.isActivated(componentType, container, TOGGLE_TEXT_ON);

         // Then
         assertThat(result).isTrue();
         verify(toggleMock).isActivated(container, TOGGLE_TEXT_ON);
         verifyNoMoreInteractions(toggleMock);
      }

      @Test
      @DisplayName("isActivated with text only delegates correctly")
      void isActivatedWithTextOnly() {
         // Given
         when(toggleMock.isActivated(TOGGLE_TEXT_ON)).thenReturn(true);

         // When
         var result = service.isActivated(componentType, TOGGLE_TEXT_ON);

         // Then
         assertThat(result).isTrue();
         verify(toggleMock).isActivated(TOGGLE_TEXT_ON);
         verifyNoMoreInteractions(toggleMock);
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
         verifyNoMoreInteractions(toggleMock);
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
         service.activate(componentType, container, TOGGLE_TEXT_ON);
         service.isEnabled(componentType, container, TOGGLE_TEXT_ON);

         // Then
         factoryMock.verify(() -> ComponentFactory.getToggleComponent(eq(componentType), eq(driver)), times(1));
      }
   }
}