package com.theairebellion.zeus.ui.components.table.insertion;

import com.theairebellion.zeus.ui.components.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.insertion.mock.MockComponentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellInsertionComponentTest extends BaseUnitUITest {

    @Test
    void testGetters() {
        int order = 3;
        String compTypeStr = "DUMMY_INSERT";
        CellInsertionComponent component = new CellInsertionComponent(MockComponentType.class, compTypeStr, order);
        assertEquals(MockComponentType.class, component.getType());
        assertEquals(compTypeStr, component.getComponentType());
        assertEquals(order, component.getOrder());
    }
}