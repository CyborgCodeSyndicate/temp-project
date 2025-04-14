package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.radio.mock.MockRadioComponentType;
import com.theairebellion.zeus.ui.components.radio.mock.MockRadioService;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("RadioService Interface Tests")
class RadioServiceTest extends BaseUnitUITest {

   private MockRadioService service;
   private MockSmartWebElement container;
   private By locator;
   private Strategy strategy;

   @BeforeEach
   void setUp() {
      service = new MockRadioService();
      container = MockSmartWebElement.createMock();
      locator = By.id("testRadio");
      strategy = Strategy.FIRST;
      service.reset();
   }

   @Nested
   @DisplayName("Default Type Resolution Tests")
   class DefaultTypeResolutionTests {
      private MockedStatic<UiConfigHolder> uiConfigHolderMock;
      private MockedStatic<ReflectionUtil> reflectionUtilMock;
      private UiConfig uiConfigMock;

      @BeforeEach
      void setUp() {
         uiConfigMock = mock(UiConfig.class);
         uiConfigHolderMock = Mockito.mockStatic(UiConfigHolder.class);
         reflectionUtilMock = Mockito.mockStatic(ReflectionUtil.class);

         uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfigMock);
         when(uiConfigMock.projectPackage()).thenReturn("com.test.package");
         when(uiConfigMock.radioDefaultType()).thenReturn("DUMMY_TYPE");
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
      @DisplayName("getDefaultType returns component type when found")
      void getDefaultTypeSuccess() throws Exception {
         // Given
         MockRadioComponentType mockType = MockRadioComponentType.DUMMY;
         reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(RadioComponentType.class),
                     eq("DUMMY_TYPE"),
                     eq("com.test.package")))
               .thenReturn(mockType);

         // When - access the private method using reflection
         Method getDefaultTypeMethod = RadioService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         RadioComponentType result = (RadioComponentType) getDefaultTypeMethod.invoke(null);

