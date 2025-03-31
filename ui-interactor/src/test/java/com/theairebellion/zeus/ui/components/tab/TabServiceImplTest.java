package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.accordion.mock.MockSmartWebElement;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.tab.mock.MockTabComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.openqa.selenium.By;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("TabServiceImpl Unit Tests")
class TabServiceImplTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private TabServiceImpl service;
    private SmartWebElement container;
    private SmartWebElement cellElement;
    private MockTabComponentType componentType;
    private Tab tabMock;
    private By locator;
    private MockedStatic<ComponentFactory> factoryMock;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        driver = mock(SmartWebDriver.class);
        service = new TabServiceImpl(driver);
        container = MockSmartWebElement.createMock();
        cellElement = MockSmartWebElement.createMock();
        tabMock = mock(Tab.class);
        componentType = MockTabComponentType.DUMMY_TAB;
        locator = By.id("tab");

        // Configure static mock for ComponentFactory
        factoryMock = mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getTabComponent(eq(componentType), eq(driver)))
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
            var tabText = "Tab1";

            // When
            service.click(componentType, container, tabText);

            // Then
            verify(tabMock).click(container, tabText);
        }

        @Test
        @DisplayName("click with container delegates correctly")
        void clickWithContainer() {
            // When
            service.click(componentType, container);

            // Then
            verify(tabMock).click(container);
        }

        @Test
        @DisplayName("click with text only delegates correctly")
        void clickWithTextOnly() {
            // Given
            var tabText = "Tab1";

            // When
            service.click(componentType, tabText);

            // Then
            verify(tabMock).click(tabText);
        }

        @Test
        @DisplayName("click with locator delegates correctly")
        void clickWithLocator() {
            // When
            service.click(componentType, locator);

            // Then
            verify(tabMock).click(locator);
        }
    }

    @Nested
    @DisplayName("IsSelected Method Tests")
    class IsSelectedMethodTests {

        @Test
        @DisplayName("isSelected with container and text delegates correctly")
        void isSelectedWithContainerAndText() {
            // Given
            var tabText = "Tab1";
            when(tabMock.isSelected(container, tabText)).thenReturn(true);

            // When
            var result = service.isSelected(componentType, container, tabText);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isSelected(container, tabText);
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
        }

        @Test
        @DisplayName("isSelected with text only delegates correctly")
        void isSelectedWithTextOnly() {
            // Given
            var tabText = "Tab1";
            when(tabMock.isSelected(tabText)).thenReturn(true);

            // When
            var result = service.isSelected(componentType, tabText);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isSelected(tabText);
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
        }
    }

    @Nested
    @DisplayName("IsEnabled Method Tests")
    class IsEnabledMethodTests {

        @Test
        @DisplayName("isEnabled with container and text delegates correctly")
        void isEnabledWithContainerAndText() {
            // Given
            var tabText = "Tab2";
            when(tabMock.isEnabled(container, tabText)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, container, tabText);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isEnabled(container, tabText);
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
        }

        @Test
        @DisplayName("isEnabled with text only delegates correctly")
        void isEnabledWithTextOnly() {
            // Given
            var tabText = "Tab2";
            when(tabMock.isEnabled(tabText)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, tabText);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isEnabled(tabText);
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
        }
    }

    @Nested
    @DisplayName("IsVisible Method Tests")
    class IsVisibleMethodTests {

        @Test
        @DisplayName("isVisible with container and text delegates correctly")
        void isVisibleWithContainerAndText() {
            // Given
            var tabText = "Tab3";
            when(tabMock.isVisible(container, tabText)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, container, tabText);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isVisible(container, tabText);
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
        }

        @Test
        @DisplayName("isVisible with text only delegates correctly")
        void isVisibleWithTextOnly() {
            // Given
            var tabText = "Tab3";
            when(tabMock.isVisible(tabText)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, tabText);

            // Then
            assertThat(result).isTrue();
            verify(tabMock).isVisible(tabText);
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
        }
    }

    @Test
    @DisplayName("tableInsertion delegates to clickElementInCell correctly")
    void tableInsertion() {
        // When
        service.tableInsertion(cellElement, componentType, "value1", "value2");

        // Then
        verify(tabMock).clickElementInCell(cellElement);
    }

    @Nested
    @DisplayName("Component Caching Tests")
    class ComponentCachingTests {

        @Test
        @DisplayName("Component is cached and reused")
        void componentCaching() {
            // When
            service.isSelected(componentType, container, "Tab1");
            service.isEnabled(componentType, container, "Tab2");
            service.isVisible(componentType, container, "Tab3");

            // Then
            factoryMock.verify(() -> ComponentFactory.getTabComponent(eq(componentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("Different component types create different instances")
        void differentComponentTypes() {
            // Setup mock component types
            componentType = MockTabComponentType.DUMMY_TAB;
            var componentType2 = MockTabComponentType.TEST;

            // Create mock components
            var tab1 = mock(Tab.class);
            var tab2 = mock(Tab.class);

            // Configure behavior
            when(tab1.isSelected(container)).thenReturn(false);
            when(tab2.isSelected(container)).thenReturn(true);

            // Configure factory mock
            factoryMock.reset();
            factoryMock.when(() -> ComponentFactory.getTabComponent(eq(componentType), eq(driver)))
                    .thenReturn(tab1);
            factoryMock.when(() -> ComponentFactory.getTabComponent(eq(componentType2), eq(driver)))
                    .thenReturn(tab2);

            // First component type operation
            var result1 = service.isSelected(componentType, container);
            assertThat(result1).isFalse();
            verify(tab1).isSelected(container);

            // Second component type operation
            var result2 = service.isSelected(componentType2, container);
            assertThat(result2).isTrue();
            verify(tab2).isSelected(container);

            // Verify factory calls
            factoryMock.verify(() -> ComponentFactory.getTabComponent(eq(componentType), eq(driver)), times(1));
            factoryMock.verify(() -> ComponentFactory.getTabComponent(eq(componentType2), eq(driver)), times(1));
        }
    }
}