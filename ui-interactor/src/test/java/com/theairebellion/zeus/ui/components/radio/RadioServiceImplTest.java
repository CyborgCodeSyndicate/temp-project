package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.radio.mock.MockRadioComponentType;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("RadioServiceImpl Tests")
class RadioServiceImplTest extends BaseUnitUITest {

    @Mock private SmartWebDriver driver;
    @Mock private SmartWebElement container;
    @Mock private By locator;
    @Mock private Strategy strategy;
    @Mock private Radio radioMock;

    private RadioServiceImpl service;
    private MockedStatic<ComponentFactory> factoryMock;
    private final MockRadioComponentType componentType = MockRadioComponentType.DUMMY_RADIO;

    private static final String RADIO_OPTION_1 = "Option1";
    private static final String SELECTED_OPTION_TEXT = "SelectedOption";
    private static final List<String> ALL_OPTIONS = Arrays.asList("Option1", "Option2", "Option3");


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new RadioServiceImpl(driver);
        locator = By.id("radio");

        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getRadioComponent(any(RadioComponentType.class), eq(driver)))
                .thenReturn(radioMock);
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
        @DisplayName("select(componentType, container, radioButtonText) delegates correctly")
        void selectContainerText() {
            // Given - Setup in BeforeEach

            // When
            service.select(componentType, container, RADIO_OPTION_1);

            // Then
            verify(radioMock).select(container, RADIO_OPTION_1);
            verifyNoMoreInteractions(radioMock);
        }

        @Test
        @DisplayName("select(componentType, container, strategy) delegates correctly")
        void selectContainerStrategy() {
            // Given
            when(radioMock.select(container, strategy)).thenReturn(SELECTED_OPTION_TEXT);

            // When
            var result = service.select(componentType, container, strategy);

            // Then
            assertThat(result).isEqualTo(SELECTED_OPTION_TEXT);
            verify(radioMock).select(container, strategy);
            verifyNoMoreInteractions(radioMock);
        }

        @Test
        @DisplayName("select(componentType, radioButtonText) delegates correctly")
        void selectText() {
            // Given - Setup in BeforeEach

            // When
            service.select(componentType, RADIO_OPTION_1);

            // Then
            verify(radioMock).select(RADIO_OPTION_1);
            verifyNoMoreInteractions(radioMock);
        }

        @Test
        @DisplayName("select(componentType, radioButtonLocator) delegates correctly")
        void selectLocator() {
            // Given - Setup in BeforeEach

            // When
            service.select(componentType, locator);

            // Then
            verify(radioMock).select(locator);
            verifyNoMoreInteractions(radioMock);
        }
    }

    @Nested
    @DisplayName("IsEnabled Method Tests")
    class IsEnabledMethodTests {

        @Test
        @DisplayName("isEnabled(componentType, container, radioButtonText) delegates correctly")
        void isEnabledContainerText() {
            // Given
            when(radioMock.isEnabled(container, RADIO_OPTION_1)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, container, RADIO_OPTION_1);

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isEnabled(container, RADIO_OPTION_1);
            verifyNoMoreInteractions(radioMock);
        }

        @Test
        @DisplayName("isEnabled(componentType, radioButtonText) delegates correctly")
        void isEnabledText() {
            // Given
            when(radioMock.isEnabled(RADIO_OPTION_1)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, RADIO_OPTION_1);

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isEnabled(RADIO_OPTION_1);
            verifyNoMoreInteractions(radioMock);
        }

        @Test
        @DisplayName("isEnabled(componentType, radioButtonLocator) delegates correctly")
        void isEnabledLocator() {
            // Given
            when(radioMock.isEnabled(locator)).thenReturn(true);

            // When
            var result = service.isEnabled(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isEnabled(locator);
            verifyNoMoreInteractions(radioMock);
        }
    }

    @Nested
    @DisplayName("IsSelected Method Tests")
    class IsSelectedMethodTests {

        @Test
        @DisplayName("isSelected(componentType, container, radioButtonText) delegates correctly")
        void isSelectedContainerText() {
            // Given
            when(radioMock.isSelected(container, RADIO_OPTION_1)).thenReturn(true);

            // When
            var result = service.isSelected(componentType, container, RADIO_OPTION_1);

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isSelected(container, RADIO_OPTION_1);
            verifyNoMoreInteractions(radioMock);
        }

        @Test
        @DisplayName("isSelected(componentType, radioButtonText) delegates correctly")
        void isSelectedText() {
            // Given
            when(radioMock.isSelected(RADIO_OPTION_1)).thenReturn(true);

            // When
            var result = service.isSelected(componentType, RADIO_OPTION_1);

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isSelected(RADIO_OPTION_1);
            verifyNoMoreInteractions(radioMock);
        }

        @Test
        @DisplayName("isSelected(componentType, radioButtonLocator) delegates correctly")
        void isSelectedLocator() {
            // Given
            when(radioMock.isSelected(locator)).thenReturn(true);

            // When
            var result = service.isSelected(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isSelected(locator);
            verifyNoMoreInteractions(radioMock);
        }
    }

    @Nested
    @DisplayName("IsVisible Method Tests")
    class IsVisibleMethodTests {

        @Test
        @DisplayName("isVisible(componentType, container, radioButtonText) delegates correctly")
        void isVisibleContainerText() {
            // Given
            when(radioMock.isVisible(container, RADIO_OPTION_1)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, container, RADIO_OPTION_1);

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isVisible(container, RADIO_OPTION_1);
            verifyNoMoreInteractions(radioMock);
        }

        @Test
        @DisplayName("isVisible(componentType, radioButtonText) delegates correctly")
        void isVisibleText() {
            // Given
            when(radioMock.isVisible(RADIO_OPTION_1)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, RADIO_OPTION_1);

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isVisible(RADIO_OPTION_1);
            verifyNoMoreInteractions(radioMock);
        }

        @Test
        @DisplayName("isVisible(componentType, radioButtonLocator) delegates correctly")
        void isVisibleLocator() {
            // Given
            when(radioMock.isVisible(locator)).thenReturn(true);

            // When
            var result = service.isVisible(componentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(radioMock).isVisible(locator);
            verifyNoMoreInteractions(radioMock);
        }
    }

    @Nested
    @DisplayName("Get Methods Tests")
    class GetMethodsTests {

        @Test
        @DisplayName("getSelected(componentType, container) delegates correctly")
        void getSelectedContainer() {
            // Given
            when(radioMock.getSelected(container)).thenReturn(SELECTED_OPTION_TEXT);

            // When
            var result = service.getSelected(componentType, container);

            // Then
            assertThat(result).isEqualTo(SELECTED_OPTION_TEXT);
            verify(radioMock).getSelected(container);
            verifyNoMoreInteractions(radioMock);
        }

        @Test
        @DisplayName("getSelected(componentType, containerLocator) delegates correctly")
        void getSelectedLocator() {
            // Given
            when(radioMock.getSelected(locator)).thenReturn(SELECTED_OPTION_TEXT);

            // When
            var result = service.getSelected(componentType, locator);

            // Then
            assertThat(result).isEqualTo(SELECTED_OPTION_TEXT);
            verify(radioMock).getSelected(locator);
            verifyNoMoreInteractions(radioMock);
        }

        @Test
        @DisplayName("getAll(componentType, container) delegates correctly")
        void getAllContainer() {
            // Given
            when(radioMock.getAll(container)).thenReturn(ALL_OPTIONS);

            // When
            var result = service.getAll(componentType, container);

            // Then
            assertThat(result).isEqualTo(ALL_OPTIONS);
            verify(radioMock).getAll(container);
            verifyNoMoreInteractions(radioMock);
        }

        @Test
        @DisplayName("getAll(componentType, containerLocator) delegates correctly")
        void getAllLocator() {
            // Given
            when(radioMock.getAll(locator)).thenReturn(ALL_OPTIONS);

            // When
            var result = service.getAll(componentType, locator);

            // Then
            assertThat(result).isEqualTo(ALL_OPTIONS);
            verify(radioMock).getAll(locator);
            verifyNoMoreInteractions(radioMock);
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
            service.select(componentType, RADIO_OPTION_1);
            service.isSelected(componentType, RADIO_OPTION_1);

            // Then
            factoryMock.verify(() -> ComponentFactory.getRadioComponent(eq(componentType), eq(driver)), times(1));
        }
    }

    @Nested
    @DisplayName("Interface Implementation Tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("insertion method delegates to select with radioButtonText")
        void insertionDelegatesToSelect() {
            // Given - setup in @BeforeEach

            // When
            service.insertion(componentType, locator, RADIO_OPTION_1);

            // Then
            // Based on implementation: insertion -> select(type, text) -> component.select(text)
            verify(radioMock).select(RADIO_OPTION_1);
            verifyNoMoreInteractions(radioMock);
            factoryMock.verify(() -> ComponentFactory.getRadioComponent(eq(componentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("insertion with non-RadioComponentType throws exception")
        void testInsertionWithNonRadioComponentType() {
            // Given
            ComponentType nonRadioType = mock(ComponentType.class);

            // When / Then
            assertThatThrownBy(() -> service.insertion(nonRadioType, locator, RADIO_OPTION_1))
                    .isInstanceOf(ClassCastException.class); // Direct cast in implementation

            factoryMock.verifyNoInteractions();
            verifyNoInteractions(radioMock);
        }
    }
}