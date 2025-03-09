package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

public class ButtonServiceImpl extends AbstractComponentService<ButtonComponentType, Button> implements ButtonService {

    public ButtonServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Button createComponent(final ButtonComponentType componentType) {
        return ComponentFactory.getButtonComponent(componentType, driver);
    }

    @Override
    public void click(final ButtonComponentType componentType, final SmartWebElement container,
                      final String buttonText) {
        Allure.step(String.format("[UI - Button] Clicking button: %s in container", buttonText));
        LogUI.step("Clicking button: " + buttonText + " in container");
        buttonComponent(componentType).click(container, buttonText);
    }

    @Override
    public void click(final ButtonComponentType componentType, final SmartWebElement container) {
        Allure.step("[UI - Button] Clicking button in container");
        LogUI.step("Clicking button in container");
        buttonComponent(componentType).click(container);
    }

    @Override
    public void click(final ButtonComponentType componentType, final String buttonText) {
        Allure.step(String.format("[UI - Button] Clicking button: %s", buttonText));
        LogUI.step("Clicking button: " + buttonText);
        buttonComponent(componentType).click(buttonText);
    }

    @Override
    public void click(final ButtonComponentType componentType, final By buttonLocator) {
        Allure.step(String.format("[UI - Button] Clicking button using locator: %s", buttonLocator));
        LogUI.step("Clicking button using locator: " + buttonLocator);
        buttonComponent(componentType).click(buttonLocator);
    }

    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final SmartWebElement container,
                             final String buttonText) {
        Allure.step(String.format("[UI - Button] Checking if button is enabled: %s", buttonText));
        LogUI.step("Checking if button is enabled: " + buttonText);
        return buttonComponent(componentType).isEnabled(container, buttonText);
    }

    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final SmartWebElement container) {
        Allure.step("[UI - Button] Checking if button is enabled in container");
        LogUI.step("Checking if button is enabled in container");
        return buttonComponent(componentType).isEnabled(container);
    }

    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final String buttonText) {
        Allure.step(String.format("[UI - Button] Checking if button is enabled: %s", buttonText));
        LogUI.step("Checking if button is enabled: " + buttonText);
        return buttonComponent(componentType).isEnabled(buttonText);
    }

    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final By buttonLocator) {
        Allure.step(String.format("[UI - Button] Checking if button is enabled using locator: %s", buttonLocator));
        LogUI.step("Checking if button is enabled using locator: " + buttonLocator);
        return buttonComponent(componentType).isEnabled(buttonLocator);
    }

    @Override
    public boolean isVisible(final ButtonComponentType componentType, final SmartWebElement container,
                             final String buttonText) {
        Allure.step(String.format("[UI - Button] Checking if button is visible: %s", buttonText));
        LogUI.step("Checking if button is visible: " + buttonText);
        return buttonComponent(componentType).isVisible(container, buttonText);
    }

    @Override
    public boolean isVisible(final ButtonComponentType componentType, final SmartWebElement container) {
        Allure.step("[UI - Button] Checking if button is visible in container");
        LogUI.step("Checking if button is visible in container");
        return buttonComponent(componentType).isVisible(container);
    }

    @Override
    public boolean isVisible(final ButtonComponentType componentType, final String buttonText) {
        Allure.step(String.format("[UI - Button] Checking if button is visible: %s", buttonText));
        LogUI.step("Checking if button is visible: " + buttonText);
        return buttonComponent(componentType).isVisible(buttonText);
    }

    @Override
    public boolean isVisible(final ButtonComponentType componentType, final By buttonLocator) {
        Allure.step(String.format("[UI - Button] Checking if button is visible using locator: %s", buttonLocator));
        LogUI.step("Checking if button is visible using locator: " + buttonLocator);
        return buttonComponent(componentType).isVisible(buttonLocator);
    }

    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        Allure.step("[UI - Button] Performing table insertion");
        LogUI.step("Performing table insertion in cell element");
        buttonComponent((ButtonComponentType) componentType).clickElementInCell(cellElement);
    }

    private Button buttonComponent(final ButtonComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
