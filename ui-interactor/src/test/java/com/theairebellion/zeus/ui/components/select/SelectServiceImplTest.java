package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.select.mock.MockSelectComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import java.util.List;
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
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("SelectServiceImpl Unit Tests")
class SelectServiceImplTest extends BaseUnitUITest {

   private SmartWebDriver driver;
   private SelectServiceImpl service;
   private SmartWebElement container;
   private By locator;
   private Strategy strategy;
   private Select selectMock;
   private MockSelectComponentType componentType;
   private MockedStatic<ComponentFactory> factoryMock;

   @BeforeEach
   void setUp() {
      // Initialize mocks
      driver = mock(SmartWebDriver.class);
      service = new SelectServiceImpl(driver);
      container = MockSmartWebElement.createMock();
      locator = By.id("testSelect");
      strategy = Strategy.FIRST;
      selectMock = mock(Select.class);
      componentType = MockSelectComponentType.DUMMY;

      // Configure static mock for ComponentFactory
      factoryMock = mockStatic(ComponentFactory.class);
      factoryMock.when(() -> ComponentFactory.getSelectComponent(eq(componentType), eq(driver)))
            .thenReturn(selectMock);
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
      @DisplayName("selectOptions with SmartWebElement correctly delegates to component")
      void selectOptionsWithSmartWebElement() {
         // Given
         var values = new String[] {"option1", "option2"};

         // When
         service.selectOptions(componentType, container, values);

         // Then
         verify(selectMock).selectOptions(container, values);
      }

      @Test
      @DisplayName("selectOption with SmartWebElement correctly delegates to component")
      void selectOptionWithSmartWebElement() {
         // Given
         var value = "option1";

         // When
         service.selectOption(componentType, container, value);

         // Then
         verify(selectMock).selectOptions(container, value);
      }

      @Test
      @DisplayName("selectOptions with By locator correctly delegates to component")
      void selectOptionsWithByLocator() {
         // Given
         var values = new String[] {"option1", "option2"};

         // When
         service.selectOptions(componentType, locator, values);

         // Then
         verify(selectMock).selectOptions(locator, values);
      }

      @Test
      @DisplayName("selectOption with By locator correctly delegates to component")
      void selectOptionWithByLocator() {
         // Given
         var value = "option1";

         // When
         service.selectOption(componentType, locator, value);

         // Then
         verify(selectMock).selectOptions(locator, value);
      }

      @Test
      @DisplayName("selectOptions with SmartWebElement and Strategy correctly delegates")
      void selectOptionsWithSmartWebElementAndStrategy() {
         // Given
         var expectedOptions = List.of("option1", "option2");
         when(selectMock.selectOptions(container, strategy)).thenReturn(expectedOptions);

         // When
         var result = service.selectOptions(componentType, container, strategy);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         verify(selectMock).selectOptions(container, strategy);
      }

      @Test
      @DisplayName("selectOptions with By locator and Strategy correctly delegates")
      void selectOptionsWithByLocatorAndStrategy() {
         // Given
         var expectedOptions = List.of("option1", "option2");
         when(selectMock.selectOptions(locator, strategy)).thenReturn(expectedOptions);

         // When
         var result = service.selectOptions(componentType, locator, strategy);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         verify(selectMock).selectOptions(locator, strategy);
      }

      @Test
      @DisplayName("getAvailableOptions with SmartWebElement correctly delegates")
      void getAvailableOptionsWithSmartWebElement() {
         // Given
         var expectedOptions = List.of("option1", "option2");
         when(selectMock.getAvailableOptions(container)).thenReturn(expectedOptions);

         // When
         var result = service.getAvailableOptions(componentType, container);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         verify(selectMock).getAvailableOptions(container);
      }

      @Test
      @DisplayName("getAvailableOptions with By locator correctly delegates")
      void getAvailableOptionsWithByLocator() {
         // Given
         var expectedOptions = List.of("option1", "option2");
         when(selectMock.getAvailableOptions(locator)).thenReturn(expectedOptions);

         // When
         var result = service.getAvailableOptions(componentType, locator);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         verify(selectMock).getAvailableOptions(locator);
      }

      @Test
      @DisplayName("getSelectedOptions with SmartWebElement correctly delegates")
      void getSelectedOptionsWithSmartWebElement() {
         // Given
         var expectedOptions = List.of("selected1", "selected2");
         when(selectMock.getSelectedOptions(container)).thenReturn(expectedOptions);

         // When
         var result = service.getSelectedOptions(componentType, container);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         verify(selectMock).getSelectedOptions(container);
      }

      @Test
      @DisplayName("getSelectedOptions with By locator correctly delegates")
      void getSelectedOptionsWithByLocator() {
         // Given
         var expectedOptions = List.of("selected1", "selected2");
         when(selectMock.getSelectedOptions(locator)).thenReturn(expectedOptions);

         // When
         var result = service.getSelectedOptions(componentType, locator);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         verify(selectMock).getSelectedOptions(locator);
      }

      @Test
      @DisplayName("isOptionVisible with SmartWebElement correctly delegates")
      void isOptionVisibleWithSmartWebElement() {
         // Given
         when(selectMock.isOptionVisible(container, "optionValue")).thenReturn(true);

         // When
         var result = service.isOptionVisible(componentType, container, "optionValue");

         // Then
         assertThat(result).isTrue();
         verify(selectMock).isOptionVisible(container, "optionValue");
      }

      @Test
      @DisplayName("isOptionVisible with By locator correctly delegates")
      void isOptionVisibleWithByLocator() {
         // Given
         when(selectMock.isOptionVisible(locator, "optionValue")).thenReturn(true);

         // When
         var result = service.isOptionVisible(componentType, locator, "optionValue");

         // Then
         assertThat(result).isTrue();
         verify(selectMock).isOptionVisible(locator, "optionValue");
      }

      @Test
      @DisplayName("isOptionEnabled with SmartWebElement correctly delegates")
      void isOptionEnabledWithSmartWebElement() {
         // Given
         when(selectMock.isOptionEnabled(container, "optionValue")).thenReturn(true);

         // When
         var result = service.isOptionEnabled(componentType, container, "optionValue");

         // Then
         assertThat(result).isTrue();
         verify(selectMock).isOptionEnabled(container, "optionValue");
      }

      @Test
      @DisplayName("isOptionEnabled with By locator correctly delegates")
      void isOptionEnabledWithByLocator() {
         // Given
         when(selectMock.isOptionEnabled(locator, "optionValue")).thenReturn(true);

         // When
         var result = service.isOptionEnabled(componentType, locator, "optionValue");

         // Then
         assertThat(result).isTrue();
         verify(selectMock).isOptionEnabled(locator, "optionValue");
      }
   }

