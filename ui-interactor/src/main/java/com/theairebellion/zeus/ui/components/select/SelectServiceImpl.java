package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

public class SelectServiceImpl implements SelectService {

    protected SmartSelenium smartSelenium;
    private static Map<SelectComponentType, Select> components;

    public SelectServiceImpl(WebDriver driver) {
        this.smartSelenium = new SmartSelenium(driver);
        components = new HashMap<>();
    }

    public SelectServiceImpl(SmartSelenium smartSelenium) {
        this.smartSelenium = smartSelenium;
        components = new HashMap<>();
    }

    @Override
    public void selectItems(final SelectComponentType componentType, final DdlMode mode, final WebElement container,
                            final String... values) {
        selectComponent(componentType).selectItems(mode, container, values);
    }

    @Override
    public void selectItems(final SelectComponentType componentType, final DdlMode mode, final By containerLocator,
                            final String... values) {
        selectComponent(componentType).selectItems(mode, containerLocator, values);
    }

    @Override
    public List<String> selectItems(final SelectComponentType componentType, final DdlMode mode,
                                    final WebElement container, final Strategy strategy) {
        return selectComponent(componentType).selectItems(mode, container, strategy);
    }

    @Override
    public List<String> selectItems(final SelectComponentType componentType, final DdlMode mode,
                                    final By containerLocator, final Strategy strategy) {
        return selectComponent(componentType).selectItems(mode, containerLocator, strategy);
    }

    @Override
    public List<String> getAvailableItems(final SelectComponentType componentType, final WebElement container) {
        return selectComponent(componentType).getAvailableItems(container);
    }

    @Override
    public List<String> getAvailableItems(final SelectComponentType componentType, final By containerLocator) {
        return selectComponent(componentType).getAvailableItems(containerLocator);
    }

    @Override
    public List<String> getAvailableItems(final SelectComponentType componentType, final WebElement container,
                                          final String search) {
        return selectComponent(componentType).getAvailableItems(container, search);
    }

    @Override
    public List<String> getAvailableItems(final SelectComponentType componentType, final By containerLocator,
                                          final String search) {
        return selectComponent(componentType).getAvailableItems(containerLocator, search);
    }

    @Override
    public List<String> getSelectedItems(final SelectComponentType componentType, final WebElement container) {
        return selectComponent(componentType).getSelectedItems(container);
    }

    @Override
    public List<String> getSelectedItems(final SelectComponentType componentType, final By containerLocator) {
        return selectComponent(componentType).getSelectedItems(containerLocator);
    }

    @Override
    public boolean isOptionPresent(final SelectComponentType componentType, final WebElement container,
                                   final String value) {
        return selectComponent(componentType).isOptionPresent(container, value);
    }

    @Override
    public boolean isOptionPresent(final SelectComponentType componentType, final By containerLocator,
                                   final String value) {
        return selectComponent(componentType).isOptionPresent(containerLocator, value);
    }

    @Override
    public boolean isOptionEnabled(final SelectComponentType componentType, final WebElement container,
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
        selectItems((SelectComponentType) componentType, DdlMode.MULTI_SELECT_WITH_CHOOSING, locator, stringValues);
    }

    private Select selectComponent(final SelectComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getSelectComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}