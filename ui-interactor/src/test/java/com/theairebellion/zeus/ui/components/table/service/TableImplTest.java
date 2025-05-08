package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.table.annotations.*;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.CellFilterFunction;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionFunction;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.components.table.model.CellLocator;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.components.table.model.TableLocators;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import com.theairebellion.zeus.util.reflections.exceptions.ReflectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("TableImpl Tests")
@SuppressWarnings("all")
class TableImplTest extends BaseUnitUITest {

    // --- Mocks ---
    @Mock private SmartWebDriver driver;
    @Mock private SmartWebElement container;
    @Mock private SmartWebElement rowElement;
    @Mock private SmartWebElement cellElement;
    @Mock private SmartWebElement headerRowElement;
    @Mock private SmartWebElement headerCellElement;
    @Mock private WebDriverWait wait;
    @Mock private TableServiceRegistry registry;

    // --- Test Subject & Helpers ---
    private List<SmartWebElement> rows;
    private TableLocators locators;
    private TestTableImpl tableImpl;

    // --- Constants ---
    private static final String SECTION_1 = "section1";
    private static final String SECTION_2 = "section2";
    private static final By TABLE_LOCATOR = By.id("dummyTable");
    private static final By ROWS_LOCATOR = By.className("dummyRow");
    private static final By HEADER_LOCATOR = By.className("dummyHeader");
    private static final By CELL_LOCATOR_DUMMY = By.className("dummyCell");
    private static final By TEXT_LOCATOR_DUMMY = By.className("dummyText");
    private static final By HEADER_CELL_LOCATOR_DUMMY = By.className("dummyHeaderCell");
    private static final By CELL_LOCATOR_S1 = By.className("section1Cell");
    private static final By CELL_LOCATOR_S2 = By.className("section2Cell");
    private static final By HEADER_LOCATOR_S1 = By.className("header1");
    private static final By HEADER_LOCATOR_S2 = By.className("header2");
    private static final By LIST_CELL_LOCATOR = By.className("listCell");
    private static final By INSERT_CELL_LOCATOR = By.className("insCell");
    private static final By CUSTOM_INSERT_CELL_LOCATOR = By.className("customInsCell");
    private static final By FILTER_DATA_CELL_LOCATOR = By.className("filterDataCell");
    private static final By FILTER_HEADER_CELL_LOCATOR = By.className("filterHeaderCell");
    private static final By CUSTOM_FILTER_DATA_CELL_LOCATOR = By.className("customFilterDataCell");
    private static final By CUSTOM_FILTER_HEADER_CELL_LOCATOR = By.className("customFilterHeaderCell");
    private static final By MISSING_TABLE_LOCATOR = By.id("missingAnnotation");
    private static final List<String> CRITERIA_MATCH = List.of("match");
    private static final List<String> CRITERIA_NO_MATCH = List.of("no match");
    private static final String CELL_TEXT_1 = "cell 1 text";
    private static final String CELL_TEXT_VALUE = "value";

    // --- Inner Row Model Classes (Necessary for scenarios) ---

    @TableInfo(tableContainerLocator = @FindBy(id = "dummyTable"), rowsLocator = @FindBy(className = "dummyRow"), headerRowLocator = @FindBy(className = "dummyHeader"))
    static class DummyRow {
        @TableCellLocator(cellLocator = @FindBy(className = "dummyCell"), cellTextLocator = @FindBy(className = "dummyText"), headerCellLocator = @FindBy(className = "dummyHeaderCell"), tableSection = SECTION_1)
        private TableCell field1;
        public TableCell getField1() { return field1; }
        public void setField1(TableCell cell) { this.field1 = cell; }
    }

    @TableInfo(tableContainerLocator = @FindBy(id = "dummyTable"), rowsLocator = @FindBy(className = "dummyRow"), headerRowLocator = @FindBy(className = "dummyHeader"))
    static class MultiSectionRow {
        @TableCellLocator(cellLocator = @FindBy(className = "section1Cell"), tableSection = SECTION_1, headerCellLocator = @FindBy(className = "header1"))
        private TableCell section1Field;
        @TableCellLocator(cellLocator = @FindBy(className = "section2Cell"), tableSection = SECTION_2, headerCellLocator = @FindBy(className = "header2"))
        private TableCell section2Field;
        public TableCell getSection1Field() { return section1Field; }
        public void setSection1Field(TableCell cell) { this.section1Field = cell; }
        public TableCell getSection2Field() { return section2Field; }
        public void setSection2Field(TableCell cell) { this.section2Field = cell; }
    }

    @TableInfo(tableContainerLocator = @FindBy(id = "dummyTable"), rowsLocator = @FindBy(className = "dummyRow"), headerRowLocator = @FindBy(className = "dummyHeader"))
    static class ListFieldRow {
        @TableCellLocator(cellLocator = @FindBy(className = "listCell"))
        private List<TableCell> listField;
        public List<TableCell> getListField() { return listField; }
        public void setListField(List<TableCell> list) { this.listField = list; }
    }

    @TableInfo(tableContainerLocator = @FindBy(id = "dummyTable"), rowsLocator = @FindBy(className = "dummyRow"), headerRowLocator = @FindBy(className = "dummyHeader"))
    static class InsertionRow {
        @TableCellLocator(cellLocator = @FindBy(className = "insCell"), tableSection = "dummySection") // Add tableSection
        @CellInsertion(type = MockComponentTypeForTable.class, componentType = "INSERT_TYPE", order = 1)
        private TableCell insertionField;
        public TableCell getInsertionField() { return insertionField; }
        public void setInsertionField(TableCell cell) { this.insertionField = cell; }
    }

    @TableInfo(tableContainerLocator = @FindBy(id = "dummyTable"), rowsLocator = @FindBy(className = "dummyRow"), headerRowLocator = @FindBy(className = "dummyHeader"))
    static class CustomInsertionRow {
        @TableCellLocator(cellLocator = @FindBy(className = "customInsCell"), tableSection = "dummySection") // Add tableSection
        @CustomCellInsertion(insertionFunction = WorkingCellInsertionFunction.class, order = 1)
        private TableCell customInsertionField;
        public TableCell getCustomInsertionField() { return customInsertionField; }
        public void setCustomInsertionField(TableCell cell) { this.customInsertionField = cell; }
    }

    @TableInfo(tableContainerLocator = @FindBy(id = "dummyTable"), rowsLocator = @FindBy(className = "dummyRow"), headerRowLocator = @FindBy(className = "dummyHeader"))
    static class FilterRow {
        @TableCellLocator(cellLocator = @FindBy(className = "filterDataCell"), headerCellLocator = @FindBy(className = "filterHeaderCell"), tableSection = "dummySection") // Add tableSection
        @CellFilter(type = MockComponentTypeForTable.class, componentType = "FILTER_TYPE")
        private TableCell filterField;
        public TableCell getFilterField() { return filterField; }
        public void setFilterField(TableCell cell) { this.filterField = cell; }
    }

    @TableInfo(tableContainerLocator = @FindBy(id = "dummyTable"), rowsLocator = @FindBy(className = "dummyRow"), headerRowLocator = @FindBy(className = "dummyHeader"))
    static class CustomFilterRow {
        @TableCellLocator(cellLocator = @FindBy(className = "customFilterDataCell"), headerCellLocator = @FindBy(className = "customFilterHeaderCell"), tableSection = "dummySection") // Add tableSection
        @CustomCellFilter(cellFilterFunction = WorkingCellFilterFunction.class)
        private TableCell customFilterField;
        public TableCell getCustomFilterField() { return customFilterField; }
        public void setCustomFilterField(TableCell cell) { this.customFilterField = cell; }
    }

    @TableInfo(tableContainerLocator=@FindBy(id="t"),rowsLocator=@FindBy(id="r"),headerRowLocator=@FindBy(id="h"))
    static class RowWithMissingLocator {
        private TableCell missingAnnotationField; // No @TableCellLocator
        public void setMissingAnnotationField(TableCell c) { missingAnnotationField = c;}
    }

    @TableInfo(tableContainerLocator=@FindBy(id="t"),rowsLocator=@FindBy(id="r"),headerRowLocator=@FindBy(id="h"))
    static class BadCustomRow {
        @TableCellLocator(cellLocator=@FindBy(id="c"))
        @CustomCellInsertion(insertionFunction = PrivateConstructorInsertionFunction.class) // Private constructor
        TableCell field = new TableCell("bad");
        public void setField(TableCell c) { field=c; }
        public TableCell getField(){ return field;} // Add getter
    }

    @TableInfo(tableContainerLocator = @FindBy(id="t"), rowsLocator = @FindBy(id="r"), headerRowLocator = @FindBy(id="h"))
    static class BadCustomFilterRow {
        @TableCellLocator(cellLocator=@FindBy(id="c"), headerCellLocator=@FindBy(id="hc"))
        @CustomCellFilter(cellFilterFunction = PrivateConstructorFilterFunction.class) // Use helper below
        TableCell field = new TableCell("bad");
        public void setField(TableCell c) { field=c; }
    }

    @TableInfo(tableContainerLocator=@FindBy(id="t"),rowsLocator=@FindBy(id="r"),headerRowLocator=@FindBy(id="h"))
    static class RowWithCustomInsertion {
        @TableCellLocator(cellLocator=@FindBy(id="c"))
        @CustomCellInsertion(insertionFunction = ValidCustomInsertionFunction.class)
        TableCell field = new TableCell("bad");
        public void setField(TableCell c) { field=c; }
    }

