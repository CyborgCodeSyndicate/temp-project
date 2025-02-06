package com.theairebellion.zeus.ui.components.table.filters;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.TriConsumer;

public interface CellFilterFunction extends TriConsumer<SmartWebElement, FilterStrategy, String[]> {


    void cellFilterFunction(SmartWebElement cellElement, FilterStrategy filterStrategy, String... values);

    @Override
    default void accept(SmartWebElement smartWebElement, FilterStrategy filterStrategy, String[] objects) {
        cellFilterFunction(smartWebElement, filterStrategy, objects);
    }

}
