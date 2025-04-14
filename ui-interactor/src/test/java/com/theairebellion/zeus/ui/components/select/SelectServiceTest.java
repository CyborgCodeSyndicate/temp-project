package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.select.mock.MockSelectComponentType;
import com.theairebellion.zeus.ui.components.select.mock.MockSelectService;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@DisplayName("SelectService Interface Default Methods")
class SelectServiceTest extends BaseUnitUITest {

   private MockSelectService service;
   private SmartWebElement container;
   private By locator;
   private Strategy strategy;

   @BeforeEach
   void setUp() {
      service = new MockSelectService();
      WebElement webElement = mock(WebElement.class);
      WebDriver driver = mock(WebDriver.class);
      container = new MockSmartWebElement(webElement, driver);
      locator = By.id("testSelect");
      strategy = Strategy.RANDOM;
   }

   @Nested
   @DisplayName("Default Methods with SmartWebElement")
   class DefaultMethodsWithSmartWebElement {

      @Test
      @DisplayName("selectOptions delegates with correct parameters")
      void selectOptionsCorrectlyDelegates() {
         // When
         service.selectOptions(container, "val1", "val2");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastValues).containsExactly("val1", "val2");
      }

      @Test
      @DisplayName("selectOption delegates with correct parameters")
      void selectOptionCorrectlyDelegates() {
         // When
         service.selectOption(container, "singleVal");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastValues).containsExactly("singleVal");
      }

      @Test
      @DisplayName("selectOptions with strategy delegates correctly")
      void selectOptionsWithStrategyDelegates() {
         // Given
         var expectedResult = List.of("option1", "option2");
         service.returnOptions = expectedResult;

         // When
         var result = service.selectOptions(container, strategy);

         // Then
         assertThat(result).isEqualTo(expectedResult);
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("getAvailableOptions delegates correctly")
      void getAvailableOptionsDelegates() {
         // Given
         var expectedOptions = List.of("avail1", "avail2");
         service.returnOptions = expectedOptions;

         // When
         var result = service.getAvailableOptions(container);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("getSelectedOptions delegates correctly")
      void getSelectedOptionsDelegates() {
         // Given
         var expectedOptions = List.of("sel1", "sel2");
         service.returnOptions = expectedOptions;

         // When
         var result = service.getSelectedOptions(container);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("isOptionVisible delegates correctly")
      void isOptionVisibleDelegates() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isOptionVisible(container, "optionValue");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastValues).containsExactly("optionValue");
      }

      @Test
      @DisplayName("isOptionEnabled delegates correctly")
      void isOptionEnabledDelegates() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isOptionEnabled(container, "optionValue");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastValues).containsExactly("optionValue");
      }
   }

   @Nested
   @DisplayName("Default Methods with By Locator")
   class DefaultMethodsWithByLocator {

      @Test
      @DisplayName("selectOptions delegates with correct parameters")
      void selectOptionsCorrectlyDelegates() {
         // When
         service.selectOptions(locator, "val1", "val2");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastValues).containsExactly("val1", "val2");
      }

      @Test
      @DisplayName("selectOption delegates with correct parameters")
      void selectOptionCorrectlyDelegates() {
         // When
         service.selectOption(locator, "singleVal");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastValues).containsExactly("singleVal");
      }

      @Test
      @DisplayName("selectOptions with strategy delegates correctly")
      void selectOptionsWithStrategyDelegates() {
         // Given
         var expectedResult = List.of("option1", "option2");
         service.returnOptions = expectedResult;

         // When
         var result = service.selectOptions(locator, strategy);

         // Then
         assertThat(result).isEqualTo(expectedResult);
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("getAvailableOptions delegates correctly")
      void getAvailableOptionsDelegates() {
         // Given
         var expectedOptions = List.of("avail1", "avail2");
         service.returnOptions = expectedOptions;

         // When
         var result = service.getAvailableOptions(locator);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
      }

      @Test
      @DisplayName("getSelectedOptions delegates correctly")
      void getSelectedOptionsDelegates() {
         // Given
         var expectedOptions = List.of("sel1", "sel2");
         service.returnOptions = expectedOptions;

         // When
         var result = service.getSelectedOptions(locator);

         // Then
         assertThat(result).isEqualTo(expectedOptions);
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
      }

      @Test
      @DisplayName("isOptionVisible delegates correctly")
      void isOptionVisibleDelegates() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isOptionVisible(locator, "optionValue");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastValues).containsExactly("optionValue");
      }

      @Test
      @DisplayName("isOptionEnabled delegates correctly")
      void isOptionEnabledDelegates() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isOptionEnabled(locator, "optionValue");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastValues).containsExactly("optionValue");
      }
   }

   @Test
   @DisplayName("insertion method correctly delegates")
   void insertionMethodDelegates() {
      // Given
      service.reset();

      // When
      service.insertion(MockSelectComponentType.DUMMY, locator, "val1", "val2");

      // Then
      assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
      assertThat(service.lastLocator).isEqualTo(locator);
      assertThat(service.lastValues).containsExactly("val1", "val2");
   }

   @Test
   @DisplayName("insertion method handles non-string values")
   void insertionMethodHandlesNonStringValues() {
      // Given
      service.reset();

      // When
      service.insertion(MockSelectComponentType.DUMMY, locator, 42, true);

      // Then
      assertThat(service.lastComponentType).isEqualTo(MockSelectComponentType.DUMMY);
      assertThat(service.lastLocator).isEqualTo(locator);
      assertThat(service.lastValues).containsExactly("42", "true");
   }

   @Nested
   @DisplayName("Default Type Resolution Tests")
   class DefaultTypeResolutionTests {

      private MockedStatic<UiConfigHolder> uiConfigHolderMock;
      private MockedStatic<com.theairebellion.zeus.util.reflections.ReflectionUtil> reflectionUtilMock;
      private com.theairebellion.zeus.ui.config.UiConfig uiConfigMock;

      @BeforeEach
      void setUp() {
         uiConfigMock = mock(com.theairebellion.zeus.ui.config.UiConfig.class);
         uiConfigHolderMock = mockStatic(com.theairebellion.zeus.ui.config.UiConfigHolder.class);
         reflectionUtilMock = mockStatic(com.theairebellion.zeus.util.reflections.ReflectionUtil.class);

         uiConfigHolderMock.when(UiConfigHolder::getUiConfig)
               .thenReturn(uiConfigMock);
         when(uiConfigMock.selectDefaultType()).thenReturn("TEST_TYPE");
         when(uiConfigMock.projectPackage()).thenReturn("com.test.package");
      }

      @AfterEach
      void tearDown() {
         if (uiConfigHolderMock != null) {
            uiConfigHolderMock.close();
         }
         if (reflectionUtilMock != null) {
            reflectionUtilMock.close();
         }
      }

      @Test
      @DisplayName("getDefaultType returns null when exception occurs")
      void getDefaultTypeWithException() throws Exception {
         // Given - ReflectionUtil throws exception when called
         reflectionUtilMock.when(() -> com.theairebellion.zeus.util.reflections.ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(SelectComponentType.class),
                     anyString(),
                     anyString()))
               .thenThrow(new RuntimeException("Test exception"));

         // When - access private method via reflection
         java.lang.reflect.Method getDefaultTypeMethod = SelectService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         SelectComponentType result = (SelectComponentType) getDefaultTypeMethod.invoke(null);

         // Then - verify null is returned when exception occurs
         assertThat(result).isNull();
      }
   }
}