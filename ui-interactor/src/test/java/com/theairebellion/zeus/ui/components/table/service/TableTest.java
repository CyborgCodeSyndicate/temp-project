package com.theairebellion.zeus.ui.components.table.service;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.base.TableField;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

@SuppressWarnings("all")
class TableTest extends BaseUnitUITest {

    @Test
    void insertCellValueWithRowDefaultIndex() {
        Table table = Mockito.mock(Table.class, Mockito.withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
        TableField<Object> field = Mockito.mock(TableField.class);
        table.insertCellValue(3, Object.class, field, "val1", "val2");
        Mockito.verify(table).insertCellValue(3, Object.class, field, 1, "val1", "val2");
    }

    @Test
    void insertCellValueWithSearchCriteriaDefaultIndex() {
        Table table = Mockito.mock(Table.class, Mockito.withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
        TableField<Object> field = Mockito.mock(TableField.class);
        table.insertCellValue(List.of("test"), Object.class, field, "val");
        Mockito.verify(table).insertCellValue(List.of("test"), Object.class, field, 1, "val");
    }
}
