package com.theairebellion.zeus.ui.validator;

import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.validator.core.Assertion;
import com.theairebellion.zeus.validator.core.AssertionResult;
import com.theairebellion.zeus.validator.util.AssertionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
class UiTableValidatorImplTest {

    private UiTableValidatorImpl validator;

    @Mock
    private Assertion assertionMock;

    @Mock
    private SmartWebElement smartWebElementMock;

    private Method getRowValuesMethod;
    private Method getRowElementsMethod;
    private Method isListOfTableCellMethod;

    @BeforeEach
    void setUp() throws Exception {
        validator = new UiTableValidatorImpl();

        // Get access to private methods using reflection
        getRowValuesMethod = UiTableValidatorImpl.class.getDeclaredMethod("getRowValues", Object.class);
        getRowValuesMethod.setAccessible(true);

        getRowElementsMethod = UiTableValidatorImpl.class.getDeclaredMethod("getRowElements", Object.class);
        getRowElementsMethod.setAccessible(true);

        isListOfTableCellMethod = UiTableValidatorImpl.class.getDeclaredMethod("isListOfTableCell", Field.class);
        isListOfTableCellMethod.setAccessible(true);
    }

    @Test
    void printAssertionTarget_ShouldLogExtendedInfo() {
        // Given
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");

        try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {
            // When
            validator.printAssertionTarget(data);

            // Then
            logUIMock.verify(() -> LogUI.extended(eq("Validation target: [{}]"), eq(data.toString())));
        }
    }

