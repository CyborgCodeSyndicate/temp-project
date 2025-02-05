package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.openqa.selenium.By;

import java.util.List;

import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;

/**
 * Interface for interacting with item lists in a web context.
 */
public interface ItemListService extends Insertion {

    ItemListComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Selects items with the specified text within the given container
     * using the default item list component type.
     *
     * @param container The SmartWebElement representing the container.
     * @param itemText  The text of the items to be selected.
     */
    default void select(SmartWebElement container, String... itemText) {
        select(DEFAULT_TYPE, container, itemText);
    }

    /**
     * Selects items with the specified text and component type within the given container.
     *
     * @param componentType The type of the item list component.
     * @param container     The SmartWebElement representing the container.
     * @param itemText      The text of the items to be selected.
     */
    void select(ItemListComponentType componentType, SmartWebElement container, String... itemText);

    /**
     * Selects items with the specified text within the container located
     * by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be selected.
     */
    default void select(By containerLocator, String... itemText) {
        select(DEFAULT_TYPE, containerLocator, itemText);
    }

    /**
     * Selects items with the specified text and component type within the container
     * located by the given locator.
     *
     * @param componentType    The type of the item list component.
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be selected.
     */
    void select(ItemListComponentType componentType, By containerLocator, String... itemText);

    /**
     * Selects items using the specified strategy within the given container
     * using the default item list component type.
     *
     * @param container The SmartWebElement representing the container.
     * @param strategy  The strategy to be used for selecting items.
     * @return The selected item.
     */
    default String select(SmartWebElement container, Strategy strategy) {
        return select(DEFAULT_TYPE, container, strategy);
    }

    /**
     * Selects items using the specified strategy and component type within the given container.
     *
     * @param componentType The type of the item list component.
     * @param container     The SmartWebElement representing the container.
     * @param strategy      The strategy to be used for selecting items.
     * @return The selected item.
     */
    String select(ItemListComponentType componentType, SmartWebElement container, Strategy strategy);

    /**
     * Selects items using the specified strategy within the container located
     * by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param strategy         The strategy to be used for selecting items.
     * @return The selected item.
     */
    default String select(By containerLocator, Strategy strategy) {
        return select(DEFAULT_TYPE, containerLocator, strategy);
    }

    /**
     * Selects items using the specified strategy and component type within the container
     * located by the given locator.
     *
     * @param componentType    The type of the item list component.
     * @param containerLocator The {@link By} locator representing the container.
     * @param strategy         The strategy to be used for selecting items.
     * @return The selected item.
     */
    String select(ItemListComponentType componentType, By containerLocator, Strategy strategy);

    /**
     * Selects items with the specified text using the default item list component type.
     *
     * @param itemText The text of the items to be selected.
     */
    default void select(String... itemText) {
        select(DEFAULT_TYPE, itemText);
    }

    /**
     * Selects items with the specified text and component type.
     *
     * @param componentType The type of the item list component.
     * @param itemText      The text of the items to be selected.
     */
    void select(ItemListComponentType componentType, String... itemText);

    /**
     * Selects items using the specified locator using the default item list component type.
     *
     * @param itemListLocator The locator for the item list.
     */
    default void select(By... itemListLocator) {
        select(DEFAULT_TYPE, itemListLocator);
    }

    /**
     * Selects items using the specified component type and locator.
     *
     * @param componentType   The type of the item list component.
     * @param itemListLocator The locator for the item list.
     */
    void select(ItemListComponentType componentType, By... itemListLocator);

    /**
     * Deselects items with the specified text within the given container
     * using the default item list component type.
     *
     * @param container The SmartWebElement representing the container.
     * @param itemText  The text of the items to be deselected.
     */
    default void deSelect(SmartWebElement container, String... itemText) {
        deSelect(DEFAULT_TYPE, container, itemText);
    }

    /**
     * Deselects items with the specified text and component type within the given container.
     *
     * @param componentType The type of the item list component.
     * @param container     The SmartWebElement representing the container.
     * @param itemText      The text of the items to be deselected.
     */
    void deSelect(ItemListComponentType componentType, SmartWebElement container, String... itemText);

