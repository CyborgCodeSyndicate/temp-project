package com.theairebellion.zeus.ui.components.select.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.components.select.SelectService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.Collections;
import java.util.List;

public class MockSelectService implements SelectService {

    public SelectComponentType lastComponentTypeUsed;
    public SelectComponentType explicitComponentType;
    public SmartWebElement lastContainer;
    public By lastLocator;
    public String[] lastValues;
    public Strategy lastStrategy;
    public List<String> returnOptions = Collections.emptyList();
    public boolean returnBool = false;

    public MockSelectService() {
        reset();
    }

    private void setLastType(SelectComponentType type) {
        this.explicitComponentType = type;
        if (MockSelectComponentType.DUMMY_SELECT.equals(type)) {
            this.lastComponentTypeUsed = MockSelectComponentType.DUMMY_SELECT;
        } else {
            this.lastComponentTypeUsed = null;
        }
    }

    public void reset() {
        lastComponentTypeUsed = null;
        explicitComponentType = MockSelectComponentType.DUMMY_SELECT;
        lastContainer = null;
        lastLocator = null;
        lastValues = null;
        lastStrategy = null;
        returnOptions = Collections.emptyList();
        returnBool = false;
    }

    @Override
    public void selectOptions(SelectComponentType componentType, SmartWebElement container, String... values) {
        setLastType(componentType);
        lastContainer = container;
        lastValues = values;
    }

    @Override
    public void selectOption(SelectComponentType componentType, SmartWebElement container, String value) {
        setLastType(componentType);
        lastContainer = container;
        lastValues = new String[]{value};
    }

    @Override
    public void selectOptions(SelectComponentType componentType, By containerLocator, String... values) {
        setLastType(componentType);
        lastLocator = containerLocator;
        lastValues = values;
    }

    @Override
    public void selectOption(SelectComponentType componentType, By containerLocator, String value) {
        setLastType(componentType);
        lastLocator = containerLocator;
        lastValues = new String[]{value};
    }

    @Override
    public List<String> selectOptions(SelectComponentType componentType, SmartWebElement container, Strategy strategy) {
        setLastType(componentType);
        lastContainer = container;
        lastStrategy = strategy;
        return returnOptions;
    }

    @Override
    public List<String> selectOptions(SelectComponentType componentType, By containerLocator, Strategy strategy) {
        setLastType(componentType);
        lastLocator = containerLocator;
        lastStrategy = strategy;
        return returnOptions;
    }

    @Override
    public List<String> getAvailableOptions(SelectComponentType componentType, SmartWebElement container) {
        setLastType(componentType);
        lastContainer = container;
        return returnOptions;
    }

    @Override
    public List<String> getAvailableOptions(SelectComponentType componentType, By containerLocator) {
        setLastType(componentType);
        lastLocator = containerLocator;
        return returnOptions;
    }

    @Override
    public List<String> getSelectedOptions(SelectComponentType componentType, SmartWebElement container) {
        setLastType(componentType);
        lastContainer = container;
        return returnOptions;
    }

    @Override
    public List<String> getSelectedOptions(SelectComponentType componentType, By containerLocator) {
        setLastType(componentType);
        lastLocator = containerLocator;
        return returnOptions;
    }

    @Override
    public boolean isOptionVisible(SelectComponentType componentType, SmartWebElement container, String value) {
        setLastType(componentType);
        lastContainer = container;
        lastValues = new String[]{value};
        return returnBool;
    }

    @Override
    public boolean isOptionVisible(SelectComponentType componentType, By containerLocator, String value) {
        setLastType(componentType);
        lastLocator = containerLocator;
        lastValues = new String[]{value};
        return returnBool;
    }

    @Override
    public boolean isOptionEnabled(SelectComponentType componentType, SmartWebElement container, String value) {
        setLastType(componentType);
        lastContainer = container;
        lastValues = new String[]{value};
        return returnBool;
    }

    @Override
    public boolean isOptionEnabled(SelectComponentType componentType, By containerLocator, String value) {
        setLastType(componentType);
        lastLocator = containerLocator;
        lastValues = new String[]{value};
        return returnBool;
    }

    @Override
    public void insertion(ComponentType componentType, By locator, Object... values) {
        setLastType((SelectComponentType) componentType);
        lastLocator = locator;
        if (values != null && values.length > 0) {
            String[] strValues = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                strValues[i] = String.valueOf(values[i]);
            }
            lastValues = strValues;
        }
    }
}