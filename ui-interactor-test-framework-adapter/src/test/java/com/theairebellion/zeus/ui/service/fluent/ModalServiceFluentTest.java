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

@DisplayName("ModalServiceFluent Tests")
class ModalServiceFluentTest extends BaseUnitUITest {

   private MockModalService mockService;
   private ModalServiceFluent<UiServiceFluent<?>> sut;
   private Storage storageUI;
   private UiServiceFluent<?> uiServiceFluent;
   private ModalUiElement element;
   private MockedStatic<Allure> allureMock;

   @BeforeEach
   void setUp() {
      // Setup mocks
      mockService = new MockModalService();
      Storage storage = mock(Storage.class);
      storageUI = mock(Storage.class);
      uiServiceFluent = mock(UiServiceFluent.class);
      SmartWebDriver driver = mock(SmartWebDriver.class);
      element = new MockModalUiElement(By.id("testModal"), MockModalComponentType.DUMMY);

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
      sut = new ModalServiceFluent<>(uiServiceFluent, storage, mockService, driver);
   }

   @AfterEach
   void tearDown() {
      allureMock.close();
   }

   @Test
   @DisplayName("should call modalService.isOpened, store result and return uiServiceFluent")
   void isOpenedShouldCallModalServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      boolean shouldBeOpened = true;
      mockService.returnOpened = shouldBeOpened;

      // When
      UiServiceFluent<?> result = sut.isOpened(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), shouldBeOpened);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("should call modalService.validateIsOpened, assert result and return uiServiceFluent")
   void validateIsOpenedShouldCallModalServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      boolean shouldBeOpened = true;
      mockService.returnOpened = shouldBeOpened;

      // When
      UiServiceFluent<?> result = sut.validateIsOpened(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), shouldBeOpened);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("should call modalService.validateIsOpened, soft assert result and return uiServiceFluent")
   void validateIsOpenedWithSoftAssertionShouldCallModalServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      boolean shouldBeOpened = true;
      mockService.returnOpened = shouldBeOpened;

      // When
      UiServiceFluent<?> result = sut.validateIsOpened(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), shouldBeOpened);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("should call modalService.validateIsClosed, assert result and return uiServiceFluent")
   void validateIsClosedShouldCallModalServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      boolean shouldBeOpened = false;
      mockService.returnOpened = shouldBeOpened;

      // When
      UiServiceFluent<?> result = sut.validateIsClosed(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), shouldBeOpened);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("should call modalService.validateIsClosed, soft assert result and return uiServiceFluent")
   void validateIsClosedWithSoftAssertionShouldCallModalServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      boolean shouldBeOpened = false;
      mockService.returnOpened = shouldBeOpened;

      // When
      UiServiceFluent<?> result = sut.validateIsClosed(element, true);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), shouldBeOpened);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("click should call modalService.clickButton and return uiServiceFluent")
   void clickShouldCallModalServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();

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
   @DisplayName("should call modalService.validateTitle, assert result and return uiServiceFluent")
   void validateTitleShouldCallModalServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      String title = "TITLE";
      mockService.returnTitle = title;

      // When
      UiServiceFluent<?> result = sut.validateTitle(element, title);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), title);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("should call modalService.validateTitle, soft assert result and return uiServiceFluent")
   void validateTitleWithSoftAssertionShouldCallModalServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      String title = "TITLE";
      mockService.returnTitle = title;

      // When
      UiServiceFluent<?> result = sut.validateTitle(element, true, title);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), title);
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
   @DisplayName("should call modalService.validateContentTitle, assert result and return uiServiceFluent")
   void validateContentTitleShouldCallModalServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      String contentTitle = "CONTENT_TITLE";
      mockService.returnContentTitle = contentTitle;

      // When
      UiServiceFluent<?> result = sut.validateContentTitle(element, contentTitle);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), contentTitle);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("should call modalService.validateContentTitle, soft assert result and return uiServiceFluent")
   void validateContentTitleWithSoftAssertionShouldCallModalServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      String contentTitle = "CONTENT_TITLE";
      mockService.returnContentTitle = contentTitle;

      // When
      UiServiceFluent<?> result = sut.validateContentTitle(element, true, contentTitle);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), contentTitle);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("getBodyText should call modalService.getBodyText, store result and return uiServiceFluent")
   void getBodyTextShouldCallModalServiceStoreResultAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      String bodyText = "Modal Body Text";
      mockService.returnBodyText = bodyText;

      // When
      UiServiceFluent<?> result = sut.getBodyText(element);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), bodyText);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("should call modalService.validateBodyText, assert result and return uiServiceFluent")
   void validateBodyTextShouldCallModalServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      String bodyText = "Modal Body Text";
      mockService.returnBodyText = bodyText;

      // When
      UiServiceFluent<?> result = sut.validateBodyText(element, bodyText);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), bodyText);
      assertThat(result).isSameAs(uiServiceFluent);
   }

   @Test
   @DisplayName("should call modalService.validateBodyText, soft assert result and return uiServiceFluent")
   void validateBodyTextWithSoftAssertionShouldCallModalServiceAndReturnUiServiceFluent() {
      // Given
      mockService.reset();
      String bodyText = "Modal Body Text";
      mockService.returnBodyText = bodyText;

      // When
      UiServiceFluent<?> result = sut.validateBodyText(element, true, bodyText);

      // Then
      assertThat(mockService.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      verify(storageUI).put(element.enumImpl(), bodyText);
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