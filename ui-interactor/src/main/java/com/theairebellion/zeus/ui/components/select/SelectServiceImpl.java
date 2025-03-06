package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import io.qameta.allure.Step;

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

    @Step("Selecting options {values} in container {container} for select component {componentType}")
    @Override
    public void selectOptions(final SelectComponentType componentType, final SmartWebElement container,
                              final String... values) {
        LogUI.step("Selecting options " + Arrays.toString(values) + " in container " + container + " for select component " + componentType);
        selectComponent(componentType).selectOptions(container, values);
    }

    @Step("Selecting option {value} in container {container} for select component {componentType}")
    @Override
    public void selectOption(SelectComponentType componentType, SmartWebElement container, String value) {
        LogUI.step("Selecting option " + value + " in container " + container + " for select component " + componentType);
        selectOptions(componentType, container, value);
    }

    @Step("Selecting options {values} in container with locator {containerLocator} for select component {componentType}")
    @Override
    public void selectOptions(final SelectComponentType componentType, final By containerLocator,
                              final String... values) {
        LogUI.step("Selecting options " + Arrays.toString(values) + " in container with locator " + containerLocator + " for select component " + componentType);
        selectComponent(componentType).selectOptions(containerLocator, values);
    }

    @Step("Selecting option {value} in container with locator {containerLocator} for select component {componentType}")
    @Override
    public void selectOption(final SelectComponentType componentType, final By containerLocator,
                             final String value) {
        LogUI.step("Selecting option " + value + " in container with locator " + containerLocator + " for select component " + componentType);
        selectOptions(componentType, containerLocator, value);
    }

    @Step("Selecting options with strategy {strategy} in container {container} for select component {componentType}")
    @Override
    public List<String> selectOptions(final SelectComponentType componentType, final SmartWebElement container,
                                      final Strategy strategy) {
        LogUI.step("Selecting options with strategy " + strategy + " in container " + container + " for select component " + componentType);
        return selectComponent(componentType).selectOptions(container, strategy);
    }

    @Step("Selecting options with strategy {strategy} in container with locator {containerLocator} for select component {componentType}")
    @Override
    public List<String> selectOptions(final SelectComponentType componentType, final By containerLocator,
                                      final Strategy strategy) {
        LogUI.step("Selecting options with strategy " + strategy + " in container with locator " + containerLocator + " for select component " + componentType);
        return selectComponent(componentType).selectOptions(containerLocator, strategy);
    }

    @Step("Getting available options in container {container} for select component {componentType}")
    @Override
    public List<String> getAvailableOptions(final SelectComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting available options in container " + container + " for select component " + componentType);
        return selectComponent(componentType).getAvailableOptions(container);
    }

    @Step("Getting available options in container with locator {containerLocator} for select component {componentType}")
    @Override
    public List<String> getAvailableOptions(final SelectComponentType componentType, final By containerLocator) {
        LogUI.step("Getting available options in container with locator " + containerLocator + " for select component " + componentType);
        return selectComponent(componentType).getAvailableOptions(containerLocator);
    }

    @Step("Getting selected options in container {container} for select component {componentType}")
    @Override
    public List<String> getSelectedOptions(final SelectComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting selected options in container " + container + " for select component " + componentType);
        return selectComponent(componentType).getSelectedOptions(container);
    }

    @Step("Getting selected options in container with locator {containerLocator} for select component {componentType}")
    @Override
    public List<String> getSelectedOptions(final SelectComponentType componentType, final By containerLocator) {
        LogUI.step("Getting selected options in container with locator " + containerLocator + " for select component " + componentType);
        return selectComponent(componentType).getSelectedOptions(containerLocator);
    }

    @Step("Checking if option {value} is visible in container {container} for select component {componentType}")
    @Override
    public boolean isOptionVisible(final SelectComponentType componentType, final SmartWebElement container,
                                   final String value) {
        LogUI.step("Checking if option " + value + " is visible in container " + container + " for select component " + componentType);
        return selectComponent(componentType).isOptionVisible(container, value);
    }

    @Step("Checking if option {value} is visible in container with locator {containerLocator} for select component {componentType}")
    @Override
    public boolean isOptionVisible(final SelectComponentType componentType, final By containerLocator,
                                   final String value) {
        LogUI.step("Checking if option " + value + " is visible in container with locator " + containerLocator + " for select component " + componentType);
        return selectComponent(componentType).isOptionVisible(containerLocator, value);
    }

    @Step("Checking if option {value} is enabled in container {container} for select component {componentType}")
    @Override
    public boolean isOptionEnabled(final SelectComponentType componentType, final SmartWebElement container,
                                   final String value) {
        LogUI.step("Checking if option " + value + " is enabled in container " + container + " for select component " + componentType);
        return selectComponent(componentType).isOptionEnabled(container, value);
    }

    @Step("Checking if option {value} is enabled in container with locator {containerLocator} for select component {componentType}")
    @Override
    public boolean isOptionEnabled(final SelectComponentType componentType, final By containerLocator,
                                   final String value) {
        LogUI.step("Checking if option " + value + " is enabled in container with locator " + containerLocator + " for select component " + componentType);
        return selectComponent(componentType).isOptionEnabled(containerLocator, value);
    }

    @Step("Inserting values {values} for select component {componentType} using locator {locator}")
    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
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
