package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.table.annotations.*;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.CellFilterFunction;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionFunction;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.components.table.model.TableCell;
import com.theairebellion.zeus.ui.components.table.model.TableLocators;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.service.mock.MockComponentType;
import com.theairebellion.zeus.ui.components.table.service.mock.MockRow;
import com.theairebellion.zeus.ui.components.table.service.mock.MockTableField;
import com.theairebellion.zeus.ui.components.table.service.mock.TestTableImpl;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.support.FindBy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
public class TableImplConsolidatedTest extends BaseUnitUITest {

    private SmartWebDriver driver;
    private SmartWebElement container;
    private SmartWebElement rowElement;
    private SmartWebElement cellElement;
    private List<SmartWebElement> rows;
    private TableLocators locators;
    private TableServiceRegistry registry;
    private TestTableImpl tableImpl;

    @BeforeEach
    void setUp() {
        driver = mock(SmartWebDriver.class);
        container = mock(SmartWebElement.class);
        rowElement = mock(SmartWebElement.class);
        cellElement = mock(SmartWebElement.class);
        rows = new ArrayList<>();
        locators = new TableLocators(By.id("dummyTable"), By.className("dummyRow"), By.className("dummyHeader"));
        registry = new TableServiceRegistry();
        tableImpl = new TestTableImpl(driver, registry, locators, container, rows);
        when(rowElement.findSmartElement(any(By.class))).thenReturn(cellElement);
        when(cellElement.findSmartElement(any(By.class))).thenReturn(cellElement);
        when(cellElement.getText()).thenReturn("value");
        rows.add(rowElement);
    }

    @Test
    void testConvertFieldValueToStrings_List() throws Exception {
        DummyRowWithList row = new DummyRowWithList();
        Field f = DummyRowWithList.class.getDeclaredField("cells");
        f.setAccessible(true);
        Method m = TableImpl.class.getDeclaredMethod("convertFieldValueToStrings", Field.class, Object.class);
        m.setAccessible(true);
        String[] result = (String[]) m.invoke(tableImpl, f, row);
        assertArrayEquals(new String[]{"listValue"}, result);
    }

    @Test
    void testConvertFieldValueToStrings_Unsupported() throws Exception {
        DummyRowUnsupported row = new DummyRowUnsupported();
        Field f = DummyRowUnsupported.class.getDeclaredField("value");
        f.setAccessible(true);
        Method m = TableImpl.class.getDeclaredMethod("convertFieldValueToStrings", Field.class, Object.class);
        m.setAccessible(true);
        String[] result = (String[]) m.invoke(tableImpl, f, row);
        assertArrayEquals(new String[0], result);
    }

    @Test
    void testInvokeSetterSuccess() throws Exception {
        DummySetter dummy = new DummySetter();
        Method m = TableImpl.class.getDeclaredMethod("invokeSetter", Object.class, String.class, Object.class);
        m.setAccessible(true);
        m.invoke(tableImpl, dummy, "data", "testValue");
        assertEquals("testValue", dummy.getData());
    }

    @Test
    void testFindRowElementUnsupported() throws Exception {
        when(container.findSmartElements(any(By.class))).thenReturn(Collections.emptyList());
        Method m = TableImpl.class.getDeclaredMethod("findRowElement", SmartWebElement.class, By.class, Object.class, String.class);
        m.setAccessible(true);
        Exception ex = assertThrows(Exception.class, () ->
                m.invoke(tableImpl, container, By.id("dummy"), "unsupported", "sec"));
        assertTrue(ex.getCause().getMessage().contains("Unsupported row identifier type"));
    }

    @Test
    void testFindRowByCriteriaNotFound() throws Exception {
        when(rowElement.getAttribute("innerText")).thenReturn("abc");
        Method m = TableImpl.class.getDeclaredMethod("findRowByCriteria", List.class, List.class);
        m.setAccessible(true);
        Exception ex = assertThrows(Exception.class, () ->
                m.invoke(tableImpl, List.of("xyz"), List.of(rowElement)));
        assertTrue(ex.getCause().getMessage().contains("No row found containing all criteria"));
    }

