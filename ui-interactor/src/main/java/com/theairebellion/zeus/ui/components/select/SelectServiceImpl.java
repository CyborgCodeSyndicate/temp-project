package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the {@link SelectService} interface, providing methods to select options,
 * retrieve available and selected options, and determine if certain options are visible or enabled.
 * Extends {@link AbstractComponentService} to handle the creation and retrieval of {@link Select}
 * instances.
 *
 * <p>This class relies on the {@link SelectComponentType} to identify the correct component for
 * select-type elements, allowing test automation to manage dropdowns, multi-select fields, or
 * similar widgets in a consistent manner.</p>
 *
 * @author Cyborg Code Syndicate
 */
public class SelectServiceImpl extends AbstractComponentService<SelectComponentType, Select> implements SelectService {

    /**
     * Constructs a new {@code SelectServiceImpl} with the specified {@link SmartWebDriver}.
     *
     * @param driver the smart web driver for interacting with browser elements.
     */
    public SelectServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    /**
     * Creates and returns a {@link Select} component instance based on the provided
     * {@link SelectComponentType}.
     *
     * @param componentType the select component type.
     * @return a new or existing {@link Select} instance.
     */
    @Override
    protected Select createComponent(final SelectComponentType componentType) {
        return ComponentFactory.getSelectComponent(componentType, driver);
    }

    /**
     * Selects one or more options by text or value within a container.
     *
     * @param componentType the select component type.
     * @param container     the container holding the select element.
     * @param values        one or more option values to select.
     */
    @Override
    public void selectOptions(final SelectComponentType componentType, final SmartWebElement container,
                              final String... values) {
        selectComponent(componentType).selectOptions(container, values);
    }

    /**
     * Selects a single option by text or value within a container. Delegates to
     * {@link #selectOptions(SelectComponentType, SmartWebElement, String...)}.
     *
     * @param componentType the select component type.
     * @param container     the container holding the select element.
     * @param value         the value of the option to select.
     */
    @Override
    public void selectOption(SelectComponentType componentType, SmartWebElement container, String value) {
        selectOptions(componentType, container, value);
    }

    /**
     * Selects one or more options within a container identified by a locator.
     *
     * @param componentType    the select component type.
     * @param containerLocator the locator for the container.
     * @param values           one or more option values to select.
     */
    @Override
    public void selectOptions(final SelectComponentType componentType, final By containerLocator,
                              final String... values) {
        selectComponent(componentType).selectOptions(containerLocator, values);
    }

    /**
     * Selects a single option by text or value within a container identified by a locator.
     * Delegates to {@link #selectOptions(SelectComponentType, By, String...)}.
     *
     * @param componentType    the select component type.
     * @param containerLocator the locator for the container.
     * @param value            the value of the option to select.
     */
    @Override
    public void selectOption(final SelectComponentType componentType, final By containerLocator,
                             final String value) {
        selectOptions(componentType, containerLocator, value);
    }

    /**
     * Selects options using a custom {@link Strategy}, returning the list of newly selected
     * option texts.
     *
     * @param componentType the select component type.
     * @param container     the container holding the select element.
     * @param strategy      the strategy to apply.
     * @return a list of texts representing the selected options.
     */
    @Override
    public List<String> selectOptions(final SelectComponentType componentType, final SmartWebElement container,
                                      final Strategy strategy) {
        return selectComponent(componentType).selectOptions(container, strategy);
    }

    /**
     * Selects options using a custom {@link Strategy} within a container identified by a locator,
     * returning the list of newly selected option texts.
     *
     * @param componentType    the select component type.
     * @param containerLocator the locator for the container.
     * @param strategy         the strategy to apply.
     * @return a list of texts representing the selected options.
     */
    @Override
    public List<String> selectOptions(final SelectComponentType componentType, final By containerLocator,
                                      final Strategy strategy) {
        return selectComponent(componentType).selectOptions(containerLocator, strategy);
    }

