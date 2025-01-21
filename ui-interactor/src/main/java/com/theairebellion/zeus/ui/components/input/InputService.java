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
        insert(container, value, DEFAULT_TYPE);
    }

    /**
     * Inserts a given value into a specified container element, considering a specific input component type.
     *
     * @param container     The WebElement container where the value will be inserted.
     * @param value         The value to be inserted.
     * @param componentType The type of the input component.
     */
    void insert(WebElement container, String value, InputComponentType componentType);

    /**
     * Inserts a given value into a specified container element identified by a label
     * using the default input component type.
     *
     * @param container       The WebElement container where the value will be inserted.
     * @param inputFieldLabel The label of the input field.
     * @param value           The value to be inserted.
     */
    default void insert(WebElement container, String inputFieldLabel, String value) {
        insert(container, inputFieldLabel, value, DEFAULT_TYPE);
    }

    /**
     * Inserts a given value into a specified container element identified by a label, considering a specific input component type.
     *
     * @param container       The WebElement container where the value will be inserted.
     * @param inputFieldLabel The label of the input field.
     * @param value           The value to be inserted.
     * @param componentType   The type of the input component.
     */
    void insert(WebElement container, String inputFieldLabel, String value, InputComponentType componentType);

    /**
     * Inserts a given value into an input field identified by a label
     * using the default input component type.
     *
     * @param inputFieldLabel The label of the input field.
     * @param value           The value to be inserted.
     */
    default void insert(String inputFieldLabel, String value) {
        insert(inputFieldLabel, value, DEFAULT_TYPE);
    }

    /**
     * Inserts a given value into an input field identified by a label, considering a specific input component type.
     *
     * @param inputFieldLabel The label of the input field.
     * @param value           The value to be inserted.
     * @param componentType   The type of the input component.
     */
    void insert(String inputFieldLabel, String value, InputComponentType componentType);

    /**
     * Inserts a given value into an input field located using a By locator
     * using the default input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container.
     * @param value                      The value to be inserted.
     */
    default void insert(By inputFieldContainerLocator, String value) {
        insert(inputFieldContainerLocator, value, DEFAULT_TYPE);
    }

    /**
     * Inserts a given value into an input field located using a By locator, considering a specific input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container.
     * @param value                      The value to be inserted.
     * @param componentType              The type of the input component.
     */
    void insert(By inputFieldContainerLocator, String value, InputComponentType componentType);

    /**
     * Clears the value from a specified container element using the default input component type.
     *
     * @param container The WebElement container to be cleared.
     */
    default void clear(WebElement container) {
        clear(container, DEFAULT_TYPE);
    }

    /**
     * Clears the value from a specified container element, considering a specific input component type.
     *
     * @param container     The WebElement container to be cleared.
     * @param componentType The type of the input component.
     */
    void clear(WebElement container, InputComponentType componentType);

    /**
     * Clears the value from a specified container element identified by a label
     * using the default input component type.
     *
     * @param container       The WebElement container to be cleared.
     * @param inputFieldLabel The label of the input field.
     */
    default void clear(WebElement container, String inputFieldLabel) {
        clear(container, inputFieldLabel, DEFAULT_TYPE);
    }

    /**
     * Clears the value from a specified container element identified by a label,
     * considering a specific input component type.
     *
     * @param container       The WebElement container to be cleared.
     * @param inputFieldLabel The label of the input field.
     * @param componentType   The type of the input component.
     */
    void clear(WebElement container, String inputFieldLabel, InputComponentType componentType);

    /**
     * Clears the value from an input field identified by a label using the default input component type.
     *
     * @param inputFieldLabel The label of the input field to be cleared.
     */
    default void clear(String inputFieldLabel) {
        clear(inputFieldLabel, DEFAULT_TYPE);
    }

    /**
     * Clears the value from an input field identified by a label, considering a specific input component type.
     *
     * @param inputFieldLabel The label of the input field to be cleared.
     * @param componentType   The type of the input component.
     */
    void clear(String inputFieldLabel, InputComponentType componentType);

    /**
     * Clears the value from an input field located using a By locator
     * using the default input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container to be cleared.
     */
    default void clear(By inputFieldContainerLocator) {
        clear(inputFieldContainerLocator, DEFAULT_TYPE);
    }

    /**
     * Clears the value from an input field located using a By locator,
     * considering a specific input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container to be cleared.
     * @param componentType              The type of the input component.
     */
    void clear(By inputFieldContainerLocator, InputComponentType componentType);

    /**
     * Retrieves the value from a specified container element
     * using the default input component type.
     *
     * @param container The WebElement container from which the value will be retrieved.
     * @return The value from the specified container.
     */
    default String getValue(WebElement container) {
        return getValue(container, DEFAULT_TYPE);
    }

    /**
     * Retrieves the value from a specified container element, considering a specific input component type.
     *
     * @param container     The WebElement container from which the value will be retrieved.
     * @param componentType The type of the input component.
     * @return The value from the specified container.
     */
    String getValue(WebElement container, InputComponentType componentType);

    /**
     * Retrieves the value from a specified container element identified by a label
     * using the default input component type.
     *
     * @param container       The WebElement container from which the value will be retrieved.
     * @param inputFieldLabel The label of the input field.
     * @return The value from the specified container.
     */
    default String getValue(WebElement container, String inputFieldLabel) {
        return getValue(container, inputFieldLabel, DEFAULT_TYPE);
    }

    /**
     * Retrieves the value from a specified container element identified by a label,
     * considering a specific input component type.
     *
     * @param container       The WebElement container from which the value will be retrieved.
     * @param inputFieldLabel The label of the input field.
     * @param componentType   The type of the input component.
     * @return The value from the specified container.
     */
    String getValue(WebElement container, String inputFieldLabel, InputComponentType componentType);

    /**
     * Retrieves the value from an input field identified by a label
     * using the default input component type.
     *
     * @param inputFieldLabel The label of the input field from which the value will be retrieved.
     * @return The value from the specified input field.
     */
    default String getValue(String inputFieldLabel) {
        return getValue(inputFieldLabel, DEFAULT_TYPE);
    }

    /**
     * Retrieves the value from an input field identified by a label,
     * considering a specific input component type.
     *
     * @param inputFieldLabel The label of the input field from which the value will be retrieved.
     * @param componentType   The type of the input component.
     * @return The value from the specified input field.
     */
    String getValue(String inputFieldLabel, InputComponentType componentType);

    /**
     * Retrieves the value from an input field located using a By locator
     * using the default input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container from which the value will be retrieved.
     * @return The value from the specified input field container.
     */
    default String getValue(By inputFieldContainerLocator) {
        return getValue(inputFieldContainerLocator, DEFAULT_TYPE);
    }

    /**
     * Retrieves the value from an input field located using a By locator,
     * considering a specific input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container from which the value will be retrieved.
     * @param componentType              The type of the input component.
     * @return The value from the specified input field container.
     */
    String getValue(By inputFieldContainerLocator, InputComponentType componentType);

    /**
     * Checks if the specified container element is enabled using the default input component type.
     *
     * @param container The WebElement container to be checked.
     * @return true if the container is enabled, false otherwise.
     */
    default boolean isEnabled(WebElement container) {
        return isEnabled(container, DEFAULT_TYPE);
    }

    /**
     * Checks if the specified container element is enabled, considering a specific input component type.
     *
     * @param container     The WebElement container to be checked.
     * @param componentType The type of the input component.
     * @return true if the container is enabled, false otherwise.
     */
    boolean isEnabled(WebElement container, InputComponentType componentType);

    /**
     * Checks if the specified container element identified by a label is enabled
     * using the default input component type.
     *
     * @param container       The WebElement container to be checked.
     * @param inputFieldLabel The label of the input field.
     * @return true if the container is enabled, false otherwise.
     */
    default boolean isEnabled(WebElement container, String inputFieldLabel) {
        return isEnabled(container, inputFieldLabel, DEFAULT_TYPE);
    }

    /**
     * Checks if the specified container element identified by a label is enabled,
     * considering a specific input component type.
     *
     * @param container       The WebElement container to be checked.
     * @param inputFieldLabel The label of the input field.
     * @param componentType   The type of the input component.
     * @return true if the container is enabled, false otherwise.
     */
    boolean isEnabled(WebElement container, String inputFieldLabel, InputComponentType componentType);

    /**
     * Checks if an input field identified by a label is enabled using the default input component type.
     *
     * @param inputFieldLabel The label of the input field to be checked.
     * @return true if the input field is enabled, false otherwise.
     */
    default boolean isEnabled(String inputFieldLabel) {
        return isEnabled(inputFieldLabel, DEFAULT_TYPE);
    }

    /**
     * Checks if an input field identified by a label is enabled, considering a specific input component type.
     *
     * @param inputFieldLabel The label of the input field to be checked.
     * @param componentType   The type of the input component.
     * @return true if the input field is enabled, false otherwise.
     */
    boolean isEnabled(String inputFieldLabel, InputComponentType componentType);

    /**
     * Checks if an input field located using a By locator is enabled
     * using the default input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container to be checked.
     * @return true if the input field container is enabled, false otherwise.
     */
    default boolean isEnabled(By inputFieldContainerLocator) {
        return isEnabled(inputFieldContainerLocator, DEFAULT_TYPE);
    }

    /**
     * Checks if an input field located using a By locator is enabled,
     * considering a specific input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container to be checked.
     * @param componentType              The type of the input component.
     * @return true if the input field container is enabled, false otherwise.
     */
    boolean isEnabled(By inputFieldContainerLocator, InputComponentType componentType);

    /**
     * Retrieves the error message from a specified container element
     * using the default input component type.
     *
     * @param container The WebElement container from which the error message will be retrieved.
     * @return The error message from the specified container.
     */
    default String getErrorMessage(WebElement container) {
        return getErrorMessage(container, DEFAULT_TYPE);
    }

    /**
     * Retrieves the error message from a specified container element,
     * considering a specific input component type.
     *
     * @param container     The WebElement container from which the error message will be retrieved.
     * @param componentType The type of the input component.
     * @return The error message from the specified container.
     */
    String getErrorMessage(WebElement container, InputComponentType componentType);

    /**
     * Retrieves the error message from a specified container element identified by a label
     * using the default input component type.
     *
     * @param container       The WebElement container from which the error message will be retrieved.
     * @param inputFieldLabel The label of the input field.
     * @return The error message from the specified container.
     */
    default String getErrorMessage(WebElement container, String inputFieldLabel) {
        return getErrorMessage(container, inputFieldLabel, DEFAULT_TYPE);
    }

    /**
     * Retrieves the error message from a specified container element identified by a label,
     * considering a specific input component type.
     *
     * @param container       The WebElement container from which the error message will be retrieved.
     * @param inputFieldLabel The label of the input field.
     * @param componentType   The type of the input component.
     * @return The error message from the specified container.
     */
    String getErrorMessage(WebElement container, String inputFieldLabel, InputComponentType componentType);

    /**
     * Retrieves the error message from an input field identified by a label
     * using the default input component type.
     *
     * @param inputFieldLabel The label of the input field from which the error message will be retrieved.
     * @return The error message from the specified input field.
     */
    default String getErrorMessage(String inputFieldLabel) {
        return getErrorMessage(inputFieldLabel, DEFAULT_TYPE);
    }

    /**
     * Retrieves the error message from an input field identified by a label,
     * considering a specific input component type.
     *
     * @param inputFieldLabel The label of the input field from which the error message will be retrieved.
     * @param componentType   The type of the input component.
     * @return The error message from the specified input field.
     */
    String getErrorMessage(String inputFieldLabel, InputComponentType componentType);

    /**
     * Retrieves the error message from an input field located using a By locator
     * using the default input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container from which the error message will be retrieved.
     * @return The error message from the specified input field container.
     */
    default String getErrorMessage(By inputFieldContainerLocator) {
        return getErrorMessage(inputFieldContainerLocator, DEFAULT_TYPE);
    }

    /**
     * Retrieves the error message from an input field located using a By locator,
     * considering a specific input component type.
     *
     * @param inputFieldContainerLocator The By locator for the input field container from which the error message will be retrieved.
     * @param componentType              The type of the input component.
     * @return The error message from the specified input field container.
     */
    String getErrorMessage(By inputFieldContainerLocator, InputComponentType componentType);

    /**
     * Retrieves the default input component type from the configuration.
     *
     * @return The default RadioComponentType.
     */
    private static InputComponentType getDefaultType() {
        return ReflectionUtil.findEnumImplementationsOfInterface(InputComponentType.class,
            uiConfig.inputDefaultType(),
            uiConfig.projectPackage());
    }


}
