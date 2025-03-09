package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import io.qameta.allure.Allure;

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
        Allure.step(String.format("[UI - Tab] Clicking tab %s in container %s for tab component %s", tabText, container, componentType));
        LogUI.step("Clicking tab " + tabText + " in container " + container + " for tab component " + componentType);
        tabComponent((TabComponentType) componentType).click(container, tabText);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Tab] Clicking tab in container %s for tab component %s", container, componentType));
        LogUI.step("Clicking tab in container " + container + " for tab component " + componentType);
        tabComponent((TabComponentType) componentType).click(container);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final String tabText) {
        Allure.step(String.format("[UI - Tab] Clicking tab %s for tab component %s", tabText, componentType));
        LogUI.step("Clicking tab " + tabText + " for tab component " + componentType);
        tabComponent((TabComponentType) componentType).click(tabText);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final By tabLocator) {
        Allure.step(String.format("[UI - Tab] Clicking tab with locator %s for tab component %s", tabLocator, componentType));
        LogUI.step("Clicking tab with locator " + tabLocator + " for tab component " + componentType);
        tabComponent((TabComponentType) componentType).click(tabLocator);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container,
                                                             final String tabText) {
        Allure.step(String.format("[UI - Tab] Checking if tab %s is enabled in container %s for tab component %s", tabText, container, componentType));
        LogUI.step("Checking if tab " + tabText + " is enabled in container " + container + " for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isEnabled(container, tabText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Tab] Checking if tab is enabled in container %s for tab component %s", container, componentType));
        LogUI.step("Checking if tab is enabled in container " + container + " for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isEnabled(container);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final String tabText) {
        Allure.step(String.format("[UI - Tab] Checking if tab %s is enabled for tab component %s", tabText, componentType));
        LogUI.step("Checking if tab " + tabText + " is enabled for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isEnabled(tabText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final By tabLocator) {
        Allure.step(String.format("[UI - Tab] Checking if tab with locator %s is enabled for tab component %s", tabLocator, componentType));
        LogUI.step("Checking if tab with locator " + tabLocator + " is enabled for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isEnabled(tabLocator);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container,
                                                             final String tabText) {
        Allure.step(String.format("[UI - Tab] Checking if tab %s is visible in container %s for tab component %s", tabText, container, componentType));
        LogUI.step("Checking if tab " + tabText + " is visible in container " + container + " for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isVisible(container, tabText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Tab] Checking if tab is visible in container %s for tab component %s", container, componentType));
        LogUI.step("Checking if tab is visible in container " + container + " for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isVisible(container);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final String tabText) {
        Allure.step(String.format("[UI - Tab] Checking if tab %s is visible for tab component %s", tabText, componentType));
        LogUI.step("Checking if tab " + tabText + " is visible for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isVisible(tabText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final By tabLocator) {
        Allure.step(String.format("[UI - Tab] Checking if tab with locator %s is visible for tab component %s", tabLocator, componentType));
        LogUI.step("Checking if tab with locator " + tabLocator + " is visible for tab component " + componentType);
        return tabComponent((TabComponentType) componentType).isVisible(tabLocator);
    }

    @Override
    public boolean isSelected(final TabComponentType componentType, final SmartWebElement container, final String tabText) {
        Allure.step(String.format("[UI - Tab] Checking if tab %s is selected in container %s for tab component %s", tabText, container, componentType));
        LogUI.step("Checking if tab " + tabText + " is selected in container " + container + " for tab component " + componentType);
        return tabComponent(componentType).isSelected(container, tabText);
    }

    @Override
    public boolean isSelected(final TabComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Tab] Checking if tab is selected in container %s for tab component %s", container, componentType));
        LogUI.step("Checking if tab is selected in container " + container + " for tab component " + componentType);
        return tabComponent(componentType).isSelected(container);
    }

    @Override
    public boolean isSelected(final TabComponentType componentType, final String tabText) {
        Allure.step(String.format("[UI - Tab] Checking if tab %s is selected for tab component %s", tabText, componentType));
        LogUI.step("Checking if tab " + tabText + " is selected for tab component " + componentType);
        return tabComponent(componentType).isSelected(tabText);
    }

    @Override
    public boolean isSelected(final TabComponentType componentType, final By tabLocator) {
        Allure.step(String.format("[UI - Tab] Checking if tab with locator %s is selected for tab component %s", tabLocator, componentType));
        LogUI.step("Checking if tab with locator " + tabLocator + " is selected for tab component " + componentType);
        return tabComponent(componentType).isSelected(tabLocator);
    }

    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        Allure.step(String.format("[UI - Tab] Inserting values for tab component %s in table cell element", componentType));
        LogUI.step("Inserting values for tab component " + componentType + " in table cell element");
        tabComponent((TabComponentType) componentType).clickElementInCell(cellElement);
    }

    private Tab tabComponent(final TabComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}