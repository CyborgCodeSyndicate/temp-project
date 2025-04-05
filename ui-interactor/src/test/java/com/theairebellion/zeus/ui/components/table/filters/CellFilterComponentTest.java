package com.theairebellion.zeus.ui.components.table.filters;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.filters.mock.MockComponentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellFilterComponentTest extends BaseUnitUITest {

    @Test
    void testGetters() {
        CellFilterComponent component = new CellFilterComponent(MockComponentType.class, "DUMMY_FILTER");
        assertEquals(MockComponentType.class, component.getType());
        assertEquals("DUMMY_FILTER", component.getComponentType());
    }
}