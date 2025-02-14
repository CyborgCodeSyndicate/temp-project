package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CheckboxServiceImpl extends AbstractComponentService<CheckboxComponentType, Checkbox> implements CheckboxService {

    public CheckboxServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Checkbox createComponent(CheckboxComponentType componentType) {
        return ComponentFactory.getCheckBoxComponent(componentType, driver);
    }

    @Override
    public void select(final CheckboxComponentType componentType, final SmartWebElement container, final String... checkBoxText) {
        checkboxComponent(componentType).select(container, checkBoxText);
    }

    @Override
    public String select(final CheckboxComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        return checkboxComponent(componentType).select(container, strategy);
    }

    @Override
    public void select(final CheckboxComponentType componentType, final String... checkBoxText) {
        checkboxComponent(componentType).select(checkBoxText);
    }

    @Override
    public void select(final CheckboxComponentType componentType, final By... checkBoxLocator) {
        checkboxComponent(componentType).select(checkBoxLocator);
    }

    @Override
    public void deSelect(final CheckboxComponentType componentType, final SmartWebElement container, final String... checkBoxText) {
        checkboxComponent(componentType).deSelect(container, checkBoxText);
    }

    @Override
    public String deSelect(final CheckboxComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        return checkboxComponent(componentType).deSelect(container, strategy);
    }

    @Override
    public void deSelect(final CheckboxComponentType componentType, final String... checkBoxText) {
        checkboxComponent(componentType).deSelect(checkBoxText);
    }

    @Override
    public void deSelect(final CheckboxComponentType componentType, final By... checkBoxLocator) {
        checkboxComponent(componentType).deSelect(checkBoxLocator);
    }

    @Override
    public boolean areSelected(final CheckboxComponentType componentType, final SmartWebElement container, final String... checkBoxText) {
        return checkboxComponent(componentType).areSelected(container, checkBoxText);
    }

    @Override
    public boolean areSelected(final CheckboxComponentType componentType, final String... checkBoxText) {
        return checkboxComponent(componentType).areSelected(checkBoxText);
    }

    @Override
    public boolean areSelected(final CheckboxComponentType componentType, final By... checkBoxLocator) {
        return checkboxComponent(componentType).areSelected(checkBoxLocator);
    }

    @Override
    public boolean isSelected(final CheckboxComponentType componentType, final SmartWebElement container, final String checkBoxText) {
        return checkboxComponent(componentType).areSelected(container, checkBoxText);
    }

    @Override
    public boolean isSelected(final CheckboxComponentType componentType, final String checkBoxText) {
        return checkboxComponent(componentType).areSelected(checkBoxText);
    }

    @Override
    public boolean isSelected(final CheckboxComponentType componentType, final By checkBoxLocator) {
        return checkboxComponent(componentType).areSelected(checkBoxLocator);
    }

    @Override
    public boolean areEnabled(final CheckboxComponentType componentType, final SmartWebElement container, final String... checkBoxText) {
        return checkboxComponent(componentType).areEnabled(container, checkBoxText);
    }

    @Override
    public boolean areEnabled(final CheckboxComponentType componentType, final String... checkBoxText) {
        return checkboxComponent(componentType).areEnabled(checkBoxText);
    }

    @Override
    public boolean areEnabled(final CheckboxComponentType componentType, final By... checkBoxLocator) {
        return checkboxComponent(componentType).areEnabled(checkBoxLocator);
    }

    @Override
    public boolean isEnabled(final CheckboxComponentType componentType, final SmartWebElement container, final String checkBoxText) {
        return checkboxComponent(componentType).areEnabled(container, checkBoxText);
    }

    @Override
    public boolean isEnabled(final CheckboxComponentType componentType, final String checkBoxText) {
        return checkboxComponent(componentType).areEnabled(checkBoxText);
    }

    @Override
    public boolean isEnabled(final CheckboxComponentType componentType, final By checkBoxLocator) {
        return checkboxComponent(componentType).areEnabled(checkBoxLocator);
    }

    @Override
    public List<String> getSelected(final CheckboxComponentType componentType, final SmartWebElement container) {
        return checkboxComponent(componentType).getSelected(container);
    }

    @Override
    public List<String> getSelected(final CheckboxComponentType componentType, final By containerLocator) {
        return checkboxComponent(componentType).getSelected(containerLocator);
    }

    @Override
    public List<String> getAll(final CheckboxComponentType componentType, final SmartWebElement container) {
        return checkboxComponent(componentType).getAll(container);
    }

    @Override
    public List<String> getAll(final CheckboxComponentType componentType, final By containerLocator) {
        return checkboxComponent(componentType).getAll(containerLocator);
    }

    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        select((CheckboxComponentType) componentType, (String) values[0]);
    }

    private Checkbox checkboxComponent(CheckboxComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
