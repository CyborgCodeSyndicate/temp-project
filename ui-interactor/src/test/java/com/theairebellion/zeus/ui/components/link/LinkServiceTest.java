package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.components.button.mock.MockButtonComponentType;
import com.theairebellion.zeus.ui.components.link.mock.MockLinkComponentType;
import com.theairebellion.zeus.ui.components.link.mock.MockLinkService;
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

@DisplayName("LinkService Interface Default Methods")
class LinkServiceTest extends BaseUnitUITest {

   private static final String LINK_TEXT = "ClickMeLink";
   private static final MockLinkComponentType LINK_DEFAULT_TYPE = MockLinkComponentType.DUMMY_LINK;
   private static final MockButtonComponentType BUTTON_DEFAULT_TYPE = MockButtonComponentType.DUMMY_BUTTON;
   private MockLinkService service;
   private SmartWebElement container;
   private By locator;

   @BeforeEach
   void setUp() {
      // Given
      service = new MockLinkService();
      container = MockSmartWebElement.createMock();
      locator = By.id("testLink");
      service.reset();
   }

   @Test
   void testGetDefaultTypeShouldReturnNullWhenExceptionIsThrown() throws Exception {
      UiConfig mockConfig = mock(UiConfig.class);
      when(mockConfig.linkDefaultType()).thenReturn("SomeType");
      when(mockConfig.projectPackage()).thenReturn("com.example");

      try (
            MockedStatic<UiConfigHolder> configMock = mockStatic(UiConfigHolder.class);
            MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)
      ) {
         configMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);

         reflectionMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
               LinkComponentType.class,
               "SomeType",
               "com.example"
         )).thenThrow(new RuntimeException("Simulated failure"));

         // Use reflection to access the private static method
         Method method = LinkService.class.getDeclaredMethod("getDefaultType");
         method.setAccessible(true);

         LinkComponentType result = (LinkComponentType) method.invoke(null);

         assertNull(result);
      }
   }

   @Nested
   @DisplayName("Double Click Default Methods Tests")
   class DoubleClickDefaultMethodsTests {

      @Test
      @DisplayName("doubleClick with container and text default delegates correctly")
      void doubleClickWithContainerAndTextDefault() {
         // Given

         // When
         service.doubleClick(container, LINK_TEXT);

         // Then
         assertThat(service.lastLinkType).isEqualTo(LINK_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(LINK_DEFAULT_TYPE);
         assertThat(service.lastComponentTypeUsed).isEqualTo(LINK_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isEqualTo(LINK_TEXT);
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("doubleClick with container default delegates correctly")
      void doubleClickWithContainerDefault() {
         // Given

         // When
         service.doubleClick(container);

         // Then
         assertThat(service.lastLinkType).isEqualTo(LINK_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(LINK_DEFAULT_TYPE);
         assertThat(service.lastComponentTypeUsed).isEqualTo(LINK_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("doubleClick with text only default delegates correctly")
      void doubleClickWithTextOnlyDefault() {
         // Given

         // When
         service.doubleClick(LINK_TEXT);

         // Then
         assertThat(service.lastLinkType).isEqualTo(LINK_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(LINK_DEFAULT_TYPE);
         assertThat(service.lastComponentTypeUsed).isEqualTo(LINK_DEFAULT_TYPE);
         assertThat(service.lastButtonText).isEqualTo(LINK_TEXT);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("doubleClick with locator default delegates correctly")
      void doubleClickWithLocatorDefault() {
         // Given

         // When
         service.doubleClick(locator);

         // Then
         assertThat(service.lastLinkType).isEqualTo(LINK_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(LINK_DEFAULT_TYPE);
         assertThat(service.lastComponentTypeUsed).isEqualTo(LINK_DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastButtonText).isNull();
      }
   }

   @Nested
   @DisplayName("Inherited Default Click Method Tests")
   class InheritedClickDefaultTests {

      @Test
      @DisplayName("click with container and text default delegates correctly")
      void clickWithContainerAndTextDefault() {
         // Given

         // When
         service.click(container, LINK_TEXT);

         // Then
         // Assert against BUTTON's default type for inherited methods
         assertThat(service.lastComponentTypeUsed).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isEqualTo(LINK_TEXT);
         assertThat(service.lastLocator).isNull();
         assertThat(service.lastLinkType).isNull();
      }

      @Test
      @DisplayName("click with container default delegates correctly")
      void clickWithContainerDefault() {
         // Given

         // When
         service.click(container);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isNull();
         assertThat(service.lastLocator).isNull();
         assertThat(service.lastLinkType).isNull();
      }

      @Test
      @DisplayName("click with text default delegates correctly")
      void clickWithTextDefault() {
         // Given

         // When
         service.click(LINK_TEXT);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastButtonText).isEqualTo(LINK_TEXT);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
         assertThat(service.lastLinkType).isNull();
      }

      @Test
      @DisplayName("click with locator default delegates correctly")
      void clickWithLocatorDefault() {
         // Given

         // When
         service.click(locator);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastButtonText).isNull();
         assertThat(service.lastLinkType).isNull();
      }
   }

   @Nested
   @DisplayName("Inherited Default IsEnabled Method Tests")
   class InheritedIsEnabledDefaultTests {

      @Test
      @DisplayName("isEnabled with container and text default delegates correctly")
      void isEnabledWithContainerAndTextDefault() {
         // Given
         service.returnEnabled = true;

         // When
         var result = service.isEnabled(container, LINK_TEXT);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isEqualTo(LINK_TEXT);
         assertThat(service.lastLocator).isNull();
         assertThat(service.lastLinkType).isNull();
      }

      @Test
      @DisplayName("isEnabled with container default delegates correctly")
      void isEnabledWithContainerDefault() {
         // Given
         service.returnEnabled = true;

         // When
         var result = service.isEnabled(container);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isNull();
         assertThat(service.lastLocator).isNull();
         assertThat(service.lastLinkType).isNull();
      }

      @Test
      @DisplayName("isEnabled with text default delegates correctly")
      void isEnabledWithTextDefault() {
         // Given
         service.returnEnabled = true;

         // When
         var result = service.isEnabled(LINK_TEXT);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastButtonText).isEqualTo(LINK_TEXT);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
         assertThat(service.lastLinkType).isNull();
      }

      @Test
      @DisplayName("isEnabled with locator default delegates correctly")
      void isEnabledWithLocatorDefault() {
         // Given
         service.returnEnabled = true;

         // When
         var result = service.isEnabled(locator);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastButtonText).isNull();
         assertThat(service.lastLinkType).isNull();
      }
   }

   @Nested
   @DisplayName("Inherited Default IsVisible Method Tests")
   class InheritedIsVisibleDefaultTests {

      @Test
      @DisplayName("isVisible with container and text default delegates correctly")
      void isVisibleWithContainerAndTextDefault() {
         // Given
         service.returnVisible = true;

         // When
         var result = service.isVisible(container, LINK_TEXT);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isEqualTo(LINK_TEXT);
         assertThat(service.lastLocator).isNull();
         assertThat(service.lastLinkType).isNull();
      }

      @Test
      @DisplayName("isVisible with container default delegates correctly")
      void isVisibleWithContainerDefault() {
         // Given
         service.returnVisible = true;

         // When
         var result = service.isVisible(container);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isNull();
         assertThat(service.lastLocator).isNull();
         assertThat(service.lastLinkType).isNull();
      }

      @Test
      @DisplayName("isVisible with text default delegates correctly")
      void isVisibleWithTextDefault() {
         // Given
         service.returnVisible = true;

         // When
         var result = service.isVisible(LINK_TEXT);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastButtonText).isEqualTo(LINK_TEXT);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
         assertThat(service.lastLinkType).isNull();
      }

      @Test
      @DisplayName("isVisible with locator default delegates correctly")
      void isVisibleWithLocatorDefault() {
         // Given
         service.returnVisible = true;

         // When
         var result = service.isVisible(locator);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastButtonText).isNull();
         assertThat(service.lastLinkType).isNull();
      }
   }
}