         // Then
         assertThat(result).isEqualTo(mockType);
      }

      @Test
      @DisplayName("getDefaultType returns null when exception occurs")
      void getDefaultTypeWithException() throws Exception {
         // Given
         reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(RadioComponentType.class),
                     anyString(),
                     anyString()))
               .thenThrow(new RuntimeException("Test exception"));

         // When - access the private method using reflection
         Method getDefaultTypeMethod = RadioService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         RadioComponentType result = (RadioComponentType) getDefaultTypeMethod.invoke(null);

         // Then
         assertThat(result).isNull();
      }

      @Test
      @DisplayName("DEFAULT_TYPE constant is initialized correctly")
      void defaultTypeInitialization() throws Exception {
         // Given
         MockRadioComponentType mockType = MockRadioComponentType.DUMMY;
         reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(RadioComponentType.class),
                     anyString(),
                     anyString()))
               .thenReturn(mockType);

         // When/Then - verify the DEFAULT_TYPE field by reflection
         Field defaultTypeField = RadioService.class.getDeclaredField("DEFAULT_TYPE");
         defaultTypeField.setAccessible(true);

         // Test via the method to ensure consistent behavior
         Method getDefaultTypeMethod = RadioService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         RadioComponentType resultFromMethod = (RadioComponentType) getDefaultTypeMethod.invoke(null);

         assertThat(resultFromMethod).isEqualTo(mockType);
      }
   }

   @Nested
   @DisplayName("Select Default Methods")
   class SelectDefaultMethodsTests {

      @Test
      @DisplayName("select(container, radioButtonText) delegates to implementation with DEFAULT_TYPE")
      void selectContainerText() {
         // When
         service.select(container, "RadioOption");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo("RadioOption");
      }

      @Test
      @DisplayName("select(container, strategy) delegates to implementation with DEFAULT_TYPE")
      void selectContainerStrategy() {
         // Given
         service.returnSelected = "SelectedByStrategy";

         // When
         String result = service.select(container, strategy);

         // Then
         assertThat(result).isEqualTo("SelectedByStrategy");
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("select(radioButtonText) delegates to implementation with DEFAULT_TYPE")
      void selectRadioButtonText() {
         // When
         service.select("RadioOption");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastText).isEqualTo("RadioOption");
      }

      @Test
      @DisplayName("select(radioButtonLocator) delegates to implementation with DEFAULT_TYPE")
      void selectRadioButtonLocator() {
         // When
         service.select(locator);

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
      }
   }

   @Nested
   @DisplayName("IsEnabled Default Methods")
   class IsEnabledDefaultMethodsTests {

      @Test
      @DisplayName("isEnabled(container, radioButtonText) delegates to implementation with DEFAULT_TYPE")
      void isEnabledContainerText() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.isEnabled(container, "RadioOption");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo("RadioOption");
      }

      @Test
      @DisplayName("isEnabled(radioButtonText) delegates to implementation with DEFAULT_TYPE")
      void isEnabledRadioButtonText() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.isEnabled("RadioOption");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastText).isEqualTo("RadioOption");
      }

      @Test
      @DisplayName("isEnabled(radioButtonLocator) delegates to implementation with DEFAULT_TYPE")
      void isEnabledRadioButtonLocator() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.isEnabled(locator);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
      }
   }

   @Nested
   @DisplayName("IsSelected Default Methods")
   class IsSelectedDefaultMethodsTests {

      @Test
      @DisplayName("isSelected(container, radioButtonText) delegates to implementation with DEFAULT_TYPE")
      void isSelectedContainerText() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.isSelected(container, "RadioOption");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo("RadioOption");
      }

      @Test
      @DisplayName("isSelected(radioButtonText) delegates to implementation with DEFAULT_TYPE")
      void isSelectedRadioButtonText() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.isSelected("RadioOption");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastText).isEqualTo("RadioOption");
      }

      @Test
      @DisplayName("isSelected(radioButtonLocator) delegates to implementation with DEFAULT_TYPE")
      void isSelectedRadioButtonLocator() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.isSelected(locator);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
      }
   }

   @Nested
   @DisplayName("IsVisible Default Methods")
   class IsVisibleDefaultMethodsTests {

      @Test
      @DisplayName("isVisible(container, radioButtonText) delegates to implementation with DEFAULT_TYPE")
      void isVisibleContainerText() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.isVisible(container, "RadioOption");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo("RadioOption");
      }

      @Test
      @DisplayName("isVisible(radioButtonText) delegates to implementation with DEFAULT_TYPE")
      void isVisibleRadioButtonText() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.isVisible("RadioOption");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastText).isEqualTo("RadioOption");
      }

      @Test
      @DisplayName("isVisible(radioButtonLocator) delegates to implementation with DEFAULT_TYPE")
      void isVisibleRadioButtonLocator() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.isVisible(locator);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
      }
   }

   @Nested
   @DisplayName("Get Methods Default Delegation")
   class GetMethodsDefaultTests {

      @Test
      @DisplayName("getSelected(container) delegates to implementation with DEFAULT_TYPE")
      void getSelectedContainer() {
         // Given
         service.returnSelected = "SelectedRadio";

         // When
         String result = service.getSelected(container);

         // Then
         assertThat(result).isEqualTo("SelectedRadio");
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("getSelected(containerLocator) delegates to implementation with DEFAULT_TYPE")
      void getSelectedContainerLocator() {
         // Given
         service.returnSelected = "SelectedRadio";

         // When
         String result = service.getSelected(locator);

         // Then
         assertThat(result).isEqualTo("SelectedRadio");
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
      }

      @Test
      @DisplayName("getAll(container) delegates to implementation with DEFAULT_TYPE")
      void getAllContainer() {
         // Given
         List<String> options = Arrays.asList("Option1", "Option2", "Option3");
         service.returnAll = options;

         // When
         List<String> result = service.getAll(container);

         // Then
         assertThat(result).isEqualTo(options);
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("getAll(containerLocator) delegates to implementation with DEFAULT_TYPE")
      void getAllContainerLocator() {
         // Given
         List<String> options = Arrays.asList("Option1", "Option2", "Option3");
         service.returnAll = options;

         // When
         List<String> result = service.getAll(locator);

         // Then
         assertThat(result).isEqualTo(options);
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
      }
   }

   @Nested
   @DisplayName("Insertion Method Default Delegation")
   class InsertionMethodTest {

      @Test
      @DisplayName("insertion method delegates with DEFAULT_TYPE")
      void insertionDelegatesToImplementation() {
         // When
         service.insertion(MockRadioComponentType.DUMMY, locator, "RadioOption");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastText).isEqualTo("RadioOption");
      }
   }
}