package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import io.qameta.allure.Step;

public class TabServiceImpl extends AbstractComponentService<TabComponentType, Tab> implements TabService {

    public TabServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Tab createComponent(final TabComponentType componentType) {
        return ComponentFactory.getTabComponent(componentType, driver);
    }

    @Step("Clicking tab {tabText} in container {container} for tab component {componentType}")
    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final SmartWebElement container,
                                                      final String tabText) {
        LogUI.step("Clicking tab " + tabText + " in container " + container + " for tab component " + componentType);
        tabComponent((TabComponentType) componentType).click(container, tabText);
    }

    @Step("Clicking tab in container {container} for tab component {componentType}")
    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final SmartWebElement container) {
        LogUI.step("Clicking tab in container " + container + " for tab component " + componentType);
        tabComponent((TabComponentType) componentType).click(container);
    }

    @Step("Clicking tab {tabText} for tab component {componentType}")
    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final String tabText) {
        LogUI.step("Clicking tab " + tabText + " for tab component " + componentType);
        tabComponent((TabComponentType) componentType).click(tabText);
    }

    @Step("Clicking tab with locator {tabLocator} for tab component {componentType}")
    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final By tabLocator) {
        LogUI.step("Clicking tab with locator " + tabLocator + " for tab component " + componentType);
        tabComponent((TabComponentType) componentType).click(tabLocator);
    }

    @Step("Checking if tab {tabText} is enabled in container {container} for tab component {componentType}")
    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container,
                                                             final String tabText) {
        LogUI.step("Checking if tab " + tabText + " is enabled in container " + container + " for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isEnabled(container, tabText);
    }

    @Step("Checking if tab is enabled in container {container} for tab component {componentType}")
    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container) {
        LogUI.step("Checking if tab is enabled in container " + container + " for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isEnabled(container);
    }

    @Step("Checking if tab {tabText} is enabled for tab component {componentType}")
    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final String tabText) {
        LogUI.step("Checking if tab " + tabText + " is enabled for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isEnabled(tabText);
    }

    @Step("Checking if tab with locator {tabLocator} is enabled for tab component {componentType}")
    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final By tabLocator) {
        LogUI.step("Checking if tab with locator " + tabLocator + " is enabled for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isEnabled(tabLocator);
    }

    @Step("Checking if tab {tabText} is visible in container {container} for tab component {componentType}")
    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container,
                                                             final String tabText) {
        LogUI.step("Checking if tab " + tabText + " is visible in container " + container + " for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isVisible(container, tabText);
    }

    @Step("Checking if tab is visible in container {container} for tab component {componentType}")
    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container) {
        LogUI.step("Checking if tab is visible in container " + container + " for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isVisible(container);
    }

    @Step("Checking if tab {tabText} is visible for tab component {componentType}")
    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final String tabText) {
        LogUI.step("Checking if tab " + tabText + " is visible for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isVisible(tabText);
    }

    @Step("Checking if tab with locator {tabLocator} is visible for tab component {componentType}")
    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final By tabLocator) {
        LogUI.step("Checking if tab with locator " + tabLocator + " is visible for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isVisible(tabLocator);
    }

    @Step("Checking if tab {tabText} is selected in container {container} for tab component {componentType}")
    @Override
    public boolean isSelected(final TabComponentType componentType, final SmartWebElement container, final String tabText) {
        LogUI.step("Checking if tab " + tabText + " is selected in container " + container + " for tab component " + componentType);
        return tabComponent(componentType).isSelected(container, tabText);
    }

    @Step("Checking if tab is selected in container {container} for tab component {componentType}")
    @Override
    public boolean isSelected(final TabComponentType componentType, final SmartWebElement container) {
        LogUI.step("Checking if tab is selected in container " + container + " for tab component " + componentType);
        return tabComponent(componentType).isSelected(container);
    }

    @Step("Checking if tab {tabText} is selected for tab component {componentType}")
    @Override
    public boolean isSelected(final TabComponentType componentType, final String tabText) {
        LogUI.step("Checking if tab " + tabText + " is selected for tab component " + componentType);
        return tabComponent(componentType).isSelected(tabText);
    }

    @Step("Checking if tab with locator {tabLocator} is selected for tab component {componentType}")
    @Override
    public boolean isSelected(final TabComponentType componentType, final By tabLocator) {
        LogUI.step("Checking if tab with locator " + tabLocator + " is selected for tab component " + componentType);
        return tabComponent(componentType).isSelected(tabLocator);
    }

    @Step("Inserting values for tab component {componentType} in table cell element")
    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        LogUI.step("Inserting values for tab component " + componentType + " in table cell element");
        tabComponent((TabComponentType) componentType).clickElementInCell(cellElement);
    }

    private Tab tabComponent(final TabComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