   @Nested
   @DisplayName("Default Method Overloads Tests")
   class DefaultMethodOverloadsTests {

      @BeforeEach
      void setUpDefaultTypeMock() {
         // Additional setup for testing default method overloads
         factoryMock.when(() -> ComponentFactory.getSelectComponent(any(SelectComponentType.class), eq(driver)))
               .thenReturn(selectMock);
      }

      @Test
      @DisplayName("selectOptions default overload with SmartWebElement delegates correctly")
      void selectOptionsDefaultOverloadWithSmartWebElement() {
         // When
         service.selectOptions(container, "val1", "val2");

         // Then
         verify(selectMock).selectOptions(container, "val1", "val2");
      }

      @Test
      @DisplayName("selectOption default overload with SmartWebElement delegates correctly")
      void selectOptionDefaultOverloadWithSmartWebElement() {
         // When
         service.selectOption(container, "val1");

         // Then
         verify(selectMock).selectOptions(container, "val1");
      }

      @Test
      @DisplayName("selectOptions default overload with By locator delegates correctly")
      void selectOptionsDefaultOverloadWithByLocator() {
         // When
         service.selectOptions(locator, "val1", "val2");

         // Then
         verify(selectMock).selectOptions(locator, "val1", "val2");
      }

      @Test
      @DisplayName("selectOption default overload with By locator delegates correctly")
      void selectOptionDefaultOverloadWithByLocator() {
         // When
         service.selectOption(locator, "val1");

         // Then
         verify(selectMock).selectOptions(locator, "val1");
      }

      @Test
      @DisplayName("getAvailableOptions default overload with SmartWebElement delegates correctly")
      void getAvailableOptionsDefaultOverloadWithSmartWebElement() {
         // Given
         var expectedOptions = List.of("opt1", "opt2");
         when(selectMock.getAvailableOptions(container)).thenReturn(expectedOptions);

         // When
         var result = service.getAvailableOptions(container);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         verify(selectMock).getAvailableOptions(container);
      }

      @Test
      @DisplayName("getAvailableOptions default overload with By locator delegates correctly")
      void getAvailableOptionsDefaultOverloadWithByLocator() {
         // Given
         var expectedOptions = List.of("opt1", "opt2");
         when(selectMock.getAvailableOptions(locator)).thenReturn(expectedOptions);

         // When
         var result = service.getAvailableOptions(locator);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         verify(selectMock).getAvailableOptions(locator);
      }

