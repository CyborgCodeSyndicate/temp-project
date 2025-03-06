package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import io.qameta.allure.Step;

import java.util.List;

public class RadioServiceImpl extends AbstractComponentService<RadioComponentType, Radio> implements RadioService {

    public RadioServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Radio createComponent(final RadioComponentType componentType) {
        return ComponentFactory.getRadioComponent(componentType, driver);
    }

    @Step("Selecting radio button with text {radioButtonText} in container {container} for radio component {componentType}")
    @Override
    public void select(final RadioComponentType componentType, final SmartWebElement container,
                       final String radioButtonText) {
        LogUI.step("Selecting radio button with text " + radioButtonText + " in container " + container + " for radio component " + componentType);
        radioComponent(componentType).select(container, radioButtonText);
    }

    @Step("Selecting radio button with strategy {strategy} in container {container} for radio component {componentType}")
    @Override
    public String select(final RadioComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        LogUI.step("Selecting radio button with strategy " + strategy + " in container " + container + " for radio component " + componentType);
        return radioComponent(componentType).select(container, strategy);
    }

    @Step("Selecting radio button with text {radioButtonText} for radio component {componentType}")
    @Override
    public void select(final RadioComponentType componentType, final String radioButtonText) {
        LogUI.step("Selecting radio button with text " + radioButtonText + " for radio component " + componentType);
        radioComponent(componentType).select(radioButtonText);
    }

    @Step("Selecting radio button with locator {radioButtonLocator} for radio component {componentType}")
    @Override
    public void select(final RadioComponentType componentType, final By radioButtonLocator) {
        LogUI.step("Selecting radio button with locator " + radioButtonLocator + " for radio component " + componentType);
        radioComponent(componentType).select(radioButtonLocator);
    }

    @Step("Checking if radio button with text {radioButtonText} is enabled in container {container} for radio component {componentType}")
    @Override
    public boolean isEnabled(final RadioComponentType componentType, final SmartWebElement container,
                             final String radioButtonText) {
        LogUI.step("Checking if radio button with text " + radioButtonText + " is enabled in container " + container + " for radio component " + componentType);
        return radioComponent(componentType).isEnabled(container, radioButtonText);
    }

    @Step("Checking if radio button with text {radioButtonText} is enabled for radio component {componentType}")
    @Override
    public boolean isEnabled(final RadioComponentType componentType, final String radioButtonText) {
        LogUI.step("Checking if radio button with text " + radioButtonText + " is enabled for radio component " + componentType);
        return radioComponent(componentType).isEnabled(radioButtonText);
    }

    @Step("Checking if radio button with locator {radioButtonLocator} is enabled for radio component {componentType}")
    @Override
    public boolean isEnabled(final RadioComponentType componentType, final By radioButtonLocator) {
        LogUI.step("Checking if radio button with locator " + radioButtonLocator + " is enabled for radio component " + componentType);
        return radioComponent(componentType).isEnabled(radioButtonLocator);
    }

    @Step("Checking if radio button with text {radioButtonText} is selected in container {container} for radio component {componentType}")
    @Override
    public boolean isSelected(final RadioComponentType componentType, final SmartWebElement container,
                              final String radioButtonText) {
        LogUI.step("Checking if radio button with text " + radioButtonText + " is selected in container " + container + " for radio component " + componentType);
        return radioComponent(componentType).isSelected(container, radioButtonText);
    }

    @Step("Checking if radio button with text {radioButtonText} is selected for radio component {componentType}")
    @Override
    public boolean isSelected(final RadioComponentType componentType, final String radioButtonText) {
        LogUI.step("Checking if radio button with text " + radioButtonText + " is selected for radio component " + componentType);
        return radioComponent(componentType).isSelected(radioButtonText);
    }

    @Step("Checking if radio button with locator {radioButtonLocator} is selected for radio component {componentType}")
    @Override
    public boolean isSelected(final RadioComponentType componentType, final By radioButtonLocator) {
        LogUI.step("Checking if radio button with locator " + radioButtonLocator + " is selected for radio component " + componentType);
        return radioComponent(componentType).isSelected(radioButtonLocator);
    }

    @Step("Checking if radio button with text {radioButtonText} is visible in container {container} for radio component {componentType}")
    @Override
    public boolean isVisible(final RadioComponentType componentType, final SmartWebElement container,
                             final String radioButtonText) {
        LogUI.step("Checking if radio button with text " + radioButtonText + " is visible in container " + container + " for radio component " + componentType);
        return radioComponent(componentType).isVisible(container, radioButtonText);
    }

    @Step("Checking if radio button with text {radioButtonText} is visible for radio component {componentType}")
    @Override
    public boolean isVisible(final RadioComponentType componentType, final String radioButtonText) {
        LogUI.step("Checking if radio button with text " + radioButtonText + " is visible for radio component " + componentType);
        return radioComponent(componentType).isVisible(radioButtonText);
    }

    @Step("Checking if radio button with locator {radioButtonLocator} is visible for radio component {componentType}")
    @Override
    public boolean isVisible(final RadioComponentType componentType, final By radioButtonLocator) {
        LogUI.step("Checking if radio button with locator " + radioButtonLocator + " is visible for radio component " + componentType);
        return radioComponent(componentType).isVisible(radioButtonLocator);
    }

    @Step("Getting selected radio button text from container {container} for radio component {componentType}")
    @Override
    public String getSelected(final RadioComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting selected radio button text from container " + container + " for radio component " + componentType);
        return radioComponent(componentType).getSelected(container);
    }

    @Step("Getting selected radio button text from locator {containerLocator} for radio component {componentType}")
    @Override
    public String getSelected(final RadioComponentType componentType, final By containerLocator) {
        LogUI.step("Getting selected radio button text from locator " + containerLocator + " for radio component " + componentType);
        return radioComponent(componentType).getSelected(containerLocator);
    }

    @Step("Getting all radio buttons from container {container} for radio component {componentType}")
    @Override
    public List<String> getAll(final RadioComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting all radio buttons from container " + container + " for radio component " + componentType);
        return radioComponent(componentType).getAll(container);
    }

    @Step("Getting all radio buttons from locator {containerLocator} for radio component {componentType}")
    @Override
    public List<String> getAll(final RadioComponentType componentType, final By containerLocator) {
        LogUI.step("Getting all radio buttons from locator " + containerLocator + " for radio component " + componentType);
        return radioComponent(componentType).getAll(containerLocator);
    }

    @Step("Inserting values {values} for radio component {componentType} using locator {locator}")
    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        LogUI.step("Inserting values " + values + " for radio component " + componentType + " using locator " + locator);
        select((RadioComponentType) componentType, (String) values[0]);
    }

    private Radio radioComponent(final RadioComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
