package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ButtonServiceImpl implements ButtonService {

    protected SmartSelenium smartSelenium;
    private static Map<ButtonComponentType, Button> components;

    public ButtonServiceImpl(WebDriver driver) {
        this.smartSelenium = new SmartSelenium(driver);
        components = new HashMap<>();
    }

    public ButtonServiceImpl(SmartSelenium smartSelenium) {
        this.smartSelenium = smartSelenium;
        components = new HashMap<>();
    }

    @Override
    public void click(final ButtonComponentType componentType, final WebElement container, final String buttonText) {
        buttonComponent(componentType).click(container, buttonText);
    }

    @Override
    public void click(final ButtonComponentType componentType, final WebElement container) {
        buttonComponent(componentType).click(container);
    }

    @Override
    public void click(final ButtonComponentType componentType, final String buttonText) {
        buttonComponent(componentType).click(buttonText);
    }

    @Override
    public void click(final ButtonComponentType componentType, final By buttonLocator) {
        buttonComponent(componentType).click(buttonLocator);
    }

    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final WebElement container,
                             final String buttonText) {
        return buttonComponent(componentType).isEnabled(container, buttonText);
    }

    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final WebElement container) {
        return buttonComponent(componentType).isEnabled(container);
    }

    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final String buttonText) {
        return buttonComponent(componentType).isEnabled(buttonText);
    }

    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final By buttonLocator) {
        return buttonComponent(componentType).isEnabled(buttonLocator);
    }

    @Override
    public boolean isPresent(final ButtonComponentType componentType, final WebElement container,
                             final String buttonText) {
        return buttonComponent(componentType).isPresent(container, buttonText);
    }

    @Override
    public boolean isPresent(final ButtonComponentType componentType, final WebElement container) {
        return buttonComponent(componentType).isPresent(container);
    }

    @Override
    public boolean isPresent(final ButtonComponentType componentType, final String buttonText) {
        return buttonComponent(componentType).isPresent(buttonText);
    }

    @Override
    public boolean isPresent(final ButtonComponentType componentType, final By buttonLocator) {
        return buttonComponent(componentType).isPresent(buttonLocator);
    }

    private Button buttonComponent(final ButtonComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getButtonComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}
