package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.ToggleUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockToggleComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockToggleService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockToggleUiElement;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import io.qameta.allure.Allure;
import java.util.function.Consumer;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("ToggleServiceFluent Tests")
class ToggleServiceFluentTest extends BaseUnitUITest {

   private MockToggleService mockService;
   private ToggleServiceFluent<UiServiceFluent<?>> sut;
   private Storage storageUI;
   private UiServiceFluent<?> uiServiceFluent;
   private ToggleUiElement element;
   private MockedStatic<Allure> allureMock;

   @BeforeEach
   void setUp() {
      // Setup mocks
      mockService = new MockToggleService();
      Storage storage = mock(Storage.class);
      storageUI = mock(Storage.class);
      uiServiceFluent = mock(UiServiceFluent.class);
      SmartWebDriver driver = mock(SmartWebDriver.class);
      element = new MockToggleUiElement(By.id("testToggle"), MockToggleComponentType.DUMMY);

      doAnswer(invocation -> {
         Runnable runnable = invocation.getArgument(0);
         runnable.run(); // now your assertion runs
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(Runnable.class));

      doAnswer(invocation -> {
         Consumer<SoftAssertions> consumer = invocation.getArgument(0);
         SoftAssertions softly = new SoftAssertions();
         consumer.accept(softly); // run assertions
         softly.assertAll(); // to trigger any collected failures
         return uiServiceFluent;
      }).when(uiServiceFluent).validate(any(Consumer.class));

      when(storage.sub(StorageKeysUi.UI)).thenReturn(storageUI);

      // Mock Allure steps
      allureMock = mockStatic(Allure.class);

      // Create the SUT
      sut = new ToggleServiceFluent<>(uiServiceFluent, storage, mockService, driver);
   }

   @AfterEach
   void tearDown() {
      allureMock.close();
   }

   @Test
   @DisplayName("isEnabled should call toggleService.isEnabled and store result")
   void isEnabledShouldCallToggleServiceStoreResult() {
      // Given
      mockService.reset();
      mockService.returnBool = true;

      // When
      UiServiceFluent<?> result = sut.isEnabled(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(element.locator());
      verify(storageUI).put(element.enumImpl(), true);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsEnabled should call toggleService.isEnabled, assert and store result")
   void validateIsEnabledShouldCallToggleServiceAssertAndStoreResult() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = true;
      mockService.returnBool = shouldBeEnabled;

      // When
      UiServiceFluent<?> result = sut.validateIsEnabled(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(element.locator());
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsEnabled should call toggleService.validateIsEnabled, soft assert and store result")
   void validateIsEnabledWithSoftAssertionShouldCallToggleServiceSoftAssertAndStoreResult() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = true;
      mockService.returnBool = shouldBeEnabled;

      // When
      UiServiceFluent<?> result = sut.validateIsEnabled(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(element.locator());
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsDisabled should call toggleService.validateIsDisabled, assert and store result")
   void validateIsDisabledShouldCallToggleServiceAssertAndStoreResult() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = false;
      mockService.returnBool = shouldBeEnabled;

      // When
      UiServiceFluent<?> result = sut.validateIsDisabled(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(element.locator());
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsDisabled should call toggleService.validateIsDisabled, soft assert and store result")
   void validateIsDisabledWithSoftAssertionShouldCallToggleServiceSoftAssertAndStoreResult() {
      // Given
      mockService.reset();
      boolean shouldBeEnabled = false;
      mockService.returnBool = shouldBeEnabled;

      // When
      UiServiceFluent<?> result = sut.validateIsDisabled(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(element.locator());
      verify(storageUI).put(element.enumImpl(), shouldBeEnabled);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("activate should call toggleService.activate and return uiServiceFluent")
   void activateShouldCallToggleServiceAndReturnUiServiceFluent() {
      // When
      UiServiceFluent<?> result = sut.activate(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(element.locator());
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("deactivate should call toggleService.deactivate and return uiServiceFluent")
   void deactivateShouldCallToggleServiceAndReturnUiServiceFluent() {
      // When
      UiServiceFluent<?> result = sut.deactivate(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(element.locator());
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("isActivated should call toggleService.isActivated and store result")
   void isActivatedShouldCallToggleServiceAndStoreResult() {
      // Given
      mockService.reset();
      boolean shouldBeActivated = true;
      mockService.returnBool = shouldBeActivated;

      // When
      UiServiceFluent<?> result = sut.isActivated(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(element.locator());
      verify(storageUI).put(element.enumImpl(), shouldBeActivated);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsActivated should call toggleService.validateIsActivated, assert and store result")
   void validateIsActivatedShouldCallToggleServiceStoreResult() {
      // Given
      mockService.reset();
      boolean shouldBeActivated = true;
      mockService.returnBool = shouldBeActivated;

      // When
      UiServiceFluent<?> result = sut.validateIsActivated(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(element.locator());
      verify(storageUI).put(element.enumImpl(), shouldBeActivated);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsActivated should call toggleService.validateIsActivated, soft assert and store result")
   void validateIsActivatedWithSoftAssertionShouldCallToggleServiceStoreResult() {
      // Given
      mockService.reset();
      boolean shouldBeActivated = true;
      mockService.returnBool = shouldBeActivated;

      // When
      UiServiceFluent<?> result = sut.validateIsActivated(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(element.locator());
      verify(storageUI).put(element.enumImpl(), shouldBeActivated);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsDeactivated should call toggleService.validateIsDeactivated, assert and store result")
   void validateIsDeactivatedShouldCallToggleServiceStoreResult() {
      // Given
      mockService.reset();
      boolean shouldBeActivated = false;
      mockService.returnBool = shouldBeActivated;

      // When
      UiServiceFluent<?> result = sut.validateIsDeactivated(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(element.locator());
      verify(storageUI).put(element.enumImpl(), shouldBeActivated);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("validateIsDeactivated should call toggleService.validateIsDeactivated, soft assert and store result")
   void validateIsDeactivatedWithSoftAssertionShouldCallToggleServiceStoreResult() {
      // Given
      mockService.reset();
      boolean shouldBeActivated = false;
      mockService.returnBool = shouldBeActivated;

      // When
      UiServiceFluent<?> result = sut.validateIsDeactivated(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockToggleComponentType.DUMMY);
      assertThat(mockService.lastLocator).isEqualTo(element.locator());
      verify(storageUI).put(element.enumImpl(), shouldBeActivated);
      assertThat(result).isSameAs(uiServiceFluent);
   }
}