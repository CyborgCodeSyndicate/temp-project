package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import io.qameta.allure.Allure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemListServiceImpl extends AbstractComponentService<ItemListComponentType, ItemList>
        implements ItemListService {

    public ItemListServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected ItemList createComponent(final ItemListComponentType componentType) {
        return ComponentFactory.getListComponent(componentType, driver);
    }

    @Override
    public void select(final ItemListComponentType componentType, final SmartWebElement container,
                       final String... itemText) {
        Allure.step(String.format("[UI - Item List] Selecting items %s from list %s", String.join(", ", itemText), componentType));
        LogUI.step("Selecting items " + String.join(", ", itemText) + " from list " + componentType);
        ItemListComponent(componentType).select(container, itemText);
    }

    @Override
    public void select(final ItemListComponentType componentType, final By containerLocator, final String... itemText) {
        Allure.step(String.format("[UI - Item List] Selecting items %s from list %s using locator %s", String.join(", ", itemText), componentType, containerLocator));
        LogUI.step("Selecting items " + String.join(", ", itemText) + " from list " + componentType + " using locator " + containerLocator);
        ItemListComponent(componentType).select(containerLocator, itemText);
    }

    @Override
    public String select(final ItemListComponentType componentType, final SmartWebElement container,
                         final Strategy strategy) {
        Allure.step(String.format("[UI - Item List] Selecting items from list %s using strategy %s", componentType, strategy));
        LogUI.step("Selecting items from list " + componentType + " using strategy " + strategy);
        return ItemListComponent(componentType).select(container, strategy);
    }

    @Override
    public String select(final ItemListComponentType componentType, final By containerLocator,
                         final Strategy strategy) {
        Allure.step(String.format("[UI - Item List] Selecting items from list %s using locator %s and strategy %s", componentType, containerLocator, strategy));
        LogUI.step("Selecting items from list " + componentType + " using locator " + containerLocator + " and strategy " + strategy);
        return ItemListComponent(componentType).select(containerLocator, strategy);
    }

    @Override
    public void select(final ItemListComponentType componentType, final String... itemText) {
        Allure.step(String.format("[UI - Item List] Selecting items %s from list %s", String.join(", ", itemText), componentType));
        LogUI.step("Selecting items " + String.join(", ", itemText) + " from list " + componentType);
        ItemListComponent(componentType).select(itemText);
    }

    @Override
    public void select(final ItemListComponentType componentType, final By... itemListLocator) {
        Allure.step(String.format("[UI - Item List] Selecting items from list %s using locator %s", componentType, Arrays.toString(itemListLocator)));
        LogUI.step("Selecting items from list " + componentType + " using locator " + Arrays.toString(itemListLocator));
        ItemListComponent(componentType).select(itemListLocator);
    }

    @Override
    public void deSelect(final ItemListComponentType componentType, final SmartWebElement container,
                         final String... itemText) {
        Allure.step(String.format("[UI - Item List] De-selecting items %s from list %s", String.join(", ", itemText), componentType));
        LogUI.step("De-selecting items " + String.join(", ", itemText) + " from list " + componentType);
        ItemListComponent(componentType).deSelect(container, itemText);
    }

    @Override
    public void deSelect(final ItemListComponentType componentType, final By containerLocator,
                         final String... itemText) {
        Allure.step(String.format("[UI - Item List] De-selecting items %s from list %s using locator %s", String.join(", ", itemText), componentType, containerLocator));
        LogUI.step("De-selecting items " + String.join(", ", itemText) + " from list " + componentType + " using locator " + containerLocator);
        ItemListComponent(componentType).deSelect(containerLocator, itemText);
    }

    @Override
    public String deSelect(final ItemListComponentType componentType, final SmartWebElement container,
                           final Strategy strategy) {
        Allure.step(String.format("[UI - Item List] De-selecting items from list %s using strategy %s", componentType, strategy));
        LogUI.step("De-selecting items from list " + componentType + " using strategy " + strategy);
        return ItemListComponent(componentType).deSelect(container, strategy);
    }

    @Override
    public String deSelect(final ItemListComponentType componentType, final By containerLocator,
                           final Strategy strategy) {
        Allure.step(String.format("[UI - Item List] De-selecting items from list %s using locator %s and strategy %s", componentType, containerLocator, strategy));
        LogUI.step("De-selecting items from list " + componentType + " using locator " + containerLocator + " and strategy " + strategy);
        return ItemListComponent(componentType).deSelect(containerLocator, strategy);
    }

    @Override
    public void deSelect(final ItemListComponentType componentType, final String... itemText) {
        Allure.step(String.format("[UI - Item List] De-selecting items %s from list %s", String.join(", ", itemText), componentType));
        LogUI.step("De-selecting items " + String.join(", ", itemText) + " from list " + componentType);
        ItemListComponent(componentType).deSelect(itemText);
    }

    @Override
    public void deSelect(final ItemListComponentType componentType, final By... itemListLocator) {
        Allure.step(String.format("[UI - Item List] De-selecting items from list %s using locator %s", componentType, Arrays.toString(itemListLocator)));
        LogUI.step("De-selecting items from list " + componentType + " using locator " + Arrays.toString(itemListLocator));
        ItemListComponent(componentType).deSelect(itemListLocator);
    }

    @Override
    public boolean areSelected(final ItemListComponentType componentType, final SmartWebElement container,
                               final String... itemText) {
        Allure.step(String.format("[UI - Item List] Checking if items %s are selected in list %s", String.join(", ", itemText), componentType));
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are selected in list " + componentType);
        return ItemListComponent(componentType).areSelected(container, itemText);
    }

    @Override
    public boolean isSelected(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        Allure.step(String.format("[UI - Item List] Checking if item %s is selected in list %s", itemText, componentType));
        LogUI.step("Checking if item " + itemText + " is selected in list " + componentType);
        return areSelected(componentType, container, itemText);
    }

    @Override
    public boolean areSelected(final ItemListComponentType componentType, final By containerLocator,
                               final String... itemText) {
        Allure.step(String.format("[UI - Item List] Checking if items %s are selected in list %s using locator %s", String.join(", ", itemText), componentType, containerLocator));
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are selected in list " + componentType + " using locator " + containerLocator);
        return ItemListComponent(componentType).areSelected(containerLocator, itemText);
    }

    @Override
    public boolean isSelected(ItemListComponentType componentType, By containerLocator, String itemText) {
        Allure.step(String.format("[UI - Item List] Checking if item %s is selected in list %s using locator %s", itemText, componentType, containerLocator));
        LogUI.step("Checking if item " + itemText + " is selected in list " + componentType + " using locator " + containerLocator);
        return areSelected(componentType, containerLocator, itemText);
    }

    @Override
    public boolean areSelected(final ItemListComponentType componentType, final String... itemText) {
        Allure.step(String.format("[UI - Item List] Checking if items %s are selected in list %s", String.join(", ", itemText), componentType));
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are selected in list " + componentType);
        return ItemListComponent(componentType).areSelected(itemText);
    }

    @Override
    public boolean isSelected(ItemListComponentType componentType, String itemText) {
        Allure.step(String.format("[UI - Item List] Checking if item %s is selected in list %s", itemText, componentType));
        LogUI.step("Checking if item " + itemText + " is selected in list " + componentType);
        return areSelected(componentType, itemText);
    }

    @Override
    public boolean areSelected(final ItemListComponentType componentType, final By... itemListLocator) {
        Allure.step(String.format("[UI - Item List] Checking if items are selected in list %s using locator %s", componentType, Arrays.toString(itemListLocator)));
        LogUI.step("Checking if items are selected in list " + componentType + " using locator " + Arrays.toString(itemListLocator));
        return ItemListComponent(componentType).areSelected(itemListLocator);
    }

    @Override
    public boolean isSelected(ItemListComponentType componentType, By itemListLocator) {
        Allure.step(String.format("[UI - Item List] Checking if item is selected in list %s using locator %s", componentType, itemListLocator));
        LogUI.step("Checking if item is selected in list " + componentType + " using locator " + itemListLocator);
        return areSelected(componentType, Collections.singletonList(itemListLocator).toArray(new By[0]));
    }

    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final SmartWebElement container,
                              final String... itemText) {
        Allure.step(String.format("[UI - Item List] Checking if items %s are enabled in list %s", String.join(", ", itemText), componentType));
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are enabled in list " + componentType);
        return ItemListComponent(componentType).areEnabled(container, itemText);
    }

    @Override
    public boolean isEnabled(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        Allure.step(String.format("[UI - Item List] Checking if item %s is enabled in list %s", itemText, componentType));
        LogUI.step("Checking if item " + itemText + " is enabled in list " + componentType);
        return areEnabled(componentType, container, itemText);
    }

    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final By containerLocator,
                              final String... itemText) {
        Allure.step(String.format("[UI - Item List] Checking if items %s are enabled in list %s using locator %s", String.join(", ", itemText), componentType, containerLocator));
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are enabled in list " + componentType + " using locator " + containerLocator);
        return ItemListComponent(componentType).areEnabled(containerLocator, itemText);
    }

    @Override
    public boolean isEnabled(ItemListComponentType componentType, By containerLocator, String itemText) {
        Allure.step(String.format("[UI - Item List] Checking if item %s is enabled in list %s using locator %s", itemText, componentType, containerLocator));
        LogUI.step("Checking if item " + itemText + " is enabled in list " + componentType + " using locator " + containerLocator);
        return areEnabled(componentType, containerLocator, itemText);
    }

    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final String... itemText) {
        Allure.step(String.format("[UI - Item List] Checking if items %s are enabled in list %s", String.join(", ", itemText), componentType));
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are enabled in list " + componentType);
        return ItemListComponent(componentType).areEnabled(itemText);
    }

    @Override
    public boolean isEnabled(ItemListComponentType componentType, String itemText) {
        Allure.step(String.format("[UI - Item List] Checking if item %s is enabled in list %s", itemText, componentType));
        LogUI.step("Checking if item " + itemText + " is enabled in list " + componentType);
        return areEnabled(componentType, itemText);
    }

    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final By... itemLocator) {
        Allure.step(String.format("[UI - Item List] Checking if items are enabled in list %s using locator %s", componentType, Arrays.toString(itemLocator)));
        LogUI.step("Checking if items are enabled in list " + componentType + " using locator " + Arrays.toString(itemLocator));
        return ItemListComponent(componentType).areEnabled(itemLocator);
    }

    @Override
    public boolean isEnabled(ItemListComponentType componentType, By itemLocator) {
        Allure.step(String.format("[UI - Item List] Checking if item is enabled in list %s using locator %s", componentType, itemLocator));
        LogUI.step("Checking if item is enabled in list " + componentType + " using locator " + itemLocator);
        return areEnabled(componentType, Collections.singletonList(itemLocator).toArray(new By[0]));
    }

    @Override
    public boolean areVisible(final ItemListComponentType componentType, final SmartWebElement container,
                              final String... itemText) {
        Allure.step(String.format("[UI - Item List] Checking if items %s are visible in list %s", String.join(", ", itemText), componentType));
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are visible in list " + componentType);
        return ItemListComponent(componentType).areVisible(container, itemText);
    }

    @Override
    public boolean isVisible(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        Allure.step(String.format("[UI - Item List] Checking if item %s is visible in list %s", itemText, componentType));
        LogUI.step("Checking if item " + itemText + " is visible in list " + componentType);
        return areVisible(componentType, container, itemText);
    }

    @Override
    public boolean areVisible(final ItemListComponentType componentType, final By containerLocator,
                              final String... itemText) {
        Allure.step(String.format("[UI - Item List] Checking if items %s are visible in list %s using locator %s", String.join(", ", itemText), componentType, containerLocator));
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are visible in list " + componentType + " using locator " + containerLocator);
        return ItemListComponent(componentType).areVisible(containerLocator, itemText);
    }

    @Override
    public boolean isVisible(ItemListComponentType componentType, By containerLocator, String itemText) {
        Allure.step(String.format("[UI - Item List] Checking if item %s is visible in list %s using locator %s", itemText, componentType, containerLocator));
        LogUI.step("Checking if item " + itemText + " is visible in list " + componentType + " using locator " + containerLocator);
        return areVisible(componentType, containerLocator, itemText);
    }

    @Override
    public boolean areVisible(final ItemListComponentType componentType, final String... itemText) {
        Allure.step(String.format("[UI - Item List] Checking if items %s are visible in list %s", String.join(", ", itemText), componentType));
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are visible in list " + componentType);
        return ItemListComponent(componentType).areVisible(itemText);
    }

    @Override
    public boolean isVisible(ItemListComponentType componentType, String itemText) {
        Allure.step(String.format("[UI - Item List] Checking if item %s is visible in list %s", itemText, componentType));
        LogUI.step("Checking if item " + itemText + " is visible in list " + componentType);
        return areVisible(componentType, itemText);
    }

    @Override
    public boolean areVisible(final ItemListComponentType componentType, final By... itemLocator) {
        Allure.step(String.format("[UI - Item List] Checking if items are visible in list %s using locator %s", componentType, Arrays.toString(itemLocator)));
        LogUI.step("Checking if items are visible in list " + componentType + " using locator " + Arrays.toString(itemLocator));
        return ItemListComponent(componentType).areVisible(itemLocator);
    }

    @Override
    public boolean isVisible(ItemListComponentType componentType, By itemLocator) {
        Allure.step(String.format("[UI - Item List] Checking if item is visible in list %s using locator %s", componentType, itemLocator));
        LogUI.step("Checking if item is visible in list " + componentType + " using locator " + itemLocator);
        return areVisible(componentType, Collections.singletonList(itemLocator).toArray(new By[0]));
    }

    @Override
    public List<String> getSelected(final ItemListComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Item List] Getting selected items from list %s using container", componentType));
        LogUI.step("Getting selected items from list " + componentType + " using container");
        return ItemListComponent(componentType).getSelected(container);
    }

    @Override
    public List<String> getSelected(final ItemListComponentType componentType, final By containerLocator) {
        Allure.step(String.format("[UI - Item List] Getting selected items from list %s using locator %s", componentType, containerLocator));
        LogUI.step("Getting selected items from list " + componentType + " using locator " + containerLocator);
        return ItemListComponent(componentType).getSelected(containerLocator);
    }

    @Override
    public List<String> getAll(final ItemListComponentType componentType, final SmartWebElement container) {
        Allure.step(String.format("[UI - Item List] Getting all items from list %s using container", componentType));
        LogUI.step("Getting all items from list " + componentType + " using container");
        return ItemListComponent(componentType).getAll(container);
    }

    @Override
    public List<String> getAll(final ItemListComponentType componentType, final By containerLocator) {
        Allure.step(String.format("[UI - Item List] Getting all items from list %s using locator %s", componentType, containerLocator));
        LogUI.step("Getting all items from list " + componentType + " using locator " + containerLocator);
        return ItemListComponent(componentType).getAll(containerLocator);
    }

    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        String[] stringValues = Arrays.stream(values)
                .map(String::valueOf)
                .toArray(String[]::new);
        Allure.step(String.format("[UI - Item List] Inserting values %s into component %s using locator %s", Arrays.toString(stringValues), componentType, locator));
        LogUI.step("Inserting values " + Arrays.toString(stringValues) + " into component " + componentType + " using locator " + locator);
        select((ItemListComponentType) componentType, locator, stringValues);
    }

    private ItemList ItemListComponent(final ItemListComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}