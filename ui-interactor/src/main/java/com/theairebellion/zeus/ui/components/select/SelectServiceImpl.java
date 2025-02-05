package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

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
        selectComponent(componentType).selectOptions(container, values);
    }

    @Override
    public void selectOption(SelectComponentType componentType, SmartWebElement container, String value) {
        selectOptions(componentType, container, value);
    }

    @Override
    public void selectOptions(final SelectComponentType componentType, final By containerLocator,
                              final String... values) {
        selectComponent(componentType).selectOptions(containerLocator, values);
    }

    @Override
    public void selectOption(final SelectComponentType componentType, final By containerLocator,
                             final String value) {
        selectOptions(componentType, containerLocator, value);
    }

    @Override
    public List<String> selectOptions(final SelectComponentType componentType, final SmartWebElement container,
                                      final Strategy strategy) {
        return selectComponent(componentType).selectOptions(container, strategy);
    }

    @Override
    public List<String> selectOptions(final SelectComponentType componentType, final By containerLocator,
                                      final Strategy strategy) {
        return selectComponent(componentType).selectOptions(containerLocator, strategy);
    }

    @Override
    public List<String> getAvailableOptions(final SelectComponentType componentType, final SmartWebElement container) {
        return selectComponent(componentType).getAvailableOptions(container);
    }

    @Override
    public List<String> getAvailableOptions(final SelectComponentType componentType, final By containerLocator) {
        return selectComponent(componentType).getAvailableOptions(containerLocator);
    }

    @Override
    public List<String> getSelectedOptions(final SelectComponentType componentType, final SmartWebElement container) {
        return selectComponent(componentType).getSelectedOptions(container);
    }

    @Override
    public List<String> getSelectedOptions(final SelectComponentType componentType, final By containerLocator) {
        return selectComponent(componentType).getSelectedOptions(containerLocator);
    }

    @Override
    public boolean isOptionVisible(final SelectComponentType componentType, final SmartWebElement container,
                                   final String value) {
        return selectComponent(componentType).isOptionVisible(container, value);
    }

    @Override
    public boolean isOptionVisible(final SelectComponentType componentType, final By containerLocator,
                                   final String value) {
        return selectComponent(componentType).isOptionVisible(containerLocator, value);
    }

    @Override
    public boolean isOptionEnabled(final SelectComponentType componentType, final SmartWebElement container,
                                   final String value) {
        return selectComponent(componentType).isOptionEnabled(container, value);
    }

    @Override
    public boolean isOptionEnabled(final SelectComponentType componentType, final By containerLocator,
                                   final String value) {
        return selectComponent(componentType).isOptionEnabled(containerLocator, value);
    }

    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        String[] stringValues = Arrays.stream(values)
                .map(String::valueOf)
                .toArray(String[]::new);
        selectOptions((SelectComponentType) componentType, locator, stringValues);
    }

    private Select selectComponent(final SelectComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}