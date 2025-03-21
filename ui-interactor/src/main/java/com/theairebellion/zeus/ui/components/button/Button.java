package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

/**
 * Represents a UI button component with various ways to locate and interact
 * with buttons in a web interface.
 * <p>
 * Provides methods for clicking a button by text label, container reference,
 * or locator, as well as checking if the button is enabled or visible.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface Button {

    /**
     * Clicks a button identified by text, within a given container.
     *
     * @param container  The container holding the button.
     * @param buttonText The text label of the button to click.
     */
    void click(SmartWebElement container, String buttonText);

    /**
     * Clicks a button within the given container, selecting the first or only
     * button found in that container.
     *
     * @param container The container holding the button.
     */
    void click(SmartWebElement container);

    /**
     * Clicks a button identified by its text label, at the top level (no container).
     *
     * @param buttonText The text label of the button to click.
     */
    void click(String buttonText);

    /**
     * Clicks a button identified by a specific locator.
     *
     * @param buttonLocator A {@link By} locator for the button element.
     */
    void click(By buttonLocator);

    /**
     * Checks if a button (by text) inside the specified container is enabled.
     *
     * @param container  The container holding the button.
     * @param buttonText The text label of the button.
     * @return {@code true} if the button is enabled; {@code false} otherwise.
     */
    boolean isEnabled(SmartWebElement container, String buttonText);

    /**
     * Checks if the only or first button inside the specified container is enabled.
     *
     * @param container The container holding the button.
     * @return {@code true} if the button is enabled; {@code false} otherwise.
     */
    boolean isEnabled(SmartWebElement container);

    /**
     * Checks if a button identified by text is enabled at the top level (no container).
     *
     * @param buttonText The text label of the button.
     * @return {@code true} if the button is enabled; {@code false} otherwise.
     */
    boolean isEnabled(String buttonText);

    /**
     * Checks if a button identified by a locator is enabled.
     *
     * @param buttonLocator A {@link By} locator for the button element.
     * @return {@code true} if the button is enabled; {@code false} otherwise.
     */
    boolean isEnabled(By buttonLocator);

    /**
     * Checks if a button (by text) inside the specified container is visible on the page.
     *
     * @param container  The container holding the button.
     * @param buttonText The text label of the button.
     * @return {@code true} if the button is visible; {@code false} otherwise.
     */
    boolean isVisible(SmartWebElement container, String buttonText);

    /**
     * Checks if the only or first button inside the specified container is visible on the page.
     *
     * @param container The container holding the button.
     * @return {@code true} if the button is visible; {@code false} otherwise.
     */
    boolean isVisible(SmartWebElement container);

    /**
     * Checks if a button identified by text is visible at the top level (no container).
     *
     * @param buttonText The text label of the button.
     * @return {@code true} if the button is visible; {@code false} otherwise.
     */
    boolean isVisible(String buttonText);

    /**
     * Checks if a button identified by a locator is visible.
     *
     * @param buttonLocator A {@link By} locator for the button element.
     * @return {@code true} if the button is visible; {@code false} otherwise.
     */
    boolean isVisible(By buttonLocator);

    /**
     * Optionally clicks an element within a table cell or container; default no-op.
     * <p>
     * Overridden by implementations if cell-level actions for buttons are required.
     * </p>
     *
     * @param cell The cell element that may contain a button.
     */
    default void clickElementInCell(SmartWebElement cell) {
    }
}
