package com.theairebellion.zeus.ui.components.table.filters.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

public class TestTableFilter implements TableFilter {
   public SmartWebElement capturedElement;
   public ComponentType capturedComponent;
   public FilterStrategy capturedStrategy;
   public String[] capturedValues;

   @Override
   public void tableFilter(SmartWebElement cellElement, ComponentType componentType, FilterStrategy filterStrategy, String... values) {
      capturedElement = cellElement;
      capturedComponent = componentType;
      capturedStrategy = filterStrategy;
      capturedValues = values;
   }
}
