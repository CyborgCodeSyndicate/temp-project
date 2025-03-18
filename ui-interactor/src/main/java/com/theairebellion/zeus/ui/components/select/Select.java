package com.theairebellion.zeus.ui.components.select;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

/**
 * Represents a selectable component, typically an HTML 'select' or similar UI element
 * that supports selecting multiple options. Provides methods to select options by text
 * or strategy, retrieve available or selected options, and check visibility or enabled states.
 *
 * <p>Implementations commonly rely on Selenium-based operations using container elements
 * ({@link SmartWebElement}) or locators ({@link By}). This interface standardizes how
 * select widgets are handled, regardless of the underlying framework or implementation.
 *
 * <p>Usage ranges from simple dropdown selects to more complex multi-select scenarios.
 * The {@code selectOptions()} methods allow specifying multiple values or applying a
 * {@link Strategy} for custom selection logic.</p>
 *
 * @author Cyborg Code Syndicate
 */
public interface Select {

    /**
     * Selects one or more options, identified by their display text or value,
     * within the specified container.
     *
     * @param container the container holding the select component.
     * @param values    one or more option texts or values to be selected.
     */
    void selectOptions(SmartWebElement container, String... values);

    /**
     * Selects one or more options, identified by their display text or value,
     * within the container located by the given locator.
     *
     * @param containerLocator the locator referencing the container.
     * @param values           one or more option texts or values to be selected.
     */
    void selectOptions(By containerLocator, String... values);

    /**
     * Selects one or more options based on a custom {@link Strategy}, returning the texts
     * of the selected options.
     *
     * @param container the container holding the select component.
     * @param strategy  the selection strategy determining which options to select.
     * @return a list of texts representing the newly selected options.
     */
    List<String> selectOptions(SmartWebElement container, Strategy strategy);

    /**
     * Selects one or more options, determined by a custom {@link Strategy}, within the container
     * located by the given locator.
     *
     * @param containerLocator the locator referencing the container.
     * @param strategy         the selection strategy determining which options to select.
     * @return a list of texts representing the newly selected options.
     */
    List<String> selectOptions(By containerLocator, Strategy strategy);

    /**
     * Retrieves all available option texts within the specified container.
     *
     * @param container the container holding the select component.
     * @return a list of strings representing the available options.
     */
    List<String> getAvailableOptions(SmartWebElement container);

    /**
     * Retrieves all available option texts within the container located by the given locator.
     *
     * @param containerLocator the locator referencing the container.
     * @return a list of strings representing the available options.
     */
    List<String> getAvailableOptions(By containerLocator);

    /**
     * Retrieves all currently selected option texts within the specified container.
     *
     * @param container the container holding the select component.
     * @return a list of strings representing the selected options.
     */
    List<String> getSelectedOptions(SmartWebElement container);

    /**
     * Retrieves all currently selected option texts within the container located by the given locator.
     *
     * @param containerLocator the locator referencing the container.
     * @return a list of strings representing the selected options.
     */
    List<String> getSelectedOptions(By containerLocator);

    /**
     * Checks if a specific option is visible, identified by display text or value, within the given container.
     *
     * @param container the container holding the select component.
     * @param value     the text or value identifying the option.
     * @return true if the option is visible, otherwise false.
     */
    boolean isOptionVisible(SmartWebElement container, String value);

    /**
     * Checks if a specific option is visible within the container located by the given locator.
     *
     * @param containerLocator the locator referencing the container.
     * @param value            the text or value identifying the option.
     * @return true if the option is visible, otherwise false.
     */
    boolean isOptionVisible(By containerLocator, String value);

    /**
     * Checks if a specific option is enabled, identified by display text or value,
     * within the given container.
     *
     * @param container the container holding the select component.
     * @param value     the text or value identifying the option.
     * @return true if the option is enabled, otherwise false.
     */
    boolean isOptionEnabled(SmartWebElement container, String value);

    /**
     * Checks if a specific option is enabled within the container located by the given locator.
     *
     * @param containerLocator the locator referencing the container.
     * @param value            the text or value identifying the option.
     * @return true if the option is enabled, otherwise false.
     */
    boolean isOptionEnabled(By containerLocator, String value);

}
