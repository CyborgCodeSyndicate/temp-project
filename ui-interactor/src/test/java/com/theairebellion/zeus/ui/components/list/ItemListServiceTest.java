package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.list.mock.MockItemListComponentType;
import com.theairebellion.zeus.ui.components.list.mock.MockItemListService;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("ItemListService Interface Tests")
class ItemListServiceTest extends BaseUnitUITest {

   private MockItemListService service;
   private MockSmartWebElement container;
   private By locator;
   private Strategy strategy;

   @BeforeEach
   void setUp() {
      service = new MockItemListService();
      container = MockSmartWebElement.createMock();
      locator = By.id("testList");
      strategy = Strategy.FIRST;
      service.reset();
   }

   @Nested
   @DisplayName("Select Default Methods")
   class SelectDefaultMethodsTests {

      @Test
      @DisplayName("select(container, itemText) delegates to implementation with DEFAULT_TYPE")
      void selectContainerItemText() {
         // When
         service.select(container, "item1", "item2");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
      }

      @Test
      @DisplayName("select(locator, itemText) delegates to implementation with DEFAULT_TYPE")
      void selectLocatorItemText() {
         // When
         service.select(locator, "item1");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastLocators).isEqualTo(new By[] {locator});
         assertThat(service.lastText).isEqualTo(new String[] {"item1"});
      }

