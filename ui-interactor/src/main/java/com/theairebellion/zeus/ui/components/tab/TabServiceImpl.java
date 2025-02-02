package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class TabServiceImpl extends AbstractComponentService<TabComponentType, Tab> implements TabService {

    public TabServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Tab createComponent(final TabComponentType componentType) {
        return ComponentFactory.getTabComponent(componentType, driver);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final SmartWebElement container,
                                                      final String tabText) {
        tabComponent((TabComponentType) componentType).click(container, tabText);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final SmartWebElement container) {
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
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container,
                                                             final String tabText) {
        return tabComponent((TabComponentType) componentType).isEnabled(container, tabText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container) {
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
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container,
                                                             final String tabText) {
        return tabComponent((TabComponentType) componentType).isVisible(container, tabText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container) {
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
    public boolean isSelected(final TabComponentType componentType, final SmartWebElement container, final String tabText) {
        return tabComponent(componentType).isSelected(container, tabText);
    }

    @Override
    public boolean isSelected(final TabComponentType componentType, final SmartWebElement container) {
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
        return getOrCreateComponent(componentType);
    }
}
