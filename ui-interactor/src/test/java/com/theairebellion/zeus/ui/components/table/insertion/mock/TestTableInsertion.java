package com.theairebellion.zeus.ui.components.table.insertion.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;

public class TestTableInsertion implements TableInsertion {

    public SmartWebElement capturedElement;
    public ComponentType capturedComponent;
    public String[] capturedValues;

    @Override
    public void tableInsertion(SmartWebElement cellElement, ComponentType componentType, String... values) {
        capturedElement = cellElement;
        capturedComponent = componentType;
        capturedValues = values;
    }
}
