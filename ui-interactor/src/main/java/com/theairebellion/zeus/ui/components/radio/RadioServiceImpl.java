package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

public class RadioServiceImpl extends AbstractComponentService<RadioComponentType, Radio> implements RadioService {

    public RadioServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Radio createComponent(final RadioComponentType componentType) {
        return ComponentFactory.getRadioComponent(componentType, driver);
    }

    @Override
    public void select(final RadioComponentType componentType, final SmartWebElement container,
                       final String radioButtonText) {
        radioComponent(componentType).select(container, radioButtonText);
    }

    @Override
    public String select(final RadioComponentType componentType, final SmartWebElement container, final Strategy strategy) {
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
    public boolean isEnabled(final RadioComponentType componentType, final SmartWebElement container,
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
    public boolean isSelected(final RadioComponentType componentType, final SmartWebElement container,
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
    public boolean isVisible(final RadioComponentType componentType, final SmartWebElement container,
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
    public String getSelected(final RadioComponentType componentType, final SmartWebElement container) {
        return radioComponent(componentType).getSelected(container);
    }

    @Override
    public String getSelected(final RadioComponentType componentType, final By containerLocator) {
        return radioComponent(componentType).getSelected(containerLocator);
    }

    @Override
    public List<String> getAll(final RadioComponentType componentType, final SmartWebElement container) {
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
        return getOrCreateComponent(componentType);
    }
}