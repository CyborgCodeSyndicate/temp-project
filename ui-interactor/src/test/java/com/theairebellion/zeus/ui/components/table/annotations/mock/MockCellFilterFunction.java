package com.theairebellion.zeus.ui.components.table.annotations.mock;

import com.theairebellion.zeus.ui.components.table.filters.CellFilterFunction;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

public class MockCellFilterFunction implements CellFilterFunction {

   @Override
   public void cellFilterFunction(SmartWebElement cellElement,
         FilterStrategy filterStrategy, String... values) {
   }
}
