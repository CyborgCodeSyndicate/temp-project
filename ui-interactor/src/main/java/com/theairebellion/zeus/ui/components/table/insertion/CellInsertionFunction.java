package com.theairebellion.zeus.ui.components.table.insertion;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

import java.util.function.BiConsumer;

public interface CellInsertionFunction extends BiConsumer<SmartWebElement, String[]> {


    void cellInsertionFunction(SmartWebElement cellElement, String... values);

    @Override
    default void accept(SmartWebElement smartWebElement, String[] objects) {
        cellInsertionFunction(smartWebElement, objects);
    }

}
