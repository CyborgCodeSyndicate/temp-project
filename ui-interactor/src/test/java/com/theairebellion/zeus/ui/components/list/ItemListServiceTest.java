package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.components.list.mock.MockItemListComponentType;
import com.theairebellion.zeus.ui.components.list.mock.MockItemListService;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.testutil.MockSmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import java.lang.reflect.Method;
import java.util.List;
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


@DisplayName("ItemListService Interface Tests")
class ItemListServiceTest extends BaseUnitUITest {

   private static final String[] SAMPLE_ITEMS = {"item1", "item2"};
   private static final String SINGLE_ITEM = "item1";
   private static final By[] SAMPLE_LOCATORS = {By.id("loc1"), By.id("loc2")};
   private static final By SINGLE_LOCATOR = By.id("loc1");
   private static final MockItemListComponentType DEFAULT_TYPE = MockItemListComponentType.DUMMY_LIST;
   private MockItemListService service;
   private SmartWebElement container;
   private By locator;
   private Strategy strategy;

   @BeforeEach
   void setUp() {
      // Given
      service = new MockItemListService();
      container = MockSmartWebElement.createMock();
      locator = By.id("testList");
      strategy = Strategy.FIRST;
      service.reset();
   }

   @Test
   void testGetDefaultTypeShouldReturnNullWhenExceptionIsThrown() throws Exception {
      UiConfig mockConfig = mock(UiConfig.class);
      when(mockConfig.listDefaultType()).thenReturn("SomeType");
      when(mockConfig.projectPackage()).thenReturn("com.example");

      try (
            MockedStatic<UiConfigHolder> configMock = mockStatic(UiConfigHolder.class);
            MockedStatic<ReflectionUtil> reflectionMock = mockStatic(ReflectionUtil.class)
      ) {
         configMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);

         reflectionMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(
               ItemListComponentType.class,
               "SomeType",
               "com.example"
         )).thenThrow(new RuntimeException("Simulated failure"));

         // Use reflection to access the private static method
         Method method = ItemListService.class.getDeclaredMethod("getDefaultType");
         method.setAccessible(true);

         ItemListComponentType result = (ItemListComponentType) method.invoke(null);

