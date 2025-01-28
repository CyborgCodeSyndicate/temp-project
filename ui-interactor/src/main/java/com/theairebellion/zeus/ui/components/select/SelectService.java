package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.components.radio.RadioComponentType;
import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Interface defining operations for interacting with select components within a web interface using Selenium.
 */
public interface SelectService extends Insertion {

    UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);
    SelectComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Selects one or more items in a select component based on the given values. Uses the default select component type.
     *
     * @param mode      The mode of dropdown list to interact with.
     * @param container The WebElement representing the container of the select component.
     * @param values    The values of the items to be selected. Varargs argument to select multiple items.
     */
    default void selectItems(DdlMode mode, WebElement container, String... values) {
        selectItems(DEFAULT_TYPE, mode, container, values);
    }

    /**
     * Selects one or more items in a select component based on the given values.
     *
     * @param componentType The type of select component.
     * @param mode          The mode of dropdown list to interact with.
     * @param container     The WebElement representing the container of the select component.
     * @param values        The values of the items to be selected. Varargs argument to select multiple items.
     */
    void selectItems(SelectComponentType componentType, DdlMode mode, WebElement container, String... values);

    /**
     * Selects one or more items in a select component based on the given values using the default select component type.
     * The select component is identified by the provided container locator.
     *
     * @param mode             The mode of the dropdown list to interact with.
     * @param containerLocator The By locator that identifies the container of the select component.
     * @param values           The values of the items to be selected. Varargs argument to select multiple items.
     */
    default void selectItems(DdlMode mode, By containerLocator, String... values) {
        selectItems(DEFAULT_TYPE, mode, containerLocator, values);
    }

    /**
     * Selects one or more items in a select component based on the given values.
     * The select component is identified by the provided container locator and the specified component type.
     *
     * @param componentType    The type of select component.
     * @param mode             The mode of dropdown list to interact with.
     * @param containerLocator The By locator that identifies the container of the select component.
     * @param values           The values of the items to be selected. Varargs argument to select multiple items.
     */
    void selectItems(SelectComponentType componentType, DdlMode mode, By containerLocator, String... values);

    /**
     * Selects items in a select component based on a specific strategy. Uses the default select component type.
     *
     * @param mode      The mode of dropdown list to interact with.
     * @param container The WebElement representing the container of the select component.
     * @param strategy  The strategy to use for selecting items.
     * @return A List of Strings representing the values of the selected item(s).
     */
    default List<String> selectItems(DdlMode mode, WebElement container, Strategy strategy) {
        return selectItems(DEFAULT_TYPE, mode, container, strategy);
    }

    /**
     * Selects items in a select component based on a specific strategy.
     *
     * @param componentType The type of select component.
     * @param mode          The mode of dropdown list to interact with.
     * @param container     The WebElement representing the container of the select component.
     * @param strategy      The strategy to use for selecting items.
     * @return A List of Strings representing the values of the selected item(s).
     */
    List<String> selectItems(SelectComponentType componentType, DdlMode mode, WebElement container, Strategy strategy);

    /**
     * Selects items in a select component based on a specific strategy using the default select component type.
     * The select component is identified by the provided container locator.
     *
     * @param mode             The mode of the dropdown list to interact with.
     * @param containerLocator The By locator that identifies the container of the select component.
     * @param strategy         The strategy to use for selecting items.
     * @return A List of Strings representing the values of the selected item(s).
     */
    default List<String> selectItems(DdlMode mode, By containerLocator, Strategy strategy) {
        return selectItems(DEFAULT_TYPE, mode, containerLocator, strategy);
    }

    /**
     * Selects items in a select component based on a specific strategy.
     * The select component is identified by the provided container locator and the specified component type.
     *
     * @param componentType    The type of select component.
     * @param mode             The mode of dropdown list to interact with.
     * @param containerLocator The By locator that identifies the container of the select component.
     * @param strategy         The strategy to use for selecting items.
     * @return A List of Strings representing the values of the selected item(s).
     */
    List<String> selectItems(SelectComponentType componentType, DdlMode mode, By containerLocator, Strategy strategy);

    /**
     * Retrieves a list of available items in a select component. Uses the default select component type.
     *
     * @param container The WebElement representing the container of the select component.
     * @return A List of Strings representing the available items.
     */
    default List<String> getAvailableItems(WebElement container) {
        return getAvailableItems(DEFAULT_TYPE, container);
    }

    /**
     * Retrieves a list of available items in a select component.
     *
     * @param componentType The type of select component.
     * @param container     The WebElement representing the container of the select component.
     * @return A List of Strings representing the available items.
     */
    List<String> getAvailableItems(SelectComponentType componentType, WebElement container);

    /**
     * Retrieves a list of available items in a select component using the default select component type.
     * The select component is identified by the provided container locator.
     *
     * @param containerLocator The By locator that identifies the container of the select component.
     * @return A List of Strings representing the available items.
     */
    default List<String> getAvailableItems(By containerLocator) {
        return getAvailableItems(DEFAULT_TYPE, containerLocator);
    }

    /**
     * Retrieves a list of available items in a select component.
     * The select component is identified by the provided container locator and the specified component type.
     *
     * @param componentType    The type of select component.
     * @param containerLocator The By locator that identifies the container of the select component.
     * @return A List of Strings representing the available items.
     */
    List<String> getAvailableItems(SelectComponentType componentType, By containerLocator);

    /**
     * Retrieves a list of available items in a select component that match a given search term. Uses the default select component type.
     *
     * @param container The WebElement representing the container of the select component.
     * @param search    The search term to filter the available items.
     * @return A List of Strings representing the filtered available items.
     */
    default List<String> getAvailableItems(WebElement container, String search) {
        return getAvailableItems(DEFAULT_TYPE, container, search);
    }

    /**
     * Retrieves a list of available items in a select component that match a given search term.
     *
     * @param componentType The type of select component.
     * @param container     The WebElement representing the container of the select component.
     * @param search        The search term to filter the available items.
     * @return A List of Strings representing the filtered available items.
     */
    List<String> getAvailableItems(SelectComponentType componentType, WebElement container, String search);

    /**
     * Retrieves a list of available items in a select component that match a given search term.
     * The select component is identified by the provided container locator, using the default select component type.
     *
     * @param containerLocator The By locator that identifies the container of the select component.
     * @param search           The search term to filter the available items.
     * @return A List of Strings representing the filtered available items.
     */
    default List<String> getAvailableItems(By containerLocator, String search) {
        return getAvailableItems(DEFAULT_TYPE, containerLocator, search);
    }

    /**
     * Retrieves a list of available items in a select component that match a given search term.
     * The select component is identified by the provided container locator and the specified component type.
     *
     * @param componentType    The type of select component.
     * @param containerLocator The By locator that identifies the container of the select component.
     * @param search           The search term to filter the available items.
     * @return A List of Strings representing the filtered available items.
     */
    List<String> getAvailableItems(SelectComponentType componentType, By containerLocator, String search);

    /**
     * Retrieves a list of currently selected items in a select component. Uses the default select component type.
     *
     * @param container The WebElement representing the container of the select component.
     * @return A List of Strings representing the selected items.
     */
    default List<String> getSelectedItems(WebElement container) {
        return getSelectedItems(DEFAULT_TYPE, container);
    }

    /**
     * Retrieves a list of currently selected items in a select component.
     *
     * @param componentType The type of select component.
     * @param container     The WebElement representing the container of the select component.
     * @return A List of Strings representing the selected items.
     */
    List<String> getSelectedItems(SelectComponentType componentType, WebElement container);

    /**
     * Retrieves a list of currently selected items in a select component.
     * The select component is identified by the provided container locator, using the default select component type.
     *
     * @param containerLocator The By locator that identifies the container of the select component.
     * @return A List of Strings representing the selected items.
     */
    default List<String> getSelectedItems(By containerLocator) {
        return getSelectedItems(DEFAULT_TYPE, containerLocator);
    }

    /**
     * Retrieves a list of currently selected items in a select component.
     * The select component is identified by the provided container locator and the specified component type.
     *
     * @param componentType    The type of select component.
     * @param containerLocator The By locator that identifies the container of the select component.
     * @return A List of Strings representing the selected items.
     */
    List<String> getSelectedItems(SelectComponentType componentType, By containerLocator);

    /**
     * Checks if a specific option is visible in a select component. Uses the default select component type.
     *
     * @param container The WebElement representing the container of the select component.
     * @param value     The value of the option to check.
     * @return true if the option is present, false otherwise.
     */
    default boolean isOptionVisible(WebElement container, String value) {
        return isOptionVisible(DEFAULT_TYPE, container, value);
    }

    /**
     * Checks if a specific option is visible in a select component.
     *
     * @param componentType The type of select component.
     * @param container     The WebElement representing the container of the select component.
     * @param value         The value of the option to check.
     * @return true if the option is present, false otherwise.
     */
    boolean isOptionVisible(SelectComponentType componentType, WebElement container, String value);

    /**
     * Checks if a specific option is visible in a select component.
     * The select component is identified by the provided container locator, using the default select component type.
     *
     * @param containerLocator The By locator that identifies the container of the select component.
     * @param value            The value of the option to check.
     * @return true if the option is present, false otherwise.
     */
    default boolean isOptionVisible(By containerLocator, String value) {
        return isOptionVisible(DEFAULT_TYPE, containerLocator, value);
    }

    /**
     * Checks if a specific option is visible in a select component.
     * The select component is identified by the provided container locator and the specified component type.
     *
     * @param componentType    The type of select component.
     * @param containerLocator The By locator that identifies the container of the select component.
     * @param value            The value of the option to check.
     * @return true if the option is present, false otherwise.
     */
    boolean isOptionVisible(SelectComponentType componentType, By containerLocator, String value);

    /**
     * Checks if a specific option is enabled in a select component. Uses the default select component type.
     *
     * @param container The WebElement representing the container of the select component.
     * @param value     The value of the option to check.
     * @return true if the option is enabled, false otherwise.
     */
    default boolean isOptionEnabled(WebElement container, String value) {
        return isOptionEnabled(DEFAULT_TYPE, container, value);
    }

    /**
     * Checks if a specific option is enabled in a select component.
     *
     * @param componentType The type of select component.
     * @param container     The WebElement representing the container of the select component.
     * @param value         The value of the option to check.
     * @return true if the option is enabled, false otherwise.
     */
    boolean isOptionEnabled(SelectComponentType componentType, WebElement container, String value);

    /**
     * Checks if a specific option is enabled in a select component.
     * The select component is identified by the provided container locator, using the default select component type.
     *
     * @param containerLocator The By locator that identifies the container of the select component.
     * @param value            The value of the option to check.
     * @return true if the option is enabled, false otherwise.
     */
    default boolean isOptionEnabled(By containerLocator, String value) {
        return isOptionEnabled(DEFAULT_TYPE, containerLocator, value);
    }

    /**
     * Checks if a specific option is enabled in a select component.
     * The select component is identified by the provided container locator and the specified component type.
     *
     * @param componentType    The type of select component.
     * @param containerLocator The By locator that identifies the container of the select component.
     * @param value            The value of the option to check.
     * @return true if the option is enabled, false otherwise.
     */
    boolean isOptionEnabled(SelectComponentType componentType, By containerLocator, String value);

    /**
     * Retrieves the default select component type from the configuration.
     *
     * @return The default SelectComponentType.
     */

    private static SelectComponentType getDefaultType() {
        try {
            return ReflectionUtil.findEnumImplementationsOfInterface(SelectComponentType.class,
                    uiConfig.selectDefaultType(),
                    uiConfig.projectPackage());
        } catch (Exception ignored) {
            return null;
        }
    }
}