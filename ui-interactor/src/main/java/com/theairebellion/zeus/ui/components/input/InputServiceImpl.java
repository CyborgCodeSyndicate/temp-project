package com.theairebellion.zeus.ui.components.input;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class InputServiceImpl extends AbstractComponentService<InputComponentType, Input> implements InputService {

    public InputServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Input createComponent(final InputComponentType componentType) {
        return ComponentFactory.getInputComponent(componentType, driver);
    }

    @Step("[UI - Accordion] Inserting value '{value}' into input component of type '{componentType}'")
    @Override
    public void insert(final InputComponentType componentType, final SmartWebElement container, final String value) {
        LogUI.step("Inserting value: '{}' into input component of type: '{}'.", value, componentType.getType().name());
        getOrCreateComponent(componentType).insert(container, value);
    }

    @Step("[UI - Accordion] Inserting value '{value}' into input field labeled '{inputFieldLabel}' for component type '{componentType}'")
    @Override
    public void insert(final InputComponentType componentType, final SmartWebElement container,
                       final String inputFieldLabel, final String value) {
        LogUI.step("Inserting value: '{}' into input field labeled: '{}' of type: '{}'.", value, inputFieldLabel,
                componentType.getType().name());
        getOrCreateComponent(componentType).insert(container, inputFieldLabel, value);
    }

    @Step("[UI - Accordion] Inserting value '{value}' into input field labeled '{inputFieldLabel}' for component type '{componentType}'")
    @Override
    public void insert(final InputComponentType componentType, final String inputFieldLabel, final String value) {
        LogUI.step("Inserting value: '{}' into input field labeled: '{}' of type: '{}'.", value, inputFieldLabel,
                componentType.getType().name());
        getOrCreateComponent(componentType).insert(inputFieldLabel, value);
    }

    @Step("[UI - Accordion] Inserting value '{value}' using locator '{inputFieldContainerLocator}' into input component of type '{componentType}'")
    @Override
    public void insert(final InputComponentType componentType, final By inputFieldContainerLocator,
                       final String value) {
        LogUI.step("Inserting value: '{}' into input component of type: '{}'.", value, componentType.getType().name());
        getOrCreateComponent(componentType).insert(inputFieldContainerLocator, value);
    }

    @Step("[UI - Accordion] Clearing value in input component of type '{componentType}'")
    @Override
    public void clear(final InputComponentType componentType, final SmartWebElement container) {
        LogUI.step("Clearing value in input component of type: '{}'.", componentType.getType().name());
        getOrCreateComponent(componentType).clear(container);
    }

    @Step("[UI - Accordion] Clearing value in input field labeled '{inputFieldLabel}' for component type '{componentType}'")
    @Override
    public void clear(final InputComponentType componentType, final SmartWebElement container,
                      final String inputFieldLabel) {
        LogUI.step("Clearing value in input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        getOrCreateComponent(componentType).clear(container, inputFieldLabel);
    }

    @Step("[UI - Accordion] Clearing value in input field labeled '{inputFieldLabel}' for component type '{componentType}'")
    @Override
    public void clear(final InputComponentType componentType, final String inputFieldLabel) {
        LogUI.step("Clearing value in input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        getOrCreateComponent(componentType).clear(inputFieldLabel);
    }

    @Step("[UI - Accordion] Clearing value using locator '{inputFieldContainerLocator}' in input component of type '{componentType}'")
    @Override
    public void clear(final InputComponentType componentType, final By inputFieldContainerLocator) {
        LogUI.step("Clearing value in input component of type: '{}'.", componentType.getType().name());
        getOrCreateComponent(componentType).clear(inputFieldContainerLocator);
    }

    @Step("[UI - Accordion] Fetching value from input component of type '{componentType}'")
    @Override
    public String getValue(final InputComponentType componentType, final SmartWebElement container) {
        LogUI.step("Fetching value from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(container);
    }

    @Step("[UI - Accordion] Fetching value from input field labeled '{inputFieldLabel}' for component type '{componentType}'")
    @Override
    public String getValue(final InputComponentType componentType, final SmartWebElement container,
                           final String inputFieldLabel) {
        LogUI.step("Fetching value from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(container);
    }

    @Step("[UI - Accordion] Fetching value from input field labeled '{inputFieldLabel}' for component type '{componentType}'")
    @Override
    public String getValue(final InputComponentType componentType, final String inputFieldLabel) {
        LogUI.step("Fetching value from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(inputFieldLabel);
    }

    @Step("[UI - Accordion] Fetching value using locator '{inputFieldContainerLocator}' from input component of type '{componentType}'")
    @Override
    public String getValue(final InputComponentType componentType, final By inputFieldContainerLocator) {
        LogUI.step("Fetching value from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(inputFieldContainerLocator);
    }

    @Step("[UI - Accordion] Checking if input component of type '{componentType}' is enabled")
    @Override
    public boolean isEnabled(final InputComponentType componentType, final SmartWebElement container) {
        LogUI.step("Checking if input component of type: '{}' is enabled.", componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(container);
    }

    @Step("[UI - Accordion] Checking if input field labeled '{inputFieldLabel}' for component type '{componentType}' is enabled")
    @Override
    public boolean isEnabled(final InputComponentType componentType, final SmartWebElement container, final String inputFieldLabel) {
        LogUI.step("Checking if input field labeled: '{}' of type: '{}' is enabled.", inputFieldLabel,
                componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(container, inputFieldLabel);
    }

    @Step("[UI - Accordion] Checking if input field labeled '{inputFieldLabel}' for component type '{componentType}' is enabled")
    @Override
    public boolean isEnabled(final InputComponentType componentType, final String inputFieldLabel) {
        LogUI.step("Checking if input field labeled: '{}' of type: '{}' is enabled.", inputFieldLabel,
                componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(inputFieldLabel);
    }

    @Step("[UI - Accordion] Checking if input component using locator '{inputFieldContainerLocator}' for type '{componentType}' is enabled")
    @Override
    public boolean isEnabled(final InputComponentType componentType, final By inputFieldContainerLocator) {
        LogUI.step("Checking if input component of type: '{}' is enabled.", componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(inputFieldContainerLocator);
    }

    @Step("[UI - Accordion] Fetching error message from input component of type '{componentType}'")
    @Override
    public String getErrorMessage(final InputComponentType componentType, final SmartWebElement container) {
        LogUI.step("Fetching error message from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(container);
    }

    @Step("[UI - Accordion] Fetching error message from input field labeled '{inputFieldLabel}' for component type '{componentType}'")
    @Override
    public String getErrorMessage(final InputComponentType componentType, final SmartWebElement container,
                                  final String inputFieldLabel) {
        LogUI.step("Fetching error message from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(container, inputFieldLabel);
    }

    @Step("[UI - Accordion] Fetching error message from input field labeled '{inputFieldLabel}' for component type '{componentType}'")
    @Override
    public String getErrorMessage(final InputComponentType componentType, final String inputFieldLabel) {
        LogUI.step("Fetching error message from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(inputFieldLabel);
    }

    @Step("[UI - Accordion] Fetching error message using locator '{inputFieldContainerLocator}' from input component of type '{componentType}'")
    @Override
    public String getErrorMessage(final InputComponentType componentType, final By inputFieldContainerLocator) {
        LogUI.step("Fetching error message from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(inputFieldContainerLocator);
    }

    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        LogUI.step("Inserting values into table cell for component type: '{}'.", componentType.getType().name());
        getOrCreateComponent((InputComponentType) componentType).tableInsertion(cellElement, values);
    }

    @Step("[UI - Accordion] Applying table filter in cell for component type '{componentType}' with strategy '{filterStrategy}' and values {values}")
    @Override
    public void tableFilter(final SmartWebElement cellElement, final ComponentType componentType,
                            final FilterStrategy filterStrategy,
                            final String... values) {
        LogUI.step("Applying table filter for component type: '{}' with strategy: '{}'.", componentType.getType().name(), filterStrategy);
        getOrCreateComponent((InputComponentType) componentType).tableFilter(cellElement, filterStrategy, values);
    }

    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        LogUI.step("Inserting value into component of type: '{}' using locator.", componentType.getType().name());
        insert((InputComponentType) componentType, locator, (String) values[0]);
    }

    private Input inputComponent(final InputComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
