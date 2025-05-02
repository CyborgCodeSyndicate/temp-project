package com.theairebellion.zeus.ui.components.table.registry.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

public class MockTableFilter implements TableFilter {

   @Override
   public void tableFilter(SmartWebElement cellElement, ComponentType componentType,
         FilterStrategy filterStrategy, String... values) {
   }
}
