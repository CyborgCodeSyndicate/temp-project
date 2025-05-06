package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.tab.mock.MockTabComponentType;
import com.theairebellion.zeus.ui.components.tab.mock.MockTabService;
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

@DisplayName("TabService Interface Methods")
class TabServiceTest extends BaseUnitUITest {

   private MockTabService mockService;
   private SmartWebElement container;
   private By locator;

   // For direct testing of default methods
   private TabService directService;

   @BeforeEach
   void setUp() {
      mockService = new MockTabService();
      WebElement webElement = mock(WebElement.class);
      WebDriver driver = mock(WebDriver.class);
      container = new MockSmartWebElement(webElement, driver);
      locator = By.id("testTab");

      // Setup for direct testing of default methods
      directService = new TabService() {
         @Override
         public void tableInsertion(SmartWebElement cellElement, ComponentType componentType, String... values) {
         }

         @Override
         public boolean isSelected(TabComponentType componentType, SmartWebElement c, String text) {
            return true;
         }

         @Override
         public boolean isSelected(TabComponentType componentType, SmartWebElement c) {
            return true;
         }

         @Override
         public boolean isSelected(TabComponentType componentType, String text) {
            return true;
         }

         @Override
         public boolean isSelected(TabComponentType componentType, By by) {
            return true;
         }

         @Override
         public <T extends ButtonComponentType> void click(T componentType, SmartWebElement c, String text) {
         }

         @Override
         public <T extends ButtonComponentType> void click(T componentType, SmartWebElement c) {
         }

         @Override
         public <T extends ButtonComponentType> void click(T componentType, String text) {
         }

         @Override
         public <T extends ButtonComponentType> void click(T componentType, By by) {
         }

         @Override
         public <T extends ButtonComponentType> boolean isEnabled(T componentType, SmartWebElement c, String text) {
            return true;
         }

         @Override
         public <T extends ButtonComponentType> boolean isEnabled(T componentType, SmartWebElement c) {
            return true;
         }

         @Override
         public <T extends ButtonComponentType> boolean isEnabled(T componentType, String text) {
            return true;
         }

         @Override
         public <T extends ButtonComponentType> boolean isEnabled(T componentType, By by) {
            return true;
         }

         @Override
         public <T extends ButtonComponentType> boolean isVisible(T componentType, SmartWebElement c, String text) {
            return true;
         }

         @Override
         public <T extends ButtonComponentType> boolean isVisible(T componentType, SmartWebElement c) {
            return true;
         }

         @Override
         public <T extends ButtonComponentType> boolean isVisible(T componentType, String text) {
            return true;
         }

         @Override
         public <T extends ButtonComponentType> boolean isVisible(T componentType, By by) {
            return true;
         }
      };
   }

   @Nested
   @DisplayName("Mock Service Tests")
   class MockServiceTests {

      @Nested
      @DisplayName("IsSelected Method Tests")
      class IsSelectedMethodTests {

         @Test
         @DisplayName("isSelected with container and text delegates correctly")
         void isSelectedWithContainerAndText() {
            // Given
            mockService.reset();
            mockService.returnBool = true;
            var componentType = MockTabComponentType.DUMMY_TAB;
            var tabText = "TabX";

            // When
            var result = mockService.isSelected(componentType, container, tabText);

            // Then
            assertThat(result).isTrue();
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastContainer).isEqualTo(container);
            assertThat(mockService.lastText).isEqualTo(tabText);
         }

         @Test
         @DisplayName("isSelected with container delegates correctly")
         void isSelectedWithContainer() {
            // Given
            mockService.reset();
            mockService.returnBool = true;
            var componentType = MockTabComponentType.DUMMY_TAB;

            // When
            var result = mockService.isSelected(componentType, container);

            // Then
            assertThat(result).isTrue();
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastContainer).isEqualTo(container);
         }