    @TableInfo(tableContainerLocator=@FindBy(id="t"),rowsLocator=@FindBy(id="r"),headerRowLocator=@FindBy(id="h"))
    static class InvalidRow {
        @TableCellLocator(cellLocator=@FindBy(id="c"))
        private String invalidField;
    }

    @TableInfo(tableContainerLocator=@FindBy(id="t"),rowsLocator=@FindBy(id="r"),headerRowLocator=@FindBy(id="h"))
    static class RowMissingLocator {
        private TableCell fieldWithoutLocator;
        public void setFieldWithoutLocator(TableCell c){ fieldWithoutLocator = c;}
        public TableCell getFieldWithoutLocator(){ return fieldWithoutLocator; }
    }

    // Class missing @TableInfo used for testing getTableLocators error path via public API
    static class MissingTableInfoRow {}


    // --- Inner Mock Functions (Keep Necessary) ---
    static class WorkingCellInsertionFunction implements CellInsertionFunction {
        static int callCount = 0; static SmartWebElement lastCell; static String[] lastValues;
        @Override public void cellInsertionFunction(SmartWebElement element, String... vals) { accept(element, vals); }
        @Override public void accept(SmartWebElement cell, String[] vals) { callCount++; lastCell = cell; lastValues = vals; }
        static void reset() { callCount = 0; lastCell = null; lastValues = null; }
    }

    static class WorkingCellFilterFunction implements CellFilterFunction {
        static int callCount = 0; static SmartWebElement lastCell; static FilterStrategy lastStrategy; static String[] lastValues;
        @Override public void cellFilterFunction(SmartWebElement element, FilterStrategy strategy, String... vals) { accept(element, strategy, vals); }
        @Override public void accept(SmartWebElement cell, FilterStrategy strategy, String[] vals) { callCount++; lastCell = cell; lastStrategy = strategy; lastValues = vals; }
        static void reset() { callCount = 0; lastCell = null; lastStrategy = null; lastValues = null; }
    }

    // --- Inner Mock Component Type (Keep Necessary) ---
    enum MockComponentTypeForTable implements ComponentType {
        INSERT_TYPE, FILTER_TYPE;
        @Override public Enum<?> getType() { return this; } // Return self for simple type checking
    }

    // --- Test Implementation of TableImpl (Keep) ---
    static class TestTableImpl extends TableImpl {
        private final SmartWebElement headerRowMock;
        boolean sortTableCalled = false;
        SmartWebElement sortHeaderCellArg = null;
        SortingStrategy sortStrategyArg = null;


        TestTableImpl(SmartWebDriver driver, TableServiceRegistry registry, TableLocators locators, SmartWebElement container,
                      List<SmartWebElement> rows, SmartWebElement headerRow) {
            super(driver, registry);
            this.headerRowMock = headerRow;
        }

        @Override
        protected SmartWebElement getTableContainer(By tableContainerLocator) {
            // Can add verification of locator if needed, return mocked container provided in setup
            return super.getTableContainer(tableContainerLocator); // Call super to test its logic, rely on driver mock
        }

        @Override
        protected List<SmartWebElement> getRows(SmartWebElement tableContainer, By tableRowsLocator, String section) {
            // Can add verification, return mocked rows provided in setup
            return super.getRows(tableContainer, tableRowsLocator, section); // Call super to test its logic
        }

        @Override
        protected SmartWebElement getHeaderRow(SmartWebElement tableContainer, By headerRowLocator, String tableSection) {
            // Can add verification, return mocked header provided in setup
            return headerRowMock != null ? headerRowMock : super.getHeaderRow(tableContainer, headerRowLocator, tableSection); // Return specific mock if provided
        }

        @Override
        protected void sortTable(SmartWebElement headerCell, SortingStrategy sortingStrategy) {
            // Track calls for verification
            this.sortTableCalled = true;
            this.sortHeaderCellArg = headerCell;
            this.sortStrategyArg = sortingStrategy;
        }
    }

    public static class ValidCustomInsertionFunction implements CellInsertionFunction {
        public static boolean called = false;
        public static SmartWebElement lastElement = null;
        public static String[] lastValues = null;

        @Override
        public void cellInsertionFunction(SmartWebElement cellElement, String... values) {
            called = true;
            lastElement = cellElement;
            lastValues = values;
        }

        @Override
        public void accept(SmartWebElement smartWebElement, String[] objects) {
            cellInsertionFunction(smartWebElement, (String[]) objects);
        }
    }

    @BeforeEach
    void setUp() {
        // Given
        MockitoAnnotations.openMocks(this); // Ensures @Mock fields are initialized
        rows = new ArrayList<>(List.of(rowElement));
        locators = new TableLocators(TABLE_LOCATOR, ROWS_LOCATOR, HEADER_LOCATOR);
        WorkingCellInsertionFunction.reset();
        WorkingCellFilterFunction.reset();

        // Instantiate the TestTableImpl (make sure TestTableImpl class is defined correctly)
        tableImpl = spy(new TestTableImpl(driver, registry, locators, container, rows, headerRowElement));

        // Setup common mock behaviors needed by TableImpl logic being tested
        lenient().when(driver.getWait()).thenReturn(wait);
        lenient().when(wait.until(any())).thenReturn(true);
        lenient().when(driver.findSmartElement(TABLE_LOCATOR)).thenReturn(container);
        lenient().when(container.findSmartElements(ROWS_LOCATOR)).thenReturn(rows);
        lenient().when(rowElement.findSmartElement(CELL_LOCATOR_DUMMY)).thenReturn(cellElement);
        lenient().when(cellElement.findSmartElement(TEXT_LOCATOR_DUMMY)).thenReturn(cellElement);
        lenient().when(cellElement.getText()).thenReturn(CELL_TEXT_VALUE);
        lenient().when(container.findSmartElement(HEADER_LOCATOR)).thenReturn(headerRowElement);
        lenient().when(headerRowElement.findSmartElement(HEADER_CELL_LOCATOR_DUMMY)).thenReturn(headerCellElement);
    }

    @Nested
    @DisplayName("Constructor and Protected Getters")
    class ConstructorAndProtectedGetters {

        @Test
        @DisplayName("Constructor with driver and registry initializes properly")
        void testConstructorWithDriverAndRegistry() {
            // Given
            var testRegistry = new TableServiceRegistry();

            // When
            var impl = new TestTableImpl(driver, testRegistry, locators, container, rows, headerRowElement);

            // Then
            assertThat(impl).isNotNull();
            // Verify registry was stored - Use reflection carefully only if absolutely necessary for coverage
            try {
                var registryField = TableImpl.class.getDeclaredField("serviceRegistry");
                registryField.setAccessible(true);
                assertThat(registryField.get(impl)).isSameAs(testRegistry);
            } catch (Exception e) {
                fail("Reflection failed for registry check", e);
            }
        }

        @Test
        @DisplayName("getTableContainer calls driver")
        void testGetTableContainer() {
            // Given - driver mock setup in main @BeforeEach

            // When
            // Call directly on the instance (which uses super implementation if not overridden fully)
            var result = tableImpl.getTableContainer(TABLE_LOCATOR);

            // Then
            assertThat(result).isSameAs(container);
            verify(driver).findSmartElement(TABLE_LOCATOR); // Verify interaction with driver mock
        }

        @Test
        @DisplayName("getRows calls container and waits")
        void testGetRows() {
            // Given - container mock setup in main @BeforeEach

            // When
            var result = tableImpl.getRows(container, ROWS_LOCATOR, SECTION_1);

            // Then
            assertThat(result).isSameAs(rows);
            verify(driver.getWait()).until(any()); // Verify wait was called
            verify(container).findSmartElements(ROWS_LOCATOR); // Verify find was called
        }

        @Test
        @DisplayName("getHeaderRow calls container")
        void testGetHeaderRow() {
            // Given - container mock setup in main @BeforeEach

            // When
            var result = tableImpl.getHeaderRow(container, HEADER_LOCATOR, SECTION_1);

            // Then
            assertThat(result).isSameAs(headerRowElement);
            verify(container).findSmartElement(HEADER_LOCATOR); // Verify find was called
        }
    }

    @Nested
    @DisplayName("Table Reading Tests (Public API)")
    class TableReadingTest {

        // Define specific constants used in this section
        private static final String SIMPLE_VALUE = "simple value";
        private static final String FIELD_VALUE = "field value";
        private static final String ROW_PREFIX = "row ";
        private static final String FIELD_ROW_PREFIX = "field row ";
        private static final String ROW_BY_INDEX_VALUE = "row by index";
        private static final String ROW_BY_CRITERIA_VALUE = "row by criteria";
        private static final String ROW_INDEX_WITH_FIELDS_VALUE = "row index with fields";
        private static final String ROW_CRITERIA_WITH_FIELDS_VALUE = "row criteria with fields";
        private static final String MATCH_CRITERIA_TEXT = "match this criteria";
        private static final String NO_MATCH_CRITERIA_TEXT = "no match here";
        private static final String LIST_VALUE_1 = "listValue1";
        private static final String LIST_VALUE_2 = "listValue2";
        private static final String SECTION_1_VALUE = "S1 Value";
        private static final String SECTION_2_VALUE = "S2 Value";


