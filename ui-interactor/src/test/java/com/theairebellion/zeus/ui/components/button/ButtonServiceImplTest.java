package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.mock.MockButtonComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
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

@DisplayName("ButtonServiceImpl Test")
class ButtonServiceImplTest extends BaseUnitUITest {

    @Mock private SmartWebDriver driver;
    @Mock private SmartWebElement container;
    @Mock private Button buttonMock;
    @Mock private By locator;
    @Mock private SmartWebElement cellElement;

    private ButtonServiceImpl service;
    private MockedStatic<ComponentFactory> factoryMock;
    private final MockButtonComponentType mockButtonComponentType = MockButtonComponentType.DUMMY_BUTTON;

    private static final String BUTTON_TEXT = "ClickMe";
    private static final String[] TABLE_VALUES = {"value1", "value2"};


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ButtonServiceImpl(driver);
        locator = By.id("button");

        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getButtonComponent(any(ButtonComponentType.class), eq(driver)))
                .thenReturn(buttonMock);
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
        @DisplayName("click with container and text delegates to component correctly")
        void testClickContainerAndText() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.click(mockButtonComponentType, container, BUTTON_TEXT);

            // Then
            verify(buttonMock).click(container, BUTTON_TEXT);
            verifyNoMoreInteractions(buttonMock);
        }

        @Test
        @DisplayName("click with container delegates to component correctly")
        void testClickContainer() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.click(mockButtonComponentType, container);

            // Then
            verify(buttonMock).click(container);
            verifyNoMoreInteractions(buttonMock);
        }

        @Test
        @DisplayName("click with text delegates to component correctly")
        void testClickString() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.click(mockButtonComponentType, BUTTON_TEXT);

            // Then
            verify(buttonMock).click(BUTTON_TEXT);
            verifyNoMoreInteractions(buttonMock);
        }

        @Test
        @DisplayName("click with locator delegates to component correctly")
        void testClickLocator() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.click(mockButtonComponentType, locator);

            // Then
            verify(buttonMock).click(locator);
            verifyNoMoreInteractions(buttonMock);
        }
    }

    @Nested
    @DisplayName("IsEnabled Method Tests")
    class IsEnabledMethodTests {

        @Test
        @DisplayName("isEnabled with container and text delegates to component correctly")
        void testIsEnabledContainerAndText() {
            // Given
            when(buttonMock.isEnabled(container, BUTTON_TEXT)).thenReturn(true);

            // When
            var result = service.isEnabled(mockButtonComponentType, container, BUTTON_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isEnabled(container, BUTTON_TEXT);
            verifyNoMoreInteractions(buttonMock);
        }

        @Test
        @DisplayName("isEnabled with container delegates to component correctly")
        void testIsEnabledContainer() {
            // Given
            when(buttonMock.isEnabled(container)).thenReturn(true);

            // When
            var result = service.isEnabled(mockButtonComponentType, container);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isEnabled(container);
            verifyNoMoreInteractions(buttonMock);
        }

        @Test
        @DisplayName("isEnabled with text delegates to component correctly")
        void testIsEnabledString() {
            // Given
            when(buttonMock.isEnabled(BUTTON_TEXT)).thenReturn(true);

            // When
            var result = service.isEnabled(mockButtonComponentType, BUTTON_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isEnabled(BUTTON_TEXT);
            verifyNoMoreInteractions(buttonMock);
        }

        @Test
        @DisplayName("isEnabled with locator delegates to component correctly")
        void testIsEnabledLocator() {
            // Given
            when(buttonMock.isEnabled(locator)).thenReturn(true);

            // When
            var result = service.isEnabled(mockButtonComponentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isEnabled(locator);
            verifyNoMoreInteractions(buttonMock);
        }
    }

    @Nested
    @DisplayName("IsVisible Method Tests")
    class IsVisibleMethodTests {

        @Test
        @DisplayName("isVisible with container and text delegates to component correctly")
        void testIsVisibleContainerAndText() {
            // Given
            when(buttonMock.isVisible(container, BUTTON_TEXT)).thenReturn(true);

            // When
            var result = service.isVisible(mockButtonComponentType, container, BUTTON_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isVisible(container, BUTTON_TEXT);
            verifyNoMoreInteractions(buttonMock);
        }

        @Test
        @DisplayName("isVisible with container delegates to component correctly")
        void testIsVisibleContainer() {
            // Given
            when(buttonMock.isVisible(container)).thenReturn(true);

            // When
            var result = service.isVisible(mockButtonComponentType, container);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isVisible(container);
            verifyNoMoreInteractions(buttonMock);
        }

        @Test
        @DisplayName("isVisible with text delegates to component correctly")
        void testIsVisibleString() {
            // Given
            when(buttonMock.isVisible(BUTTON_TEXT)).thenReturn(true);

            // When
            var result = service.isVisible(mockButtonComponentType, BUTTON_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isVisible(BUTTON_TEXT);
            verifyNoMoreInteractions(buttonMock);
        }

        @Test
        @DisplayName("isVisible with locator delegates to component correctly")
        void testIsVisibleLocator() {
            // Given
            when(buttonMock.isVisible(locator)).thenReturn(true);

            // When
            var result = service.isVisible(mockButtonComponentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(buttonMock).isVisible(locator);
            verifyNoMoreInteractions(buttonMock);
        }
    }

    @Nested
    @DisplayName("Component Caching Tests")
    class ComponentCachingTests {

        @Test
        @DisplayName("Component is cached and reused between method calls")
        void testComponentCaching() {
            // Given - setup in @BeforeEach

            // When
            service.click(mockButtonComponentType, container, BUTTON_TEXT);
            service.isEnabled(mockButtonComponentType, container);

            // Then
            factoryMock.verify(() -> ComponentFactory.getButtonComponent(eq(mockButtonComponentType), eq(driver)), times(1));
        }
    }

    @Nested
    @DisplayName("TableInsertion Method Tests")
    class TableInsertionMethodTests {

        @Test
        @DisplayName("tableInsertion delegates to button component's clickElementInCell")
        void testTableInsertion() {
            // Given - cellElement mock setup in class level mocks

            // When
            service.tableInsertion(cellElement, mockButtonComponentType, TABLE_VALUES);

            // Then
            verify(buttonMock).clickElementInCell(cellElement);
            factoryMock.verify(() -> ComponentFactory.getButtonComponent(eq(mockButtonComponentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("tableInsertion with non-ButtonComponentType throws exception")
        void testTableInsertionWithNonButtonComponentType() {
            // Given
            ComponentType nonButtonType = mock(ComponentType.class);

            // When / Then
            assertThatThrownBy(() -> service.tableInsertion(cellElement, nonButtonType, TABLE_VALUES))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Component type needs to be from: ButtonComponentType");

            // Verify component wasn't created/retrieved for this invalid type
            factoryMock.verifyNoInteractions();
            verify(buttonMock, never()).clickElementInCell(any());
        }
    }

    @Nested
    @DisplayName("Null Handling")
    class NullHandlingTests {

        @Test
        @DisplayName("click with null container delegates to component")
        void testClickWithNullContainer() {
            // Given
            SmartWebElement nullContainer = null;

            // When
            service.click(mockButtonComponentType, nullContainer, BUTTON_TEXT);

            // Then
            verify(buttonMock).click(nullContainer, BUTTON_TEXT);
        }

        @Test
        @DisplayName("click with null text delegates to component")
        void testClickWithNullText() {
            // Given
            String nullText = null;

            // When
            service.click(mockButtonComponentType, container, nullText);

            // Then
            verify(buttonMock).click(container, nullText);
        }

        @Test
        @DisplayName("isEnabled with null container delegates to component")
        void testIsEnabledWithNullContainer() {
            // Given
            SmartWebElement nullContainer = null;
            when(buttonMock.isEnabled(nullContainer, BUTTON_TEXT)).thenReturn(false); // example return

            // When
            var result = service.isEnabled(mockButtonComponentType, nullContainer, BUTTON_TEXT);

            // Then
            assertThat(result).isFalse();
            verify(buttonMock).isEnabled(nullContainer, BUTTON_TEXT);
        }

        @Test
        @DisplayName("isEnabled with null text delegates to component")
        void testIsEnabledWithNullText() {
            // Given
            String nullText = null;
            when(buttonMock.isEnabled(container, nullText)).thenReturn(false);

            // When
            var result = service.isEnabled(mockButtonComponentType, container, nullText);

            // Then
            assertThat(result).isFalse();
            verify(buttonMock).isEnabled(container, nullText);
        }

        @Test
        @DisplayName("isVisible with null container delegates to component")
        void testIsVisibleWithNullContainer() {
            // Given
            SmartWebElement nullContainer = null;
            when(buttonMock.isVisible(nullContainer, BUTTON_TEXT)).thenReturn(false);

            // When
            var result = service.isVisible(mockButtonComponentType, nullContainer, BUTTON_TEXT);

            // Then
            assertThat(result).isFalse();
            verify(buttonMock).isVisible(nullContainer, BUTTON_TEXT);
        }

        @Test
        @DisplayName("isVisible with null text delegates to component")
        void testIsVisibleWithNullText() {
            // Given
            String nullText = null;
            when(buttonMock.isVisible(container, nullText)).thenReturn(false);

            // When
            var result = service.isVisible(mockButtonComponentType, container, nullText);

            // Then
            assertThat(result).isFalse();
            verify(buttonMock).isVisible(container, nullText);
        }
    }
}