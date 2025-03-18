package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.openqa.selenium.By;

import java.util.List;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Provides an interface for interacting with radio button elements in a UI automation framework,
 * enabling operations like selection, state verification, and retrieving lists of available radios.
 * By referencing a {@link RadioComponentType} and the {@link #DEFAULT_TYPE}, implementations can
 * uniformly identify and control radio buttons in various application contexts.
 *
 * <p>Classes implementing this interface typically leverage Selenium-based approaches for
 * container/locator handling, ensuring consistent behavior across different UI designs.</p>
 *
 * @author Cyborg Code Syndicate
 */
public interface RadioService extends Insertion {

    RadioComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Selects a radio button within a container based on the button text. Uses the default radio component type.
     *
     * @param container       The SmartWebElement that contains the radio buttons.
     * @param radioButtonText The text of the radio button to be selected.
     */
    default void select(SmartWebElement container, String radioButtonText) {
        select(DEFAULT_TYPE, container, radioButtonText);
    }

    /**
     * Selects a radio button within a container based on the button text and specific radio component type.
     *
     * @param componentType   The specific type of radio component to interact with.
     * @param container       The SmartWebElement that contains the radio buttons.
     * @param radioButtonText The text of the radio button to be selected.
     */
    void select(RadioComponentType componentType, SmartWebElement container, String radioButtonText);

    /**
     * Selects a radio button within a container based on a strategy. Uses the default radio component type.
     *
     * @param container The SmartWebElement that contains the radio buttons.
     * @param strategy  The strategy to use for selecting the radio button.
     * @return The text of the selected radio button.
     */
    default String select(SmartWebElement container, Strategy strategy) {
        return select(DEFAULT_TYPE, container, strategy);
    }

    /**
     * Selects a radio button within a container based on a strategy and specific radio component type.
     *
     * @param componentType The specific type of radio component to interact with.
     * @param container     The SmartWebElement that contains the radio buttons.
     * @param strategy      The strategy to use for selecting the radio button.
     * @return The text of the selected radio button.
     */
    String select(RadioComponentType componentType, SmartWebElement container, Strategy strategy);

    /**
     * Selects a radio button based on the button text. Uses the default radio component type.
     *
     * @param radioButtonText The text of the radio button to be selected.
     */
    default void select(String radioButtonText) {
        select(DEFAULT_TYPE, radioButtonText);
    }

    /**
     * Selects a radio button based on the button text and specific radio component type.
     *
     * @param componentType   The specific type of radio component to interact with.
     * @param radioButtonText The text of the radio button to be selected.
     */
    void select(RadioComponentType componentType, String radioButtonText);

    /**
     * Selects a radio button based on a locator. Uses the default radio component type.
     *
     * @param radioButtonLocator The locator of the radio button to be selected.
     */
    default void select(By radioButtonLocator) {
        select(DEFAULT_TYPE, radioButtonLocator);
    }

    /**
     * Selects a radio button based on a locator and specific radio component type.
     *
     * @param componentType      The specific type of radio component to interact with.
     * @param radioButtonLocator The locator of the radio button to be selected.
     */
    void select(RadioComponentType componentType, By radioButtonLocator);

    /**
     * Checks if a radio button within a container is enabled based on the button text. Uses the default radio component type.
     *
     * @param container       The SmartWebElement that contains the radio buttons.
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is enabled, false otherwise.
     */
    default boolean isEnabled(SmartWebElement container, String radioButtonText) {
        return isEnabled(DEFAULT_TYPE, container, radioButtonText);
    }

    /**
     * Checks if a radio button within a container is enabled based on the button text and specific radio component type.
     *
     * @param componentType   The specific type of radio component.
     * @param container       The SmartWebElement that contains the radio buttons.
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is enabled, false otherwise.
     */
    boolean isEnabled(RadioComponentType componentType, SmartWebElement container, String radioButtonText);

    /**
     * Checks if a radio button is enabled based on the button text. Uses the default radio component type.
     *
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is enabled, false otherwise.
     */
    default boolean isEnabled(String radioButtonText) {
        return isEnabled(DEFAULT_TYPE, radioButtonText);
    }

    /**
     * Checks if a radio button is enabled based on the button text and specific radio component type.
     *
     * @param componentType   The specific type of radio component.
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is enabled, false otherwise.
     */
    boolean isEnabled(RadioComponentType componentType, String radioButtonText);

    /**
     * Checks if a radio button is enabled based on a locator. Uses the default radio component type.
     *
     * @param radioButtonLocator The locator of the radio button to check.
     * @return true if the radio button is enabled, false otherwise.
     */
    default boolean isEnabled(By radioButtonLocator) {
        return isEnabled(DEFAULT_TYPE, radioButtonLocator);
    }

    /**
     * Checks if a radio button is enabled based on a locator and specific radio component type.
     *
     * @param componentType      The specific type of radio component.
     * @param radioButtonLocator The locator of the radio button to check.
     * @return true if the radio button is enabled, false otherwise.
     */
    boolean isEnabled(RadioComponentType componentType, By radioButtonLocator);

    /**
     * Checks if a radio button within a container is selected based on the button text. Uses the default radio component type.
     *
     * @param container       The SmartWebElement that contains the radio buttons.
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is selected, false otherwise.
     */
    default boolean isSelected(SmartWebElement container, String radioButtonText) {
        return isSelected(DEFAULT_TYPE, container, radioButtonText);
    }

    /**
     * Checks if a radio button within a container is selected based on the button text and specific radio component type.
     *
     * @param componentType   The specific type of radio component.
     * @param container       The SmartWebElement that contains the radio buttons.
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is selected, false otherwise.
     */
    boolean isSelected(RadioComponentType componentType, SmartWebElement container, String radioButtonText);

    /**
     * Checks if a radio button is selected based on the button text. Uses the default radio component type.
     *
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is selected, false otherwise.
     */
    default boolean isSelected(String radioButtonText) {
        return isSelected(DEFAULT_TYPE, radioButtonText);
    }

    /**
     * Checks if a radio button is selected based on the button text and specific radio component type.
     *
     * @param componentType   The specific type of radio component.
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is selected, false otherwise.
     */
    boolean isSelected(RadioComponentType componentType, String radioButtonText);

    /**
     * Checks if a radio button is selected based on a locator. Uses the default radio component type.
     *
     * @param radioButtonLocator The locator of the radio button to check.
     * @return true if the radio button is selected, false otherwise.
     */
    default boolean isSelected(By radioButtonLocator) {
        return isSelected(DEFAULT_TYPE, radioButtonLocator);
    }

    /**
     * Checks if a radio button is selected based on a locator and specific radio component type.
     *
     * @param componentType      The specific type of radio component.
     * @param radioButtonLocator The locator of the radio button to check.
     * @return true if the radio button is selected, false otherwise.
     */
    boolean isSelected(RadioComponentType componentType, By radioButtonLocator);

    /**
     * Checks if a radio button within a container is visible based on the button text. Uses the default radio component type.
     *
     * @param container       The SmartWebElement that contains the radio buttons.
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is visible, false otherwise.
     */
    default boolean isVisible(SmartWebElement container, String radioButtonText) {
        return isVisible(DEFAULT_TYPE, container, radioButtonText);
    }

    /**
     * Checks if a radio button within a container is visible based on the button text and specific radio component type.
     *
     * @param componentType   The specific type of radio component.
     * @param container       The SmartWebElement that contains the radio buttons.
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is visible, false otherwise.
     */
    boolean isVisible(RadioComponentType componentType, SmartWebElement container, String radioButtonText);

    /**
     * Checks if a radio button is visible based on the button text. Uses the default radio component type.
     *
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is visible, false otherwise.
     */
    default boolean isVisible(String radioButtonText) {
        return isVisible(DEFAULT_TYPE, radioButtonText);
    }

    /**
     * Checks if a radio button is visible based on the button text and specific radio component type.
     *
     * @param componentType   The specific type of radio component.
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is visible, false otherwise.
     */
    boolean isVisible(RadioComponentType componentType, String radioButtonText);

    /**
     * Checks if a radio button is visible based on a locator. Uses the default radio component type.
     *
     * @param radioButtonLocator The locator of the radio button to check.
     * @return true if the radio button is visible, false otherwise.
     */
    default boolean isVisible(By radioButtonLocator) {
        return isVisible(DEFAULT_TYPE, radioButtonLocator);
    }

    /**
     * Checks if a radio button is visible based on a locator and specific radio component type.
     *
     * @param componentType      The specific type of radio component.
     * @param radioButtonLocator The locator of the radio button to check.
     * @return true if the radio button is visible, false otherwise.
     */
    boolean isVisible(RadioComponentType componentType, By radioButtonLocator);

    /**
     * Gets the text of the selected radio button within a container. Uses the default radio component type.
     *
     * @param container The SmartWebElement that contains the radio buttons.
     * @return The text of the selected radio button.
     */
    default String getSelected(SmartWebElement container) {
        return getSelected(DEFAULT_TYPE, container);
    }

    /**
     * Gets the text of the selected radio button within a container based on specific radio component type.
     *
     * @param componentType The specific type of radio component.
     * @param container     The SmartWebElement that contains the radio buttons.
     * @return The text of the selected radio button.
     */
    String getSelected(RadioComponentType componentType, SmartWebElement container);

    /**
     * Gets the text of the selected radio button within a container, specified by its locator. Uses the default radio component type.
     *
     * @param containerLocator The By locator for the container element that contains the radio buttons.
     * @return The text of the selected radio button in the container identified by the locator.
     */
    default String getSelected(By containerLocator) {
        return getSelected(DEFAULT_TYPE, containerLocator);
    }

    /**
     * Gets the text of the selected radio button within a container, specified by its locator, based on a specific radio component type.
     *
     * @param componentType    The specific type of radio component.
     * @param containerLocator The By locator for the container element that contains the radio buttons.
     * @return The text of the selected radio button in the container identified by the locator, based on the specified component type.
     */
    String getSelected(RadioComponentType componentType, By containerLocator);

    /**
     * Gets all the texts of the radio buttons within a container. Uses the default radio component type.
     *
     * @param container The SmartWebElement that contains the radio buttons.
     * @return A list of texts of all radio buttons in the container.
     */
    default List<String> getAll(SmartWebElement container) {
        return getAll(DEFAULT_TYPE, container);
    }

    /**
     * Gets all the texts of the radio buttons within a container based on specific radio component type.
     *
     * @param componentType The specific type of radio component.
     * @param container     The SmartWebElement that contains the radio buttons.
     * @return A list of texts of all radio buttons in the container.
     */
    List<String> getAll(RadioComponentType componentType, SmartWebElement container);

    /**
     * Gets all the texts of the radio buttons within a container, specified by its locator. Uses the default radio component type.
     *
     * @param containerLocator The By locator for the container element that contains the radio buttons.
     * @return A list of texts of all radio buttons in the container identified by the locator.
     */
    default List<String> getAll(By containerLocator) {
        return getAll(DEFAULT_TYPE, containerLocator);
    }

    /**
     * Gets all the texts of the radio buttons within a container, specified by its locator, based on a specific radio component type.
     *
     * @param componentType    The specific type of radio component.
     * @param containerLocator The By locator for the container element that contains the radio buttons.
     * @return A list of texts of all radio buttons in the container identified by the locator and based on the specified component type.
     */
    List<String> getAll(RadioComponentType componentType, By containerLocator);

    /**
     * Retrieves the default radio component type from the configuration.
     *
     * @return The default RadioComponentType.
     */
    private static RadioComponentType getDefaultType() {
        try {
            return ReflectionUtil.findEnumImplementationsOfInterface(RadioComponentType.class,
                    getUiConfig().radioDefaultType(),
                    getUiConfig().projectPackage());
        } catch (Exception ignored) {
            return null;
        }
    }
}