package com.theairebellion.zeus.ui.components.table.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import org.junit.jupiter.api.Test;

public class FilterStrategyTest extends BaseUnitUITest {

    @Test
    void testEnumValues() {
        FilterStrategy[] strategies = FilterStrategy.values();
        assertEquals(5, strategies.length);
        assertEquals(FilterStrategy.SELECT_ONLY, FilterStrategy.valueOf("SELECT_ONLY"));
    }
}