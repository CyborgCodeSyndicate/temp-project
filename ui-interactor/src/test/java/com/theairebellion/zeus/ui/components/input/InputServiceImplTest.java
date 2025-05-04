package com.theairebellion.zeus.ui.components.input;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.input.mock.MockInputComponentType;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
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

@DisplayName("InputServiceImpl Test")
class InputServiceImplTest extends BaseUnitUITest {

    @Mock private SmartWebDriver driver;
    @Mock private SmartWebElement container;
    @Mock private Input inputMock;
    @Mock private By locator;
    @Mock private SmartWebElement cell;
    @Mock private SmartWebElement headerCell;
    @Mock private FilterStrategy filterStrategy;

    private InputServiceImpl service;
    private MockedStatic<ComponentFactory> factoryMock;
    private final MockInputComponentType mockInputComponentType = MockInputComponentType.DUMMY_INPUT;

    private static final String SAMPLE_VALUE = "value";
    private static final String SAMPLE_LABEL = "label";
    private static final String SAMPLE_ERROR_MSG = "err";
    private static final String[] SAMPLE_VALUES = {"val1", "val2"};


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new InputServiceImpl(driver);
        locator = By.id("input");

        factoryMock = Mockito.mockStatic(ComponentFactory.class);
        factoryMock.when(() -> ComponentFactory.getInputComponent(any(InputComponentType.class), eq(driver)))
                .thenReturn(inputMock);
    }

    @AfterEach
    void tearDown() {
        if (factoryMock != null) {
            factoryMock.close();
        }
    }

    @Nested
    @DisplayName("Insert Method Tests")
    class InsertMethodTests {

        @Test
        @DisplayName("insert with container delegates correctly")
        void testInsertContainer() {
            // Given - setup in @BeforeEach

            // When
            service.insert(mockInputComponentType, container, SAMPLE_VALUE);

            // Then
            verify(inputMock).insert(container, SAMPLE_VALUE);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("insert with container and label delegates correctly")
        void testInsertContainerLabel() {
            // Given - setup in @BeforeEach

            // When
            service.insert(mockInputComponentType, container, SAMPLE_LABEL, SAMPLE_VALUE);

            // Then
            verify(inputMock).insert(container, SAMPLE_LABEL, SAMPLE_VALUE);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("insert with label delegates correctly")
        void testInsertLabel() {
            // Given - setup in @BeforeEach

            // When
            service.insert(mockInputComponentType, SAMPLE_LABEL, SAMPLE_VALUE);

            // Then
            verify(inputMock).insert(SAMPLE_LABEL, SAMPLE_VALUE);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("insert with locator delegates correctly")
        void testInsertBy() {
            // Given - setup in @BeforeEach

            // When
            service.insert(mockInputComponentType, locator, SAMPLE_VALUE);

            // Then
            verify(inputMock).insert(locator, SAMPLE_VALUE);
            verifyNoMoreInteractions(inputMock);
        }
    }

    @Nested
    @DisplayName("Clear Method Tests")
    class ClearMethodTests {

        @Test
        @DisplayName("clear with container delegates correctly")
        void testClearContainer() {
            // Given - setup in @BeforeEach

            // When
            service.clear(mockInputComponentType, container);

            // Then
            verify(inputMock).clear(container);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("clear with container and label delegates correctly")
        void testClearContainerLabel() {
            // Given - setup in @BeforeEach

            // When
            service.clear(mockInputComponentType, container, SAMPLE_LABEL);

            // Then
            verify(inputMock).clear(container, SAMPLE_LABEL);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("clear with label delegates correctly")
        void testClearLabel() {
            // Given - setup in @BeforeEach

            // When
            service.clear(mockInputComponentType, SAMPLE_LABEL);

            // Then
            verify(inputMock).clear(SAMPLE_LABEL);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("clear with locator delegates correctly")
        void testClearBy() {
            // Given - setup in @BeforeEach

            // When
            service.clear(mockInputComponentType, locator);

            // Then
            verify(inputMock).clear(locator);
            verifyNoMoreInteractions(inputMock);
        }
    }

    @Nested
    @DisplayName("GetValue Method Tests")
    class GetValueMethodTests {

        @Test
        @DisplayName("getValue with container delegates correctly")
        void testGetValueContainer() {
            // Given
            when(inputMock.getValue(container)).thenReturn(SAMPLE_VALUE);

            // When
            var result = service.getValue(mockInputComponentType, container);

            // Then
            assertThat(result).isEqualTo(SAMPLE_VALUE);
            verify(inputMock).getValue(container);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("getValue with container and label delegates to component correctly")
        void testGetValueContainerLabel() {
            // Given
            when(inputMock.getValue(container, SAMPLE_LABEL)).thenReturn(SAMPLE_VALUE);

            // When
            var result = service.getValue(mockInputComponentType, container, SAMPLE_LABEL);

            // Then
            assertThat(result).isEqualTo(SAMPLE_VALUE);
            verify(inputMock).getValue(container, SAMPLE_LABEL);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("getValue with label delegates correctly")
        void testGetValueLabel() {
            // Given
            when(inputMock.getValue(SAMPLE_LABEL)).thenReturn(SAMPLE_VALUE);

            // When
            var result = service.getValue(mockInputComponentType, SAMPLE_LABEL);

            // Then
            assertThat(result).isEqualTo(SAMPLE_VALUE);
            verify(inputMock).getValue(SAMPLE_LABEL);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("getValue with locator delegates correctly")
        void testGetValueBy() {
            // Given
            when(inputMock.getValue(locator)).thenReturn(SAMPLE_VALUE);

            // When
            var result = service.getValue(mockInputComponentType, locator);

            // Then
            assertThat(result).isEqualTo(SAMPLE_VALUE);
            verify(inputMock).getValue(locator);
            verifyNoMoreInteractions(inputMock);
        }
    }

    @Nested
    @DisplayName("IsEnabled Method Tests")
    class IsEnabledMethodTests {

        @Test
        @DisplayName("isEnabled with container delegates correctly")
        void testIsEnabledContainer() {
            // Given
            when(inputMock.isEnabled(container)).thenReturn(true);

            // When
            var result = service.isEnabled(mockInputComponentType, container);

            // Then
            assertThat(result).isTrue();
            verify(inputMock).isEnabled(container);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("isEnabled with container and label delegates correctly")
        void testIsEnabledContainerLabel() {
            // Given
            when(inputMock.isEnabled(container, SAMPLE_LABEL)).thenReturn(true);

            // When
            var result = service.isEnabled(mockInputComponentType, container, SAMPLE_LABEL);

            // Then
            assertThat(result).isTrue();
            verify(inputMock).isEnabled(container, SAMPLE_LABEL);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("isEnabled with label delegates correctly")
        void testIsEnabledLabel() {
            // Given
            when(inputMock.isEnabled(SAMPLE_LABEL)).thenReturn(true);

            // When
            var result = service.isEnabled(mockInputComponentType, SAMPLE_LABEL);

            // Then
            assertThat(result).isTrue();
            verify(inputMock).isEnabled(SAMPLE_LABEL);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("isEnabled with locator delegates correctly")
        void testIsEnabledBy() {
            // Given
            when(inputMock.isEnabled(locator)).thenReturn(true);

            // When
            var result = service.isEnabled(mockInputComponentType, locator);

            // Then
            assertThat(result).isTrue();
            verify(inputMock).isEnabled(locator);
            verifyNoMoreInteractions(inputMock);
        }
    }

    @Nested
    @DisplayName("GetErrorMessage Method Tests")
    class GetErrorMessageMethodTests {

        @Test
        @DisplayName("getErrorMessage with container delegates correctly")
        void testGetErrorMessageContainer() {
            // Given
            when(inputMock.getErrorMessage(container)).thenReturn(SAMPLE_ERROR_MSG);

            // When
            var result = service.getErrorMessage(mockInputComponentType, container);

            // Then
            assertThat(result).isEqualTo(SAMPLE_ERROR_MSG);
            verify(inputMock).getErrorMessage(container);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("getErrorMessage with container and label delegates correctly")
        void testGetErrorMessageContainerLabel() {
            // Given
            when(inputMock.getErrorMessage(container, SAMPLE_LABEL)).thenReturn(SAMPLE_ERROR_MSG);

            // When
            var result = service.getErrorMessage(mockInputComponentType, container, SAMPLE_LABEL);

            // Then
            assertThat(result).isEqualTo(SAMPLE_ERROR_MSG);
            verify(inputMock).getErrorMessage(container, SAMPLE_LABEL);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("getErrorMessage with label delegates correctly")
        void testGetErrorMessageLabel() {
            // Given
            when(inputMock.getErrorMessage(SAMPLE_LABEL)).thenReturn(SAMPLE_ERROR_MSG);

            // When
            var result = service.getErrorMessage(mockInputComponentType, SAMPLE_LABEL);

            // Then
            assertThat(result).isEqualTo(SAMPLE_ERROR_MSG);
            verify(inputMock).getErrorMessage(SAMPLE_LABEL);
            verifyNoMoreInteractions(inputMock);
        }

        @Test
        @DisplayName("getErrorMessage with locator delegates correctly")
        void testGetErrorMessageBy() {
            // Given
            when(inputMock.getErrorMessage(locator)).thenReturn(SAMPLE_ERROR_MSG);

            // When
            var result = service.getErrorMessage(mockInputComponentType, locator);

            // Then
            assertThat(result).isEqualTo(SAMPLE_ERROR_MSG);
            verify(inputMock).getErrorMessage(locator);
            verifyNoMoreInteractions(inputMock);
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
            service.insert(mockInputComponentType, container, SAMPLE_VALUE);
            service.clear(mockInputComponentType, container);

            // Then
            factoryMock.verify(() -> ComponentFactory.getInputComponent(eq(mockInputComponentType), eq(driver)), times(1));
        }
    }

    @Nested
    @DisplayName("Interface Implementation Method Tests")
    class InterfaceImplementationMethodTests {

        @Test
        @DisplayName("tableInsertion delegates to component correctly")
        void testTableInsertion() {
            // Given - setup in @BeforeEach

            // When
            service.tableInsertion(cell, mockInputComponentType, SAMPLE_VALUES);

            // Then
            verify(inputMock).tableInsertion(cell, SAMPLE_VALUES);
            factoryMock.verify(() -> ComponentFactory.getInputComponent(eq(mockInputComponentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("tableFilter delegates to component correctly")
        void testTableFilter() {
            // Given - setup in @BeforeEach

            // When
            service.tableFilter(headerCell, mockInputComponentType, filterStrategy, SAMPLE_VALUE);

            // Then
            verify(inputMock).tableFilter(headerCell, filterStrategy, SAMPLE_VALUE);
            factoryMock.verify(() -> ComponentFactory.getInputComponent(eq(mockInputComponentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("insertion method delegates to component's insert method")
        void testInsertionMethod() {
            // Given - setup in @BeforeEach

            // When
            service.insertion(mockInputComponentType, locator, SAMPLE_VALUE);

            // Then
            verify(inputMock).insert(locator, SAMPLE_VALUE);
            factoryMock.verify(() -> ComponentFactory.getInputComponent(eq(mockInputComponentType), eq(driver)), times(1));
        }

        @Test
        @DisplayName("tableInsertion with non-InputComponentType throws exception")
        void testTableInsertionWithNonInputComponentType() {
            // Given
            ComponentType nonInputType = mock(ComponentType.class);

            // When / Then
            assertThatThrownBy(() -> service.tableInsertion(cell, nonInputType, SAMPLE_VALUES))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Component type needs to be from: InputComponentType");
            factoryMock.verifyNoInteractions();
            verify(inputMock, never()).tableInsertion(any(), any());
        }

        @Test
        @DisplayName("tableFilter with non-InputComponentType throws exception")
        void testTableFilterWithNonInputComponentType() {
            // Given
            ComponentType nonInputType = mock(ComponentType.class);

            // When / Then
            assertThatThrownBy(() -> service.tableFilter(headerCell, nonInputType, filterStrategy, SAMPLE_VALUE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Component type needs to be from: InputComponentType");
            factoryMock.verifyNoInteractions();
            verify(inputMock, never()).tableFilter(any(), any(), any());
        }

        @Test
        @DisplayName("insertion with non-InputComponentType throws exception")
        void testInsertionWithNonInputComponentType() {
            // Given
            ComponentType nonInputType = mock(ComponentType.class);

            // When / Then
            assertThatThrownBy(() -> service.insertion(nonInputType, locator, SAMPLE_VALUE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Component type needs to be from: InputComponentType");
            factoryMock.verifyNoInteractions();
            verify(inputMock, never()).insert(any(By.class), anyString());
        }
    }

    @Nested
    @DisplayName("Null Handling")
    class NullHandlingTests {

        @Test
        @DisplayName("insert with null container delegates correctly")
        void testInsertWithNullContainer() {
            // Given
            SmartWebElement nullContainer = null;

            // When
            service.insert(mockInputComponentType, nullContainer, SAMPLE_VALUE);

            // Then
            verify(inputMock).insert(nullContainer, SAMPLE_VALUE);
        }

        @Test
        @DisplayName("insert with null value delegates correctly")
        void testInsertWithNullValue() {
            // Given
            String nullValue = null;

            // When
            service.insert(mockInputComponentType, container, nullValue);

            // Then
            verify(inputMock).insert(container, nullValue);
        }

        @Test
        @DisplayName("getValue with null container delegates correctly")
        void testGetValueWithNullContainer() {
            // Given
            SmartWebElement nullContainer = null;
            when(inputMock.getValue(nullContainer)).thenReturn("nullContainerValue");

            // When
            var result = service.getValue(mockInputComponentType, nullContainer);

            // Then
            assertThat(result).isEqualTo("nullContainerValue");
            verify(inputMock).getValue(nullContainer);
        }

        @Test
        @DisplayName("isEnabled with null container delegates correctly")
        void testIsEnabledWithNullContainer() {
            // Given
            SmartWebElement nullContainer = null;
            when(inputMock.isEnabled(nullContainer)).thenReturn(false);

            // When
            var result = service.isEnabled(mockInputComponentType, nullContainer);

            // Then
            assertThat(result).isFalse();
            verify(inputMock).isEnabled(nullContainer);
        }

        @Test
        @DisplayName("getErrorMessage with null container delegates correctly")
        void testGetErrorMessageWithNullContainer() {
            // Given
            SmartWebElement nullContainer = null;
            when(inputMock.getErrorMessage(nullContainer)).thenReturn("nullError");

            // When
            var result = service.getErrorMessage(mockInputComponentType, nullContainer);

            // Then
            assertThat(result).isEqualTo("nullError");
            verify(inputMock).getErrorMessage(nullContainer);
        }
    }
}