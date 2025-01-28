package com.theairebellion.zeus.ui.components.input;

import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Interface defining operations for interacting with input elements within a web interface using Selenium.
 */
public interface InputService extends Insertion {

    UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);
    InputComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Inserts a given value into a specified container element using the default input component type.
     *
     * @param container The WebElement container where the value will be inserted.
     * @param value     The value to be inserted.
     */
    default void insert(WebElement container, String value) {
        insert(DEFAULT_TYPE, container, value);
    }

    /**
     * Inserts a given value into a specified container element, considering a specific input component type.
     *
     * @param componentType The type of the input component.
     * @param container     The WebElement container where the value will be inserted.
     * @param value         The value to be inserted.
     */
    void insert(InputComponentType componentType, WebElement container, String value);

    /**
     * Inserts a given value into a specified container element identified by a label
     * using the default input component type.
     *
     * @param container       The WebElement container where the value will be inserted.
     * @param inputFieldLabel The label of the input field.
     * @param value           The value to be inserted.
     */
    default void insert(WebElement container, String inputFieldLabel, String value) {
        insert(DEFAULT_TYPE, container, inputFieldLabel, value);
    }

    /**
     * Inserts a given value into a specified container element identified by a label, considering a specific input component type.
     *
     * @param componentType   The type of the input component.
     * @param container       The WebElement container where the value will be inserted.
     * @param inputFieldLabel The label of the input field.
     * @param value           The value to be inserted.
     */
    void insert(InputComponentType componentType, WebElement container, String inputFieldLabel, String value);

    /**
     * Inserts a given value into an input field identified by a label
     * using the default input component type.
     *
     * @param inputFieldLabel The label of the input field.
     * @param value           The value to be inserted.
     */
    default void insert(String inputFieldLabel, String value) {
        insert(DEFAULT_TYPE, inputFieldLabel, value);
    }

    /**
     * Inserts a given value into an input field identified by a label, considering a specific input component type.
     *
     * @param componentType   The type of the input component.
     * @param inputFieldLabel The label of the input field.
     * @param value           The value to be inserted.
     */
    void insert(InputComponentType componentType, String inputFieldLabel, String value);

    /**
     * Inserts a given value into an input field located using a By locator
     * using the default input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container.
     * @param value                      The value to be inserted.
     */
    default void insert(By inputFieldContainerLocator, String value) {
        insert(DEFAULT_TYPE, inputFieldContainerLocator, value);
    }

    /**
     * Inserts a given value into an input field located using a By locator, considering a specific input component type.
     *
     * @param componentType              The type of the input component.
     * @param inputFieldContainerLocator The By locator for the input field container.
     * @param value                      The value to be inserted.
     */
    void insert(InputComponentType componentType, By inputFieldContainerLocator, String value);

    /**
     * Clears the value from a specified container element using the default input component type.
     *
     * @param container The WebElement container to be cleared.
     */
    default void clear(WebElement container) {
        clear(DEFAULT_TYPE, container);
    }

    /**
     * Clears the value from a specified container element, considering a specific input component type.
     *
     * @param componentType The type of the input component.
     * @param container     The WebElement container to be cleared.
     */
    void clear(InputComponentType componentType, WebElement container);

    /**
     * Clears the value from a specified container element identified by a label
     * using the default input component type.
     *
     * @param container       The WebElement container to be cleared.
     * @param inputFieldLabel The label of the input field.
     */
    default void clear(WebElement container, String inputFieldLabel) {
        clear(DEFAULT_TYPE, container, inputFieldLabel);
    }

    /**
     * Clears the value from a specified container element identified by a label,
     * considering a specific input component type.
     *
     * @param componentType   The type of the input component.
     * @param container       The WebElement container to be cleared.
     * @param inputFieldLabel The label of the input field.
     */
    void clear(InputComponentType componentType, WebElement container, String inputFieldLabel);

    /**
     * Clears the value from an input field identified by a label using the default input component type.
     *
     * @param inputFieldLabel The label of the input field to be cleared.
     */
    default void clear(String inputFieldLabel) {
        clear(DEFAULT_TYPE, inputFieldLabel);
    }

    /**
     * Clears the value from an input field identified by a label, considering a specific input component type.
     *
     * @param componentType   The type of the input component.
     * @param inputFieldLabel The label of the input field to be cleared.
     */
    void clear(InputComponentType componentType, String inputFieldLabel);

    /**
     * Clears the value from an input field located using a By locator
     * using the default input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container to be cleared.
     */
    default void clear(By inputFieldContainerLocator) {
        clear(DEFAULT_TYPE, inputFieldContainerLocator);
    }

    /**
     * Clears the value from an input field located using a By locator,
     * considering a specific input component type.
     *
     * @param componentType              The type of the input component.
     * @param inputFieldContainerLocator The By locator for the input field container to be cleared.
     */
    void clear(InputComponentType componentType, By inputFieldContainerLocator);

    /**
     * Retrieves the value from a specified container element
     * using the default input component type.
     *
     * @param container The WebElement container from which the value will be retrieved.
     * @return The value from the specified container.
     */
    default String getValue(WebElement container) {
        return getValue(DEFAULT_TYPE, container);
    }

    /**
     * Retrieves the value from a specified container element, considering a specific input component type.
     *
     * @param componentType The type of the input component.
     * @param container     The WebElement container from which the value will be retrieved.
     * @return The value from the specified container.
     */
    String getValue(InputComponentType componentType, WebElement container);

    /**
     * Retrieves the value from a specified container element identified by a label
     * using the default input component type.
     *
     * @param container       The WebElement container from which the value will be retrieved.
     * @param inputFieldLabel The label of the input field.
     * @return The value from the specified container.
     */
    default String getValue(WebElement container, String inputFieldLabel) {
        return getValue(DEFAULT_TYPE, container, inputFieldLabel);
    }

    /**
     * Retrieves the value from a specified container element identified by a label,
     * considering a specific input component type.
     *
     * @param componentType   The type of the input component.
     * @param container       The WebElement container from which the value will be retrieved.
     * @param inputFieldLabel The label of the input field.
     * @return The value from the specified container.
     */
    String getValue(InputComponentType componentType, WebElement container, String inputFieldLabel);

    /**
     * Retrieves the value from an input field identified by a label
     * using the default input component type.
     *
     * @param inputFieldLabel The label of the input field from which the value will be retrieved.
     * @return The value from the specified input field.
     */
    default String getValue(String inputFieldLabel) {
        return getValue(DEFAULT_TYPE, inputFieldLabel);
    }

    /**
     * Retrieves the value from an input field identified by a label,
     * considering a specific input component type.
     *
     * @param componentType   The type of the input component.
     * @param inputFieldLabel The label of the input field from which the value will be retrieved.
     * @return The value from the specified input field.
     */
    String getValue(InputComponentType componentType, String inputFieldLabel);

    /**
     * Retrieves the value from an input field located using a By locator
     * using the default input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container from which the value will be retrieved.
     * @return The value from the specified input field container.
     */
    default String getValue(By inputFieldContainerLocator) {
        return getValue(DEFAULT_TYPE, inputFieldContainerLocator);
    }

    /**
     * Retrieves the value from an input field located using a By locator,
     * considering a specific input component type.
     *
     * @param componentType              The type of the input component.
     * @param inputFieldContainerLocator The By locator for the input field container from which the value will be retrieved.
     * @return The value from the specified input field container.
     */
    String getValue(InputComponentType componentType, By inputFieldContainerLocator);

    /**
     * Checks if the specified container element is enabled using the default input component type.
     *
     * @param container The WebElement container to be checked.
     * @return true if the container is enabled, false otherwise.
     */
    default boolean isEnabled(WebElement container) {
        return isEnabled(DEFAULT_TYPE, container);
    }

    /**
     * Checks if the specified container element is enabled, considering a specific input component type.
     *
     * @param componentType The type of the input component.
     * @param container     The WebElement container to be checked.
     * @return true if the container is enabled, false otherwise.
     */
    boolean isEnabled(InputComponentType componentType, WebElement container);

    /**
     * Checks if the specified container element identified by a label is enabled
     * using the default input component type.
     *
     * @param container       The WebElement container to be checked.
     * @param inputFieldLabel The label of the input field.
     * @return true if the container is enabled, false otherwise.
     */
    default boolean isEnabled(WebElement container, String inputFieldLabel) {
        return isEnabled(DEFAULT_TYPE, container, inputFieldLabel);
    }

    /**
     * Checks if the specified container element identified by a label is enabled,
     * considering a specific input component type.
     *
     * @param componentType   The type of the input component.
     * @param container       The WebElement container to be checked.
     * @param inputFieldLabel The label of the input field.
     * @return true if the container is enabled, false otherwise.
     */
    boolean isEnabled(InputComponentType componentType, WebElement container, String inputFieldLabel);

    /**
     * Checks if an input field identified by a label is enabled using the default input component type.
     *
     * @param inputFieldLabel The label of the input field to be checked.
     * @return true if the input field is enabled, false otherwise.
     */
    default boolean isEnabled(String inputFieldLabel) {
        return isEnabled(DEFAULT_TYPE, inputFieldLabel);
    }

    /**
     * Checks if an input field identified by a label is enabled, considering a specific input component type.
     *
     * @param componentType   The type of the input component.
     * @param inputFieldLabel The label of the input field to be checked.
     * @return true if the input field is enabled, false otherwise.
     */
    boolean isEnabled(InputComponentType componentType, String inputFieldLabel);

    /**
     * Checks if an input field located using a By locator is enabled
     * using the default input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container to be checked.
     * @return true if the input field container is enabled, false otherwise.
     */
    default boolean isEnabled(By inputFieldContainerLocator) {
        return isEnabled(DEFAULT_TYPE, inputFieldContainerLocator);
    }

    /**
     * Checks if an input field located using a By locator is enabled,
     * considering a specific input component type.
     *
     * @param componentType              The type of the input component.
     * @param inputFieldContainerLocator The By locator for the input field container to be checked.
     * @return true if the input field container is enabled, false otherwise.
     */
    boolean isEnabled(InputComponentType componentType, By inputFieldContainerLocator);

    /**
     * Retrieves the error message from a specified container element
     * using the default input component type.
     *
     * @param container The WebElement container from which the error message will be retrieved.
     * @return The error message from the specified container.
     */
    default String getErrorMessage(WebElement container) {
        return getErrorMessage(DEFAULT_TYPE, container);
    }

    /**
     * Retrieves the error message from a specified container element,
     * considering a specific input component type.
     *
     * @param componentType The type of the input component.
     * @param container     The WebElement container from which the error message will be retrieved.
     * @return The error message from the specified container.
     */
    String getErrorMessage(InputComponentType componentType, WebElement container);

    /**
     * Retrieves the error message from a specified container element identified by a label
     * using the default input component type.
     *
     * @param container       The WebElement container from which the error message will be retrieved.
     * @param inputFieldLabel The label of the input field.
     * @return The error message from the specified container.
     */
    default String getErrorMessage(WebElement container, String inputFieldLabel) {
        return getErrorMessage(DEFAULT_TYPE, container, inputFieldLabel);
    }

    /**
     * Retrieves the error message from a specified container element identified by a label,
     * considering a specific input component type.
     *
     * @param componentType   The type of the input component.
     * @param container       The WebElement container from which the error message will be retrieved.
     * @param inputFieldLabel The label of the input field.
     * @return The error message from the specified container.
     */
    String getErrorMessage(InputComponentType componentType, WebElement container, String inputFieldLabel);

    /**
     * Retrieves the error message from an input field identified by a label
     * using the default input component type.
     *
     * @param inputFieldLabel The label of the input field from which the error message will be retrieved.
     * @return The error message from the specified input field.
     */
    default String getErrorMessage(String inputFieldLabel) {
        return getErrorMessage(DEFAULT_TYPE, inputFieldLabel);
    }

    /**
     * Retrieves the error message from an input field identified by a label,
     * considering a specific input component type.
     *
     * @param componentType   The type of the input component.
     * @param inputFieldLabel The label of the input field from which the error message will be retrieved.
     * @return The error message from the specified input field.
     */
    String getErrorMessage(InputComponentType componentType, String inputFieldLabel);

    /**
     * Retrieves the error message from an input field located using a By locator
     * using the default input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container from which the error message will be retrieved.
     * @return The error message from the specified input field container.
     */
    default String getErrorMessage(By inputFieldContainerLocator) {
        return getErrorMessage(DEFAULT_TYPE, inputFieldContainerLocator);
    }

    /**
     * Retrieves the error message from an input field located using a By locator,
     * considering a specific input component type.
     *
     * @param componentType              The type of the input component.
     * @param inputFieldContainerLocator The By locator for the input field container from which the error message will be retrieved.
     * @return The error message from the specified input field container.
     */
    String getErrorMessage(InputComponentType componentType, By inputFieldContainerLocator);

    /**
     * Retrieves the default input component type from the configuration.
     *
     * @return The default RadioComponentType.
     */
    private static InputComponentType getDefaultType() {
        try {
            return ReflectionUtil.findEnumImplementationsOfInterface(InputComponentType.class,
                    uiConfig.inputDefaultType(),
                    uiConfig.projectPackage());
        } catch (Exception ignored) {
            return null;
        }
    }
}
