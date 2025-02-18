package com.theairebellion.zeus.ui.components.table.base;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableFieldTest {

    @Setter
    @Getter
    static class Dummy {
        private String value;

    }

    @Test
    public void testInvoke() throws IllegalAccessException, InvocationTargetException {
        TableField<Dummy> field = TableField.of(Dummy::setValue);
        Dummy dummy = new Dummy();
        field.invoke(dummy, "test");
        assertEquals("test", dummy.getValue());
    }
}