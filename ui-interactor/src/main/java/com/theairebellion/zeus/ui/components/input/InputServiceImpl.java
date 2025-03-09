package com.theairebellion.zeus.ui.components.input;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import io.qameta.allure.Allure;
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
        Allure.step(String.format("[UI - Input] Inserting value '%s' into input component of type '%s'", value, componentType));
        LogUI.step("Inserting value: '{}' into input component of type: '{}'.", value, componentType.getType().name());
        getOrCreateComponent(componentType).insert(container, value);
    }

    @Override
    public void insert(final InputComponentType componentType, final SmartWebElement container,
                       final String inputFieldLabel, final String value) {
        Allure.step(String.format("[UI - Input] Inserting value '%s' into input field labeled '%s' for component type '%s'",
                value, inputFieldLabel, componentType));
        LogUI.step("Inserting value: '{}' into input field labeled: '{}' of type: '{}'.", value, inputFieldLabel,
                componentType.getType().name());
        getOrCreateComponent(componentType).insert(container, inputFieldLabel, value);
    }

    @Override
    public void insert(final InputComponentType componentType, final String inputFieldLabel, final String value) {
        Allure.step(String.format("[UI - Input] Inserting value '%s' into input field labeled '%s' for component type '%s'",
                value, inputFieldLabel, componentType));
        LogUI.step("Inserting value: '{}' into input field labeled: '{}' of type: '{}'.", value, inputFieldLabel,
                componentType.getType().name());
        getOrCreateComponent(componentType).insert(inputFieldLabel, value);
    }

    @Override
    public void insert(final InputComponentType componentType, final By inputFieldContainerLocator,
                       final String value) {
        Allure.step(String.format("[UI - Input] Inserting value '%s' into input component of type '%s' with container locator '%s'.",
                value, componentType, inputFieldContainerLocator));
        LogUI.step("Inserting value: '{}' into input component of type: '{}'.", value, componentType.getType().name());
        getOrCreateComponent(componentType).insert(inputFieldContainerLocator, value);
    }

    @Override
    public void clear(final InputComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Input] Clearing value in input component of type '%s'", componentType));
        LogUI.step("Clearing value in input component of type: '{}'.", componentType.getType().name());
        getOrCreateComponent(componentType).clear(container);
    }

    @Override
    public void clear(final InputComponentType componentType, final SmartWebElement container,
                      final String inputFieldLabel) {
        Allure.step(String.format("[UI - Input] Clearing value in input field labeled '%s' for component type '%s'",
                inputFieldLabel, componentType));
        LogUI.step("Clearing value in input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        getOrCreateComponent(componentType).clear(container, inputFieldLabel);
    }

    @Override
    public void clear(final InputComponentType componentType, final String inputFieldLabel) {
        Allure.step(String.format("[UI - Input] Clearing value in input field labeled '%s' for component type '%s'",
                inputFieldLabel, componentType));
        LogUI.step("Clearing value in input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        getOrCreateComponent(componentType).clear(inputFieldLabel);
    }

    @Override
    public void clear(final InputComponentType componentType, final By inputFieldContainerLocator) {
        Allure.step(String.format("[UI - Input] Clearing value using locator '%s' in input component of type '%s'",
                inputFieldContainerLocator, componentType));
        LogUI.step("Clearing value in input component of type: '{}'.", componentType.getType().name());
        getOrCreateComponent(componentType).clear(inputFieldContainerLocator);
    }

    @Override
    public String getValue(final InputComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Input] Fetching value from input component of type '%s'", componentType));
        LogUI.step("Fetching value from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(container);
    }

    @Override
    public String getValue(final InputComponentType componentType, final SmartWebElement container,
                           final String inputFieldLabel) {
        Allure.step(String.format("[UI - Input] Fetching value from input field labeled '%s' for component type '%s'",
                inputFieldLabel, componentType));
        LogUI.step("Fetching value from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(container);
    }

    @Override
    public String getValue(final InputComponentType componentType, final String inputFieldLabel) {
        Allure.step(String.format("[UI - Input] Fetching value from input field labeled '%s' for component type '%s'",
                inputFieldLabel, componentType));
        LogUI.step("Fetching value from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(inputFieldLabel);
    }

    @Override
    public String getValue(final InputComponentType componentType, final By inputFieldContainerLocator) {
        Allure.step(String.format("[UI - Input] Fetching value using locator '%s' from input component of type '%s'",
                inputFieldContainerLocator, componentType));
        LogUI.step("Fetching value from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getValue(inputFieldContainerLocator);
    }

    @Override
    public boolean isEnabled(final InputComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Input] Checking if input component of type '%s' is enabled", componentType));
        LogUI.step("Checking if input component of type: '{}' is enabled.", componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(container);
    }

    @Override
    public boolean isEnabled(final InputComponentType componentType, final SmartWebElement container, final String inputFieldLabel) {
        Allure.step(String.format("[UI - Input] Checking if input field labeled '%s' for component type '%s' is enabled",
                inputFieldLabel, componentType));
        LogUI.step("Checking if input field labeled: '{}' of type: '{}' is enabled.", inputFieldLabel,
                componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(container, inputFieldLabel);
    }

    @Override
    public boolean isEnabled(final InputComponentType componentType, final String inputFieldLabel) {
        Allure.step(String.format("[UI - Input] Checking if input field labeled '%s' for component type '%s' is enabled",
                inputFieldLabel, componentType));
        LogUI.step("Checking if input field labeled: '{}' of type: '{}' is enabled.", inputFieldLabel,
                componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(inputFieldLabel);
    }

    @Override
    public boolean isEnabled(final InputComponentType componentType, final By inputFieldContainerLocator) {
        Allure.step(String.format("[UI - Input] Checking if input component using locator '%s' for type '%s' is enabled",
                inputFieldContainerLocator, componentType));
        LogUI.step("Checking if input component of type: '{}' is enabled.", componentType.getType().name());
        return getOrCreateComponent(componentType).isEnabled(inputFieldContainerLocator);
    }

    @Override
    public String getErrorMessage(final InputComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Input] Fetching error message from input component of type '%s'", componentType));
        LogUI.step("Fetching error message from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(container);
    }

    @Override
    public String getErrorMessage(final InputComponentType componentType, final SmartWebElement container,
                                  final String inputFieldLabel) {
        Allure.step(String.format("[UI - Input] Fetching error message from input field labeled '%s' for component type '%s'",
                inputFieldLabel, componentType));
        LogUI.step("Fetching error message from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(container, inputFieldLabel);
    }

    @Override
    public String getErrorMessage(final InputComponentType componentType, final String inputFieldLabel) {
        Allure.step(String.format("[UI - Input] Fetching error message from input field labeled '%s' for component type '%s'",
                inputFieldLabel, componentType));
        LogUI.step("Fetching error message from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(inputFieldLabel);
    }

    @Override
    public String getErrorMessage(final InputComponentType componentType, final By inputFieldContainerLocator) {
        Allure.step(String.format("[UI - Input] Fetching error message using locator '%s' from input component of type '%s'",
                inputFieldContainerLocator, componentType));
        LogUI.step("Fetching error message from input component of type: '{}'.", componentType.getType().name());
        return getOrCreateComponent(componentType).getErrorMessage(inputFieldContainerLocator);
    }

    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        LogUI.step("Inserting values into table cell for component type: '{}'.", componentType.getType().name());
        getOrCreateComponent((InputComponentType) componentType).tableInsertion(cellElement, values);
    }

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
