package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TabServiceImpl implements TabService {

    protected SmartSelenium smartSelenium;
    private static Map<TabComponentType, Tab> components;

    public TabServiceImpl(WebDriver driver) {
        this.smartSelenium = new SmartSelenium(driver);
        components = new HashMap<>();
    }

    public TabServiceImpl(SmartSelenium smartSelenium) {
        this.smartSelenium = smartSelenium;
        components = new HashMap<>();
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final WebElement container,
                                                      final String tabText) {
        tabComponent((TabComponentType) componentType).click(container, tabText);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final WebElement container) {
        tabComponent((TabComponentType) componentType).click(container);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final String tabText) {
        tabComponent((TabComponentType) componentType).click(tabText);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final By tabLocator) {
        tabComponent((TabComponentType) componentType).click(tabLocator);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final WebElement container,
                                                             final String tabText) {
        return tabComponent((TabComponentType) componentType).isEnabled(container, tabText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final WebElement container) {
        return tabComponent((TabComponentType) componentType).isEnabled(container);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final String tabText) {
        return tabComponent((TabComponentType) componentType).isEnabled(tabText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final By tabLocator) {
        return tabComponent((TabComponentType) componentType).isEnabled(tabLocator);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final WebElement container,
                                                             final String tabText) {
        return tabComponent((TabComponentType) componentType).isVisible(container, tabText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final WebElement container) {
        return tabComponent((TabComponentType) componentType).isVisible(container);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final String tabText) {
        return tabComponent((TabComponentType) componentType).isVisible(tabText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final By tabLocator) {
        return tabComponent((TabComponentType) componentType).isVisible(tabLocator);
    }

    @Override
    public boolean isSelected(final TabComponentType componentType, final WebElement container, final String tabText) {
        return tabComponent(componentType).isSelected(container, tabText);
    }

    @Override
    public boolean isSelected(final TabComponentType componentType, final WebElement container) {
        return tabComponent(componentType).isSelected(container);
    }

    @Override
    public boolean isSelected(final TabComponentType componentType, final String tabText) {
        return tabComponent(componentType).isSelected(tabText);
    }

    @Override
    public boolean isSelected(final TabComponentType componentType, final By tabLocator) {
        return tabComponent(componentType).isSelected(tabLocator);
    }

    private Tab tabComponent(final TabComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getTabComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}
