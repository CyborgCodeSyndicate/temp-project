package com.theairebellion.zeus.ui.components.table.base;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TableComponentTypeTest {

    enum DummyTableComponentType implements TableComponentType {
        VALUE;
        @Override
        public Enum<?> getType() {
            return this;
        }
    }

    @Test
    public void testGetType() {
        DummyTableComponentType type = DummyTableComponentType.VALUE;
        assertEquals(DummyTableComponentType.VALUE, type.getType());
    }
}