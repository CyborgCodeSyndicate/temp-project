package com.theairebellion.zeus.ui.components.table.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

public interface TableInsertion {

    void tableInsertion(SmartWebElement cellElement, ComponentType componentType, String... values);

}
