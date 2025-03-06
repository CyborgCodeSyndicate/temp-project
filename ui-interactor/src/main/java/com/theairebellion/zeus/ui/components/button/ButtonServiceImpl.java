package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class ButtonServiceImpl extends AbstractComponentService<ButtonComponentType, Button> implements ButtonService {

    public ButtonServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Button createComponent(final ButtonComponentType componentType) {
        return ComponentFactory.getButtonComponent(componentType, driver);
    }

    @Step("[UI - Button] Clicking button: {buttonText} in container")
    @Override
    public void click(final ButtonComponentType componentType, final SmartWebElement container,
                      final String buttonText) {
        LogUI.step("Clicking button: " + buttonText + " in container");
        buttonComponent(componentType).click(container, buttonText);
    }

    @Step("[UI - Button] Clicking button in container")
    @Override
    public void click(final ButtonComponentType componentType, final SmartWebElement container) {
        LogUI.step("Clicking button in container");
        buttonComponent(componentType).click(container);
    }

    @Step("[UI - Button] Clicking button: {buttonText}")
    @Override
    public void click(final ButtonComponentType componentType, final String buttonText) {
        LogUI.step("Clicking button: " + buttonText);
        buttonComponent(componentType).click(buttonText);
    }

    @Step("[UI - Button] Clicking button using locator")
    @Override
    public void click(final ButtonComponentType componentType, final By buttonLocator) {
        LogUI.step("Clicking button using locator: " + buttonLocator);
        buttonComponent(componentType).click(buttonLocator);
    }

    @Step("[UI - Button] Checking if button is enabled")
    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final SmartWebElement container,
                             final String buttonText) {
        LogUI.step("Checking if button is enabled: " + buttonText);
        return buttonComponent(componentType).isEnabled(container, buttonText);
    }

    @Step("[UI - Button] Checking if button is enabled in container")
    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final SmartWebElement container) {
        LogUI.step("Checking if button is enabled in container");
        return buttonComponent(componentType).isEnabled(container);
    }

    @Step("[UI - Button] Checking if button: {buttonText} is enabled")
    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final String buttonText) {
        LogUI.step("Checking if button is enabled: " + buttonText);
        return buttonComponent(componentType).isEnabled(buttonText);
    }

    @Step("[UI - Button] Checking if button is enabled using locator")
    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final By buttonLocator) {
        LogUI.step("Checking if button is enabled using locator: " + buttonLocator);
        return buttonComponent(componentType).isEnabled(buttonLocator);
    }

    @Step("[UI - Button] Checking if button is visible")
    @Override
    public boolean isVisible(final ButtonComponentType componentType, final SmartWebElement container,
                             final String buttonText) {
        LogUI.step("Checking if button is visible: " + buttonText);
        return buttonComponent(componentType).isVisible(container, buttonText);
    }

    @Step("[UI - Button] Checking if button is visible in container")
    @Override
    public boolean isVisible(final ButtonComponentType componentType, final SmartWebElement container) {
        LogUI.step("Checking if button is visible in container");
        return buttonComponent(componentType).isVisible(container);
    }

    @Step("[UI - Button] Checking if button: {buttonText} is visible")
    @Override
    public boolean isVisible(final ButtonComponentType componentType, final String buttonText) {
        LogUI.step("Checking if button is visible: " + buttonText);
        return buttonComponent(componentType).isVisible(buttonText);
    }

    @Step("[UI - Button] Checking if button is visible using locator")
    @Override
    public boolean isVisible(final ButtonComponentType componentType, final By buttonLocator) {
        LogUI.step("Checking if button is visible using locator: " + buttonLocator);
        return buttonComponent(componentType).isVisible(buttonLocator);
    }

    @Step("[UI - Button] Performing table insertion")
    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        LogUI.step("Performing table insertion in cell element");
        buttonComponent((ButtonComponentType) componentType).clickElementInCell(cellElement);
    }

    private Button buttonComponent(final ButtonComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
