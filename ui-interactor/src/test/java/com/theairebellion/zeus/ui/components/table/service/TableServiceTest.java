package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.service.mock.MockTableService;
import com.theairebellion.zeus.ui.components.table.sort.SortingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

@SuppressWarnings("all")
class TableServiceTest extends BaseUnitUITest {

    private MockTableService service;

    @BeforeEach
    void setUp() {
        service = new MockTableService();
    }

    @Nested
    class ReadTableTests {
        @Test
        void readTableDefault() {
            MockTableService spyService = spy(service);
            spyService.readTable(Object.class);
            verify(spyService, times(1)).readTable(TableService.DEFAULT_TYPE, Object.class);
        }

        @Test
        void readTableWithFieldsDefault() {
            MockTableService spyService = spy(service);
            TableField<Object> field = mock(TableField.class);
            spyService.readTable(Object.class, field);
            verify(spyService, times(1)).readTable(TableService.DEFAULT_TYPE, Object.class, field);
        }

        @Test
        void readTableRangeDefault() {
            MockTableService spyService = spy(service);
            spyService.readTable(1, 2, Object.class);
            verify(spyService, times(1)).readTable(TableService.DEFAULT_TYPE, 1, 2, Object.class);
        }

        @Test
        void readTableRangeWithFieldsDefault() {
            MockTableService spyService = spy(service);
            TableField<Object> field = mock(TableField.class);
            spyService.readTable(1, 2, Object.class, field);
            verify(spyService, times(1)).readTable(TableService.DEFAULT_TYPE, 1, 2, Object.class, field);
        }
    }

    @Nested
    class ReadRowTests {
        @Test
        void readRowByIndexDefault() {
            MockTableService spyService = spy(service);
            spyService.readRow(3, Object.class);
            verify(spyService, times(1)).readRow(TableService.DEFAULT_TYPE, 3, Object.class);
        }

        @Test
        void readRowByCriteriaDefault() {
            MockTableService spyService = spy(service);
            spyService.readRow(List.of("test"), Object.class);
            verify(spyService, times(1)).readRow(TableService.DEFAULT_TYPE, List.of("test"), Object.class);
        }

        @Test
        void readRowByIndexWithFieldsDefault() {
            MockTableService spyService = spy(service);
            TableField<Object> field = mock(TableField.class);
            spyService.readRow(2, Object.class, field);
            verify(spyService, times(1)).readRow(TableService.DEFAULT_TYPE, 2, Object.class, field);
        }

        @Test
        void readRowByCriteriaWithFieldsDefault() {
            MockTableService spyService = spy(service);
            TableField<Object> field = mock(TableField.class);
            spyService.readRow(List.of("criteria"), Object.class, field);
            verify(spyService, times(1)).readRow(TableService.DEFAULT_TYPE, List.of("criteria"), Object.class, field);
        }
    }

    @Nested
    class InsertCellValueTests {
        @Test
        void insertCellValueByRowDefault() {
            MockTableService spyService = spy(service);
            Object data = new Object();
            spyService.insertCellValue(4, Object.class, data);
            verify(spyService, times(1)).insertCellValue(TableService.DEFAULT_TYPE, 4, Object.class, data);
        }

        @Test
        void insertCellValueByRowAndFieldDefault() {
            MockTableService spyService = spy(service);
            TableField<Object> field = mock(TableField.class);
            spyService.insertCellValue(4, Object.class, field, "val");
            verify(spyService, times(1)).insertCellValue(TableService.DEFAULT_TYPE, 4, Object.class, field, 1, "val");
        }

        @Test
        void insertCellValueByRowAndFieldIndexDefault() {
            MockTableService spyService = spy(service);
            TableField<Object> field = mock(TableField.class);
            spyService.insertCellValue(3, Object.class, field, 2, "val1");
            verify(spyService, times(1)).insertCellValue(TableService.DEFAULT_TYPE, 3, Object.class, field, 2, "val1");
        }

        @Test
        void insertCellValueByCriteriaAndFieldDefault() {
            MockTableService spyService = spy(service);
            TableField<Object> field = mock(TableField.class);
            spyService.insertCellValue(List.of("c"), Object.class, field, "val");
            verify(spyService, times(1)).insertCellValue(TableService.DEFAULT_TYPE, List.of("c"), Object.class, field, 1, "val");
        }

        @Test
        void insertCellValueByCriteriaAndFieldIndexDefault() {
            MockTableService spyService = spy(service);
            TableField<Object> field = mock(TableField.class);
            spyService.insertCellValue(List.of("x"), Object.class, field, 2, "val2");
            verify(spyService, times(1)).insertCellValue(TableService.DEFAULT_TYPE, List.of("x"), Object.class, field, 2, "val2");
        }

        @Test
        void insertCellValueByCriteriaObjectDefault() {
            MockTableService spyService = spy(service);
            Object data = new Object();
            spyService.insertCellValue(List.of("z"), Object.class, data);
            verify(spyService, times(1)).insertCellValue(TableService.DEFAULT_TYPE, List.of("z"), Object.class, data);
        }

        @Test
        void insertCellValueByRowAndFieldWithValues() {
            MockTableService spyService = spy(service);
            TableField<Object> field = mock(TableField.class);
            String[] values = {"val1", "val2"};
            spyService.insertCellValue(4, Object.class, field, values);
            verify(spyService, times(1)).insertCellValue(TableService.DEFAULT_TYPE, 4, Object.class, field, 1, values);
        }

        @Test
        void insertCellValueByCriteriaAndFieldWithValues() {
            MockTableService spyService = spy(service);
            TableField<Object> field = mock(TableField.class);
            String[] values = {"val1", "val2"};
            spyService.insertCellValue(List.of("c"), Object.class, field, values);
            verify(spyService, times(1)).insertCellValue(TableService.DEFAULT_TYPE, List.of("c"), Object.class, field, 1, values);
        }
    }

    @Nested
    class TableOperationTests {
        @Test
        void filterTableDefault() {
            MockTableService spyService = spy(service);
            TableField<Object> field = mock(TableField.class);
            spyService.filterTable(Object.class, field, FilterStrategy.SELECT, "val");
            verify(spyService, times(1)).filterTable(TableService.DEFAULT_TYPE, Object.class, field, FilterStrategy.SELECT, "val");
        }

        @Test
        void sortTableDefault() {
            MockTableService spyService = spy(service);
            TableField<Object> field = mock(TableField.class);
            spyService.sortTable(Object.class, field, SortingStrategy.ASC);
            verify(spyService, times(1)).sortTable(TableService.DEFAULT_TYPE, Object.class, field, SortingStrategy.ASC);
        }
    }
}