package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RadioServiceImpl implements RadioService {

    protected SmartSelenium smartSelenium;
    private static Map<RadioComponentType, Radio> components;

    public RadioServiceImpl(WebDriver driver) {
        this.smartSelenium = new SmartSelenium(driver);
        components = new HashMap<>();
    }

    public RadioServiceImpl(SmartSelenium smartSelenium) {
        this.smartSelenium = smartSelenium;
        components = new HashMap<>();
    }

    @Override
    public void select(final RadioComponentType componentType, final WebElement container,
                       final String radioButtonText) {
        radioComponent(componentType).select(container, radioButtonText);
    }

    @Override
    public String select(final RadioComponentType componentType, final WebElement container, final Strategy strategy) {
        return radioComponent(componentType).select(container, strategy);
    }

    @Override
    public void select(final RadioComponentType componentType, final String radioButtonText) {
        radioComponent(componentType).select(radioButtonText);
    }

    @Override
    public void select(final RadioComponentType componentType, final By radioButtonLocator) {
        radioComponent(componentType).select(radioButtonLocator);
    }

    @Override
    public boolean isEnabled(final RadioComponentType componentType, final WebElement container,
                             final String radioButtonText) {
        return radioComponent(componentType).isEnabled(container, radioButtonText);
    }

    @Override
    public boolean isEnabled(final RadioComponentType componentType, final String radioButtonText) {
        return radioComponent(componentType).isEnabled(radioButtonText);
    }

    @Override
    public boolean isEnabled(final RadioComponentType componentType, final By radioButtonLocator) {
        return radioComponent(componentType).isEnabled(radioButtonLocator);
    }

    @Override
    public boolean isSelected(final RadioComponentType componentType, final WebElement container,
                              final String radioButtonText) {
        return radioComponent(componentType).isSelected(container, radioButtonText);
    }

    @Override
    public boolean isSelected(final RadioComponentType componentType, final String radioButtonText) {
        return radioComponent(componentType).isSelected(radioButtonText);
    }

    @Override
    public boolean isSelected(final RadioComponentType componentType, final By radioButtonLocator) {
        return radioComponent(componentType).isSelected(radioButtonLocator);
    }

    @Override
    public boolean isVisible(final RadioComponentType componentType, final WebElement container,
                             final String radioButtonText) {
        return radioComponent(componentType).isVisible(container, radioButtonText);
    }

    @Override
    public boolean isVisible(final RadioComponentType componentType, final String radioButtonText) {
        return radioComponent(componentType).isVisible(radioButtonText);
    }

    @Override
    public boolean isVisible(final RadioComponentType componentType, final By radioButtonLocator) {
        return radioComponent(componentType).isVisible(radioButtonLocator);
    }

    @Override
    public String getSelected(final RadioComponentType componentType, final WebElement container) {
        return radioComponent(componentType).getSelected(container);
    }

    @Override
    public String getSelected(final RadioComponentType componentType, final By containerLocator) {
        return radioComponent(componentType).getSelected(containerLocator);
    }

    @Override
    public List<String> getAll(final RadioComponentType componentType, final WebElement container) {
        return radioComponent(componentType).getAll(container);
    }

    @Override
    public List<String> getAll(final RadioComponentType componentType, final By containerLocator) {
        return radioComponent(componentType).getAll(containerLocator);
    }

    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        select((RadioComponentType) componentType, (String) values[0]);
    }

    private Radio radioComponent(final RadioComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getRadioComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}