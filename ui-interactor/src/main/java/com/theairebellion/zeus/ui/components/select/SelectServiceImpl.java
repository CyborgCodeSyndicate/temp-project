package com.theairebellion.zeus.ui.components.select;

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

public class SelectServiceImpl extends AbstractComponentService<SelectComponentType, Select> implements SelectService {

    public SelectServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Select createComponent(final SelectComponentType componentType) {
        return ComponentFactory.getSelectComponent(componentType, driver);
    }

    @Override
    public void selectOptions(final SelectComponentType componentType, final SmartWebElement container,
                              final String... values) {
        Allure.step(String.format("[UI - Select] Selecting options %s in container %s for select component %s", Arrays.toString(values), container, componentType));
        LogUI.step("Selecting options " + Arrays.toString(values) + " in container " + container + " for select component " + componentType);
        selectComponent(componentType).selectOptions(container, values);
    }

    @Override
    public void selectOption(SelectComponentType componentType, SmartWebElement container, String value) {
        Allure.step(String.format("[UI - Select] Selecting option %s in container %s for select component %s", value, container, componentType));
        LogUI.step("Selecting option " + value + " in container " + container + " for select component " + componentType);
        selectOptions(componentType, container, value);
    }

    @Override
    public void selectOptions(final SelectComponentType componentType, final By containerLocator,
                              final String... values) {
        Allure.step(String.format("[UI - Select] Selecting options %s in container with locator %s for select component %s", Arrays.toString(values), containerLocator, componentType));
        LogUI.step("Selecting options " + Arrays.toString(values) + " in container with locator " + containerLocator + " for select component " + componentType);
        selectComponent(componentType).selectOptions(containerLocator, values);
    }

    @Override
    public void selectOption(final SelectComponentType componentType, final By containerLocator,
                             final String value) {
        Allure.step(String.format("[UI - Select] Selecting option %s in container with locator %s for select component %s", value, containerLocator, componentType));
        LogUI.step("Selecting option " + value + " in container with locator " + containerLocator + " for select component " + componentType);
        selectOptions(componentType, containerLocator, value);
    }

    @Override
    public List<String> selectOptions(final SelectComponentType componentType, final SmartWebElement container,
                                      final Strategy strategy) {
        Allure.step(String.format("[UI - Select] Selecting options with strategy %s in container %s for select component %s", strategy, container, componentType));
        LogUI.step("Selecting options with strategy " + strategy + " in container " + container + " for select component " + componentType);
        return selectComponent(componentType).selectOptions(container, strategy);
    }

    @Override
    public List<String> selectOptions(final SelectComponentType componentType, final By containerLocator,
                                      final Strategy strategy) {
        Allure.step(String.format("[UI - Select] Selecting options with strategy %s in container with locator %s for select component %s", strategy, containerLocator, componentType));
        LogUI.step("Selecting options with strategy " + strategy + " in container with locator " + containerLocator + " for select component " + componentType);
        return selectComponent(componentType).selectOptions(containerLocator, strategy);
    }

    @Override
    public List<String> getAvailableOptions(final SelectComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Select] Getting available options in container %s for select component %s", container, componentType));
        LogUI.step("Getting available options in container " + container + " for select component " + componentType);
        return selectComponent(componentType).getAvailableOptions(container);
    }

    @Override
    public List<String> getAvailableOptions(final SelectComponentType componentType, final By containerLocator) {
        Allure.step(String.format("[UI - Select] Getting available options in container with locator %s for select component %s", containerLocator, componentType));
        LogUI.step("Getting available options in container with locator " + containerLocator + " for select component " + componentType);
        return selectComponent(componentType).getAvailableOptions(containerLocator);
    }

    @Override
    public List<String> getSelectedOptions(final SelectComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Select] Getting selected options in container %s for select component %s", container, componentType));
        LogUI.step("Getting selected options in container " + container + " for select component " + componentType);
        return selectComponent(componentType).getSelectedOptions(container);
    }

    @Override
    public List<String> getSelectedOptions(final SelectComponentType componentType, final By containerLocator) {
        Allure.step(String.format("[UI - Select] Getting selected options in container with locator %s for select component %s", containerLocator, componentType));
        LogUI.step("Getting selected options in container with locator " + containerLocator + " for select component " + componentType);
        return selectComponent(componentType).getSelectedOptions(containerLocator);
    }

    @Override
    public boolean isOptionVisible(final SelectComponentType componentType, final SmartWebElement container,
                                   final String value) {
        Allure.step(String.format("[UI - Select] Checking if option %s is visible in container %s for select component %s", value, container, componentType));
        LogUI.step("Checking if option " + value + " is visible in container " + container + " for select component " + componentType);
        return selectComponent(componentType).isOptionVisible(container, value);
    }

    @Override
    public boolean isOptionVisible(final SelectComponentType componentType, final By containerLocator,
                                   final String value) {
        Allure.step(String.format("[UI - Select] Checking if option %s is visible in container with locator %s for select component %s", value, containerLocator, componentType));
        LogUI.step("Checking if option " + value + " is visible in container with locator " + containerLocator + " for select component " + componentType);
        return selectComponent(componentType).isOptionVisible(containerLocator, value);
    }

    @Override
    public boolean isOptionEnabled(final SelectComponentType componentType, final SmartWebElement container,
                                   final String value) {
        Allure.step(String.format("[UI - Select] Checking if option %s is enabled in container %s for select component %s", value, container, componentType));
        LogUI.step("Checking if option " + value + " is enabled in container " + container + " for select component " + componentType);
        return selectComponent(componentType).isOptionEnabled(container, value);
    }

    @Override
    public boolean isOptionEnabled(final SelectComponentType componentType, final By containerLocator,
                                   final String value) {
        Allure.step(String.format("[UI - Select] Checking if option %s is enabled in container with locator %s for select component %s", value, containerLocator, componentType));
        LogUI.step("Checking if option " + value + " is enabled in container with locator " + containerLocator + " for select component " + componentType);
        return selectComponent(componentType).isOptionEnabled(containerLocator, value);
    }

    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        Allure.step(String.format("[UI - Select] Inserting values %s for select component %s using locator %s", Arrays.toString(values), componentType, locator));
        LogUI.step("Inserting values " + Arrays.toString(values) + " for select component " + componentType + " using locator " + locator);
        String[] stringValues = Arrays.stream(values)
                .map(String::valueOf)
                .toArray(String[]::new);
        selectOptions((SelectComponentType) componentType, locator, stringValues);
    }

    private Select selectComponent(final SelectComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}