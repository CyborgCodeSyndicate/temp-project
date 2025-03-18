package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.List;

/**
 * Represents a list-based component allowing items to be selected, deselected, verified, or retrieved.
 * Provides methods for interacting with items via text, container locators, and custom strategies.
 *
 * <p>Usage examples include selecting items by text, deselecting them, checking their state (selected, visible, or enabled),
 * and retrieving all or only the selected items from a list component.</p>
 *
 * @author Cyborg Code Syndicate
 */
public interface ItemList {

    /**
     * Selects one or more items, identified by the provided text, within the specified container.
     *
     * @param container the container element holding the item list.
     * @param itemText  one or more text labels identifying the items to select.
     */
    void select(SmartWebElement container, String... itemText);

    /**
     * Selects one or more items, identified by the provided text, within the container located by the specified locator.
     *
     * @param containerLocator the locator used to find the container element.
     * @param itemText         one or more text labels identifying the items to select.
     */
    void select(By containerLocator, String... itemText);

    /**
     * Selects items in the specified container using a custom strategy.
     *
     * @param container the container element holding the item list.
     * @param strategy  the strategy defining how the items should be selected.
     * @return a string representation of the selection result, if applicable.
     */
    String select(SmartWebElement container, Strategy strategy);

    /**
     * Selects items within the container located by the specified locator, using a custom strategy.
     *
     * @param containerLocator the locator used to find the container element.
     * @param strategy         the strategy defining how the items should be selected.
     * @return a string representation of the selection result, if applicable.
     */
    String select(By containerLocator, Strategy strategy);

    /**
     * Selects one or more items by text without specifying a container, if the default container is implicitly known.
     *
     * @param itemText one or more text labels identifying the items to select.
     */
    void select(String... itemText);

    /**
     * Selects items via one or more locators.
     *
     * @param itemListLocator one or more locators identifying the items to select.
     */
    void select(By... itemListLocator);

    /**
     * Deselects one or more items, identified by the provided text, within the specified container.
     *
     * @param container the container element holding the item list.
     * @param itemText  one or more text labels identifying the items to deselect.
     */
    void deSelect(SmartWebElement container, String... itemText);

    /**
     * Deselects one or more items, identified by the provided text, within the container located by the specified locator.
     *
     * @param containerLocator the locator used to find the container element.
     * @param itemText         one or more text labels identifying the items to deselect.
     */
    void deSelect(By containerLocator, String... itemText);

    /**
     * Deselects items in the specified container using a custom strategy.
     *
     * @param container the container element holding the item list.
     * @param strategy  the strategy defining how the items should be deselected.
     * @return a string representation of the deselection result, if applicable.
     */
    String deSelect(SmartWebElement container, Strategy strategy);

    /**
     * Deselects items within the container located by the specified locator, using a custom strategy.
     *
     * @param containerLocator the locator used to find the container element.
     * @param strategy         the strategy defining how the items should be deselected.
     * @return a string representation of the deselection result, if applicable.
     */
    String deSelect(By containerLocator, Strategy strategy);

    /**
     * Deselects one or more items by text without specifying a container, if the default container is implicitly known.
     *
     * @param itemText one or more text labels identifying the items to deselect.
     */
    void deSelect(String... itemText);

    /**
     * Deselects items identified by one or more locators.
     *
     * @param itemListLocator one or more locators identifying the items to deselect.
     */
    void deSelect(By... itemListLocator);

    /**
     * Determines if the specified items, identified by text, are selected within the specified container.
     *
     * @param container the container element holding the item list.
     * @param itemText  one or more text labels identifying the items to check.
     * @return true if all specified items are selected, otherwise false.
     */
    boolean areSelected(SmartWebElement container, String... itemText);

    /**
     * Determines if the specified items, identified by text, are selected within the container located by the specified locator.
     *
     * @param containerLocator the locator used to find the container element.
     * @param itemText         one or more text labels identifying the items to check.
     * @return true if all specified items are selected, otherwise false.
     */
    boolean areSelected(By containerLocator, String... itemText);

    /**
     * Determines if the specified items, identified by text, are selected without specifying a container.
     *
     * @param itemText one or more text labels identifying the items to check.
     * @return true if all specified items are selected, otherwise false.
     */
    boolean areSelected(String... itemText);

    /**
     * Determines if the specified items are selected using one or more locators.
     *
     * @param itemListLocator one or more locators identifying the items to check.
     * @return true if all located items are selected, otherwise false.
     */
    boolean areSelected(By... itemListLocator);

    /**
     * Determines if the specified items, identified by text, are enabled within the specified container.
     *
     * @param container the container element holding the item list.
     * @param itemText  one or more text labels identifying the items to check.
     * @return true if all specified items are enabled, otherwise false.
     */
    boolean areEnabled(SmartWebElement container, String... itemText);

    /**
     * Determines if the specified items, identified by text, are enabled within the container located by the specified locator.
     *
     * @param containerLocator the locator used to find the container element.
     * @param itemText         one or more text labels identifying the items to check.
     * @return true if all specified items are enabled, otherwise false.
     */
    boolean areEnabled(By containerLocator, String... itemText);

    /**
     * Determines if the specified items, identified by text, are enabled without specifying a container.
     *
     * @param itemText one or more text labels identifying the items to check.
     * @return true if all specified items are enabled, otherwise false.
     */
    boolean areEnabled(String... itemText);

    /**
     * Determines if items identified by one or more locators are enabled.
     *
     * @param itemLocator one or more locators identifying the items to check.
     * @return true if all located items are enabled, otherwise false.
     */
    boolean areEnabled(By... itemLocator);

    /**
     * Determines if the specified items, identified by text, are visible within the specified container.
     *
     * @param container the container element holding the item list.
     * @param itemText  one or more text labels identifying the items to check.
     * @return true if all specified items are visible, otherwise false.
     */
    boolean areVisible(SmartWebElement container, String... itemText);

    /**
     * Determines if the specified items, identified by text, are visible within the container located by the specified locator.
     *
     * @param containerLocator the locator used to find the container element.
     * @param itemText         one or more text labels identifying the items to check.
     * @return true if all specified items are visible, otherwise false.
     */
    boolean areVisible(By containerLocator, String... itemText);

    /**
     * Determines if the specified items, identified by text, are visible without specifying a container.
     *
     * @param itemText one or more text labels identifying the items to check.
     * @return true if all specified items are visible, otherwise false.
     */
    boolean areVisible(String... itemText);

    /**
     * Determines if items identified by one or more locators are visible.
     *
     * @param itemLocator one or more locators identifying the items to check.
     * @return true if all located items are visible, otherwise false.
     */
    boolean areVisible(By... itemLocator);

    /**
     * Retrieves the currently selected items as text from the specified container.
     *
     * @param container the container element holding the item list.
     * @return a list of text values for the selected items.
     */
    List<String> getSelected(SmartWebElement container);

    /**
     * Retrieves the currently selected items as text from the container located by the specified locator.
     *
     * @param containerLocator the locator used to find the container element.
     * @return a list of text values for the selected items.
     */
    List<String> getSelected(By containerLocator);

    /**
     * Retrieves all items as text from the specified container.
     *
     * @param container the container element holding the item list.
     * @return a list of all item text values.
     */
    List<String> getAll(SmartWebElement container);

    /**
     * Retrieves all items as text from the container located by the specified locator.
     *
     * @param containerLocator the locator used to find the container element.
     * @return a list of all item text values.
     */
    List<String> getAll(By containerLocator);

}
