package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.list.mock.MockItemListComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("ItemListServiceImpl Tests")
class ItemListServiceImplTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private ItemListServiceImpl service;
    private SmartWebElement container;
    private By locator;
    private Strategy strategy;
    private ItemList itemListMock;
    private MockItemListComponentType componentType;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeEach
    void setUp() {
        driver = mock(SmartWebDriver.class);
        service = new ItemListServiceImpl(driver);
        container = MockSmartWebElement.createMock();
        locator = By.id("list");
        strategy = Strategy.FIRST;
        itemListMock = mock(ItemList.class);
        componentType = MockItemListComponentType.DUMMY;

        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getListComponent(eq(componentType), eq(driver)))
                .thenReturn(itemListMock);
    }

    @AfterEach
    void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Nested
    @DisplayName("Component Creation and Caching")
    class ComponentCreationAndCaching {

        @Test
        @DisplayName("Components are cached and reused")
        void componentCaching() {
            // When
            service.select(componentType, "item1");
            service.select(componentType, "item2");

            // Then
            factoryMock.verify(() -> ComponentFactory.getListComponent(eq(componentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("Different component types create different instances")
        void differentComponentTypes() throws Exception {
            // Given
            ItemListServiceImpl service2 = new ItemListServiceImpl(driver);

            ItemList component1 = mock(ItemList.class);
            ItemList component2 = mock(ItemList.class);

            // Set up component caches manually using reflection
            Map<ItemListComponentType, ItemList> componentsMap1 = new HashMap<>();
            componentsMap1.put(componentType, component1);

            Map<ItemListComponentType, ItemList> componentsMap2 = new HashMap<>();
            componentsMap2.put(componentType, component2);

            // Access private field to set components map
            Field componentsField = AbstractComponentService.class.getDeclaredField("components");
            componentsField.setAccessible(true);
            componentsField.set(service, componentsMap1);
            componentsField.set(service2, componentsMap2);

            // When
            service.select(componentType, "test1");
            service2.select(componentType, "test2");

            // Then
            verify(component1).select("test1");
            verify(component2).select("test2");
        }

        @Test
        @DisplayName("createComponent calls ComponentFactory.getListComponent")
        void createComponentCallsFactory() throws Exception {
            // Given
            Method createComponentMethod = ItemListServiceImpl.class.getDeclaredMethod(
                    "createComponent", ItemListComponentType.class);
            createComponentMethod.setAccessible(true);

            // When
            ItemList result = (ItemList) createComponentMethod.invoke(service, componentType);

            // Then
            assertThat(result).isSameAs(itemListMock);
            factoryMock.verify(() -> ComponentFactory.getListComponent(eq(componentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("ItemListComponent helper method returns correct component")
        void itemListComponentReturnsComponent() throws Exception {
            // Given
            Method itemListComponentMethod = ItemListServiceImpl.class.getDeclaredMethod(
                    "ItemListComponent", ItemListComponentType.class);
            itemListComponentMethod.setAccessible(true);

            // Insert component into cache
            service.select(componentType, "test"); // This will cache the component

            // When
            ItemList result = (ItemList) itemListComponentMethod.invoke(service, componentType);

            // Then
            assertThat(result).isSameAs(itemListMock);
        }
    }

    @Nested
    @DisplayName("Select Method Tests")
    class SelectMethodTests {

        @Test
        @DisplayName("select(componentType, container, itemText) delegates correctly")
        void selectContainerItemText() {
            // When
            service.select(componentType, container, "item1", "item2");

            // Then
            verify(itemListMock).select(container, "item1", "item2");
        }

        @Test
        @DisplayName("select(componentType, locator, itemText) delegates correctly")
        void selectLocatorItemText() {
            // When
            service.select(componentType, locator, "item1", "item2");

            // Then
            verify(itemListMock).select(locator, "item1", "item2");
        }

        @Test
        @DisplayName("select(componentType, container, strategy) delegates correctly")
        void selectContainerStrategy() {
            // Given
            when(itemListMock.select(container, strategy)).thenReturn("selectedItem");

            // When
            String result = service.select(componentType, container, strategy);

            // Then
            assertThat(result).isEqualTo("selectedItem");
            verify(itemListMock).select(container, strategy);
        }

        @Test
        @DisplayName("select(componentType, locator, strategy) delegates correctly")
        void selectLocatorStrategy() {
            // Given
            when(itemListMock.select(locator, strategy)).thenReturn("selectedItem");

            // When
            String result = service.select(componentType, locator, strategy);

            // Then
            assertThat(result).isEqualTo("selectedItem");
            verify(itemListMock).select(locator, strategy);
        }

        @Test
        @DisplayName("select(componentType, itemText) delegates correctly")
        void selectItemText() {
            // When
            service.select(componentType, "item1", "item2");

            // Then
            verify(itemListMock).select("item1", "item2");
        }

        @Test
        @DisplayName("select(componentType, itemLocators) delegates correctly")
        void selectItemLocators() {
            // Given
            By locator1 = By.id("item1");
            By locator2 = By.id("item2");

            // When
            service.select(componentType, locator1, locator2);

            // Then
            verify(itemListMock).select(locator1, locator2);
        }
    }

    @Nested
    @DisplayName("DeSelect Method Tests")
    class DeSelectMethodTests {

        @Test
        @DisplayName("deSelect(componentType, container, itemText) delegates correctly")
        void deSelectContainerItemText() {
            // When
            service.deSelect(componentType, container, "item1", "item2");

            // Then
            verify(itemListMock).deSelect(container, "item1", "item2");
        }

        @Test
        @DisplayName("deSelect(componentType, locator, itemText) delegates correctly")
        void deSelectLocatorItemText() {
            // When
            service.deSelect(componentType, locator, "item1", "item2");

            // Then
            verify(itemListMock).deSelect(locator, "item1", "item2");
        }

        @Test
        @DisplayName("deSelect(componentType, container, strategy) delegates correctly")
        void deSelectContainerStrategy() {
            // Given
            when(itemListMock.deSelect(container, strategy)).thenReturn("deselectedItem");

            // When
            String result = service.deSelect(componentType, container, strategy);

            // Then
            assertThat(result).isEqualTo("deselectedItem");
            verify(itemListMock).deSelect(container, strategy);
        }

        @Test
        @DisplayName("deSelect(componentType, locator, strategy) delegates correctly")
        void deSelectLocatorStrategy() {
            // Given
            when(itemListMock.deSelect(locator, strategy)).thenReturn("deselectedItem");

            // When
            String result = service.deSelect(componentType, locator, strategy);

            // Then
            assertThat(result).isEqualTo("deselectedItem");
            verify(itemListMock).deSelect(locator, strategy);
        }

        @Test
        @DisplayName("deSelect(componentType, itemText) delegates correctly")
        void deSelectItemText() {
            // When
            service.deSelect(componentType, "item1", "item2");

            // Then
            verify(itemListMock).deSelect("item1", "item2");
        }

        @Test
        @DisplayName("deSelect(componentType, itemLocators) delegates correctly")
        void deSelectItemLocators() {
            // Given
            By locator1 = By.id("item1");
            By locator2 = By.id("item2");

            // When
            service.deSelect(componentType, locator1, locator2);

            // Then
            verify(itemListMock).deSelect(locator1, locator2);
        }
    }

    @Nested
    @DisplayName("AreSelected/IsSelected Method Tests")
    class AreSelectedIsSelectedMethodTests {

        @Test
        @DisplayName("areSelected(componentType, container, itemText) delegates correctly")
        void areSelectedContainerItemText() {
            // Given
            when(itemListMock.areSelected(container, "item1", "item2")).thenReturn(true);

            // When
            boolean result = service.areSelected(componentType, container, "item1", "item2");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(container, "item1", "item2");
        }

        @Test
        @DisplayName("isSelected(componentType, container, itemText) delegates to areSelected")
        void isSelectedContainerItemText() {
            // Given
            when(itemListMock.areSelected(container, "item")).thenReturn(true);

            // When
            boolean result = service.isSelected(componentType, container, "item");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(container, "item");
        }

        @Test
        @DisplayName("areSelected(componentType, locator, itemText) delegates correctly")
        void areSelectedLocatorItemText() {
            // Given
            when(itemListMock.areSelected(locator, "item1", "item2")).thenReturn(true);

            // When
            boolean result = service.areSelected(componentType, locator, "item1", "item2");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(locator, "item1", "item2");
        }

        @Test
        @DisplayName("isSelected(componentType, locator, itemText) delegates to areSelected")
        void isSelectedLocatorItemText() {
            // Given
            when(itemListMock.areSelected(locator, "item")).thenReturn(true);

            // When
            boolean result = service.isSelected(componentType, locator, "item");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(locator, "item");
        }

        @Test
        @DisplayName("areSelected(componentType, itemText) delegates correctly")
        void areSelectedItemText() {
            // Given
            when(itemListMock.areSelected("item1", "item2")).thenReturn(true);

            // When
            boolean result = service.areSelected(componentType, "item1", "item2");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected("item1", "item2");
        }

        @Test
        @DisplayName("isSelected(componentType, itemText) delegates to areSelected")
        void isSelectedItemText() {
            // Given
            when(itemListMock.areSelected("item")).thenReturn(true);

            // When
            boolean result = service.isSelected(componentType, "item");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected("item");
        }

        @Test
        @DisplayName("areSelected(componentType, itemLocators) delegates correctly")
        void areSelectedItemLocators() {
            // Given
            By locator1 = By.id("item1");
            By locator2 = By.id("item2");
            when(itemListMock.areSelected(locator1, locator2)).thenReturn(true);

            // When
            boolean result = service.areSelected(componentType, locator1, locator2);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(locator1, locator2);
        }

        @Test
        @DisplayName("isSelected(componentType, itemLocator) delegates to areSelected with singleton array")
        void isSelectedItemLocator() {
            // Given
            By locator1 = By.id("item1");
            when(itemListMock.areSelected(any(By[].class))).thenReturn(true);

            // When
            boolean result = service.isSelected(componentType, locator1);

            // Then
            assertThat(result).isTrue();
            // Since we're verifying an array creation, we can't use eq() matcher directly
            verify(itemListMock).areSelected(any(By[].class));
        }
    }

    @Nested
    @DisplayName("AreEnabled/IsEnabled Method Tests")
    class AreEnabledIsEnabledMethodTests {

        @Test
        @DisplayName("areEnabled(componentType, container, itemText) delegates correctly")
        void areEnabledContainerItemText() {
            // Given
            when(itemListMock.areEnabled(container, "item1", "item2")).thenReturn(true);

            // When
            boolean result = service.areEnabled(componentType, container, "item1", "item2");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(container, "item1", "item2");
        }

        @Test
        @DisplayName("isEnabled(componentType, container, itemText) delegates to areEnabled")
        void isEnabledContainerItemText() {
            // Given
            when(itemListMock.areEnabled(container, "item")).thenReturn(true);

            // When
            boolean result = service.isEnabled(componentType, container, "item");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(container, "item");
        }

        @Test
        @DisplayName("areEnabled(componentType, locator, itemText) delegates correctly")
        void areEnabledLocatorItemText() {
            // Given
            when(itemListMock.areEnabled(locator, "item1", "item2")).thenReturn(true);

            // When
            boolean result = service.areEnabled(componentType, locator, "item1", "item2");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(locator, "item1", "item2");
        }

        @Test
        @DisplayName("isEnabled(componentType, locator, itemText) delegates to areEnabled")
        void isEnabledLocatorItemText() {
            // Given
            when(itemListMock.areEnabled(locator, "item")).thenReturn(true);

            // When
            boolean result = service.isEnabled(componentType, locator, "item");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(locator, "item");
        }

        @Test
        @DisplayName("areEnabled(componentType, itemText) delegates correctly")
        void areEnabledItemText() {
            // Given
            when(itemListMock.areEnabled("item1", "item2")).thenReturn(true);

            // When
            boolean result = service.areEnabled(componentType, "item1", "item2");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled("item1", "item2");
        }

        @Test
        @DisplayName("isEnabled(componentType, itemText) delegates to areEnabled")
        void isEnabledItemText() {
            // Given
            when(itemListMock.areEnabled("item")).thenReturn(true);

            // When
            boolean result = service.isEnabled(componentType, "item");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled("item");
        }

        @Test
        @DisplayName("areEnabled(componentType, itemLocators) delegates correctly")
        void areEnabledItemLocators() {
            // Given
            By locator1 = By.id("item1");
            By locator2 = By.id("item2");
            when(itemListMock.areEnabled(locator1, locator2)).thenReturn(true);

            // When
            boolean result = service.areEnabled(componentType, locator1, locator2);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(locator1, locator2);
        }

        @Test
        @DisplayName("isEnabled(componentType, itemLocator) delegates to areEnabled with singleton array")
        void isEnabledItemLocator() {
            // Given
            By locator1 = By.id("item1");
            when(itemListMock.areEnabled(any(By[].class))).thenReturn(true);

            // When
            boolean result = service.isEnabled(componentType, locator1);

            // Then
            assertThat(result).isTrue();
            // Since we're verifying an array creation, we can't use eq() matcher directly
            verify(itemListMock).areEnabled(any(By[].class));
        }
    }

    @Nested
    @DisplayName("AreVisible/IsVisible Method Tests")
    class AreVisibleIsVisibleMethodTests {

        @Test
        @DisplayName("areVisible(componentType, container, itemText) delegates correctly")
        void areVisibleContainerItemText() {
            // Given
            when(itemListMock.areVisible(container, "item1", "item2")).thenReturn(true);

            // When
            boolean result = service.areVisible(componentType, container, "item1", "item2");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(container, "item1", "item2");
        }

        @Test
        @DisplayName("isVisible(componentType, container, itemText) delegates to areVisible")
        void isVisibleContainerItemText() {
            // Given
            when(itemListMock.areVisible(container, "item")).thenReturn(true);

            // When
            boolean result = service.isVisible(componentType, container, "item");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(container, "item");
        }

        @Test
        @DisplayName("areVisible(componentType, locator, itemText) delegates correctly")
        void areVisibleLocatorItemText() {
            // Given
            when(itemListMock.areVisible(locator, "item1", "item2")).thenReturn(true);

            // When
            boolean result = service.areVisible(componentType, locator, "item1", "item2");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(locator, "item1", "item2");
        }

        @Test
        @DisplayName("isVisible(componentType, locator, itemText) delegates to areVisible")
        void isVisibleLocatorItemText() {
            // Given
            when(itemListMock.areVisible(locator, "item")).thenReturn(true);

            // When
            boolean result = service.isVisible(componentType, locator, "item");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(locator, "item");
        }

        @Test
        @DisplayName("areVisible(componentType, itemText) delegates correctly")
        void areVisibleItemText() {
            // Given
            when(itemListMock.areVisible("item1", "item2")).thenReturn(true);

            // When
            boolean result = service.areVisible(componentType, "item1", "item2");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible("item1", "item2");
        }

        @Test
        @DisplayName("isVisible(componentType, itemText) delegates to areVisible")
        void isVisibleItemText() {
            // Given
            when(itemListMock.areVisible("item")).thenReturn(true);

            // When
            boolean result = service.isVisible(componentType, "item");

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible("item");
        }

        @Test
        @DisplayName("areVisible(componentType, itemLocators) delegates correctly")
        void areVisibleItemLocators() {
            // Given
            By locator1 = By.id("item1");
            By locator2 = By.id("item2");
            when(itemListMock.areVisible(locator1, locator2)).thenReturn(true);

            // When
            boolean result = service.areVisible(componentType, locator1, locator2);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(locator1, locator2);
        }

        @Test
        @DisplayName("isVisible(componentType, itemLocator) delegates to areVisible with singleton array")
        void isVisibleItemLocator() {
            // Given
            By locator1 = By.id("item1");
            when(itemListMock.areVisible(any(By[].class))).thenReturn(true);

            // When
            boolean result = service.isVisible(componentType, locator1);

            // Then
            assertThat(result).isTrue();
            // Since we're verifying an array creation, we can't use eq() matcher directly
            verify(itemListMock).areVisible(any(By[].class));
        }
    }

    @Nested
    @DisplayName("Get Methods Tests")
    class GetMethodsTests {

        @Test
        @DisplayName("getSelected(componentType, container) delegates correctly")
        void getSelectedContainer() {
            // Given
            List<String> selectedItems = Arrays.asList("item1", "item2");
            when(itemListMock.getSelected(container)).thenReturn(selectedItems);

            // When
            List<String> result = service.getSelected(componentType, container);

            // Then
            assertThat(result).isEqualTo(selectedItems);
            verify(itemListMock).getSelected(container);
        }

        @Test
        @DisplayName("getSelected(componentType, locator) delegates correctly")
        void getSelectedLocator() {
            // Given
            List<String> selectedItems = Arrays.asList("item1", "item2");
            when(itemListMock.getSelected(locator)).thenReturn(selectedItems);

            // When
            List<String> result = service.getSelected(componentType, locator);

            // Then
            assertThat(result).isEqualTo(selectedItems);
            verify(itemListMock).getSelected(locator);
        }

        @Test
        @DisplayName("getAll(componentType, container) delegates correctly")
        void getAllContainer() {
            // Given
            List<String> allItems = Arrays.asList("item1", "item2", "item3");
            when(itemListMock.getAll(container)).thenReturn(allItems);

            // When
            List<String> result = service.getAll(componentType, container);

            // Then
            assertThat(result).isEqualTo(allItems);
            verify(itemListMock).getAll(container);
        }

        @Test
        @DisplayName("getAll(componentType, locator) delegates correctly")
        void getAllLocator() {
            // Given
            List<String> allItems = Arrays.asList("item1", "item2", "item3");
            when(itemListMock.getAll(locator)).thenReturn(allItems);

            // When
            List<String> result = service.getAll(componentType, locator);

            // Then
            assertThat(result).isEqualTo(allItems);
            verify(itemListMock).getAll(locator);
        }
    }

    @Nested
    @DisplayName("Interface Implementation Tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("insertion method converts parameters and delegates to select")
        void insertionConvertsAndDelegates() {
            // When
            service.insertion(componentType, locator, "item1", 2, true);

            // Then
            verify(itemListMock).select(locator, "item1", "2", "true");
        }
    }
}