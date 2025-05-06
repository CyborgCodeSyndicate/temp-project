package com.theairebellion.zeus.ui.components.modal;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.modal.mock.MockModalComponentType;
import com.theairebellion.zeus.ui.components.modal.mock.MockModalService;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@DisplayName("ModalService Interface Default Methods")
class ModalServiceTest extends BaseUnitUITest {

   private MockModalService service;
   private SmartWebElement container;
   private By locator;

   @BeforeEach
   void setUp() {
      service = new MockModalService();
      WebElement webElement = mock(WebElement.class);
      WebDriver driver = mock(WebDriver.class);
      container = new MockSmartWebElement(webElement, driver);
      locator = By.id("testModal");
   }

   @Nested
   @DisplayName("Default Methods Tests")
   class DefaultMethodsTests {

      @Test
      @DisplayName("isOpened delegates correctly")
      void isOpenedDelegates() {
         // Given
         service.reset();
         service.returnOpened = true;

         // When
         var result = service.isOpened();

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      }

      @Test
      @DisplayName("clickButton with container and text delegates correctly")
      void clickButtonWithContainerAndText() {
         // Given
         service.reset();
         var buttonText = "OK";

         // When
         service.clickButton(container, buttonText);

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isEqualTo(buttonText);
      }

      @Test
      @DisplayName("clickButton with text only delegates correctly")
      void clickButtonWithTextOnly() {
         // Given
         service.reset();
         var buttonText = "Proceed";

         // When
         service.clickButton(buttonText);

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
         assertThat(service.lastButtonText).isEqualTo(buttonText);
      }

      @Test
      @DisplayName("clickButton with locator delegates correctly")
      void clickButtonWithLocator() {
         // Given
         service.reset();

         // When
         service.clickButton(locator);

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
         assertThat(service.lastButtonLocator).isEqualTo(locator);
      }

      @Test
      @DisplayName("getTitle delegates correctly")
      void getTitleDelegates() {
         // Given
         service.reset();
         var expectedTitle = "Modal Title Here";
         service.returnTitle = expectedTitle;

         // When
         var result = service.getTitle();

         // Then
         assertThat(result).isEqualTo(expectedTitle);
         assertThat(service.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      }

      @Test
      @DisplayName("getBodyText delegates correctly")
      void getBodyTextDelegates() {
         // Given
         service.reset();
         var expectedBody = "Body content sample";
         service.returnBodyText = expectedBody;

         // When
         var result = service.getBodyText();

         // Then
         assertThat(result).isEqualTo(expectedBody);
         assertThat(service.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      }

      @Test
      @DisplayName("getContentTitle delegates correctly")
      void getContentTitleDelegates() {
         // Given
         service.reset();
         var expectedTitle = "Content Title Sample";
         service.returnContentTitle = expectedTitle;

         // When
         var result = service.getContentTitle();

         // Then
         assertThat(result).isEqualTo(expectedTitle);
         assertThat(service.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      }

      @Test
      @DisplayName("close delegates correctly")
      void closeDelegates() {
         // Given
         service.reset();

         // When
         service.close();

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockModalComponentType.DUMMY);
      }
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
         uiConfigHolderMock = mockStatic(UiConfigHolder.class);
         reflectionUtilMock = mockStatic(ReflectionUtil.class);

         uiConfigHolderMock.when(UiConfigHolder::getUiConfig)
               .thenReturn(uiConfigMock);
         when(uiConfigMock.modalDefaultType()).thenReturn("TEST_TYPE");
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
      @DisplayName("getDefaultType returns component type when found")
      void getDefaultTypeSuccess() throws Exception {
         // Given
         var mockType = MockModalComponentType.DUMMY;
         reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(ModalComponentType.class),
                     eq("TEST_TYPE"),
                     eq("com.test.package")))
               .thenReturn(mockType);

         // When - access the private method using reflection
         var getDefaultTypeMethod = ModalService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         var result = (ModalComponentType) getDefaultTypeMethod.invoke(null);

         // Then
         assertThat(result).isEqualTo(mockType);
      }

      @Test
      @DisplayName("getDefaultType returns null when exception occurs")
      void getDefaultTypeWithException() throws Exception {
         // Given - ReflectionUtil throws exception when called
         reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(ModalComponentType.class),
                     anyString(),
                     anyString()))
               .thenThrow(new RuntimeException("Test exception"));

         // When - access private method via reflection
         var getDefaultTypeMethod = ModalService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         var result = (ModalComponentType) getDefaultTypeMethod.invoke(null);

         // Then - verify null is returned when exception occurs
         assertThat(result).isNull();
      }
   }
}