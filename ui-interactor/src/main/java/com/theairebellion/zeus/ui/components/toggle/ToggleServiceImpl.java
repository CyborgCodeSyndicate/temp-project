package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import io.qameta.allure.Allure;

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

    @Override
    public void activate(final ToggleComponentType componentType, final SmartWebElement container, final String toggleText) {
        Allure.step(String.format("[UI - Toggle] Activating toggle %s in container %s for toggle component %s", toggleText, container, componentType));
        LogUI.step("Activating toggle " + toggleText + " in container " + container + " for toggle component " + componentType);
        toggleComponent(componentType).activate(container, toggleText);
    }

    @Override
    public void activate(final ToggleComponentType componentType, final String toggleText) {
        Allure.step(String.format("[UI - Toggle] Activating toggle %s for toggle component %s", toggleText, componentType));
        LogUI.step("Activating toggle " + toggleText + " for toggle component " + componentType);
        toggleComponent(componentType).activate(toggleText);
    }

    @Override
    public void activate(final ToggleComponentType componentType, final By toggleLocator) {
        Allure.step(String.format("[UI - Toggle] Activating toggle with locator %s for toggle component %s", toggleLocator, componentType));
        LogUI.step("Activating toggle with locator " + toggleLocator + " for toggle component " + componentType);
        toggleComponent(componentType).activate(toggleLocator);
    }

    @Override
    public void deactivate(final ToggleComponentType componentType, final SmartWebElement container, final String toggleText) {
        Allure.step(String.format("[UI - Toggle] Deactivating toggle %s in container %s for toggle component %s", toggleText, container, componentType));
        LogUI.step("Deactivating toggle " + toggleText + " in container " + container + " for toggle component " + componentType);
        toggleComponent(componentType).deactivate(container, toggleText);
    }

    @Override
    public void deactivate(final ToggleComponentType componentType, final String toggleText) {
        Allure.step(String.format("[UI - Toggle] Deactivating toggle %s for toggle component %s", toggleText, componentType));
        LogUI.step("Deactivating toggle " + toggleText + " for toggle component " + componentType);
        toggleComponent(componentType).deactivate(toggleText);
    }

    @Override
    public void deactivate(final ToggleComponentType componentType, final By toggleLocator) {
        Allure.step(String.format("[UI - Toggle] Deactivating toggle with locator %s for toggle component %s", toggleLocator, componentType));
        LogUI.step("Deactivating toggle with locator " + toggleLocator + " for toggle component " + componentType);
        toggleComponent(componentType).deactivate(toggleLocator);
    }

    @Override
    public boolean isEnabled(final ToggleComponentType componentType, final SmartWebElement container, final String toggleText) {
        Allure.step(String.format("[UI - Toggle] Checking if toggle %s is enabled in container %s for toggle component %s", toggleText, container, componentType));
        LogUI.step("Checking if toggle " + toggleText + " is enabled in container " + container + " for toggle component " + componentType);
        return toggleComponent(componentType).isEnabled(container, toggleText);
    }

    @Override
    public boolean isEnabled(final ToggleComponentType componentType, final String toggleText) {
        Allure.step(String.format("[UI - Toggle] Checking if toggle %s is enabled for toggle component %s", toggleText, componentType));
        LogUI.step("Checking if toggle " + toggleText + " is enabled for toggle component " + componentType);
        return toggleComponent(componentType).isEnabled(toggleText);
    }

    @Override
    public boolean isEnabled(final ToggleComponentType componentType, final By toggleLocator) {
        Allure.step(String.format("[UI - Toggle] Checking if toggle with locator %s is enabled for toggle component %s", toggleLocator, componentType));
        LogUI.step("Checking if toggle with locator " + toggleLocator + " is enabled for toggle component " + componentType);
        return toggleComponent(componentType).isEnabled(toggleLocator);
    }

    @Override
    public boolean isActivated(final ToggleComponentType componentType, final SmartWebElement container, final String toggleText) {
        Allure.step(String.format("[UI - Toggle] Checking if toggle %s is activated in container %s for toggle component %s", toggleText, container, componentType));
        LogUI.step("Checking if toggle " + toggleText + " is activated in container " + container + " for toggle component " + componentType);
        return toggleComponent(componentType).isActivated(container, toggleText);
    }

    @Override
    public boolean isActivated(final ToggleComponentType componentType, final String toggleText) {
        Allure.step(String.format("[UI - Toggle] Checking if toggle %s is activated for toggle component %s", toggleText, componentType));
        LogUI.step("Checking if toggle " + toggleText + " is activated for toggle component " + componentType);
        return toggleComponent(componentType).isActivated(toggleText);
    }

    @Override
    public boolean isActivated(final ToggleComponentType componentType, final By toggleLocator) {
        Allure.step(String.format("[UI - Toggle] Checking if toggle with locator %s is activated for toggle component %s", toggleLocator, componentType));
        LogUI.step("Checking if toggle with locator " + toggleLocator + " is activated for toggle component " + componentType);
        return toggleComponent(componentType).isActivated(toggleLocator);
    }

    private Toggle toggleComponent(final ToggleComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}