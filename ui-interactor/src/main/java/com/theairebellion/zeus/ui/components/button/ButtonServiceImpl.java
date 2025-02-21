package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
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
        buttonComponent(componentType).click(container, buttonText);
    }


    @Override
    public void click(final ButtonComponentType componentType, final SmartWebElement container) {
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
    public boolean isEnabled(final ButtonComponentType componentType, final SmartWebElement container,
                             final String buttonText) {
        return buttonComponent(componentType).isEnabled(container, buttonText);
    }


    @Override
    public boolean isEnabled(final ButtonComponentType componentType, final SmartWebElement container) {
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
    public boolean isVisible(final ButtonComponentType componentType, final SmartWebElement container,
                             final String buttonText) {
        return buttonComponent(componentType).isVisible(container, buttonText);
    }


    @Override
    public boolean isVisible(final ButtonComponentType componentType, final SmartWebElement container) {
        return buttonComponent(componentType).isVisible(container);
    }


    @Override
    public boolean isVisible(final ButtonComponentType componentType, final String buttonText) {
        return buttonComponent(componentType).isVisible(buttonText);
    }


    @Override
    public boolean isVisible(final ButtonComponentType componentType, final By buttonLocator) {
        return buttonComponent(componentType).isVisible(buttonLocator);
    }


    private Button buttonComponent(final ButtonComponentType componentType) {
        return getOrCreateComponent(componentType);
    }


    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        buttonComponent((ButtonComponentType) componentType).clickInTable(cellElement);
    }

}