      @Test
      @DisplayName("select(container, strategy) delegates to implementation with DEFAULT_TYPE")
      void selectContainerStrategy() {
         // When
         String result = service.select(container, strategy);

         // Then
         assertThat(result).isEqualTo("mockSelectStrategy");
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("select(locator, strategy) delegates to implementation with DEFAULT_TYPE")
      void selectLocatorStrategy() {
         // When
         String result = service.select(locator, strategy);

         // Then
         assertThat(result).isEqualTo("mockSelectStrategy");
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastLocators).isEqualTo(new By[] {locator});
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("select(itemText) delegates to implementation with DEFAULT_TYPE")
      void selectItemText() {
         // When
         service.select("item1", "item2");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
      }

      @Test
      @DisplayName("select(itemLocators) delegates to implementation with DEFAULT_TYPE")
      void selectItemLocators() {
         // Given
         By locator1 = By.id("loc1");
         By locator2 = By.id("loc2");

         // When
         service.select(locator1, locator2);

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastLocators).isEqualTo(new By[] {locator1, locator2});
      }
   }

   @Nested
   @DisplayName("AreSelected/IsSelected Default Methods")
   class AreSelectedDefaultMethodsTests {

      @Test
      @DisplayName("areSelected(container, itemText) delegates to implementation with DEFAULT_TYPE")
      void areSelectedContainerItemText() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.areSelected(container, "item1", "item2");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
      }

      @Test
      @DisplayName("isSelected(container, itemText) delegates to implementation with DEFAULT_TYPE")
      void isSelectedContainerItemText() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.isSelected(container, "item");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(new String[] {"item"});
      }

      @Test
      @DisplayName("areSelected(locator, itemText) delegates to implementation with DEFAULT_TYPE")
      void areSelectedLocatorItemText() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.areSelected(locator, "item1", "item2");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastLocators).isEqualTo(new By[] {locator});
         assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
      }

      @Test
      @DisplayName("isSelected(locator, itemText) delegates to implementation with DEFAULT_TYPE")
      void isSelectedLocatorItemText() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.isSelected(locator, "item");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastLocators).isEqualTo(new By[] {locator});
         assertThat(service.lastText).isEqualTo(new String[] {"item"});
      }

      @Test
      @DisplayName("areSelected(itemText) delegates to implementation with DEFAULT_TYPE")
      void areSelectedItemText() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.areSelected("item1", "item2");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
      }

      @Test
      @DisplayName("isSelected(itemText) delegates to implementation with DEFAULT_TYPE")
      void isSelectedItemText() {
         // Given
         service.returnBool = true;

         // When
         boolean result = service.isSelected("item");

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastText).isEqualTo(new String[] {"item"});
      }

      @Test
      @DisplayName("areSelected(itemLocators) delegates to implementation with DEFAULT_TYPE")
      void areSelectedItemLocators() {
         // Given
         service.returnBool = true;
         By locator1 = By.id("item1");
         By locator2 = By.id("item2");

         // When
         boolean result = service.areSelected(locator1, locator2);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastLocators).isEqualTo(new By[] {locator1, locator2});
      }

      @Test
      @DisplayName("isSelected(itemLocator) delegates to implementation with DEFAULT_TYPE")
      void isSelectedItemLocator() {
         // Given
         service.returnBool = true;
         By itemLocator = By.id("item");

         // When
         boolean result = service.isSelected(itemLocator);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastLocators).isEqualTo(new By[] {itemLocator});
      }
   }

   @Nested
   @DisplayName("DeSelect Default Methods")
   class DeSelectDefaultMethodsTests {

      @Test
      @DisplayName("deSelect(container, itemText) delegates to implementation with DEFAULT_TYPE")
      void deSelectContainerItemText() {
         // When
         service.deSelect(container, "item1", "item2");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
      }

      @Test
      @DisplayName("deSelect(locator, itemText) delegates to implementation with DEFAULT_TYPE")
      void deSelectLocatorItemText() {
         // When
         service.deSelect(locator, "item1");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastLocators).isEqualTo(new By[] {locator});
         assertThat(service.lastText).isEqualTo(new String[] {"item1"});
      }

      @Test
      @DisplayName("deSelect(container, strategy) delegates to implementation with DEFAULT_TYPE")
      void deSelectContainerStrategy() {
         // When
         String result = service.deSelect(container, strategy);

         // Then
         assertThat(result).isEqualTo("mockDeSelectStrategy");
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("deSelect(locator, strategy) delegates to implementation with DEFAULT_TYPE")
      void deSelectLocatorStrategy() {
         // When
         String result = service.deSelect(locator, strategy);

         // Then
         assertThat(result).isEqualTo("mockDeSelectStrategy");
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastLocators).isEqualTo(new By[] {locator});
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("deSelect(itemText) delegates to implementation with DEFAULT_TYPE")
      void deSelectItemText() {
         // When
         service.deSelect("item1", "item2");

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
      }

      @Test
      @DisplayName("deSelect(itemLocators) delegates to implementation with DEFAULT_TYPE")
      void deSelectItemLocators() {
         // Given
         By locator1 = By.id("loc1");
         By locator2 = By.id("loc2");

         // When
         service.deSelect(locator1, locator2);

         // Then
         assertThat(service.lastComponentType).isEqualTo(MockItemListComponentType.DUMMY);
         assertThat(service.lastLocators).isEqualTo(new By[] {locator1, locator2});
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
         uiConfigHolderMock = Mockito.mockStatic(UiConfigHolder.class);
         reflectionUtilMock = Mockito.mockStatic(ReflectionUtil.class);

         uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfigMock);
         Mockito.when(uiConfigMock.projectPackage()).thenReturn("com.test.package");
         Mockito.when(uiConfigMock.listDefaultType()).thenReturn("DUMMY_TYPE");
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
         MockItemListComponentType mockType = MockItemListComponentType.DUMMY;
         reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                     Mockito.eq(ItemListComponentType.class),
                     Mockito.eq("DUMMY_TYPE"),
                     Mockito.eq("com.test.package")))
               .thenReturn(mockType);

         // When - access the private method using reflection
         Method getDefaultTypeMethod = ItemListService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         ItemListComponentType result = (ItemListComponentType) getDefaultTypeMethod.invoke(null);

         // Then
         assertThat(result).isEqualTo(mockType);
      }

      @Test
      @DisplayName("getDefaultType returns null when exception occurs")
      void getDefaultTypeWithException() throws Exception {
         // Given
         reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                     Mockito.eq(ItemListComponentType.class),
                     Mockito.anyString(),
                     Mockito.anyString()))
               .thenThrow(new RuntimeException("Test exception"));

         // When - access the private method using reflection
         Method getDefaultTypeMethod = ItemListService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         ItemListComponentType result = (ItemListComponentType) getDefaultTypeMethod.invoke(null);

         // Then
         assertThat(result).isNull();
      }

      @Test
      @DisplayName("DEFAULT_TYPE constant is initialized correctly")
      void defaultTypeInitialization() throws Exception {
         // Given
         MockItemListComponentType mockType = MockItemListComponentType.DUMMY;
         reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
                     Mockito.eq(ItemListComponentType.class),
                     Mockito.anyString(),
                     Mockito.anyString()))
               .thenReturn(mockType);

         // When/Then - verify the DEFAULT_TYPE field by reflection
         Field defaultTypeField = ItemListService.class.getDeclaredField("DEFAULT_TYPE");
         defaultTypeField.setAccessible(true);

         // Test via the method to ensure consistent behavior
         Method getDefaultTypeMethod = ItemListService.class.getDeclaredMethod("getDefaultType");
         getDefaultTypeMethod.setAccessible(true);
         ItemListComponentType resultFromMethod = (ItemListComponentType) getDefaultTypeMethod.invoke(null);

         assertThat(resultFromMethod).isEqualTo(mockType);
      }
   }

   @Test
   @DisplayName("areEnabled(container, itemText) delegates correctly")
   void areEnabledContainerItemText() {
      // Given
      service.returnBool = true;

      // When
      boolean result = service.areEnabled(container, "item1", "item2");

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastContainer).isEqualTo(container);
      assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
   }

   @Test
   @DisplayName("isEnabled(container, itemText) delegates correctly")
   void isEnabledContainerItemText() {
      // Given
      service.returnBool = true;

      // When
      boolean result = service.isEnabled(container, "item");

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastContainer).isEqualTo(container);
      assertThat(service.lastText).isEqualTo(new String[] {"item"});
   }

   @Test
   @DisplayName("areEnabled(locator, itemText) delegates correctly")
   void areEnabledLocatorItemText() {
      // Given
      service.returnBool = true;

      // When
      boolean result = service.areEnabled(locator, "item1", "item2");

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastLocators).isEqualTo(new By[] {locator});
      assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
   }

   @Test
   @DisplayName("isEnabled(locator, itemText) delegates correctly")
   void isEnabledLocatorItemText() {
      // Given
      service.returnBool = true;

      // When
      boolean result = service.isEnabled(locator, "item");

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastLocators).isEqualTo(new By[] {locator});
      assertThat(service.lastText).isEqualTo(new String[] {"item"});
   }

   @Test
   @DisplayName("areEnabled(itemText) delegates correctly")
   void areEnabledItemText() {
      // Given
      service.returnBool = true;

      // When
      boolean result = service.areEnabled("item1", "item2");

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
   }

   @Test
   @DisplayName("isEnabled(itemText) delegates correctly")
   void isEnabledItemText() {
      // Given
      service.returnBool = true;

      // When
      boolean result = service.isEnabled("item");

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastText).isEqualTo(new String[] {"item"});
   }

   @Test
   @DisplayName("areEnabled(itemLocators) delegates correctly")
   void areEnabledItemLocators() {
      // Given
      service.returnBool = true;
      By locator1 = By.id("loc1");
      By locator2 = By.id("loc2");

      // When
      boolean result = service.areEnabled(locator1, locator2);

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastLocators).isEqualTo(new By[] {locator1, locator2});
   }

   @Test
   @DisplayName("isEnabled(itemLocator) delegates correctly")
   void isEnabledItemLocator() {
      // Given
      service.returnBool = true;
      By itemLocator = By.id("item");

      // When
      boolean result = service.isEnabled(itemLocator);

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastLocators).isEqualTo(new By[] {itemLocator});
   }

   @Test
   @DisplayName("areVisible(container, itemText) delegates correctly")
   void areVisibleContainerItemText() {
      // Given
      service.returnBool = true;

      // When
      boolean result = service.areVisible(container, "item1", "item2");

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastContainer).isEqualTo(container);
      assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
   }

   @Test
   @DisplayName("isVisible(container, itemText) delegates correctly")
   void isVisibleContainerItemText() {
      // Given
      service.returnBool = true;

      // When
      boolean result = service.isVisible(container, "item");

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastContainer).isEqualTo(container);
      assertThat(service.lastText).isEqualTo(new String[] {"item"});
   }

   @Test
   @DisplayName("areVisible(locator, itemText) delegates correctly")
   void areVisibleLocatorItemText() {
      // Given
      service.returnBool = true;

      // When
      boolean result = service.areVisible(locator, "item1", "item2");

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastLocators).isEqualTo(new By[] {locator});
      assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
   }

   @Test
   @DisplayName("isVisible(locator, itemText) delegates correctly")
   void isVisibleLocatorItemText() {
      // Given
      service.returnBool = true;

      // When
      boolean result = service.isVisible(locator, "item");

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastLocators).isEqualTo(new By[] {locator});
      assertThat(service.lastText).isEqualTo(new String[] {"item"});
   }

   @Test
   @DisplayName("areVisible(itemText) delegates correctly")
   void areVisibleItemText() {
      // Given
      service.returnBool = true;

      // When
      boolean result = service.areVisible("item1", "item2");

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastText).isEqualTo(new String[] {"item1", "item2"});
   }

   @Test
   @DisplayName("isVisible(itemText) delegates correctly")
   void isVisibleItemText() {
      // Given
      service.returnBool = true;

      // When
      boolean result = service.isVisible("item");

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastText).isEqualTo(new String[] {"item"});
   }

   @Test
   @DisplayName("areVisible(itemLocators) delegates correctly")
   void areVisibleItemLocators() {
      // Given
      service.returnBool = true;
      By locator1 = By.id("loc1");
      By locator2 = By.id("loc2");

      // When
      boolean result = service.areVisible(locator1, locator2);

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastLocators).isEqualTo(new By[] {locator1, locator2});
   }

   @Test
   @DisplayName("isVisible(itemLocator) delegates correctly")
   void isVisibleItemLocator() {
      // Given
      service.returnBool = true;
      By itemLocator = By.id("item");

      // When
      boolean result = service.isVisible(itemLocator);

      // Then
      assertThat(result).isTrue();
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastLocators).isEqualTo(new By[] {itemLocator});
   }

   @Test
   @DisplayName("getSelected(container) delegates correctly")
   void getSelectedContainer() {
      // Given
      List<String> selectedItems = Arrays.asList("item1", "item2");
      service.returnSelectedList = selectedItems;

      // When
      List<String> result = service.getSelected(container);

      // Then
      assertThat(result).isEqualTo(selectedItems);
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastContainer).isEqualTo(container);
   }

   @Test
   @DisplayName("getSelected(locator) delegates correctly")
   void getSelectedLocator() {
      // Given
      List<String> selectedItems = Arrays.asList("item1", "item2");
      service.returnSelectedList = selectedItems;

      // When
      List<String> result = service.getSelected(locator);

      // Then
      assertThat(result).isEqualTo(selectedItems);
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastLocators).isEqualTo(new By[] {locator});
   }

   @Test
   @DisplayName("getAll(container) delegates correctly")
   void getAllContainer() {
      // Given
      List<String> allItems = Arrays.asList("item1", "item2", "item3");
      service.returnAllList = allItems;

      // When
      List<String> result = service.getAll(container);

      // Then
      assertThat(result).isEqualTo(allItems);
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastContainer).isEqualTo(container);
   }

   @Test
   @DisplayName("getAll(locator) delegates correctly")
   void getAllLocator() {
      // Given
      List<String> allItems = Arrays.asList("item1", "item2", "item3");
      service.returnAllList = allItems;

      // When
      List<String> result = service.getAll(locator);

      // Then
      assertThat(result).isEqualTo(allItems);
      assertThat(service.lastComponentType).isNotNull();
      assertThat(service.lastLocators).isEqualTo(new By[] {locator});
   }
}