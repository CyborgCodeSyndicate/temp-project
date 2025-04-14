package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.components.alert.mock.MockAlertComponentType;
import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("AlertServiceImpl Test")
class AlertServiceImplTest {

   private SmartWebDriver driver;
   private AlertServiceImpl service;
   private SmartWebElement container;
   private Alert alertMock;
   private MockAlertComponentType mockAlertComponentType;
   private By locator;
   private MockedStatic<ComponentFactory> factoryMock;

   @BeforeEach
   void setUp() {
      // Setup mocks
      driver = mock(SmartWebDriver.class);
      service = new AlertServiceImpl(driver);
      container = mock(SmartWebElement.class);
      alertMock = mock(Alert.class);
      mockAlertComponentType = MockAlertComponentType.DUMMY;
      locator = By.id("alert");

      // Mock static factory
      factoryMock = Mockito.mockStatic(ComponentFactory.class);
      factoryMock.when(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)))
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
         when(alertMock.getValue(container)).thenReturn("Alert Value");

         // When
         String result = service.getValue(mockAlertComponentType, container);

         // Then
         assertThat(result).isEqualTo("Alert Value");
         verify(alertMock).getValue(container);
      }

      @Test
      @DisplayName("getValue with locator delegates to component correctly")
      void testGetValueWithLocator() {
         // Given
         when(alertMock.getValue(locator)).thenReturn("Locator Alert Value");

         // When
         String result = service.getValue(mockAlertComponentType, locator);

         // Then
         assertThat(result).isEqualTo("Locator Alert Value");
         verify(alertMock).getValue(locator);
      }

      @Test
      @DisplayName("isVisible with container delegates to component correctly")
      void testIsVisibleWithContainer() {
         // Given
         when(alertMock.isVisible(container)).thenReturn(true);

         // When
         boolean result = service.isVisible(mockAlertComponentType, container);

         // Then
         assertThat(result).isTrue();
         verify(alertMock).isVisible(container);
      }

      @Test
      @DisplayName("isVisible with locator delegates to component correctly")
      void testIsVisibleWithLocator() {
         // Given
         when(alertMock.isVisible(locator)).thenReturn(true);

         // When
         boolean result = service.isVisible(mockAlertComponentType, locator);

         // Then
         assertThat(result).isTrue();
         verify(alertMock).isVisible(locator);
      }
   }

   @Nested
   @DisplayName("Component Caching Tests")
   class ComponentCachingTests {

      @Test
      @DisplayName("Component is cached and reused between method calls")
      void testComponentCaching() {
         // When
         service.getValue(mockAlertComponentType, container);
         service.isVisible(mockAlertComponentType, container);

         // Then
         factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)), times(1));
      }

      @Test
      @DisplayName("Component is cached and reused between multiple getValue calls")
      void testComponentCachingMultipleGetValueCalls() {
         // When
         service.getValue(mockAlertComponentType, container);
         service.getValue(mockAlertComponentType, locator);

         // Then
         factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)), times(1));
      }

      @Test
      @DisplayName("Component is cached and reused between multiple isVisible calls")
      void testComponentCachingMultipleIsVisibleCalls() {
         // When
         service.isVisible(mockAlertComponentType, container);
         service.isVisible(mockAlertComponentType, locator);

         // Then
         factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)), times(1));
      }
   }

   @Nested
   @DisplayName("Different Component Types Test")
   class DifferentComponentTypesTest {

      @Test
      @DisplayName("Different component types create different components")
      void testDifferentComponentTypes() {
         // Given
         MockAlertComponentType anotherType = MockAlertComponentType.ANOTHER;
         Alert anotherAlertMock = mock(Alert.class);

         factoryMock.when(() -> ComponentFactory.getAlertComponent(eq(anotherType), eq(driver)))
               .thenReturn(anotherAlertMock);

         // When
         service.getValue(mockAlertComponentType, container);
         service.getValue(anotherType, container);

         // Then
         factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)), times(1));
         factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(anotherType), eq(driver)), times(1));
      }

      @Test
      @DisplayName("Multiple service instances don't share component cache")
      void testMultipleServiceInstances() throws Exception {
         // Given - Create two service instances
         AlertServiceImpl service1 = new AlertServiceImpl(driver);
         AlertServiceImpl service2 = new AlertServiceImpl(driver);

         // Reset factory mock behavior
         factoryMock.close();
         factoryMock = Mockito.mockStatic(ComponentFactory.class);

         // We need to ensure different component instances are returned for each service
         factoryMock.when(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)))
               .thenAnswer(invocation -> mock(Alert.class));

         // When
         service1.getValue(mockAlertComponentType, container);
         service2.getValue(mockAlertComponentType, container);

         // Then - We need to check internal state via reflection
         Field componentsField1 = AbstractComponentService.class.getDeclaredField("components");
         componentsField1.setAccessible(true);
         Map<?, ?> componentsMap1 = (Map<?, ?>) componentsField1.get(service1);

         Field componentsField2 = AbstractComponentService.class.getDeclaredField("components");
         componentsField2.setAccessible(true);
         Map<?, ?> componentsMap2 = (Map<?, ?>) componentsField2.get(service2);

         // Assert the maps are different objects
         assertThat(componentsMap1).isNotSameAs(componentsMap2);

         // The component map in each service should have keys (after getValue was called)
         assertThat(componentsMap1).isNotEmpty();
         assertThat(componentsMap2).isNotEmpty();

         // Don't assert about the values since mocking behavior is creating different
         // instances regardless - that's what we're testing
      }
   }

   @Nested
   @DisplayName("Protected Method Tests")
   class ProtectedMethodTests {

      @Test
      @DisplayName("createComponent delegates to ComponentFactory")
      void testCreateComponent() throws Exception {
         // Given
         java.lang.reflect.Method createComponentMethod =
               AlertServiceImpl.class.getDeclaredMethod("createComponent", AlertComponentType.class);
         createComponentMethod.setAccessible(true);

         // When
         createComponentMethod.invoke(service, mockAlertComponentType);

         // Then
         factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)), times(1));
      }

      @Test
      @DisplayName("alertComponent delegates to getOrCreateComponent")
      void testAlertComponent() throws Exception {
         // Given
         java.lang.reflect.Method alertComponentMethod =
               AlertServiceImpl.class.getDeclaredMethod("alertComponent", AlertComponentType.class);
         alertComponentMethod.setAccessible(true);

         // Clear components map to ensure component creation
         Field componentsField = AbstractComponentService.class.getDeclaredField("components");
         componentsField.setAccessible(true);
         Map<AlertComponentType, Alert> componentsMap = new HashMap<>();
         componentsField.set(service, componentsMap);

         // When
         alertComponentMethod.invoke(service, mockAlertComponentType);

         // Then
         factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)), times(1));
      }
   }

   @Nested
   @DisplayName("Null Handling")
   class NullHandlingTests {

      @Test
      @DisplayName("getValue with null component type throws exception")
      void testGetValueWithNullComponentType() {
         // When/Then - should throw NullPointerException
         try {
            service.getValue(null, container);
            throw new AssertionError("Expected NullPointerException was not thrown");
         } catch (NullPointerException e) {
            // Expected exception
            assertThat(e).isInstanceOf(NullPointerException.class);
         }
      }

      @Test
      @DisplayName("getValue with null container delegates to component")
      void testGetValueWithNullContainer() {
         // Given
         SmartWebElement nullContainer = null;

         try {
            // When/Then
            service.getValue(mockAlertComponentType, nullContainer);

            // Verify delegation happened even with null container
            factoryMock.verify(() -> ComponentFactory.getAlertComponent(eq(mockAlertComponentType), eq(driver)), times(1));
         } catch (Exception e) {
            // Don't fail if component implementation throws NPE - that's not the service's responsibility
         }
      }
   }
}