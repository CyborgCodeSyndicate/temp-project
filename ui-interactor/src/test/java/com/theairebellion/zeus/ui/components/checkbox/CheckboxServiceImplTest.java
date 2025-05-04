package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.checkbox.mock.MockCheckboxComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("CheckboxServiceImpl Test")
class CheckboxServiceImplTest extends BaseUnitUITest {

    @Mock private SmartWebDriver driver;
    @Mock private SmartWebElement container;
    @Mock private Checkbox checkboxMock;
    @Mock private By locator;
    @Mock private Strategy strategy;

    private CheckboxServiceImpl service;
    private MockedStatic<ComponentFactory> factoryMock;
    private final MockCheckboxComponentType mockCheckboxComponentType = MockCheckboxComponentType.DUMMY_CHECKBOX;

    private static final String[] CHECKBOX_TEXT_ARRAY = {"A", "B"};
    private static final String SINGLE_CHECKBOX_TEXT = "A";
    private static final String STRATEGY_SELECT_RESULT = "StrategySelected";
    private static final String STRATEGY_DESELECT_RESULT = "StrategyDeSelected";
    private static final List<String> SAMPLE_LIST = List.of("A", "B");
    private static final String INSERTION_VALUE = "InsertedValue";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CheckboxServiceImpl(driver);
        locator = By.id("checkbox");

        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getCheckBoxComponent(any(CheckboxComponentType.class), eq(driver)))
                .thenReturn(checkboxMock);
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
        @DisplayName("select with container and text delegates to component correctly")
        void testSelectWithContainerAndText() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.select(mockCheckboxComponentType, container, CHECKBOX_TEXT_ARRAY);

            // Then
            verify(checkboxMock).select(container, CHECKBOX_TEXT_ARRAY);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("select with container and strategy delegates to component correctly")
        void testSelectWithContainerAndStrategy() {
            // Given
            when(checkboxMock.select(container, strategy)).thenReturn(STRATEGY_SELECT_RESULT);

            // When
            var result = service.select(mockCheckboxComponentType, container, strategy);

            // Then
            assertThat(result).isEqualTo(STRATEGY_SELECT_RESULT);
            verify(checkboxMock).select(container, strategy);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("select with text only delegates to component correctly")
        void testSelectWithTextOnly() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.select(mockCheckboxComponentType, SINGLE_CHECKBOX_TEXT);

            // Then
            verify(checkboxMock).select(SINGLE_CHECKBOX_TEXT);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("select with locator delegates to component correctly")
        void testSelectWithLocator() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.select(mockCheckboxComponentType, locator);

            // Then
            verify(checkboxMock).select(locator);
            verifyNoMoreInteractions(checkboxMock);
        }
    }

    @Nested
    @DisplayName("DeSelect Method Tests")
    class DeSelectMethodTests {

        @Test
        @DisplayName("deSelect with container and text delegates to component correctly")
        void testDeSelectWithContainerAndText() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.deSelect(mockCheckboxComponentType, container, CHECKBOX_TEXT_ARRAY);

            // Then
            verify(checkboxMock).deSelect(container, CHECKBOX_TEXT_ARRAY);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("deSelect with container and strategy delegates to component correctly")
        void testDeSelectWithContainerAndStrategy() {
            // Given
            when(checkboxMock.deSelect(container, strategy)).thenReturn(STRATEGY_DESELECT_RESULT);

            // When
            var result = service.deSelect(mockCheckboxComponentType, container, strategy);

            // Then
            assertThat(result).isEqualTo(STRATEGY_DESELECT_RESULT);
            verify(checkboxMock).deSelect(container, strategy);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("deSelect with text only delegates to component correctly")
        void testDeSelectWithTextOnly() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.deSelect(mockCheckboxComponentType, SINGLE_CHECKBOX_TEXT);

            // Then
            verify(checkboxMock).deSelect(SINGLE_CHECKBOX_TEXT);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("deSelect with locator delegates to component correctly")
        void testDeSelectWithLocator() {
            // Given - Mocks setup in @BeforeEach

            // When
            service.deSelect(mockCheckboxComponentType, locator);

            // Then
            verify(checkboxMock).deSelect(locator);
            verifyNoMoreInteractions(checkboxMock);
        }
    }

    @Nested
    @DisplayName("AreSelected/IsSelected Method Tests")
    class SelectedMethodTests {

        @Test
        @DisplayName("areSelected with container and text delegates to component correctly")
        void testAreSelectedWithContainerAndText() {
            // Given
            when(checkboxMock.areSelected(container, CHECKBOX_TEXT_ARRAY)).thenReturn(true);

            // When
            var result = service.areSelected(mockCheckboxComponentType, container, CHECKBOX_TEXT_ARRAY);

            // Then
            assertThat(result).isTrue();
            verify(checkboxMock).areSelected(container, CHECKBOX_TEXT_ARRAY);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("areSelected with text only delegates to component correctly")
        void testAreSelectedWithTextOnly() {
            // Given
            when(checkboxMock.areSelected(SINGLE_CHECKBOX_TEXT)).thenReturn(true);

            // When
            var result = service.areSelected(mockCheckboxComponentType, SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(checkboxMock).areSelected(SINGLE_CHECKBOX_TEXT);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("areSelected with locator delegates to component correctly")
        void testAreSelectedWithLocator() {
            // Given
            when(checkboxMock.areSelected(locator)).thenReturn(true);

            // When
            var result = service.areSelected(mockCheckboxComponentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(checkboxMock).areSelected(locator);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("isSelected with container and text delegates to component correctly")
        void testIsSelectedWithContainer() {
            // Given
            when(checkboxMock.areSelected(container, SINGLE_CHECKBOX_TEXT)).thenReturn(true);

            // When
            var result = service.isSelected(mockCheckboxComponentType, container, SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(checkboxMock).areSelected(container, SINGLE_CHECKBOX_TEXT);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("isSelected with text only delegates to component correctly")
        void testIsSelectedWithTextOnly() {
            // Given
            when(checkboxMock.areSelected(SINGLE_CHECKBOX_TEXT)).thenReturn(true);

            // When
            var result = service.isSelected(mockCheckboxComponentType, SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(checkboxMock).areSelected(SINGLE_CHECKBOX_TEXT);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("isSelected with locator delegates to component correctly")
        void testIsSelectedWithLocator() {
            // Given
            when(checkboxMock.areSelected(locator)).thenReturn(true);

            // When
            var result = service.isSelected(mockCheckboxComponentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(checkboxMock).areSelected(locator);
            verifyNoMoreInteractions(checkboxMock);
        }
    }


    @Nested
    @DisplayName("AreEnabled/IsEnabled Method Tests")
    class EnabledMethodTests {

        @Test
        @DisplayName("areEnabled with container and text delegates to component correctly")
        void testAreEnabledWithContainerAndText() {
            // Given
            when(checkboxMock.areEnabled(container, CHECKBOX_TEXT_ARRAY)).thenReturn(true);

            // When
            var result = service.areEnabled(mockCheckboxComponentType, container, CHECKBOX_TEXT_ARRAY);

            // Then
            assertThat(result).isTrue();
            verify(checkboxMock).areEnabled(container, CHECKBOX_TEXT_ARRAY);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("areEnabled with text only delegates to component correctly")
        void testAreEnabledWithTextOnly() {
            // Given
            when(checkboxMock.areEnabled(SINGLE_CHECKBOX_TEXT)).thenReturn(true);

            // When
            var result = service.areEnabled(mockCheckboxComponentType, SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(checkboxMock).areEnabled(SINGLE_CHECKBOX_TEXT);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("areEnabled with locator delegates to component correctly")
        void testAreEnabledWithLocator() {
            // Given
            when(checkboxMock.areEnabled(locator)).thenReturn(true);

            // When
            var result = service.areEnabled(mockCheckboxComponentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(checkboxMock).areEnabled(locator);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("isEnabled with container and text delegates to component correctly")
        void testIsEnabledWithContainer() {
            // Given
            when(checkboxMock.areEnabled(container, SINGLE_CHECKBOX_TEXT)).thenReturn(true);

            // When
            var result = service.isEnabled(mockCheckboxComponentType, container, SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(checkboxMock).areEnabled(container, SINGLE_CHECKBOX_TEXT);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("isEnabled with text only delegates to component correctly")
        void testIsEnabledWithTextOnly() {
            // Given
            when(checkboxMock.areEnabled(SINGLE_CHECKBOX_TEXT)).thenReturn(true);

            // When
            var result = service.isEnabled(mockCheckboxComponentType, SINGLE_CHECKBOX_TEXT);

            // Then
            assertThat(result).isTrue();
            verify(checkboxMock).areEnabled(SINGLE_CHECKBOX_TEXT);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("isEnabled with locator delegates to component correctly")
        void testIsEnabledWithLocator() {
            // Given
            when(checkboxMock.areEnabled(locator)).thenReturn(true);

            // When
            var result = service.isEnabled(mockCheckboxComponentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(checkboxMock).areEnabled(locator);
            verifyNoMoreInteractions(checkboxMock);
        }
    }

    @Nested
    @DisplayName("Get Methods Tests")
    class GetMethodsTests {

        @Test
        @DisplayName("getSelected with container delegates to component correctly")
        void testGetSelectedWithContainer() {
            // Given
            when(checkboxMock.getSelected(container)).thenReturn(SAMPLE_LIST);

            // When
            var result = service.getSelected(mockCheckboxComponentType, container);

            // Then
            assertThat(result).isEqualTo(SAMPLE_LIST);
            verify(checkboxMock).getSelected(container);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("getSelected with locator delegates to component correctly")
        void testGetSelectedWithLocator() {
            // Given
            when(checkboxMock.getSelected(locator)).thenReturn(SAMPLE_LIST);

            // When
            var result = service.getSelected(mockCheckboxComponentType, locator);

            // Then
            assertThat(result).isEqualTo(SAMPLE_LIST);
            verify(checkboxMock).getSelected(locator);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("getAll with container delegates to component correctly")
        void testGetAllWithContainer() {
            // Given
            when(checkboxMock.getAll(container)).thenReturn(SAMPLE_LIST);

            // When
            var result = service.getAll(mockCheckboxComponentType, container);

            // Then
            assertThat(result).isEqualTo(SAMPLE_LIST);
            verify(checkboxMock).getAll(container);
            verifyNoMoreInteractions(checkboxMock);
        }

        @Test
        @DisplayName("getAll with locator delegates to component correctly")
        void testGetAllWithLocator() {
            // Given
            when(checkboxMock.getAll(locator)).thenReturn(SAMPLE_LIST);

            // When
            var result = service.getAll(mockCheckboxComponentType, locator);

            // Then
            assertThat(result).isEqualTo(SAMPLE_LIST);
            verify(checkboxMock).getAll(locator);
            verifyNoMoreInteractions(checkboxMock);
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
            service.select(mockCheckboxComponentType, container, CHECKBOX_TEXT_ARRAY);
            service.deSelect(mockCheckboxComponentType, container, CHECKBOX_TEXT_ARRAY);

            // Then
            factoryMock.verify(() -> ComponentFactory.getCheckBoxComponent(eq(mockCheckboxComponentType), eq(driver)), times(1));
        }
    }

    @Nested
    @DisplayName("Insertion Method Tests")
    class InsertionMethodTests {

        @Test
        @DisplayName("insertion method delegates to select method correctly")
        void testInsertion() {
            // Given - setup in @BeforeEach

            // When
            service.insertion(mockCheckboxComponentType, locator, INSERTION_VALUE);

            // Then
            // Insertion calls select(type, locator, values), which delegates to component.select(locator, values)
            // However, the CheckboxServiceImpl implementation calls select(type, text...)
            // Let's verify based on the provided impl: insertion -> select(type, text...)
            verify(checkboxMock).select(INSERTION_VALUE);
            factoryMock.verify(() -> ComponentFactory.getCheckBoxComponent(eq(mockCheckboxComponentType), eq(driver)), times(1));

        }

        @Test
        @DisplayName("insertion with non-CheckboxComponentType throws ClassCastException")
        void testInsertionWithNonCheckboxComponentType() {
            // Given
            ComponentType nonCheckboxType = mock(ComponentType.class);

            // When / Then
            assertThatThrownBy(() -> service.insertion(nonCheckboxType, locator, INSERTION_VALUE))
                    .isInstanceOf(IllegalArgumentException.class) // Based on impl check
                    .hasMessageContaining("Component type needs to be from: CheckboxComponentType");

            // Verify component wasn't created/retrieved for this invalid type
            factoryMock.verifyNoInteractions();
            verify(checkboxMock, never()).select(anyString());
            verify(checkboxMock, never()).select(any(By[].class));
        }
    }


    @Nested
    @DisplayName("Null Handling")
    class NullHandlingTests {

        @Test
        @DisplayName("select with null container delegates correctly")
        void testSelectWithNullContainer() {
            // Given
            SmartWebElement nullContainer = null;

            // When
            service.select(mockCheckboxComponentType, nullContainer, CHECKBOX_TEXT_ARRAY);

            // Then
            verify(checkboxMock).select(nullContainer, CHECKBOX_TEXT_ARRAY);
        }

        @Test
        @DisplayName("select with null text delegates correctly")
        void testSelectWithNullText() {
            // Given
            String[] nullTexts = null;

            // When
            service.select(mockCheckboxComponentType, container, nullTexts);

            // Then
            verify(checkboxMock).select(container, (String[]) null);
        }

        @Test
        @DisplayName("areSelected with null container delegates correctly")
        void testAreSelectedWithNullContainer() {
            // Given
            SmartWebElement nullContainer = null;
            when(checkboxMock.areSelected(nullContainer, CHECKBOX_TEXT_ARRAY)).thenReturn(false);

            // When
            var result = service.areSelected(mockCheckboxComponentType, nullContainer, CHECKBOX_TEXT_ARRAY);

            // Then
            assertThat(result).isFalse();
            verify(checkboxMock).areSelected(nullContainer, CHECKBOX_TEXT_ARRAY);
        }

        @Test
        @DisplayName("areSelected with null text delegates correctly")
        void testAreSelectedWithNullText() {
            // Given
            String[] nullTexts = null;
            when(checkboxMock.areSelected(container, nullTexts)).thenReturn(false);

            // When
            var result = service.areSelected(mockCheckboxComponentType, container, nullTexts);

            // Then
            assertThat(result).isFalse();
            verify(checkboxMock).areSelected(container, (String[]) null);
        }

        @Test
        @DisplayName("getSelected with null container delegates correctly")
        void testGetSelectedWithNullContainer() {
            // Given
            SmartWebElement nullContainer = null;
            when(checkboxMock.getSelected(nullContainer)).thenReturn(Collections.emptyList());

            // When
            var result = service.getSelected(mockCheckboxComponentType, nullContainer);

            // Then
            assertThat(result).isEmpty();
            verify(checkboxMock).getSelected(nullContainer);
        }
    }
}