    /**
     * Deselects items with the specified text within the container located
     * by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be deselected.
     */
    default void deSelect(By containerLocator, String... itemText) {
        deSelect(DEFAULT_TYPE, containerLocator, itemText);
    }

    /**
     * Deselects items with the specified text and component type within the container
     * located by the given locator.
     *
     * @param componentType    The type of the item list component.
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be deselected.
     */
    void deSelect(ItemListComponentType componentType, By containerLocator, String... itemText);

    /**
     * Deselects items using the specified strategy within the given container
     * using the default item list component type.
     *
     * @param container The SmartWebElement representing the container.
     * @param strategy  The strategy to be used for deselecting items.
     * @return The deselected item.
     */
    default String deSelect(SmartWebElement container, Strategy strategy) {
        return deSelect(DEFAULT_TYPE, container, strategy);
    }

    /**
     * Deselects items using the specified strategy and component type within the given container.
     *
     * @param componentType The type of the item list component.
     * @param container     The SmartWebElement representing the container.
     * @param strategy      The strategy to be used for deselecting items.
     * @return The deselected item.
     */
    String deSelect(ItemListComponentType componentType, SmartWebElement container, Strategy strategy);

    /**
     * Deselects items using the specified strategy within the container located
     * by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param strategy         The strategy to be used for deselecting items.
     * @return The deselected item.
     */
    default String deSelect(By containerLocator, Strategy strategy) {
        return deSelect(DEFAULT_TYPE, containerLocator, strategy);
    }

    /**
     * Deselects items using the specified strategy and component type within the container
     * located by the given locator.
     *
     * @param componentType    The type of the item list component.
     * @param containerLocator The {@link By} locator representing the container.
     * @param strategy         The strategy to be used for deselecting items.
     * @return The deselected item.
     */
    String deSelect(ItemListComponentType componentType, By containerLocator, Strategy strategy);

    /**
     * Deselects items with the specified text using the default item list component type.
     *
     * @param itemText The text of the items to be deselected.
     */
    default void deSelect(String... itemText) {
        deSelect(DEFAULT_TYPE, itemText);
    }

    /**
     * Deselects items with the specified text and component type.
     *
     * @param componentType The type of the item list component.
     * @param itemText      The text of the items to be deselected.
     */
    void deSelect(ItemListComponentType componentType, String... itemText);

    /**
     * Deselects items using the specified locator using the default item list component type.
     *
     * @param itemListLocator The locator for the item list.
     */
    default void deSelect(By... itemListLocator) {
        deSelect(DEFAULT_TYPE, itemListLocator);
    }

    /**
     * Deselects items using the specified component type and locator.
     *
     * @param componentType   The type of the item list component.
     * @param itemListLocator The locator for the item list.
     */
    void deSelect(ItemListComponentType componentType, By... itemListLocator);

    /**
     * Checks if items with the specified text are selected within the given container
     * using the default item list component type.
     *
     * @param container The SmartWebElement representing the container.
     * @param itemText  The text of the items to be selected.
     * @return true if all items are selected, {@code false} otherwise.
     */
    default boolean areSelected(SmartWebElement container, String... itemText) {
        return areSelected(DEFAULT_TYPE, container, itemText);
    }

    /**
     * Checks if items with the specified text and component type are selected within the given container.
     *
     * @param componentType The type of the item list component.
     * @param container     The SmartWebElement representing the container.
     * @param itemText      The text of the items to be selected.
     * @return true if all items are selected, {@code false} otherwise.
     */
    boolean areSelected(ItemListComponentType componentType, SmartWebElement container, String... itemText);

    /**
     * Checks if items with the specified text are selected within the given container
     * using the default item list component type.
     *
     * @param container The SmartWebElement representing the container.
     * @param itemText  The text of the item to be selected.
     * @return true if item is selected, {@code false} otherwise.
     */
    default boolean isSelected(SmartWebElement container, String itemText) {
        return isSelected(DEFAULT_TYPE, container, itemText);
    }

