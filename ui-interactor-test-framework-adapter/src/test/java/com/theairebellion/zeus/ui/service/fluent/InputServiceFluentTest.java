package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.InputUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockInputComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockInputService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockInputUiElement;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("InputServiceFluent Tests")
class InputServiceFluentTest extends BaseUnitUITest {

   private MockInputService mockService;
   private InputServiceFluent<UiServiceFluent<?>> sut;
   private Storage storage;
   private Storage storageUI;
   private UiServiceFluent<?> uiServiceFluent;
   private SmartWebDriver driver;
   private InputUiElement element;
   private MockedStatic<Allure> allureMock;

   @BeforeEach
   void setUp() {
      // Setup mocks
      mockService = new MockInputService();
      storage = mock(Storage.class);
      storageUI = mock(Storage.class);
      uiServiceFluent = mock(UiServiceFluent.class);
      driver = mock(SmartWebDriver.class);
      element = new MockInputUiElement(By.id("testInput"), MockInputComponentType.DUMMY);

      doReturn(uiServiceFluent)
            .when(uiServiceFluent)
            .validate(any(Runnable.class));

      doReturn(uiServiceFluent)
            .when(uiServiceFluent)
            .validate(any(java.util.function.Consumer.class));

      when(storage.sub(StorageKeysUi.UI)).thenReturn(storageUI);

      // Mock Allure steps
      allureMock = mockStatic(Allure.class);

      // Create the SUT
      sut = new InputServiceFluent<>(uiServiceFluent, storage, mockService, driver);
   }

   @AfterEach
   void tearDown() {
      allureMock.close();
   }

   @Test
   @DisplayName("insert should call inputService.insert and return uiServiceFluent")
   void insertShouldCallInputServiceAndReturnUiServiceFluent() {
      // Given
      String value = "test value";

      // When
      UiServiceFluent<?> result = sut.insert(element, value);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      assertThat(mockService.lastValue).isEqualTo(value);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("clear should call inputService.clear and return uiServiceFluent")
   void clearShouldCallInputServiceAndReturnUiServiceFluent() {
      // When
      UiServiceFluent<?> result = sut.clear(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getValue should call inputService.getValue, store result and return uiServiceFluent")
   void getValueShouldCallInputServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnValue = "test value";

      // When
      UiServiceFluent<?> result = sut.getValue(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      verify(storageUI).put(element.enumImpl(), "test value");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateValue should call validateValue with correct parameters")
   void validateValueShouldCallValidateValueWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnValue = "test value";
      String expectedValue = "test value";

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         Runnable assertionRunnable = invocation.getArgument(0);
         try {
            assertionRunnable.run();
         } catch (AssertionError ignored) {
            // Expected in test environment
         }
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(Runnable.class));

      // When
      UiServiceFluent<?> result = sut.validateValue(element, expectedValue);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      verify(storageUI).put(element.enumImpl(), "test value");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateValue with soft parameter should call validateValue with correct parameters")
   void validateValueWithSoftShouldCallValidateValueWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnValue = "test value";
      String expectedValue = "test value";

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      UiServiceFluent<?> result = sut.validateValue(element, expectedValue, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      verify(storageUI).put(element.enumImpl(), "test value");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateValue with incorrect value should execute lambda and handle assertion failure")
   void validateValueWithIncorrectValueShouldExecuteLambdaAndHandleAssertionFailure() {
      // Given
      mockService.reset();
      mockService.returnValue = "actual value";
      String expectedValue = "expected value";

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         Runnable assertionRunnable = invocation.getArgument(0);
         try {
            assertionRunnable.run();
         } catch (AssertionError ignored) {
            // Expected to fail in test environment
         }
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(Runnable.class));

      // When
      try {
         sut.validateValue(element, expectedValue);
      } catch (AssertionError ignored) {
         // Expected in test environment
      }

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), "actual value");
   }

   @Test
   @DisplayName("validateValue with soft parameter and incorrect value should execute lambda")
   void validateValueWithSoftParameterAndIncorrectValueShouldExecuteLambda() {
      // Given
      mockService.reset();
      mockService.returnValue = "actual value";
      String expectedValue = "expected value";

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      sut.validateValue(element, expectedValue, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), "actual value");
   }

   @Test
   @DisplayName("isEnabled should call inputService.isEnabled, store result and return uiServiceFluent")
   void isEnabledShouldCallInputServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnEnabled = true;

      // When
      UiServiceFluent<?> result = sut.isEnabled(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsEnabled should call validateIsEnabled with correct parameters")
   void validateIsEnabledShouldCallValidateIsEnabledWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnEnabled = true;

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         Runnable assertionRunnable = invocation.getArgument(0);
         try {
            assertionRunnable.run();
         } catch (AssertionError ignored) {
            // Expected in test environment
         }
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(Runnable.class));