         assertNull(result);
      }
   }

   @Nested
   @DisplayName("Select Default Methods")
   class SelectDefaultMethodsTests {

      @Test
      @DisplayName("select(container, itemText) delegates correctly")
      void selectContainerItemText() {
         // Given - setup in @BeforeEach

         // When
         service.select(container, SAMPLE_ITEMS);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("select(locator, itemText) delegates correctly")
      void selectLocatorItemText() {
         // Given - setup in @BeforeEach

         // When
         service.select(locator, SAMPLE_ITEMS);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(locator);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("select(container, strategy) delegates correctly")
      void selectContainerStrategy() {
         // Given - setup in @BeforeEach

         // When
         var result = service.select(container, strategy);

         // Then
         assertThat(result).isEqualTo("mockSelectStrategy");
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("select(locator, strategy) delegates correctly")
      void selectLocatorStrategy() {
         // Given - setup in @BeforeEach

         // When
         var result = service.select(locator, strategy);

         // Then
         assertThat(result).isEqualTo("mockSelectStrategy");
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(locator);
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("select(itemText) delegates correctly")
      void selectItemText() {
         // Given - setup in @BeforeEach

         // When
         service.select(SAMPLE_ITEMS);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("select(itemLocators) delegates correctly")
      void selectItemLocators() {
         // Given - setup in @BeforeEach

         // When
         service.select(SAMPLE_LOCATORS);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).isEqualTo(SAMPLE_LOCATORS);
      }
   }

   @Nested
   @DisplayName("DeSelect Default Methods")
   class DeSelectDefaultMethodsTests {

      @Test
      @DisplayName("deSelect(container, itemText) delegates correctly")
      void deSelectContainerItemText() {
         // Given - setup in @BeforeEach

         // When
         service.deSelect(container, SAMPLE_ITEMS);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("deSelect(locator, itemText) delegates correctly")
      void deSelectLocatorItemText() {
         // Given - setup in @BeforeEach

         // When
         service.deSelect(locator, SAMPLE_ITEMS);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(locator);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("deSelect(container, strategy) delegates correctly")
      void deSelectContainerStrategy() {
         // Given - setup in @BeforeEach

         // When
         var result = service.deSelect(container, strategy);

         // Then
         assertThat(result).isEqualTo("mockDeSelectStrategy");
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("deSelect(locator, strategy) delegates correctly")
      void deSelectLocatorStrategy() {
         // Given - setup in @BeforeEach

         // When
         var result = service.deSelect(locator, strategy);

         // Then
         assertThat(result).isEqualTo("mockDeSelectStrategy");
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(locator);
         assertThat(service.lastStrategy).isEqualTo(strategy);
      }

      @Test
      @DisplayName("deSelect(itemText) delegates correctly")
      void deSelectItemText() {
         // Given - setup in @BeforeEach

         // When
         service.deSelect(SAMPLE_ITEMS);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("deSelect(itemLocators) delegates correctly")
      void deSelectItemLocators() {
         // Given - setup in @BeforeEach

         // When
         service.deSelect(SAMPLE_LOCATORS);

         // Then
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).isEqualTo(SAMPLE_LOCATORS);
      }
   }

   @Nested
   @DisplayName("AreSelected/IsSelected Default Methods")
   class AreSelectedIsSelectedDefaultMethodsTests {

      @Test
      @DisplayName("areSelected(container, itemText) delegates correctly")
      void areSelectedContainerItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.areSelected(container, SAMPLE_ITEMS);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("isSelected(container, itemText) delegates correctly")
      void isSelectedContainerItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isSelected(container, SINGLE_ITEM);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).containsExactly(SINGLE_ITEM);
      }

      @Test
      @DisplayName("areSelected(locator, itemText) delegates correctly")
      void areSelectedLocatorItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.areSelected(locator, SAMPLE_ITEMS);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(locator);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("isSelected(locator, itemText) delegates correctly")
      void isSelectedLocatorItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isSelected(locator, SINGLE_ITEM);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(locator);
         assertThat(service.lastText).containsExactly(SINGLE_ITEM);
      }

      @Test
      @DisplayName("areSelected(itemText) delegates correctly")
      void areSelectedItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.areSelected(SAMPLE_ITEMS);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("isSelected(itemText) delegates correctly")
      void isSelectedItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isSelected(SINGLE_ITEM);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastText).containsExactly(SINGLE_ITEM);
      }

      @Test
      @DisplayName("areSelected(itemLocators) delegates correctly")
      void areSelectedItemLocators() {
         // Given
         service.returnBool = true;

         // When
         var result = service.areSelected(SAMPLE_LOCATORS);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).isEqualTo(SAMPLE_LOCATORS);
      }

      @Test
      @DisplayName("isSelected(itemLocator) delegates correctly")
      void isSelectedItemLocator() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isSelected(SINGLE_LOCATOR);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(SINGLE_LOCATOR);
      }
   }

   @Nested
   @DisplayName("AreEnabled/IsEnabled Default Methods")
   class AreEnabledIsEnabledDefaultMethodsTests {

      @Test
      @DisplayName("areEnabled(container, itemText) delegates correctly")
      void areEnabledContainerItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.areEnabled(container, SAMPLE_ITEMS);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("isEnabled(container, itemText) delegates correctly")
      void isEnabledContainerItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isEnabled(container, SINGLE_ITEM);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).containsExactly(SINGLE_ITEM);
      }

      @Test
      @DisplayName("areEnabled(locator, itemText) delegates correctly")
      void areEnabledLocatorItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.areEnabled(locator, SAMPLE_ITEMS);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(locator);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("isEnabled(locator, itemText) delegates correctly")
      void isEnabledLocatorItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isEnabled(locator, SINGLE_ITEM);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(locator);
         assertThat(service.lastText).containsExactly(SINGLE_ITEM);
      }

      @Test
      @DisplayName("areEnabled(itemText) delegates correctly")
      void areEnabledItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.areEnabled(SAMPLE_ITEMS);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("isEnabled(itemText) delegates correctly")
      void isEnabledItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isEnabled(SINGLE_ITEM);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastText).containsExactly(SINGLE_ITEM);
      }

      @Test
      @DisplayName("areEnabled(itemLocators) delegates correctly")
      void areEnabledItemLocators() {
         // Given
         service.returnBool = true;

         // When
         var result = service.areEnabled(SAMPLE_LOCATORS);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).isEqualTo(SAMPLE_LOCATORS);
      }

      @Test
      @DisplayName("isEnabled(itemLocator) delegates correctly")
      void isEnabledItemLocator() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isEnabled(SINGLE_LOCATOR);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(SINGLE_LOCATOR);
      }
   }

   @Nested
   @DisplayName("AreVisible/IsVisible Default Methods")
   class AreVisibleIsVisibleDefaultMethodsTests {

      @Test
      @DisplayName("areVisible(container, itemText) delegates correctly")
      void areVisibleContainerItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.areVisible(container, SAMPLE_ITEMS);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("isVisible(container, itemText) delegates correctly")
      void isVisibleContainerItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isVisible(container, SINGLE_ITEM);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
         assertThat(service.lastText).containsExactly(SINGLE_ITEM);
      }

      @Test
      @DisplayName("areVisible(locator, itemText) delegates correctly")
      void areVisibleLocatorItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.areVisible(locator, SAMPLE_ITEMS);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(locator);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("isVisible(locator, itemText) delegates correctly")
      void isVisibleLocatorItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isVisible(locator, SINGLE_ITEM);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(locator);
         assertThat(service.lastText).containsExactly(SINGLE_ITEM);
      }

      @Test
      @DisplayName("areVisible(itemText) delegates correctly")
      void areVisibleItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.areVisible(SAMPLE_ITEMS);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastText).isEqualTo(SAMPLE_ITEMS);
      }

      @Test
      @DisplayName("isVisible(itemText) delegates correctly")
      void isVisibleItemText() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isVisible(SINGLE_ITEM);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastText).containsExactly(SINGLE_ITEM);
      }

      @Test
      @DisplayName("areVisible(itemLocators) delegates correctly")
      void areVisibleItemLocators() {
         // Given
         service.returnBool = true;

         // When
         var result = service.areVisible(SAMPLE_LOCATORS);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).isEqualTo(SAMPLE_LOCATORS);
      }

      @Test
      @DisplayName("isVisible(itemLocator) delegates correctly")
      void isVisibleItemLocator() {
         // Given
         service.returnBool = true;

         // When
         var result = service.isVisible(SINGLE_LOCATOR);

         // Then
         assertThat(result).isTrue();
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(SINGLE_LOCATOR);
      }
   }

   @Nested
   @DisplayName("Get Default Methods")
   class GetDefaultMethodsTests {

      @Test
      @DisplayName("getSelected(container) delegates correctly")
      void getSelectedContainer() {
         // Given
         var expectedList = List.of("sel1");
         service.returnSelectedList = expectedList;

         // When
         var result = service.getSelected(container);

         // Then
         assertThat(result).isEqualTo(expectedList);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("getSelected(locator) delegates correctly")
      void getSelectedLocator() {
         // Given
         var expectedList = List.of("sel2");
         service.returnSelectedList = expectedList;

         // When
         var result = service.getSelected(locator);

         // Then
         assertThat(result).isEqualTo(expectedList);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(locator);
      }

      @Test
      @DisplayName("getAll(container) delegates correctly")
      void getAllContainer() {
         // Given
         var expectedList = List.of("all1");
         service.returnAllList = expectedList;

         // When
         var result = service.getAll(container);

         // Then
         assertThat(result).isEqualTo(expectedList);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastContainer).isEqualTo(container);
      }

      @Test
      @DisplayName("getAll(locator) delegates correctly")
      void getAllLocator() {
         // Given
         var expectedList = List.of("all2");
         service.returnAllList = expectedList;

         // When
         var result = service.getAll(locator);

         // Then
         assertThat(result).isEqualTo(expectedList);
         assertThat(service.lastComponentTypeUsed).isEqualTo(DEFAULT_TYPE);
         assertThat(service.explicitComponentType).isEqualTo(DEFAULT_TYPE);
         assertThat(service.lastLocators).containsExactly(locator);
      }
   }
}