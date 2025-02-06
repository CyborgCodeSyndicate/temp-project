package com.theairebellion.zeus.ui.components.table.filters;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

public interface TableFilter {

    void tableFilter(SmartWebElement cellElement, ComponentType componentType, FilterStrategy filterStrategy, String... values);

}