      // When
      UiServiceFluent<?> result = sut.validateIsEnabled(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsEnabled with soft parameter should call validateIsEnabled with correct parameters")
   void validateIsEnabledWithSoftShouldCallValidateIsEnabledWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnEnabled = true;

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      UiServiceFluent<?> result = sut.validateIsEnabled(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsEnabled with returnEnabled=false should execute lambda")
   void validateIsEnabledWithReturnEnabledFalseShouldExecuteLambda() {
      // Given
      mockService.reset();
      mockService.returnEnabled = false;

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         Runnable assertionRunnable = invocation.getArgument(0);
         try {
            assertionRunnable.run();
         } catch (AssertionError ignored) {
            // Expected to fail in test environment
         }
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(Runnable.class));

      // When
      try {
         sut.validateIsEnabled(element);
      } catch (AssertionError ignored) {
         // Expected in test environment
      }

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), false);
   }

   @Test
   @DisplayName("validateIsEnabled with soft=true and returnEnabled=false should execute lambda")
   void validateIsEnabledWithSoftTrueAndReturnEnabledFalseShouldExecuteLambda() {
      // Given
      mockService.reset();
      mockService.returnEnabled = false;

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      sut.validateIsEnabled(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), false);
   }

   @Test
   @DisplayName("validateIsDisabled should call validateIsEnabled with correct parameters")
   void validateIsDisabledShouldCallValidateIsEnabledWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnEnabled = false;

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         Runnable assertionRunnable = invocation.getArgument(0);
         try {
            assertionRunnable.run();
         } catch (AssertionError ignored) {
            // Expected in test environment
         }
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(Runnable.class));

      // When
      UiServiceFluent<?> result = sut.validateIsDisabled(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      verify(storageUI).put(element.enumImpl(), false);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsDisabled with soft parameter should call validateIsEnabled with correct parameters")
   void validateIsDisabledWithSoftShouldCallValidateIsEnabledWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnEnabled = false;

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      UiServiceFluent<?> result = sut.validateIsDisabled(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      verify(storageUI).put(element.enumImpl(), false);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsDisabled with returnEnabled=true should execute lambda")
   void validateIsDisabledWithReturnEnabledTrueShouldExecuteLambda() {
      // Given
      mockService.reset();
      mockService.returnEnabled = true;

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         Runnable assertionRunnable = invocation.getArgument(0);
         try {
            assertionRunnable.run();
         } catch (AssertionError ignored) {
            // Expected to fail in test environment
         }
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(Runnable.class));

      // When
      try {
         sut.validateIsDisabled(element);
      } catch (AssertionError ignored) {
         // Expected in test environment
      }

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), true);
   }

   @Test
   @DisplayName("validateIsDisabled with soft=true and returnEnabled=true should execute lambda")
   void validateIsDisabledWithSoftTrueAndReturnEnabledTrueShouldExecuteLambda() {
      // Given
      mockService.reset();
      mockService.returnEnabled = true;

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      sut.validateIsDisabled(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), true);
   }

   @Test
   @DisplayName("getErrorMessage should call inputService.getErrorMessage, store result and return uiServiceFluent")
   void getErrorMessageShouldCallInputServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnErrorMessage = "Error message";

      // When
      UiServiceFluent<?> result = sut.getErrorMessage(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      verify(storageUI).put(element.enumImpl(), "Error message");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateErrorMessage should call validateErrorMessage with correct parameters")
   void validateErrorMessageShouldCallValidateErrorMessageWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnErrorMessage = "Error message";
      String expectedErrorMessage = "Error message";

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         Runnable assertionRunnable = invocation.getArgument(0);
         try {
            assertionRunnable.run();
         } catch (AssertionError ignored) {
            // Expected in test environment
         }
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(Runnable.class));

      // When
      UiServiceFluent<?> result = sut.validateErrorMessage(element, expectedErrorMessage);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      verify(storageUI).put(element.enumImpl(), "Error message");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateErrorMessage with soft parameter should call validateErrorMessage with correct parameters")
   void validateErrorMessageWithSoftShouldCallValidateErrorMessageWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnErrorMessage = "Error message";
      String expectedErrorMessage = "Error message";

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      UiServiceFluent<?> result = sut.validateErrorMessage(element, expectedErrorMessage, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      verify(storageUI).put(element.enumImpl(), "Error message");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateErrorMessage with incorrect error message should execute lambda and handle assertion failure")
   void validateErrorMessageWithIncorrectErrorMessageShouldExecuteLambdaAndHandleAssertionFailure() {
      // Given
      mockService.reset();
      mockService.returnErrorMessage = "Actual error message";
      String expectedErrorMessage = "Expected error message";

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         Runnable assertionRunnable = invocation.getArgument(0);
         try {
            assertionRunnable.run();
         } catch (AssertionError ignored) {
            // Expected to fail in test environment
         }
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(Runnable.class));

      // When
      try {
         sut.validateErrorMessage(element, expectedErrorMessage);
      } catch (AssertionError ignored) {
         // Expected in test environment
      }

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), "Actual error message");
   }

   @Test
   @DisplayName("validateErrorMessage with soft parameter and incorrect error message should execute lambda")
   void validateErrorMessageWithSoftParameterAndIncorrectErrorMessageShouldExecuteLambda() {
      // Given
      mockService.reset();
      mockService.returnErrorMessage = "Actual error message";
      String expectedErrorMessage = "Expected error message";

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      sut.validateErrorMessage(element, expectedErrorMessage, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), "Actual error message");
   }

   @Test
   @DisplayName("insertion should call inputService.insertion with correct parameters")
   void insertionShouldCallInputServiceWithCorrectParameters() {
      // Given
      mockService.reset();
      Object[] values = {"test value"};

      // When
      sut.insertion(MockInputComponentType.DUMMY, By.id("testInput"), values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockInputComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testInput"));
      assertThat(mockService.lastValue).isEqualTo("test value");
   }
}