    @Test
    void testCreateInstanceFailure() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("createInstance", Class.class);
        m.setAccessible(true);
        Exception ex = assertThrows(Exception.class, () -> m.invoke(tableImpl, NoNoArgConstructor.class));
        assertTrue(ex.getCause().getMessage().contains("Could not create a new instance"));
    }

    @Test
    void testGetTableLocatorsMissingAnnotation() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("getTableLocators", Class.class);
        m.setAccessible(true);
        Exception ex = assertThrows(Exception.class, () -> m.invoke(tableImpl, NoTableInfoRow.class));
        assertTrue(ex.getCause().getMessage().contains("is missing @TableInfo annotation"));
    }

    @Test
    void testPrepareFieldInvokersMapFailure() throws Exception {
        DummyFailRow row = new DummyFailRow();
        Method m = TableImpl.class.getDeclaredMethod("prepareFieldInvokersMap", Class.class, Object.class);
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<TableField<DummyFailRow>, String[]> invokersMap = (Map<TableField<DummyFailRow>, String[]>) m.invoke(tableImpl, DummyFailRow.class, row);
        for (TableField<DummyFailRow> fieldInvoker : invokersMap.keySet()) {
            Exception ex = assertThrows(Exception.class, () -> fieldInvoker.invoke(row, "dummyValue"));
            assertNotNull(ex);
        }
    }

    @Test
    void testReadRowsInRangeAll() throws Exception {
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        Method m = TableImpl.class.getDeclaredMethod("readRowsInRange", SmartWebElement.class, By.class, String.class, Integer.class, Integer.class);
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<SmartWebElement> result = (List<SmartWebElement>) m.invoke(tableImpl, container, By.id("dummy"), "sec", null, null);
        assertEquals(rows, result);
    }

    @Test
    void testMergeRowsAcrossSectionsEmpty() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("mergeRowsAcrossSections", Map.class, Map.class, Class.class);
        m.setAccessible(true);
        Map<String, List<SmartWebElement>> emptyRows = Collections.emptyMap();
        Map<String, List<?>> dummyLocators = Collections.emptyMap();
        @SuppressWarnings("unchecked")
        List<?> merged = (List<?>) m.invoke(tableImpl, emptyRows, dummyLocators, DummyRowWithList.class);
        assertTrue(merged.isEmpty());
    }

    @Test
    void testReadTableNoFields() {
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(cellElement.getText()).thenReturn("someValue");
        List<MockRow> result = tableImpl.readTable(MockRow.class);
        assertEquals(1, result.size());
        assertEquals("someValue", result.get(0).getCell().getText());
    }

    @Test
    void testReadRowIndexOk() {
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(cellElement.getText()).thenReturn("rowVal");
        MockRow row = tableImpl.readRow(1, MockRow.class);
        assertNotNull(row.getCell());
        assertEquals("rowVal", row.getCell().getText());
    }

    @Test
    void testReadRowIndexOutOfRange() {
        when(container.findSmartElements(any(By.class))).thenReturn(Collections.emptyList());
        assertThrows(IndexOutOfBoundsException.class, () ->
                tableImpl.readRow(3, MockRow.class)
        );
    }

    @Test
    void testReadRowByCriteria() {
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(rowElement.getAttribute("innerText")).thenReturn("some-match");
        when(cellElement.getText()).thenReturn("searchValue");
        MockRow row = tableImpl.readRow(List.of("some-match"), MockRow.class);
        assertEquals("searchValue", row.getCell().getText());
    }

    @Test
    void testReadRowCriteriaNotFound() {
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(rowElement.getAttribute("innerText")).thenReturn("no-match");
        assertThrows(NotFoundException.class, () ->
                tableImpl.readRow(List.of("need-match"), MockRow.class)
        );
    }

    @Test
    void testInsertCellValueNoRegistry() {
        TestTableImpl localTable = new TestTableImpl(driver, null, locators, container, rows);
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(rowElement.findSmartElements(any(By.class))).thenReturn(List.of(mock(SmartWebElement.class)));
        MockTableField field = new MockTableField("field");
        assertThrows(IllegalStateException.class, () ->
                localTable.insertCellValue(1, MockRow.class, field, 1, "someVal")
        );
    }

    @Test
    void testInsertCellValueInvalidIndex() {
        when(container.findSmartElements(any(By.class))).thenReturn(Collections.emptyList());
        MockTableField field = new MockTableField("fieldX");
        Exception ex = assertThrows(RuntimeException.class, () ->
                tableImpl.insertCellValue(1, MockRow.class, field, 1, "xyz")
        );
        assertTrue(ex.getMessage().contains("Invalid cell index"));
    }

    @Test
    void testInsertCellValueUsingComponent() {
        registry.registerService(ComponentType.class,
                (cellElement, compType, values) -> cellElement.sendKeys(values));
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(rowElement.findSmartElements(any(By.class))).thenReturn(List.of(cellElement));
        MockTableField field = new MockTableField("fieldY");
        tableImpl.insertCellValue(1, MockRow.class, field, 1, "val1");
    }

    @Test
    void testInsertCellValueUsingCustomFunction() {
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(rowElement.findSmartElements(any(By.class))).thenReturn(List.of(cellElement));
        assertThrows(RuntimeException.class, () ->
                tableImpl.insertCellValue(1, RowWithCustomInsert.class, new MockTableFieldCustomInsert("test"), 1, "X")
        );
    }

    @Test
    void testFilterTableNoRegistry() {
        TestTableImpl localImpl = new TestTableImpl(driver, null, locators, container, rows);
        FilterField field = new FilterField();
        assertThrows(IllegalStateException.class, () ->
                localImpl.filterTable(FilterRow.class, field, FilterStrategy.SELECT, "some")
        );
    }

    @Test
    void testFilterTableUsingComponent() {
        registry.registerService(ComponentType.class,
                (cellElement, compType, filterStrategy, values) -> cellElement.sendKeys("filtered"));
        when(container.findSmartElement(locators.getHeaderRowLocator())).thenReturn(container);
        FilterField field = new FilterField();
        Exception ex = assertThrows(RuntimeException.class, () ->
                tableImpl.filterTable(FilterRow.class, field, FilterStrategy.SELECT, "someVal")
        );
        assertTrue(ex.getMessage().contains("Failed to filter using component"));
    }

    @Test
    void testFilterTableUsingCustomFunction() {
        when(container.findSmartElement(locators.getHeaderRowLocator())).thenReturn(container);
        FilterFieldCustom field = new FilterFieldCustom();
        assertThrows(RuntimeException.class, () ->
                tableImpl.filterTable(FilterRowCustom.class, field, FilterStrategy.SELECT, "valX")
        );
    }

    @Test
    void testSortTable() {
        assertDoesNotThrow(() ->
                tableImpl.sortTable(MockRow.class, new MockTableField("sortTest"), SortingStrategy.ASC)
        );
    }

    @Test
    void testPrivateConvertFieldValueToStrings() throws Exception {
        InsertRow sample = new InsertRow();
        sample.setNameCell(new TableCell(null, "convertedName"));
        Field f = InsertRow.class.getDeclaredField("nameCell");
        Method convertM = TableImpl.class.getDeclaredMethod("convertFieldValueToStrings", Field.class, Object.class);
        convertM.setAccessible(true);
        String[] result = (String[]) convertM.invoke(tableImpl, f, sample);
        assertArrayEquals(new String[]{"convertedName"}, result);
    }

    @Test
    void testPrivateMergeObjects() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("mergeObjects", Object.class, Object.class);
        m.setAccessible(true);
        MergedRow row1 = new MergedRow();
        row1.setValA("A");
        MergedRow row2 = new MergedRow();
        row2.setValB("B");
        MergedRow merged = (MergedRow) m.invoke(tableImpl, row1, row2);
        assertEquals("A", merged.getValA());
        assertEquals("B", merged.getValB());
    }

    @Test
    void testConstructorAcceptedValues() {
        TableImpl impl = new TestTableImpl(driver, registry, locators, container, rows);
        try {
            Field f = TableImpl.class.getDeclaredField("acceptedValues");
            f.setAccessible(true);
            List<?> list = (List<?>) f.get(impl);
            assertEquals(2, list.size());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testReadRowsInRangeValid() throws Exception {
        SmartWebElement dummyRow1 = mock(SmartWebElement.class);
        SmartWebElement dummyRow2 = mock(SmartWebElement.class);
        List<SmartWebElement> allRows = List.of(dummyRow1, dummyRow2);
        when(container.findSmartElements(any(By.class))).thenReturn(allRows);
        TestTableImpl localImpl = new TestTableImpl(driver, registry, locators, container, allRows);
        Method m = TableImpl.class.getDeclaredMethod("readRowsInRange", SmartWebElement.class, By.class, String.class, Integer.class, Integer.class);
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<SmartWebElement> result = (List<SmartWebElement>) m.invoke(localImpl, container, By.id("dummy"), "anySection", 1, 2);
        assertEquals(allRows, result);
    }

    @Test
    void testReadRowsInRangeInvalidRange() throws Exception {
        List<SmartWebElement> allRows = List.of(mock(SmartWebElement.class));
        when(container.findSmartElements(any(By.class))).thenReturn(allRows);
        Method m = TableImpl.class.getDeclaredMethod("readRowsInRange", SmartWebElement.class, By.class, String.class, Integer.class, Integer.class);
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<SmartWebElement> result = (List<SmartWebElement>) m.invoke(tableImpl, container, By.id("dummy"), "sec", 5, 3);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMergeObjects() throws Exception {
        DummyRowNoLocators r1 = new DummyRowNoLocators();
        r1.setValue("A");
        DummyRowNoLocators r2 = new DummyRowNoLocators();
        r2.setValue("B");
        Method m = TableImpl.class.getDeclaredMethod("mergeObjects", Object.class, Object.class);
        m.setAccessible(true);
        DummyRowNoLocators merged = (DummyRowNoLocators) m.invoke(tableImpl, r1, r2);
        assertEquals("A", merged.getValue());
        merged = (DummyRowNoLocators) m.invoke(tableImpl, null, r2);
        assertEquals("B", merged.getValue());
    }

    @Test
    void testInvokeSetter() throws Exception {
        @Setter
        @Getter
        class Dummy {
            private String field;
        }
        Dummy d = new Dummy();
        Method m = TableImpl.class.getDeclaredMethod("invokeSetter", Object.class, String.class, Object.class);
        m.setAccessible(true);
        m.invoke(tableImpl, d, "field", "testValue");
        assertEquals("testValue", d.getField());
    }

    @Test
    void testCreateInstanceSuccess() throws Exception {
        Method createInstanceMethod = TableImpl.class.getDeclaredMethod("createInstance", Class.class);
        createInstanceMethod.setAccessible(true);
        DummyClass instance = (DummyClass) createInstanceMethod.invoke(tableImpl, DummyClass.class);
        assertNotNull(instance);
    }

    @Test
    void testCreateInstanceFailureIllegalStateException() throws NoSuchMethodException {
        Method createInstanceMethod = TableImpl.class.getDeclaredMethod("createInstance", Class.class);
        createInstanceMethod.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            createInstanceMethod.invoke(tableImpl, NoNoArgConstructor.class);
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalStateException);
        assertTrue(cause.getMessage().contains("Could not create a new instance of class"));
    }

    @Test
    void testIsListOfTableCellTrue() throws Exception {
        class Dummy {
            public List<TableCell> list;
        }
        Field f = Dummy.class.getDeclaredField("list");
        Method m = TableImpl.class.getDeclaredMethod("isListOfTableCell", Field.class);
        m.setAccessible(true);
        Boolean result = (Boolean) m.invoke(tableImpl, f);
        assertTrue(result);
    }

    @Test
    void testIsListOfTableCellFalse() throws Exception {
        class Dummy {
            public String notAList;
        }
        Field f = Dummy.class.getDeclaredField("notAList");
        Method m = TableImpl.class.getDeclaredMethod("isListOfTableCell", Field.class);
        m.setAccessible(true);
        Boolean result = (Boolean) m.invoke(tableImpl, f);
        assertFalse(result);
    }

    @Test
    void testMapToCellLocator() throws Exception {
        class Dummy {
            @TableCellLocator(cellLocator = @FindBy(id = "cell"), tableSection = "sec")
            public String dummy;
        }
        Field f = Dummy.class.getDeclaredField("dummy");
        Method m = TableImpl.class.getDeclaredMethod("mapToCellLocator", Field.class);
        m.setAccessible(true);
        Object obj = m.invoke(tableImpl, f);
        assertNotNull(obj);
    }

    @Test
    void testPrepareFieldInvokersMapSuccess() throws Exception {
        @Setter
        @Getter
        class DummyRow {
            @TableCellLocator(cellLocator = @FindBy(id = "c"), tableSection = "s")
            @CellInsertion(type = MockComponentType.class, componentType = "DUMMY", order = 1)
            private TableCell cell = new TableCell(null, "init");
        }
        DummyRow row = new DummyRow();
        Method m = TableImpl.class.getDeclaredMethod("prepareFieldInvokersMap", Class.class, Object.class);
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<TableField<DummyRow>, String[]> map = (Map<TableField<DummyRow>, String[]>) m.invoke(tableImpl, DummyRow.class, row);
        assertFalse(map.isEmpty());
        for (String[] arr : map.values()) {
            assertArrayEquals(new String[]{"init"}, arr);
        }
    }

    @Test
    void testConvertFieldValueToStrings_TableCell() throws Exception {
        TableCell cell = new TableCell(null, "textValue");
        class Dummy {
            @TableCellLocator(cellLocator = @FindBy(id = "dummy"), tableSection = "s")
            @CellInsertion(type = MockComponentType.class, componentType = "DUMMY", order = 1)
            private final TableCell field = cell;
        }
        Dummy d = new Dummy();
        Field f = Dummy.class.getDeclaredField("field");
        Method m = TableImpl.class.getDeclaredMethod("convertFieldValueToStrings", Field.class, Object.class);
        m.setAccessible(true);
        String[] result = (String[]) m.invoke(tableImpl, f, d);
        assertArrayEquals(new String[]{"textValue"}, result);
    }

    @Test
    void testProcessInsertCellValue() throws Exception {
        DummyRow row = new DummyRow();
        Method processMethod = TableImpl.class.getDeclaredMethod(
                "processInsertCellValue",
                java.util.function.BiConsumer.class,
                Class.class,
                Object.class
        );
        processMethod.setAccessible(true);
        java.util.function.BiConsumer<TableField<DummyRow>, String[]> action = (field, arr) -> {
            try {
                field.invoke(row, new TableCell(null, arr[0]));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
        processMethod.invoke(tableImpl, action, DummyRow.class, row);
        assertEquals("", row.getDummyField().getText());
    }

    @Test
    void testFilterCellsUsingCustomFunctionSuccess() throws Exception {
        SmartWebElement dummyElement = mock(SmartWebElement.class);
        Method m = TableImpl.class.getDeclaredMethod("filterCellsUsingCustomFunction", Class.class, SmartWebElement.class, FilterStrategy.class, String[].class);
        m.setAccessible(true);
        assertDoesNotThrow(() -> m.invoke(tableImpl, DummyCustomFilter.class, dummyElement, FilterStrategy.SELECT, new String[]{"v"}));
    }

    @Test
    void testInsertUsingCustomFunctionFailure() throws Exception {
        SmartWebElement dummyElement = mock(SmartWebElement.class);
        Method m = TableImpl.class.getDeclaredMethod("insertUsingCustomFunction", Class.class, SmartWebElement.class, String[].class);
        m.setAccessible(true);
        Exception ex = assertThrows(Exception.class, () -> m.invoke(tableImpl, DummyFailInsertion.class, dummyElement, new String[]{"x"}));
        assertNotNull(ex);
    }

    @Test
    void testProtectedConstructor() throws Exception {
        TableImpl impl = new TableImpl(driver) {
            @Override
            protected SmartWebElement getTableContainer(By tableContainerLocator) {
                return container;
            }

            @Override
            protected List<SmartWebElement> getRows(SmartWebElement tableContainer, By tableRowsLocator, String section) {
                return rows;
            }

            @Override
            protected SmartWebElement getHeaderRow(SmartWebElement tableContainer, By headerRowLocator, String tableSection) {
                return container;
            }

            @Override
            protected void sortTable(SmartWebElement headerCell, SortingStrategy sortingStrategy) {
            }
        };
        Field f = TableImpl.class.getDeclaredField("acceptedValues");
        f.setAccessible(true);
        List<?> list = (List<?>) f.get(impl);
        assertEquals(2, list.size());
    }

    @Test
    void testReadTableNoFieldsOverload() {
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(cellElement.getText()).thenReturn("value");
        List<DummyRow> list = tableImpl.readTable(DummyRow.class);
        assertEquals(1, list.size());
        assertEquals("value", list.get(0).getDummyField().getText());
    }

    @Test
    void testReadTableWithFieldsOverload() {
        TableField<DummyRow> field = (instance, o) -> instance.setDummyField((TableCell) o);
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(cellElement.getText()).thenReturn("fieldValue");
        List<DummyRow> list = tableImpl.readTable(DummyRow.class, field);
        assertEquals(1, list.size());
        assertEquals("fieldValue", list.get(0).getDummyField().getText());
    }

    @Test
    void testReadTableWithFields() {
        DummyRow expected = new DummyRow();
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        when(cellElement.getText()).thenReturn("value");
        List<DummyRow> list = tableImpl.readTable(DummyRow.class, (instance, o) -> instance.setDummyField((TableCell) o));
        assertEquals(1, list.size());
        assertEquals("value", list.get(0).getDummyField().getText());
    }

    @Test
    void testReadTableRangeWithFields() {
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        when(cellElement.getText()).thenReturn("value");
        List<DummyRow> list = tableImpl.readTable(1, 1, DummyRow.class, (instance, o) -> instance.setDummyField((TableCell) o));
        assertEquals(1, list.size());
        assertEquals("value", list.get(0).getDummyField().getText());
    }

    @Test
    void testReadRowWithFieldsByIndex() {
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        when(cellElement.getText()).thenReturn("value");
        DummyRow model = tableImpl.readRow(1, DummyRow.class, (instance, o) -> instance.setDummyField((TableCell) o));
        assertNotNull(model);
        assertEquals("value", model.getDummyField().getText());
    }

    @Test
    void testReadRowWithFieldsByCriteria() {
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        when(rowElement.getAttribute("innerText")).thenReturn("criteria");
        when(cellElement.getText()).thenReturn("value");
        DummyRow model = tableImpl.readRow(List.of("criteria"), DummyRow.class, (instance, o) -> instance.setDummyField((TableCell) o));
        assertNotNull(model);
        assertEquals("value", model.getDummyField().getText());
    }

    @Test
    void testInsertCellValueListSearchCriteriaWithField() {
        registry.registerService(ComponentType.class, (TableInsertion) (cell, compType, values) -> {
            cell.sendKeys(values);
        });
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        when(rowElement.findSmartElements(any(By.class))).thenReturn(List.of(cellElement));
        when(rowElement.getAttribute("innerText")).thenReturn("dummy row");
        tableImpl.insertCellValue(
                List.of("dummy"),
                DummyRow.class,
                (TableField<DummyRow>) (instance, o) -> instance.setDummyField((TableCell) o),
                1,
                "value"
        );
        DummyRow model = tableImpl.readRow(1, DummyRow.class, (instance, o) -> instance.setDummyField((TableCell) o));
        assertEquals("value", model.getDummyField().getText());
    }

    @Test
    void testInsertCellValueListSearchCriteriaWithData() {
        registry.registerService(ComponentType.class, (TableInsertion) (cell, compType, values) -> {
            cell.sendKeys(values);
        });
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        when(rowElement.findSmartElements(any(By.class))).thenReturn(List.of(cellElement));
        when(rowElement.getAttribute("innerText")).thenReturn("dummy row");
        tableImpl.insertCellValue(List.of("dummy"), DummyRow.class, new DummyRow());
        DummyRow model = tableImpl.readRow(1, DummyRow.class, (instance, o) -> instance.setDummyField((TableCell) o));
        assertEquals("value", model.getDummyField().getText());
    }

    @Test
    void testInsertCellValueByIndexWithField() {
        registry.registerService(ComponentType.class, (TableInsertion) (cell, compType, values) -> {
            cell.sendKeys(values);
        });
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        when(rowElement.findSmartElements(any(By.class))).thenReturn(List.of(cellElement));
        tableImpl.insertCellValue(1, DummyRow.class, (TableField<DummyRow>) (instance, o) -> instance.setDummyField((TableCell) o), "direct");
        DummyRow model = tableImpl.readRow(1, DummyRow.class, (instance, o) -> instance.setDummyField((TableCell) o));
        assertEquals("value", model.getDummyField().getText());
    }

    @Test
    void testInsertCellValueByIndexWithData() {
        registry.registerService(ComponentType.class, (TableInsertion) (cell, compType, values) -> {
            cell.sendKeys(values);
        });
        when(container.findSmartElements(any(By.class))).thenReturn(List.of(rowElement));
        when(rowElement.findSmartElements(any(By.class))).thenReturn(List.of(cellElement));
        tableImpl.insertCellValue(1, DummyRow.class, new DummyRow());
        DummyRow model = tableImpl.readRow(1, DummyRow.class, (instance, o) -> instance.setDummyField((TableCell) o));
        assertEquals("value", model.getDummyField().getText());
    }

    @Test
    void testReadTableRangeOverload() {
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(cellElement.getText()).thenReturn("rangeValue");
        List<DummyRow> list = tableImpl.readTable(1, 1, DummyRow.class);
        assertEquals(1, list.size());
        assertEquals("rangeValue", list.get(0).getDummyField().getText());
    }

    @Test
    void testReadTableRangeWithFieldsOverload() {
        TableField<DummyRow> field = (instance, o) -> instance.setDummyField((TableCell) o);
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(cellElement.getText()).thenReturn("rangeFieldValue");
        List<DummyRow> list = tableImpl.readTable(1, 1, DummyRow.class, field);
        assertEquals(1, list.size());
        assertEquals("rangeFieldValue", list.get(0).getDummyField().getText());
    }

    @Test
    void testReadRowWithFieldsOverload() {
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(rows.get(0).getAttribute("innerText")).thenReturn("match");
        when(cellElement.getText()).thenReturn("rowField");
        DummyRow row = tableImpl.readRow(List.of("match"), DummyRow.class, (instance, o) -> instance.setDummyField((TableCell) o));
        assertNotNull(row);
        assertEquals("rowField", row.getDummyField().getText());
    }

    @Test
    void testInsertCellValueListSearchCriteria() {
        registry.registerService(ComponentType.class, (TableInsertion) (cell, compType, values) -> {
            cell.sendKeys(values);
        });
        SmartWebElement dummyElement = mock(SmartWebElement.class);
        when(rows.get(0).findSmartElements(any(By.class))).thenReturn(List.of(dummyElement));
        when(rows.get(0).getAttribute("innerText")).thenReturn("criteria row");
        assertDoesNotThrow(() -> tableImpl.insertCellValue(
                List.of("criteria"),
                DummyRow.class,
                (TableField<DummyRow>) (instance, o) -> instance.setDummyField((TableCell) o),
                "inserted"
        ));
    }

    @Test
    void testInsertCellValueIntSearchCriteria() {
        registry.registerService(ComponentType.class, (TableInsertion) (cell, compType, values) -> {
            cell.sendKeys(values);
        });
        SmartWebElement dummyElement = mock(SmartWebElement.class);
        when(rows.get(0).findSmartElements(By.xpath("//dummyCell"))).thenReturn(List.of(cellElement));
        assertDoesNotThrow(() -> tableImpl.insertCellValue(1, DummyRow.class,
                (instance, o) -> instance.setDummyField((TableCell) o), "direct"));
    }

    @Test
    void testInsertCellValueDataOverload() {
        registry.registerService(ComponentType.class, (TableInsertion) (cell, compType, values) -> {
            cell.sendKeys(values);
        });
        when(container.findSmartElements(By.className("dummyRow"))).thenReturn(rows);
        when(rows.get(0).findSmartElements(By.xpath("//dummyCell"))).thenReturn(List.of(cellElement));
        assertDoesNotThrow(() -> tableImpl.insertCellValue(1, DummyRow.class, new DummyRow()));
    }

    @Test
    void testGetTableContainerReflectively() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("getTableContainer", By.class);
        m.setAccessible(true);
        SmartWebElement element = mock(SmartWebElement.class);
        when(container.findSmartElement(By.id("test"))).thenReturn(element);
        SmartWebElement result = (SmartWebElement) m.invoke(tableImpl, By.id("test"));
        assertEquals(element.getAttribute("id"), result.getAttribute("id"));
    }

    @Test
    void testGetRowsReflectively() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("getRows", SmartWebElement.class, By.class, String.class);
        m.setAccessible(true);
        List<SmartWebElement> elems = List.of(mock(SmartWebElement.class), mock(SmartWebElement.class));
        when(container.findSmartElements(By.className("row"))).thenReturn(elems);
        @SuppressWarnings("unchecked")
        List<SmartWebElement> result = (List<SmartWebElement>) m.invoke(tableImpl, container, By.className("row"), "any");
        assertEquals(elems.get(0).getAttribute("class"), result.get(0).getAttribute("class"));
    }

    @Test
    void testGetHeaderRowReflectively() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("getHeaderRow", SmartWebElement.class, By.class, String.class);
        m.setAccessible(true);
        SmartWebElement header = mock(SmartWebElement.class);
        when(container.findSmartElement(By.className("header"))).thenReturn(header);
        SmartWebElement result = (SmartWebElement) m.invoke(tableImpl, container, By.className("header"), "any");
        assertEquals(header.getAttribute("class"), result.getAttribute("class"));
    }

    @Test
    void testSortTableReflectively() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("sortTable", SmartWebElement.class, SortingStrategy.class);
        m.setAccessible(true);
        SmartWebElement headerCell = mock(SmartWebElement.class);
        assertDoesNotThrow(() -> m.invoke(tableImpl, headerCell, SortingStrategy.ASC));
    }

    @Test
    void testReadRowsInRangeInvalid() throws Exception {
        when(container.findSmartElements(locators.getTableRowsLocator())).thenReturn(Arrays.asList(rowElement, rowElement));
        Method m = TableImpl.class.getDeclaredMethod("readRowsInRange", SmartWebElement.class, By.class, String.class, Integer.class, Integer.class);
        m.setAccessible(true);
        List<?> result = (List<?>) m.invoke(tableImpl, container, locators.getTableRowsLocator(), "dummySection", 3, 2);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMergeObjectsNullCases() throws Exception {
        Method mergeM = TableImpl.class.getDeclaredMethod("mergeObjects", Object.class, Object.class);
        mergeM.setAccessible(true);
        Object res1 = mergeM.invoke(tableImpl, null, "value");
        assertEquals("value", res1);
        Object res2 = mergeM.invoke(tableImpl, "value", null);
        assertEquals("value", res2);
    }

    @Test
    void testInvokeSetterNoSetterFound() throws Exception {
        DummyNoSetter dummy = new DummyNoSetter();
        Method setterM = TableImpl.class.getDeclaredMethod("invokeSetter", Object.class, String.class, Object.class);
        setterM.setAccessible(true);
        setterM.invoke(tableImpl, dummy, "nonExisting", "test");
        assertNull(dummy.payload);
    }

    @Test
    void testIsListOfTableCell() throws Exception {
        Field f1 = DummyList.class.getDeclaredField("cells");
        Field f2 = DummyList.class.getDeclaredField("notCells");
        Method m = TableImpl.class.getDeclaredMethod("isListOfTableCell", Field.class);
        m.setAccessible(true);
        boolean res1 = (boolean) m.invoke(tableImpl, f1);
        boolean res2 = (boolean) m.invoke(tableImpl, f2);
        assertTrue(res1);
        assertFalse(res2);
    }

    @Test
    void testExtractAnnotatedFieldsNoFields() throws Exception {
        List<?> list = callExtractAnnotatedFields(NoAnnotatedRow.class, Collections.emptyList());
        assertTrue(list.isEmpty());
    }

    @Test
    void testPrepareFieldInvokersMapWithValidField() throws Exception {
        InsertRow row = new InsertRow();
        row.setNameCell(new TableCell(null, "initName"));
        Method m = TableImpl.class.getDeclaredMethod("prepareFieldInvokersMap", Class.class, Object.class);
        m.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<?, ?> map = (Map<?, ?>) m.invoke(tableImpl, InsertRow.class, row);
        assertEquals(1, map.size());
    }

    @Test
    void testReadTableWithNullFields() {
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(cellElement.getText()).thenReturn("value");
        List<DummyRow> list = tableImpl.readTable(DummyRow.class, (TableField<DummyRow>[]) null);
        assertEquals(1, list.size());
        assertEquals("value", list.get(0).getDummyField().getText());
    }

    @Test
    void testReadRowWithNullFields() {
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(cellElement.getText()).thenReturn("value");
        DummyRow row = tableImpl.readRow(1, DummyRow.class, (TableField<DummyRow>[]) null);
        assertNotNull(row);
        assertEquals("value", row.getDummyField().getText());
    }

    @Test
    void testInsertCellValueWithNullValues() {
        registry.registerService(ComponentType.class, (TableInsertion) (cell, compType, values) -> {
            cell.sendKeys(values);
        });
        when(container.findSmartElements(any(By.class))).thenReturn(rows);
        when(rowElement.findSmartElements(any(By.class))).thenReturn(List.of(cellElement));
        assertThrows(NullPointerException.class, () ->
                tableImpl.insertCellValue(1, null, (TableField<DummyRow>) (instance, o) -> instance.setDummyField((TableCell) o), 1, (String[]) null)
        );
    }

    @Test
    void testFilterTableWithNullValues() {
        when(container.findSmartElement(locators.getHeaderRowLocator())).thenReturn(container);
        FilterField field = new FilterField();
        assertThrows(RuntimeException.class, () ->
                tableImpl.filterTable(FilterRow.class, field, FilterStrategy.SELECT, (String[]) null)
        );
    }

    @Test
    void testPrepareFieldInvokersMapWithNullData() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("prepareFieldInvokersMap", Class.class, Object.class);
        m.setAccessible(true);
        assertThrows(InvocationTargetException.class, () -> m.invoke(tableImpl, DummyRow.class, null));
    }

    @Test
    void testConvertFieldValueToStringsWithNullData() throws Exception {
        Field f = DummyRowWithList.class.getDeclaredField("cells");
        f.setAccessible(true);
        Method m = TableImpl.class.getDeclaredMethod("convertFieldValueToStrings", Field.class, Object.class);
        m.setAccessible(true);
        assertThrows(InvocationTargetException.class, () -> m.invoke(tableImpl, f, null));
    }

    @Test
    void testMergeObjectsWithNullObjects() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("mergeObjects", Object.class, Object.class);
        m.setAccessible(true);
        Object result = m.invoke(tableImpl, null, null);
        assertNull(result);
    }

    @Test
    void testInvokeSetterWithNullTarget() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("invokeSetter", Object.class, String.class, Object.class);
        m.setAccessible(true);
        m.invoke(tableImpl, null, "field", "testValue");
        // No exception expected, just a no-op
    }

    @Test
    void testInvokeSetterWithNullFieldName() throws Exception {
        DummySetter dummy = new DummySetter();
        Method m = TableImpl.class.getDeclaredMethod("invokeSetter", Object.class, String.class, Object.class);
        m.setAccessible(true);
        m.invoke(tableImpl, dummy, null, "testValue");
        assertEquals(null, dummy.getData());
    }

    @Test
    void testInvokeSetterWithNullValue() throws Exception {
        DummySetter dummy = new DummySetter();
        Method m = TableImpl.class.getDeclaredMethod("invokeSetter", Object.class, String.class, Object.class);
        m.setAccessible(true);
        m.invoke(tableImpl, dummy, "data", null);
        assertEquals(null, dummy.getData());
    }

    @Test
    void testCreateInstanceWithNullClass() throws Exception {
        Method createInstanceMethod = TableImpl.class.getDeclaredMethod("createInstance", Class.class);
        createInstanceMethod.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            createInstanceMethod.invoke(tableImpl, (Class<?>) null);
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof NullPointerException);
    }

    @Test
    void testIsListOfTableCellWithNullField() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("isListOfTableCell", Field.class);
        m.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            m.invoke(tableImpl, (Field) null);
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof NullPointerException);
    }

    @Test
    void testMapToCellLocatorWithNullField() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("mapToCellLocator", Field.class);
        m.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            m.invoke(tableImpl, (Field) null);
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof NullPointerException);
    }

    @Test
    void testPrepareFieldInvokersMapWithNullClass() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("prepareFieldInvokersMap", Class.class, Object.class);
        m.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            m.invoke(tableImpl, (Class<?>) null, new DummyRow());
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof NullPointerException);
    }

    @Test
    void testConvertFieldValueToStringsWithNullField() throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("convertFieldValueToStrings", Field.class, Object.class);
        m.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            m.invoke(tableImpl, (Field) null, new DummyRowWithList());
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof NullPointerException);
    }

    @Test
    void testProcessInsertCellValueWithNullAction() throws Exception {
        Method processMethod = TableImpl.class.getDeclaredMethod(
                "processInsertCellValue",
                java.util.function.BiConsumer.class,
                Class.class,
                Object.class
        );
        processMethod.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            processMethod.invoke(tableImpl, (java.util.function.BiConsumer<?, ?>) null, DummyRow.class, new DummyRow());
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof NullPointerException);
    }

    @Test
    void testProcessInsertCellValueWithNullClass() throws Exception {
        Method processMethod = TableImpl.class.getDeclaredMethod(
                "processInsertCellValue",
                java.util.function.BiConsumer.class,
                Class.class,
                Object.class
        );
        processMethod.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            processMethod.invoke(tableImpl, (java.util.function.BiConsumer<?, ?>) (field, arr) -> {}, null, new DummyRow());
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof NullPointerException);
    }

    @Test
    void testProcessInsertCellValueWithNullData() throws Exception {
        Method processMethod = TableImpl.class.getDeclaredMethod(
                "processInsertCellValue",
                java.util.function.BiConsumer.class,
                Class.class,
                Object.class
        );
        processMethod.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            processMethod.invoke(tableImpl, (java.util.function.BiConsumer<?, ?>) (field, arr) -> {}, DummyRow.class, null);
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof NullPointerException);
    }

    @SuppressWarnings("unchecked")
    private <T> List<?> callExtractAnnotatedFields(Class<T> clazz, List<TableField<T>> fields) throws Exception {
        Method m = TableImpl.class.getDeclaredMethod("extractAnnotatedFields", Class.class, List.class);
        m.setAccessible(true);
        return (List<?>) m.invoke(tableImpl, clazz, fields);
    }

    public static class DummyNoSetter {
        public String payload;
    }

    public static class DummyList {
        public List<TableCell> cells;
        public String notCells;
    }

    @TableInfo(
            tableContainerLocator = @FindBy(id = "dummyContainer"),
            rowsLocator = @FindBy(id = "dummyRows"),
            headerRowLocator = @FindBy(id = "dummyHeader")
    )
    public static class NoAnnotatedRow {
        public String dummy;
    }

    @TableInfo(
            tableContainerLocator = @FindBy(id = "dummyContainer"),
            rowsLocator = @FindBy(id = "dummyRows"),
            headerRowLocator = @FindBy(id = "dummyHeader")
    )
    public static class DummyRowWithList {
        @TableCellLocator(cellLocator = @FindBy(id = "cellList"), tableSection = "listSec")
        @CellInsertion(type = com.theairebellion.zeus.ui.components.table.service.mock.MockComponentType.class, componentType = "DUMMY", order = 1)
        private List<TableCell> cells = List.of(new TableCell("listValue"));

        public List<TableCell> getCells() {
            return cells;
        }

        public void setCells(List<TableCell> cells) {
            this.cells = cells;
        }
    }

    @TableInfo(
            tableContainerLocator = @FindBy(id = "dummy"),
            rowsLocator = @FindBy(id = "dummyRows"),
            headerRowLocator = @FindBy(id = "dummyHeader")
    )
    public static class DummyRowUnsupported {
        @TableCellLocator(cellLocator = @FindBy(id = "unsupported"), tableSection = "unsup")
        private String value = "unsupported";

        public String getValue() {
            return value;
        }

        public void setValue(String v) {
            value = v;
        }
    }

    public static class DummySetter {
        private String data;

        public void setData(String d) {
            this.data = d;
        }

        public String getData() {
            return data;
        }
    }

    public static class NoNoArgConstructor {
        public NoNoArgConstructor(String s) {
        }
    }

    public static class NoTableInfoRow {
    }

    public static class DummyFailRow {

        @TableCellLocator(cellLocator = @FindBy(id = "fail"), tableSection = "failSec")
        @CellInsertion(type = MockComponentType.class, componentType = "DUMMY", order = 1)
        private final TableCell cell = new TableCell(null, "fail");

    }

    @TableInfo(
            tableContainerLocator = @FindBy(xpath = "//table"),
            rowsLocator = @FindBy(xpath = ".//tr"),
            headerRowLocator = @FindBy(xpath = ".//thead/tr")
    )
    public static class FilterRow {

        @CellFilter(type = ComponentType.class, componentType = "DUMMY")
        @TableCellLocator(cellLocator = @FindBy(className = "tdFilter"), tableSection = "filterSec")
        private TableCell cell = new TableCell(null, "init");

        public TableCell getCell() {
            return cell;
        }

        public void setCell(TableCell c) {
            cell = c;
        }

        public FilterRow() {
            if (cell == null) {
                cell = new TableCell(null, "placeholder");
            }
        }
    }

    @TableInfo(
            tableContainerLocator = @FindBy(xpath = "//table"),
            rowsLocator = @FindBy(xpath = ".//tr"),
            headerRowLocator = @FindBy(xpath = ".//thead/tr")
    )
    public static class RowWithCustomInsert {
        @TableCellLocator(cellLocator = @FindBy(className = "tdCustom"), tableSection = "cInsert")
        @CustomCellInsertion(insertionFunction = FailInsertion.class, order = 1)
        private TableCell customCell;

        public TableCell getCustomCell() {
            return customCell;
        }

        public void setCustomCell(TableCell c) {
            customCell = c;
        }
    }

    public static class FilterField implements TableField<FilterRow> {
        @CellInsertion(type = ComponentType.class, componentType = "DUMMY", order = 1)
        @TableCellLocator(cellLocator = @FindBy(className = "tdFilter"), tableSection = "filterSec")
        private TableCell cell;

        @Override
        public void invoke(FilterRow instance, Object o) {
        }
    }

    public static class FilterFieldCustom implements TableField<FilterRowCustom> {
        @CustomCellFilter(cellFilterFunction = CustomFilterFail.class)
        @TableCellLocator(cellLocator = @FindBy(className = "tdFilterCustom"), tableSection = "filterCustom")
        private TableCell special;

        @Override
        public void invoke(FilterRowCustom instance, Object o) {
        }
    }

    public static class CustomFilterFail implements CellFilterFunction {
        @Override
        public void cellFilterFunction(SmartWebElement cellElement, FilterStrategy filterStrategy, String... values) {
            throw new RuntimeException("Custom cell filter function error");
        }
    }

    public static class MockTableFieldCustomInsert implements TableField<RowWithCustomInsert> {
        private final String name;

        public MockTableFieldCustomInsert(String n) {
            name = n;
        }

        @Override
        public void invoke(RowWithCustomInsert instance, Object o) {
            instance.setCustomCell((TableCell) o);
        }
    }

    public static class FailInsertion implements CellInsertionFunction {
        @Override
        public void cellInsertionFunction(SmartWebElement cellElement, String... values) {
            throw new RuntimeException("Custom insertion function fail");
        }
    }

    public static class InsertRow {
        @TableCellLocator(cellLocator = @FindBy(className = "nameCell"), tableSection = "rowSection")
        @CellInsertion(type = MockComponentType.class, componentType = "DUMMY", order = 1)
        private TableCell nameCell;

        public TableCell getNameCell() {
            return nameCell;
        }

        public void setNameCell(TableCell c) {
            nameCell = c;
        }
    }

    @Setter
    @Getter
    public static class MergedRow {
        private String valA;
        private String valB;
    }

    public static class FilterRowCustom {
    }

    @Setter
    @Getter
    @TableInfo(
            tableContainerLocator = @FindBy(xpath = "//dummy"),
            rowsLocator = @FindBy(xpath = "//dummy"),
            headerRowLocator = @FindBy(xpath = "//dummy")
    )
    public static class DummyRow {

        @CellInsertion(type = ComponentType.class, componentType = "DUMMY", order = 1)
        @TableCellLocator(cellLocator = @FindBy(xpath = "//dummyCell"), tableSection = "dummySection")
        private TableCell dummyField = new TableCell(null, "");
    }

    @Setter
    @Getter
    public static class DummyRowNoLocators {
        private String value;
    }

    public static class DummyClass {
        public DummyClass() {
        }
    }

    public static class DummyField implements TableField<DummyRowNoLocators> {
        @Override
        public void invoke(DummyRowNoLocators instance, Object o) throws IllegalAccessException, InvocationTargetException {
            try {
                DummyRowNoLocators.class.getDeclaredMethod("setValue", String.class).invoke(instance, o);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class DummyFailInsertion implements CellInsertionFunction {
        @Override
        public void cellInsertionFunction(SmartWebElement cellElement, String... values) {
            throw new RuntimeException("Dummy insertion failure");
        }
    }

    public static class DummyCustomFilter implements CellFilterFunction {
        @Override
        public void cellFilterFunction(SmartWebElement cellElement, FilterStrategy filterStrategy, String... values) {
            // do nothing
        }
    }
}