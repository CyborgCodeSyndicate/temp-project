package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.link.mock.MockLinkComponentType;
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

@DisplayName("LinkServiceImpl Unit Tests")
class LinkServiceImplTest extends BaseUnitUITest {

    @Mock private SmartWebDriver driver;
    @Mock private SmartWebElement container;
    @Mock private SmartWebElement cellElement;
    @Mock private Link linkMock;
    @Mock private By locator;

    private LinkServiceImpl service;
    private MockedStatic<ComponentFactory> factoryMock;
    private final MockLinkComponentType componentType = MockLinkComponentType.DUMMY_LINK;

    private static final String LINK_TEXT = "LinkText";
    private static final String[] TABLE_VALUES = {"value1", "value2"};

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new LinkServiceImpl(driver);
        locator = By.id("link");

        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getLinkComponent(any(LinkComponentType.class), eq(driver)))
                .thenReturn(linkMock);
    }

    @AfterEach
    void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Nested
    @DisplayName("Click Method Tests (Inherited/Overridden)")
    class ClickMethodTests {

        @Test
        @DisplayName("click with container and text delegates correctly")
        void clickWithContainerAndText() {
            // Given

            // When
            service.click(componentType, container, LINK_TEXT);

            // Then
            verify(linkMock).click(container, LINK_TEXT);
            verifyNoMoreInteractions(linkMock);
        }

        @Test
        @DisplayName("click with container delegates correctly")
        void clickWithContainer() {
            // Given

            // When
            service.click(componentType, container);

            // Then
            verify(linkMock).click(container);
            verifyNoMoreInteractions(linkMock);
        }

        @Test
        @DisplayName("click with text only delegates correctly")
        void clickWithTextOnly() {
            // Given

            // When
            service.click(componentType, LINK_TEXT);

            // Then
            verify(linkMock).click(LINK_TEXT);
            verifyNoMoreInteractions(linkMock);
        }

        @Test
        @DisplayName("click with locator delegates correctly")
        void clickWithLocator() {
            // Given

            // When
            service.click(componentType, locator);

            // Then
            verify(linkMock).click(locator);
            verifyNoMoreInteractions(linkMock);
        }
    }

    @Nested
    @DisplayName("Double Click Method Tests")
    class DoubleClickMethodTests {

        @Test
        @DisplayName("doubleClick with container and text delegates correctly")
        void doubleClickWithContainerAndText() {
            // Given

            // When
            service.doubleClick(componentType, container, LINK_TEXT);

            // Then
            verify(linkMock).doubleClick(container, LINK_TEXT);
            verifyNoMoreInteractions(linkMock);
        }

        @Test
        @DisplayName("doubleClick with container delegates correctly")
        void doubleClickWithContainer() {
            // Given

            // When
            service.doubleClick(componentType, container);

            // Then
            verify(linkMock).doubleClick(container);
            verifyNoMoreInteractions(linkMock);
        }

        @Test
        @DisplayName("doubleClick with text only delegates correctly")
        void doubleClickWithTextOnly() {
            // Given

            // When
            service.doubleClick(componentType, LINK_TEXT);

            // Then
            verify(linkMock).doubleClick(LINK_TEXT);
            verifyNoMoreInteractions(linkMock);
        }

        @Test
        @DisplayName("doubleClick with locator delegates correctly")
        void doubleClickWithLocator() {
            // Given

            // When
            service.doubleClick(componentType, locator);

            // Then
            verify(linkMock).doubleClick(locator);
            verifyNoMoreInteractions(linkMock);
        }
    }

    @Nested
    @DisplayName("IsEnabled Method Tests (Inherited/Overridden)")
    class IsEnabledMethodTests {

        @Test
        @DisplayName("isEnabled with container and text delegates correctly")
        void isEnabledWithContainerAndText() {
            // Given
            when(linkMock.isEnabled(container, LINK_TEXT)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, container, LINK_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(linkMock).isEnabled(container, LINK_TEXT);
            verifyNoMoreInteractions(linkMock);
        }

        @Test
        @DisplayName("isEnabled with container delegates correctly")
        void isEnabledWithContainer() {
            // Given
            when(linkMock.isEnabled(container)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, container);

            // Then
            assertThat(result).isTrue();
            verify(linkMock).isEnabled(container);
            verifyNoMoreInteractions(linkMock);
        }

        @Test
        @DisplayName("isEnabled with text only delegates correctly")
        void isEnabledWithTextOnly() {
            // Given
            when(linkMock.isEnabled(LINK_TEXT)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, LINK_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(linkMock).isEnabled(LINK_TEXT);
            verifyNoMoreInteractions(linkMock);
        }

        @Test
        @DisplayName("isEnabled with locator delegates correctly")
        void isEnabledWithLocator() {
            // Given
            when(linkMock.isEnabled(locator)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(linkMock).isEnabled(locator);
            verifyNoMoreInteractions(linkMock);
        }
    }

    @Nested
    @DisplayName("IsVisible Method Tests (Inherited/Overridden)")
    class IsVisibleMethodTests {

        @Test
        @DisplayName("isVisible with container and text delegates correctly")
        void isVisibleWithContainerAndText() {
            // Given
            when(linkMock.isVisible(container, LINK_TEXT)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, container, LINK_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(linkMock).isVisible(container, LINK_TEXT);
            verifyNoMoreInteractions(linkMock);
        }

        @Test
        @DisplayName("isVisible with container delegates correctly")
        void isVisibleWithContainer() {
            // Given
            when(linkMock.isVisible(container)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, container);

            // Then
            assertThat(result).isTrue();
            verify(linkMock).isVisible(container);
            verifyNoMoreInteractions(linkMock);
        }

        @Test
        @DisplayName("isVisible with text only delegates correctly")
        void isVisibleWithTextOnly() {
            // Given
            when(linkMock.isVisible(LINK_TEXT)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, LINK_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(linkMock).isVisible(LINK_TEXT);
            verifyNoMoreInteractions(linkMock);
        }

        @Test
        @DisplayName("isVisible with locator delegates correctly")
        void isVisibleWithLocator() {
            // Given
            when(linkMock.isVisible(locator)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(linkMock).isVisible(locator);
            verifyNoMoreInteractions(linkMock);
        }
    }

    @Nested
    @DisplayName("TableInsertion Method Tests")
    class TableInsertionMethodTests {
        @Test
        @DisplayName("tableInsertion delegates to link component's clickElementInCell")
        void tableInsertion() {
            // Given

            // When
            service.tableInsertion(cellElement, componentType, TABLE_VALUES);

            // Then
            verify(linkMock).clickElementInCell(cellElement);
            factoryMock.verify(() -> ComponentFactory.getLinkComponent(eq(componentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("tableInsertion with non-LinkComponentType throws exception")
        void testTableInsertionWithNonButtonComponentType() {
            // Given
            ComponentType nonLinkType = mock(ComponentType.class);

            // When / Then
            assertThatThrownBy(() -> service.tableInsertion(cellElement, nonLinkType, TABLE_VALUES))
                    .isInstanceOf(IllegalArgumentException.class);

            factoryMock.verifyNoInteractions();
            verify(linkMock, never()).clickElementInCell(any());
        }
    }

    @Nested
    @DisplayName("Component Caching Tests")
    class ComponentCachingTests {

        @Test
        @DisplayName("Component is cached and reused")
        void componentCaching() {
            // Given

            // When
            service.click(componentType, container, LINK_TEXT);
            service.isEnabled(componentType, locator);

            // Then
            factoryMock.verify(() -> ComponentFactory.getLinkComponent(eq(componentType), eq(driver)), times(1));
        }
    }
}