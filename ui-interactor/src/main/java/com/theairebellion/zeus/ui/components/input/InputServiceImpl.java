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
    protected Input createComponent(InputComponentType componentType) {
        return ComponentFactory.getInputComponent(componentType, driver);
    }


    @Override
    public void insert(final SmartWebElement container, final String value, final InputComponentType componentType) {
        LogUI.info("Inserting value: '{}' into input component of type: '{}'.", value, componentType.getType().name());
        getOrCreateComponent(componentType).insert(container, value);
    }


    @Override
    public void insert(final SmartWebElement container, final String inputFieldLabel, final String value,
                       final InputComponentType componentType) {
        LogUI.info("Inserting value: '{}' into input field labeled: '{}' of type: '{}'.", value, inputFieldLabel,
            componentType.getType().name());
        getOrCreateComponent(componentType).insert(container, inputFieldLabel, value);
    }


    @Override
    public void insert(final String inputFieldLabel, final String value, final InputComponentType componentType) {
        LogUI.info("Inserting value: '{}' into input field labeled: '{}' of type: '{}'.", value, inputFieldLabel,
            componentType.getType().name());
        getOrCreateComponent(componentType).insert(inputFieldLabel, value);
    }


    @Override
    public void insert(final By inputFieldContainerLocator, final String value,
                       final InputComponentType componentType) {
        LogUI.info("Inserting value: '{}' into input component of type: '{}'.", value, componentType.getType().name());
        getOrCreateComponent(componentType).insert(inputFieldContainerLocator, value);
    }


    @Override
    public void clear(final SmartWebElement container, final InputComponentType componentType) {
        LogUI.info("Clearing value in input component of type: '{}'.", componentType.getType().name());
        getOrCreateComponent(componentType).clear(container);
    }


    @Override
    public void clear(final SmartWebElement container, final String inputFieldLabel,
                      final InputComponentType componentType) {
        LogUI.info("Clearing value in input field labeled: '{}' of type: '{}'.", inputFieldLabel,
            componentType.getType().name());
        getOrCreateComponent(componentType).clear(container, inputFieldLabel);
    }


    @Override
    public void clear(final String inputFieldLabel, final InputComponentType componentType) {
        LogUI.info("Clearing value in input field labeled: '{}' of type: '{}'.", inputFieldLabel,
            componentType.getType().name());
        getOrCreateComponent(componentType).clear(inputFieldLabel);
    }


    @Override
    public void clear(final By inputFieldContainerLocator, final InputComponentType componentType) {
        LogUI.info("Clearing value in input component of type: '{}'.", componentType.getType().name());
        getOrCreateComponent(componentType).clear(inputFieldContainerLocator);
    }


    @Override
    public String getValue(final SmartWebElement container, final InputComponentType componentType) {
        LogUI.info("Fetching value from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(container);
    }


    @Override
    public String getValue(final SmartWebElement container, final String inputFieldLabel,
                           final InputComponentType componentType) {
        LogUI.info("Fetching value from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
            componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(container);
    }


    @Override
    public String getValue(final String inputFieldLabel, final InputComponentType componentType) {
        LogUI.info("Fetching value from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
            componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(inputFieldLabel);
    }


    @Override
    public String getValue(final By inputFieldContainerLocator, final InputComponentType componentType) {
        LogUI.info("Fetching value from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(inputFieldContainerLocator);
    }


    @Override
    public boolean isEnabled(final SmartWebElement container, final InputComponentType componentType) {
        LogUI.info("Checking if input component of type: '{}' is enabled.", componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(container);
    }


    @Override
    public boolean isEnabled(final SmartWebElement container, final String inputFieldLabel,
                             final InputComponentType componentType) {
        LogUI.info("Checking if input field labeled: '{}' of type: '{}' is enabled.", inputFieldLabel,
            componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(container, inputFieldLabel);
    }


    @Override
    public boolean isEnabled(final String inputFieldLabel, final InputComponentType componentType) {
        LogUI.info("Checking if input field labeled: '{}' of type: '{}' is enabled.", inputFieldLabel,
            componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(inputFieldLabel);
    }


    @Override
    public boolean isEnabled(final By inputFieldContainerLocator, final InputComponentType componentType) {
        LogUI.info("Checking if input component of type: '{}' is enabled.", componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(inputFieldContainerLocator);
    }


    @Override
    public String getErrorMessage(final SmartWebElement container, final InputComponentType componentType) {
        LogUI.info("Fetching error message from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(container);
    }


    @Override
    public String getErrorMessage(final SmartWebElement container, final String inputFieldLabel,
                                  final InputComponentType componentType) {
        LogUI.info("Fetching error message from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
            componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(container, inputFieldLabel);
    }


    @Override
    public String getErrorMessage(final String inputFieldLabel, final InputComponentType componentType) {
        LogUI.info("Fetching error message from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
            componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(inputFieldLabel);
    }


    @Override
    public String getErrorMessage(final By inputFieldContainerLocator, final InputComponentType componentType) {
        LogUI.info("Fetching error message from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(inputFieldContainerLocator);
    }


    @Override
    public void insertion(final By locator, ComponentType componentType, final Object... values) {
        insert(locator, (String) values[0], (InputComponentType) componentType);
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


}