         @Test
         @DisplayName("isSelected with text only delegates correctly")
         void isSelectedWithTextOnly() {
            // Given
            mockService.reset();
            mockService.returnBool = true;
            var componentType = MockTabComponentType.DUMMY_TAB;
            var tabText = "TabZ";

            // When
            var result = mockService.isSelected(componentType, tabText);

            // Then
            assertThat(result).isTrue();
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastText).isEqualTo(tabText);
         }

         @Test
         @DisplayName("isSelected with locator delegates correctly")
         void isSelectedWithLocator() {
            // Given
            mockService.reset();
            mockService.returnBool = true;
            var componentType = MockTabComponentType.DUMMY_TAB;

            // When
            var result = mockService.isSelected(componentType, locator);

            // Then
            assertThat(result).isTrue();
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastLocator).isEqualTo(locator);
         }
      }

      @Nested
      @DisplayName("Click Method Tests")
      class ClickMethodTests {

         @Test
         @DisplayName("click with container and text delegates correctly")
         void clickWithContainerAndText() {
            // Given
            mockService.reset();
            var componentType = MockTabComponentType.DUMMY_TAB;
            var tabText = "ClickTab";

            // When
            mockService.click(componentType, container, tabText);

            // Then
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastContainer).isEqualTo(container);
            assertThat(mockService.lastText).isEqualTo(tabText);
         }

         @Test
         @DisplayName("click with container delegates correctly")
         void clickWithContainer() {
            // Given
            mockService.reset();
            var componentType = MockTabComponentType.DUMMY_TAB;

            // When
            mockService.click(componentType, container);

            // Then
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastContainer).isEqualTo(container);
         }

         @Test
         @DisplayName("click with text only delegates correctly")
         void clickWithTextOnly() {
            // Given
            mockService.reset();
            var componentType = MockTabComponentType.DUMMY_TAB;
            var tabText = "CText";

            // When
            mockService.click(componentType, tabText);

            // Then
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastText).isEqualTo(tabText);
         }

         @Test
         @DisplayName("click with locator delegates correctly")
         void clickWithLocator() {
            // Given
            mockService.reset();
            var componentType = MockTabComponentType.DUMMY_TAB;

            // When
            mockService.click(componentType, locator);

            // Then
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastLocator).isEqualTo(locator);
         }
      }

      @Nested
      @DisplayName("IsEnabled Method Tests")
      class IsEnabledMethodTests {

         @Test
         @DisplayName("isEnabled with container and text delegates correctly")
         void isEnabledWithContainerAndText() {
            // Given
            mockService.reset();
            mockService.returnBool = true;
            var componentType = MockTabComponentType.DUMMY_TAB;
            var tabText = "TabA";

            // When
            var result = mockService.isEnabled(componentType, container, tabText);

            // Then
            assertThat(result).isTrue();
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastContainer).isEqualTo(container);
            assertThat(mockService.lastText).isEqualTo(tabText);
         }

         @Test
         @DisplayName("isEnabled with container delegates correctly")
         void isEnabledWithContainer() {
            // Given
            mockService.reset();
            mockService.returnBool = true;
            var componentType = MockTabComponentType.DUMMY_TAB;

            // When
            var result = mockService.isEnabled(componentType, container);

            // Then
            assertThat(result).isTrue();
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastContainer).isEqualTo(container);
         }

         @Test
         @DisplayName("isEnabled with text only delegates correctly")
         void isEnabledWithTextOnly() {
            // Given
            mockService.reset();
            mockService.returnBool = true;
            var componentType = MockTabComponentType.DUMMY_TAB;
            var tabText = "TT";

            // When
            var result = mockService.isEnabled(componentType, tabText);

            // Then
            assertThat(result).isTrue();
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastText).isEqualTo(tabText);
         }

         @Test
         @DisplayName("isEnabled with locator delegates correctly")
         void isEnabledWithLocator() {
            // Given
            mockService.reset();
            mockService.returnBool = true;
            var componentType = MockTabComponentType.DUMMY_TAB;

            // When
            var result = mockService.isEnabled(componentType, locator);

            // Then
            assertThat(result).isTrue();
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastLocator).isEqualTo(locator);
         }
      }

      @Nested
      @DisplayName("IsVisible Method Tests")
      class IsVisibleMethodTests {

         @Test
         @DisplayName("isVisible with container and text delegates correctly")
         void isVisibleWithContainerAndText() {
            // Given
            mockService.reset();
            mockService.returnBool = true;
            var componentType = MockTabComponentType.DUMMY_TAB;
            var tabText = "TabView";

            // When
            var result = mockService.isVisible(componentType, container, tabText);

            // Then
            assertThat(result).isTrue();
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastContainer).isEqualTo(container);
            assertThat(mockService.lastText).isEqualTo(tabText);
         }

         @Test
         @DisplayName("isVisible with container delegates correctly")
         void isVisibleWithContainer() {
            // Given
            mockService.reset();
            mockService.returnBool = true;
            var componentType = MockTabComponentType.DUMMY_TAB;

            // When
            var result = mockService.isVisible(componentType, container);

            // Then
            assertThat(result).isTrue();
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastContainer).isEqualTo(container);
         }

         @Test
         @DisplayName("isVisible with text only delegates correctly")
         void isVisibleWithTextOnly() {
            // Given
            mockService.reset();
            mockService.returnBool = true;
            var componentType = MockTabComponentType.DUMMY_TAB;
            var tabText = "TCheck";

            // When
            var result = mockService.isVisible(componentType, tabText);

            // Then
            assertThat(result).isTrue();
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastText).isEqualTo(tabText);
         }

         @Test
         @DisplayName("isVisible with locator delegates correctly")
         void isVisibleWithLocator() {
            // Given
            mockService.reset();
            mockService.returnBool = true;
            var componentType = MockTabComponentType.DUMMY_TAB;

            // When
            var result = mockService.isVisible(componentType, locator);

            // Then
            assertThat(result).isTrue();
            assertThat(mockService.lastComponentType).isEqualTo(componentType);
            assertThat(mockService.lastLocator).isEqualTo(locator);
         }
      }
   }

   @Nested
   @DisplayName("Direct Default Methods Tests")
   class DirectDefaultMethodsTests {

      @Test
      @DisplayName("isSelected default methods delegate correctly")
      void isSelectedDefaultMethods() {
         // Test container and text
         assertThat(directService.isSelected(container, "TabA")).isTrue();

         // Test container only
         assertThat(directService.isSelected(container)).isTrue();

         // Test text only
         assertThat(directService.isSelected("TabA")).isTrue();

         // Test locator
         assertThat(directService.isSelected(locator)).isTrue();
      }

      @Test
      @DisplayName("isEnabled default methods delegate correctly")
      void isEnabledDefaultMethods() {
         // Test container and text
         assertThat(directService.isEnabled(container, "BtnA")).isTrue();

         // Test container only
         assertThat(directService.isEnabled(container)).isTrue();

         // Test text only
         assertThat(directService.isEnabled("BtnA")).isTrue();

         // Test locator
         assertThat(directService.isEnabled(locator)).isTrue();
      }

      @Test
      @DisplayName("isVisible default methods delegate correctly")
      void isVisibleDefaultMethods() {
         // Test container and text
         assertThat(directService.isVisible(container, "BtnVis")).isTrue();

         // Test container only
         assertThat(directService.isVisible(container)).isTrue();

         // Test text only
         assertThat(directService.isVisible("BtnVis")).isTrue();

         // Test locator
         assertThat(directService.isVisible(locator)).isTrue();
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
         when(uiConfigMock.tabDefaultType()).thenReturn("TEST_TYPE");
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
         var mockType = MockTabComponentType.DUMMY_TAB;
         reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(TabComponentType.class),
                     eq("TEST_TYPE"),
                     eq("com.test.package")))
               .thenReturn(mockType);

         // When - access the private method using reflection
         var getDefaultTypeMethod = TabService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         var result = (TabComponentType) getDefaultTypeMethod.invoke(null);

         // Then
         assertThat(result).isEqualTo(mockType);
      }

      @Test
      @DisplayName("getDefaultType returns null when exception occurs")
      void getDefaultTypeWithException() throws Exception {
         // Given - ReflectionUtil throws exception when called
         reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                     eq(TabComponentType.class),
                     anyString(),
                     anyString()))
               .thenThrow(new RuntimeException("Test exception"));

         // When - access private method via reflection
         var getDefaultTypeMethod = TabService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         var result = (TabComponentType) getDefaultTypeMethod.invoke(null);

         // Then - verify null is returned when exception occurs
         assertThat(result).isNull();
      }
   }

   @Test
   @DisplayName("reset method clears all fields properly")
   void resetMethodClearsAllFields() {
      // Given
      mockService.lastComponentType = MockTabComponentType.DUMMY_TAB;
      mockService.lastContainer = container;
      mockService.lastText = "buttonText";
      mockService.lastLocator = locator;
      mockService.returnBool = true;

      // When
      mockService.reset();

      // Then
      assertThat(mockService.lastComponentType).isNull();
      assertThat(mockService.lastContainer).isNull();
      assertThat(mockService.lastText).isNull();
      assertThat(mockService.lastLocator).isNull();
      assertThat(mockService.returnBool).isFalse();
   }
}