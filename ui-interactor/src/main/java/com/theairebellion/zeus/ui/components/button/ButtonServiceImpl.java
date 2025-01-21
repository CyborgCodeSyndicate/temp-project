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
    public void click(ButtonComponentType componentType, WebElement container, String buttonText) {
        buttonComponent(componentType).click(container, buttonText);
    }

    @Override
    public void click(ButtonComponentType componentType, WebElement container) {
        buttonComponent(componentType).click(container);
    }

    @Override
    public void click(ButtonComponentType componentType, String buttonText) {
        buttonComponent(componentType).click(buttonText);
    }

    @Override
    public void click(ButtonComponentType componentType, By buttonLocator) {
        buttonComponent(componentType).click(buttonLocator);
    }

    @Override
    public boolean isEnabled(ButtonComponentType componentType, WebElement container, String buttonText) {
        return buttonComponent(componentType).isEnabled(container, buttonText);
    }

    @Override
    public boolean isEnabled(ButtonComponentType componentType, WebElement container) {
        return buttonComponent(componentType).isEnabled(container);
    }

    @Override
    public boolean isEnabled(ButtonComponentType componentType, String buttonText) {
        return buttonComponent(componentType).isEnabled(buttonText);
    }

    @Override
    public boolean isEnabled(ButtonComponentType componentType, By buttonLocator) {
        return buttonComponent(componentType).isEnabled(buttonLocator);
    }

    @Override
    public boolean isPresent(ButtonComponentType componentType, WebElement container, String buttonText) {
        return buttonComponent(componentType).isPresent(container, buttonText);
    }

    @Override
    public boolean isPresent(ButtonComponentType componentType, WebElement container) {
        return buttonComponent(componentType).isPresent(container);
    }

    @Override
    public boolean isPresent(ButtonComponentType componentType, String buttonText) {
        return buttonComponent(componentType).isPresent(buttonText);
    }

    @Override
    public boolean isPresent(ButtonComponentType componentType, By buttonLocator) {
        return buttonComponent(componentType).isPresent(buttonLocator);
    }

    private Button buttonComponent(ButtonComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getButtonComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}
