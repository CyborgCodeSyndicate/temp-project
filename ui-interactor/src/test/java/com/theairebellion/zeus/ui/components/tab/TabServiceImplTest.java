package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.tab.mock.MockTabComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("TabServiceImpl Unit Tests")
class TabServiceImplTest extends BaseUnitUITest {

    @Mock private SmartWebDriver driver;
    @Mock private SmartWebElement container;
    @Mock private SmartWebElement cellElement;
    @Mock private Tab tabMock;
    @Mock private By locator;

    private TabServiceImpl service;
    private MockedStatic<ComponentFactory> factoryMock;
    private final MockTabComponentType componentType = MockTabComponentType.DUMMY_TAB;

    private static final String TAB_TEXT = "Tab1";
    private static final String[] TABLE_VALUES = {"value1"};


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TabServiceImpl(driver);
        locator = By.id("tab");

        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getTabComponent(any(TabComponentType.class), eq(driver)))
                .thenReturn(tabMock);
    }

    @AfterEach
    void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Nested
    @DisplayName("Click Method Tests")
    class ClickMethodTests {

        @Test
        @DisplayName("click with container and text delegates correctly")
        void clickWithContainerAndText() {
            // Given

            // When
            service.click(componentType, container, TAB_TEXT);

            // Then
            verify(tabMock).click(container, TAB_TEXT);
            verifyNoMoreInteractions(tabMock);
        }

        @Test
        @DisplayName("click with container delegates correctly")
        void clickWithContainer() {
            // Given

            // When
            service.click(componentType, container);

            // Then
            verify(tabMock).click(container);
            verifyNoMoreInteractions(tabMock);
        }

        @Test
        @DisplayName("click with text only delegates correctly")
        void clickWithTextOnly() {
            // Given

            // When
            service.click(componentType, TAB_TEXT);

            // Then
            verify(tabMock).click(TAB_TEXT);
            verifyNoMoreInteractions(tabMock);
        }

        @Test
        @DisplayName("click with locator delegates correctly")
        void clickWithLocator() {
            // Given

            // When
            service.click(componentType, locator);

            // Then
            verify(tabMock).click(locator);
            verifyNoMoreInteractions(tabMock);
        }
    }

    @Nested
    @DisplayName("IsSelected Method Tests")
    class IsSelectedMethodTests {

        @Test
        @DisplayName("isSelected with container and text delegates correctly")
        void isSelectedWithContainerAndText() {
            // Given
            when(tabMock.isSelected(container, TAB_TEXT)).thenReturn(true);

            // When
            var result = service.isSelected(componentType, container, TAB_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isSelected(container, TAB_TEXT);
            verifyNoMoreInteractions(tabMock);
        }

        @Test
        @DisplayName("isSelected with container delegates correctly")
        void isSelectedWithContainer() {
            // Given
            when(tabMock.isSelected(container)).thenReturn(true);

            // When
            var result = service.isSelected(componentType, container);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isSelected(container);
            verifyNoMoreInteractions(tabMock);
        }

        @Test
        @DisplayName("isSelected with text only delegates correctly")
        void isSelectedWithTextOnly() {
            // Given
            when(tabMock.isSelected(TAB_TEXT)).thenReturn(true);

            // When
            var result = service.isSelected(componentType, TAB_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isSelected(TAB_TEXT);
            verifyNoMoreInteractions(tabMock);
        }

        @Test
        @DisplayName("isSelected with locator delegates correctly")
        void isSelectedWithLocator() {
            // Given
            when(tabMock.isSelected(locator)).thenReturn(true);

            // When
            var result = service.isSelected(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isSelected(locator);
            verifyNoMoreInteractions(tabMock);
        }
    }

    @Nested
    @DisplayName("IsEnabled Method Tests")
    class IsEnabledMethodTests {

        @Test
        @DisplayName("isEnabled with container and text delegates correctly")
        void isEnabledWithContainerAndText() {
            // Given
            when(tabMock.isEnabled(container, TAB_TEXT)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, container, TAB_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isEnabled(container, TAB_TEXT);
            verifyNoMoreInteractions(tabMock);
        }

        @Test
        @DisplayName("isEnabled with container delegates correctly")
        void isEnabledWithContainer() {
            // Given
            when(tabMock.isEnabled(container)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, container);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isEnabled(container);
            verifyNoMoreInteractions(tabMock);
        }

        @Test
        @DisplayName("isEnabled with text only delegates correctly")
        void isEnabledWithTextOnly() {
            // Given
            when(tabMock.isEnabled(TAB_TEXT)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, TAB_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isEnabled(TAB_TEXT);
            verifyNoMoreInteractions(tabMock);
        }

        @Test
        @DisplayName("isEnabled with locator delegates correctly")
        void isEnabledWithLocator() {
            // Given
            when(tabMock.isEnabled(locator)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isEnabled(locator);
            verifyNoMoreInteractions(tabMock);
        }
    }

    @Nested
    @DisplayName("IsVisible Method Tests")
    class IsVisibleMethodTests {

        @Test
        @DisplayName("isVisible with container and text delegates correctly")
        void isVisibleWithContainerAndText() {
            // Given
            when(tabMock.isVisible(container, TAB_TEXT)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, container, TAB_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isVisible(container, TAB_TEXT);
            verifyNoMoreInteractions(tabMock);
        }

        @Test
        @DisplayName("isVisible with container delegates correctly")
        void isVisibleWithContainer() {
            // Given
            when(tabMock.isVisible(container)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, container);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isVisible(container);
            verifyNoMoreInteractions(tabMock);
        }

        @Test
        @DisplayName("isVisible with text only delegates correctly")
        void isVisibleWithTextOnly() {
            // Given
            when(tabMock.isVisible(TAB_TEXT)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, TAB_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isVisible(TAB_TEXT);
            verifyNoMoreInteractions(tabMock);
        }

        @Test
        @DisplayName("isVisible with locator delegates correctly")
        void isVisibleWithLocator() {
            // Given
            when(tabMock.isVisible(locator)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isVisible(locator);
            verifyNoMoreInteractions(tabMock);
        }
    }

    @Nested
    @DisplayName("TableInsertion Method Tests")
    class TableInsertionMethodTests {
        @Test
        @DisplayName("tableInsertion delegates to clickElementInCell correctly")
        void tableInsertion() {
            // Given - setup in @BeforeEach

            // When
            service.tableInsertion(cellElement, componentType, TABLE_VALUES);

            // Then
            verify(tabMock).clickElementInCell(cellElement);
            verifyNoMoreInteractions(tabMock);
            factoryMock.verify(() -> ComponentFactory.getTabComponent(eq(componentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("tableInsertion with non-TabComponentType throws exception")
        void testTableInsertionWithNonTabComponentType() {
            // Given
            ComponentType nonTabType = mock(ComponentType.class);

            // When / Then
            assertThatThrownBy(() -> service.tableInsertion(cellElement, nonTabType, TABLE_VALUES))
                    .isInstanceOf(IllegalArgumentException.class);

            factoryMock.verifyNoInteractions();
            verifyNoInteractions(tabMock);
        }
    }


    @Nested
    @DisplayName("Component Caching Tests")
    class ComponentCachingTests {

        @Test
        @DisplayName("Component is cached and reused")
        void componentCaching() {
            // Given - setup in @BeforeEach

            // When
            service.isSelected(componentType, container, TAB_TEXT);
            service.click(componentType, container, TAB_TEXT);

            // Then
            factoryMock.verify(() -> ComponentFactory.getTabComponent(eq(componentType), eq(driver)), times(1));
        }
    }
}