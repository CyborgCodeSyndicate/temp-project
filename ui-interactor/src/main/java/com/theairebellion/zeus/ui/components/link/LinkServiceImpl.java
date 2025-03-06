package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import io.qameta.allure.Step;

public class LinkServiceImpl extends AbstractComponentService<LinkComponentType, Link> implements LinkService {

    public LinkServiceImpl(SmartWebDriver driver) {
        super(driver);
    }


    @Override
    protected Link createComponent(final LinkComponentType componentType) {
        return ComponentFactory.getLinkComponent(componentType, driver);
    }

    @Step("Clicking link {componentType} with text {buttonText}")
    @Override
    public <T extends ButtonComponentType> void click(final T componentType, SmartWebElement container, String buttonText) {
        LogUI.step("Clicking link " + componentType + " with text " + buttonText);
        linkComponent((LinkComponentType) componentType).click(container, buttonText);
    }

    @Step("Clicking link {componentType}")
    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final SmartWebElement container) {
        LogUI.step("Clicking link " + componentType);
        linkComponent((LinkComponentType) componentType).click(container);
    }

    @Step("Clicking link {componentType} with text {buttonText}")
    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final String buttonText) {
        LogUI.step("Clicking link " + componentType + " with text " + buttonText);
        linkComponent((LinkComponentType) componentType).click(buttonText);
    }

    @Step("Clicking link {componentType} using locator {buttonLocator}")
    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final By buttonLocator) {
        LogUI.step("Clicking link " + componentType + " using locator " + buttonLocator);
        linkComponent((LinkComponentType) componentType).click(buttonLocator);
    }

    @Step("Double-clicking link {componentType} with text {buttonText}")
    @Override
    public void doubleClick(final LinkComponentType componentType, final SmartWebElement container,
                            final String buttonText) {
        LogUI.step("Double-clicking link " + componentType + " with text " + buttonText);
        linkComponent(componentType).doubleClick(container, buttonText);
    }

    @Step("Double-clicking link {componentType}")
    @Override
    public void doubleClick(final LinkComponentType componentType, final SmartWebElement container) {
        LogUI.step("Double-clicking link " + componentType);
        linkComponent(componentType).doubleClick(container);
    }

    @Step("Double-clicking link {componentType} with text {buttonText}")
    @Override
    public void doubleClick(final LinkComponentType componentType, final String buttonText) {
        LogUI.step("Double-clicking link " + componentType + " with text " + buttonText);
        linkComponent(componentType).doubleClick(buttonText);
    }

    @Step("Double-clicking link {componentType} using locator {buttonLocator}")
    @Override
    public void doubleClick(final LinkComponentType componentType, final By buttonLocator) {
        LogUI.step("Double-clicking link " + componentType + " using locator " + buttonLocator);
        linkComponent(componentType).doubleClick(buttonLocator);
    }

    @Step("Checking if link {componentType} is enabled with text {buttonText}")
    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container,
                                                             final String buttonText) {
        LogUI.step("Checking if link " + componentType + " is enabled with text " + buttonText);
        return linkComponent((LinkComponentType) componentType).isEnabled(container, buttonText);
    }

    @Step("Checking if link {componentType} is enabled")
    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container) {
        LogUI.step("Checking if link " + componentType + " is enabled");
        return linkComponent((LinkComponentType) componentType).isEnabled(container);
    }

    @Step("Checking if link {componentType} is enabled with text {buttonText}")
    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final String buttonText) {
        LogUI.step("Checking if link " + componentType + " is enabled with text " + buttonText);
        return linkComponent((LinkComponentType) componentType).isEnabled(buttonText);
    }

    @Step("Checking if link {componentType} is enabled using locator {buttonLocator}")
    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final By buttonLocator) {
        LogUI.step("Checking if link " + componentType + " is enabled using locator " + buttonLocator);
        return linkComponent((LinkComponentType) componentType).isEnabled(buttonLocator);
    }

    @Step("Checking if link {componentType} is visible with text {buttonText}")
    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container,
                                                             final String buttonText) {
        LogUI.step("Checking if link " + componentType + " is visible with text " + buttonText);
        return linkComponent((LinkComponentType) componentType).isVisible(container, buttonText);
    }

    @Step("Checking if link {componentType} is visible")
    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container) {
        LogUI.step("Checking if link " + componentType + " is visible");
        return linkComponent((LinkComponentType) componentType).isVisible(container);
    }

    @Step("Checking if link {componentType} is visible with text {buttonText}")
    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final String buttonText) {
        LogUI.step("Checking if link " + componentType + " is visible with text " + buttonText);
        return linkComponent((LinkComponentType) componentType).isVisible(buttonText);
    }

    @Step("Checking if link {componentType} is visible using locator {buttonLocator}")
    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final By buttonLocator) {
        LogUI.step("Checking if link " + componentType + " is visible using locator " + buttonLocator);
        return linkComponent((LinkComponentType) componentType).isVisible(buttonLocator);
    }

    @Step("Inserting values {values} in cell for link {componentType}")
    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        LogUI.step("Inserting values " + String.join(", ", values) + " in cell for link " + componentType);
        linkComponent((LinkComponentType) componentType).clickElementInCell(cellElement);
    }


    private Link linkComponent(final LinkComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
