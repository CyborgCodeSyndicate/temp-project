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

    public CheckboxComponentType lastComponentTypeUsed;
    public CheckboxComponentType explicitComponentType;
    public SmartWebElement lastContainer;
    public String[] lastCheckboxText;
    public By[] lastCheckboxLocators;
    public Strategy lastStrategy;
    public boolean returnSelected = true;
    public boolean returnEnabled = true;
    public List<String> returnSelectedList = Collections.emptyList();
    public List<String> returnAllList = Collections.emptyList();

    private static final String SELECT_STRATEGY_MOCK_RESULT = "selectStrategyMock";
    private static final String DESELECT_STRATEGY_MOCK_RESULT = "deSelectStrategyMock";


    public MockCheckboxService() {
        reset();
    }

    private void setLastType(CheckboxComponentType type) {
        this.explicitComponentType = type;
        if (MockCheckboxComponentType.DUMMY_CHECKBOX.equals(type)) {
            this.lastComponentTypeUsed = MockCheckboxComponentType.DUMMY_CHECKBOX;
        } else {
            this.lastComponentTypeUsed = null;
        }
    }

    public void reset() {
        lastComponentTypeUsed = null;
        explicitComponentType = MockCheckboxComponentType.DUMMY_CHECKBOX;
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
        setLastType(componentType);
        lastContainer = container;
        lastCheckboxText = checkBoxText;
    }

    @Override
    public String select(CheckboxComponentType componentType, SmartWebElement container, Strategy strategy) {
        setLastType(componentType);
        lastContainer = container;
        lastStrategy = strategy;
        return SELECT_STRATEGY_MOCK_RESULT;
    }

    @Override
    public void select(CheckboxComponentType componentType, String... checkBoxText) {
        setLastType(componentType);
        lastCheckboxText = checkBoxText;
    }

    @Override
    public void select(CheckboxComponentType componentType, By... checkBoxLocator) {
        setLastType(componentType);
        lastCheckboxLocators = checkBoxLocator;
    }

    @Override
    public void deSelect(CheckboxComponentType componentType, SmartWebElement container, String... checkBoxText) {
        setLastType(componentType);
        lastContainer = container;
        lastCheckboxText = checkBoxText;
    }

    @Override
    public String deSelect(CheckboxComponentType componentType, SmartWebElement container, Strategy strategy) {
        setLastType(componentType);
        lastContainer = container;
        lastStrategy = strategy;
        return DESELECT_STRATEGY_MOCK_RESULT;
    }

    @Override
    public void deSelect(CheckboxComponentType componentType, String... checkBoxText) {
        setLastType(componentType);
        lastCheckboxText = checkBoxText;
    }

    @Override
    public void deSelect(CheckboxComponentType componentType, By... checkBoxLocator) {
        setLastType(componentType);
        lastCheckboxLocators = checkBoxLocator;
    }

    @Override
    public boolean areSelected(CheckboxComponentType componentType, SmartWebElement container, String... checkBoxText) {
        setLastType(componentType);
        lastContainer = container;
        lastCheckboxText = checkBoxText;
        return returnSelected;
    }

    @Override
    public boolean areSelected(CheckboxComponentType componentType, String... checkBoxText) {
        setLastType(componentType);
        lastCheckboxText = checkBoxText;
        return returnSelected;
    }

    @Override
    public boolean areSelected(CheckboxComponentType componentType, By... checkBoxLocator) {
        setLastType(componentType);
        lastCheckboxLocators = checkBoxLocator;
        return returnSelected;
    }

    @Override
    public boolean isSelected(CheckboxComponentType componentType, SmartWebElement container, String checkBoxText) {
        setLastType(componentType);
        lastContainer = container;
        lastCheckboxText = new String[]{checkBoxText};
        return returnSelected;
    }

    @Override
    public boolean isSelected(CheckboxComponentType componentType, String checkBoxText) {
        setLastType(componentType);
        lastCheckboxText = new String[]{checkBoxText};
        return returnSelected;
    }

    @Override
    public boolean isSelected(CheckboxComponentType componentType, By checkBoxLocator) {
        setLastType(componentType);
        lastCheckboxLocators = new By[]{checkBoxLocator};
        return returnSelected;
    }

    @Override
    public boolean areEnabled(CheckboxComponentType componentType, SmartWebElement container, String... checkBoxText) {
        setLastType(componentType);
        lastContainer = container;
        lastCheckboxText = checkBoxText;
        return returnEnabled;
    }

    @Override
    public boolean areEnabled(CheckboxComponentType componentType, String... checkBoxText) {
        setLastType(componentType);
        lastCheckboxText = checkBoxText;
        return returnEnabled;
    }

    @Override
    public boolean areEnabled(CheckboxComponentType componentType, By... checkBoxLocator) {
        setLastType(componentType);
        lastCheckboxLocators = checkBoxLocator;
        return returnEnabled;
    }

    @Override
    public boolean isEnabled(CheckboxComponentType componentType, SmartWebElement container, String checkBoxText) {
        setLastType(componentType);
        lastContainer = container;
        lastCheckboxText = new String[]{checkBoxText};
        return returnEnabled;
    }

    @Override
    public boolean isEnabled(CheckboxComponentType componentType, String checkBoxText) {
        setLastType(componentType);
        lastCheckboxText = new String[]{checkBoxText};
        return returnEnabled;
    }

    @Override
    public boolean isEnabled(CheckboxComponentType componentType, By checkBoxLocator) {
        setLastType(componentType);
        lastCheckboxLocators = new By[]{checkBoxLocator};
        return returnEnabled;
    }

    @Override
    public List<String> getSelected(CheckboxComponentType componentType, SmartWebElement container) {
        setLastType(componentType);
        lastContainer = container;
        return returnSelectedList;
    }

    @Override
    public List<String> getSelected(CheckboxComponentType componentType, By containerLocator) {
        setLastType(componentType);
        lastCheckboxLocators = new By[]{containerLocator};
        return returnSelectedList;
    }

    @Override
    public List<String> getAll(CheckboxComponentType componentType, SmartWebElement container) {
        setLastType(componentType);
        lastContainer = container;
        return returnAllList;
    }

    @Override
    public List<String> getAll(CheckboxComponentType componentType, By containerLocator) {
        setLastType(componentType);
        lastCheckboxLocators = new By[]{containerLocator};
        return returnAllList;
    }

    @Override
    public void insertion(ComponentType componentType, By locator, Object... values) {
        setLastType((CheckboxComponentType) componentType);
        lastCheckboxLocators = new By[]{locator};
        lastCheckboxText = Arrays.stream(values)
                .map(String::valueOf)
                .toArray(String[]::new);
    }
}