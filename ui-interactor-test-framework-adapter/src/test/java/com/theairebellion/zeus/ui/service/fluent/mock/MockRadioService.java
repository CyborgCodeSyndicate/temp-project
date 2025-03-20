package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.Collections;
import java.util.List;

public class MockRadioService implements RadioService {

    public RadioComponentType lastComponentType;
    public SmartWebElement lastContainer;
    public By lastLocator;
    public String lastText;
    public Strategy lastStrategy;
    public boolean returnBool = false;
    public String returnSelected = "";
    public List<String> returnAll = Collections.emptyList();

    public void reset() {
        lastComponentType = null;
        lastContainer = null;
        lastLocator = null;
        lastText = null;
        lastStrategy = null;
        returnBool = false;
        returnSelected = "";
        returnAll = Collections.emptyList();
    }

    @Override
    public void select(RadioComponentType componentType, SmartWebElement container, String radioButtonText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastText = radioButtonText;
    }

    @Override
    public String select(RadioComponentType componentType, SmartWebElement container, Strategy strategy) {
        lastComponentType = componentType;
        lastContainer = container;
        lastStrategy = strategy;
        return returnSelected;
    }

    @Override
    public void select(RadioComponentType componentType, String radioButtonText) {
        lastComponentType = componentType;
        lastText = radioButtonText;
    }

    @Override
    public void select(RadioComponentType componentType, By radioButtonLocator) {
        lastComponentType = componentType;
        lastLocator = radioButtonLocator;
    }

    @Override
    public boolean isEnabled(RadioComponentType componentType, SmartWebElement container, String radioButtonText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastText = radioButtonText;
        return returnBool;
    }

    @Override
    public boolean isEnabled(RadioComponentType componentType, String radioButtonText) {
        lastComponentType = componentType;
        lastText = radioButtonText;
        return returnBool;
    }

    @Override
    public boolean isEnabled(RadioComponentType componentType, By radioButtonLocator) {
        lastComponentType = componentType;
        lastLocator = radioButtonLocator;
        return returnBool;
    }

    @Override
    public boolean isSelected(RadioComponentType componentType, SmartWebElement container, String radioButtonText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastText = radioButtonText;
        return returnBool;
    }

    @Override
    public boolean isSelected(RadioComponentType componentType, String radioButtonText) {
        lastComponentType = componentType;
        lastText = radioButtonText;
        return returnBool;
    }

    @Override
    public boolean isSelected(RadioComponentType componentType, By radioButtonLocator) {
        lastComponentType = componentType;
        lastLocator = radioButtonLocator;
        return returnBool;
    }

    @Override
    public boolean isVisible(RadioComponentType componentType, SmartWebElement container, String radioButtonText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastText = radioButtonText;
        return returnBool;
    }

    @Override
    public boolean isVisible(RadioComponentType componentType, String radioButtonText) {
        lastComponentType = componentType;
        lastText = radioButtonText;
        return returnBool;
    }

    @Override
    public boolean isVisible(RadioComponentType componentType, By radioButtonLocator) {
        lastComponentType = componentType;
        lastLocator = radioButtonLocator;
        return returnBool;
    }

    @Override
    public String getSelected(RadioComponentType componentType, SmartWebElement container) {
        lastComponentType = componentType;
        lastContainer = container;
        return returnSelected;
    }

    @Override
    public String getSelected(RadioComponentType componentType, By containerLocator) {
        lastComponentType = componentType;
        lastLocator = containerLocator;
        return returnSelected;
    }

    @Override
    public List<String> getAll(RadioComponentType componentType, SmartWebElement container) {
        lastComponentType = componentType;
        lastContainer = container;
        return returnAll;
    }

    @Override
    public List<String> getAll(RadioComponentType componentType, By containerLocator) {
        lastComponentType = componentType;
        lastLocator = containerLocator;
        return returnAll;
    }

    @Override
    public void insertion(ComponentType componentType, By locator, Object... values) {
        lastComponentType = (RadioComponentType) componentType;
        lastLocator = locator;
        if (values != null && values.length > 0) {
            lastText = String.valueOf(values[0]);
        }
    }
}