    /**
     * Checks if items with the specified text and component type are selected within the given container.
     *
     * @param componentType The type of the item list component.
     * @param container     The SmartWebElement representing the container.
     * @param itemText      The text of the item to be selected.
     * @return true if item is selected, {@code false} otherwise.
     */
    boolean isSelected(ItemListComponentType componentType, SmartWebElement container, String itemText);

    /**
     * Checks if items with the specified text are selected within the container
     * located by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be selected.
     * @return true if all items are selected, {@code false} otherwise.
     */
    default boolean areSelected(By containerLocator, String... itemText) {
        return areSelected(DEFAULT_TYPE, containerLocator, itemText);
    }

    /**
     * Checks if items with the specified text and component type are selected within the container
     * located by the given locator.
     *
     * @param componentType    The type of the item list component.
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be selected.
     * @return true if all items are selected, {@code false} otherwise.
     */
    boolean areSelected(ItemListComponentType componentType, By containerLocator, String... itemText);

    /**
     * Checks if items with the specified text are selected within the container
     * located by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the item to be selected.
     * @return true if item is selected, {@code false} otherwise.
     */
    default boolean isSelected(By containerLocator, String itemText) {
        return isSelected(DEFAULT_TYPE, containerLocator, itemText);
    }

    /**
     * Checks if items with the specified text and component type are selected within the container
     * located by the given locator.
     *
     * @param componentType    The type of the item list component.
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the item to be selected.
     * @return true if item is selected, {@code false} otherwise.
     */
    boolean isSelected(ItemListComponentType componentType, By containerLocator, String itemText);