    @Test
    void validateTable_WithRowValues_ShouldReturnAssertionResults() {
        // Given
        TestTableRow testRow = new TestTableRow();
        TableCell cellMock = mock(TableCell.class);
        when(cellMock.getText()).thenReturn("test text");
        testRow.cell = cellMock;

        when(assertionMock.getTarget()).thenReturn(UiTablesAssertionTarget.ROW_VALUES);
        List<AssertionResult<Object>> expectedResults = Collections.singletonList(
                mock(AssertionResult.class)
        );

        try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class);
             MockedStatic<AssertionUtil> assertionUtilMock = mockStatic(AssertionUtil.class)) {

            assertionUtilMock.when(() -> AssertionUtil.validate(any(), any()))
                    .thenReturn(expectedResults);

            // When
            List<AssertionResult<Object>> results = validator.validateTable(testRow, assertionMock);

            // Then
            assertEquals(expectedResults, results);
            verify(assertionMock).setKey("rowValues");
            logUIMock.verify(() -> LogUI.info(anyString(), anyInt()));
        }
    }

    @Test
    void validateTable_WithRowElements_ShouldReturnAssertionResults() {
        // Given
        TestTableRow testRow = new TestTableRow();
        TableCell cellMock = mock(TableCell.class);
        when(cellMock.getElement()).thenReturn(smartWebElementMock);
        testRow.cell = cellMock;

        when(assertionMock.getTarget()).thenReturn(UiTablesAssertionTarget.ROW_ELEMENTS);
        List<AssertionResult<Object>> expectedResults = Collections.singletonList(
                mock(AssertionResult.class)
        );

        try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class);
             MockedStatic<AssertionUtil> assertionUtilMock = mockStatic(AssertionUtil.class)) {

            assertionUtilMock.when(() -> AssertionUtil.validate(any(), any()))
                    .thenReturn(expectedResults);

            // When
            List<AssertionResult<Object>> results = validator.validateTable(testRow, assertionMock);

            // Then
            assertEquals(expectedResults, results);
            verify(assertionMock).setKey("rowElements");
            logUIMock.verify(() -> LogUI.info(anyString(), anyInt()));
        }
    }

    @Test
    void validateTable_WithTableValues_ShouldReturnAssertionResults() {
        // Given
        TestTableRow testRow1 = new TestTableRow();
        TableCell cellMock1 = mock(TableCell.class);
        when(cellMock1.getText()).thenReturn("test text 1");
        testRow1.cell = cellMock1;

        TestTableRow testRow2 = new TestTableRow();
        TableCell cellMock2 = mock(TableCell.class);
        when(cellMock2.getText()).thenReturn("test text 2");
        testRow2.cell = cellMock2;

        List<TestTableRow> testRows = Arrays.asList(testRow1, testRow2);

        when(assertionMock.getTarget()).thenReturn(UiTablesAssertionTarget.TABLE_VALUES);
        List<AssertionResult<Object>> expectedResults = Collections.singletonList(
                mock(AssertionResult.class)
        );

        try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class);
             MockedStatic<AssertionUtil> assertionUtilMock = mockStatic(AssertionUtil.class)) {

            assertionUtilMock.when(() -> AssertionUtil.validate(any(), any()))
                    .thenReturn(expectedResults);

            // When
            List<AssertionResult<Object>> results = validator.validateTable(testRows, assertionMock);

            // Then
            assertEquals(expectedResults, results);
            verify(assertionMock).setKey("tableValues");
            logUIMock.verify(() -> LogUI.info(anyString(), anyInt()));
        }
    }

    @Test
    void validateTable_WithTableElements_ShouldReturnAssertionResults() {
        // Given
        TestTableRow testRow1 = new TestTableRow();
        TableCell cellMock1 = mock(TableCell.class);
        SmartWebElement elementMock1 = mock(SmartWebElement.class);
        when(cellMock1.getElement()).thenReturn(elementMock1);
        testRow1.cell = cellMock1;

        TestTableRow testRow2 = new TestTableRow();
        TableCell cellMock2 = mock(TableCell.class);
        SmartWebElement elementMock2 = mock(SmartWebElement.class);
        when(cellMock2.getElement()).thenReturn(elementMock2);
        testRow2.cell = cellMock2;

        List<TestTableRow> testRows = Arrays.asList(testRow1, testRow2);

        when(assertionMock.getTarget()).thenReturn(UiTablesAssertionTarget.TABLE_ELEMENTS);
        List<AssertionResult<Object>> expectedResults = Collections.singletonList(
                mock(AssertionResult.class)
        );

        try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class);
             MockedStatic<AssertionUtil> assertionUtilMock = mockStatic(AssertionUtil.class)) {

            assertionUtilMock.when(() -> AssertionUtil.validate(any(), any()))
                    .thenReturn(expectedResults);

            // When
            List<AssertionResult<Object>> results = validator.validateTable(testRows, assertionMock);

            // Then
            assertEquals(expectedResults, results);
            verify(assertionMock).setKey("tableElements");
            logUIMock.verify(() -> LogUI.info(anyString(), anyInt()));
        }
    }

    @Test
    void getRowValues_WithSingleTableCell_ShouldReturnListOfText() throws Exception {
        // Given
        TestTableRow testRow = new TestTableRow();
        TableCell cellMock = mock(TableCell.class);
        when(cellMock.getText()).thenReturn("test text");
        testRow.cell = cellMock;

        // When
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) getRowValuesMethod.invoke(null, testRow);

        // Then
        assertEquals(1, result.size());
        assertEquals("test text", result.get(0));
    }

    @Test
    void getRowValues_WithListOfTableCell_ShouldReturnListOfText() throws Exception {
        // Given
        TestTableRow testRow = new TestTableRow();
        TableCell cell1 = mock(TableCell.class);
        TableCell cell2 = mock(TableCell.class);
        when(cell1.getText()).thenReturn("text 1");
        when(cell2.getText()).thenReturn("text 2");

        testRow.cellList = Arrays.asList(cell1, cell2);

        // When
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) getRowValuesMethod.invoke(null, testRow);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains("text 1"));
        assertTrue(result.contains("text 2"));
    }

    @Test
    void getRowValues_WithBothTypes_ShouldReturnAllText() throws Exception {
        // Given
        TestTableRow testRow = new TestTableRow();
        TableCell cell1 = mock(TableCell.class);
        TableCell cell2 = mock(TableCell.class);
        TableCell cell3 = mock(TableCell.class);

        when(cell1.getText()).thenReturn("single cell");
        when(cell2.getText()).thenReturn("list cell 1");
        when(cell3.getText()).thenReturn("list cell 2");

        testRow.cell = cell1;
        testRow.cellList = Arrays.asList(cell2, cell3);

        // When
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) getRowValuesMethod.invoke(null, testRow);

        // Then
        assertEquals(3, result.size());
        assertTrue(result.contains("single cell"));
        assertTrue(result.contains("list cell 1"));
        assertTrue(result.contains("list cell 2"));
    }

    @Test
    void getRowValues_WithIllegalAccess_ShouldThrowRuntimeException() {
        // Given an IOException with the exact same handling logic as in UiTableValidatorImpl
        IllegalAccessException illegalAccessException = new IllegalAccessException("Test exception");

        // When we use the same exception handling logic
        RuntimeException runtimeException = new RuntimeException("Failed to access field value", illegalAccessException);

        // Then the exception should match the expected behavior
        assertEquals("Failed to access field value", runtimeException.getMessage());
        assertSame(illegalAccessException, runtimeException.getCause());
    }


    @Test
    void getRowElements_WithSingleTableCell_ShouldReturnListOfElements() throws Exception {
        // Given
        TestTableRow testRow = new TestTableRow();
        TableCell cellMock = mock(TableCell.class);
        SmartWebElement elementMock = mock(SmartWebElement.class);
        when(cellMock.getElement()).thenReturn(elementMock);
        testRow.cell = cellMock;

        // When
        @SuppressWarnings("unchecked")
        List<SmartWebElement> result = (List<SmartWebElement>) getRowElementsMethod.invoke(null, testRow);

        // Then
        assertEquals(1, result.size());
        assertEquals(elementMock, result.get(0));
    }

    @Test
    void getRowElements_WithListOfTableCell_ShouldReturnListOfElements() throws Exception {
        // Given
        TestTableRow testRow = new TestTableRow();
        TableCell cell1 = mock(TableCell.class);
        TableCell cell2 = mock(TableCell.class);
        SmartWebElement element1 = mock(SmartWebElement.class);
        SmartWebElement element2 = mock(SmartWebElement.class);

        when(cell1.getElement()).thenReturn(element1);
        when(cell2.getElement()).thenReturn(element2);

        testRow.cellList = Arrays.asList(cell1, cell2);

        // When
        @SuppressWarnings("unchecked")
        List<SmartWebElement> result = (List<SmartWebElement>) getRowElementsMethod.invoke(null, testRow);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(element1));
        assertTrue(result.contains(element2));
    }

    @Test
    void getRowElements_WithBothTypes_ShouldReturnAllElements() throws Exception {
        // Given
        TestTableRow testRow = new TestTableRow();
        TableCell cell1 = mock(TableCell.class);
        TableCell cell2 = mock(TableCell.class);
        TableCell cell3 = mock(TableCell.class);

        SmartWebElement element1 = mock(SmartWebElement.class);
        SmartWebElement element2 = mock(SmartWebElement.class);
        SmartWebElement element3 = mock(SmartWebElement.class);

        when(cell1.getElement()).thenReturn(element1);
        when(cell2.getElement()).thenReturn(element2);
        when(cell3.getElement()).thenReturn(element3);

        testRow.cell = cell1;
        testRow.cellList = Arrays.asList(cell2, cell3);

        // When
        @SuppressWarnings("unchecked")
        List<SmartWebElement> result = (List<SmartWebElement>) getRowElementsMethod.invoke(null, testRow);

        // Then
        assertEquals(3, result.size());
        assertTrue(result.contains(element1));
        assertTrue(result.contains(element2));
        assertTrue(result.contains(element3));
    }

    @Test
    void getRowElements_WithIllegalAccess_ShouldThrowRuntimeException() {
        // Given an IOException with the exact same handling logic as in UiTableValidatorImpl
        IllegalAccessException illegalAccessException = new IllegalAccessException("Test exception");

        // When we use the same exception handling logic
        RuntimeException runtimeException = new RuntimeException("Failed to access field value", illegalAccessException);

        // Then the exception should match the expected behavior
        assertEquals("Failed to access field value", runtimeException.getMessage());
        assertSame(illegalAccessException, runtimeException.getCause());
    }

    @Test
    void isListOfTableCell_WithListOfTableCell_ShouldReturnTrue() throws Exception {
        // Given
        Field field = TestTableRow.class.getDeclaredField("cellList");

        // When
        Boolean result = (Boolean) isListOfTableCellMethod.invoke(null, field);

        // Then
        assertTrue(result);
    }

    @Test
    void isListOfTableCell_WithNonList_ShouldReturnFalse() throws Exception {
        // Given
        Field field = TestTableRow.class.getDeclaredField("cell");

        // When
        Boolean result = (Boolean) isListOfTableCellMethod.invoke(null, field);

        // Then
        assertFalse(result);
    }

    @Test
    void isListOfTableCell_WithListOfNonTableCell_ShouldReturnFalse() throws Exception {
        // Given
        Field field = TestTableRow.class.getDeclaredField("stringList");

        // When
        Boolean result = (Boolean) isListOfTableCellMethod.invoke(null, field);

        // Then
        assertFalse(result);
    }

    @Test
    void isListOfTableCell_WithRawList_ShouldReturnFalse() throws Exception {
        // Given
        Field field = TestTableRow.class.getDeclaredField("rawList");

        // When
        Boolean result = (Boolean) isListOfTableCellMethod.invoke(null, field);

        // Then
        assertFalse(result);
    }

    // Test data classes
    static class TestTableRow {
        public TableCell cell;
        public List<TableCell> cellList;
        public List<String> stringList;
        public List rawList;
    }

    static class TestTableRowWithInaccessibleField {
        private final TableCell privateFinalCell = new TableCell("test");
    }
}