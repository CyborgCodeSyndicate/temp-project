package com.theairebellion.zeus.ui.components.accordion.mock;

import com.theairebellion.zeus.ui.components.accordion.AccordionComponentType;
import com.theairebellion.zeus.ui.components.accordion.AccordionService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

public class MockAccordionService implements AccordionService {

    public static final String EXPAND_STRATEGY_RESULT = "expandedStrategy";
    public static final String COLLAPSE_STRATEGY_RESULT = "collapsedStrategy";
    public static final List<String> EXPANDED_LIST = List.of("expanded");
    public static final List<String> COLLAPSED_LIST = List.of("collapsed");
    public static final List<String> ALL_LIST = List.of("all");
    public static final String TITLE = "title";
    public static final String TEXT = "text";

    public MockAccordionComponentType lastComponentType;
    public SmartWebElement lastContainer;
    public String[] lastAccordionText;
    public By[] lastAccordionLocators;
    public Strategy lastStrategy;

    public void reset() {
        lastComponentType = MockAccordionComponentType.DUMMY;
        lastContainer = null;
        lastAccordionText = null;
        lastAccordionLocators = null;
        lastStrategy = null;
    }

    @Override
    public void expand(AccordionComponentType componentType, SmartWebElement container, String... accordionText) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastContainer = container;
        lastAccordionText = accordionText;
    }

    @Override
    public String expand(AccordionComponentType componentType, SmartWebElement container, Strategy strategy) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastContainer = container;
        lastStrategy = strategy;
        return EXPAND_STRATEGY_RESULT;
    }

    @Override
    public void expand(AccordionComponentType componentType, String... accordionText) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastAccordionText = accordionText;
    }

    @Override
    public void expand(AccordionComponentType componentType, By... accordionLocator) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastAccordionLocators = accordionLocator;
    }

    @Override
    public void collapse(AccordionComponentType componentType, SmartWebElement container, String... accordionText) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastContainer = container;
        lastAccordionText = accordionText;
    }

    @Override
    public String collapse(AccordionComponentType componentType, SmartWebElement container, Strategy strategy) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastContainer = container;
        lastStrategy = strategy;
        return COLLAPSE_STRATEGY_RESULT;
    }

    @Override
    public void collapse(AccordionComponentType componentType, String... accordionText) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastAccordionText = accordionText;
    }

    @Override
    public void collapse(AccordionComponentType componentType, By... accordionLocator) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastAccordionLocators = accordionLocator;
    }

    @Override
    public boolean areEnabled(AccordionComponentType componentType, SmartWebElement container, String... accordionText) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastContainer = container;
        lastAccordionText = accordionText;
        return true;
    }

    @Override
    public boolean areEnabled(AccordionComponentType componentType, String... accordionText) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastAccordionText = accordionText;
        return true;
    }

    @Override
    public boolean areEnabled(AccordionComponentType componentType, By... accordionLocator) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastAccordionLocators = accordionLocator;
        return true;
    }

    @Override
    public List<String> getExpanded(AccordionComponentType componentType, SmartWebElement container) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastContainer = container;
        return EXPANDED_LIST;
    }

    @Override
    public List<String> getCollapsed(AccordionComponentType componentType, SmartWebElement container) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastContainer = container;
        return COLLAPSED_LIST;
    }

    @Override
    public List<String> getAll(AccordionComponentType componentType, SmartWebElement container) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastContainer = container;
        return ALL_LIST;
    }

    @Override
    public String getTitle(AccordionComponentType componentType, By accordionLocator) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastAccordionLocators = new By[]{accordionLocator};
        return TITLE;
    }

    @Override
    public String getText(AccordionComponentType componentType, By accordionLocator) {
        lastComponentType = (MockAccordionComponentType) componentType;
        lastAccordionLocators = new By[]{accordionLocator};
        return TEXT;
    }
}

