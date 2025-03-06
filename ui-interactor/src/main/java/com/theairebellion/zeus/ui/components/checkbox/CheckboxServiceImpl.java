package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import io.qameta.allure.Step;
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

    @Step("Selecting checkbox {componentType} with text {checkBoxText}")
    @Override
    public void select(final CheckboxComponentType componentType, final SmartWebElement container, final String... checkBoxText) {
        LogUI.step("Selecting checkbox " + componentType + " with text " + String.join(", ", checkBoxText));
        checkboxComponent(componentType).select(container, checkBoxText);
    }

    @Step("Selecting checkbox {componentType} using strategy {strategy}")
    @Override
    public String select(final CheckboxComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        LogUI.step("Selecting checkbox " + componentType + " using strategy " + strategy);
        return checkboxComponent(componentType).select(container, strategy);
    }

    @Step("Selecting checkbox {componentType} with text {checkBoxText}")
    @Override
    public void select(final CheckboxComponentType componentType, final String... checkBoxText) {
        LogUI.step("Selecting checkbox " + componentType + " with text " + String.join(", ", checkBoxText));
        checkboxComponent(componentType).select(checkBoxText);
    }

    @Step("Selecting checkbox {componentType} by locator")
    @Override
    public void select(final CheckboxComponentType componentType, final By... checkBoxLocator) {
        LogUI.step("Selecting checkbox " + componentType + " by locator");
        checkboxComponent(componentType).select(checkBoxLocator);
    }

    @Step("Deselecting checkbox {componentType} with text {checkBoxText}")
    @Override
    public void deSelect(final CheckboxComponentType componentType, final SmartWebElement container, final String... checkBoxText) {
        LogUI.step("Deselecting checkbox " + componentType + " with text " + String.join(", ", checkBoxText));
        checkboxComponent(componentType).deSelect(container, checkBoxText);
    }

    @Step("Deselecting checkbox {componentType} using strategy {strategy}")
    @Override
    public String deSelect(final CheckboxComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        LogUI.step("Deselecting checkbox " + componentType + " using strategy " + strategy);
        return checkboxComponent(componentType).deSelect(container, strategy);
    }

    @Step("Deselecting checkbox {componentType} with text {checkBoxText}")
    @Override
    public void deSelect(final CheckboxComponentType componentType, final String... checkBoxText) {
        LogUI.step("Deselecting checkbox " + componentType + " with text " + String.join(", ", checkBoxText));
        checkboxComponent(componentType).deSelect(checkBoxText);
    }

    @Step("Deselecting checkbox {componentType} by locator")
    @Override
    public void deSelect(final CheckboxComponentType componentType, final By... checkBoxLocator) {
        LogUI.step("Deselecting checkbox " + componentType + " by locator");
        checkboxComponent(componentType).deSelect(checkBoxLocator);
    }

    @Step("Checking if checkboxes {componentType} are selected")
    @Override
    public boolean areSelected(final CheckboxComponentType componentType, final SmartWebElement container, final String... checkBoxText) {
        LogUI.step("Checking if checkboxes " + componentType + " are selected");
        return checkboxComponent(componentType).areSelected(container, checkBoxText);
    }

    @Step("Checking if checkboxes {componentType} are selected")
    @Override
    public boolean areSelected(final CheckboxComponentType componentType, final String... checkBoxText) {
        LogUI.step("Checking if checkboxes " + componentType + " are selected");
        return checkboxComponent(componentType).areSelected(checkBoxText);
    }

    @Step("Checking if checkboxes {componentType} are selected by locator")
    @Override
    public boolean areSelected(final CheckboxComponentType componentType, final By... checkBoxLocator) {
        LogUI.step("Checking if checkboxes " + componentType + " are selected by locator");
        return checkboxComponent(componentType).areSelected(checkBoxLocator);
    }

    @Step("Checking if checkbox {componentType} is selected")
    @Override
    public boolean isSelected(final CheckboxComponentType componentType, final SmartWebElement container, final String checkBoxText) {
        LogUI.step("Checking if checkbox " + componentType + " is selected");
        return checkboxComponent(componentType).areSelected(container, checkBoxText);
    }

    @Step("Checking if checkbox {componentType} is selected")
    @Override
    public boolean isSelected(final CheckboxComponentType componentType, final String checkBoxText) {
        LogUI.step("Checking if checkbox " + componentType + " is selected");
        return checkboxComponent(componentType).areSelected(checkBoxText);
    }

    @Step("Checking if checkbox {componentType} is selected by locator")
    @Override
    public boolean isSelected(final CheckboxComponentType componentType, final By checkBoxLocator) {
        LogUI.step("Checking if checkbox " + componentType + " is selected by locator");
        return checkboxComponent(componentType).areSelected(checkBoxLocator);
    }

    @Step("Checking if checkboxes {componentType} are enabled")
    @Override
    public boolean areEnabled(final CheckboxComponentType componentType, final SmartWebElement container, final String... checkBoxText) {
        LogUI.step("Checking if checkboxes " + componentType + " are enabled");
        return checkboxComponent(componentType).areEnabled(container, checkBoxText);
    }

    @Step("Checking if checkboxes {componentType} are enabled")
    @Override
    public boolean areEnabled(final CheckboxComponentType componentType, final String... checkBoxText) {
        LogUI.step("Checking if checkboxes " + componentType + " are enabled");
        return checkboxComponent(componentType).areEnabled(checkBoxText);
    }

    @Step("Checking if checkboxes {componentType} are enabled by locator")
    @Override
    public boolean areEnabled(final CheckboxComponentType componentType, final By... checkBoxLocator) {
        LogUI.step("Checking if checkboxes " + componentType + " are enabled by locator");
        return checkboxComponent(componentType).areEnabled(checkBoxLocator);
    }

    @Step("Checking if checkbox {componentType} is enabled")
    @Override
    public boolean isEnabled(final CheckboxComponentType componentType, final SmartWebElement container, final String checkBoxText) {
        LogUI.step("Checking if checkbox " + componentType + " is enabled");
        return checkboxComponent(componentType).areEnabled(container, checkBoxText);
    }

    @Step("Checking if checkbox {componentType} is enabled")
    @Override
    public boolean isEnabled(final CheckboxComponentType componentType, final String checkBoxText) {
        LogUI.step("Checking if checkbox " + componentType + " is enabled");
        return checkboxComponent(componentType).areEnabled(checkBoxText);
    }

    @Step("Checking if checkbox {componentType} is enabled by locator")
    @Override
    public boolean isEnabled(final CheckboxComponentType componentType, final By checkBoxLocator) {
        LogUI.step("Checking if checkbox " + componentType + " is enabled by locator");
        return checkboxComponent(componentType).areEnabled(checkBoxLocator);
    }

    @Step("Getting selected checkboxes {componentType}")
    @Override
    public List<String> getSelected(final CheckboxComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting selected checkboxes " + componentType);
        return checkboxComponent(componentType).getSelected(container);
    }

    @Step("Getting selected checkboxes {componentType} by locator")
    @Override
    public List<String> getSelected(final CheckboxComponentType componentType, final By containerLocator) {
        LogUI.step("Getting selected checkboxes " + componentType + " by locator");
        return checkboxComponent(componentType).getSelected(containerLocator);
    }

    @Step("Getting all checkboxes {componentType}")
    @Override
    public List<String> getAll(final CheckboxComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting all checkboxes " + componentType);
        return checkboxComponent(componentType).getAll(container);
    }

    @Step("Getting all checkboxes {componentType} by locator")
    @Override
    public List<String> getAll(final CheckboxComponentType componentType, final By containerLocator) {
        LogUI.step("Getting all checkboxes " + componentType + " by locator");
        return checkboxComponent(componentType).getAll(containerLocator);
    }

    @Step("Inserting value into checkbox {componentType}")
    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        LogUI.step("Inserting value into checkbox " + componentType);
        select((CheckboxComponentType) componentType, (String) values[0]);
    }

    private Checkbox checkboxComponent(CheckboxComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
