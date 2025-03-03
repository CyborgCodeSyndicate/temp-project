package com.theairebellion.zeus.ui.components.input;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class InputServiceImpl extends AbstractComponentService<InputComponentType, Input> implements InputService {


    public InputServiceImpl(SmartWebDriver driver) {
        super(driver);
    }


    @Override
    protected Input createComponent(final InputComponentType componentType) {
        return ComponentFactory.getInputComponent(componentType, driver);
    }


    @Override
    public void insert(final InputComponentType componentType, final SmartWebElement container, final String value) {
        LogUI.info("Inserting value: '{}' into input component of type: '{}'.", value, componentType.getType().name());
        getOrCreateComponent(componentType).insert(container, value);
    }


    @Override
    public void insert(final InputComponentType componentType, final SmartWebElement container,
                       final String inputFieldLabel, final String value) {
        LogUI.info("Inserting value: '{}' into input field labeled: '{}' of type: '{}'.", value, inputFieldLabel,
            componentType.getType().name());
        getOrCreateComponent(componentType).insert(container, inputFieldLabel, value);
    }


    @Override
    public void insert(final InputComponentType componentType, final String inputFieldLabel, final String value) {
        LogUI.info("Inserting value: '{}' into input field labeled: '{}' of type: '{}'.", value, inputFieldLabel,
            componentType.getType().name());
        getOrCreateComponent(componentType).insert(inputFieldLabel, value);
    }


    @Override
    public void insert(final InputComponentType componentType, final By inputFieldContainerLocator,
                       final String value) {
        LogUI.info("Inserting value: '{}' into input component of type: '{}'.", value, componentType.getType().name());
        getOrCreateComponent(componentType).insert(inputFieldContainerLocator, value);
    }


    @Override
    public void clear(final InputComponentType componentType, final SmartWebElement container) {
        LogUI.info("Clearing value in input component of type: '{}'.", componentType.getType().name());
        getOrCreateComponent(componentType).clear(container);
    }


    @Override
    public void clear(final InputComponentType componentType, final SmartWebElement container,
                      final String inputFieldLabel) {
        LogUI.info("Clearing value in input field labeled: '{}' of type: '{}'.", inputFieldLabel,
            componentType.getType().name());
        getOrCreateComponent(componentType).clear(container, inputFieldLabel);
    }


    @Override
    public void clear(final InputComponentType componentType, final String inputFieldLabel) {
        LogUI.info("Clearing value in input field labeled: '{}' of type: '{}'.", inputFieldLabel,
            componentType.getType().name());
        getOrCreateComponent(componentType).clear(inputFieldLabel);
    }


    @Override
    public void clear(final InputComponentType componentType, final By inputFieldContainerLocator) {
        LogUI.info("Clearing value in input component of type: '{}'.", componentType.getType().name());
        getOrCreateComponent(componentType).clear(inputFieldContainerLocator);
    }


    @Override
    public String getValue(final InputComponentType componentType, final SmartWebElement container) {
        LogUI.info("Fetching value from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(container);
    }


    @Override
    public String getValue(final InputComponentType componentType, final SmartWebElement container,
                           final String inputFieldLabel) {
        LogUI.info("Fetching value from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
            componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(container);
    }


    @Override
    public String getValue(final InputComponentType componentType, final String inputFieldLabel) {
        LogUI.info("Fetching value from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
            componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(inputFieldLabel);
    }


    @Override
    public String getValue(final InputComponentType componentType, final By inputFieldContainerLocator) {
        LogUI.info("Fetching value from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(inputFieldContainerLocator);
    }


    @Override
    public boolean isEnabled(final InputComponentType componentType, final SmartWebElement container) {
        LogUI.info("Checking if input component of type: '{}' is enabled.", componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(container);
    }


    @Override
    public boolean isEnabled(final InputComponentType componentType, final SmartWebElement container, final String inputFieldLabel) {
        LogUI.info("Checking if input field labeled: '{}' of type: '{}' is enabled.", inputFieldLabel,
            componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(container, inputFieldLabel);
    }


    @Override
    public boolean isEnabled(final InputComponentType componentType, final String inputFieldLabel) {
        LogUI.info("Checking if input field labeled: '{}' of type: '{}' is enabled.", inputFieldLabel,
            componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(inputFieldLabel);
    }


    @Override
    public boolean isEnabled(final InputComponentType componentType, final By inputFieldContainerLocator) {
        LogUI.info("Checking if input component of type: '{}' is enabled.", componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(inputFieldContainerLocator);
    }


    @Override
    public String getErrorMessage(final InputComponentType componentType, final SmartWebElement container) {
        LogUI.info("Fetching error message from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(container);
    }


    @Override
    public String getErrorMessage(final InputComponentType componentType, final SmartWebElement container,
                                  final String inputFieldLabel) {
        LogUI.info("Fetching error message from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
            componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(container, inputFieldLabel);
    }


    @Override
    public String getErrorMessage(final InputComponentType componentType, final String inputFieldLabel) {
        LogUI.info("Fetching error message from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
            componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(inputFieldLabel);
    }


    @Override
    public String getErrorMessage(final InputComponentType componentType, final By inputFieldContainerLocator) {
        LogUI.info("Fetching error message from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(inputFieldContainerLocator);
    }


    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        getOrCreateComponent((InputComponentType) componentType).tableInsertion(cellElement, values);
    }


    @Override
    public void tableFilter(final SmartWebElement cellElement, final ComponentType componentType,
                            final FilterStrategy filterStrategy,
                            final String... values) {
        getOrCreateComponent((InputComponentType) componentType).tableFilter(cellElement, filterStrategy, values);
    }


    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        insert((InputComponentType) componentType, locator, (String) values[0]);
    }


    private Input inputComponent(final InputComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