    /**
     * Retrieves all available options in a select component within the specified container.
     *
     * @param componentType the select component type.
     * @param container     the container holding the select element.
     * @return a list of all available option texts.
     */
    @Override
    public List<String> getAvailableOptions(final SelectComponentType componentType, final SmartWebElement container) {
        return selectComponent(componentType).getAvailableOptions(container);
    }

    /**
     * Retrieves all available options in a select component, identified by a container locator.
     *
     * @param componentType    the select component type.
     * @param containerLocator the locator for the container.
     * @return a list of all available option texts.
     */
    @Override
    public List<String> getAvailableOptions(final SelectComponentType componentType, final By containerLocator) {
        return selectComponent(componentType).getAvailableOptions(containerLocator);
    }

    /**
     * Retrieves all currently selected options within a container holding a select component.
     *
     * @param componentType the select component type.
     * @param container     the container holding the select element.
     * @return a list of texts representing the selected options.
     */
    @Override
    public List<String> getSelectedOptions(final SelectComponentType componentType, final SmartWebElement container) {
        return selectComponent(componentType).getSelectedOptions(container);
    }

    /**
     * Retrieves all currently selected options from a select component, identified by a container locator.
     *
     * @param componentType    the select component type.
     * @param containerLocator the locator for the container.
     * @return a list of texts representing the selected options.
     */
    @Override
    public List<String> getSelectedOptions(final SelectComponentType componentType, final By containerLocator) {
        return selectComponent(componentType).getSelectedOptions(containerLocator);
    }

    /**
     * Checks if a specific option is visible in the select component within the given container.
     *
     * @param componentType the select component type.
     * @param container     the container holding the select element.
     * @param value         the text or value of the option.
     * @return true if the option is visible, otherwise false.
     */
    @Override
    public boolean isOptionVisible(final SelectComponentType componentType, final SmartWebElement container,
                                   final String value) {
        return selectComponent(componentType).isOptionVisible(container, value);
    }

    /**
     * Checks if a specific option is visible in the select component identified by a container locator.
     *
     * @param componentType    the select component type.
     * @param containerLocator the locator for the container.
     * @param value            the text or value of the option.
     * @return true if the option is visible, otherwise false.
     */
    @Override
    public boolean isOptionVisible(final SelectComponentType componentType, final By containerLocator,
                                   final String value) {
        return selectComponent(componentType).isOptionVisible(containerLocator, value);
    }

    /**
     * Checks if a specific option is enabled in the select component within the given container.
     *
     * @param componentType the select component type.
     * @param container     the container holding the select element.
     * @param value         the text or value of the option.
     * @return true if the option is enabled, otherwise false.
     */
    @Override
    public boolean isOptionEnabled(final SelectComponentType componentType, final SmartWebElement container,
                                   final String value) {
        return selectComponent(componentType).isOptionEnabled(container, value);
    }

    /**
     * Checks if a specific option is enabled in the select component identified by a container locator.
     *
     * @param componentType    the select component type.
     * @param containerLocator the locator for the container.
     * @param value            the text or value of the option.
     * @return true if the option is enabled, otherwise false.
     */
    @Override
    public boolean isOptionEnabled(final SelectComponentType componentType, final By containerLocator,
                                   final String value) {
        return selectComponent(componentType).isOptionEnabled(containerLocator, value);
    }

    /**
     * Inserts data by selecting one or more options based on the provided array of values.
     * This supports the {@link com.theairebellion.zeus.ui.insertion.Insertion} contract.
     *
     * @param componentType the component type (expected to be a {@link SelectComponentType}).
     * @param locator       the locator identifying the container.
     * @param values        the values to be selected.
     */
    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        String[] stringValues = Arrays.stream(values)
                .map(String::valueOf)
                .toArray(String[]::new);
        selectOptions((SelectComponentType) componentType, locator, stringValues);
    }

    /**
     * Retrieves the underlying {@link Select} instance for the specified component type.
     *
     * @param componentType the select component type.
     * @return the {@link Select} instance handling operations on this component type.
     */
    private Select selectComponent(final SelectComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
