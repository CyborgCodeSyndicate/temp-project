package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the {@link ItemListService} interface providing operations for
 * selecting, deselecting, verifying, and retrieving items in a list-based component.
 * Extends {@link AbstractComponentService} to manage creation and retrieval of
 * {@link ItemList} instances.
 *
 * @author Cyborg Code Syndicate
 */
public class ItemListServiceImpl extends AbstractComponentService<ItemListComponentType, ItemList>
        implements ItemListService {

    private static final String SELECT_ITEMS_WITH_TEXT = "Selecting items %s from list %s";
    private static final String SELECT_ITEMS_WITH_TEXT_BY_LOCATOR = "Selecting items %s from list %s using locator %s";
    private static final String SELECT_ITEMS_USING_STRATEGY = "Selecting items from list %s using strategy %s";
    private static final String SELECT_ITEMS_LOCATOR_AND_STRATEGY =
            "Selecting items from list %s using locator %s and strategy %s";
    private static final String SELECT_ITEMS_NO_TEXT_BY_LOCATOR = "Selecting items from list %s using locator %s";

    private static final String DESELECT_ITEMS_WITH_TEXT = "De-selecting items %s from list %s";
    private static final String DESELECT_ITEMS_WITH_TEXT_BY_LOCATOR =
            "De-selecting items %s from list %s using locator %s";
    private static final String DESELECT_ITEMS_USING_STRATEGY = "De-selecting items from list %s using strategy %s";
    private static final String DESELECT_ITEMS_LOCATOR_AND_STRATEGY =
            "De-selecting items from list %s using locator %s and strategy %s";
    private static final String DESELECT_ITEMS_NO_TEXT_BY_LOCATOR = "De-selecting items from list %s using locator %s";

    private static final String CHECK_ITEMS_SELECTED_IN_LIST = "Checking if items %s are selected in list %s";
    private static final String CHECK_ITEMS_SELECTED_IN_LIST_BY_LOCATOR =
            "Checking if items %s are selected in list %s using locator %s";
    private static final String CHECK_ITEMS_NO_TEXT_SELECTED_BY_LOCATOR =
            "Checking if items are selected in list %s using locator %s";
    private static final String CHECK_ITEM_SELECTED_IN_LIST = "Checking if item %s is selected in list %s";
    private static final String CHECK_ITEM_SELECTED_IN_LIST_BY_LOCATOR =
            "Checking if item %s is selected in list %s using locator %s";
    private static final String CHECK_ITEM_NO_TEXT_SELECTED_BY_LOCATOR =
            "Checking if item is selected in list %s using locator %s";

    private static final String CHECK_ITEMS_ENABLED_IN_LIST = "Checking if items %s are enabled in list %s";
    private static final String CHECK_ITEMS_ENABLED_IN_LIST_BY_LOCATOR =
            "Checking if items %s are enabled in list %s using locator %s";
    private static final String CHECK_ITEMS_NO_TEXT_ENABLED_BY_LOCATOR =
            "Checking if items are enabled in list %s using locator %s";
    private static final String CHECK_ITEM_ENABLED_IN_LIST = "Checking if item %s is enabled in list %s";
    private static final String CHECK_ITEM_ENABLED_IN_LIST_BY_LOCATOR =
            "Checking if item %s is enabled in list %s using locator %s";
    private static final String CHECK_ITEM_NO_TEXT_ENABLED_BY_LOCATOR =
            "Checking if item is enabled in list %s using locator %s";

    private static final String CHECK_ITEMS_VISIBLE_IN_LIST = "Checking if items %s are visible in list %s";
    private static final String CHECK_ITEMS_VISIBLE_IN_LIST_BY_LOCATOR =
            "Checking if items %s are visible in list %s using locator %s";
    private static final String CHECK_ITEMS_NO_TEXT_VISIBLE_BY_LOCATOR =
            "Checking if items are visible in list %s using locator %s";
    private static final String CHECK_ITEM_VISIBLE_IN_LIST = "Checking if item %s is visible in list %s";
    private static final String CHECK_ITEM_VISIBLE_IN_LIST_BY_LOCATOR =
            "Checking if item %s is visible in list %s using locator %s";
    private static final String CHECK_ITEM_NO_TEXT_VISIBLE_BY_LOCATOR =
            "Checking if item is visible in list %s using locator %s";

    private static final String GET_SELECTED_ITEMS_CONTAINER = "Getting selected items from list %s using container";
    private static final String GET_SELECTED_ITEMS_LOCATOR = "Getting selected items from list %s using locator %s";
    private static final String GET_ALL_ITEMS_CONTAINER = "Getting all items from list %s using container";
    private static final String GET_ALL_ITEMS_LOCATOR = "Getting all items from list %s using locator %s";

    private static final String INSERT_VALUES_INTO_COMPONENT_LOCATOR =
            "Inserting values %s into component %s using locator %s";


    /**
     * Constructs a new {@code ItemListServiceImpl} with the provided {@link SmartWebDriver}.
     *
     * @param driver the smart web driver used to interact with browser elements.
     */
    public ItemListServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    /**
     * Creates and returns an {@link ItemList} component instance based on the provided
     * {@link ItemListComponentType}.
     *
     * @param componentType the specific type of the item list component.
     * @return a new {@link ItemList} instance for the given component type.
     */
    @Override
    protected ItemList createComponent(final ItemListComponentType componentType) {
        return ComponentFactory.getListComponent(componentType, driver);
    }

    /**
     * Selects items identified by one or more texts within the specified container.
     *
     * @param componentType the type of the item list component.
     * @param container     the container in which to select items.
     * @param itemText      one or more text identifiers for the items.
     */
    @Override
    public void select(final ItemListComponentType componentType, final SmartWebElement container,
                       final String... itemText) {
        LogUI.step(String.format(SELECT_ITEMS_WITH_TEXT, String.join(", ", itemText), componentType));
        itemListComponent(componentType).select(container, itemText);
    }

    /**
     * Selects items identified by one or more texts within a container located by a given locator.
     *
     * @param componentType    the type of the item list component.
     * @param containerLocator the locator for the container in which to select items.
     * @param itemText         one or more text identifiers for the items.
     */
    @Override
    public void select(final ItemListComponentType componentType, final By containerLocator, final String... itemText) {
        LogUI.step(String.format(SELECT_ITEMS_WITH_TEXT_BY_LOCATOR, String.join(", ", itemText), componentType,
                containerLocator));
        itemListComponent(componentType).select(containerLocator, itemText);
    }

    /**
     * Selects items in the specified container using a custom {@link Strategy}.
     *
     * @param componentType the type of the item list component.
     * @param container     the container in which to select items.
     * @param strategy      the selection strategy to apply.
     * @return a string representation of the selected item(s), if available.
     */
    @Override
    public String select(final ItemListComponentType componentType, final SmartWebElement container,
                         final Strategy strategy) {
        LogUI.step(String.format(SELECT_ITEMS_USING_STRATEGY, componentType, strategy));
        return itemListComponent(componentType).select(container, strategy);
    }

    /**
     * Selects items in a container (located by a given locator) using a custom {@link Strategy}.
     *
     * @param componentType    the type of the item list component.
     * @param containerLocator the locator for the container in which to select items.
     * @param strategy         the selection strategy to apply.
     * @return a string representation of the selected item(s), if available.
     */
    @Override
    public String select(final ItemListComponentType componentType, final By containerLocator,
                         final Strategy strategy) {
        LogUI.step(String.format(SELECT_ITEMS_LOCATOR_AND_STRATEGY, componentType, containerLocator, strategy));
        return itemListComponent(componentType).select(containerLocator, strategy);
    }

    /**
     * Selects items identified by one or more texts, without specifying a container.
     *
     * @param componentType the type of the item list component.
     * @param itemText      one or more text identifiers for the items.
     */
    @Override
    public void select(final ItemListComponentType componentType, final String... itemText) {
        LogUI.step(String.format(SELECT_ITEMS_WITH_TEXT, String.join(", ", itemText), componentType));
        itemListComponent(componentType).select(itemText);
    }

    /**
     * Selects items using the specified locator(s) without specifying a container.
     *
     * @param componentType   the type of the item list component.
     * @param itemListLocator one or more locators identifying the items to select.
     */
    @Override
    public void select(final ItemListComponentType componentType, final By... itemListLocator) {
        LogUI.step(String.format(SELECT_ITEMS_NO_TEXT_BY_LOCATOR, componentType, Arrays.toString(itemListLocator)));
        itemListComponent(componentType).select(itemListLocator);
    }

    /**
     * Deselects items identified by one or more texts within the specified container.
     *
     * @param componentType the type of the item list component.
     * @param container     the container in which to deselect items.
     * @param itemText      one or more text identifiers for the items.
     */
    @Override
    public void deSelect(final ItemListComponentType componentType, final SmartWebElement container,
                         final String... itemText) {
        LogUI.step(String.format(DESELECT_ITEMS_WITH_TEXT, String.join(", ", itemText), componentType));
        itemListComponent(componentType).deSelect(container, itemText);
    }

    /**
     * Deselects items identified by one or more texts within a container located by a given locator.
     *
     * @param componentType    the type of the item list component.
     * @param containerLocator the locator for the container in which to deselect items.
     * @param itemText         one or more text identifiers for the items.
     */
    @Override
    public void deSelect(final ItemListComponentType componentType, final By containerLocator,
                         final String... itemText) {
        LogUI.step(String.format(DESELECT_ITEMS_WITH_TEXT_BY_LOCATOR, String.join(", ", itemText), componentType,
                containerLocator));
        itemListComponent(componentType).deSelect(containerLocator, itemText);
    }

    /**
     * Deselects items in the specified container using a custom {@link Strategy}.
     *
     * @param componentType the type of the item list component.
     * @param container     the container in which to deselect items.
     * @param strategy      the deselection strategy to apply.
     * @return a string representation of the deselected item(s), if available.
     */
    @Override
    public String deSelect(final ItemListComponentType componentType, final SmartWebElement container,
                           final Strategy strategy) {
        LogUI.step(String.format(DESELECT_ITEMS_USING_STRATEGY, componentType, strategy));
        return itemListComponent(componentType).deSelect(container, strategy);
    }

    /**
     * Deselects items in a container (located by a given locator) using a custom {@link Strategy}.
     *
     * @param componentType    the type of the item list component.
     * @param containerLocator the locator for the container in which to deselect items.
     * @param strategy         the deselection strategy to apply.
     * @return a string representation of the deselected item(s), if available.
     */
    @Override
    public String deSelect(final ItemListComponentType componentType, final By containerLocator,
                           final Strategy strategy) {
        LogUI.step(String.format(DESELECT_ITEMS_LOCATOR_AND_STRATEGY, componentType, containerLocator, strategy));
        return itemListComponent(componentType).deSelect(containerLocator, strategy);
    }

    /**
     * Deselects items identified by one or more texts, without specifying a container.
     *
     * @param componentType the type of the item list component.
     * @param itemText      one or more text identifiers for the items.
     */
    @Override
    public void deSelect(final ItemListComponentType componentType, final String... itemText) {
        LogUI.step(String.format(DESELECT_ITEMS_WITH_TEXT, String.join(", ", itemText), componentType));
        itemListComponent(componentType).deSelect(itemText);
    }

    /**
     * Deselects items using the specified locator(s) without specifying a container.
     *
     * @param componentType   the type of the item list component.
     * @param itemListLocator one or more locators identifying the items to deselect.
     */
    @Override
    public void deSelect(final ItemListComponentType componentType, final By... itemListLocator) {
        LogUI.step(String.format(DESELECT_ITEMS_NO_TEXT_BY_LOCATOR, componentType, Arrays.toString(itemListLocator)));
        itemListComponent(componentType).deSelect(itemListLocator);
    }

    /**
     * Checks whether the specified items are selected within the given container.
     *
     * @param componentType the type of the item list component.
     * @param container     the container to check for item selection.
     * @param itemText      one or more text identifiers for the items.
     * @return true if all specified items are selected, otherwise false.
     */
    @Override
    public boolean areSelected(final ItemListComponentType componentType, final SmartWebElement container,
                               final String... itemText) {
        LogUI.step(String.format(CHECK_ITEMS_SELECTED_IN_LIST, String.join(", ", itemText), componentType));
        return itemListComponent(componentType).areSelected(container, itemText);
    }

    /**
     * Checks whether a specific item is selected within the given container.
     *
     * @param componentType the type of the item list component.
     * @param container     the container to check for item selection.
     * @param itemText      the text identifier for the item.
     * @return true if the item is selected, otherwise false.
     */
    @Override
    public boolean isSelected(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        LogUI.step(String.format(CHECK_ITEM_SELECTED_IN_LIST, itemText, componentType));
        return areSelected(componentType, container, itemText);
    }

    /**
     * Checks whether the specified items are selected within a container located by the given locator.
     *
     * @param componentType    the type of the item list component.
     * @param containerLocator the locator for the container.
     * @param itemText         one or more text identifiers for the items.
     * @return true if all specified items are selected, otherwise false.
     */
    @Override
    public boolean areSelected(final ItemListComponentType componentType, final By containerLocator,
                               final String... itemText) {
        LogUI.step(String.format(CHECK_ITEMS_SELECTED_IN_LIST_BY_LOCATOR, String.join(", ", itemText), componentType,
                containerLocator));
        return itemListComponent(componentType).areSelected(containerLocator, itemText);
    }

    /**
     * Checks whether a specific item is selected within a container located by the given locator.
     *
     * @param componentType    the type of the item list component.
     * @param containerLocator the locator for the container.
     * @param itemText         the text identifier for the item.
     * @return true if the item is selected, otherwise false.
     */
    @Override
    public boolean isSelected(ItemListComponentType componentType, By containerLocator, String itemText) {
        LogUI.step(String.format(CHECK_ITEM_SELECTED_IN_LIST_BY_LOCATOR, itemText, componentType, containerLocator));
        return areSelected(componentType, containerLocator, itemText);
    }

    /**
     * Checks whether the specified items are selected without specifying a container.
     *
     * @param componentType the type of the item list component.
     * @param itemText      one or more text identifiers for the items.
     * @return true if all specified items are selected, otherwise false.
     */
    @Override
    public boolean areSelected(final ItemListComponentType componentType, final String... itemText) {
        LogUI.step(String.format(CHECK_ITEMS_SELECTED_IN_LIST, String.join(", ", itemText), componentType));
        return itemListComponent(componentType).areSelected(itemText);
    }

    /**
     * Checks whether a specific item is selected without specifying a container.
     *
     * @param componentType the type of the item list component.
     * @param itemText      the text identifier for the item.
     * @return true if the item is selected, otherwise false.
     */
    @Override
    public boolean isSelected(ItemListComponentType componentType, String itemText) {
        LogUI.step(String.format(CHECK_ITEM_SELECTED_IN_LIST, itemText, componentType));
        return areSelected(componentType, itemText);
    }

    /**
     * Checks whether the specified items are selected using the provided locator(s).
     *
     * @param componentType   the type of the item list component.
     * @param itemListLocator one or more locators identifying the items.
     * @return true if all specified items are selected, otherwise false.
     */
    @Override
    public boolean areSelected(final ItemListComponentType componentType, final By... itemListLocator) {
        LogUI.step(String.format(CHECK_ITEMS_NO_TEXT_SELECTED_BY_LOCATOR, componentType,
                Arrays.toString(itemListLocator)));
        return itemListComponent(componentType).areSelected(itemListLocator);
    }

    /**
     * Checks whether a specific item is selected using the provided locator.
     *
     * @param componentType   the type of the item list component.
     * @param itemListLocator the locator identifying the item.
     * @return true if the item is selected, otherwise false.
     */
    @Override
    public boolean isSelected(ItemListComponentType componentType, By itemListLocator) {
        LogUI.step(String.format(CHECK_ITEM_NO_TEXT_SELECTED_BY_LOCATOR, componentType, itemListLocator));
        return areSelected(componentType, Collections.singletonList(itemListLocator).toArray(new By[0]));
    }

    /**
     * Checks if the specified items are enabled within the given container.
     *
     * @param componentType the type of the item list component.
     * @param container     the container to check.
     * @param itemText      one or more text identifiers for the items.
     * @return true if all specified items are enabled, otherwise false.
     */
    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final SmartWebElement container,
                              final String... itemText) {
        LogUI.step(String.format(CHECK_ITEMS_ENABLED_IN_LIST, String.join(", ", itemText), componentType));
        return itemListComponent(componentType).areEnabled(container, itemText);
    }

    /**
     * Checks if a specific item is enabled within the given container.
     *
     * @param componentType the type of the item list component.
     * @param container     the container to check.
     * @param itemText      the text identifier for the item.
     * @return true if the item is enabled, otherwise false.
     */
    @Override
    public boolean isEnabled(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        LogUI.step(String.format(CHECK_ITEM_ENABLED_IN_LIST, itemText, componentType));
        return areEnabled(componentType, container, itemText);
    }

    /**
     * Checks if the specified items are enabled within a container located by the given locator.
     *
     * @param componentType    the type of the item list component.
     * @param containerLocator the locator for the container.
     * @param itemText         one or more text identifiers for the items.
     * @return true if all specified items are enabled, otherwise false.
     */
    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final By containerLocator,
                              final String... itemText) {
        LogUI.step(String.format(CHECK_ITEMS_ENABLED_IN_LIST_BY_LOCATOR, String.join(", ", itemText), componentType,
                containerLocator));
        return itemListComponent(componentType).areEnabled(containerLocator, itemText);
    }

    /**
     * Checks if a specific item is enabled within a container located by the given locator.
     *
     * @param componentType    the type of the item list component.
     * @param containerLocator the locator for the container.
     * @param itemText         the text identifier for the item.
     * @return true if the item is enabled, otherwise false.
     */
    @Override
    public boolean isEnabled(ItemListComponentType componentType, By containerLocator, String itemText) {
        LogUI.step(String.format(CHECK_ITEM_ENABLED_IN_LIST_BY_LOCATOR, itemText, componentType, containerLocator));
        return areEnabled(componentType, containerLocator, itemText);
    }

    /**
     * Checks if the specified items are enabled without specifying a container.
     *
     * @param componentType the type of the item list component.
     * @param itemText      one or more text identifiers for the items.
     * @return true if all specified items are enabled, otherwise false.
     */
    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final String... itemText) {
        LogUI.step(String.format(CHECK_ITEMS_ENABLED_IN_LIST, String.join(", ", itemText), componentType));
        return itemListComponent(componentType).areEnabled(itemText);
    }

    /**
     * Checks if a specific item is enabled without specifying a container.
     *
     * @param componentType the type of the item list component.
     * @param itemText      the text identifier for the item.
     * @return true if the item is enabled, otherwise false.
     */
    @Override
    public boolean isEnabled(ItemListComponentType componentType, String itemText) {
        LogUI.step(String.format(CHECK_ITEM_ENABLED_IN_LIST, itemText, componentType));
        return areEnabled(componentType, itemText);
    }

    /**
     * Checks if the specified items are enabled using one or more locators.
     *
     * @param componentType the type of the item list component.
     * @param itemLocator   one or more locators identifying the items.
     * @return true if all specified items are enabled, otherwise false.
     */
    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final By... itemLocator) {
        LogUI.step(String.format(CHECK_ITEMS_NO_TEXT_ENABLED_BY_LOCATOR, componentType, Arrays.toString(itemLocator)));
        return itemListComponent(componentType).areEnabled(itemLocator);
    }

    /**
     * Checks if a specific item is enabled using the provided locator.
     *
     * @param componentType the type of the item list component.
     * @param itemLocator   the locator identifying the item.
     * @return true if the item is enabled, otherwise false.
     */
    @Override
    public boolean isEnabled(ItemListComponentType componentType, By itemLocator) {
        LogUI.step(String.format(CHECK_ITEM_NO_TEXT_ENABLED_BY_LOCATOR, componentType, itemLocator));
        return areEnabled(componentType, Collections.singletonList(itemLocator).toArray(new By[0]));
    }

    /**
     * Checks if the specified items are visible within the given container.
     *
     * @param componentType the type of the item list component.
     * @param container     the container to check.
     * @param itemText      one or more text identifiers for the items.
     * @return true if all specified items are visible, otherwise false.
     */
    @Override
    public boolean areVisible(final ItemListComponentType componentType, final SmartWebElement container,
                              final String... itemText) {
        LogUI.step(String.format(CHECK_ITEMS_VISIBLE_IN_LIST, String.join(", ", itemText), componentType));
        return itemListComponent(componentType).areVisible(container, itemText);
    }

    /**
     * Checks if a specific item is visible within the given container.
     *
     * @param componentType the type of the item list component.
     * @param container     the container to check.
     * @param itemText      the text identifier for the item.
     * @return true if the item is visible, otherwise false.
     */
    @Override
    public boolean isVisible(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        LogUI.step(String.format(CHECK_ITEM_VISIBLE_IN_LIST, itemText, componentType));
        return areVisible(componentType, container, itemText);
    }

    /**
     * Checks if the specified items are visible within a container located by the given locator.
     *
     * @param componentType    the type of the item list component.
     * @param containerLocator the locator for the container.
     * @param itemText         one or more text identifiers for the items.
     * @return true if all specified items are visible, otherwise false.
     */
    @Override
    public boolean areVisible(final ItemListComponentType componentType, final By containerLocator,
                              final String... itemText) {
        LogUI.step(String.format(CHECK_ITEMS_VISIBLE_IN_LIST_BY_LOCATOR, String.join(", ", itemText), componentType,
                containerLocator));
        return itemListComponent(componentType).areVisible(containerLocator, itemText);
    }

    /**
     * Checks if a specific item is visible within a container located by the given locator.
     *
     * @param componentType    the type of the item list component.
     * @param containerLocator the locator for the container.
     * @param itemText         the text identifier for the item.
     * @return true if the item is visible, otherwise false.
     */
    @Override
    public boolean isVisible(ItemListComponentType componentType, By containerLocator, String itemText) {
        LogUI.step(String.format(CHECK_ITEM_VISIBLE_IN_LIST_BY_LOCATOR, itemText, componentType, containerLocator));
        return areVisible(componentType, containerLocator, itemText);
    }

    /**
     * Checks if the specified items are visible without specifying a container.
     *
     * @param componentType the type of the item list component.
     * @param itemText      one or more text identifiers for the items.
     * @return true if all specified items are visible, otherwise false.
     */
    @Override
    public boolean areVisible(final ItemListComponentType componentType, final String... itemText) {
        LogUI.step(String.format(CHECK_ITEMS_VISIBLE_IN_LIST, String.join(", ", itemText), componentType));
        return itemListComponent(componentType).areVisible(itemText);
    }

    /**
     * Checks if a specific item is visible without specifying a container.
     *
     * @param componentType the type of the item list component.
     * @param itemText      the text identifier for the item.
     * @return true if the item is visible, otherwise false.
     */
    @Override
    public boolean isVisible(ItemListComponentType componentType, String itemText) {
        LogUI.step(String.format(CHECK_ITEM_VISIBLE_IN_LIST, itemText, componentType));
        return areVisible(componentType, itemText);
    }

    /**
     * Checks if the specified items are visible using one or more locators.
     *
     * @param componentType the type of the item list component.
     * @param itemLocator   one or more locators identifying the items.
     * @return true if all specified items are visible, otherwise false.
     */
    @Override
    public boolean areVisible(final ItemListComponentType componentType, final By... itemLocator) {
        LogUI.step(String.format(CHECK_ITEMS_NO_TEXT_VISIBLE_BY_LOCATOR, componentType, Arrays.toString(itemLocator)));
        return itemListComponent(componentType).areVisible(itemLocator);
    }

    /**
     * Checks if a specific item is visible using the provided locator.
     *
     * @param componentType the type of the item list component.
     * @param itemLocator   the locator identifying the item.
     * @return true if the item is visible, otherwise false.
     */
    @Override
    public boolean isVisible(ItemListComponentType componentType, By itemLocator) {
        LogUI.step(String.format(CHECK_ITEM_NO_TEXT_VISIBLE_BY_LOCATOR, componentType, itemLocator));
        return areVisible(componentType, Collections.singletonList(itemLocator).toArray(new By[0]));
    }

    /**
     * Retrieves a list of texts for the currently selected items within the given container.
     *
     * @param componentType the type of the item list component.
     * @param container     the container holding the item list.
     * @return a list of text labels for the selected items.
     */
    @Override
    public List<String> getSelected(final ItemListComponentType componentType, final SmartWebElement container) {
        LogUI.step(String.format(GET_SELECTED_ITEMS_CONTAINER, componentType));
        return itemListComponent(componentType).getSelected(container);
    }

    /**
     * Retrieves a list of texts for the currently selected items within the container located by the given locator.
     *
     * @param componentType    the type of the item list component.
     * @param containerLocator the locator for the container holding the item list.
     * @return a list of text labels for the selected items.
     */
    @Override
    public List<String> getSelected(final ItemListComponentType componentType, final By containerLocator) {
        LogUI.step(String.format(GET_SELECTED_ITEMS_LOCATOR, componentType, containerLocator));
        return itemListComponent(componentType).getSelected(containerLocator);
    }

    /**
     * Retrieves a list of all item texts within the given container.
     *
     * @param componentType the type of the item list component.
     * @param container     the container holding the item list.
     * @return a list of text labels for all items.
     */
    @Override
    public List<String> getAll(final ItemListComponentType componentType, final SmartWebElement container) {
        LogUI.step(String.format(GET_ALL_ITEMS_CONTAINER, componentType));
        return itemListComponent(componentType).getAll(container);
    }

    /**
     * Retrieves a list of all item texts within the container located by the given locator.
     *
     * @param componentType    the type of the item list component.
     * @param containerLocator the locator for the container holding the item list.
     * @return a list of text labels for all items.
     */
    @Override
    public List<String> getAll(final ItemListComponentType componentType, final By containerLocator) {
        LogUI.step(String.format(GET_ALL_ITEMS_LOCATOR, componentType, containerLocator));
        return itemListComponent(componentType).getAll(containerLocator);
    }

    /**
     * Inserts items into the list by converting the provided values to strings and selecting them
     * within the specified locator.
     *
     * @param componentType the type of the component.
     * @param locator       the locator indicating where to insert/select items.
     * @param values        one or more values to be converted to strings and selected.
     */
    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        if (!(componentType instanceof ItemListComponentType listType)) {
            throw new IllegalArgumentException("Component type needs to be from: ItemListComponentType.");
        }
        String[] stringValues = Arrays.stream(values)
                .map(String::valueOf)
                .toArray(String[]::new);
        LogUI.step(String.format(INSERT_VALUES_INTO_COMPONENT_LOCATOR, Arrays.toString(stringValues), componentType,
                locator));
        select(listType, locator, stringValues);
    }

    /**
     * Retrieves or creates an {@link ItemList} for the specified component type.
     *
     * @param componentType the type of the item list component.
     * @return the existing or newly created {@link ItemList}.
     */
    private ItemList itemListComponent(final ItemListComponentType componentType) {
        return getOrCreateComponent(componentType);
    }

}
