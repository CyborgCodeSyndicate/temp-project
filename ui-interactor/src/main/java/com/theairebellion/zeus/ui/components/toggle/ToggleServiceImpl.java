package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ToggleServiceImpl implements ToggleService {

    protected SmartSelenium smartSelenium;
    private static Map<ToggleComponentType, Toggle> components;

    public ToggleServiceImpl(WebDriver driver) {
        this.smartSelenium = new SmartSelenium(driver);
        components = new HashMap<>();
    }

    public ToggleServiceImpl(SmartSelenium smartSelenium) {
        this.smartSelenium = smartSelenium;
        components = new HashMap<>();
    }

    @Override
    public void activate(ToggleComponentType componentType, WebElement container, String toggleText) {
        toggleComponent(componentType).activate(container, toggleText);
    }

    @Override
    public void activate(ToggleComponentType componentType, String toggleText) {
        toggleComponent(componentType).activate(toggleText);
    }

    @Override
    public void activate(ToggleComponentType componentType, By toggleLocator) {
        toggleComponent(componentType).activate(toggleLocator);
    }

    @Override
    public void deactivate(ToggleComponentType componentType, WebElement container, String toggleText) {
        toggleComponent(componentType).deactivate(container, toggleText);
    }

    @Override
    public void deactivate(ToggleComponentType componentType, String toggleText) {
        toggleComponent(componentType).deactivate(toggleText);
    }

    @Override
    public void deactivate(ToggleComponentType componentType, By toggleLocator) {
        toggleComponent(componentType).deactivate(toggleLocator);
    }

    @Override
    public boolean isEnabled(ToggleComponentType componentType, WebElement container, String toggleText) {
        return toggleComponent(componentType).isEnabled(container, toggleText);
    }

    @Override
    public boolean isEnabled(ToggleComponentType componentType, String toggleText) {
        return toggleComponent(componentType).isEnabled(toggleText);
    }

    @Override
    public boolean isEnabled(ToggleComponentType componentType, By toggleLocator) {
        return toggleComponent(componentType).isEnabled(toggleLocator);
    }

    @Override
    public boolean isActivated(ToggleComponentType componentType, WebElement container, String toggleText) {
        return toggleComponent(componentType).isActivated(container, toggleText);
    }

    @Override
    public boolean isActivated(ToggleComponentType componentType, String toggleText) {
        return toggleComponent(componentType).isActivated(toggleText);
    }

    @Override
    public boolean isActivated(ToggleComponentType componentType, By toggleLocator) {
        return toggleComponent(componentType).isActivated(toggleLocator);
    }

    private Toggle toggleComponent(ToggleComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getToggleComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}
