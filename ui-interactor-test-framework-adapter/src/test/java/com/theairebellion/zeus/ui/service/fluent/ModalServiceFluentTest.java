package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.selenium.ModalUiElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.mock.MockModalComponentType;
import com.theairebellion.zeus.ui.service.fluent.mock.MockModalService;
import com.theairebellion.zeus.ui.service.fluent.mock.MockModalUiElement;
import com.theairebellion.zeus.ui.storage.StorageKeysUi;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("ModalServiceFluent Tests")
class ModalServiceFluentTest extends BaseUnitUITest {

   private MockModalService mockService;
   private ModalServiceFluent<UiServiceFluent<?>> sut;
   private Storage storage;
   private Storage storageUI;
   private UiServiceFluent<?> uiServiceFluent;
   private SmartWebDriver driver;
   private ModalUiElement element;
   private MockedStatic<Allure> allureMock;

   @BeforeEach
   void setUp() {
      // Setup mocks
      mockService = new MockModalService();
      storage = mock(Storage.class);
      storageUI = mock(Storage.class);
      uiServiceFluent = mock(UiServiceFluent.class);
      driver = mock(SmartWebDriver.class);
      element = new MockModalUiElement(By.id("testModal"), MockModalComponentType.DUMMY);

      when(storage.sub(StorageKeysUi.UI)).thenReturn(storageUI);

      // Mock Allure steps
      allureMock = mockStatic(Allure.class);

      // Create the SUT
      sut = new ModalServiceFluent<>(uiServiceFluent, storage, mockService, driver);
   }

   @AfterEach
   void tearDown() {
      allureMock.close();
   }

   @Test
   @DisplayName("click should call modalService.clickButton and return uiServiceFluent")
   void clickShouldCallModalServiceAndReturnUiServiceFluent() {
      // When
      UiServiceFluent<?> result = sut.click(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      assertThat(mockService.lastButtonLocator).isEqualTo(By.id("testModal"));
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getTitle should call modalService.getTitle, store result and return uiServiceFluent")
   void getTitleShouldCallModalServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnTitle = "Modal Title";

      // When
      UiServiceFluent<?> result = sut.getTitle(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), "Modal Title");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getContentTitle should call modalService.getContentTitle, store result and return uiServiceFluent")
   void getContentTitleShouldCallModalServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnContentTitle = "Content Title";

      // When
      UiServiceFluent<?> result = sut.getContentTitle(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), "Content Title");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getBodyText should call modalService.getBodyText, store result and return uiServiceFluent")
   void getBodyTextShouldCallModalServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      mockService.returnBodyText = "Modal body text";

      // When
      UiServiceFluent<?> result = sut.getBodyText(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), "Modal body text");
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("close should call modalService.close and return uiServiceFluent")
   void closeShouldCallModalServiceAndReturnUiServiceFluent() {
      // When
      UiServiceFluent<?> result = sut.close(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      assertThat(result).isSameAs(uiServiceFluent);
   }
}