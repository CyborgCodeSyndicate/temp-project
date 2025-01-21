package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Interface defining operations for interacting with radio button elements within a web interface using Selenium.
 */
public interface RadioService extends Insertion {

    UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);
    RadioComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Selects a radio button within a container based on the button text. Uses the default radio component type.
     *
     * @param container       The WebElement that contains the radio buttons.
     * @param radioButtonText The text of the radio button to be selected.
     */
    default void select(WebElement container, String radioButtonText) {
        select(container, radioButtonText, DEFAULT_TYPE);
    }

    /**
     * Selects a radio button within a container based on the button text and specific radio component type.
     *
     * @param container          The WebElement that contains the radio buttons.
     * @param radioButtonText    The text of the radio button to be selected.
     * @param componentType The specific type of radio component to interact with.
     */
    void select(WebElement container, String radioButtonText, RadioComponentType componentType);

    /**
     * Selects a radio button within a container based on a strategy. Uses the default radio component type.
     *
     * @param container The WebElement that contains the radio buttons.
     * @param strategy  The strategy to use for selecting the radio button.
     * @return The text of the selected radio button.
     */
    default String select(WebElement container, Strategy strategy) {
        return select(container, strategy, DEFAULT_TYPE);
    }

    /**
     * Selects a radio button within a container based on a strategy and specific radio component type.
     *
     * @param container          The WebElement that contains the radio buttons.
     * @param strategy           The strategy to use for selecting the radio button.
     * @param componentType The specific type of radio component to interact with.
     * @return The text of the selected radio button.
     */
    String select(WebElement container, Strategy strategy, RadioComponentType componentType);

    /**
     * Selects a radio button based on the button text. Uses the default radio component type.
     *
     * @param radioButtonText The text of the radio button to be selected.
     */
    default void select(String radioButtonText) {
        select(radioButtonText, DEFAULT_TYPE);
    }

    /**
     * Selects a radio button based on the button text and specific radio component type.
     *
     * @param radioButtonText    The text of the radio button to be selected.
     * @param componentType The specific type of radio component to interact with.
     */
    void select(String radioButtonText, RadioComponentType componentType);

    /**
     * Selects a radio button based on a locator. Uses the default radio component type.
     *
     * @param radioButtonLocator The locator of the radio button to be selected.
     */
    default void select(By radioButtonLocator) {
        select(radioButtonLocator, DEFAULT_TYPE);
    }

    /**
     * Selects a radio button based on a locator and specific radio component type.
     *
     * @param radioButtonLocator The locator of the radio button to be selected.
     * @param componentType The specific type of radio component to interact with.
     */
    void select(By radioButtonLocator, RadioComponentType componentType);

    /**
     * Checks if a radio button within a container is enabled based on the button text. Uses the default radio component type.
     *
     * @param container       The WebElement that contains the radio buttons.
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is enabled, false otherwise.
     */
    default boolean isEnabled(WebElement container, String radioButtonText) {
        return isEnabled(container, radioButtonText, DEFAULT_TYPE);
    }

    /**
     * Checks if a radio button within a container is enabled based on the button text and specific radio component type.
     *
     * @param container          The WebElement that contains the radio buttons.
     * @param radioButtonText    The text of the radio button to check.
     * @param componentType The specific type of radio component.
     * @return true if the radio button is enabled, false otherwise.
     */
    boolean isEnabled(WebElement container, String radioButtonText, RadioComponentType componentType);

    /**
     * Checks if a radio button is enabled based on the button text. Uses the default radio component type.
     *
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is enabled, false otherwise.
     */
    default boolean isEnabled(String radioButtonText) {
        return isEnabled(radioButtonText, DEFAULT_TYPE);
    }

    /**
     * Checks if a radio button is enabled based on the button text and specific radio component type.
     *
     * @param radioButtonText    The text of the radio button to check.
     * @param componentType The specific type of radio component.
     * @return true if the radio button is enabled, false otherwise.
     */
    boolean isEnabled(String radioButtonText, RadioComponentType componentType);

    /**
     * Checks if a radio button is enabled based on a locator. Uses the default radio component type.
     *
     * @param radioButtonLocator The locator of the radio button to check.
     * @return true if the radio button is enabled, false otherwise.
     */
    default boolean isEnabled(By radioButtonLocator) {
        return isEnabled(radioButtonLocator, DEFAULT_TYPE);
    }

    /**
     * Checks if a radio button is enabled based on a locator and specific radio component type.
     *
     * @param radioButtonLocator The locator of the radio button to check.
     * @param componentType The specific type of radio component.
     * @return true if the radio button is enabled, false otherwise.
     */
    boolean isEnabled(By radioButtonLocator, RadioComponentType componentType);

    /**
     * Checks if a radio button within a container is selected based on the button text. Uses the default radio component type.
     *
     * @param container       The WebElement that contains the radio buttons.
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is selected, false otherwise.
     */
    default boolean isSelected(WebElement container, String radioButtonText) {
        return isSelected(container, radioButtonText, DEFAULT_TYPE);
    }

    /**
     * Checks if a radio button within a container is selected based on the button text and specific radio component type.
     *
     * @param container          The WebElement that contains the radio buttons.
     * @param radioButtonText    The text of the radio button to check.
     * @param componentType The specific type of radio component.
     * @return true if the radio button is selected, false otherwise.
     */
    boolean isSelected(WebElement container, String radioButtonText, RadioComponentType componentType);

    /**
     * Checks if a radio button is selected based on the button text. Uses the default radio component type.
     *
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is selected, false otherwise.
     */
    default boolean isSelected(String radioButtonText) {
        return isSelected(radioButtonText, DEFAULT_TYPE);
    }

    /**
     * Checks if a radio button is selected based on the button text and specific radio component type.
     *
     * @param radioButtonText    The text of the radio button to check.
     * @param componentType The specific type of radio component.
     * @return true if the radio button is selected, false otherwise.
     */
    boolean isSelected(String radioButtonText, RadioComponentType componentType);

    /**
     * Checks if a radio button is selected based on a locator. Uses the default radio component type.
     *
     * @param radioButtonLocator The locator of the radio button to check.
     * @return true if the radio button is selected, false otherwise.
     */
    default boolean isSelected(By radioButtonLocator) {
        return isSelected(radioButtonLocator, DEFAULT_TYPE);
    }

    /**
     * Checks if a radio button is selected based on a locator and specific radio component type.
     *
     * @param radioButtonLocator The locator of the radio button to check.
     * @param componentType The specific type of radio component.
     * @return true if the radio button is selected, false otherwise.
     */
    boolean isSelected(By radioButtonLocator, RadioComponentType componentType);

    /**
     * Checks if a radio button within a container is visible based on the button text. Uses the default radio component type.
     *
     * @param container       The WebElement that contains the radio buttons.
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is visible, false otherwise.
     */
    default boolean isVisible(WebElement container, String radioButtonText) {
        return isVisible(container, radioButtonText, DEFAULT_TYPE);
    }

    /**
     * Checks if a radio button within a container is visible based on the button text and specific radio component type.
     *
     * @param container          The WebElement that contains the radio buttons.
     * @param radioButtonText    The text of the radio button to check.
     * @param componentType The specific type of radio component.
     * @return true if the radio button is visible, false otherwise.
     */
    boolean isVisible(WebElement container, String radioButtonText, RadioComponentType componentType);

    /**
     * Checks if a radio button is visible based on the button text. Uses the default radio component type.
     *
     * @param radioButtonText The text of the radio button to check.
     * @return true if the radio button is visible, false otherwise.
     */
    default boolean isVisible(String radioButtonText) {
        return isVisible(radioButtonText, DEFAULT_TYPE);
    }

    /**
     * Checks if a radio button is visible based on the button text and specific radio component type.
     *
     * @param radioButtonText    The text of the radio button to check.
     * @param componentType The specific type of radio component.
     * @return true if the radio button is visible, false otherwise.
     */
    boolean isVisible(String radioButtonText, RadioComponentType componentType);

    /**
     * Checks if a radio button is visible based on a locator. Uses the default radio component type.
     *
     * @param radioButtonLocator The locator of the radio button to check.
     * @return true if the radio button is visible, false otherwise.
     */
    default boolean isVisible(By radioButtonLocator) {
        return isVisible(radioButtonLocator, DEFAULT_TYPE);
    }

    /**
     * Checks if a radio button is visible based on a locator and specific radio component type.
     *
     * @param radioButtonLocator The locator of the radio button to check.
     * @param componentType The specific type of radio component.
     * @return true if the radio button is visible, false otherwise.
     */
    boolean isVisible(By radioButtonLocator, RadioComponentType componentType);

    /**
     * Gets the text of the selected radio button within a container. Uses the default radio component type.
     *
     * @param container The WebElement that contains the radio buttons.
     * @return The text of the selected radio button.
     */
    default String getSelected(WebElement container) {
        return getSelected(container, DEFAULT_TYPE);
    }

    /**
     * Gets the text of the selected radio button within a container based on specific radio component type.
     *
     * @param container          The WebElement that contains the radio buttons.
     * @param componentType The specific type of radio component.
     * @return The text of the selected radio button.
     */
    String getSelected(WebElement container, RadioComponentType componentType);

    /**
     * Gets the text of the selected radio button within a container, specified by its locator. Uses the default radio component type.
     *
     * @param containerLocator The By locator for the container element that contains the radio buttons.
     * @return The text of the selected radio button in the container identified by the locator.
     */
    default String getSelected(By containerLocator) {
        return getSelected(containerLocator, DEFAULT_TYPE);
    }

    /**
     * Gets the text of the selected radio button within a container, specified by its locator, based on a specific radio component type.
     *
     * @param containerLocator   The By locator for the container element that contains the radio buttons.
     * @param componentType The specific type of radio component.
     * @return The text of the selected radio button in the container identified by the locator, based on the specified component type.
     */
    String getSelected(By containerLocator, RadioComponentType componentType);

    /**
     * Gets all the texts of the radio buttons within a container. Uses the default radio component type.
     *
     * @param container The WebElement that contains the radio buttons.
     * @return A list of texts of all radio buttons in the container.
     */
    default List<String> getAll(WebElement container) {
        return getAll(container, DEFAULT_TYPE);
    }

    /**
     * Gets all the texts of the radio buttons within a container based on specific radio component type.
     *
     * @param container          The WebElement that contains the radio buttons.
     * @param componentType The specific type of radio component.
     * @return A list of texts of all radio buttons in the container.
     */
    List<String> getAll(WebElement container, RadioComponentType componentType);

    /**
     * Gets all the texts of the radio buttons within a container, specified by its locator. Uses the default radio component type.
     *
     * @param containerLocator The By locator for the container element that contains the radio buttons.
     * @return A list of texts of all radio buttons in the container identified by the locator.
     */
    default List<String> getAll(By containerLocator) {
        return getAll(containerLocator, DEFAULT_TYPE);
    }

    /**
     * Gets all the texts of the radio buttons within a container, specified by its locator, based on a specific radio component type.
     *
     * @param containerLocator   The By locator for the container element that contains the radio buttons.
     * @param componentType The specific type of radio component.
     * @return A list of texts of all radio buttons in the container identified by the locator and based on the specified component type.
     */
    List<String> getAll(By containerLocator, RadioComponentType componentType);

    /**
     * Retrieves the default radio component type from the configuration.
     *
     * @return The default RadioComponentType.
     */
    private static RadioComponentType getDefaultType() {
        return ReflectionUtil.findEnumImplementationsOfInterface(RadioComponentType.class,
                uiConfig.radioDefaultType(),
                uiConfig.projectPackage());
    }
}