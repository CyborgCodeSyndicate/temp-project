package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import io.qameta.allure.Step;

import java.util.Map;

public class ToggleServiceImpl extends AbstractComponentService<ToggleComponentType, Toggle> implements ToggleService {

    private static Map<ToggleComponentType, Toggle> components;

    public ToggleServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Toggle createComponent(ToggleComponentType componentType) {
        return ComponentFactory.getToggleComponent(componentType, driver);
    }

    @Step("Activating toggle {toggleText} in container {container} for toggle component {componentType}")
    @Override
    public void activate(final ToggleComponentType componentType, final SmartWebElement container, final String toggleText) {
        LogUI.step("Activating toggle " + toggleText + " in container " + container + " for toggle component " + componentType);
        toggleComponent(componentType).activate(container, toggleText);
    }

    @Step("Activating toggle {toggleText} for toggle component {componentType}")
    @Override
    public void activate(final ToggleComponentType componentType, final String toggleText) {
        LogUI.step("Activating toggle " + toggleText + " for toggle component " + componentType);
        toggleComponent(componentType).activate(toggleText);
    }

    @Step("Activating toggle with locator {toggleLocator} for toggle component {componentType}")
    @Override
    public void activate(final ToggleComponentType componentType, final By toggleLocator) {
        LogUI.step("Activating toggle with locator " + toggleLocator + " for toggle component " + componentType);
        toggleComponent(componentType).activate(toggleLocator);
    }

    @Step("Deactivating toggle {toggleText} in container {container} for toggle component {componentType}")
    @Override
    public void deactivate(final ToggleComponentType componentType, final SmartWebElement container, final String toggleText) {
        LogUI.step("Deactivating toggle " + toggleText + " in container " + container + " for toggle component " + componentType);
        toggleComponent(componentType).deactivate(container, toggleText);
    }

    @Step("Deactivating toggle {toggleText} for toggle component {componentType}")
    @Override
    public void deactivate(final ToggleComponentType componentType, final String toggleText) {
        LogUI.step("Deactivating toggle " + toggleText + " for toggle component " + componentType);
        toggleComponent(componentType).deactivate(toggleText);
    }

    @Step("Deactivating toggle with locator {toggleLocator} for toggle component {componentType}")
    @Override
    public void deactivate(final ToggleComponentType componentType, final By toggleLocator) {
        LogUI.step("Deactivating toggle with locator " + toggleLocator + " for toggle component " + componentType);
        toggleComponent(componentType).deactivate(toggleLocator);
    }

    @Step("Checking if toggle {toggleText} is enabled in container {container} for toggle component {componentType}")
    @Override
    public boolean isEnabled(final ToggleComponentType componentType, final SmartWebElement container, final String toggleText) {
        LogUI.step("Checking if toggle " + toggleText + " is enabled in container " + container + " for toggle component " + componentType);
        return toggleComponent(componentType).isEnabled(container, toggleText);
    }

    @Step("Checking if toggle {toggleText} is enabled for toggle component {componentType}")
    @Override
    public boolean isEnabled(final ToggleComponentType componentType, final String toggleText) {
        LogUI.step("Checking if toggle " + toggleText + " is enabled for toggle component " + componentType);
        return toggleComponent(componentType).isEnabled(toggleText);
    }

    @Step("Checking if toggle with locator {toggleLocator} is enabled for toggle component {componentType}")
    @Override
    public boolean isEnabled(final ToggleComponentType componentType, final By toggleLocator) {
        LogUI.step("Checking if toggle with locator " + toggleLocator + " is enabled for toggle component " + componentType);
        return toggleComponent(componentType).isEnabled(toggleLocator);
    }

    @Step("Checking if toggle {toggleText} is activated in container {container} for toggle component {componentType}")
    @Override
    public boolean isActivated(final ToggleComponentType componentType, final SmartWebElement container, final String toggleText) {
        LogUI.step("Checking if toggle " + toggleText + " is activated in container " + container + " for toggle component " + componentType);
        return toggleComponent(componentType).isActivated(container, toggleText);
    }

    @Step("Checking if toggle {toggleText} is activated for toggle component {componentType}")
    @Override
    public boolean isActivated(final ToggleComponentType componentType, final String toggleText) {
        LogUI.step("Checking if toggle " + toggleText + " is activated for toggle component " + componentType);
        return toggleComponent(componentType).isActivated(toggleText);
    }

    @Step("Checking if toggle with locator {toggleLocator} is activated for toggle component {componentType}")
    @Override
    public boolean isActivated(final ToggleComponentType componentType, final By toggleLocator) {
        LogUI.step("Checking if toggle with locator " + toggleLocator + " is activated for toggle component " + componentType);
        return toggleComponent(componentType).isActivated(toggleLocator);
    }

    private Toggle toggleComponent(final ToggleComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
