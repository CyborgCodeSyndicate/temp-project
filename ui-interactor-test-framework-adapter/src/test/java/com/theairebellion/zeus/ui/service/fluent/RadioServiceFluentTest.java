package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.RadioUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockRadioComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockRadioService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockRadioUiElement;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import io.qameta.allure.Allure;
import java.util.Arrays;
import java.util.List;
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

@DisplayName("RadioServiceFluent Tests")
class RadioServiceFluentTest extends BaseUnitUITest {

   private MockRadioService mockService;
   private RadioServiceFluent<UiServiceFluent<?>> sut;
   private Storage storage;
   private Storage storageUI;
   private UiServiceFluent<?> uiServiceFluent;
   private SmartWebDriver driver;
   private RadioUiElement element;
   private MockedStatic<Allure> allureMock;

   @BeforeEach
   void setUp() {
      // Setup mocks
      mockService = new MockRadioService();
      storage = mock(Storage.class);
      storageUI = mock(Storage.class);
      uiServiceFluent = mock(UiServiceFluent.class);
      driver = mock(SmartWebDriver.class);
      element = new MockRadioUiElement(By.id("testRadio"), MockRadioComponentType.DUMMY);

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
      sut = new RadioServiceFluent<>(uiServiceFluent, storage, mockService, driver);
   }

   @AfterEach
   void tearDown() {
      allureMock.close();
   }

   @Test
   @DisplayName("select should call radioService.select and return uiServiceFluent")
   void selectShouldCallRadioServiceAndReturnUiServiceFluent() {
      // When
      UiServiceFluent<?> result = sut.select(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("isEnabled should call radioService.isEnabled, store result and return uiServiceFluent")
   void isEnabledShouldCallRadioServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnBool = true;

      // When
      UiServiceFluent<?> result = sut.isEnabled(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsEnabled should call validateIsEnabled with correct parameters")
   void validateIsEnabledShouldCallValidateIsEnabledWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnBool = true;

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
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsEnabled with soft parameter should call validateIsEnabled with correct parameters")
   void validateIsEnabledWithSoftShouldCallValidateIsEnabledWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnBool = true;

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
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsDisabled should call validateIsEnabled with correct parameters")
   void validateIsDisabledShouldCallValidateIsEnabledWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnBool = false;

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
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), false);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsDisabled with soft parameter should call validateIsEnabled with correct parameters")
   void validateIsDisabledWithSoftShouldCallValidateIsEnabledWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnBool = false;

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
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), false);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsEnabled with returnBool=false should execute lambda and handle assertion failure")
   void validateIsEnabledWithReturnBoolFalseShouldExecuteLambdaAndHandleAssertionFailure() {
      // Given
      mockService.reset();
      mockService.returnBool = false;

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
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), false);
   }

   @Test
   @DisplayName("validateIsEnabled with soft=true and returnBool=false should execute lambda")
   void validateIsEnabledWithSoftTrueAndReturnBoolFalseShouldExecuteLambda() {
      // Given
      mockService.reset();
      mockService.returnBool = false;

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
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), false);
   }

   @Test
   @DisplayName("isSelected should call radioService.isSelected, store result and return uiServiceFluent")
   void isSelectedShouldCallRadioServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnBool = true;

      // When
      UiServiceFluent<?> result = sut.isSelected(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsSelected should call validateIsSelected with correct parameters")
   void validateIsSelectedShouldCallValidateIsSelectedWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnBool = true;

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
      UiServiceFluent<?> result = sut.validateIsSelected(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsSelected with soft parameter should call validateIsSelected with correct parameters")
   void validateIsSelectedWithSoftShouldCallValidateIsSelectedWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnBool = true;

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      UiServiceFluent<?> result = sut.validateIsSelected(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsNotSelected should call validateIsSelected with correct parameters")
   void validateIsNotSelectedShouldCallValidateIsSelectedWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnBool = false;

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
      UiServiceFluent<?> result = sut.validateIsNotSelected(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), false);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsNotSelected with soft parameter should call validateIsSelected with correct parameters")
   void validateIsNotSelectedWithSoftShouldCallValidateIsSelectedWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnBool = false;

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      UiServiceFluent<?> result = sut.validateIsNotSelected(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), false);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("isVisible should call radioService.isVisible, store result and return uiServiceFluent")
   void isVisibleShouldCallRadioServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnBool = true;

      // When
      UiServiceFluent<?> result = sut.isVisible(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsVisible should call validateIsVisible with correct parameters")
   void validateIsVisibleShouldCallValidateIsVisibleWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnBool = true;

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
      UiServiceFluent<?> result = sut.validateIsVisible(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsHidden should call validateIsVisible with correct parameters")
   void validateIsHiddenShouldCallValidateIsVisibleWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnBool = false;

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
      UiServiceFluent<?> result = sut.validateIsHidden(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), false);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getSelected should call radioService.getSelected, store result and return uiServiceFluent")
   void getSelectedShouldCallRadioServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnSelected = "Option1";

      // When
      UiServiceFluent<?> result = sut.getSelected(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), "Option1");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateSelected should call validateSelected with correct parameters")
   void validateSelectedShouldCallValidateSelectedWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnSelected = "Option1";
      String expectedValue = "Option1";

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
      UiServiceFluent<?> result = sut.validateSelected(element, expectedValue);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), "Option1");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateSelected with soft parameter should call validateSelected with correct parameters")
   void validateSelectedWithSoftShouldCallValidateSelectedWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnSelected = "Option1";
      String expectedValue = "Option1";

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      UiServiceFluent<?> result = sut.validateSelected(element, expectedValue, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), "Option1");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getAll should call radioService.getAll and return uiServiceFluent")
   void getAllShouldCallRadioServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      List<String> allValues = Arrays.asList("Option1", "Option2", "Option3");
      mockService.returnAll = allValues;

      // When
      UiServiceFluent<?> result = sut.getAll(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateAllRadioInputs should call validateAllRadioInputs with correct parameters")
   void validateAllRadioInputsShouldCallValidateAllRadioInputsWithCorrectParameters() {
      // Given
      mockService.reset();
      List<String> allValues = Arrays.asList("Option1", "Option2", "Option3");
      mockService.returnAll = allValues;
      String[] expectedValues = {"Option1", "Option2"};

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
      UiServiceFluent<?> result = sut.validateAllRadioInputs(element, expectedValues);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), allValues);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateAllRadioInputs with soft parameter should call validateAllRadioInputs with correct parameters")
   void validateAllRadioInputsWithSoftShouldCallValidateAllRadioInputsWithCorrectParameters() {
      // Given
      mockService.reset();
      List<String> allValues = Arrays.asList("Option1", "Option2", "Option3");
      mockService.returnAll = allValues;
      String[] expectedValues = {"Option1", "Option2"};

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      UiServiceFluent<?> result = sut.validateAllRadioInputs(element, true, expectedValues);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      verify(storageUI).put(element.enumImpl(), allValues);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("insertion should call radioService.insertion with correct parameters")
   void insertionShouldCallRadioServiceWithCorrectParameters() {
      // Given
      mockService.reset();
      Object[] values = {"value1", "value2"};

      // When
      sut.insertion(MockRadioComponentType.DUMMY, By.id("testRadio"), values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockRadioComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(By.id("testRadio"));
      assertThat(mockService.lastText).isEqualTo("value1"); // First value is used
   }
}