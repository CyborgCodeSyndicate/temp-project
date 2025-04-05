package com.theairebellion.zeus.ui.components.checkbox.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MockCheckboxService implements CheckboxService {

    public CheckboxComponentType lastComponentType;
    public SmartWebElement lastContainer;
    public String[] lastCheckboxText;
    public By[] lastCheckboxLocators;
    public Strategy lastStrategy;
    public boolean returnSelected = true;
    public boolean returnEnabled = true;
    public List<String> returnSelectedList = Collections.emptyList();
    public List<String> returnAllList = Collections.emptyList();

    public void reset() {
        lastComponentType = null;
        lastContainer = null;
        lastCheckboxText = null;
        lastCheckboxLocators = null;
        lastStrategy = null;
        returnSelected = true;
        returnEnabled = true;
        returnSelectedList = Collections.emptyList();
        returnAllList = Collections.emptyList();
    }

    @Override
    public void select(CheckboxComponentType componentType, SmartWebElement container, String... checkBoxText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastCheckboxText = checkBoxText;
    }

    @Override
    public String select(CheckboxComponentType componentType, SmartWebElement container, Strategy strategy) {
        lastComponentType = componentType;
        lastContainer = container;
        lastStrategy = strategy;
        return "selectStrategyMock";
    }

    @Override
    public void select(CheckboxComponentType componentType, String... checkBoxText) {
        lastComponentType = componentType;
        lastCheckboxText = checkBoxText;
    }

    @Override
    public void select(CheckboxComponentType componentType, By... checkBoxLocator) {
        lastComponentType = componentType;
        lastCheckboxLocators = checkBoxLocator;
    }

    @Override
    public void deSelect(CheckboxComponentType componentType, SmartWebElement container, String... checkBoxText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastCheckboxText = checkBoxText;
    }

    @Override
    public String deSelect(CheckboxComponentType componentType, SmartWebElement container, Strategy strategy) {
        lastComponentType = componentType;
        lastContainer = container;
        lastStrategy = strategy;
        return "deSelectStrategyMock";
    }

    @Override
    public void deSelect(CheckboxComponentType componentType, String... checkBoxText) {
        lastComponentType = componentType;
        lastCheckboxText = checkBoxText;
    }

    @Override
    public void deSelect(CheckboxComponentType componentType, By... checkBoxLocator) {
        lastComponentType = componentType;
        lastCheckboxLocators = checkBoxLocator;
    }

    @Override
    public boolean areSelected(CheckboxComponentType componentType, SmartWebElement container, String... checkBoxText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastCheckboxText = checkBoxText;
        return returnSelected;
    }

    @Override
    public boolean areSelected(CheckboxComponentType componentType, String... checkBoxText) {
        lastComponentType = componentType;
        lastCheckboxText = checkBoxText;
        return returnSelected;
    }

    @Override
    public boolean areSelected(CheckboxComponentType componentType, By... checkBoxLocator) {
        lastComponentType = componentType;
        lastCheckboxLocators = checkBoxLocator;
        return returnSelected;
    }

    @Override
    public boolean isSelected(CheckboxComponentType componentType, SmartWebElement container, String checkBoxText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastCheckboxText = new String[]{checkBoxText};
        return returnSelected;
    }

    @Override
    public boolean isSelected(CheckboxComponentType componentType, String checkBoxText) {
        lastComponentType = componentType;
        lastCheckboxText = new String[]{checkBoxText};
        return returnSelected;
    }

    @Override
    public boolean isSelected(CheckboxComponentType componentType, By checkBoxLocator) {
        lastComponentType = componentType;
        lastCheckboxLocators = new By[]{checkBoxLocator};
        return returnSelected;
    }

    @Override
    public boolean areEnabled(CheckboxComponentType componentType, SmartWebElement container, String... checkBoxText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastCheckboxText = checkBoxText;
        return returnEnabled;
    }

    @Override
    public boolean areEnabled(CheckboxComponentType componentType, String... checkBoxText) {
        lastComponentType = componentType;
        lastCheckboxText = checkBoxText;
        return returnEnabled;
    }

    @Override
    public boolean areEnabled(CheckboxComponentType componentType, By... checkBoxLocator) {
        lastComponentType = componentType;
        lastCheckboxLocators = checkBoxLocator;
        return returnEnabled;
    }

    @Override
    public boolean isEnabled(CheckboxComponentType componentType, SmartWebElement container, String checkBoxText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastCheckboxText = new String[]{checkBoxText};
        return returnEnabled;
    }

    @Override
    public boolean isEnabled(CheckboxComponentType componentType, String checkBoxText) {
        lastComponentType = componentType;
        lastCheckboxText = new String[]{checkBoxText};
        return returnEnabled;
    }

    @Override
    public boolean isEnabled(CheckboxComponentType componentType, By checkBoxLocator) {
        lastComponentType = componentType;
        lastCheckboxLocators = new By[]{checkBoxLocator};
        return returnEnabled;
    }

    @Override
    public List<String> getSelected(CheckboxComponentType componentType, SmartWebElement container) {
        lastComponentType = componentType;
        lastContainer = container;
        return returnSelectedList;
    }

    @Override
    public List<String> getSelected(CheckboxComponentType componentType, By containerLocator) {
        lastComponentType = componentType;
        lastCheckboxLocators = new By[]{containerLocator};
        return returnSelectedList;
    }

    @Override
    public List<String> getAll(CheckboxComponentType componentType, SmartWebElement container) {
        lastComponentType = componentType;
        lastContainer = container;
        return returnAllList;
    }

    @Override
    public List<String> getAll(CheckboxComponentType componentType, By containerLocator) {
        lastComponentType = componentType;
        lastCheckboxLocators = new By[]{containerLocator};
        return returnAllList;
    }

    @Override
    public void insertion(ComponentType componentType, By locator, Object... values) {
        lastComponentType = (CheckboxComponentType) componentType;
        lastCheckboxLocators = new By[]{locator};
        lastCheckboxText = Arrays.stream(values)
                .map(String::valueOf)
                .toArray(String[]::new);
    }
}