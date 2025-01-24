package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AlertServiceImpl implements AlertService {

    protected SmartSelenium smartSelenium;
    private static Map<AlertComponentType, Alert> components;

    public AlertServiceImpl(WebDriver driver) {
        this.smartSelenium = new SmartSelenium(driver);
        components = new HashMap<>();
    }

    public AlertServiceImpl(SmartSelenium smartSelenium) {
        this.smartSelenium = smartSelenium;
        components = new HashMap<>();
    }

    @Override
    public String getValue(AlertComponentType componentType, WebElement container) {
        return alertComponent(componentType).getValue(container);
    }

    @Override
    public String getValue(AlertComponentType componentType, By containerLocator) {
        return alertComponent(componentType).getValue(containerLocator);
    }

    @Override
    public boolean isVisible(AlertComponentType componentType, WebElement container) {
        return alertComponent(componentType).isVisible(container);
    }

    @Override
    public boolean isVisible(AlertComponentType componentType, By containerLocator) {
        return alertComponent(componentType).isVisible(containerLocator);
    }

    private Alert alertComponent(final AlertComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getAlertComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}
