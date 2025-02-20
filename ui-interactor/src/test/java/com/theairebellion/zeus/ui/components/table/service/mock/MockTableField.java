package com.theairebellion.zeus.ui.components.table.service.mock;

import com.theairebellion.zeus.ui.components.table.base.TableField;
import com.theairebellion.zeus.ui.components.table.model.TableCell;

import java.lang.reflect.InvocationTargetException;

public class MockTableField implements TableField<MockRow> {

    private final String fieldName;

    public MockTableField(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void invoke(MockRow instance, Object o) throws IllegalAccessException, InvocationTargetException {
        try {
            MockRow.class.getDeclaredMethod("setCell", TableCell.class).invoke(instance, o);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}