package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import io.qameta.allure.Allure;

import java.util.Arrays;
import java.util.List;

public class RadioServiceImpl extends AbstractComponentService<RadioComponentType, Radio> implements RadioService {

    public RadioServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Radio createComponent(final RadioComponentType componentType) {
        return ComponentFactory.getRadioComponent(componentType, driver);
    }

    @Override
    public void select(final RadioComponentType componentType, final SmartWebElement container,
                       final String radioButtonText) {
        Allure.step(String.format("[UI - Radio] Selecting radio button with text %s in container %s for radio component %s", radioButtonText, container, componentType));
        LogUI.step("Selecting radio button with text " + radioButtonText + " in container " + container + " for radio component " + componentType);
        radioComponent(componentType).select(container, radioButtonText);
    }

    @Override
    public String select(final RadioComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        Allure.step(String.format("[UI - Radio] Selecting radio button with strategy %s in container %s for radio component %s", strategy, container, componentType));
        LogUI.step("Selecting radio button with strategy " + strategy + " in container " + container + " for radio component " + componentType);
        return radioComponent(componentType).select(container, strategy);
    }

    @Override
    public void select(final RadioComponentType componentType, final String radioButtonText) {
        Allure.step(String.format("[UI - Radio] Selecting radio button with text %s for radio component %s", radioButtonText, componentType));
        LogUI.step("Selecting radio button with text " + radioButtonText + " for radio component " + componentType);
        radioComponent(componentType).select(radioButtonText);
    }

    @Override
    public void select(final RadioComponentType componentType, final By radioButtonLocator) {
        Allure.step(String.format("[UI - Radio] Selecting radio button with locator %s for radio component %s", radioButtonLocator, componentType));
        LogUI.step("Selecting radio button with locator " + radioButtonLocator + " for radio component " + componentType);
        radioComponent(componentType).select(radioButtonLocator);
    }

    @Override
    public boolean isEnabled(final RadioComponentType componentType, final SmartWebElement container,
                             final String radioButtonText) {
        Allure.step(String.format("[UI - Radio] Checking if radio button with text %s is enabled in container %s for radio component %s", radioButtonText, container, componentType));
        LogUI.step("Checking if radio button with text " + radioButtonText + " is enabled in container " + container + " for radio component " + componentType);
        return radioComponent(componentType).isEnabled(container, radioButtonText);
    }

    @Override
    public boolean isEnabled(final RadioComponentType componentType, final String radioButtonText) {
        Allure.step(String.format("[UI - Radio] Checking if radio button with text %s is enabled for radio component %s", radioButtonText, componentType));
        LogUI.step("Checking if radio button with text " + radioButtonText + " is enabled for radio component " + componentType);
        return radioComponent(componentType).isEnabled(radioButtonText);
    }

    @Override
    public boolean isEnabled(final RadioComponentType componentType, final By radioButtonLocator) {
        Allure.step(String.format("[UI - Radio] Checking if radio button with locator %s is enabled for radio component %s", radioButtonLocator, componentType));
        LogUI.step("Checking if radio button with locator " + radioButtonLocator + " is enabled for radio component " + componentType);
        return radioComponent(componentType).isEnabled(radioButtonLocator);
    }

    @Override
    public boolean isSelected(final RadioComponentType componentType, final SmartWebElement container,
                              final String radioButtonText) {
        Allure.step(String.format("[UI - Radio] Checking if radio button with text %s is selected in container %s for radio component %s", radioButtonText, container, componentType));
        LogUI.step("Checking if radio button with text " + radioButtonText + " is selected in container " + container + " for radio component " + componentType);
        return radioComponent(componentType).isSelected(container, radioButtonText);
    }

    @Override
    public boolean isSelected(final RadioComponentType componentType, final String radioButtonText) {
        Allure.step(String.format("[UI - Radio] Checking if radio button with text %s is selected for radio component %s", radioButtonText, componentType));
        LogUI.step("Checking if radio button with text " + radioButtonText + " is selected for radio component " + componentType);
        return radioComponent(componentType).isSelected(radioButtonText);
    }

    @Override
    public boolean isSelected(final RadioComponentType componentType, final By radioButtonLocator) {
        Allure.step(String.format("[UI - Radio] Checking if radio button with locator %s is selected for radio component %s", radioButtonLocator, componentType));
        LogUI.step("Checking if radio button with locator " + radioButtonLocator + " is selected for radio component " + componentType);
        return radioComponent(componentType).isSelected(radioButtonLocator);
    }

    @Override
    public boolean isVisible(final RadioComponentType componentType, final SmartWebElement container,
                             final String radioButtonText) {
        Allure.step(String.format("[UI - Radio] Checking if radio button with text %s is visible in container %s for radio component %s", radioButtonText, container, componentType));
        LogUI.step("Checking if radio button with text " + radioButtonText + " is visible in container " + container + " for radio component " + componentType);
        return radioComponent(componentType).isVisible(container, radioButtonText);
    }

    @Override
    public boolean isVisible(final RadioComponentType componentType, final String radioButtonText) {
        Allure.step(String.format("[UI - Radio] Checking if radio button with text %s is visible for radio component %s", radioButtonText, componentType));
        LogUI.step("Checking if radio button with text " + radioButtonText + " is visible for radio component " + componentType);
        return radioComponent(componentType).isVisible(radioButtonText);
    }

    @Override
    public boolean isVisible(final RadioComponentType componentType, final By radioButtonLocator) {
        Allure.step(String.format("[UI - Radio] Checking if radio button with locator %s is visible for radio component %s", radioButtonLocator, componentType));
        LogUI.step("Checking if radio button with locator " + radioButtonLocator + " is visible for radio component " + componentType);
        return radioComponent(componentType).isVisible(radioButtonLocator);
    }

    @Override
    public String getSelected(final RadioComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Radio] Getting selected radio button text from container %s for radio component %s", container, componentType));
        LogUI.step("Getting selected radio button text from container " + container + " for radio component " + componentType);
        return radioComponent(componentType).getSelected(container);
    }

    @Override
    public String getSelected(final RadioComponentType componentType, final By containerLocator) {
        Allure.step(String.format("[UI - Radio] Getting selected radio button text from locator %s for radio component %s", containerLocator, componentType));
        LogUI.step("Getting selected radio button text from locator " + containerLocator + " for radio component " + componentType);
        return radioComponent(componentType).getSelected(containerLocator);
    }

    @Override
    public List<String> getAll(final RadioComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Radio] Getting all radio buttons from container %s for radio component %s", container, componentType));
        LogUI.step("Getting all radio buttons from container " + container + " for radio component " + componentType);
        return radioComponent(componentType).getAll(container);
    }

    @Override
    public List<String> getAll(final RadioComponentType componentType, final By containerLocator) {
        Allure.step(String.format("[UI - Radio] Getting all radio buttons from locator %s for radio component %s", containerLocator, componentType));
        LogUI.step("Getting all radio buttons from locator " + containerLocator + " for radio component " + componentType);
        return radioComponent(componentType).getAll(containerLocator);
    }

    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        Allure.step(String.format("[UI - Radio] Inserting values %s for radio component %s using locator %s", Arrays.toString(values), componentType, locator));
        LogUI.step("Inserting values " + Arrays.toString(values) + " for radio component " + componentType + " using locator " + locator);
        select((RadioComponentType) componentType, (String) values[0]);
    }

    private Radio radioComponent(final RadioComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}