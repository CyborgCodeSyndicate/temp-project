package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import io.qameta.allure.Step;
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

    @Step("Expanding accordion {componentType} inside container with text {accordionText}")
    @Override
    public void expand(final AccordionComponentType componentType, final SmartWebElement container, final String... accordionText) {
        LogUI.step("Expanding accordion {} inside container with text: {}", componentType, (Object) accordionText);
        accordionComponent(componentType).expand(container, accordionText);
    }

    @Step("Expanding accordion {componentType} inside container using strategy")
    @Override
    public String expand(final AccordionComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        LogUI.step("Expanding accordion {} inside container using strategy: {}", componentType, strategy);
        return accordionComponent(componentType).expand(container, strategy);
    }

    @Step("Expanding accordion {componentType} with text {accordionText}")
    @Override
    public void expand(final AccordionComponentType componentType, final String... accordionText) {
        LogUI.step("Expanding accordion {} with text: {}", componentType, (Object) accordionText);
        accordionComponent(componentType).expand(accordionText);
    }

    @Step("Expanding accordion {componentType} using locators")
    @Override
    public void expand(final AccordionComponentType componentType, final By... accordionLocator) {
        LogUI.step("Expanding accordion {} using locators", componentType);
        accordionComponent(componentType).expand(accordionLocator);
    }

    @Step("Collapsing accordion {componentType} inside container with text {accordionText}")
    @Override
    public void collapse(final AccordionComponentType componentType, final SmartWebElement container, final String... accordionText) {
        LogUI.step("Collapsing accordion {} inside container with text: {}", componentType, (Object) accordionText);
        accordionComponent(componentType).collapse(container, accordionText);
    }

    @Step("Collapsing accordion {componentType} inside container using strategy")
    @Override
    public String collapse(final AccordionComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        LogUI.step("Collapsing accordion {} inside container using strategy: {}", componentType, strategy);
        return accordionComponent(componentType).collapse(container, strategy);
    }

    @Step("Collapsing accordion {componentType} with text {accordionText}")
    @Override
    public void collapse(final AccordionComponentType componentType, final String... accordionText) {
        LogUI.step("Collapsing accordion {} with text: {}", componentType, (Object) accordionText);
        accordionComponent(componentType).collapse(accordionText);
    }

    @Step("Collapsing accordion {componentType} using locators")
    @Override
    public void collapse(final AccordionComponentType componentType, final By... accordionLocator) {
        LogUI.step("Collapsing accordion {} using locators", componentType);
        accordionComponent(componentType).collapse(accordionLocator);
    }

    @Step("Checking if accordion {componentType} is enabled inside container with text {accordionText}")
    @Override
    public boolean areEnabled(final AccordionComponentType componentType, final SmartWebElement container, final String... accordionText) {
        LogUI.step("Checking if accordion {} is enabled inside container with text: {}", componentType, (Object) accordionText);
        return accordionComponent(componentType).areEnabled(container, accordionText);
    }

    @Step("Checking if accordion {componentType} is enabled with text {accordionText}")
    @Override
    public boolean areEnabled(final AccordionComponentType componentType, final String... accordionText) {
        LogUI.step("Checking if accordion {} is enabled with text: {}", componentType, (Object) accordionText);
        return accordionComponent(componentType).areEnabled(accordionText);
    }

    @Step("Checking if accordion {componentType} is enabled using locators")
    @Override
    public boolean areEnabled(final AccordionComponentType componentType, final By... accordionLocator) {
        LogUI.step("Checking if accordion {} is enabled using locators", componentType);
        return accordionComponent(componentType).areEnabled(accordionLocator);
    }

    @Step("Getting expanded accordions for {componentType}")
    @Override
    public List<String> getExpanded(final AccordionComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting expanded accordions for {}", componentType);
        return accordionComponent(componentType).getExpanded(container);
    }

    @Step("Getting collapsed accordions for {componentType}")
    @Override
    public List<String> getCollapsed(final AccordionComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting collapsed accordions for {}", componentType);
        return accordionComponent(componentType).getCollapsed(container);
    }

    @Step("Getting all accordions for {componentType}")
    @Override
    public List<String> getAll(final AccordionComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting all accordions for {}", componentType);
        return accordionComponent(componentType).getAll(container);
    }

    @Step("Getting title for accordion {componentType} using locator")
    @Override
    public String getTitle(final AccordionComponentType componentType, final By accordionLocator) {
        LogUI.step("Getting title for accordion {} using locator", componentType);
        return accordionComponent(componentType).getTitle(accordionLocator);
    }

    @Step("Getting text for accordion {componentType} using locator")
    @Override
    public String getText(final AccordionComponentType componentType, final By accordionLocator) {
        LogUI.step("Getting text for accordion {} using locator", componentType);
        return accordionComponent(componentType).getText(accordionLocator);
    }

    private Accordion accordionComponent(final AccordionComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
