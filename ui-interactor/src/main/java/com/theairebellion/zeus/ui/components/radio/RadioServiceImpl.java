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
    public void select(final WebElement container, final String radioButtonText,
                       final RadioComponentType componentType) {
        radioComponent(componentType).select(container, radioButtonText);
    }

    @Override
    public String select(final WebElement container, final Strategy strategy,
                         final RadioComponentType componentType) {
        return radioComponent(componentType).select(container, strategy);
    }

    @Override
    public void select(final String radioButtonText, final RadioComponentType componentType) {
        radioComponent(componentType).select(radioButtonText);
    }

    @Override
    public void select(final By radioButtonLocator, final RadioComponentType componentType) {
        radioComponent(componentType).select(radioButtonLocator);
    }

    @Override
    public boolean isEnabled(final WebElement container, final String radioButtonText,
                             final RadioComponentType componentType) {
        return radioComponent(componentType).isEnabled(container, radioButtonText);
    }

    @Override
    public boolean isEnabled(final String radioButtonText, final RadioComponentType componentType) {
        return radioComponent(componentType).isEnabled(radioButtonText);
    }

    @Override
    public boolean isEnabled(final By radioButtonLocator, final RadioComponentType componentType) {
        return radioComponent(componentType).isEnabled(radioButtonLocator);
    }

    @Override
    public boolean isSelected(final WebElement container, final String radioButtonText,
                              final RadioComponentType componentType) {
        return radioComponent(componentType).isSelected(container, radioButtonText);
    }

    @Override
    public boolean isSelected(final String radioButtonText, final RadioComponentType componentType) {
        return radioComponent(componentType).isSelected(radioButtonText);
    }

    @Override
    public boolean isSelected(final By radioButtonLocator, final RadioComponentType componentType) {
        return radioComponent(componentType).isSelected(radioButtonLocator);
    }

    @Override
    public boolean isVisible(final WebElement container, final String radioButtonText,
                             final RadioComponentType componentType) {
        return radioComponent(componentType).isVisible(container, radioButtonText);
    }

    @Override
    public boolean isVisible(final String radioButtonText, final RadioComponentType componentType) {
        return radioComponent(componentType).isVisible(radioButtonText);
    }

    @Override
    public boolean isVisible(final By radioButtonLocator, final RadioComponentType componentType) {
        return radioComponent(componentType).isVisible(radioButtonLocator);
    }

    @Override
    public String getSelected(final WebElement container, final RadioComponentType componentType) {
        return radioComponent(componentType).getSelected(container);
    }

    @Override
    public String getSelected(final By containerLocator, final RadioComponentType componentType) {
        return radioComponent(componentType).getSelected(containerLocator);
    }

    @Override
    public List<String> getAll(final WebElement container, final RadioComponentType componentType) {
        return radioComponent(componentType).getAll(container);
    }

    @Override
    public List<String> getAll(final By containerLocator, final RadioComponentType componentType) {
        return radioComponent(componentType).getAll(containerLocator);
    }

    @Override
    public void insertion(By locator, ComponentType componentType, Object... values) {
        select((String) values[0], (RadioComponentType) componentType);
    }

    private Radio radioComponent(RadioComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getRadioComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}