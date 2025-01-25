package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CheckboxServiceImpl implements CheckboxService {

    protected SmartSelenium smartSelenium;
    private static Map<CheckboxComponentType, Checkbox> components;

    public CheckboxServiceImpl(WebDriver driver) {
        this.smartSelenium = new SmartSelenium(driver);
        components = new HashMap<>();
    }

    public CheckboxServiceImpl(SmartSelenium smartSelenium) {
        this.smartSelenium = smartSelenium;
        components = new HashMap<>();
    }

    @Override
    public void select(final CheckboxComponentType componentType, final WebElement container, final String... checkBoxText) {
        checkboxComponent(componentType).select(container, checkBoxText);
    }

    @Override
    public String select(final CheckboxComponentType componentType, final WebElement container, final Strategy strategy) {
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
    public void deSelect(final CheckboxComponentType componentType, final WebElement container, final String... checkBoxText) {
        checkboxComponent(componentType).deSelect(container, checkBoxText);
    }

    @Override
    public String deSelect(final CheckboxComponentType componentType, final WebElement container, final Strategy strategy) {
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
    public boolean areSelected(CheckboxComponentType componentType, WebElement container, String... checkBoxText) {
        return checkboxComponent(componentType).areSelected(container, checkBoxText);
    }

    @Override
    public boolean areSelected(CheckboxComponentType componentType, String... checkBoxText) {
        return checkboxComponent(componentType).areSelected(checkBoxText);
    }

    @Override
    public boolean areSelected(CheckboxComponentType componentType, By... checkBoxLocator) {
        return checkboxComponent(componentType).areSelected(checkBoxLocator);
    }

    @Override
    public boolean areEnabled(CheckboxComponentType componentType, WebElement container, String... checkBoxText) {
        return checkboxComponent(componentType).areEnabled(container, checkBoxText);
    }

    @Override
    public boolean areEnabled(CheckboxComponentType componentType, String... checkBoxText) {
        return checkboxComponent(componentType).areEnabled(checkBoxText);
    }

    @Override
    public boolean areEnabled(CheckboxComponentType componentType, By... checkBoxLocator) {
        return checkboxComponent(componentType).areEnabled(checkBoxLocator);
    }

    @Override
    public List<String> getSelected(CheckboxComponentType componentType, WebElement container) {
        return checkboxComponent(componentType).getSelected(container);
    }

    @Override
    public List<String> getAll(CheckboxComponentType componentType, WebElement container) {
        return checkboxComponent(componentType).getAll(container);
    }

    @Override
    public void insertion(ComponentType componentType, By locator, Object... values) {
        select((CheckboxComponentType) componentType, (String) values[0]);
    }

    private Checkbox checkboxComponent(CheckboxComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getCheckBoxComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}
