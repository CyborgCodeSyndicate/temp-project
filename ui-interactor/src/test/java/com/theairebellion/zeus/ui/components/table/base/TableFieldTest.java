package com.theairebellion.zeus.ui.components.table.base;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.base.mock.TableFieldTestRow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("TableField Tests")
class TableFieldTest extends BaseUnitUITest {

    @Test
    @DisplayName("of() and invoke() should correctly use setter method reference")
    public void testInvoke() throws IllegalAccessException, InvocationTargetException {
        // Given
        TableField<TableFieldTestRow> field = TableField.of(TableFieldTestRow::setValue);
        var mockInstance = new TableFieldTestRow();
        var testValue = "test";

        // When
        field.invoke(mockInstance, testValue);

        // Then
        assertEquals(testValue, mockInstance.getValue());
    }
}