package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

public class AccordionServiceImpl extends AbstractComponentService<AccordionComponentType, Accordion> implements AccordionService {

    public AccordionServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Accordion createComponent(final AccordionComponentType componentType) {
        String s = "test";
        return ComponentFactory.getAccordionComponent(componentType, driver);
    }

    @Override
    public void expand(final AccordionComponentType componentType, final SmartWebElement container, final String... accordionText) {
        Allure.step(String.format("[UI - Accordion] Expanding accordion %s inside container with text %s", componentType, Arrays.toString(accordionText)));
        LogUI.step("Expanding accordion {} inside container with text: {}", componentType, accordionText);
        accordionComponent(componentType).expand(container, accordionText);
    }

    @Override
    public String expand(final AccordionComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        Allure.step(String.format("[UI - Accordion] Expanding accordion %s inside container using strategy %s", componentType, strategy));
        LogUI.step("Expanding accordion {} inside container using strategy: {}", componentType, strategy);
        return accordionComponent(componentType).expand(container, strategy);
    }

    @Override
    public void expand(final AccordionComponentType componentType, final String... accordionText) {
        Allure.step(String.format("[UI - Accordion] Expanding accordion %s with text %s", componentType, Arrays.toString(accordionText)));
        LogUI.step("Expanding accordion {} with text: {}", componentType, accordionText);
        accordionComponent(componentType).expand(accordionText);
    }

    @Override
    public void expand(final AccordionComponentType componentType, final By... accordionLocator) {
        Allure.step(String.format("[UI - Accordion] Expanding accordion %s using locators", componentType));
        LogUI.step("Expanding accordion {} using locators", componentType);
        accordionComponent(componentType).expand(accordionLocator);
    }

    @Override
    public void collapse(final AccordionComponentType componentType, final SmartWebElement container, final String... accordionText) {
        Allure.step(String.format("[UI - Accordion] Collapsing accordion %s inside container with text %s", componentType, Arrays.toString(accordionText)));
        LogUI.step("Collapsing accordion {} inside container with text: {}", componentType, accordionText);
        accordionComponent(componentType).collapse(container, accordionText);
    }

    @Override
    public String collapse(final AccordionComponentType componentType, final SmartWebElement container, final Strategy strategy) {
        Allure.step(String.format("[UI - Accordion] Collapsing accordion %s inside container using strategy %s", componentType, strategy));
        LogUI.step("Collapsing accordion {} inside container using strategy: {}", componentType, strategy);
        return accordionComponent(componentType).collapse(container, strategy);
    }

    @Override
    public void collapse(final AccordionComponentType componentType, final String... accordionText) {
        Allure.step(String.format("[UI - Accordion] Collapsing accordion %s with text %s", componentType, Arrays.toString(accordionText)));
        LogUI.step("Collapsing accordion {} with text: {}", componentType, accordionText);
        accordionComponent(componentType).collapse(accordionText);
    }

    @Override
    public void collapse(final AccordionComponentType componentType, final By... accordionLocator) {
        Allure.step(String.format("[UI - Accordion] Collapsing accordion %s using locators", componentType));
        LogUI.step("Collapsing accordion {} using locators", componentType);
        accordionComponent(componentType).collapse(accordionLocator);
    }

    @Override
    public boolean areEnabled(final AccordionComponentType componentType, final SmartWebElement container, final String... accordionText) {
        Allure.step(String.format("[UI - Accordion] Checking if accordion %s is enabled inside container with text %s", componentType, Arrays.toString(accordionText)));
        LogUI.step("Checking if accordion {} is enabled inside container with text: {}", componentType, accordionText);
        return accordionComponent(componentType).areEnabled(container, accordionText);
    }

    @Override
    public boolean areEnabled(final AccordionComponentType componentType, final String... accordionText) {
        Allure.step(String.format("[UI - Accordion] Checking if accordion %s is enabled with text %s", componentType, Arrays.toString(accordionText)));
        LogUI.step("Checking if accordion {} is enabled with text: {}", componentType, accordionText);
        return accordionComponent(componentType).areEnabled(accordionText);
    }

    @Override
    public boolean areEnabled(final AccordionComponentType componentType, final By... accordionLocator) {
        Allure.step(String.format("[UI - Accordion] Checking if accordion %s is enabled using locators", componentType));
        LogUI.step("Checking if accordion {} is enabled using locators", componentType);
        return accordionComponent(componentType).areEnabled(accordionLocator);
    }

    @Override
    public List<String> getExpanded(final AccordionComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Accordion] Getting expanded accordions for %s", componentType));
        LogUI.step("Getting expanded accordions for {}", componentType);
        return accordionComponent(componentType).getExpanded(container);
    }

    @Override
    public List<String> getCollapsed(final AccordionComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Accordion] Getting collapsed accordions for %s", componentType));
        LogUI.step("Getting collapsed accordions for {}", componentType);
        return accordionComponent(componentType).getCollapsed(container);
    }

    @Override
    public List<String> getAll(final AccordionComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Accordion] Getting all accordions for %s", componentType));
        LogUI.step("Getting all accordions for {}", componentType);
        return accordionComponent(componentType).getAll(container);
    }

    @Override
    public String getTitle(final AccordionComponentType componentType, final By accordionLocator) {
        Allure.step(String.format("[UI - Accordion] Getting title for accordion %s using locator", componentType));
        LogUI.step("Getting title for accordion {} using locator", componentType);
        return accordionComponent(componentType).getTitle(accordionLocator);
    }

    @Override
    public String getText(final AccordionComponentType componentType, final By accordionLocator) {
        Allure.step(String.format("[UI - Accordion] Getting text for accordion %s using locator", componentType));
        LogUI.step("Getting text for accordion {} using locator", componentType);
        return accordionComponent(componentType).getText(accordionLocator);
    }

    private Accordion accordionComponent(final AccordionComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