        // Helper to create a mocked row with specific cell text
        private SmartWebElement createMockedRow(String cellText) {
            var mockRow = mock(SmartWebElement.class);
            var mockCell = mock(SmartWebElement.class);
            // Assume the DummyRow's locators are used for simplicity here
            when(mockRow.findSmartElement(eq(CELL_LOCATOR_DUMMY))).thenReturn(mockCell);
            // Assume text locator points to cell itself
            when(mockCell.findSmartElement(eq(TEXT_LOCATOR_DUMMY))).thenReturn(mockCell);
            when(mockCell.getText()).thenReturn(cellText);
            when(mockRow.getAttribute("innerText")).thenReturn(cellText); // Simple inner text for criteria matching
            return mockRow;
        }

        @Test
        @DisplayName("readTable(Class) reads all rows correctly")
        void testReadTable_Class() {
            // Given
            var row1 = createMockedRow(SIMPLE_VALUE);
            // Use doReturn for methods overridden in the spy's class if needed,
            // or directly mock the dependencies called by the *original* TableImpl method
            doReturn(List.of(row1)).when(tableImpl).getRows(any(), any(), anyString());

            // When
            var result = tableImpl.readTable(DummyRow.class);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0)).isInstanceOf(DummyRow.class);
            assertThat(result.get(0).getField1().getText()).isEqualTo(SIMPLE_VALUE);
            verify(tableImpl).getRows(container, ROWS_LOCATOR, SECTION_1); // Verify protected method call on spy
            verify(row1).findSmartElement(CELL_LOCATOR_DUMMY);
            verify(cellElement, never()).getText(); // Should use mockCell's text
        }

        @Test
        @DisplayName("readTable(Class, fields) reads only specified fields")
        void testReadTable_ClassAndFields() {
            // Given
            var row1 = createMockedRow(FIELD_VALUE);
            doReturn(List.of(row1)).when(tableImpl).getRows(any(), any(), anyString());
            TableField<DummyRow> field1 = TableField.of(DummyRow::setField1);

            // When
            var result = tableImpl.readTable(DummyRow.class, field1);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getField1()).isNotNull();
            assertThat(result.get(0).getField1().getText()).isEqualTo(FIELD_VALUE);
            // Add assertions if DummyRow had other fields to ensure they are null/default
            verify(tableImpl).getRows(container, ROWS_LOCATOR, SECTION_1);
            verify(row1).findSmartElement(CELL_LOCATOR_DUMMY);
        }

        @Test
        @DisplayName("readTable(start, end, Class) reads rows in specified range")
        void testReadTable_Range() {
            // Given
            var rowList = List.of(
                    createMockedRow(ROW_PREFIX + 0),
                    createMockedRow(ROW_PREFIX + 1),
                    createMockedRow(ROW_PREFIX + 2),
                    createMockedRow(ROW_PREFIX + 3),
                    createMockedRow(ROW_PREFIX + 4)
            );
            // Mock getRows directly on the spy instance
            doReturn(rowList).when(tableImpl).getRows(any(), any(), anyString());

            // When - Test valid range (2-4, expects rows at index 1, 2, 3)
            var result = tableImpl.readTable(2, 4, DummyRow.class);
            // When - Test invalid range (start > end)
            var emptyResult = tableImpl.readTable(4, 2, DummyRow.class);
            // When - Test range exceeding available rows (reads rows at index 3, 4)
            var exceededResult = tableImpl.readTable(4, 10, DummyRow.class);

            // Then
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getField1().getText()).isEqualTo(ROW_PREFIX + 1);
            assertThat(result.get(1).getField1().getText()).isEqualTo(ROW_PREFIX + 2);
            assertThat(result.get(2).getField1().getText()).isEqualTo(ROW_PREFIX + 3);
            assertThat(emptyResult).isEmpty();
            assertThat(exceededResult).hasSize(2);
            assertThat(exceededResult.get(0).getField1().getText()).isEqualTo(ROW_PREFIX + 3);
            assertThat(exceededResult.get(1).getField1().getText()).isEqualTo(ROW_PREFIX + 4);
        }

        @Test
        @DisplayName("readTable(start, end, Class, fields) reads range with specified fields")
        void testReadTable_RangeAndFields() {
            // Given
            var rowList = List.of(
                    createMockedRow(FIELD_ROW_PREFIX + 0),
                    createMockedRow(FIELD_ROW_PREFIX + 1),
                    createMockedRow(FIELD_ROW_PREFIX + 2),
                    createMockedRow(FIELD_ROW_PREFIX + 3),
                    createMockedRow(FIELD_ROW_PREFIX + 4)
            );
            doReturn(rowList).when(tableImpl).getRows(any(), any(), anyString());
            TableField<DummyRow> field1 = TableField.of(DummyRow::setField1);

            // When
            var result = tableImpl.readTable(2, 4, DummyRow.class, field1);

            // Then
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getField1().getText()).isEqualTo(FIELD_ROW_PREFIX + 1);
            assertThat(result.get(1).getField1().getText()).isEqualTo(FIELD_ROW_PREFIX + 2);
            assertThat(result.get(2).getField1().getText()).isEqualTo(FIELD_ROW_PREFIX + 3);
        }

        @Test
        @DisplayName("readRow(index, Class) reads correct row")
        void testReadRow_Index() {
            // Given
            var row1 = createMockedRow(ROW_BY_INDEX_VALUE);
            doReturn(List.of(row1)).when(tableImpl).getRows(any(), any(), anyString());

            // When
            var result = tableImpl.readRow(1, DummyRow.class); // 1-based index

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getField1().getText()).isEqualTo(ROW_BY_INDEX_VALUE);
            verify(tableImpl).getRows(container, ROWS_LOCATOR, SECTION_1);
        }

        @Test
        @DisplayName("readRow(index, Class) throws for invalid index")
        void testReadRow_InvalidIndex() {
            // Given
            // TestTableImpl returns only 1 row in setup
            var emptyRows = Collections.<SmartWebElement>emptyList();
            // Use spy on a new instance configured with empty rows
            var emptyTableImpl = spy(new TestTableImpl(driver, registry, locators, container, emptyRows, headerRowElement));
            // Mock the getRows call on the spy
            doReturn(emptyRows).when(emptyTableImpl).getRows(any(), any(), anyString());


            // When / Then
            assertThatThrownBy(() -> tableImpl.readRow(2, DummyRow.class)) // Index 2 > size 1
                    .isInstanceOf(IndexOutOfBoundsException.class);
            assertThatThrownBy(() -> tableImpl.readRow(0, DummyRow.class)) // Index 0 invalid
                    .isInstanceOf(IndexOutOfBoundsException.class);
            assertThatThrownBy(() -> emptyTableImpl.readRow(1, DummyRow.class)) // Index 1 on empty list
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("readRow(criteria, Class) reads correct row")
        void testReadRow_Criteria() {
            // Given
            var row1 = createMockedRow(ROW_BY_CRITERIA_VALUE);
            when(row1.getAttribute("innerText")).thenReturn(MATCH_CRITERIA_TEXT); // Setup text for criteria match
            doReturn(List.of(row1)).when(tableImpl).getRows(any(), any(), anyString());


            // When
            var result = tableImpl.readRow(CRITERIA_MATCH, DummyRow.class);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getField1().getText()).isEqualTo(ROW_BY_CRITERIA_VALUE);
            verify(row1).getAttribute("innerText"); // Verify criteria check happened
        }

        @Test
        @DisplayName("readRow(criteria, Class) throws when not found")
        void testReadRow_CriteriaNotFound() {
            // Given
            var row1 = createMockedRow(CELL_TEXT_1);
            when(row1.getAttribute("innerText")).thenReturn(NO_MATCH_CRITERIA_TEXT);
            doReturn(List.of(row1)).when(tableImpl).getRows(any(), any(), anyString());

            // When / Then
            assertThatThrownBy(() -> tableImpl.readRow(CRITERIA_NO_MATCH, DummyRow.class))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("No row found containing all criteria");
        }

        @Test
        @DisplayName("readRow(index, Class, fields) reads row by index with specific fields")
        void testReadRow_IndexAndFields() {
            // Given
            var row1 = createMockedRow(ROW_INDEX_WITH_FIELDS_VALUE);
            doReturn(List.of(row1)).when(tableImpl).getRows(any(), any(), anyString());
            TableField<DummyRow> field1 = TableField.of(DummyRow::setField1);

            // When
            var result = tableImpl.readRow(1, DummyRow.class, field1);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getField1().getText()).isEqualTo(ROW_INDEX_WITH_FIELDS_VALUE);
            // Assert other fields would be null if they existed and weren't specified
        }

        @Test
        @DisplayName("readRow(criteria, Class, fields) reads row by criteria with specific fields")
        void testReadRow_CriteriaAndFields() {
            // Given
            var row1 = createMockedRow(ROW_CRITERIA_WITH_FIELDS_VALUE);
            when(row1.getAttribute("innerText")).thenReturn(MATCH_CRITERIA_TEXT);
            doReturn(List.of(row1)).when(tableImpl).getRows(any(), any(), anyString());
            TableField<DummyRow> field1 = TableField.of(DummyRow::setField1);

            // When
            var result = tableImpl.readRow(CRITERIA_MATCH, DummyRow.class, field1);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getField1().getText()).isEqualTo(ROW_CRITERIA_WITH_FIELDS_VALUE);
        }

        @Test
        @DisplayName("readTable handles List<TableCell> fields correctly")
        void testReadTable_ListField() {
            // Given
            var listCell1 = mock(SmartWebElement.class);
            var listCell2 = mock(SmartWebElement.class);
            var rowForList = mock(SmartWebElement.class); // Use a fresh row mock
            doReturn(List.of(rowForList)).when(tableImpl).getRows(any(), any(), anyString()); // Mock getRows to return this row

            when(rowForList.findSmartElements(eq(LIST_CELL_LOCATOR))).thenReturn(List.of(listCell1, listCell2));
            when(listCell1.findSmartElement(any(By.class))).thenReturn(listCell1);
            when(listCell2.findSmartElement(any(By.class))).thenReturn(listCell2);
            when(listCell1.getText()).thenReturn(LIST_VALUE_1);
            when(listCell2.getText()).thenReturn(LIST_VALUE_2);

            // When
            var result = tableImpl.readTable(ListFieldRow.class);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getListField()).isNotNull();
            assertThat(result.get(0).getListField()).hasSize(2);
            assertThat(result.get(0).getListField().get(0).getText()).isEqualTo(LIST_VALUE_1);
            assertThat(result.get(0).getListField().get(1).getText()).isEqualTo(LIST_VALUE_2);
        }

        @Test
        @DisplayName("readTable handles multiple table sections")
        void testReadTable_MultiSection() {
            // Given
            var rowSec1 = mock(SmartWebElement.class);
            var cellS1 = mock(SmartWebElement.class);
            when(rowSec1.findSmartElement(eq(CELL_LOCATOR_S1))).thenReturn(cellS1);
            when(cellS1.findSmartElement(any(By.class))).thenReturn(cellS1);
            when(cellS1.getText()).thenReturn(SECTION_1_VALUE);

            var rowSec2 = mock(SmartWebElement.class);
            var cellS2 = mock(SmartWebElement.class);
            when(rowSec2.findSmartElement(eq(CELL_LOCATOR_S2))).thenReturn(cellS2);
            when(cellS2.findSmartElement(any(By.class))).thenReturn(cellS2);
            when(cellS2.getText()).thenReturn(SECTION_2_VALUE);

            // Mock getRows to return the correct row based on section
            // Use thenAnswer for more complex mock logic
            doAnswer(invocation -> {
                String section = invocation.getArgument(2);
                if (SECTION_1.equals(section)) return List.of(rowSec1);
                if (SECTION_2.equals(section)) return List.of(rowSec2);
                return Collections.emptyList();
            }).when(tableImpl).getRows(any(SmartWebElement.class), any(By.class), anyString());


            // When
            var result = tableImpl.readTable(MultiSectionRow.class);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getSection1Field().getText()).isEqualTo(SECTION_1_VALUE);
            assertThat(result.get(0).getSection2Field().getText()).isEqualTo(SECTION_2_VALUE);
            verify(tableImpl).getRows(container, ROWS_LOCATOR, SECTION_1);
            verify(tableImpl).getRows(container, ROWS_LOCATOR, SECTION_2);
        }

        @Test
        @DisplayName("readTable throws exception for class missing @TableInfo")
        void testReadTable_MissingTableInfo() {
            // Given - MissingTableInfoRow class has no @TableInfo

            // When / Then
            assertThatThrownBy(() -> tableImpl.readTable(MissingTableInfoRow.class))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("is missing @TableInfo annotation");
        }

        @Test
        @DisplayName("readTable throws exception for field missing @TableCellLocator")
        void testReadTable_MissingCellLocator() {
            // Given
            var row1 = createMockedRow("data");
            doReturn(List.of(row1)).when(tableImpl).getRows(any(), any(), anyString());
            TableField<RowWithMissingLocator> dummyField = TableField.of(RowWithMissingLocator::setMissingAnnotationField); // Create a dummy field

            // When / Then
            assertThatThrownBy(() -> tableImpl.readTable(RowWithMissingLocator.class, dummyField)) // Call with a field
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("is missing a @TableCellLocator annotation");
        }
    }

    @Nested
    @DisplayName("Table Insertion Tests (Public API)")
    class TableInsertionTest {

        // Constants for insertion tests
        private static final String INSERT_VAL = "inserted value";
        private static final String CUSTOM_INSERT_VAL = "custom insert value";
        private static final String SINGLE_ROW_VAL = "single row value";
        private static final List<String> INSERT_CRITERIA = List.of("insert criteria");
        private static final String[] MULTI_INSERT_VALS = {"ins1", "ins2"};
        private static final String[] INSERT_VALUES = {"insert1", "insert2"};

        @Mock private TableInsertion mockInsertionService; // Mock the service interface

        @BeforeEach
        void setupInsertionTests() {
            // Given
            // Reset static counters for custom functions
            WorkingCellInsertionFunction.reset();
            // Register the mock insertion service for component-based tests
            // Use the specific mock component type defined in the outer class
            lenient().when(registry.getTableService(eq(MockComponentTypeForTable.class))).thenReturn(mockInsertionService);
        }

        // --- Tests for inserting entire data object ---

        @Test
        @DisplayName("insertCellValue(row, class, data) should handle component insertion")
        void testInsertCellValue_RowAndData_Component() {
            // Given
            var data = new InsertionRow(); // Uses @CellInsertion
            data.setInsertionField(new TableCell(null, SINGLE_ROW_VAL));
            // Mock row/cell finding for the specific @TableCellLocator in InsertionRow
            when(rowElement.findSmartElements(eq(By.className("insCell")))).thenReturn(List.of(cellElement));
            // Mock enum lookup needed by insertUsingComponent (use try-with-resources for static mock)
            try (var reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 var uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
                setupStaticMocksForComponentLookup(reflectionUtilMock, uiConfigHolderMock); // Use helper
                reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(eq(MockComponentTypeForTable.class), eq("INSERT_TYPE"), anyString()))
                        .thenReturn(MockComponentTypeForTable.INSERT_TYPE);

                // When
                tableImpl.insertCellValue(1, InsertionRow.class, data);

                // Then
                verify(registry).getTableService(MockComponentTypeForTable.class);
                verify(mockInsertionService).tableInsertion(eq(cellElement), eq(MockComponentTypeForTable.INSERT_TYPE), eq(SINGLE_ROW_VAL));
                verify(tableImpl).getRows(container, ROWS_LOCATOR, "dummySection"); // Verify row finding was triggered
            }
        }

        @Test
        @DisplayName("insertCellValue(row, class, data) should handle custom function insertion")
        void testInsertCellValue_RowAndData_CustomFunction() {
            // Given
            var data = new CustomInsertionRow(); // Uses @CustomCellInsertion
            data.setCustomInsertionField(new TableCell(null, CUSTOM_INSERT_VAL));
            when(rowElement.findSmartElements(eq(By.className("customInsCell")))).thenReturn(List.of(cellElement));

            // When
            tableImpl.insertCellValue(1, CustomInsertionRow.class, data);

            // Then
            assertThat(WorkingCellInsertionFunction.callCount).isEqualTo(1);
            assertThat(WorkingCellInsertionFunction.lastCell).isSameAs(cellElement);
            assertThat(WorkingCellInsertionFunction.lastValues).containsExactly(CUSTOM_INSERT_VAL);
            verify(registry, never()).getTableService(any()); // Ensure component service not called
            verify(tableImpl).getRows(container, ROWS_LOCATOR, "dummySection");
        }

        @Test
        @DisplayName("insertCellValue(criteria, class, data) uses component insertion")
        void testInsertCellValue_CriteriaAndData_Component() {
            // Given
            var data = new InsertionRow();
            data.setInsertionField(new TableCell(null, SINGLE_ROW_VAL));
            when(rowElement.getAttribute("innerText")).thenReturn("match criteria");
            when(rowElement.findSmartElements(eq(By.className("insCell")))).thenReturn(List.of(cellElement));
            try (var reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 var uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
                setupStaticMocksForComponentLookup(reflectionUtilMock, uiConfigHolderMock);
                reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(eq(MockComponentTypeForTable.class), eq("INSERT_TYPE"), anyString()))
                        .thenReturn(MockComponentTypeForTable.INSERT_TYPE);

                // When
                tableImpl.insertCellValue(List.of("match"), InsertionRow.class, data);

                // Then
                verify(registry).getTableService(MockComponentTypeForTable.class);
                verify(mockInsertionService).tableInsertion(eq(cellElement), eq(MockComponentTypeForTable.INSERT_TYPE), eq(SINGLE_ROW_VAL));
                verify(tableImpl).getRows(container, ROWS_LOCATOR, "dummySection");
            }
        }

        @Test
        @DisplayName("insertCellValue(criteria, class, data) uses custom function insertion")
        void testInsertCellValue_CriteriaAndData_CustomFunction() {
            // Given
            var data = new CustomInsertionRow();
            data.setCustomInsertionField(new TableCell(null, CUSTOM_INSERT_VAL));
            when(rowElement.getAttribute("innerText")).thenReturn("match criteria");
            when(rowElement.findSmartElements(eq(By.className("customInsCell")))).thenReturn(List.of(cellElement));
            WorkingCellInsertionFunction.reset();

            // When
            tableImpl.insertCellValue(List.of("match"), CustomInsertionRow.class, data);

            // Then
            assertThat(WorkingCellInsertionFunction.callCount).isEqualTo(1);
            assertThat(WorkingCellInsertionFunction.lastCell).isSameAs(cellElement);
            assertThat(WorkingCellInsertionFunction.lastValues).containsExactly(CUSTOM_INSERT_VAL);
            verify(registry, never()).getTableService(any());
            verify(tableImpl).getRows(container, ROWS_LOCATOR, "dummySection");
        }

        // --- Tests for inserting specific field values ---

        @Test
        @DisplayName("insertCellValue(row, class, field, index, values...) uses component insertion")
        void testInsertCellValue_RowAndField_Component() {
            // Given
            TableField<InsertionRow> field = TableField.of(InsertionRow::setInsertionField);
            when(rowElement.findSmartElements(eq(By.className("insCell")))).thenReturn(List.of(cellElement));
            try (var reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 var uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
                setupStaticMocksForComponentLookup(reflectionUtilMock, uiConfigHolderMock);
                reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(eq(MockComponentTypeForTable.class), eq("INSERT_TYPE"), anyString()))
                        .thenReturn(MockComponentTypeForTable.INSERT_TYPE);

                // When
                tableImpl.insertCellValue(1, InsertionRow.class, field, 1, INSERT_VALUES);

                // Then
                verify(registry).getTableService(MockComponentTypeForTable.class);
                verify(mockInsertionService).tableInsertion(eq(cellElement), eq(MockComponentTypeForTable.INSERT_TYPE), eq(INSERT_VALUES));
                verify(tableImpl).getRows(container, ROWS_LOCATOR, "dummySection");
            }
        }

        @Test
        @DisplayName("insertCellValue(criteria, class, field, index, values...) uses component insertion")
        void testInsertCellValue_CriteriaAndField_Component() {
            // Given
            TableField<InsertionRow> field = TableField.of(InsertionRow::setInsertionField);
            when(rowElement.getAttribute("innerText")).thenReturn("match criteria");
            when(rowElement.findSmartElements(eq(By.className("insCell")))).thenReturn(List.of(cellElement));
            try (var reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 var uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
                setupStaticMocksForComponentLookup(reflectionUtilMock, uiConfigHolderMock);
                reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(eq(MockComponentTypeForTable.class), eq("INSERT_TYPE"), anyString()))
                        .thenReturn(MockComponentTypeForTable.INSERT_TYPE);

                // When
                tableImpl.insertCellValue(List.of("match"), InsertionRow.class, field, 1, INSERT_VALUES);

                // Then
                verify(registry).getTableService(MockComponentTypeForTable.class);
                verify(mockInsertionService).tableInsertion(eq(cellElement), eq(MockComponentTypeForTable.INSERT_TYPE), eq(INSERT_VALUES));
                verify(tableImpl).getRows(container, ROWS_LOCATOR, "dummySection");
            }
        }

        @Test
        @DisplayName("insertCellValue(row, class, field, index, value) uses custom function")
        void testInsertCellValue_RowAndField_CustomFunction() {
            // Given
            TableField<CustomInsertionRow> field = TableField.of(CustomInsertionRow::setCustomInsertionField);
            when(rowElement.findSmartElements(eq(By.className("customInsCell")))).thenReturn(List.of(cellElement));
            WorkingCellInsertionFunction.reset();

            // When
            tableImpl.insertCellValue(1, CustomInsertionRow.class, field, 1, INSERT_VALUES);

            // Then
            assertThat(WorkingCellInsertionFunction.callCount).isEqualTo(1);
            assertThat(WorkingCellInsertionFunction.lastCell).isSameAs(cellElement);
            assertThat(WorkingCellInsertionFunction.lastValues).isEqualTo(INSERT_VALUES);
            verify(registry, never()).getTableService(any());
            verify(tableImpl).getRows(container, ROWS_LOCATOR, "dummySection");
        }

        @Test
        @DisplayName("insertCellValue(criteria, class, field, index, value) uses custom function")
        void testInsertCellValue_CriteriaAndField_CustomFunction() {
            // Given
            TableField<CustomInsertionRow> field = TableField.of(CustomInsertionRow::setCustomInsertionField);
            when(rowElement.getAttribute("innerText")).thenReturn("match criteria");
            when(rowElement.findSmartElements(eq(By.className("customInsCell")))).thenReturn(List.of(cellElement));
            WorkingCellInsertionFunction.reset();

            // When
            tableImpl.insertCellValue(List.of("match"), CustomInsertionRow.class, field, 1, INSERT_VALUES);

            // Then
            assertThat(WorkingCellInsertionFunction.callCount).isEqualTo(1);
            assertThat(WorkingCellInsertionFunction.lastCell).isSameAs(cellElement);
            assertThat(WorkingCellInsertionFunction.lastValues).isEqualTo(INSERT_VALUES);
            verify(registry, never()).getTableService(any());
            verify(tableImpl).getRows(container, ROWS_LOCATOR, "dummySection");
        }

        // --- Error Handling Tests via Public API ---

        @Test
        @DisplayName("insertCellValue throws exception for invalid index")
        void testInsertCellValue_InvalidIndex() {
            // Given
            var data = new InsertionRow();
            data.setInsertionField(new TableCell(null, "insert-me"));
            // Make findSmartElements return an empty list for the cell locator
            when(rowElement.findSmartElements(eq(By.className("insCell")))).thenReturn(Collections.emptyList());
            try (var reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 var uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
                setupStaticMocksForComponentLookup(reflectionUtilMock, uiConfigHolderMock);
                reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(eq(MockComponentTypeForTable.class), eq("INSERT_TYPE"), anyString()))
                        .thenReturn(MockComponentTypeForTable.INSERT_TYPE);

                // When / Then
                assertThatThrownBy(() -> tableImpl.insertCellValue(1, InsertionRow.class, data))
                        .isInstanceOf(com.theairebellion.zeus.ui.components.table.exceptions.TableException.class)
                        .hasMessageContaining("Invalid cell index: 1");
            }
        }

        @Test
        @DisplayName("insertCellValue throws exception if no insertion method defined for field")
        void testInsertCellValue_NoMethodDefinedForField() {
            // Given
            var data = new DummyRow(); // DummyRow field has no @CellInsertion or @CustomCellInsertion
            data.setField1(new TableCell(null, "wont-insert"));

            // When / Then
            assertThatThrownBy(() -> tableImpl.insertCellValue(1, DummyRow.class, data))
                    .isInstanceOf(com.theairebellion.zeus.ui.components.table.exceptions.TableException.class)
                    .hasMessageContaining("No table cell insertion method provided for field: field1");
        }

        @Test
        @DisplayName("insertCellValue throws exception if registry is null for component insertion")
        void testInsertCellValue_ComponentButNoRegistry() {
            // Given
            var data = new InsertionRow();
            data.setInsertionField(new TableCell(null, "insert-me"));
            when(rowElement.findSmartElements(eq(By.className("insCell")))).thenReturn(List.of(cellElement));
            // Create instance without registry
            var tableImplNoRegistry = new TestTableImpl(driver, null, locators, container, rows, headerRowElement);

            // When / Then
            try (var reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 var uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
                setupStaticMocksForComponentLookup(reflectionUtilMock, uiConfigHolderMock);
                reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(eq(MockComponentTypeForTable.class), eq("INSERT_TYPE"), anyString()))
                        .thenReturn(MockComponentTypeForTable.INSERT_TYPE);

                assertThatThrownBy(() -> tableImplNoRegistry.insertCellValue(1, InsertionRow.class, data))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("Your instance of table is not having registered services");
            }
        }

        @Test
        @DisplayName("insertCellValue uses custom function if provided")
        void testInsertCellValue_WithCustomFunction() {
            // Given
            var data = new RowWithCustomInsertion();
            data.setField(new TableCell(null, "val1")); // Set the value we want to insert
            when(rowElement.findSmartElements(eq(By.id("c")))).thenReturn(List.of(cellElement));

            SmartWebElement badTableContainer = mock(SmartWebElement.class);
            when(driver.findSmartElement(By.id("t"))).thenReturn(badTableContainer);
            when(badTableContainer.findSmartElements(By.id("r"))).thenReturn(rows);

            ValidCustomInsertionFunction.called = false;
            ValidCustomInsertionFunction.lastElement = null;
            ValidCustomInsertionFunction.lastValues = null;

            String[] testValues = {"val1"}; // Adjust to match the single value we are setting

            // When
            tableImpl.insertCellValue(1, RowWithCustomInsertion.class, data);

            // Then
            assertThat(ValidCustomInsertionFunction.called).isTrue();
            assertThat(ValidCustomInsertionFunction.lastElement).isEqualTo(cellElement);
            assertThat(ValidCustomInsertionFunction.lastValues).isEqualTo(testValues);
        }
    }

    // Helper to setup static mocks for component lookup (used in insertion/filter tests)
    private void setupStaticMocksForComponentLookup(MockedStatic<ReflectionUtil> reflectionUtilMock, MockedStatic<UiConfigHolder> uiConfigHolderMock) {
        var uiConfig = mock(UiConfig.class);
        uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
        // Use lenient() if these might not be called in every path of the test using the helper
        lenient().when(uiConfig.projectPackage()).thenReturn("com.theairebellion.zeus"); // Or test package
    }

    // Function with private constructor for negative tests
    static class PrivateConstructorInsertionFunction implements CellInsertionFunction {
        private PrivateConstructorInsertionFunction() {
            // Private constructor makes it non-instantiable via reflection's default newInstance()
        }

        @Override
        public void cellInsertionFunction(SmartWebElement cellElement, String... values) {
            // Implementation not needed for this test scenario
        }

        @Override
        public void accept(SmartWebElement cell, String[] values) {
            // Implementation not needed for this test scenario
        }
    }

    @Nested
    @DisplayName("Table Filtering Tests (Public API)")
    class TableFilteringTest {

        @Mock private TableFilter mockFilterService; // Mock the service interface

        // Constants for filtering tests
        private static final String FILTER_VALUE = "filter value";
        private static final String[] FILTER_VALUES = {"filter value", "another"};
        private static final String HEADER_CELL_FILTER_CLASS = "filterHeaderCell"; // From FilterRow annotation
        private static final String COMPONENT_TYPE_FILTER = "FILTER_TYPE"; // From FilterRow annotation


        @BeforeEach
        void setupFilteringTests() {
            // Given
            // Reset static counter for custom function mock
            WorkingCellFilterFunction.reset();
            // Register the mock filter service
            lenient().when(registry.getFilterService(eq(MockComponentTypeForTable.class))).thenReturn(mockFilterService);
            // Mock header cell finding
            lenient().when(headerRowElement.findSmartElement(any(By.class))).thenReturn(headerCellElement);
        }

        @Test
        @DisplayName("filterTable uses component filter correctly")
        void testFilterTable_Component() {
            // Given
            TableField<FilterRow> field = TableField.of(FilterRow::setFilterField); // Field annotated with @CellFilter
            when(headerRowElement.findSmartElement(eq(By.className(HEADER_CELL_FILTER_CLASS)))).thenReturn(headerCellElement);
            // Mock ReflectionUtil needed by filterCellsUsingComponent
            try (var reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 var uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
                setupStaticMocksForComponentLookup(reflectionUtilMock, uiConfigHolderMock); // Use helper
                // Mock enum lookup to return the specific type from the annotation
                reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(eq(MockComponentTypeForTable.class), eq(COMPONENT_TYPE_FILTER), anyString()))
                        .thenReturn(MockComponentTypeForTable.FILTER_TYPE);

                // When
                tableImpl.filterTable(FilterRow.class, field, FilterStrategy.SELECT_ONLY, FILTER_VALUES);

                // Then
                verify(tableImpl).getHeaderRow(container, HEADER_LOCATOR, "dummySection"); // From @TableInfo
                verify(headerRowElement).findSmartElement(By.className(HEADER_CELL_FILTER_CLASS)); // From @TableCellLocator
                verify(registry).getFilterService(MockComponentTypeForTable.class); // Verify registry lookup
                verify(mockFilterService).tableFilter(eq(headerCellElement), eq(MockComponentTypeForTable.FILTER_TYPE), eq(FilterStrategy.SELECT_ONLY), eq(FILTER_VALUES));
                verifyNoMoreInteractions(mockFilterService);
            }
        }

        @Test
        @DisplayName("filterTable uses custom function correctly")
        void testFilterTable_CustomFunction() {
            // Given
            TableField<CustomFilterRow> field = TableField.of(CustomFilterRow::setCustomFilterField); // Field annotated with @CustomCellFilter
            when(headerRowElement.findSmartElement(eq(CUSTOM_FILTER_HEADER_CELL_LOCATOR))).thenReturn(headerCellElement);
            WorkingCellFilterFunction.reset();

            // When
            tableImpl.filterTable(CustomFilterRow.class, field, FilterStrategy.UNSELECT, FILTER_VALUE);

            // Then
            verify(tableImpl).getHeaderRow(container, HEADER_LOCATOR, "dummySection");
            verify(headerRowElement).findSmartElement(CUSTOM_FILTER_HEADER_CELL_LOCATOR);
            assertThat(WorkingCellFilterFunction.callCount).isEqualTo(1);
            assertThat(WorkingCellFilterFunction.lastCell).isSameAs(headerCellElement);
            assertThat(WorkingCellFilterFunction.lastStrategy).isEqualTo(FilterStrategy.UNSELECT);
            assertThat(WorkingCellFilterFunction.lastValues).containsExactly(FILTER_VALUE);
            verify(registry, never()).getFilterService(any()); // Ensure component service not called
        }

        @Test
        @DisplayName("filterTable throws exception if no filter method defined for field")
        void testFilterTable_NoFilterAnnotation() {
            // Given
            TableField<DummyRow> field = TableField.of(DummyRow::setField1); // DummyRow field has no filter annotations

            // When / Then
            assertThatThrownBy(() -> tableImpl.filterTable(DummyRow.class, field, FilterStrategy.SELECT, FILTER_VALUE))
                    .isInstanceOf(com.theairebellion.zeus.ui.components.table.exceptions.TableException.class)
                    // Check implementation detail: filterCells reuses exception message from insertion check currently
                    .hasMessageContaining("No table cell insertion method provided for field: field1");
        }

        @Test
        @DisplayName("filterTable throws exception if registry is null for component filter")
        void testFilterTable_ComponentButNoRegistry() {
            // Given
            TableField<FilterRow> field = TableField.of(FilterRow::setFilterField);
            when(headerRowElement.findSmartElement(any(By.class))).thenReturn(headerCellElement);
            // Create instance without registry
            var tableImplNoRegistry = new TestTableImpl(driver, null, locators, container, rows, headerRowElement);

            // When / Then
            try (var reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 var uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
                setupStaticMocksForComponentLookup(reflectionUtilMock, uiConfigHolderMock);
                reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(eq(MockComponentTypeForTable.class), eq(COMPONENT_TYPE_FILTER), anyString()))
                        .thenReturn(MockComponentTypeForTable.FILTER_TYPE);

                assertThatThrownBy(() -> tableImplNoRegistry.filterTable(FilterRow.class, field, FilterStrategy.SELECT, FILTER_VALUE))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("Your instance of table is not having registered services");
            }
        }

        @Test
        @DisplayName("filterTable throws exception if component type lookup fails")
        void testFilterTable_ComponentLookupError() {
            // Given
            TableField<FilterRow> field = TableField.of(FilterRow::setFilterField);
            when(headerRowElement.findSmartElement(any(By.class))).thenReturn(headerCellElement);
            when(registry.getFilterService(eq(MockComponentTypeForTable.class))).thenReturn(mockFilterService); // Registry has service

            // Mock ReflectionUtil to throw error during enum lookup
            try (var reflectionUtilMock = mockStatic(ReflectionUtil.class);
                 var uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
                setupStaticMocksForComponentLookup(reflectionUtilMock, uiConfigHolderMock);
                reflectionUtilMock.when(() -> ReflectionUtil.findEnumImplementationsOfInterface(eq(MockComponentTypeForTable.class), eq(COMPONENT_TYPE_FILTER), anyString()))
                        .thenThrow(new ReflectionException("Simulated Lookup Failure"));

                // When / Then
                assertThatThrownBy(() -> tableImpl.filterTable(FilterRow.class, field, FilterStrategy.SELECT, FILTER_VALUE))
                        .isInstanceOf(com.theairebellion.zeus.ui.components.table.exceptions.TableException.class)
                        .hasMessageContaining("Failed to filter using component")
                        .cause() // Check cause from ReflectionUtil
                        .isInstanceOf(ReflectionException.class)
                        .hasMessageContaining("Simulated Lookup Failure");
            }
        }

        @Test
        @DisplayName("filterTable throws exception if custom function fails instantiation")
        void testFilterTable_BadCustomFunction() {
            // Given
            TableField<BadCustomFilterRow> field = TableField.of(BadCustomFilterRow::setField);
            when(headerRowElement.findSmartElement(any(By.class))).thenReturn(headerCellElement);

            // When / Then
            assertThatThrownBy(() -> tableImpl.filterTable(BadCustomFilterRow.class, field, FilterStrategy.SELECT, FILTER_VALUE))
                    .isInstanceOf(com.theairebellion.zeus.ui.components.table.exceptions.TableException.class)
                    .hasMessageContaining("Failed to instantiate custom cell filter function");
        }
    }

    // Helper class with private constructor for testing instantiation failure
    static class PrivateConstructorFilterFunction implements CellFilterFunction {
        private PrivateConstructorFilterFunction() {
            // Private constructor prevents easy instantiation via reflection's newInstance()
        }

        @Override
        public void cellFilterFunction(SmartWebElement cellElement, FilterStrategy filterStrategy, String... values) {
            // Implementation not needed as instantiation should fail first
            fail("Instantiation should have failed for PrivateConstructorFilterFunction");
        }
    }

    @Nested
    @DisplayName("Table Sorting Tests (Public API)")
    class TableSortingTest {

        @Test
        @DisplayName("sortTable delegates to protected helper with correct arguments")
        void testSortTableDelegatesToProtectedHelper() {
            // Given
            // Use TableField.of for clarity and type safety
            TableField<DummyRow> field = TableField.of(DummyRow::setField1);
            var strategy = SortingStrategy.DESC;

            // Reset spy state if needed (might not be necessary depending on test structure)
            reset(tableImpl); // Reset interactions on the spy itself

            // When
            tableImpl.sortTable(DummyRow.class, field, strategy);

            // Then
            // Verify the internal protected sortTable method on our spied TestTableImpl was called
            verify(tableImpl).sortTable(eq(headerCellElement), eq(strategy));

            // Optionally verify the interactions that lead to finding the header cell
            verify(tableImpl).getHeaderRow(container, HEADER_LOCATOR, SECTION_1);
            verify(headerRowElement).findSmartElement(HEADER_CELL_LOCATOR_DUMMY);

            // Verify the specific tracking fields in the TestTableImpl spy instance
            assertThat(tableImpl.sortTableCalled).isTrue();
            assertThat(tableImpl.sortHeaderCellArg).isSameAs(headerCellElement);
            assertThat(tableImpl.sortStrategyArg).isSameAs(strategy);
        }
    }

    @Nested
    @DisplayName("Field Extraction and Mapping Tests (Internal Logic)")
    class FieldExtractionTest {

        // --- Tests for validateFieldInvokers ---
        @Test
        @DisplayName("validateFieldInvokers should succeed with valid TableField")
        void testValidateFieldInvokers_Success() throws Exception {
            // Given
            var validateMethod = TableImpl.class.getDeclaredMethod(
                    "validateFieldInvokers", Object.class, List.class);
            validateMethod.setAccessible(true);
            var row = new DummyRow();
            // Valid field accepts TableCell
            TableField<DummyRow> validField = (instance, value) -> {
                if (value instanceof TableCell) {
                    instance.setField1((TableCell) value);
                } else {
                    throw new ClassCastException("Invalid type for TableCell");
                }
            };
            var fields = List.of(validField);

            // When & Then
            // Should not throw because TableImpl passes TableCell or List instances
            assertDoesNotThrow(() -> validateMethod.invoke(tableImpl, row, fields));
        }

        @Test
        @DisplayName("validateFieldInvokers should throw exception for invalid TableField")
        void testValidateFieldInvokers_Failure() throws NoSuchMethodException {
            // Given
            var validateMethod = TableImpl.class.getDeclaredMethod(
                    "validateFieldInvokers", Object.class, List.class);
            validateMethod.setAccessible(true);
            var row = new DummyRow();
            // Invalid field throws for any accepted value type
            TableField<DummyRow> invalidField = (instance, value) -> {
                throw new IllegalArgumentException("Setter failed validation");
            };
            var fields = List.of(invalidField);

            // When & Then
            var thrown = catchThrowable(() -> validateMethod.invoke(tableImpl, row, fields));
            assertThat(thrown).isInstanceOf(InvocationTargetException.class)
                    .cause().isInstanceOf(IllegalArgumentException.class); // Check only the exception type
        }

        // --- Tests for extractAnnotatedFields ---
        @Test
        @DisplayName("extractAnnotatedFields with empty fields list returns all annotated fields")
        void testExtractAnnotatedFields_EmptyFieldsList() throws Exception {
            // Given
            var extractMethod = TableImpl.class.getDeclaredMethod(
                    "extractAnnotatedFields", Class.class, List.class);
            extractMethod.setAccessible(true);

            // When
            @SuppressWarnings("unchecked") // Suppress for cast from reflection
            List<CellLocator> result = (List<CellLocator>)
                    extractMethod.invoke(tableImpl, DummyRow.class, Collections.emptyList());

            // Then
            assertThat(result).isNotNull().hasSize(1);
            assertThat(result.get(0).getFieldName()).isEqualTo("field1"); // Match field name in DummyRow
            assertThat(result.get(0).getTableSection()).isEqualTo(SECTION_1);
        }

        @Test
        @DisplayName("extractAnnotatedFields with field invokers returns matching fields")
        void testExtractAnnotatedFields_WithFieldInvokers() throws Exception {
            // Given
            var extractMethod = TableImpl.class.getDeclaredMethod(
                    "extractAnnotatedFields", Class.class, List.class);
            extractMethod.setAccessible(true);
            // Create field invoker matching the field in DummyRow
            TableField<DummyRow> field = TableField.of(DummyRow::setField1);
            var fieldsToExtract = List.of(field);
            // Set dummy value on an instance to simulate field being "used" by the TableField
            // This is needed to pass the filtering logic inside extractAnnotatedFields
            var dummyInstance = new DummyRow();
            field.invoke(dummyInstance, new TableCell("temp"));

            // When
            @SuppressWarnings("unchecked")
            List<CellLocator> result = (List<CellLocator>)
                    extractMethod.invoke(tableImpl, DummyRow.class, fieldsToExtract);

            // Then
            assertThat(result).isNotNull().hasSize(1);
            assertThat(result.get(0).getFieldName()).isEqualTo("field1");
        }

        @Test
        @DisplayName("extractAnnotatedFields throws exception for invalid field type syntax")
        void testExtractAnnotatedFields_InvalidSyntax() throws Exception {
            // Given
            var extractMethod = TableImpl.class.getDeclaredMethod(
                    "extractAnnotatedFields", Class.class, List.class);
            extractMethod.setAccessible(true);

            // When / Then
            try (var logMock = mockStatic(LogUI.class)) {
                var thrown = catchThrowable(() -> extractMethod.invoke(tableImpl, InvalidRow.class, Collections.emptyList()));

                assertThat(thrown)
                        .isInstanceOf(InvocationTargetException.class)
                        .cause()
                        .isInstanceOf(com.theairebellion.zeus.ui.components.table.exceptions.TableException.class)
                        .hasMessageContaining("Invalid field type for table cell usage");

                logMock.verify(() -> LogUI.error(contains("Some fields are not TableCell")));
            }
        }

        @Test
        @DisplayName("extractAnnotatedFields throws exception for field missing TableCellLocator when fields provided")
        void testExtractAnnotatedFields_MissingLocatorWithFields() throws Exception {
            // Given
            var extractMethod = TableImpl.class.getDeclaredMethod(
                    "extractAnnotatedFields", Class.class, List.class);
            extractMethod.setAccessible(true);
            var instance = new RowMissingLocator();
            // Provide TableField for the field *without* the annotation
            TableField<RowMissingLocator> field = TableField.of(RowMissingLocator::setFieldWithoutLocator);
            instance.setFieldWithoutLocator(new TableCell("value")); // Mark as 'used'

            // When / Then
            var thrown = catchThrowable(() -> extractMethod.invoke(tableImpl, RowMissingLocator.class, List.of(field)));
            assertThat(thrown)
                    .isInstanceOf(InvocationTargetException.class)
                    .cause()
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("is missing a @TableCellLocator annotation");
        }


        // --- Tests for mapToCellLocator ---
        // Testing different annotation combinations

        @Test
        @DisplayName("mapToCellLocator creates CellLocator for basic field")
        void testMapToCellLocator_BasicField() throws Exception {
            // Given
            var mapMethod = TableImpl.class.getDeclaredMethod("mapToCellLocator", Field.class);
            mapMethod.setAccessible(true);
            var field = DummyRow.class.getDeclaredField("field1");

            // When
            var result = (CellLocator) mapMethod.invoke(tableImpl, field);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFieldName()).isEqualTo("field1");
            assertThat(result.getTableSection()).isEqualTo(SECTION_1);
            assertThat(result.getLocator()).isEqualTo(CELL_LOCATOR_DUMMY);
            assertThat(result.getCellTextLocator()).isEqualTo(TEXT_LOCATOR_DUMMY);
            assertThat(result.getHeaderCellLocator()).isEqualTo(HEADER_CELL_LOCATOR_DUMMY);
            assertThat(result.getCellInsertionComponent()).isNull();
            assertThat(result.getCustomCellInsertion()).isNull();
            assertThat(result.getCellFilterComponent()).isNull();
            assertThat(result.getCustomCellFilter()).isNull();
            assertThat(result.isCollection()).isFalse();
        }

        @Test
        @DisplayName("mapToCellLocator handles cell insertion annotation")
        void testMapToCellLocator_WithCellInsertion() throws Exception {
            // Given
            var mapMethod = TableImpl.class.getDeclaredMethod("mapToCellLocator", Field.class);
            mapMethod.setAccessible(true);
            var field = InsertionRow.class.getDeclaredField("insertionField");

            // When
            var result = (CellLocator) mapMethod.invoke(tableImpl, field);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFieldName()).isEqualTo("insertionField");
            assertThat(result.getCellInsertionComponent()).isNotNull();
            assertThat(result.getCellInsertionComponent().getType()).isEqualTo(MockComponentTypeForTable.class);
            assertThat(result.getCellInsertionComponent().getComponentType()).isEqualTo("INSERT_TYPE");
            assertThat(result.getCellInsertionComponent().getOrder()).isEqualTo(1);
            assertThat(result.getCustomCellInsertion()).isNull();
        }

        @Test
        @DisplayName("mapToCellLocator handles custom cell insertion annotation")
        void testMapToCellLocator_WithCustomCellInsertion() throws Exception {
            // Given
            var mapMethod = TableImpl.class.getDeclaredMethod("mapToCellLocator", Field.class);
            mapMethod.setAccessible(true);
            var field = CustomInsertionRow.class.getDeclaredField("customInsertionField");

            // When
            var result = (CellLocator) mapMethod.invoke(tableImpl, field);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFieldName()).isEqualTo("customInsertionField");
            assertThat(result.getCellInsertionComponent()).isNull();
            assertThat(result.getCustomCellInsertion()).isEqualTo(WorkingCellInsertionFunction.class);
        }

        @Test
        @DisplayName("mapToCellLocator handles cell filter annotation")
        void testMapToCellLocator_WithCellFilter() throws Exception {
            // Given
            var mapMethod = TableImpl.class.getDeclaredMethod("mapToCellLocator", Field.class);
            mapMethod.setAccessible(true);
            var field = FilterRow.class.getDeclaredField("filterField");

            // When
            var result = (CellLocator) mapMethod.invoke(tableImpl, field);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFieldName()).isEqualTo("filterField");
            assertThat(result.getCellFilterComponent()).isNotNull();
            assertThat(result.getCellFilterComponent().getType()).isEqualTo(MockComponentTypeForTable.class);
            assertThat(result.getCellFilterComponent().getComponentType()).isEqualTo("FILTER_TYPE");
            assertThat(result.getCustomCellFilter()).isNull();
        }

        @Test
        @DisplayName("mapToCellLocator handles custom cell filter annotation")
        void testMapToCellLocator_WithCustomCellFilter() throws Exception {
            // Given
            var mapMethod = TableImpl.class.getDeclaredMethod("mapToCellLocator", Field.class);
            mapMethod.setAccessible(true);
            var field = CustomFilterRow.class.getDeclaredField("customFilterField");

            // When
            var result = (CellLocator) mapMethod.invoke(tableImpl, field);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getFieldName()).isEqualTo("customFilterField");
            assertThat(result.getCellFilterComponent()).isNull();
            assertThat(result.getCustomCellFilter()).isEqualTo(WorkingCellFilterFunction.class);
        }
    }

    @Nested
    @DisplayName("Lambda Expressions and Advanced Functionality Tests")
    class LambdaAndAdvancedTests {

        // Constants for local row model
        private static final String FIELD1_TEXT = "field1";
        private static final String FIELD2_TEXT = "field2";
        private static final String UNIQUE_FIELD1_TEXT = "UNIQUE_TEST_FIELD1";
        private static final String UNIQUE_FIELD2_TEXT = "UNIQUE_TEST_FIELD2";
        private static final String CELL1_LOCATOR_STR = "cell1";
        private static final String CELL2_LOCATOR_STR = "cell2";
        private static final String TEXT1_LOCATOR_STR = "text1";
        private static final String TEXT2_LOCATOR_STR = "text2";
        private static final String HEADER1_LOCATOR_STR = "header1";
        private static final String HEADER2_LOCATOR_STR = "header2";
        private static final String SECTION_NAME = "section";

        @Test
        @DisplayName("processInsertCellValue should process fields in specified order")
        void testProcessInsertCellValue_Order() throws Exception {
            // Given: Reflection setup for private method
            var processMethod = TableImpl.class.getDeclaredMethod(
                    "processInsertCellValue", BiConsumer.class, Class.class, Object.class);
            processMethod.setAccessible(true);

            // Row model with fields having different @CellInsertion orders
            @TableInfo(tableContainerLocator=@FindBy(id="container"),rowsLocator=@FindBy(id="rows"),headerRowLocator=@FindBy(id="header"))
            class OrderedFieldsRow {
                @TableCellLocator(cellLocator = @FindBy(id = CELL1_LOCATOR_STR), cellTextLocator = @FindBy(id = TEXT1_LOCATOR_STR), headerCellLocator = @FindBy(id = HEADER1_LOCATOR_STR), tableSection = SECTION_NAME)
                @CellInsertion(type = MockComponentTypeForTable.class, componentType = "TEST", order = 2)
                private TableCell field1 = new TableCell(null, FIELD1_TEXT);

                @TableCellLocator(cellLocator = @FindBy(id = CELL2_LOCATOR_STR), cellTextLocator = @FindBy(id = TEXT2_LOCATOR_STR), headerCellLocator = @FindBy(id = HEADER2_LOCATOR_STR), tableSection = SECTION_NAME)
                @CellInsertion(type = MockComponentTypeForTable.class, componentType = "TEST", order = 1)
                private TableCell field2 = new TableCell(null, FIELD2_TEXT);

                public TableCell getField1() { return field1; }
                public void setField1(TableCell cell) { field1 = cell; }
                public TableCell getField2() { return field2; }
                public void setField2(TableCell cell) { field2 = cell; }
            }

            var dataRow = new OrderedFieldsRow();
            var invocationOrder = new ArrayList<String>();

            // Consumer to record the effective field name based on unique values
            BiConsumer<TableField<OrderedFieldsRow>, String[]> recordingConsumer = (field, values) -> {
                var testRowForField1 = new OrderedFieldsRow();
                var testRowForField2 = new OrderedFieldsRow();
                testRowForField1.setField1(new TableCell(null, UNIQUE_FIELD1_TEXT));
                testRowForField2.setField2(new TableCell(null, UNIQUE_FIELD2_TEXT));

                try {
                    var tempCell = new TableCell(null, "temp");
                    field.invoke(testRowForField1, tempCell); // Try invoking on row where field1 is unique
                    if (testRowForField1.getField1() != tempCell) { // If field1 wasn't changed, it must be field2
                        invocationOrder.add("field2");
                    } else {
                        invocationOrder.add("field1");
                    }
                } catch (Exception e) {
                    fail("Invocation failed during field identification", e);
                }
            };

            // When
            processMethod.invoke(tableImpl, recordingConsumer, OrderedFieldsRow.class, dataRow);

            // Then
            assertThat(invocationOrder).as("Should process exactly 2 fields").hasSize(2);
            assertThat(invocationOrder.get(0)).as("Field2 (order=1) should be processed first").isEqualTo("field2");
            assertThat(invocationOrder.get(1)).as("Field1 (order=2) should be processed second").isEqualTo("field1");
        }
    }

    @Test
    @DisplayName("readTable(Class, fields...) handles null fields parameter correctly")
    void testReadTable_ClassWithNullFields() {
        // Given
        // Setup mocks for row/cell finding
        when(rowElement.findSmartElement(eq(CELL_LOCATOR_DUMMY))).thenReturn(cellElement);
        when(cellElement.findSmartElement(eq(TEXT_LOCATOR_DUMMY))).thenReturn(cellElement);
        when(cellElement.getText()).thenReturn(CELL_TEXT_VALUE);
        // Ensure getRows returns our rowElement
        doReturn(List.of(rowElement)).when(tableImpl).getRows(any(), any(), anyString());

        // When
        // Call public method with null for the varargs parameter
        var result = tableImpl.readTable(DummyRow.class, (TableField<DummyRow>[]) null);

        // Then
        // Should read all annotated fields as if no fields were specified
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getField1().getText()).isEqualTo(CELL_TEXT_VALUE);
        verify(tableImpl).getRows(container, ROWS_LOCATOR, SECTION_1); // Verify protected method called
    }

    @Test
    @DisplayName("readTable(start, end, Class, fields...) handles null fields parameter correctly")
    void testReadTable_RangeWithNullFields() {
        // Given
        var rowList = List.of(rowElement); // Only one row for simplicity
        doReturn(rowList).when(tableImpl).getRows(any(), any(), anyString());
        when(rowElement.findSmartElement(eq(CELL_LOCATOR_DUMMY))).thenReturn(cellElement);
        when(cellElement.findSmartElement(eq(TEXT_LOCATOR_DUMMY))).thenReturn(cellElement);
        when(cellElement.getText()).thenReturn(CELL_TEXT_VALUE);


        // When
        // Call public method with null for the varargs parameter
        var result = tableImpl.readTable(1, 2, DummyRow.class, (TableField<DummyRow>[]) null);

        // Then
        // Should read row 1 (index 0) with all annotated fields
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getField1().getText()).isEqualTo(CELL_TEXT_VALUE);
        verify(tableImpl).getRows(container, ROWS_LOCATOR, SECTION_1);
    }

    @Test
    @DisplayName("readTable handles null vs empty fields parameter similarly")
    void testReadTable_LambdaCoverage_NullVsEmptyFields() {
        // Given
        var rowList = List.of(rowElement, rowElement, rowElement); // 3 rows
        doReturn(rowList).when(tableImpl).getRows(any(), any(), anyString());
        when(rowElement.findSmartElement(eq(CELL_LOCATOR_DUMMY))).thenReturn(cellElement);
        when(cellElement.findSmartElement(eq(TEXT_LOCATOR_DUMMY))).thenReturn(cellElement);
        when(cellElement.getText()).thenReturn(CELL_TEXT_VALUE);

        // When
        // 1. Call with null fields varargs
        var result1 = tableImpl.readTable(DummyRow.class, (TableField<DummyRow>[]) null);
        // 2. Call with empty fields varargs
        var result2 = tableImpl.readTable(DummyRow.class, new TableField[0]);

        // Then
        // Both should read all annotated fields for all rows
        assertThat(result1).isNotNull();
        assertThat(result1).hasSize(3);
        assertThat(result2).isNotNull();
        assertThat(result2).hasSize(3);
        // Verify getRows was called (likely twice overall for this test)
        verify(tableImpl, times(2)).getRows(container, ROWS_LOCATOR, SECTION_1);
    }
}