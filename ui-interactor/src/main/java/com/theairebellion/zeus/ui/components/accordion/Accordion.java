package com.theairebellion.zeus.ui.components.accordion;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

/**
 * Defines the contract for an accordion UI component.
 * <p>
 * An accordion typically consists of multiple sections or panels
 * that can be expanded or collapsed to show or hide content.
 * Implementations of this interface provide consistent ways to
 * manipulate accordion panels by expanding or collapsing them,
 * as well as checking their state.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public interface Accordion {

    /**
     * Expands specific accordion panels within a given container.
     *
     * @param container     The container element holding the accordion.
     * @param accordionText One or more text labels identifying the panels to expand.
     */
    void expand(SmartWebElement container, String... accordionText);

    /**
     * Expands an accordion panel determined by a strategy,
     * such as selecting a random or first panel.
     *
     * @param container The container element holding the accordion.
     * @param strategy  The strategy for determining which panel to expand.
     * @return A string indicating which panel was expanded, if applicable.
     */
    String expand(SmartWebElement container, Strategy strategy);

    /**
     * Expands one or more accordion panels by text label, without specifying a container.
     *
     * @param accordionText One or more text labels identifying the panels to expand.
     */
    void expand(String... accordionText);

    /**
     * Expands accordion panels identified by one or more locators.
     *
     * @param accordionLocator One or more {@link By} locators for the target panels.
     */
    void expand(By... accordionLocator);

    /**
     * Collapses specific accordion panels within a given container.
     *
     * @param container     The container element holding the accordion.
     * @param accordionText One or more text labels identifying the panels to collapse.
     */
    void collapse(SmartWebElement container, String... accordionText);

    /**
     * Collapses an accordion panel determined by a strategy.
     *
     * @param container The container element holding the accordion.
     * @param strategy  The strategy for determining which panel to collapse.
     * @return A string indicating which panel was collapsed, if applicable.
     */
    String collapse(SmartWebElement container, Strategy strategy);

    /**
     * Collapses one or more accordion panels by text label, without specifying a container.
     *
     * @param accordionText One or more text labels identifying the panels to collapse.
     */
    void collapse(String... accordionText);

    /**
     * Collapses accordion panels identified by one or more locators.
     *
     * @param accordionLocator One or more {@link By} locators for the target panels.
     */
    void collapse(By... accordionLocator);

    /**
     * Checks whether specific accordion panels are enabled within a given container.
     *
     * @param container     The container element holding the accordion.
     * @param accordionText One or more text labels identifying the panels to check.
     * @return {@code true} if all specified panels are enabled, otherwise {@code false}.
     */
    boolean areEnabled(SmartWebElement container, String... accordionText);

    /**
     * Checks whether specific accordion panels (by text) are enabled, without a container.
     *
     * @param accordionText One or more text labels identifying the panels to check.
     * @return {@code true} if all specified panels are enabled, otherwise {@code false}.
     */
    boolean areEnabled(String... accordionText);

    /**
     * Checks whether accordion panels identified by one or more locators are enabled.
     *
     * @param accordionLocator One or more {@link By} locators for the target panels.
     * @return {@code true} if all specified panels are enabled, otherwise {@code false}.
     */
    boolean areEnabled(By... accordionLocator);

    /**
     * Retrieves a list of text labels for accordion panels currently in an expanded state.
     *
     * @param container The container element holding the accordion.
     * @return A list of expanded panel labels.
     */
    List<String> getExpanded(SmartWebElement container);

    /**
     * Retrieves a list of text labels for accordion panels currently in a collapsed state.
     *
     * @param container The container element holding the accordion.
     * @return A list of collapsed panel labels.
     */
    List<String> getCollapsed(SmartWebElement container);

    /**
     * Retrieves a list of all panel labels within the accordion container,
     * regardless of their expanded or collapsed state.
     *
     * @param container The container element holding the accordion.
     * @return A list of all panel labels in the accordion.
     */
    List<String> getAll(SmartWebElement container);

    /**
     * Retrieves the title text from a specific accordion panel using a locator.
     *
     * @param accordionLocator The {@link By} locator identifying the target panel.
     * @return The title text of the accordion panel.
     */
    String getTitle(By accordionLocator);

    /**
     * Retrieves the main text content from a specific accordion panel using a locator.
     *
     * @param accordionLocator The {@link By} locator identifying the target panel.
     * @return The text content of the accordion panel.
     */
    String getText(By accordionLocator);

}
