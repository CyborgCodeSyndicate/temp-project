package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import io.qameta.allure.Allure;

public class LinkServiceImpl extends AbstractComponentService<LinkComponentType, Link> implements LinkService {

    public LinkServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Link createComponent(final LinkComponentType componentType) {
        return ComponentFactory.getLinkComponent(componentType, driver);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, SmartWebElement container, String buttonText) {
        Allure.step(String.format("[UI - Link] Clicking link %s with text %s", componentType, buttonText));
        LogUI.step("Clicking link " + componentType + " with text " + buttonText);
        linkComponent((LinkComponentType) componentType).click(container, buttonText);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Link] Clicking link %s", componentType));
        LogUI.step("Clicking link " + componentType);
        linkComponent((LinkComponentType) componentType).click(container);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final String buttonText) {
        Allure.step(String.format("[UI - Link] Clicking link %s with text %s", componentType, buttonText));
        LogUI.step("Clicking link " + componentType + " with text " + buttonText);
        linkComponent((LinkComponentType) componentType).click(buttonText);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final By buttonLocator) {
        Allure.step(String.format("[UI - Link] Clicking link %s using locator %s", componentType, buttonLocator));
        LogUI.step("Clicking link " + componentType + " using locator " + buttonLocator);
        linkComponent((LinkComponentType) componentType).click(buttonLocator);
    }

    @Override
    public void doubleClick(final LinkComponentType componentType, final SmartWebElement container, final String buttonText) {
        Allure.step(String.format("[UI - Link] Double-clicking link %s with text %s", componentType, buttonText));
        LogUI.step("Double-clicking link " + componentType + " with text " + buttonText);
        linkComponent(componentType).doubleClick(container, buttonText);
    }

    @Override
    public void doubleClick(final LinkComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Link] Double-clicking link %s", componentType));
        LogUI.step("Double-clicking link " + componentType);
        linkComponent(componentType).doubleClick(container);
    }

    @Override
    public void doubleClick(final LinkComponentType componentType, final String buttonText) {
        Allure.step(String.format("[UI - Link] Double-clicking link %s with text %s", componentType, buttonText));
        LogUI.step("Double-clicking link " + componentType + " with text " + buttonText);
        linkComponent(componentType).doubleClick(buttonText);
    }

    @Override
    public void doubleClick(final LinkComponentType componentType, final By buttonLocator) {
        Allure.step(String.format("[UI - Link] Double-clicking link %s using locator %s", componentType, buttonLocator));
        LogUI.step("Double-clicking link " + componentType + " using locator " + buttonLocator);
        linkComponent(componentType).doubleClick(buttonLocator);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container, final String buttonText) {
        Allure.step(String.format("[UI - Link] Checking if link %s is enabled with text %s", componentType, buttonText));
        LogUI.step("Checking if link " + componentType + " is enabled with text " + buttonText);
        return linkComponent((LinkComponentType) componentType).isEnabled(container, buttonText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Link] Checking if link %s is enabled", componentType));
        LogUI.step("Checking if link " + componentType + " is enabled");
        return linkComponent((LinkComponentType) componentType).isEnabled(container);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final String buttonText) {
        Allure.step(String.format("[UI - Link] Checking if link %s is enabled with text %s", componentType, buttonText));
        LogUI.step("Checking if link " + componentType + " is enabled with text " + buttonText);
        return linkComponent((LinkComponentType) componentType).isEnabled(buttonText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final By buttonLocator) {
        Allure.step(String.format("[UI - Link] Checking if link %s is enabled using locator %s", componentType, buttonLocator));
        LogUI.step("Checking if link " + componentType + " is enabled using locator " + buttonLocator);
        return linkComponent((LinkComponentType) componentType).isEnabled(buttonLocator);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container, final String buttonText) {
        Allure.step(String.format("[UI - Link] Checking if link %s is visible with text %s", componentType, buttonText));
        LogUI.step("Checking if link " + componentType + " is visible with text " + buttonText);
        return linkComponent((LinkComponentType) componentType).isVisible(container, buttonText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Link] Checking if link %s is visible", componentType));
        LogUI.step("Checking if link " + componentType + " is visible");
        return linkComponent((LinkComponentType) componentType).isVisible(container);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final String buttonText) {
        Allure.step(String.format("[UI - Link] Checking if link %s is visible with text %s", componentType, buttonText));
        LogUI.step("Checking if link " + componentType + " is visible with text " + buttonText);
        return linkComponent((LinkComponentType) componentType).isVisible(buttonText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final By buttonLocator) {
        Allure.step(String.format("[UI - Link] Checking if link %s is visible using locator %s", componentType, buttonLocator));
        LogUI.step("Checking if link " + componentType + " is visible using locator " + buttonLocator);
        return linkComponent((LinkComponentType) componentType).isVisible(buttonLocator);
    }

    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType, final String... values) {
        Allure.step(String.format("[UI - Link] Inserting values %s in cell for link %s", String.join(", ", values), componentType));
        LogUI.step("Inserting values " + String.join(", ", values) + " in cell for link " + componentType);
        linkComponent((LinkComponentType) componentType).clickElementInCell(cellElement);
    }

    private Link linkComponent(final LinkComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
