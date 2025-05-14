package com.theairebellion.zeus.ui.components.input;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.components.table.filters.FilterStrategy;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Provides service-level operations for interacting with input components.
 * <p>
 * This class manages interactions with input fields, such as inserting values,
 * clearing fields, retrieving values, checking states, and handling table-based operations.
 * The actual interactions are delegated to specific {@link Input} implementations based on
 * the provided {@link InputComponentType}.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class InputServiceImpl extends AbstractComponentService<InputComponentType, Input> implements InputService {

    /**
     * Constructs a new InputServiceImpl using the specified SmartWebDriver.
     *
     * @param driver the SmartWebDriver used for UI interactions.
     */
    public InputServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    /**
     * Creates an Input instance for the given component type.
     *
     * @param componentType the input component type.
     * @return a new or cached Input instance.
     */
    @Override
    protected Input createComponent(final InputComponentType componentType) {
        return ComponentFactory.getInputComponent(componentType, driver);
    }

    /**
     * Inserts a value into the specified input container.
     *
     * @param componentType the input component type.
     * @param container     the input field container.
     * @param value         the value to insert.
     */
    @Override
    public void insert(final InputComponentType componentType, final SmartWebElement container, final String value) {
        LogUi.step("Inserting value: '{}' into input component of type: '{}'.", value, componentType.getType().name());
        inputComponent(componentType).insert(container, value);
    }

    /**
     * Inserts a value into an input field within a container identified by its label.
     *
     * @param componentType   the input component type.
     * @param container       the input field container.
     * @param inputFieldLabel the label of the input field.
     * @param value           the value to insert.
     */
    @Override
    public void insert(final InputComponentType componentType, final SmartWebElement container,
                       final String inputFieldLabel, final String value) {
        LogUi.step("Inserting value: '{}' into input field labeled: '{}' of type: '{}'.", value, inputFieldLabel,
                componentType.getType().name());
        inputComponent(componentType).insert(container, inputFieldLabel, value);
    }

    /**
     * Inserts a value into an input field identified by its label.
     *
     * @param componentType   the input component type.
     * @param inputFieldLabel the label of the input field.
     * @param value           the value to insert.
     */
    @Override
    public void insert(final InputComponentType componentType, final String inputFieldLabel, final String value) {
        LogUi.step("Inserting value: '{}' into input field labeled: '{}' of type: '{}'.", value, inputFieldLabel,
                componentType.getType().name());
        inputComponent(componentType).insert(inputFieldLabel, value);
    }

    /**
     * Inserts a value into an input field located by a specified locator.
     *
     * @param componentType              the input component type.
     * @param inputFieldContainerLocator the locator for the input field container.
     * @param value                      the value to insert.
     */
    @Override
    public void insert(final InputComponentType componentType, final By inputFieldContainerLocator,
                       final String value) {
        LogUi.step("Inserting value: '{}' into input component of type: '{}'.", value, componentType.getType().name());
        inputComponent(componentType).insert(inputFieldContainerLocator, value);
    }

    /**
     * Clears the value of the specified input container.
     *
     * @param componentType the input component type.
     * @param container     the input field container.
     */
    @Override
    public void clear(final InputComponentType componentType, final SmartWebElement container) {
        LogUi.step("Clearing value in input component of type: '{}'.", componentType.getType().name());
        inputComponent(componentType).clear(container);
    }

    /**
     * Clears the value of an input field within a container identified by its label.
     *
     * @param componentType   the input component type.
     * @param container       the input field container.
     * @param inputFieldLabel the label of the input field.
     */
    @Override
    public void clear(final InputComponentType componentType, final SmartWebElement container,
                      final String inputFieldLabel) {
        LogUi.step("Clearing value in input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        inputComponent(componentType).clear(container, inputFieldLabel);
    }

    /**
     * Clears the value from an input field identified by a label.
     *
     * @param componentType   the input component type.
     * @param inputFieldLabel the label of the input field to be cleared.
     */
    @Override
    public void clear(final InputComponentType componentType, final String inputFieldLabel) {
        LogUi.step("Clearing value in input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        inputComponent(componentType).clear(inputFieldLabel);
    }

    /**
     * Clears the value from an input field located by a specified locator.
     *
     * @param componentType              the input component type.
     * @param inputFieldContainerLocator the locator for the input field container to be cleared.
     */
    @Override
    public void clear(final InputComponentType componentType, final By inputFieldContainerLocator) {
        LogUi.step("Clearing value in input component of type: '{}'.", componentType.getType().name());
        inputComponent(componentType).clear(inputFieldContainerLocator);
    }

    /**
     * Retrieves the value from the specified input container.
     *
     * @param componentType the input component type.
     * @param container     the input field container.
     * @return the value from the input field.
     */
    @Override
    public String getValue(final InputComponentType componentType, final SmartWebElement container) {
        LogUi.step("Fetching value from input component of type: '{}'.", componentType.getType().name());
        return inputComponent(componentType).getValue(container);
    }

    /**
     * Retrieves the value from an input field identified by a label within a specified container.
     *
     * @param componentType   the input component type.
     * @param container       the container holding the input field.
     * @param inputFieldLabel the label of the input field.
     * @return the current value of the input field.
     */
    @Override
    public String getValue(final InputComponentType componentType, final SmartWebElement container,
                           final String inputFieldLabel) {
        LogUi.step("Fetching value from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        return inputComponent(componentType).getValue(container, inputFieldLabel);
    }

    /**
     * Retrieves the value from an input field identified by a label.
     *
     * @param componentType   the input component type.
     * @param inputFieldLabel the label of the input field.
     * @return the current value of the input field.
     */
    @Override
    public String getValue(final InputComponentType componentType, final String inputFieldLabel) {
        LogUi.step("Fetching value from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        return inputComponent(componentType).getValue(inputFieldLabel);
    }

    /**
     * Retrieves the value from an input field located by a specified locator.
     *
     * @param componentType              the input component type.
     * @param inputFieldContainerLocator the locator for the input field container.
     * @return the current value of the input field.
     */
    @Override
    public String getValue(final InputComponentType componentType, final By inputFieldContainerLocator) {
        LogUi.step("Fetching value from input component of type: '{}'.", componentType.getType().name());
        return inputComponent(componentType).getValue(inputFieldContainerLocator);
    }

    /**
     * Checks if the specified input field is enabled.
     *
     * @param componentType the input component type.
     * @param container     the input field container.
     * @return true if the field is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled(final InputComponentType componentType, final SmartWebElement container) {
        LogUi.step("Checking if input component of type: '{}' is enabled.", componentType.getType().name());
        return inputComponent(componentType).isEnabled(container);
    }

    /**
     * Checks if an input field identified by a label within a specified container is enabled.
     *
     * @param componentType   the input component type.
     * @param container       the container holding the input field.
     * @param inputFieldLabel the label of the input field.
     * @return {@code true} if the input field is enabled; {@code false} otherwise.
     */
    @Override
    public boolean isEnabled(final InputComponentType componentType, final SmartWebElement container,
                             final String inputFieldLabel) {
        LogUi.step("Checking if input field labeled: '{}' of type: '{}' is enabled.", inputFieldLabel,
                componentType.getType().name());
        return inputComponent(componentType).isEnabled(container, inputFieldLabel);
    }

    /**
     * Checks if an input field identified by a label is enabled.
     *
     * @param componentType   the input component type.
     * @param inputFieldLabel the label of the input field.
     * @return {@code true} if the input field is enabled; {@code false} otherwise.
     */
    @Override
    public boolean isEnabled(final InputComponentType componentType, final String inputFieldLabel) {
        LogUi.step("Checking if input field labeled: '{}' of type: '{}' is enabled.", inputFieldLabel,
                componentType.getType().name());
        return inputComponent(componentType).isEnabled(inputFieldLabel);
    }

    /**
     * Checks if an input field located by a specified locator is enabled.
     *
     * @param componentType              the input component type.
     * @param inputFieldContainerLocator the locator for the input field container.
     * @return {@code true} if the input field is enabled; {@code false} otherwise.
     */
    @Override
    public boolean isEnabled(final InputComponentType componentType, final By inputFieldContainerLocator) {
        LogUi.step("Checking if input component of type: '{}' is enabled.", componentType.getType().name());
        return inputComponent(componentType).isEnabled(inputFieldContainerLocator);
    }

    /**
     * Retrieves the error message from the specified input container.
     *
     * @param componentType the input component type.
     * @param container     the input field container.
     * @return the error message from the input field.
     */
    @Override
    public String getErrorMessage(final InputComponentType componentType, final SmartWebElement container) {
        LogUi.step("Fetching error message from input component of type: '{}'.", componentType.getType().name());
        return inputComponent(componentType).getErrorMessage(container);
    }

    /**
     * Retrieves the error message from an input field identified by a label within a specified container.
     *
     * @param componentType   the input component type.
     * @param container       the container holding the input field.
     * @param inputFieldLabel the label of the input field.
     * @return the error message associated with the input field, or an empty string if none exists.
     */
    @Override
    public String getErrorMessage(final InputComponentType componentType, final SmartWebElement container,
                                  final String inputFieldLabel) {
        LogUi.step("Fetching error message from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        return inputComponent(componentType).getErrorMessage(container, inputFieldLabel);
    }

    /**
     * Retrieves the error message from an input field identified by a label.
     *
     * @param componentType   the input component type.
     * @param inputFieldLabel the label of the input field.
     * @return the error message associated with the input field, or an empty string if none exists.
     */
    @Override
    public String getErrorMessage(final InputComponentType componentType, final String inputFieldLabel) {
        LogUi.step("Fetching error message from input field labeled: '{}' of type: '{}'.", inputFieldLabel,
                componentType.getType().name());
        return inputComponent(componentType).getErrorMessage(inputFieldLabel);
    }

    /**
     * Retrieves the error message from an input field located by a specified locator.
     *
     * @param componentType              the input component type.
     * @param inputFieldContainerLocator the locator for the input field container.
     * @return the error message associated with the input field, or an empty string if none exists.
     */
    @Override
    public String getErrorMessage(final InputComponentType componentType, final By inputFieldContainerLocator) {
        LogUi.step("Fetching error message from input component of type: '{}'.", componentType.getType().name());
        return inputComponent(componentType).getErrorMessage(inputFieldContainerLocator);
    }

    /**
     * Performs a table insertion action on a cell element using the input component.
     *
     * @param cellElement   the cell element where the input field is located.
     * @param componentType the input component type.
     * @param values        optional values for the insertion action.
     */
    @Override
    public void tableInsertion(final SmartWebElement cellElement, final ComponentType componentType,
                               final String... values) {
        if (!(componentType instanceof InputComponentType)) {
            throw new IllegalArgumentException("Component type needs to be from: InputComponentType.");
        }
        LogUi.step("Inserting values into table cell for component type: '{}'.", componentType.getType().name());
        inputComponent((InputComponentType) componentType).tableInsertion(cellElement, values);
    }

    /**
     * Performs a table filter operation using the input component.
     *
     * @param cellElement    the cell element where filtering is applied.
     * @param componentType  the input component type.
     * @param filterStrategy the filter strategy to apply.
     * @param values         optional values for the filtering action.
     */
    @Override
    public void tableFilter(final SmartWebElement cellElement, final ComponentType componentType,
                            final FilterStrategy filterStrategy,
                            final String... values) {
        if (!(componentType instanceof InputComponentType)) {
            throw new IllegalArgumentException("Component type needs to be from: InputComponentType.");
        }
        LogUi.step("Applying table filter for component type: '{}' with strategy: '{}'.",
                componentType.getType().name(), filterStrategy);
        inputComponent((InputComponentType) componentType).tableFilter(cellElement, filterStrategy, values);
    }

    /**
     * Performs an insertion operation using the input component.
     *
     * @param componentType the input component type.
     * @param locator       the locator of the input field.
     * @param values        optional values to insert.
     */
    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        if (!(componentType instanceof InputComponentType)) {
            throw new IllegalArgumentException("Component type needs to be from: InputComponentType.");
        }
        LogUi.step("Inserting value into component of type: '{}' using locator.", componentType.getType().name());
        insert((InputComponentType) componentType, locator, (String) values[0]);
    }

    /**
     * Retrieves the Input instance for the given component type.
     *
     * @param componentType the input component type.
     * @return the Input instance.
     */
    private Input inputComponent(final InputComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
