package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.components.select.mock.MockSelectComponentType;
import com.theairebellion.zeus.ui.components.select.mock.MockSelectService;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.testutil.MockSmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import java.lang.reflect.Method;
import java.util.List;
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


@DisplayName("SelectService Interface Default Methods")
class SelectServiceTest extends BaseUnitUITest {

   private static final MockSelectComponentType DEFAULT_TYPE = MockSelectComponentType.DUMMY_SELECT;
   private static final String[] SAMPLE_VALUES = {"val1", "val2"};
   private static final String SINGLE_VALUE = "singleVal";
   private static final List<String> EXPECTED_OPTIONS = List.of("option1", "option2");
   private static final String OPTION_VALUE = "optionValue";
   private MockSelectService service;
   private SmartWebElement container;
   private By locator;
   private Strategy strategy;

   @BeforeEach
   void setUp() {
      // Given
      service = new MockSelectService();
      container = MockSmartWebElement.createMock();
      locator = By.id("testSelect");
      strategy = Strategy.RANDOM;
      service.reset();
   }

   @Test
   void testGetDefaultTypeShouldReturnNullWhenExceptionIsThrown() throws Exception {
      UiConfig mockConfig = mock(UiConfig.class);
      when(mockConfig.selectDefaultType()).thenReturn("SomeType");
      when(mockConfig.projectPackage()).thenReturn("com.example");

      try (
            MockedStatic<UiConfigHolder> configMock = mockStatic(UiConfigHolder.class);
            MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)
      ) {
         configMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);

         reflectionMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
               SelectComponentType.class,
               "SomeType",
               "com.example"
         )).thenThrow(new RuntimeException("Simulated failure"));

         // Use reflection to access the private static method
         Method method = SelectService.class.getDeclaredMethod("getDefaultType");
         method.setAccessible(true);

         SelectComponentType result = (SelectComponentType) method.invoke(null);

         assertNull(result);
      }
   }

   @Nested
   @DisplayName("Default Methods with SmartWebElement")
   class DefaultMethodsWithSmartWebElement {

      @Test
      @DisplayName("selectOptions delegates correctly")
      void selectOptionsDelegates() {
         // Given - setup in @BeforeEach

         // When
         service.selectOptions(container, SAMPLE_VALUES);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastValues).containsExactly(SAMPLE_VALUES);
         assertThat(service.lastLocator).isNull();
         assertThat(service.lastStrategy).isNull();
      }

      @Test
      @DisplayName("selectOption delegates correctly")
      void selectOptionDelegates() {
         // Given - setup in @BeforeEach

         // When
         service.selectOption(container, SINGLE_VALUE);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastValues).containsExactly(SINGLE_VALUE);
         assertThat(service.lastLocator).isNull();
         assertThat(service.lastStrategy).isNull();
      }

      @Test
      @DisplayName("selectOptions with strategy delegates correctly")
      void selectOptionsWithStrategyDelegates() {
         // Given
         service.returnOptions = EXPECTED_OPTIONS;

         // When
         var result = service.selectOptions(container, strategy);

         // Then
         assertThat(result).isEqualTo(EXPECTED_OPTIONS);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastStrategy).isEqualTo(strategy);
         assertThat(service.lastLocator).isNull();
         assertThat(service.lastValues).isNull();
      }

      @Test
      @DisplayName("getAvailableOptions delegates correctly")
      void getAvailableOptionsDelegates() {
         // Given
         service.returnOptions = EXPECTED_OPTIONS;

         // When
         var result = service.getAvailableOptions(container);

         // Then
         assertThat(result).isEqualTo(EXPECTED_OPTIONS);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("getSelectedOptions delegates correctly")
      void getSelectedOptionsDelegates() {
         // Given
         service.returnOptions = EXPECTED_OPTIONS;

         // When
         var result = service.getSelectedOptions(container);

         // Then
         assertThat(result).isEqualTo(EXPECTED_OPTIONS);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("isOptionVisible delegates correctly")
      void isOptionVisibleDelegates() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isOptionVisible(container, OPTION_VALUE);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastValues).containsExactly(OPTION_VALUE);
      }

      @Test
      @DisplayName("isOptionEnabled delegates correctly")
      void isOptionEnabledDelegates() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isOptionEnabled(container, OPTION_VALUE);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastValues).containsExactly(OPTION_VALUE);
      }
   }

   @Nested
   @DisplayName("Default Methods with By Locator")
   class DefaultMethodsWithByLocator {

      @Test
      @DisplayName("selectOptions delegates correctly")
      void selectOptionsCorrectlyDelegates() {
         // Given - setup in @BeforeEach

         // When
         service.selectOptions(locator, SAMPLE_VALUES);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastValues).containsExactly(SAMPLE_VALUES);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastStrategy).isNull();
      }

      @Test
      @DisplayName("selectOption delegates correctly")
      void selectOptionCorrectlyDelegates() {
         // Given - setup in @BeforeEach

         // When
         service.selectOption(locator, SINGLE_VALUE);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastValues).containsExactly(SINGLE_VALUE);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastStrategy).isNull();
      }

      @Test
      @DisplayName("selectOptions with strategy delegates correctly")
      void selectOptionsWithStrategyDelegates() {
         // Given
         service.returnOptions = EXPECTED_OPTIONS;

         // When
         var result = service.selectOptions(locator, strategy);

         // Then
         assertThat(result).isEqualTo(EXPECTED_OPTIONS);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastStrategy).isEqualTo(strategy);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastValues).isNull();
      }

      @Test
      @DisplayName("getAvailableOptions delegates correctly")
      void getAvailableOptionsDelegates() {
         // Given
         service.returnOptions = EXPECTED_OPTIONS;

         // When
         var result = service.getAvailableOptions(locator);

         // Then
         assertThat(result).isEqualTo(EXPECTED_OPTIONS);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
      }

      @Test
      @DisplayName("getSelectedOptions delegates correctly")
      void getSelectedOptionsDelegates() {
         // Given
         service.returnOptions = EXPECTED_OPTIONS;

         // When
         var result = service.getSelectedOptions(locator);

         // Then
         assertThat(result).isEqualTo(EXPECTED_OPTIONS);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
      }

      @Test
      @DisplayName("isOptionVisible delegates correctly")
      void isOptionVisibleDelegates() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isOptionVisible(locator, OPTION_VALUE);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastValues).containsExactly(OPTION_VALUE);
      }

      @Test
      @DisplayName("isOptionEnabled delegates correctly")
      void isOptionEnabledDelegates() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isOptionEnabled(locator, OPTION_VALUE);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastValues).containsExactly(OPTION_VALUE);
      }
   }
}