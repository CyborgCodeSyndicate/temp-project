package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LinkServiceImpl implements LinkService {

    protected SmartSelenium smartSelenium;
    private static Map<LinkComponentType, Link> components;

    public LinkServiceImpl(WebDriver driver) {
        this.smartSelenium = new SmartSelenium(driver);
        components = new HashMap<>();
    }

    public LinkServiceImpl(SmartSelenium smartSelenium) {
        this.smartSelenium = smartSelenium;
        components = new HashMap<>();
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, WebElement container, String buttonText) {
        linkComponent((LinkComponentType) componentType).click(container, buttonText);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final WebElement container) {
        linkComponent((LinkComponentType) componentType).click(container);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final String buttonText) {
        linkComponent((LinkComponentType) componentType).click(buttonText);
    }

    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final By buttonLocator) {
        linkComponent((LinkComponentType) componentType).click(buttonLocator);
    }

    @Override
    public void doubleClick(final LinkComponentType componentType, final WebElement container,
                            final String buttonText) {
        linkComponent(componentType).click(container, buttonText);
    }

    @Override
    public void doubleClick(final LinkComponentType componentType, final WebElement container) {
        linkComponent(componentType).click(container);
    }

    @Override
    public void doubleClick(final LinkComponentType componentType, final String buttonText) {
        linkComponent(componentType).click(buttonText);
    }

    @Override
    public void doubleClick(final LinkComponentType componentType, final By buttonLocator) {
        linkComponent(componentType).click(buttonLocator);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final WebElement container,
                                                             final String buttonText) {
        return linkComponent((LinkComponentType) componentType).isEnabled(container, buttonText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final WebElement container) {
        return linkComponent((LinkComponentType) componentType).isEnabled(container);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final String buttonText) {
        return linkComponent((LinkComponentType) componentType).isEnabled(buttonText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final By buttonLocator) {
        return linkComponent((LinkComponentType) componentType).isEnabled(buttonLocator);
    }

    @Override
    public <T extends ButtonComponentType> boolean isPresent(final T componentType, final WebElement container,
                                                             final String buttonText) {
        return linkComponent((LinkComponentType) componentType).isPresent(container, buttonText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isPresent(final T componentType, final WebElement container) {
        return linkComponent((LinkComponentType) componentType).isPresent(container);
    }

    @Override
    public <T extends ButtonComponentType> boolean isPresent(final T componentType, final String buttonText) {
        return linkComponent((LinkComponentType) componentType).isPresent(buttonText);
    }

    @Override
    public <T extends ButtonComponentType> boolean isPresent(final T componentType, final By buttonLocator) {
        return linkComponent((LinkComponentType) componentType).isPresent(buttonLocator);
    }

    private Link linkComponent(final LinkComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getLinkComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}
