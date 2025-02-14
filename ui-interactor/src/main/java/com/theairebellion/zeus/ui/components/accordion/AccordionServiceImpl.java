package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

public class AccordionServiceImpl extends AbstractComponentService<AccordionComponentType, Accordion> implements AccordionService {

    public AccordionServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Accordion createComponent(final AccordionComponentType componentType) {
        return ComponentFactory.getAccordionComponent(componentType, driver);
    }

    @Override
    public void expand(final AccordionComponentType componentType, final SmartWebElement container, final String... accordionText) {
        accordionComponent(componentType).expand(container, accordionText);
    }

    @Override
    public String expand(final AccordionComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        return accordionComponent(componentType).expand(container, strategy);
    }

    @Override
    public void expand(final AccordionComponentType componentType, final String... accordionText) {
        accordionComponent(componentType).expand(accordionText);
    }

    @Override
    public void expand(final AccordionComponentType componentType, final By... accordionLocator) {
        accordionComponent(componentType).expand(accordionLocator);
    }

    @Override
    public void collapse(final AccordionComponentType componentType, final SmartWebElement container, final String... accordionText) {
        accordionComponent(componentType).collapse(container, accordionText);
    }

    @Override
    public String collapse(final AccordionComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        return accordionComponent(componentType).collapse(container, strategy);
    }

    @Override
    public void collapse(final AccordionComponentType componentType, final String... accordionText) {
        accordionComponent(componentType).collapse(accordionText);
    }

    @Override
    public void collapse(final AccordionComponentType componentType, final By... accordionLocator) {
        accordionComponent(componentType).collapse(accordionLocator);
    }

    @Override
    public boolean areEnabled(final AccordionComponentType componentType, final SmartWebElement container, final String... accordionText) {
        return accordionComponent(componentType).areEnabled(container, accordionText);
    }

    @Override
    public boolean areEnabled(final AccordionComponentType componentType, final String... accordionText) {
        return accordionComponent(componentType).areEnabled(accordionText);
    }

    @Override
    public boolean areEnabled(final AccordionComponentType componentType, final By... accordionLocator) {
        return accordionComponent(componentType).areEnabled(accordionLocator);
    }

    @Override
    public List<String> getExpanded(final AccordionComponentType componentType, final SmartWebElement container) {
        return accordionComponent(componentType).getExpanded(container);
    }

    @Override
    public List<String> getCollapsed(final AccordionComponentType componentType, final SmartWebElement container) {
        return accordionComponent(componentType).getCollapsed(container);
    }

    @Override
    public List<String> getAll(final AccordionComponentType componentType, final SmartWebElement container) {
        return accordionComponent(componentType).getAll(container);
    }

    @Override
    public String getTitle(final AccordionComponentType componentType, final By accordionLocator) {
        return accordionComponent(componentType).getTitle(accordionLocator);
    }

    @Override
    public String getText(final AccordionComponentType componentType, final By accordionLocator) {
        return accordionComponent(componentType).getText(accordionLocator);
    }

    private Accordion accordionComponent(final AccordionComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
