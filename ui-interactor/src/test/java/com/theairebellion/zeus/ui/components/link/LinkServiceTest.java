package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.link.mock.MockLinkComponentType;
import com.theairebellion.zeus.ui.components.link.mock.MockLinkService;
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

@DisplayName("LinkService Interface Default Methods")
class LinkServiceTest extends BaseUnitUITest {

   private MockLinkService service;
   private SmartWebElement container;
   private By locator;

   @BeforeEach
   void setUp() {
      service = new MockLinkService();
      WebElement webElement = mock(WebElement.class);
      WebDriver driver = mock(WebDriver.class);
      container = new MockSmartWebElement(webElement, driver);
      locator = By.id("testLink");
   }

   @Nested
   @DisplayName("Double Click Default Methods Tests")
   class DoubleClickDefaultMethodsTests {

      @Test
      @DisplayName("doubleClick with container and text default delegates correctly")
      void doubleClickWithContainerAndTextDefault() {
         // Given
         service.reset();
         var linkText = "LinkTxt";

         // When
         service.doubleClick(container, linkText);

         // Then
         assertThat(service.lastLinkType).isEqualTo(MockLinkComponentType.DUMMY_LINK);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isEqualTo(linkText);
      }

      @Test
      @DisplayName("doubleClick with container default delegates correctly")
      void doubleClickWithContainerDefault() {
         // Given
         service.reset();

         // When
         service.doubleClick(container);

         // Then
         assertThat(service.lastLinkType).isEqualTo(MockLinkComponentType.DUMMY_LINK);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("doubleClick with text only default delegates correctly")
      void doubleClickWithTextOnlyDefault() {
         // Given
         service.reset();
         var linkText = "someLink";

         // When
         service.doubleClick(linkText);

         // Then
         assertThat(service.lastLinkType).isEqualTo(MockLinkComponentType.DUMMY_LINK);
         assertThat(service.lastButtonText).isEqualTo(linkText);
      }

      @Test
      @DisplayName("doubleClick with locator default delegates correctly")
      void doubleClickWithLocatorDefault() {
         // Given
         service.reset();

         // When
         service.doubleClick(locator);

         // Then
         assertThat(service.lastLinkType).isEqualTo(MockLinkComponentType.DUMMY_LINK);
         assertThat(service.lastLocator).isEqualTo(locator);
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
         when(uiConfigMock.linkDefaultType()).thenReturn("TEST_TYPE");
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
         var mockType = MockLinkComponentType.DUMMY_LINK;
         reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(LinkComponentType.class),
                     eq("TEST_TYPE"),
                     eq("com.test.package")))
               .thenReturn(mockType);

         // When - access the private method using reflection
         var getDefaultTypeMethod = LinkService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         var result = (LinkComponentType) getDefaultTypeMethod.invoke(null);

         // Then
         assertThat(result).isEqualTo(mockType);
      }

      @Test
      @DisplayName("getDefaultType returns null when exception occurs")
      void getDefaultTypeWithException() throws Exception {
         // Given - ReflectionUtil throws exception when called
         reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(LinkComponentType.class),
                     anyString(),
                     anyString()))
               .thenThrow(new RuntimeException("Test exception"));

         // When - access private method via reflection
         var getDefaultTypeMethod = LinkService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         var result = (LinkComponentType) getDefaultTypeMethod.invoke(null);

         // Then - verify null is returned when exception occurs
         assertThat(result).isNull();
      }
   }

   @Test
   @DisplayName("reset method clears all fields properly")
   void resetMethodClearsAllFields() {
      // Given
      service.lastLinkType = MockLinkComponentType.DUMMY_LINK;
      service.lastContainer = container;
      service.lastButtonText = "buttonText";
      service.lastLocator = locator;

      // When
      service.reset();

      // Then
      assertThat(service.lastLinkType).isNull();
      assertThat(service.lastContainer).isNull();
      assertThat(service.lastButtonText).isNull();
      assertThat(service.lastLocator).isNull();
   }
}