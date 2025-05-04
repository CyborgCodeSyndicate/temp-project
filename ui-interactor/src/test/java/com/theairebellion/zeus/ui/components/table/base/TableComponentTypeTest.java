package com.theairebellion.zeus.ui.components.table.base;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.components.table.base.mock.MockTableComponentType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TableComponentTypeTest extends BaseUnitUITest {

    @Test
    public void testGetType() {
        MockTableComponentType type = MockTableComponentType.VALUE;
        assertEquals(MockTableComponentType.VALUE, type.getType());
    }
}