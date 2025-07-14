package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.components.alert.mock.MockAlertComponentType;
import com.theairebellion.zeus.ui.components.alert.mock.MockAlertService;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.testutil.MockSmartWebElement;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@DisplayName("AlertService Test")
class AlertServiceTest extends BaseUnitUITest {

   private MockAlertService service;
   private SmartWebElement container;
   private By locator;

   @BeforeEach
   void setUp() {
      // Given
      service = new MockAlertService();
      container = MockSmartWebElement.createMock();
      locator = By.id("alertLocator");
      service.reset();
   }

   @Test
   void testGetDefaultTypeShouldReturnNullWhenExceptionIsThrown() throws Exception {
      UiConfig mockConfig = mock(UiConfig.class);
      when(mockConfig.alertDefaultType()).thenReturn("SomeType");
      when(mockConfig.projectPackage()).thenReturn("com.example");

      try (
            MockedStatic<UiConfigHolder> configMock = mockStatic(UiConfigHolder.class);
            MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)
      ) {
         configMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);

         reflectionMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
               AlertComponentType.class,
               "SomeType",
               "com.example"
         )).thenThrow(new RuntimeException("Simulated failure"));

         // Use reflection to access the private static method
         Method method = AlertService.class.getDeclaredMethod("getDefaultType");
         method.setAccessible(true);

         AlertComponentType result = (AlertComponentType) method.invoke(null);

         assertNull(result);
      }
   }

   @Nested
   @DisplayName("Default Method Delegation Tests")
   class DefaultMethodDelegationTests {

      @Test
      @DisplayName("getValue with container delegates to implementation")
      void testGetValueWithContainerDefault() {
         // Given - setup in @BeforeEach

         // When
         var result = service.getValue(container);

         // Then
         assertThat(result).isEqualTo(MockAlertService.VALUE_CONTAINER);
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
         assertThat(service.explicitComponentType).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("getValue with locator delegates to implementation")
      void testGetValueWithLocatorDefault() {
         // Given - setup in @BeforeEach

         // When
         var result = service.getValue(locator);

         // Then
         assertThat(result).isEqualTo(MockAlertService.VALUE_LOCATOR);
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
         assertThat(service.explicitComponentType).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
         assertThat(service.lastLocator).isEqualTo(locator);
      }

      @Test
      @DisplayName("isVisible with container delegates to implementation")
      void testIsVisibleWithContainerDefault() {
         // Given - setup in @BeforeEach

         // When
         var visible = service.isVisible(container);

         // Then
         assertThat(visible).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
         assertThat(service.explicitComponentType).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("isVisible with locator delegates to implementation")
      void testIsVisibleWithLocatorDefault() {
         // Given - setup in @BeforeEach

         // When
         var visible = service.isVisible(locator);

         // Then
         assertThat(visible).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
         assertThat(service.explicitComponentType).isEqualTo(MockAlertComponentType.DUMMY_ALERT);
         assertThat(service.lastLocator).isEqualTo(locator);
      }
   }
}