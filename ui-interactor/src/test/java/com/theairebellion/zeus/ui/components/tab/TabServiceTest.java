package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.button.mock.MockButtonComponentType;
import com.theairebellion.zeus.ui.components.tab.mock.MockTabComponentType;
import com.theairebellion.zeus.ui.components.tab.mock.MockTabService;
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

@DisplayName("TabService Interface Default Methods")
class TabServiceTest extends BaseUnitUITest {

   private static final String TAB_TEXT = "TestTab";
   private static final MockTabComponentType TAB_DEFAULT_TYPE = MockTabComponentType.DUMMY_TAB;
   private static final MockButtonComponentType BUTTON_DEFAULT_TYPE = MockButtonComponentType.DUMMY_BUTTON;
   private MockTabService service;
   private SmartWebElement container;
   private By locator;

   @BeforeEach
   void setUp() {
      // Given
      service = new MockTabService();
      container = MockSmartWebElement.createMock();
      locator = By.id("testTab");
      service.reset();
   }

   @Test
   void testGetDefaultTypeShouldReturnNullWhenExceptionIsThrown() throws Exception {
      UiConfig mockConfig = mock(UiConfig.class);
      when(mockConfig.tabDefaultType()).thenReturn("SomeType");
      when(mockConfig.projectPackage()).thenReturn("com.example");

      try (
            MockedStatic<UiConfigHolder> configMock = mockStatic(UiConfigHolder.class);
            MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)
      ) {
         configMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);

         reflectionMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
               TabComponentType.class,
               "SomeType",
               "com.example"
         )).thenThrow(new RuntimeException("Simulated failure"));

         // Use reflection to access the private static method
         Method method = TabService.class.getDeclaredMethod("getDefaultType");
         method.setAccessible(true);

         TabComponentType result = (TabComponentType) method.invoke(null);

         assertNull(result);
      }
   }

   @Nested
   @DisplayName("IsSelected Default Method Tests")
   class IsSelectedDefaultMethodTests {

      @Test
      @DisplayName("isSelected with container and text delegates correctly")
      void isSelectedWithContainerAndTextDefault() {
         // Given
         service.returnIsSelected = true;

         // When
         var result = service.isSelected(container, TAB_TEXT);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(TAB_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(TAB_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isEqualTo(TAB_TEXT);
      }

      @Test
      @DisplayName("isSelected with container delegates correctly")
      void isSelectedWithContainerDefault() {
         // Given
         service.returnIsSelected = true;

         // When
         var result = service.isSelected(container);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(TAB_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(TAB_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("isSelected with text only delegates correctly")
      void isSelectedWithTextOnlyDefault() {
         // Given
         service.returnIsSelected = true;

         // When
         var result = service.isSelected(TAB_TEXT);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(TAB_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(TAB_DEFAULT_TYPE);
         assertThat(service.lastButtonText).isEqualTo(TAB_TEXT);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("isSelected with locator delegates correctly")
      void isSelectedWithLocatorDefault() {
         // Given
         service.returnIsSelected = true;

         // When
         var result = service.isSelected(locator);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(TAB_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(TAB_DEFAULT_TYPE);
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
         service.click(container, TAB_TEXT);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isEqualTo(TAB_TEXT);
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("click with container default delegates correctly")
      void clickWithContainerDefault() {
         // Given

         // When
         service.click(container);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("click with text default delegates correctly")
      void clickWithTextDefault() {
         // Given

         // When
         service.click(TAB_TEXT);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastButtonText).isEqualTo(TAB_TEXT);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("click with locator default delegates correctly")
      void clickWithLocatorDefault() {
         // Given

         // When
         service.click(locator);

         // Then
         assertThat(service.lastComponentTypeUsed).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastButtonText).isNull();
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
         var result = service.isEnabled(container, TAB_TEXT);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isEqualTo(TAB_TEXT);
         assertThat(service.lastLocator).isNull();
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
         assertThat(service.lastComponentTypeUsed).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("isEnabled with text default delegates correctly")
      void isEnabledWithTextDefault() {
         // Given
         service.returnEnabled = true;

         // When
         var result = service.isEnabled(TAB_TEXT);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastButtonText).isEqualTo(TAB_TEXT);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
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
         assertThat(service.lastComponentTypeUsed).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastButtonText).isNull();
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
         var result = service.isVisible(container, TAB_TEXT);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isEqualTo(TAB_TEXT);
         assertThat(service.lastLocator).isNull();
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
         assertThat(service.lastComponentTypeUsed).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastButtonText).isNull();
         assertThat(service.lastLocator).isNull();
      }

      @Test
      @DisplayName("isVisible with text default delegates correctly")
      void isVisibleWithTextDefault() {
         // Given
         service.returnVisible = true;

         // When
         var result = service.isVisible(TAB_TEXT);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastButtonText).isEqualTo(TAB_TEXT);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastLocator).isNull();
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
         assertThat(service.lastComponentTypeUsed).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isSameAs(BUTTON_DEFAULT_TYPE);
         assertThat(service.lastLocator).isEqualTo(locator);
         assertThat(service.lastContainer).isNull();
         assertThat(service.lastButtonText).isNull();
      }
   }
}