      @Test
      @DisplayName("getSelectedOptions default overload with SmartWebElement delegates correctly")
      void getSelectedOptionsDefaultOverloadWithSmartWebElement() {
         // Given
         var expectedOptions = List.of("sel1", "sel2");
         when(selectMock.getSelectedOptions(container)).thenReturn(expectedOptions);

         // When
         var result = service.getSelectedOptions(container);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         verify(selectMock).getSelectedOptions(container);
      }

      @Test
      @DisplayName("getSelectedOptions default overload with By locator delegates correctly")
      void getSelectedOptionsDefaultOverloadWithByLocator() {
         // Given
         var expectedOptions = List.of("sel1", "sel2");
         when(selectMock.getSelectedOptions(locator)).thenReturn(expectedOptions);

         // When
         var result = service.getSelectedOptions(locator);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         verify(selectMock).getSelectedOptions(locator);
      }

      @Test
      @DisplayName("isOptionVisible default overload with SmartWebElement delegates correctly")
      void isOptionVisibleDefaultOverloadWithSmartWebElement() {
         // Given
         when(selectMock.isOptionVisible(container, "val1")).thenReturn(true);

         // When
         var result = service.isOptionVisible(container, "val1");

         // Then
         assertThat(result).isTrue();
         verify(selectMock).isOptionVisible(container, "val1");
      }

      @Test
      @DisplayName("isOptionVisible default overload with By locator delegates correctly")
      void isOptionVisibleDefaultOverloadWithByLocator() {
         // Given
         when(selectMock.isOptionVisible(locator, "val1")).thenReturn(true);

         // When
         var result = service.isOptionVisible(locator, "val1");

         // Then
         assertThat(result).isTrue();
         verify(selectMock).isOptionVisible(locator, "val1");
      }

      @Test
      @DisplayName("isOptionEnabled default overload with SmartWebElement delegates correctly")
      void isOptionEnabledDefaultOverloadWithSmartWebElement() {
         // Given
         when(selectMock.isOptionEnabled(container, "val1")).thenReturn(true);

         // When
         var result = service.isOptionEnabled(container, "val1");

         // Then
         assertThat(result).isTrue();
         verify(selectMock).isOptionEnabled(container, "val1");
      }

      @Test
      @DisplayName("isOptionEnabled default overload with By locator delegates correctly")
      void isOptionEnabledDefaultOverloadWithByLocator() {
         // Given
         when(selectMock.isOptionEnabled(locator, "val1")).thenReturn(true);

         // When
         var result = service.isOptionEnabled(locator, "val1");

         // Then
         assertThat(result).isTrue();
         verify(selectMock).isOptionEnabled(locator, "val1");
      }
   }

   @Nested
   @DisplayName("Component Caching Tests")
   class ComponentCachingTests {

      @Test
      @DisplayName("Component is cached and reused")
      void componentCaching() {
         // When
         service.selectOption(componentType, container, "value1");
         service.selectOption(componentType, container, "value2");

         // Then
         factoryMock.verify(() -> ComponentFactory.getSelectComponent(eq(componentType), eq(driver)), times(1));
      }

      @Test
      @DisplayName("Different component types create different instances")
      void differentComponentTypes() {
         // Create mock components and a new service
         Select select1 = mock(Select.class);
         Select select2 = mock(Select.class);

         // Create two different component types
         MockSelectComponentType type1 = MockSelectComponentType.DUMMY;

         // Create a service for testing
         SelectServiceImpl testService = new SelectServiceImpl(driver);

         // Configure factory mock
         factoryMock.reset();
         factoryMock.when(() -> ComponentFactory.getSelectComponent(eq(type1), eq(driver)))
               .thenReturn(select1);

         // Call method with first component type
         testService.selectOption(type1, container, "value1");

         // Verify first mock received call
         verify(select1).selectOptions(container, "value1");

         // Now reset verification state
         clearInvocations(select1);

         // Configure factory to return second component for a new type
         MockSelectComponentType type2 = MockSelectComponentType.TEST;
         factoryMock.when(() -> ComponentFactory.getSelectComponent(eq(type2), eq(driver)))
               .thenReturn(select2);

         // Call method with second component type
         testService.selectOption(type2, container, "value2");

         // Verify second mock received call
         verify(select2).selectOptions(container, "value2");

         // Verify first mock didn't receive new call
         verifyNoMoreInteractions(select1);
      }
   }

   @Test
   @DisplayName("insertion method converts objects to strings")
   void insertionConvertsObjects() {
      // Given
      var obj1 = 42;
      var obj2 = 3.14;

      // When
      service.insertion(componentType, locator, obj1, obj2);

      // Then
      verify(selectMock).selectOptions(eq(locator), eq("42"), eq("3.14"));
   }
}