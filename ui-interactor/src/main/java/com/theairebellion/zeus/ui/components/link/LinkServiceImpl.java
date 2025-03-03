package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class LinkServiceImpl extends AbstractComponentService<LinkComponentType, Link> implements LinkService {

    public LinkServiceImpl(SmartWebDriver driver) {
        super(driver);
    }


    @Override
    protected Link createComponent(final LinkComponentType componentType) {
        return ComponentFactory.getLinkComponent(componentType, driver);
    }


    @Override
    public <T extends ButtonComponentType> void click(final T componentType, SmartWebElement container, String buttonText) {
        linkComponent((LinkComponentType) componentType).click(container, buttonText);
    }


    @Override
    public <T extends ButtonComponentType> void click(final T componentType, final SmartWebElement container) {
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
    public void doubleClick(final LinkComponentType componentType, final SmartWebElement container,
                            final String buttonText) {
        linkComponent(componentType).doubleClick(container, buttonText);
    }


    @Override
    public void doubleClick(final LinkComponentType componentType, final SmartWebElement container) {
        linkComponent(componentType).doubleClick(container);
    }


    @Override
    public void doubleClick(final LinkComponentType componentType, final String buttonText) {
        linkComponent(componentType).doubleClick(buttonText);
    }


    @Override
    public void doubleClick(final LinkComponentType componentType, final By buttonLocator) {
        linkComponent(componentType).doubleClick(buttonLocator);
    }


    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container,
                                                             final String buttonText) {
        return linkComponent((LinkComponentType) componentType).isEnabled(container, buttonText);
    }


    @Override
    public <T extends ButtonComponentType> boolean isEnabled(final T componentType, final SmartWebElement container) {
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
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container,
                                                             final String buttonText) {
        return linkComponent((LinkComponentType) componentType).isVisible(container, buttonText);
    }


    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final SmartWebElement container) {
        return linkComponent((LinkComponentType) componentType).isVisible(container);
    }


    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final String buttonText) {
        return linkComponent((LinkComponentType) componentType).isVisible(buttonText);
    }


    @Override
    public <T extends ButtonComponentType> boolean isVisible(final T componentType, final By buttonLocator) {
        return linkComponent((LinkComponentType) componentType).isVisible(buttonLocator);
    }


    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        linkComponent((LinkComponentType) componentType).clickElementInCell(cellElement);
    }


    private Link linkComponent(final LinkComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
