package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import java.util.List;

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
        Allure.step(String.format("[UI - Checkbox] Selecting checkbox %s with text %s", componentType, String.join(", ", checkBoxText)));
        LogUI.step("Selecting checkbox " + componentType + " with text " + String.join(", ", checkBoxText));
        checkboxComponent(componentType).select(container, checkBoxText);
    }

    @Override
    public String select(final CheckboxComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        Allure.step(String.format("[UI - Checkbox] Selecting checkbox %s using strategy %s", componentType, strategy));
        LogUI.step("Selecting checkbox " + componentType + " using strategy " + strategy);
        return checkboxComponent(componentType).select(container, strategy);
    }

    @Override
    public void select(final CheckboxComponentType componentType, final String... checkBoxText) {
        Allure.step(String.format("[UI - Checkbox] Selecting checkbox %s with text %s", componentType, String.join(", ", checkBoxText)));
        LogUI.step("Selecting checkbox " + componentType + " with text " + String.join(", ", checkBoxText));
        checkboxComponent(componentType).select(checkBoxText);
    }

    @Override
    public void select(final CheckboxComponentType componentType, final By... checkBoxLocator) {
        Allure.step(String.format("[UI - Checkbox] Selecting checkbox %s by locator", componentType));
        LogUI.step("Selecting checkbox " + componentType + " by locator");
        checkboxComponent(componentType).select(checkBoxLocator);
    }

    @Override
    public void deSelect(final CheckboxComponentType componentType, final SmartWebElement container, final String... checkBoxText) {
        Allure.step(String.format("[UI - Checkbox] Deselecting checkbox %s with text %s", componentType, String.join(", ", checkBoxText)));
        LogUI.step("Deselecting checkbox " + componentType + " with text " + String.join(", ", checkBoxText));
        checkboxComponent(componentType).deSelect(container, checkBoxText);
    }

    @Override
    public String deSelect(final CheckboxComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        Allure.step(String.format("[UI - Checkbox] Deselecting checkbox %s using strategy %s", componentType, strategy));
        LogUI.step("Deselecting checkbox " + componentType + " using strategy " + strategy);
        return checkboxComponent(componentType).deSelect(container, strategy);
    }

    @Override
    public void deSelect(final CheckboxComponentType componentType, final String... checkBoxText) {
        Allure.step(String.format("[UI - Checkbox] Deselecting checkbox %s with text %s", componentType, String.join(", ", checkBoxText)));
        LogUI.step("Deselecting checkbox " + componentType + " with text " + String.join(", ", checkBoxText));
        checkboxComponent(componentType).deSelect(checkBoxText);
    }

    @Override
    public void deSelect(final CheckboxComponentType componentType, final By... checkBoxLocator) {
        Allure.step(String.format("[UI - Checkbox] Deselecting checkbox %s by locator", componentType));
        LogUI.step("Deselecting checkbox " + componentType + " by locator");
        checkboxComponent(componentType).deSelect(checkBoxLocator);
    }

    @Override
    public boolean areSelected(final CheckboxComponentType componentType, final SmartWebElement container, final String... checkBoxText) {
        Allure.step(String.format("[UI - Checkbox] Checking if checkboxes %s are selected", componentType));
        LogUI.step("Checking if checkboxes " + componentType + " are selected");
        return checkboxComponent(componentType).areSelected(container, checkBoxText);
    }

    @Override
    public boolean areSelected(final CheckboxComponentType componentType, final String... checkBoxText) {
        Allure.step(String.format("[UI - Checkbox] Checking if checkboxes %s are selected", componentType));
        LogUI.step("Checking if checkboxes " + componentType + " are selected");
        return checkboxComponent(componentType).areSelected(checkBoxText);
    }

    @Override
    public boolean areSelected(final CheckboxComponentType componentType, final By... checkBoxLocator) {
        Allure.step(String.format("[UI - Checkbox] Checking if checkboxes %s are selected by locator", componentType));
        LogUI.step("Checking if checkboxes " + componentType + " are selected by locator");
        return checkboxComponent(componentType).areSelected(checkBoxLocator);
    }

    @Override
    public boolean isSelected(final CheckboxComponentType componentType, final SmartWebElement container, final String checkBoxText) {
        Allure.step(String.format("[UI - Checkbox] Checking if checkbox %s is selected", componentType));
        LogUI.step("Checking if checkbox " + componentType + " is selected");
        return checkboxComponent(componentType).areSelected(container, checkBoxText);
    }

    @Override
    public boolean isSelected(final CheckboxComponentType componentType, final String checkBoxText) {
        Allure.step(String.format("[UI - Checkbox] Checking if checkbox %s is selected", componentType));
        LogUI.step("Checking if checkbox " + componentType + " is selected");
        return checkboxComponent(componentType).areSelected(checkBoxText);
    }

    @Override
    public boolean isSelected(final CheckboxComponentType componentType, final By checkBoxLocator) {
        Allure.step(String.format("[UI - Checkbox] Checking if checkbox %s is selected by locator", componentType));
        LogUI.step("Checking if checkbox " + componentType + " is selected by locator");
        return checkboxComponent(componentType).areSelected(checkBoxLocator);
    }

    @Override
    public boolean areEnabled(final CheckboxComponentType componentType, final SmartWebElement container, final String... checkBoxText) {
        Allure.step(String.format("[UI - Checkbox] Checking if checkboxes %s are enabled", componentType));
        LogUI.step("Checking if checkboxes " + componentType + " are enabled");
        return checkboxComponent(componentType).areEnabled(container, checkBoxText);
    }

    @Override
    public boolean areEnabled(final CheckboxComponentType componentType, final String... checkBoxText) {
        Allure.step(String.format("[UI - Checkbox] Checking if checkboxes %s are enabled", componentType));
        LogUI.step("Checking if checkboxes " + componentType + " are enabled");
        return checkboxComponent(componentType).areEnabled(checkBoxText);
    }

    @Override
    public boolean areEnabled(final CheckboxComponentType componentType, final By... checkBoxLocator) {
        Allure.step(String.format("[UI - Checkbox] Checking if checkboxes %s are enabled by locator", componentType));
        LogUI.step("Checking if checkboxes " + componentType + " are enabled by locator");
        return checkboxComponent(componentType).areEnabled(checkBoxLocator);
    }

    @Override
    public boolean isEnabled(final CheckboxComponentType componentType, final SmartWebElement container, final String checkBoxText) {
        Allure.step(String.format("[UI - Checkbox] Checking if checkbox %s is enabled", componentType));
        LogUI.step("Checking if checkbox " + componentType + " is enabled");
        return checkboxComponent(componentType).areEnabled(container, checkBoxText);
    }

    @Override
    public boolean isEnabled(final CheckboxComponentType componentType, final String checkBoxText) {
        Allure.step(String.format("[UI - Checkbox] Checking if checkbox %s is enabled", componentType));
        LogUI.step("Checking if checkbox " + componentType + " is enabled");
        return checkboxComponent(componentType).areEnabled(checkBoxText);
    }

    @Override
    public boolean isEnabled(final CheckboxComponentType componentType, final By checkBoxLocator) {
        Allure.step(String.format("[UI - Checkbox] Checking if checkbox %s is enabled by locator", componentType));
        LogUI.step("Checking if checkbox " + componentType + " is enabled by locator");
        return checkboxComponent(componentType).areEnabled(checkBoxLocator);
    }

    @Override
    public List<String> getSelected(final CheckboxComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Checkbox] Getting selected checkboxes %s", componentType));
        LogUI.step("Getting selected checkboxes " + componentType);
        return checkboxComponent(componentType).getSelected(container);
    }

    @Override
    public List<String> getSelected(final CheckboxComponentType componentType, final By containerLocator) {
        Allure.step(String.format("[UI - Checkbox] Getting selected checkboxes %s by locator", componentType));
        LogUI.step("Getting selected checkboxes " + componentType + " by locator");
        return checkboxComponent(componentType).getSelected(containerLocator);
    }

    @Override
    public List<String> getAll(final CheckboxComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Checkbox] Getting all checkboxes %s", componentType));
        LogUI.step("Getting all checkboxes " + componentType);
        return checkboxComponent(componentType).getAll(container);
    }

    @Override
    public List<String> getAll(final CheckboxComponentType componentType, final By containerLocator) {
        Allure.step(String.format("[UI - Checkbox] Getting all checkboxes %s by locator", componentType));
        LogUI.step("Getting all checkboxes " + componentType + " by locator");
        return checkboxComponent(componentType).getAll(containerLocator);
    }

    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        Allure.step(String.format("[UI - Checkbox] Inserting value into checkbox %s", componentType));
        LogUI.step("Inserting value into checkbox " + componentType);
        select((CheckboxComponentType) componentType, (String) values[0]);
    }

    private Checkbox checkboxComponent(CheckboxComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
