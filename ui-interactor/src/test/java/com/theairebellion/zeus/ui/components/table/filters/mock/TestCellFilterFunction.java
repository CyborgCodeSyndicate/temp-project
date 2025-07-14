package com.theairebellion.zeus.ui.components.table.filters.mock;

import com.theairebellion.zeus.ui.components.table.filters.CellFilterFunction;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

public class TestCellFilterFunction implements CellFilterFunction {

   public SmartWebElement capturedElement;
   public FilterStrategy capturedStrategy;
   public String[] capturedValues;

   @Override
   public void cellFilterFunction(SmartWebElement cellElement, FilterStrategy filterStrategy, String... values) {
      capturedElement = cellElement;
      capturedStrategy = filterStrategy;
      capturedValues = values;
   }
}
