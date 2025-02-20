package com.theairebellion.zeus.ui.components.table.service.mock;

import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionFunction;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

public class FailingCellInsertionFunction implements CellInsertionFunction {

    @Override
    public void cellInsertionFunction(SmartWebElement cellElement, String... values) {
        throw new RuntimeException("Custom insertion failed");
    }
}
