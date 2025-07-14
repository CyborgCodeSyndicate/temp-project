package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.components.alert.mock.MockAlertComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
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

@DisplayName("AlertServiceImpl Test")
class AlertServiceImplTest extends BaseUnitUITest {

   private static final String ALERT_VALUE_CONTAINER = "Alert Value Container";
   private static final String ALERT_VALUE_LOCATOR = "Alert Value Locator";
   private static final String VALUE_FROM_NULL_CONTAINER = "ValueFromNullContainer";
   private static final String VALUE_FROM_NULL_LOCATOR = "ValueFromNullLocator";
   private final MockAlertComponentType mockAlertComponentType = MockAlertComponentType.DUMMY_ALERT;
   @Mock
   private SmartWebDriver driver;
   @Mock
   private SmartWebElement container;
   @Mock
   private Alert alertMock;
   @Mock
   private By locator;
   private AlertServiceImpl service;
   private MockedStatic<ComponentFactory> factoryMock;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this);
      service = new AlertServiceImpl(driver);
      locator = By.id("alert");

      factoryMock = Mockito.mockStatic(ComponentFactory.class);
      factoryMock.when(() -> ComponentFactory.getAlertComponent(any(AlertComponentType.class), eq(driver)))
            .thenReturn(alertMock);
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
      @DisplayName("getValue with container delegates to component correctly")
      void testGetValueWithContainer() {
         // Given
         when(alertMock.getValue(container)).thenReturn(ALERT_VALUE_CONTAINER);

         // When
         var result = service.getValue(mockAlertComponentType, container);

         // Then
         assertThat(result).isEqualTo(ALERT_VALUE_CONTAINER);
         verify(alertMock).getValue(container);
         verifyNoMoreInteractions(alertMock);
      }

      @Test
      @DisplayName("getValue with locator delegates to component correctly")
      void testGetValueWithLocator() {
         // Given
         when(alertMock.getValue(locator)).thenReturn(ALERT_VALUE_LOCATOR);

         // When
         var result = service.getValue(mockAlertComponentType, locator);

         // Then
         assertThat(result).isEqualTo(ALERT_VALUE_LOCATOR);
         verify(alertMock).getValue(locator);
         verifyNoMoreInteractions(alertMock);
      }

      @Test
      @DisplayName("isVisible with container delegates to component correctly")
      void testIsVisibleWithContainer() {
         // Given
         when(alertMock.isVisible(container)).thenReturn(true);

         // When
         var result = service.isVisible(mockAlertComponentType, container);

         // Then
         assertThat(result).isTrue();
         verify(alertMock).isVisible(container);
         verifyNoMoreInteractions(alertMock);
      }

      @Test
      @DisplayName("isVisible with locator delegates to component correctly")
      void testIsVisibleWithLocator() {
         // Given
         when(alertMock.isVisible(locator)).thenReturn(true);

         // When
         var result = service.isVisible(mockAlertComponentType, locator);

         // Then
         assertThat(result).isTrue();
         verify(alertMock).isVisible(locator);
         verifyNoMoreInteractions(alertMock);
      }
   }

   @Nested
   @DisplayName("Component Caching Tests")
   class ComponentCachingTests {

      @Test
      @DisplayName("Component is cached and reused between method calls")
      void testComponentCaching() {
         // Given - setup in @BeforeEach

         // When
         service.getValue(mockAlertComponentType, container);
         service.isVisible(mockAlertComponentType, container);

         // Then
         factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)), times(1));
      }
   }

   @Nested
   @DisplayName("Null Handling")
   class NullHandlingTests {
      @Test
      @DisplayName("getValue with null container delegates to component")
      void testGetValueWithNullContainer() {
         // Given
         SmartWebElement nullContainer = null;
         when(alertMock.getValue(nullContainer)).thenReturn(VALUE_FROM_NULL_CONTAINER);

         // When
         var result = service.getValue(mockAlertComponentType, nullContainer);

         // Then
         assertThat(result).isEqualTo(VALUE_FROM_NULL_CONTAINER);
         factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)), times(1));
         verify(alertMock).getValue(nullContainer);
      }

      @Test
      @DisplayName("isVisible with null container delegates to component")
      void testIsVisibleWithNullContainer() {
         // Given
         SmartWebElement nullContainer = null;
         when(alertMock.isVisible(nullContainer)).thenReturn(false);

         // When
         var result = service.isVisible(mockAlertComponentType, nullContainer);

         // Then
         assertThat(result).isFalse();
         factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)), times(1));
         verify(alertMock).isVisible(nullContainer);
      }

      @Test
      @DisplayName("getValue with null locator delegates to component")
      void testGetValueWithNullLocator() {
         // Given
         By nullLocator = null;
         when(alertMock.getValue(nullLocator)).thenReturn(VALUE_FROM_NULL_LOCATOR);

         // When
         var result = service.getValue(mockAlertComponentType, nullLocator);

         // Then
         assertThat(result).isEqualTo(VALUE_FROM_NULL_LOCATOR);
         factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)), times(1));
         verify(alertMock).getValue(nullLocator);
      }

      @Test
      @DisplayName("isVisible with null locator delegates to component")
      void testIsVisibleWithNullLocator() {
         // Given
         By nullLocator = null;
         when(alertMock.isVisible(nullLocator)).thenReturn(false);

         // When
         var result = service.isVisible(mockAlertComponentType, nullLocator);

         // Then
         assertThat(result).isFalse();
         factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)), times(1));
         verify(alertMock).isVisible(nullLocator);
      }
   }
}