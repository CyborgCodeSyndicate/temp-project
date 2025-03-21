package com.theairebellion.zeus.ui.components.radio;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

/**
 * Represents a radio button or group of radio buttons in a UI automation framework.
 * Provides methods for selecting radio buttons by text, strategy, or locator, and
 * offers checks for enabled, visible, or selected states. It also allows retrieval
 * of the currently selected button or a complete list of available radio buttons.
 *
 * <p>Implementations typically rely on Selenium-based interactions using container references
 * ({@link SmartWebElement}) or direct locators ({@link By}), enabling consistent handling of
 * radio inputs throughout an application.</p>
 *
 * <p>This interface is intended to be used wherever a radio group or individual radio
 * elements need to be tested or manipulated, providing a standard contract for
 * UI automation.</p>
 *
 * @author Cyborg Code Syndicate
 */
public interface Radio {

    /**
     * Selects a radio button, identified by its text, within the specified container.
     *
     * @param container       the container holding the radio buttons.
     * @param radioButtonText the text of the radio button to select.
     */
    void select(SmartWebElement container, String radioButtonText);

    /**
     * Selects a radio button within the specified container using a custom {@link Strategy}.
     *
     * @param container the container holding the radio buttons.
     * @param strategy  the selection strategy to determine which radio button to choose.
     * @return a string representing the selected radio button text, if available.
     */
    String select(SmartWebElement container, Strategy strategy);

    /**
     * Selects a radio button, identified by its text, without referencing a container.
     *
     * @param radioButtonText the text of the radio button to select.
     */
    void select(String radioButtonText);

    /**
     * Selects a radio button identified by a {@link By} locator.
     *
     * @param radioButtonLocator the locator identifying the radio button to select.
     */
    void select(By radioButtonLocator);

    /**
     * Checks if the specified radio button is enabled within the given container.
     *
     * @param container       the container holding the radio button.
     * @param radioButtonText the text of the radio button to check.
     * @return true if the radio button is enabled, otherwise false.
     */
    boolean isEnabled(SmartWebElement container, String radioButtonText);

    /**
     * Checks if the specified radio button, identified by text, is enabled without referencing a container.
     *
     * @param radioButtonText the text of the radio button to check.
     * @return true if the radio button is enabled, otherwise false.
     */
    boolean isEnabled(String radioButtonText);

    /**
     * Checks if the radio button identified by a locator is enabled.
     *
     * @param radioButtonLocator the locator identifying the radio button to check.
     * @return true if the radio button is enabled, otherwise false.
     */
    boolean isEnabled(By radioButtonLocator);

    /**
     * Checks if the specified radio button is selected within the given container.
     *
     * @param container       the container holding the radio button.
     * @param radioButtonText the text of the radio button to check.
     * @return true if the radio button is selected, otherwise false.
     */
    boolean isSelected(SmartWebElement container, String radioButtonText);

    /**
     * Checks if the specified radio button, identified by text, is selected without referencing a container.
     *
     * @param radioButtonText the text of the radio button to check.
     * @return true if the radio button is selected, otherwise false.
     */
    boolean isSelected(String radioButtonText);

    /**
     * Checks if the radio button identified by a locator is selected.
     *
     * @param radioButtonLocator the locator identifying the radio button to check.
     * @return true if the radio button is selected, otherwise false.
     */
    boolean isSelected(By radioButtonLocator);

    /**
     * Checks if the specified radio button is visible within the given container.
     *
     * @param container       the container holding the radio button.
     * @param radioButtonText the text of the radio button to check.
     * @return true if the radio button is visible, otherwise false.
     */
    boolean isVisible(SmartWebElement container, String radioButtonText);

    /**
     * Checks if the specified radio button, identified by text, is visible without referencing a container.
     *
     * @param radioButtonText the text of the radio button to check.
     * @return true if the radio button is visible, otherwise false.
     */
    boolean isVisible(String radioButtonText);

    /**
     * Checks if the radio button identified by a locator is visible.
     *
     * @param radioButtonLocator the locator identifying the radio button to check.
     * @return true if the radio button is visible, otherwise false.
     */
    boolean isVisible(By radioButtonLocator);

    /**
     * Retrieves the text of the currently selected radio button within the given container.
     *
     * @param container the container holding the radio buttons.
     * @return the text of the selected radio button, or an empty string if none is selected.
     */
    String getSelected(SmartWebElement container);

    /**
     * Retrieves the text of the currently selected radio button within the container located by the given locator.
     *
     * @param containerLocator the locator identifying the container.
     * @return the text of the selected radio button, or an empty string if none is selected.
     */
    String getSelected(By containerLocator);

    /**
     * Retrieves a list of all radio button texts within the given container.
     *
     * @param container the container holding the radio buttons.
     * @return a list of all available radio button texts.
     */
    List<String> getAll(SmartWebElement container);

    /**
     * Retrieves a list of all radio button texts within the container located by the given locator.
     *
     * @param containerLocator the locator identifying the container.
     * @return a list of all available radio button texts.
     */
    List<String> getAll(By containerLocator);

}