    /**
     * Checks if items with the specified text are selected using the default item list component type.
     *
     * @param itemText The text of the items to be selected.
     * @return true if all items are selected, {@code false} otherwise.
     */
    default boolean areSelected(String... itemText) {
        return areSelected(DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if items with the specified text and component type are selected.
     *
     * @param componentType The type of the item list component.
     * @param itemText      The text of the items to be selected.
     * @return true if all items are selected, {@code false} otherwise.
     */
    boolean areSelected(ItemListComponentType componentType, String... itemText);

    /**
     * Checks if items with the specified text are selected using the default item list component type.
     *
     * @param itemText The text of the item to be selected.
     * @return true if item is selected, {@code false} otherwise.
     */
    default boolean isSelected(String itemText) {
        return isSelected(DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if items with the specified text and component type are selected.
     *
     * @param componentType The type of the item list component.
     * @param itemText      The text of the item to be selected.
     * @return true if item is selected, {@code false} otherwise.
     */
    boolean isSelected(ItemListComponentType componentType, String itemText);

    /**
     * Checks if items using the specified locator are selected using the default item list component type.
     *
     * @param itemListLocator The locator for the item list.
     * @return true if all items are selected, {@code false} otherwise.
     */
    default boolean areSelected(By... itemListLocator) {
        return areSelected(DEFAULT_TYPE, itemListLocator);
    }

    /**
     * Checks if items using the specified component type and locator are selected.
     *
     * @param componentType   The type of the item list component.
     * @param itemListLocator The locator for the item list.
     * @return true if all items are selected, {@code false} otherwise.
     */
    boolean areSelected(ItemListComponentType componentType, By... itemListLocator);

    /**
     * Checks if items using the specified locator are selected using the default item list component type.
     *
     * @param itemListLocator The locator for the item list.
     * @return true if item is selected, {@code false} otherwise.
     */
    default boolean isSelected(By itemListLocator) {
        return isSelected(DEFAULT_TYPE, itemListLocator);
    }

    /**
     * Checks if items using the specified component type and locator are selected.
     *
     * @param componentType   The type of the item list component.
     * @param itemListLocator The locator for the item list.
     * @return true if item is selected, {@code false} otherwise.
     */
    boolean isSelected(ItemListComponentType componentType, By itemListLocator);

    /**
     * Checks if items with the specified text are enabled within the given container
     * using the default item list component type.
     *
     * @param container The SmartWebElement representing the container.
     * @param itemText  The text of the items to be selected.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    default boolean areEnabled(SmartWebElement container, String... itemText) {
        return areEnabled(DEFAULT_TYPE, container, itemText);
    }

    /**
     * Checks if items with the specified text and component type are enabled within the given container.
     *
     * @param componentType The type of the item list component.
     * @param container     The SmartWebElement representing the container.
     * @param itemText      The text of the items to be selected.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    boolean areEnabled(ItemListComponentType componentType, SmartWebElement container, String... itemText);

    /**
     * Checks if items with the specified text are enabled within the given container
     * using the default item list component type.
     *
     * @param container The SmartWebElement representing the container.
     * @param itemText  The text of the item to be selected.
     * @return true if item is enabled, {@code false} otherwise.
     */
    default boolean isEnabled(SmartWebElement container, String itemText) {
        return isEnabled(DEFAULT_TYPE, container, itemText);
    }

    /**
     * Checks if items with the specified text and component type are enabled within the given container.
     *
     * @param componentType The type of the item list component.
     * @param container     The SmartWebElement representing the container.
     * @param itemText      The text of the item to be selected.
     * @return true if item is enabled, {@code false} otherwise.
     */
    boolean isEnabled(ItemListComponentType componentType, SmartWebElement container, String itemText);

    /**
     * Checks if items with the specified text are enabled within the container
     * located by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be selected.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    default boolean areEnabled(By containerLocator, String... itemText) {
        return areEnabled(DEFAULT_TYPE, containerLocator, itemText);
    }

    /**
     * Checks if items with the specified text and component type are enabled within the container
     * located by the given locator.
     *
     * @param componentType    The type of the item list component.
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be selected.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    boolean areEnabled(ItemListComponentType componentType, By containerLocator, String... itemText);

    /**
     * Checks if items with the specified text are enabled within the container
     * located by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the item to be selected.
     * @return true if item is enabled, {@code false} otherwise.
     */
    default boolean isEnabled(By containerLocator, String itemText) {
        return isEnabled(DEFAULT_TYPE, containerLocator, itemText);
    }

    /**
     * Checks if items with the specified text and component type are enabled within the container
     * located by the given locator.
     *
     * @param componentType    The type of the item list component.
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the item to be selected.
     * @return true if item is enabled, {@code false} otherwise.
     */
    boolean isEnabled(ItemListComponentType componentType, By containerLocator, String itemText);

    /**
     * Checks if items with the specified text are enabled using the default item list component type.
     *
     * @param itemText The text of the items to be selected.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    default boolean areEnabled(String... itemText) {
        return areEnabled(DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if items with the specified text and component type are enabled.
     *
     * @param componentType The type of the item list component.
     * @param itemText      The text of the items to be selected.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    boolean areEnabled(ItemListComponentType componentType, String... itemText);

    /**
     * Checks if items with the specified text are enabled using the default item list component type.
     *
     * @param itemText The text of the item to be selected.
     * @return true if item is enabled, {@code false} otherwise.
     */
    default boolean isEnabled(String itemText) {
        return isEnabled(DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if items with the specified text and component type are enabled.
     *
     * @param componentType The type of the item list component.
     * @param itemText      The text of the item to be selected.
     * @return true if item is enabled, {@code false} otherwise.
     */
    boolean isEnabled(ItemListComponentType componentType, String itemText);

    /**
     * Checks if items using the specified locator are enabled using the default item list component type.
     *
     * @param itemLocator The locator for the items.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    default boolean areEnabled(By... itemLocator) {
        return areEnabled(DEFAULT_TYPE, itemLocator);
    }

    /**
     * Checks if items using the specified component type and locator are enabled.
     *
     * @param componentType The type of the item list component.
     * @param itemLocator   The locator for the items.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    boolean areEnabled(ItemListComponentType componentType, By... itemLocator);

    /**
     * Checks if items using the specified locator are enabled using the default item list component type.
     *
     * @param itemLocator The locator for the item.
     * @return true if item is enabled, {@code false} otherwise.
     */
    default boolean isEnabled(By itemLocator) {
        return isEnabled(DEFAULT_TYPE, itemLocator);
    }

    /**
     * Checks if items using the specified component type and locator are enabled.
     *
     * @param componentType The type of the item list component.
     * @param itemLocator   The locator for the item.
     * @return true if item is enabled, {@code false} otherwise.
     */
    boolean isEnabled(ItemListComponentType componentType, By itemLocator);

    /**
     * Checks if list items with the specified text are visible within the given container
     * using the default item list component type.
     *
     * @param container The SmartWebElement representing the container.
     * @param itemText  The text of the items to be selected.
     * @return true if list items are visible, {@code false} otherwise.
     */
    default boolean areVisible(SmartWebElement container, String... itemText) {
        return areVisible(DEFAULT_TYPE, container, itemText);
    }

    /**
     * Checks if list items with the specified text and component type are visible within the given container.
     *
     * @param componentType The type of the item list component.
     * @param container     The SmartWebElement representing the container.
     * @param itemText      The text of the items to be selected.
     * @return true if list items are visible, {@code false} otherwise.
     */
    boolean areVisible(ItemListComponentType componentType, SmartWebElement container, String... itemText);

    /**
     * Checks if list items with the specified text are visible within the given container
     * using the default item list component type.
     *
     * @param container The SmartWebElement representing the container.
     * @param itemText  The text of the item to be selected.
     * @return true if list item is visible, {@code false} otherwise.
     */
    default boolean isVisible(SmartWebElement container, String itemText) {
        return isVisible(DEFAULT_TYPE, container, itemText);
    }

    /**
     * Checks if list items with the specified text and component type are visible within the given container.
     *
     * @param componentType The type of the item list component.
     * @param container     The SmartWebElement representing the container.
     * @param itemText      The text of the item to be selected.
     * @return true if list item is visible, {@code false} otherwise.
     */
    boolean isVisible(ItemListComponentType componentType, SmartWebElement container, String itemText);

    /**
     * Checks if list items with the specified text are visible within the container
     * located by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be selected.
     * @return true if list items are visible, {@code false} otherwise.
     */
    default boolean areVisible(By containerLocator, String... itemText) {
        return areVisible(DEFAULT_TYPE, containerLocator, itemText);
    }

    /**
     * Checks if list items with the specified text and component type are visible within the container
     * located by the given locator.
     *
     * @param componentType    The type of the item list component.
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be selected.
     * @return true if list items are visible, {@code false} otherwise.
     */
    boolean areVisible(ItemListComponentType componentType, By containerLocator, String... itemText);

    /**
     * Checks if list items with the specified text are visible within the container
     * located by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the item to be selected.
     * @return true if list item is visible, {@code false} otherwise.
     */
    default boolean isVisible(By containerLocator, String itemText) {
        return isVisible(DEFAULT_TYPE, containerLocator, itemText);
    }

    /**
     * Checks if list items with the specified text and component type are visible within the container
     * located by the given locator.
     *
     * @param componentType    The type of the item list component.
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the item to be selected.
     * @return true if list item is visible, {@code false} otherwise.
     */
    boolean isVisible(ItemListComponentType componentType, By containerLocator, String itemText);

    /**
     * Checks if list items with the specified text are visible
     * using the default item list component type.
     *
     * @param itemText The text of the items to be selected.
     * @return true if avatars are visible, {@code false} otherwise.
     */
    default boolean areVisible(String... itemText) {
        return areVisible(DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if list items with the specified text and component type are visible.
     *
     * @param componentType The type of the item list component.
     * @param itemText      The text of the items to be selected.
     * @return true if list items are visible, {@code false} otherwise.
     */
    boolean areVisible(ItemListComponentType componentType, String... itemText);

    /**
     * Checks if list items with the specified text are visible
     * using the default item list component type.
     *
     * @param itemText The text of the item to be selected.
     * @return true if list item is visible, {@code false} otherwise.
     */
    default boolean isVisible(String itemText) {
        return isVisible(DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if list items with the specified text and component type are visible.
     *
     * @param componentType The type of the item list component.
     * @param itemText      The text of the item to be selected.
     * @return true if list item is visible, {@code false} otherwise.
     */
    boolean isVisible(ItemListComponentType componentType, String itemText);

    /**
     * Checks if list items using the specified locator are visible
     * using the default item list component type.
     *
     * @param itemLocator The locator for the items.
     * @return true if list items are visible, {@code false} otherwise.
     */
    default boolean areVisible(By... itemLocator) {
        return areVisible(DEFAULT_TYPE, itemLocator);
    }

    /**
     * Checks if list items using the specified component type and locator are visible.
     *
     * @param componentType The type of the item list component.
     * @param itemLocator   The locator for the items.
     * @return true if list items are visible, {@code false} otherwise.
     */
    boolean areVisible(ItemListComponentType componentType, By... itemLocator);

    /**
     * Checks if list items using the specified locator are visible
     * using the default item list component type.
     *
     * @param itemLocator The locator for the item.
     * @return true if list item is visible, {@code false} otherwise.
     */
    default boolean isVisible(By itemLocator) {
        return isVisible(DEFAULT_TYPE, itemLocator);
    }

    /**
     * Checks if list items using the specified component type and locator are visible.
     *
     * @param componentType The type of the item list component.
     * @param itemLocator   The locator for the item.
     * @return true if list item is visible, {@code false} otherwise.
     */
    boolean isVisible(ItemListComponentType componentType, By itemLocator);

    /**
     * Retrieves the text of selected items within the given container
     * using the default item list component type.
     *
     * @param container The SmartWebElement representing the container.
     * @return A list of text of selected items.
     */
    default List<String> getSelected(SmartWebElement container) {
        return getSelected(DEFAULT_TYPE, container);
    }

    /**
     * Retrieves the text of selected items with the specified component type within the given container.
     *
     * @param componentType The type of the item list component.
     * @param container     The SmartWebElement representing the container.
     * @return A list of text of selected items.
     */
    List<String> getSelected(ItemListComponentType componentType, SmartWebElement container);

    /**
     * Retrieves the text of selected items within the container located by the given locator
     * using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @return A list of text of selected items.
     */
    default List<String> getSelected(By containerLocator) {
        return getSelected(DEFAULT_TYPE, containerLocator);
    }

    /**
     * Retrieves the text of selected items with the specified component type within the container
     * located by the given locator.
     *
     * @param componentType    The type of the item list component.
     * @param containerLocator The {@link By} locator representing the container.
     * @return A list of text of selected items.
     */
    List<String> getSelected(ItemListComponentType componentType, By containerLocator);

    /**
     * Retrieves the text of all items within the given container using the default item list component type.
     *
     * @param container The SmartWebElement representing the container.
     * @return A list of text of all items.
     */
    default List<String> getAll(SmartWebElement container) {
        return getAll(DEFAULT_TYPE, container);
    }

    /**
     * Retrieves the text of all items with the specified component type within the given container.
     *
     * @param componentType The type of the item list component.
     * @param container     The SmartWebElement representing the container.
     * @return A list of text of all items.
     */
    List<String> getAll(ItemListComponentType componentType, SmartWebElement container);

    /**
     * Retrieves the text of all items within the container located by the given locator
     * using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @return A list of text of all items.
     */
    default List<String> getAll(By containerLocator) {
        return getAll(DEFAULT_TYPE, containerLocator);
    }

    /**
     * Retrieves the text of all items with the specified component type within the container
     * located by the given locator.
     *
     * @param componentType    The type of the item list component.
     * @param containerLocator The {@link By} locator representing the container.
     * @return A list of text of all items.
     */
    List<String> getAll(ItemListComponentType componentType, By containerLocator);

    /**
     * Retrieves the default list component type from the configuration.
     *
     * @return The default ItemListComponentType.
     */
    private static ItemListComponentType getDefaultType() {
        try {
            return ReflectionUtil.findEnumImplementationsOfInterface(ItemListComponentType.class,
                    getUiConfig().listDefaultType(),
                    getUiConfig().projectPackage());
        } catch (Exception ignored) {
            return null;
        }
    }
}