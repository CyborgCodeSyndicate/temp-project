package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.list.mock.MockItemListComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("ItemListServiceImpl Tests")
class ItemListServiceImplTest extends BaseUnitUITest {

    @Mock private SmartWebDriver driver;
    @Mock private SmartWebElement container;
    @Mock private By locator;
    @Mock private Strategy strategy;
    @Mock private ItemList itemListMock;

    private ItemListServiceImpl service;
    private MockedStatic<ComponentFactory> factoryMock;
    private final MockItemListComponentType componentType = MockItemListComponentType.DUMMY_LIST;

    private static final String[] SAMPLE_ITEMS = {"item1", "item2"};
    private static final String SINGLE_ITEM = "item1";
    private static final String MOCK_STRATEGY_RESULT = "mockStrategyResult";
    private static final List<String> MOCK_ITEM_LIST = List.of("A", "B");
    private static final By ITEM_LOCATOR_1 = By.id("item1");
    private static final By ITEM_LOCATOR_2 = By.id("item2");
    private static final By[] SAMPLE_ITEM_LOCATORS = {ITEM_LOCATOR_1, ITEM_LOCATOR_2};
    private static final String INSERTION_VALUE = "InsertedValue";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ItemListServiceImpl(driver);
        locator = By.id("list");

        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getListComponent(any(ItemListComponentType.class), eq(driver)))
                .thenReturn(itemListMock);
    }

    @AfterEach
    void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Nested
    @DisplayName("Select Method Tests")
    class SelectMethodTests {

        @Test
        @DisplayName("select(componentType, container, itemText) delegates correctly")
        void selectContainerItemText() {
            // Given

            // When
            service.select(componentType, container, SAMPLE_ITEMS);

            // Then
            verify(itemListMock).select(container, SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("select(componentType, locator, itemText) delegates correctly")
        void selectLocatorItemText() {
            // Given

            // When
            service.select(componentType, locator, SAMPLE_ITEMS);

            // Then
            verify(itemListMock).select(locator, SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("select(componentType, container, strategy) delegates correctly")
        void selectContainerStrategy() {
            // Given
            when(itemListMock.select(container, strategy)).thenReturn(MOCK_STRATEGY_RESULT);

            // When
            var result = service.select(componentType, container, strategy);

            // Then
            assertThat(result).isEqualTo(MOCK_STRATEGY_RESULT);
            verify(itemListMock).select(container, strategy);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("select(componentType, locator, strategy) delegates correctly")
        void selectLocatorStrategy() {
            // Given
            when(itemListMock.select(locator, strategy)).thenReturn(MOCK_STRATEGY_RESULT);

            // When
            var result = service.select(componentType, locator, strategy);

            // Then
            assertThat(result).isEqualTo(MOCK_STRATEGY_RESULT);
            verify(itemListMock).select(locator, strategy);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("select(componentType, itemText) delegates correctly")
        void selectItemText() {
            // Given

            // When
            service.select(componentType, SAMPLE_ITEMS);

            // Then
            verify(itemListMock).select(SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("select(componentType, itemLocators) delegates correctly")
        void selectItemLocators() {
            // Given

            // When
            service.select(componentType, SAMPLE_ITEM_LOCATORS);

            // Then
            verify(itemListMock).select(eq(SAMPLE_ITEM_LOCATORS));
            verifyNoMoreInteractions(itemListMock);
        }
    }

    @Nested
    @DisplayName("DeSelect Method Tests")
    class DeSelectMethodTests {

        @Test
        @DisplayName("deSelect(componentType, container, itemText) delegates correctly")
        void deSelectContainerItemText() {
            // Given

            // When
            service.deSelect(componentType, container, SAMPLE_ITEMS);

            // Then
            verify(itemListMock).deSelect(container, SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("deSelect(componentType, locator, itemText) delegates correctly")
        void deSelectLocatorItemText() {
            // Given

            // When
            service.deSelect(componentType, locator, SAMPLE_ITEMS);

            // Then
            verify(itemListMock).deSelect(locator, SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("deSelect(componentType, container, strategy) delegates correctly")
        void deSelectContainerStrategy() {
            // Given
            when(itemListMock.deSelect(container, strategy)).thenReturn(MOCK_STRATEGY_RESULT);

            // When
            var result = service.deSelect(componentType, container, strategy);

            // Then
            assertThat(result).isEqualTo(MOCK_STRATEGY_RESULT);
            verify(itemListMock).deSelect(container, strategy);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("deSelect(componentType, locator, strategy) delegates correctly")
        void deSelectLocatorStrategy() {
            // Given
            when(itemListMock.deSelect(locator, strategy)).thenReturn(MOCK_STRATEGY_RESULT);

            // When
            var result = service.deSelect(componentType, locator, strategy);

            // Then
            assertThat(result).isEqualTo(MOCK_STRATEGY_RESULT);
            verify(itemListMock).deSelect(locator, strategy);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("deSelect(componentType, itemText) delegates correctly")
        void deSelectItemText() {
            // Given

            // When
            service.deSelect(componentType, SAMPLE_ITEMS);

            // Then
            verify(itemListMock).deSelect(SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("deSelect(componentType, itemLocators) delegates correctly")
        void deSelectItemLocators() {
            // Given

            // When
            service.deSelect(componentType, SAMPLE_ITEM_LOCATORS);

            // Then
            verify(itemListMock).deSelect(eq(SAMPLE_ITEM_LOCATORS));
            verifyNoMoreInteractions(itemListMock);
        }
    }

    @Nested
    @DisplayName("AreSelected/IsSelected Method Tests")
    class AreSelectedIsSelectedMethodTests {

        @Test
        @DisplayName("areSelected(componentType, container, itemText) delegates correctly")
        void areSelectedContainerItemText() {
            // Given
            when(itemListMock.areSelected(container, SAMPLE_ITEMS)).thenReturn(true);

            // When
            var result = service.areSelected(componentType, container, SAMPLE_ITEMS);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(container, SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("isSelected(componentType, container, itemText) delegates to areSelected")
        void isSelectedContainerItemText() {
            // Given
            when(itemListMock.areSelected(container, SINGLE_ITEM)).thenReturn(true);

            // When
            var result = service.isSelected(componentType, container, SINGLE_ITEM);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(container, SINGLE_ITEM);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("areSelected(componentType, locator, itemText) delegates correctly")
        void areSelectedLocatorItemText() {
            // Given
            when(itemListMock.areSelected(locator, SAMPLE_ITEMS)).thenReturn(true);

            // When
            var result = service.areSelected(componentType, locator, SAMPLE_ITEMS);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(locator, SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("isSelected(componentType, locator, itemText) delegates to areSelected")
        void isSelectedLocatorItemText() {
            // Given
            when(itemListMock.areSelected(locator, SINGLE_ITEM)).thenReturn(true);

            // When
            var result = service.isSelected(componentType, locator, SINGLE_ITEM);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(locator, SINGLE_ITEM);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("areSelected(componentType, itemText) delegates correctly")
        void areSelectedItemText() {
            // Given
            when(itemListMock.areSelected(SAMPLE_ITEMS)).thenReturn(true);

            // When
            var result = service.areSelected(componentType, SAMPLE_ITEMS);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("isSelected(componentType, itemText) delegates to areSelected")
        void isSelectedItemText() {
            // Given
            when(itemListMock.areSelected(SINGLE_ITEM)).thenReturn(true);

            // When
            var result = service.isSelected(componentType, SINGLE_ITEM);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(SINGLE_ITEM);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("areSelected(componentType, itemLocators) delegates correctly")
        void areSelectedItemLocators() {
            // Given
            doReturn(true).when(itemListMock).areSelected(eq(SAMPLE_ITEM_LOCATORS));

            // When
            var result = service.areSelected(componentType, SAMPLE_ITEM_LOCATORS);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(eq(SAMPLE_ITEM_LOCATORS));
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("isSelected(componentType, itemLocator) delegates to areSelected")
        void isSelectedItemLocator() {
            // Given
            doReturn(true).when(itemListMock).areSelected(eq(new By[]{ITEM_LOCATOR_1}));

            // When
            var result = service.isSelected(componentType, ITEM_LOCATOR_1);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areSelected(eq(new By[]{ITEM_LOCATOR_1}));
            verifyNoMoreInteractions(itemListMock);
        }
    }

    @Nested
    @DisplayName("AreEnabled/IsEnabled Method Tests")
    class AreEnabledIsEnabledMethodTests {

        @Test
        @DisplayName("areEnabled(componentType, container, itemText) delegates correctly")
        void areEnabledContainerItemText() {
            // Given
            when(itemListMock.areEnabled(container, SAMPLE_ITEMS)).thenReturn(true);

            // When
            var result = service.areEnabled(componentType, container, SAMPLE_ITEMS);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(container, SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("isEnabled(componentType, container, itemText) delegates to areEnabled")
        void isEnabledContainerItemText() {
            // Given
            when(itemListMock.areEnabled(container, SINGLE_ITEM)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, container, SINGLE_ITEM);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(container, SINGLE_ITEM);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("areEnabled(componentType, locator, itemText) delegates correctly")
        void areEnabledLocatorItemText() {
            // Given
            when(itemListMock.areEnabled(locator, SAMPLE_ITEMS)).thenReturn(true);

            // When
            var result = service.areEnabled(componentType, locator, SAMPLE_ITEMS);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(locator, SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("isEnabled(componentType, locator, itemText) delegates to areEnabled")
        void isEnabledLocatorItemText() {
            // Given
            when(itemListMock.areEnabled(locator, SINGLE_ITEM)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, locator, SINGLE_ITEM);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(locator, SINGLE_ITEM);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("areEnabled(componentType, itemText) delegates correctly")
        void areEnabledItemText() {
            // Given
            when(itemListMock.areEnabled(SAMPLE_ITEMS)).thenReturn(true);

            // When
            var result = service.areEnabled(componentType, SAMPLE_ITEMS);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("isEnabled(componentType, itemText) delegates to areEnabled")
        void isEnabledItemText() {
            // Given
            when(itemListMock.areEnabled(SINGLE_ITEM)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, SINGLE_ITEM);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(SINGLE_ITEM);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("areEnabled(componentType, itemLocators) delegates correctly")
        void areEnabledItemLocators() {
            // Given
            doReturn(true).when(itemListMock).areEnabled(eq(SAMPLE_ITEM_LOCATORS));

            // When
            var result = service.areEnabled(componentType, SAMPLE_ITEM_LOCATORS);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(eq(SAMPLE_ITEM_LOCATORS));
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("isEnabled(componentType, itemLocator) delegates to areEnabled")
        void isEnabledItemLocator() {
            // Given
            doReturn(true).when(itemListMock).areEnabled(eq(new By[]{ITEM_LOCATOR_1}));

            // When
            var result = service.isEnabled(componentType, ITEM_LOCATOR_1);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areEnabled(eq(new By[]{ITEM_LOCATOR_1}));
            verifyNoMoreInteractions(itemListMock);
        }
    }

    @Nested
    @DisplayName("AreVisible/IsVisible Method Tests")
    class AreVisibleIsVisibleMethodTests {

        @Test
        @DisplayName("areVisible(componentType, container, itemText) delegates correctly")
        void areVisibleContainerItemText() {
            // Given
            when(itemListMock.areVisible(container, SAMPLE_ITEMS)).thenReturn(true);

            // When
            var result = service.areVisible(componentType, container, SAMPLE_ITEMS);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(container, SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("isVisible(componentType, container, itemText) delegates to areVisible")
        void isVisibleContainerItemText() {
            // Given
            when(itemListMock.areVisible(container, SINGLE_ITEM)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, container, SINGLE_ITEM);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(container, SINGLE_ITEM);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("areVisible(componentType, locator, itemText) delegates correctly")
        void areVisibleLocatorItemText() {
            // Given
            when(itemListMock.areVisible(locator, SAMPLE_ITEMS)).thenReturn(true);

            // When
            var result = service.areVisible(componentType, locator, SAMPLE_ITEMS);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(locator, SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("isVisible(componentType, locator, itemText) delegates to areVisible")
        void isVisibleLocatorItemText() {
            // Given
            when(itemListMock.areVisible(locator, SINGLE_ITEM)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, locator, SINGLE_ITEM);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(locator, SINGLE_ITEM);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("areVisible(componentType, itemText) delegates correctly")
        void areVisibleItemText() {
            // Given
            when(itemListMock.areVisible(SAMPLE_ITEMS)).thenReturn(true);

            // When
            var result = service.areVisible(componentType, SAMPLE_ITEMS);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(SAMPLE_ITEMS);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("isVisible(componentType, itemText) delegates to areVisible")
        void isVisibleItemText() {
            // Given
            when(itemListMock.areVisible(SINGLE_ITEM)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, SINGLE_ITEM);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(SINGLE_ITEM);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("areVisible(componentType, itemLocators) delegates correctly")
        void areVisibleItemLocators() {
            // Given
            doReturn(true).when(itemListMock).areVisible(eq(SAMPLE_ITEM_LOCATORS));

            // When
            var result = service.areVisible(componentType, SAMPLE_ITEM_LOCATORS);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(eq(SAMPLE_ITEM_LOCATORS));
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("isVisible(componentType, itemLocator) delegates to areVisible")
        void isVisibleItemLocator() {
            // Given
            doReturn(true).when(itemListMock).areVisible(eq(new By[]{ITEM_LOCATOR_1}));

            // When
            var result = service.isVisible(componentType, ITEM_LOCATOR_1);

            // Then
            assertThat(result).isTrue();
            verify(itemListMock).areVisible(eq(new By[]{ITEM_LOCATOR_1}));
            verifyNoMoreInteractions(itemListMock);
        }
    }

    @Nested
    @DisplayName("Get Methods Tests")
    class GetMethodsTests {

        @Test
        @DisplayName("getSelected(componentType, container) delegates correctly")
        void getSelectedContainer() {
            // Given
            when(itemListMock.getSelected(container)).thenReturn(MOCK_ITEM_LIST);

            // When
            var result = service.getSelected(componentType, container);

            // Then
            assertThat(result).isEqualTo(MOCK_ITEM_LIST);
            verify(itemListMock).getSelected(container);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("getSelected(componentType, locator) delegates correctly")
        void getSelectedLocator() {
            // Given
            when(itemListMock.getSelected(locator)).thenReturn(MOCK_ITEM_LIST);

            // When
            var result = service.getSelected(componentType, locator);

            // Then
            assertThat(result).isEqualTo(MOCK_ITEM_LIST);
            verify(itemListMock).getSelected(locator);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("getAll(componentType, container) delegates correctly")
        void getAllContainer() {
            // Given
            when(itemListMock.getAll(container)).thenReturn(MOCK_ITEM_LIST);

            // When
            var result = service.getAll(componentType, container);

            // Then
            assertThat(result).isEqualTo(MOCK_ITEM_LIST);
            verify(itemListMock).getAll(container);
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("getAll(componentType, locator) delegates correctly")
        void getAllLocator() {
            // Given
            when(itemListMock.getAll(locator)).thenReturn(MOCK_ITEM_LIST);

            // When
            var result = service.getAll(componentType, locator);

            // Then
            assertThat(result).isEqualTo(MOCK_ITEM_LIST);
            verify(itemListMock).getAll(locator);
            verifyNoMoreInteractions(itemListMock);
        }
    }

    @Nested
    @DisplayName("Component Caching Tests")
    class ComponentCachingTests {

        @Test
        @DisplayName("Component is cached and reused between method calls")
        void testComponentCaching() {
            // Given

            // When
            service.select(componentType, container, SAMPLE_ITEMS);
            service.deSelect(componentType, container, SAMPLE_ITEMS);

            // Then
            factoryMock.verify(() -> ComponentFactory.getListComponent(eq(componentType), eq(driver)), times(1));
        }
    }

    @Nested
    @DisplayName("Insertion Interface Method Tests")
    class InsertionInterfaceMethodTests {

        @Test
        @DisplayName("insertion method converts parameters and delegates to select")
        void testInsertionMethod() {
            // Given
            var values = new Object[]{"item1", 2, true};
            var expectedStrings = new String[]{"item1", "2", "true"};

            // When
            service.insertion(componentType, locator, values);

            // Then
            verify(itemListMock).select(eq(locator), eq(expectedStrings));
            factoryMock.verify(() -> ComponentFactory.getListComponent(eq(componentType), eq(driver)), times(1));
            verifyNoMoreInteractions(itemListMock);
        }

        @Test
        @DisplayName("insertion with non-ItemListComponentType throws exception")
        void testInsertionWithNonListComponentType() {
            // Given
            ComponentType nonListType = mock(ComponentType.class);

            // When / Then
            assertThatThrownBy(() -> service.insertion(nonListType, locator, SAMPLE_ITEMS))
                    .isInstanceOf(ClassCastException.class);

            // Verify factory and component mock were not interacted with
            factoryMock.verifyNoInteractions();
            verifyNoInteractions(itemListMock);
        }
    }
}