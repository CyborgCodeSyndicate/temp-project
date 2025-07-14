package com.theairebellion.zeus.ui.components.modal;

import com.theairebellion.zeus.ui.components.modal.mock.MockModalComponentType;
import com.theairebellion.zeus.ui.components.modal.mock.MockModalService;
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

@DisplayName("ModalService Interface Default Methods")
class ModalServiceTest extends BaseUnitUITest {

   private static final MockModalComponentType DEFAULT_TYPE = MockModalComponentType.DUMMY_MODAL;
   private static final String BUTTON_TEXT_OK = "OK";
   private static final String BUTTON_TEXT_SUBMIT = "Submit";
   private static final String MODAL_TITLE = "Modal Title";
   private static final String MODAL_BODY = "Body Content";
   private static final String MODAL_CONTENT_TITLE = "Content Title";
   private MockModalService service;
   private SmartWebElement container;
   private By locator;

   @BeforeEach
   void setUp() {
      // Given
      service = new MockModalService();
      container = MockSmartWebElement.createMock();
      locator = By.id("testModal");
      service.reset();
   }

   @Test
   void testGetDefaultTypeShouldReturnNullWhenExceptionIsThrown() throws Exception {
      UiConfig mockConfig = mock(UiConfig.class);
      when(mockConfig.modalDefaultType()).thenReturn("SomeType");
      when(mockConfig.projectPackage()).thenReturn("com.example");

      try (
            MockedStatic<UiConfigHolder> configMock = mockStatic(UiConfigHolder.class);
            MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)
      ) {
         configMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);

         reflectionMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
               ModalComponentType.class,
               "SomeType",
               "com.example"
         )).thenThrow(new RuntimeException("Simulated failure"));

         // Use reflection to access the private static method
         Method method = ModalService.class.getDeclaredMethod("getDefaultType");
         method.setAccessible(true);

         ModalComponentType result = (ModalComponentType) method.invoke(null);

         assertNull(result);
      }
   }

   @Nested
   @DisplayName("Default Methods Tests")
   class DefaultMethodsTests {

      @Test
      @DisplayName("isOpened delegates correctly")
      void isOpenedDelegates() {
         // Given
         service.returnOpened = true;

         // When
         var result = service.isOpened();

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
      }

      @Test
      @DisplayName("clickButton with container and text delegates correctly")
      void clickButtonWithContainerAndText() {
         // Given - setup in @BeforeEach

         // When
         service.clickButton(container, BUTTON_TEXT_OK);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isEqualTo(BUTTON_TEXT_OK);
         assertThat(service.lastButtonLocator).isNull();
      }

      @Test
      @DisplayName("clickButton with text only delegates correctly")
      void clickButtonWithTextOnly() {
         // Given - setup in @BeforeEach

         // When
         service.clickButton(BUTTON_TEXT_SUBMIT);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastButtonText).isEqualTo(BUTTON_TEXT_SUBMIT);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastButtonLocator).isNull();
      }

      @Test
      @DisplayName("clickButton with locator delegates correctly")
      void clickButtonWithLocator() {
         // Given - setup in @BeforeEach

         // When
         service.clickButton(locator);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastButtonLocator).isEqualTo(locator);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastButtonText).isNull();
      }

      @Test
      @DisplayName("getTitle delegates correctly")
      void getTitleDelegates() {
         // Given
         service.returnTitle = MODAL_TITLE;

         // When
         var result = service.getTitle();

         // Then
         assertThat(result).isEqualTo(MODAL_TITLE);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
      }

      @Test
      @DisplayName("getBodyText delegates correctly")
      void getBodyTextDelegates() {
         // Given
         service.returnBodyText = MODAL_BODY;

         // When
         var result = service.getBodyText();

         // Then
         assertThat(result).isEqualTo(MODAL_BODY);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
      }

      @Test
      @DisplayName("getContentTitle delegates correctly")
      void getContentTitleDelegates() {
         // Given
         service.returnContentTitle = MODAL_CONTENT_TITLE;

         // When
         var result = service.getContentTitle();

         // Then
         assertThat(result).isEqualTo(MODAL_CONTENT_TITLE);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
      }

      @Test
      @DisplayName("close delegates correctly")
      void closeDelegates() {
         // Given - setup in @BeforeEach

         // When
         service.close();

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastButtonText).isNull();
         assertThat(service.lastButtonLocator).isNull();
      }
   }
}