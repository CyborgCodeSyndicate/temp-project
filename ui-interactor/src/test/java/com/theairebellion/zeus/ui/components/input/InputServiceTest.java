package com.theairebellion.zeus.ui.components.input;

import com.theairebellion.zeus.ui.components.input.mock.MockInputComponentType;
import com.theairebellion.zeus.ui.components.input.mock.MockInputService;
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


@DisplayName("InputService Test")
class InputServiceTest extends BaseUnitUITest {

   private static final String SAMPLE_VALUE = "value";
   private static final String SAMPLE_LABEL = "label";
   private static final String SAMPLE_ERROR_MSG = "err";
   private MockInputService service;
   private SmartWebElement container;
   private By locator;

   @BeforeEach
   void setUp() {
      // Given
      service = new MockInputService();
      container = MockSmartWebElement.createMock();
      locator = By.id("testInput");
      service.reset();
   }

   @Test
   void testGetDefaultTypeShouldReturnNullWhenExceptionIsThrown() throws Exception {
      UiConfig mockConfig = mock(UiConfig.class);
      when(mockConfig.inputDefaultType()).thenReturn("SomeType");
      when(mockConfig.projectPackage()).thenReturn("com.example");

      try (
            MockedStatic<UiConfigHolder> configMock = mockStatic(UiConfigHolder.class);
            MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)
      ) {
         configMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);

         reflectionMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
               InputComponentType.class,
               "SomeType",
               "com.example"
         )).thenThrow(new RuntimeException("Simulated failure"));

         // Use reflection to access the private static method
         Method method = InputService.class.getDeclaredMethod("getDefaultType");
         method.setAccessible(true);

         InputComponentType result = (InputComponentType) method.invoke(null);

         assertNull(result);
      }
   }

   @Nested
   @DisplayName("Default Method Delegation Tests - Insert")
   class DefaultInsertDelegationTests {

      @Test
      @DisplayName("insert(container, value) delegates correctly")
      void testDefaultInsertWithContainer() {
         // Given - setup in @BeforeEach

         // When
         service.insert(container, SAMPLE_VALUE);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastValue).isEqualTo(SAMPLE_VALUE);
         assertThat(service.lastLabel).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("insert(container, label, value) delegates correctly")
      void testDefaultInsertWithContainerAndLabel() {
         // Given - setup in @BeforeEach

         // When
         service.insert(container, SAMPLE_LABEL, SAMPLE_VALUE);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastLabel).isEqualTo(SAMPLE_LABEL);
         assertThat(service.lastValue).isEqualTo(SAMPLE_VALUE);
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("insert(label, value) delegates correctly")
      void testDefaultInsertWithLabel() {
         // Given - setup in @BeforeEach

         // When
         service.insert(SAMPLE_LABEL, SAMPLE_VALUE);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastLabel).isEqualTo(SAMPLE_LABEL);
         assertThat(service.lastValue).isEqualTo(SAMPLE_VALUE);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("insert(locator, value) delegates correctly")
      void testDefaultInsertWithBy() {
         // Given - setup in @BeforeEach

         // When
         service.insert(locator, SAMPLE_VALUE);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastValue).isEqualTo(SAMPLE_VALUE);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLabel).isNull();
      }
   }

   @Nested
   @DisplayName("Default Method Delegation Tests - Clear")
   class DefaultClearDelegationTests {

      @Test
      @DisplayName("clear(container) delegates correctly")
      void testDefaultClearWithContainer() {
         // Given - setup in @BeforeEach

         // When
         service.clear(container);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastLabel).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("clear(container, label) delegates correctly")
      void testDefaultClearWithContainerAndLabel() {
         // Given - setup in @BeforeEach

         // When
         service.clear(container, SAMPLE_LABEL);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastLabel).isEqualTo(SAMPLE_LABEL);
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("clear(label) delegates correctly")
      void testDefaultClearWithLabel() {
         // Given - setup in @BeforeEach

         // When
         service.clear(SAMPLE_LABEL);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastLabel).isEqualTo(SAMPLE_LABEL);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("clear(locator) delegates correctly")
      void testDefaultClearWithBy() {
         // Given - setup in @BeforeEach

         // When
         service.clear(locator);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLabel).isNull();
      }
   }

   @Nested
   @DisplayName("Default Method Delegation Tests - GetValue")
   class DefaultGetValueDelegationTests {

      @Test
      @DisplayName("getValue(container) delegates correctly")
      void testDefaultGetValueContainer() {
         // Given
         service.returnValue = SAMPLE_VALUE;

         // When
         var val = service.getValue(container);

         // Then
         assertThat(val).isEqualTo(SAMPLE_VALUE);
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("getValue(container, label) delegates correctly")
      void testDefaultGetValueContainerLabel() {
         // Given
         service.returnValue = SAMPLE_VALUE;

         // When
         var val = service.getValue(container, SAMPLE_LABEL);

         // Then
         assertThat(val).isEqualTo(SAMPLE_VALUE);
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastLabel).isEqualTo(SAMPLE_LABEL);
      }

      @Test
      @DisplayName("getValue(label) delegates correctly")
      void testDefaultGetValueLabel() {
         // Given
         service.returnValue = SAMPLE_VALUE;

         // When
         var val = service.getValue(SAMPLE_LABEL);

         // Then
         assertThat(val).isEqualTo(SAMPLE_VALUE);
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastLabel).isEqualTo(SAMPLE_LABEL);
      }

      @Test
      @DisplayName("getValue(locator) delegates correctly")
      void testDefaultGetValueBy() {
         // Given
         service.returnValue = SAMPLE_VALUE;

         // When
         var val = service.getValue(locator);

         // Then
         assertThat(val).isEqualTo(SAMPLE_VALUE);
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastLocator).isEqualTo(locator);
      }
   }

   @Nested
   @DisplayName("Default Method Delegation Tests - IsEnabled")
   class DefaultIsEnabledDelegationTests {

      @Test
      @DisplayName("isEnabled(container) delegates correctly")
      void testDefaultIsEnabledContainer() {
         // Given
         service.returnEnabled = true;

         // When
         var enabled = service.isEnabled(container);

         // Then
         assertThat(enabled).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("isEnabled(container, label) delegates correctly")
      void testDefaultIsEnabledContainerLabel() {
         // Given
         service.returnEnabled = true;

         // When
         var enabled = service.isEnabled(container, SAMPLE_LABEL);

         // Then
         assertThat(enabled).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastLabel).isEqualTo(SAMPLE_LABEL);
      }

      @Test
      @DisplayName("isEnabled(label) delegates correctly")
      void testDefaultIsEnabledLabel() {
         // Given
         service.returnEnabled = true;

         // When
         var enabled = service.isEnabled(SAMPLE_LABEL);

         // Then
         assertThat(enabled).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastLabel).isEqualTo(SAMPLE_LABEL);
      }

      @Test
      @DisplayName("isEnabled(locator) delegates correctly")
      void testDefaultIsEnabledBy() {
         // Given
         service.returnEnabled = true;

         // When
         var enabled = service.isEnabled(locator);

         // Then
         assertThat(enabled).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastLocator).isEqualTo(locator);
      }
   }

   @Nested
   @DisplayName("Default Method Delegation Tests - GetErrorMessage")
   class DefaultGetErrorMessageDelegationTests {

      @Test
      @DisplayName("getErrorMessage(container) delegates correctly")
      void testDefaultGetErrorMessageContainer() {
         // Given
         service.returnErrorMessage = SAMPLE_ERROR_MSG;

         // When
         var msg = service.getErrorMessage(container);

         // Then
         assertThat(msg).isEqualTo(SAMPLE_ERROR_MSG);
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("getErrorMessage(container, label) delegates correctly")
      void testDefaultGetErrorMessageContainerLabel() {
         // Given
         service.returnErrorMessage = SAMPLE_ERROR_MSG;

         // When
         var msg = service.getErrorMessage(container, SAMPLE_LABEL);

         // Then
         assertThat(msg).isEqualTo(SAMPLE_ERROR_MSG);
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastLabel).isEqualTo(SAMPLE_LABEL);
      }

      @Test
      @DisplayName("getErrorMessage(label) delegates correctly")
      void testDefaultGetErrorMessageLabel() {
         // Given
         service.returnErrorMessage = SAMPLE_ERROR_MSG;

         // When
         var msg = service.getErrorMessage(SAMPLE_LABEL);

         // Then
         assertThat(msg).isEqualTo(SAMPLE_ERROR_MSG);
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastLabel).isEqualTo(SAMPLE_LABEL);
      }

      @Test
      @DisplayName("getErrorMessage(locator) delegates correctly")
      void testDefaultGetErrorMessageBy() {
         // Given
         service.returnErrorMessage = SAMPLE_ERROR_MSG;

         // When
         var msg = service.getErrorMessage(locator);

         // Then
         assertThat(msg).isEqualTo(SAMPLE_ERROR_MSG);
         assertThat(service.lastComponentTypeUsed).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.explicitComponentType).isEqualTo(MockInputComponentType.DUMMY_INPUT);
         assertThat(service.lastLocator).isEqualTo(locator);
      }
   }
}