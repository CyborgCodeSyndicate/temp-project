package com.theairebellion.zeus.ui.components.table.insertion.mock;

import com.theairebellion.zeus.ui.components.table.insertion.CellInsertionFunction;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

public class TestCellInsertionFunction implements CellInsertionFunction {

   public SmartWebElement capturedElement;
   public String[] capturedValues;

   @Override
   public void cellInsertionFunction(SmartWebElement cellElement, String... values) {
      capturedElement = cellElement;
      capturedValues = values;
   }
}
