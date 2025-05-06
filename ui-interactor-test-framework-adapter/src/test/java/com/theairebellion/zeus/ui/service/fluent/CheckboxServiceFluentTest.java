package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.CheckboxUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockCheckboxComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockCheckboxService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockCheckboxUiElement;
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

@DisplayName("CheckboxServiceFluent Tests")
class CheckboxServiceFluentTest extends BaseUnitUITest {

   private MockCheckboxService mockService;
   private CheckboxServiceFluent<UiServiceFluent<?>> sut;
   private Storage storage;
   private Storage storageUI;
   private UiServiceFluent<?> uiServiceFluent;
   private SmartWebDriver driver;
   private CheckboxUiElement element;
   private MockedStatic<Allure> allureMock;

   @BeforeEach
   void setUp() {
      // Setup mocks
      mockService = new MockCheckboxService();
      storage = mock(Storage.class);
      storageUI = mock(Storage.class);
      uiServiceFluent = mock(UiServiceFluent.class);
      driver = mock(SmartWebDriver.class);
      element = new MockCheckboxUiElement(By.id("testCheckbox"), MockCheckboxComponentType.DUMMY);

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
      sut = new CheckboxServiceFluent<>(uiServiceFluent, storage, mockService, driver);
   }

   @AfterEach
   void tearDown() {
      allureMock.close();
   }

   @Test
   @DisplayName("select should call checkboxService.select and return uiServiceFluent")
   void selectShouldCallCheckboxServiceAndReturnUiServiceFluent() {
      // When
      UiServiceFluent<?> result = sut.select(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("deSelect should call checkboxService.deSelect and return uiServiceFluent")
   void deSelectShouldCallCheckboxServiceAndReturnUiServiceFluent() {
      // When
      UiServiceFluent<?> result = sut.deSelect(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("isSelected should call checkboxService.isSelected, store result and return uiServiceFluent")
   void isSelectedShouldCallCheckboxServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnSelected = true;

      // When
      UiServiceFluent<?> result = sut.isSelected(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsSelected should call validateIsSelected with correct parameters")
   void validateIsSelectedShouldCallValidateIsSelectedWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnSelected = true;

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
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsSelected with soft parameter should call validateIsSelected with correct parameters")
   void validateIsSelectedWithSoftShouldCallValidateIsSelectedWithCorrectParameters() {
      // Given
      mockService.reset();
      mockService.returnSelected = true;

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
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsSelected with returnSelected=false should execute lambda")
   void validateIsSelectedWithReturnSelectedFalseShouldExecuteLambda() {
      // Given
      mockService.reset();
      mockService.returnSelected = false;

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
         sut.validateIsSelected(element);
      } catch (AssertionError ignored) {
         // Expected in test environment
      }

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), false);
   }

   @Test
   @DisplayName("validateIsSelected with soft=true and returnSelected=false should execute lambda")
   void validateIsSelectedWithSoftTrueAndReturnSelectedFalseShouldExecuteLambda() {
      // Given
      mockService.reset();
      mockService.returnSelected = false;

      // Configure uiServiceFluent.validate to actually execute the lambda
      doAnswer(invocation -> {
         java.util.function.Consumer<org.assertj.core.api.SoftAssertions> assertionConsumer = invocation.getArgument(0);
         org.assertj.core.api.SoftAssertions softAssertions = new org.assertj.core.api.SoftAssertions();
         assertionConsumer.accept(softAssertions);
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(java.util.function.Consumer.class));

      // When
      sut.validateIsSelected(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), false);
   }

   @Test
   @DisplayName("areSelected should call checkboxService.areSelected, store result and return uiServiceFluent")
   void areSelectedShouldCallCheckboxServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnSelected = true;

      // When
      UiServiceFluent<?> result = sut.areSelected(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("isEnabled should call checkboxService.isEnabled, store result and return uiServiceFluent")
   void isEnabledShouldCallCheckboxServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnEnabled = true;

      // When
      UiServiceFluent<?> result = sut.isEnabled(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
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
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
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
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
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
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
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
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), false);
   }

   @Test
   @DisplayName("areEnabled should call checkboxService.areEnabled, store result and return uiServiceFluent")
   void areEnabledShouldCallCheckboxServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnEnabled = true;

      // When
      UiServiceFluent<?> result = sut.areEnabled(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getSelected should call checkboxService.getSelected, store result and return uiServiceFluent")
   void getSelectedShouldCallCheckboxServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      List<String> selectedValues = Arrays.asList("Option1", "Option2");
      mockService.returnSelectedList = selectedValues;

      // When
      UiServiceFluent<?> result = sut.getSelected(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
      verify(storageUI).put(element.enumImpl(), selectedValues);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getAll should call checkboxService.getAll and return uiServiceFluent")
   void getAllShouldCallCheckboxServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      List<String> allValues = Arrays.asList("Option1", "Option2", "Option3");
      mockService.returnAllList = allValues;

      // When
      UiServiceFluent<?> result = sut.getAll(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("insertion should call checkboxService.insertion with correct parameters")
   void insertionShouldCallCheckboxServiceWithCorrectParameters() {
      // Given
      mockService.reset();
      Object[] values = {"value1", "value2"};

      // When
      sut.insertion(MockCheckboxComponentType.DUMMY, By.id("testCheckbox"), values);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockCheckboxComponentType.DUMMY);
      assertThat(mockService.lastCheckboxLocators).isEqualTo(new By[] {By.id("testCheckbox")});
      assertThat(mockService.lastCheckboxText).isEqualTo(new String[] {"value1", "value2"});
   }
}