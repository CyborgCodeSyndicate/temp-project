package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.base.TableComponentType;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
class TableServiceImplTest extends BaseUnitUITest {


    private TestTableServiceImpl testService;
    @Mock
    private SmartWebDriver smartWebDriver;
    @Mock
    private TableServiceRegistry tableServiceRegistry;
    @Mock
    private Table tableMock;
    private final DummyTableComponentType dummyComponentType = DummyTableComponentType.DUMMY;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testService = new TestTableServiceImpl(smartWebDriver, tableServiceRegistry, tableMock);
    }

    @Test
    void testReadTableNoFields() {
        List<String> result = List.of("row1", "row2");
        when(tableMock.readTable(String.class)).thenReturn(result);
        List<String> tableResult = testService.readTable(dummyComponentType, String.class);
        assertEquals(result, tableResult);
        verify(tableMock).readTable(String.class);
    }

    @Test
    void testReadTableWithFields() {
        List<String> result = List.of("row1", "row2");
        TableField<String> field = (instance, obj) -> {
        };
        when(tableMock.readTable(eq(String.class), any())).thenReturn(result);
        List<String> tableResult = testService.readTable(dummyComponentType, String.class, field);
        assertEquals(result, tableResult);
        verify(tableMock).readTable(eq(String.class), any());
    }

    @Test
    void testReadTableRangeNoFields() {
        List<String> result = List.of("row1", "row2");
        when(tableMock.readTable(1, 2, String.class)).thenReturn(result);
        List<String> tableResult = testService.readTable(dummyComponentType, 1, 2, String.class);
        assertEquals(result, tableResult);
        verify(tableMock).readTable(1, 2, String.class);
    }

    @Test
    void testReadTableRangeWithFields() {
        List<String> result = List.of("row1", "row2");
        TableField<String> field = (instance, obj) -> {
        };
        when(tableMock.readTable(1, 2, String.class, field)).thenReturn(result);
        List<String> tableResult = testService.readTable(dummyComponentType, 1, 2, String.class, field);
        assertEquals(result, tableResult);
        verify(tableMock).readTable(1, 2, String.class, field);
    }

    @Test
    void testReadRowByIndex() {
        String row = "row";
        when(tableMock.readRow(2, String.class)).thenReturn(row);
        String result = testService.readRow(dummyComponentType, 2, String.class);
        assertEquals(row, result);
        verify(tableMock).readRow(2, String.class);
    }

    @Test
    void testReadRowByCriteria() {
        String row = "row";
        List<String> criteria = List.of("search");
        when(tableMock.readRow(criteria, String.class)).thenReturn(row);
        String result = testService.readRow(dummyComponentType, criteria, String.class);
        assertEquals(row, result);
        verify(tableMock).readRow(criteria, String.class);
    }

    @Test
    void testReadRowByIndexWithFields() {
        String row = "row";
        TableField<String> field = (instance, obj) -> {
        };
        when(tableMock.readRow(2, String.class, field)).thenReturn(row);
        String result = testService.readRow(dummyComponentType, 2, String.class, field);
        assertEquals(row, result);
        verify(tableMock).readRow(2, String.class, field);
    }

    @Test
    void testReadRowByCriteriaWithFields() {
        String row = "row";
        List<String> criteria = List.of("search");
        TableField<String> field = (instance, obj) -> {
        };
        when(tableMock.readRow(criteria, String.class, field)).thenReturn(row);
        String result = testService.readRow(dummyComponentType, criteria, String.class, field);
        assertEquals(row, result);
        verify(tableMock).readRow(criteria, String.class, field);
    }

    @Test
    void testInsertCellValueDataByIndex() {
        testService.insertCellValue(dummyComponentType, 2, String.class, "data");
        verify(tableMock).insertCellValue(2, String.class, "data");
    }

    @Test
    void testInsertCellValueFieldByIndex() {
        TableField<String> field = (instance, obj) -> {
        };
        testService.insertCellValue(dummyComponentType, 2, String.class, field, 1, "val");
        verify(tableMock).insertCellValue(2, String.class, field, 1, "val");
    }

    @Test
    void testInsertCellValueDataByCriteria() {
        List<String> criteria = List.of("search");
        testService.insertCellValue(dummyComponentType, criteria, String.class, "data");
        verify(tableMock).insertCellValue(criteria, String.class, "data");
    }

    @Test
    void testInsertCellValueFieldByCriteria() {
        List<String> criteria = List.of("search");
        TableField<String> field = (instance, obj) -> {
        };
        testService.insertCellValue(dummyComponentType, criteria, String.class, field, 1, "val");
        verify(tableMock).insertCellValue(criteria, String.class, field, 1, "val");
    }

    @Test
    void testFilterTable() {
        TableField<String> field = (instance, obj) -> {
        };
        testService.filterTable(dummyComponentType, String.class, field, FilterStrategy.SELECT, "val");
        verify(tableMock).filterTable(String.class, field, FilterStrategy.SELECT, "val");
    }

    @Test
    void testSortTable() {
        TableField<String> field = (instance, obj) -> {
        };
        testService.sortTable(dummyComponentType, String.class, field, SortingStrategy.ASC);
        verify(tableMock).sortTable(String.class, field, SortingStrategy.ASC);
    }

    enum DummyTableComponentType implements TableComponentType {
        DUMMY;

        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    static class TestTableServiceImpl extends TableServiceImpl {
        private final Table tableMock;

        TestTableServiceImpl(SmartWebDriver driver, TableServiceRegistry registry, Table tableMock) {
            super(driver, registry);
            this.tableMock = tableMock;
        }

        @Override
        protected Table getOrCreateComponent(TableComponentType componentType) {
            return tableMock;
        }
    }
}