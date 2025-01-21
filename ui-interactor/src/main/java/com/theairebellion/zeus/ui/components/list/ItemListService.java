package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Interface for interacting with item lists in a web context.
 */
public interface ItemListService extends Insertion {

    UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);
    ItemListComponentType DEFAULT_TYPE = getDefaultType();

    /**
     * Selects items with the specified text within the given container
     * using the default item list component type.
     *
     * @param container The WebElement representing the container.
     * @param itemText  The text of the items to be selected.
     */
    default void select(WebElement container, String... itemText) {
        select(container, DEFAULT_TYPE, itemText);
    }

    /**
     * Selects items with the specified text and component type within the given container.
     *
     * @param container     The WebElement representing the container.
     * @param componentType The type of the item list component.
     * @param itemText      The text of the items to be selected.
     */
    void select(WebElement container, ItemListComponentType componentType, String... itemText);

    /**
     * Selects items with the specified text within the container located
     * by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be selected.
     */
    default void select(By containerLocator, String... itemText) {
        select(containerLocator, DEFAULT_TYPE, itemText);
    }

    /**
     * Selects items with the specified text and component type within the container
     * located by the given locator.
     *
     * @param containerLocator      The {@link By} locator representing the container.
     * @param componentType The type of the item list component.
     * @param itemText              The text of the items to be selected.
     */
    void select(By containerLocator, ItemListComponentType componentType, String... itemText);

    /**
     * Selects items using the specified strategy within the given container
     * using the default item list component type.
     *
     * @param container The WebElement representing the container.
     * @param strategy  The strategy to be used for selecting items.
     * @return The selected item.
     */
    default String select(WebElement container, Strategy strategy) {
        return select(container, strategy, DEFAULT_TYPE);
    }

    /**
     * Selects items using the specified strategy and component type within the given container.
     *
     * @param container             The WebElement representing the container.
     * @param strategy              The strategy to be used for selecting items.
     * @param componentType The type of the item list component.
     * @return The selected item.
     */
    String select(WebElement container, Strategy strategy, ItemListComponentType componentType);

    /**
     * Selects items using the specified strategy within the container located
     * by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param strategy         The strategy to be used for selecting items.
     * @return The selected item.
     */
    default String select(By containerLocator, Strategy strategy) {
        return select(containerLocator, strategy, DEFAULT_TYPE);
    }

    /**
     * Selects items using the specified strategy and component type within the container
     * located by the given locator.
     *
     * @param containerLocator      The {@link By} locator representing the container.
     * @param strategy              The strategy to be used for selecting items.
     * @param componentType The type of the item list component.
     * @return The selected item.
     */
    String select(By containerLocator, Strategy strategy, ItemListComponentType componentType);

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
     * @param itemText              The text of the items to be selected.
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
     * @param componentType The type of the item list component.
     * @param itemListLocator       The locator for the item list.
     */
    void select(ItemListComponentType componentType, By... itemListLocator);

    /**
     * Deselects items with the specified text within the given container
     * using the default item list component type.
     *
     * @param container The WebElement representing the container.
     * @param itemText  The text of the items to be deselected.
     */
    default void deSelect(WebElement container, String... itemText) {
        deSelect(container, DEFAULT_TYPE, itemText);
    }

    /**
     * Deselects items with the specified text and component type within the given container.
     *
     * @param container             The WebElement representing the container.
     * @param componentType The type of the item list component.
     * @param itemText              The text of the items to be deselected.
     */
    void deSelect(WebElement container, ItemListComponentType componentType, String... itemText);

    /**
     * Deselects items with the specified text within the container located
     * by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be deselected.
     */
    default void deSelect(By containerLocator, String... itemText) {
        deSelect(containerLocator, DEFAULT_TYPE, itemText);
    }

    /**
     * Deselects items with the specified text and component type within the container
     * located by the given locator.
     *
     * @param containerLocator      The {@link By} locator representing the container.
     * @param componentType The type of the item list component.
     * @param itemText              The text of the items to be deselected.
     */
    void deSelect(By containerLocator, ItemListComponentType componentType, String... itemText);

    /**
     * Deselects items using the specified strategy within the given container
     * using the default item list component type.
     *
     * @param container The WebElement representing the container.
     * @param strategy  The strategy to be used for deselecting items.
     * @return The deselected item.
     */
    default String deSelect(WebElement container, Strategy strategy) {
        return deSelect(container, strategy, DEFAULT_TYPE);
    }

    /**
     * Deselects items using the specified strategy and component type within the given container.
     *
     * @param container             The WebElement representing the container.
     * @param strategy              The strategy to be used for deselecting items.
     * @param componentType The type of the item list component.
     * @return The deselected item.
     */
    String deSelect(WebElement container, Strategy strategy, ItemListComponentType componentType);

    /**
     * Deselects items using the specified strategy within the container located
     * by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param strategy         The strategy to be used for deselecting items.
     * @return The deselected item.
     */
    default String deSelect(By containerLocator, Strategy strategy) {
        return deSelect(containerLocator, strategy, DEFAULT_TYPE);
    }

    /**
     * Deselects items using the specified strategy and component type within the container
     * located by the given locator.
     *
     * @param containerLocator      The {@link By} locator representing the container.
     * @param strategy              The strategy to be used for deselecting items.
     * @param componentType The type of the item list component.
     * @return The deselected item.
     */
    String deSelect(By containerLocator, Strategy strategy, ItemListComponentType componentType);

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
     * @param itemText              The text of the items to be deselected.
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
     * @param componentType The type of the item list component.
     * @param itemListLocator       The locator for the item list.
     */
    void deSelect(ItemListComponentType componentType, By... itemListLocator);

    /**
     * Checks if items with the specified text are selected within the given container
     * using the default item list component type.
     *
     * @param container The WebElement representing the container.
     * @param itemText  The text of the items to be checked.
     * @return true if all items are selected, {@code false} otherwise.
     */
    default boolean areSelected(WebElement container, String... itemText) {
        return areSelected(container, DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if items with the specified text and component type are selected within the given container.
     *
     * @param container             The WebElement representing the container.
     * @param componentType The type of the item list component.
     * @param itemText              The text of the items to be checked.
     * @return true if all items are selected, {@code false} otherwise.
     */
    boolean areSelected(WebElement container, ItemListComponentType componentType, String... itemText);

    /**
     * Checks if items with the specified text are selected within the container
     * located by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be checked.
     * @return true if all items are selected, {@code false} otherwise.
     */
    default boolean areSelected(By containerLocator, String... itemText) {
        return areSelected(containerLocator, DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if items with the specified text and component type are selected within the container
     * located by the given locator.
     *
     * @param containerLocator      The {@link By} locator representing the container.
     * @param componentType The type of the item list component.
     * @param itemText              The text of the items to be checked.
     * @return true if all items are selected, {@code false} otherwise.
     */
    boolean areSelected(By containerLocator, ItemListComponentType componentType, String... itemText);

    /**
     * Checks if items with the specified text are selected using the default item list component type.
     *
     * @param itemText The text of the items to be checked.
     * @return true if all items are selected, {@code false} otherwise.
     */
    default boolean areSelected(String... itemText) {
        return areSelected(DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if items with the specified text and component type are selected.
     *
     * @param componentType The type of the item list component.
     * @param itemText              The text of the items to be checked.
     * @return true if all items are selected, {@code false} otherwise.
     */
    boolean areSelected(ItemListComponentType componentType, String... itemText);

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
     * @param componentType The type of the item list component.
     * @param itemListLocator       The locator for the item list.
     * @return true if all items are selected, {@code false} otherwise.
     */
    boolean areSelected(ItemListComponentType componentType, By... itemListLocator);

    /**
     * Checks if items with the specified text are enabled within the given container
     * using the default item list component type.
     *
     * @param container The WebElement representing the container.
     * @param itemText  The text of the items to be checked.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    default boolean areEnabled(WebElement container, String... itemText) {
        return areEnabled(container, DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if items with the specified text and component type are enabled within the given container.
     *
     * @param container             The WebElement representing the container.
     * @param componentType The type of the item list component.
     * @param itemText              The text of the items to be checked.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    boolean areEnabled(WebElement container, ItemListComponentType componentType, String... itemText);

    /**
     * Checks if items with the specified text are enabled within the container
     * located by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be checked.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    default boolean areEnabled(By containerLocator, String... itemText) {
        return areEnabled(containerLocator, DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if items with the specified text and component type are enabled within the container
     * located by the given locator.
     *
     * @param containerLocator      The {@link By} locator representing the container.
     * @param componentType The type of the item list component.
     * @param itemText              The text of the items to be checked.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    boolean areEnabled(By containerLocator, ItemListComponentType componentType, String... itemText);

    /**
     * Checks if items with the specified text are enabled using the default item list component type.
     *
     * @param itemText The text of the items to be checked.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    default boolean areEnabled(String... itemText) {
        return areEnabled(DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if items with the specified text and component type are enabled.
     *
     * @param componentType The type of the item list component.
     * @param itemText              The text of the items to be checked.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    boolean areEnabled(ItemListComponentType componentType, String... itemText);

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
     * @param itemLocator           The locator for the items.
     * @return true if all items are enabled, {@code false} otherwise.
     */
    boolean areEnabled(ItemListComponentType componentType, By... itemLocator);

    /**
     * Checks if list items with the specified text are present within the given container
     * using the default item list component type.
     *
     * @param container The WebElement representing the container.
     * @param itemText  The text of the items to be checked.
     * @return true if list items are present, {@code false} otherwise.
     */
    default boolean arePresent(WebElement container, String... itemText) {
        return arePresent(container, DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if list items with the specified text and component type are present within the given container.
     *
     * @param container             The WebElement representing the container.
     * @param componentType The type of the item list component.
     * @param itemText              The text of the items to be checked.
     * @return true if list items are present, {@code false} otherwise.
     */
    boolean arePresent(WebElement container, ItemListComponentType componentType, String... itemText);

    /**
     * Checks if list items with the specified text are present within the container
     * located by the given locator using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @param itemText         The text of the items to be checked.
     * @return true if list items are present, {@code false} otherwise.
     */
    default boolean arePresent(By containerLocator, String... itemText) {
        return arePresent(containerLocator, DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if list items with the specified text and component type are present within the container
     * located by the given locator.
     *
     * @param containerLocator      The {@link By} locator representing the container.
     * @param componentType The type of the item list component.
     * @param itemText              The text of the items to be checked.
     * @return true if list items are present, {@code false} otherwise.
     */
    boolean arePresent(By containerLocator, ItemListComponentType componentType, String... itemText);

    /**
     * Checks if list items with the specified text are present
     * using the default item list component type.
     *
     * @param itemText The text of the items to be checked.
     * @return true if avatars are present, {@code false} otherwise.
     */
    default boolean arePresent(String... itemText) {
        return arePresent(DEFAULT_TYPE, itemText);
    }

    /**
     * Checks if list items with the specified text and component type are present.
     *
     * @param componentType The type of the item list component.
     * @param itemText              The text of the items to be checked.
     * @return true if list items are present, {@code false} otherwise.
     */
    boolean arePresent(ItemListComponentType componentType, String... itemText);

    /**
     * Checks if list items using the specified locator are present
     * using the default item list component type.
     *
     * @param itemLocator The locator for the items.
     * @return true if list items are present, {@code false} otherwise.
     */
    default boolean arePresent(By... itemLocator) {
        return arePresent(DEFAULT_TYPE, itemLocator);
    }

    /**
     * Checks if list items using the specified component type and locator are present.
     *
     * @param componentType The type of the item list component.
     * @param itemLocator           The locator for the items.
     * @return true if list items are present, {@code false} otherwise.
     */
    boolean arePresent(ItemListComponentType componentType, By... itemLocator);

    /**
     * Retrieves the text of selected items within the given container
     * using the default item list component type.
     *
     * @param container The WebElement representing the container.
     * @return A list of text of selected items.
     */
    default List<String> getSelected(WebElement container) {
        return getSelected(container, DEFAULT_TYPE);
    }

    /**
     * Retrieves the text of selected items with the specified component type within the given container.
     *
     * @param container             The WebElement representing the container.
     * @param componentType The type of the item list component.
     * @return A list of text of selected items.
     */
    List<String> getSelected(WebElement container, ItemListComponentType componentType);

    /**
     * Retrieves the text of selected items within the container located by the given locator
     * using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @return A list of text of selected items.
     */
    default List<String> getSelected(By containerLocator) {
        return getSelected(containerLocator, DEFAULT_TYPE);
    }

    /**
     * Retrieves the text of selected items with the specified component type within the container
     * located by the given locator.
     *
     * @param containerLocator      The {@link By} locator representing the container.
     * @param componentType The type of the item list component.
     * @return A list of text of selected items.
     */
    List<String> getSelected(By containerLocator, ItemListComponentType componentType);

    /**
     * Retrieves the text of all items within the given container using the default item list component type.
     *
     * @param container The WebElement representing the container.
     * @return A list of text of all items.
     */
    default List<String> getAll(WebElement container) {
        return getAll(container, DEFAULT_TYPE);
    }

    /**
     * Retrieves the text of all items with the specified component type within the given container.
     *
     * @param container             The WebElement representing the container.
     * @param componentType The type of the item list component.
     * @return A list of text of all items.
     */
    List<String> getAll(WebElement container, ItemListComponentType componentType);

    /**
     * Retrieves the text of all items within the container located by the given locator
     * using the default item list component type.
     *
     * @param containerLocator The {@link By} locator representing the container.
     * @return A list of text of all items.
     */
    default List<String> getAll(By containerLocator) {
        return getAll(containerLocator, DEFAULT_TYPE);
    }

    /**
     * Retrieves the text of all items with the specified component type within the container
     * located by the given locator.
     *
     * @param containerLocator      The {@link By} locator representing the container.
     * @param componentType The type of the item list component.
     * @return A list of text of all items.
     */
    List<String> getAll(By containerLocator, ItemListComponentType componentType);

    /**
     * Retrieves the default list component type from the configuration.
     *
     * @return The default ItemListComponentType.
     */
    private static ItemListComponentType getDefaultType() {
        return ReflectionUtil.findEnumImplementationsOfInterface(ItemListComponentType.class,
                uiConfig.listDefaultType(),
                uiConfig.projectPackage());
    }
}