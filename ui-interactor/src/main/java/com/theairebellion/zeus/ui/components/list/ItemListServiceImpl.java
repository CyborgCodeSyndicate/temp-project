package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import io.qameta.allure.Step;

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

    @Step("Selecting items {itemText} from list {componentType}")
    @Override
    public void select(final ItemListComponentType componentType, final SmartWebElement container,
                       final String... itemText) {
        LogUI.step("Selecting items " + String.join(", ", itemText) + " from list " + componentType);
        ItemListComponent(componentType).select(container, itemText);
    }

    @Step("Selecting items {itemText} from list {componentType} using locator {containerLocator}")
    @Override
    public void select(final ItemListComponentType componentType, final By containerLocator, final String... itemText) {
        LogUI.step("Selecting items " + String.join(", ", itemText) + " from list " + componentType + " using locator " + containerLocator);
        ItemListComponent(componentType).select(containerLocator, itemText);
    }

    @Step("Selecting items from list {componentType} using strategy {strategy}")
    @Override
    public String select(final ItemListComponentType componentType, final SmartWebElement container,
                         final Strategy strategy) {
        LogUI.step("Selecting items from list " + componentType + " using strategy " + strategy);
        return ItemListComponent(componentType).select(container, strategy);
    }

    @Step("Selecting items from list {componentType} using locator {containerLocator} and strategy {strategy}")
    @Override
    public String select(final ItemListComponentType componentType, final By containerLocator,
                         final Strategy strategy) {
        LogUI.step("Selecting items from list " + componentType + " using locator " + containerLocator + " and strategy " + strategy);
        return ItemListComponent(componentType).select(containerLocator, strategy);
    }

    @Step("Selecting items {itemText} from list {componentType}")
    @Override
    public void select(final ItemListComponentType componentType, final String... itemText) {
        LogUI.step("Selecting items " + String.join(", ", itemText) + " from list " + componentType);
        ItemListComponent(componentType).select(itemText);
    }

    @Step("Selecting items from list {componentType} using locator {itemListLocator}")
    @Override
    public void select(final ItemListComponentType componentType, final By... itemListLocator) {
        LogUI.step("Selecting items from list " + componentType + " using locator " + Arrays.toString(itemListLocator));
        ItemListComponent(componentType).select(itemListLocator);
    }

    @Step("De-selecting items {itemText} from list {componentType}")
    @Override
    public void deSelect(final ItemListComponentType componentType, final SmartWebElement container,
                         final String... itemText) {
        LogUI.step("De-selecting items " + String.join(", ", itemText) + " from list " + componentType);
        ItemListComponent(componentType).deSelect(container, itemText);
    }

    @Step("De-selecting items {itemText} from list {componentType} using locator {containerLocator}")
    @Override
    public void deSelect(final ItemListComponentType componentType, final By containerLocator,
                         final String... itemText) {
        LogUI.step("De-selecting items " + String.join(", ", itemText) + " from list " + componentType + " using locator " + containerLocator);
        ItemListComponent(componentType).deSelect(containerLocator, itemText);
    }

    @Step("De-selecting items from list {componentType} using strategy {strategy}")
    @Override
    public String deSelect(final ItemListComponentType componentType, final SmartWebElement container,
                           final Strategy strategy) {
        LogUI.step("De-selecting items from list " + componentType + " using strategy " + strategy);
        return ItemListComponent(componentType).deSelect(container, strategy);
    }

    @Step("De-selecting items from list {componentType} using locator {containerLocator} and strategy {strategy}")
    @Override
    public String deSelect(final ItemListComponentType componentType, final By containerLocator,
                           final Strategy strategy) {
        LogUI.step("De-selecting items from list " + componentType + " using locator " + containerLocator + " and strategy " + strategy);
        return ItemListComponent(componentType).deSelect(containerLocator, strategy);
    }

    @Step("De-selecting items {itemText} from list {componentType}")
    @Override
    public void deSelect(final ItemListComponentType componentType, final String... itemText) {
        LogUI.step("De-selecting items " + String.join(", ", itemText) + " from list " + componentType);
        ItemListComponent(componentType).deSelect(itemText);
    }

    @Step("De-selecting items from list {componentType} using locator {itemListLocator}")
    @Override
    public void deSelect(final ItemListComponentType componentType, final By... itemListLocator) {
        LogUI.step("De-selecting items from list " + componentType + " using locator " + Arrays.toString(itemListLocator));
        ItemListComponent(componentType).deSelect(itemListLocator);
    }

    @Step("Checking if items {itemText} are selected in list {componentType}")
    @Override
    public boolean areSelected(final ItemListComponentType componentType, final SmartWebElement container,
                               final String... itemText) {
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are selected in list " + componentType);
        return ItemListComponent(componentType).areSelected(container, itemText);
    }

    @Step("Checking if item {itemText} is selected in list {componentType}")
    @Override
    public boolean isSelected(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        LogUI.step("Checking if item " + itemText + " is selected in list " + componentType);
        return areSelected(componentType, container, itemText);
    }

    @Step("Checking if items {itemText} are selected in list {componentType} using locator {containerLocator}")
    @Override
    public boolean areSelected(final ItemListComponentType componentType, final By containerLocator,
                               final String... itemText) {
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are selected in list " + componentType + " using locator " + containerLocator);
        return ItemListComponent(componentType).areSelected(containerLocator, itemText);
    }

    @Step("Checking if item {itemText} is selected in list {componentType} using locator {containerLocator}")
    @Override
    public boolean isSelected(ItemListComponentType componentType, By containerLocator, String itemText) {
        LogUI.step("Checking if item " + itemText + " is selected in list " + componentType + " using locator " + containerLocator);
        return areSelected(componentType, containerLocator, itemText);
    }

    @Step("Checking if items {itemText} are selected in list {componentType}")
    @Override
    public boolean areSelected(final ItemListComponentType componentType, final String... itemText) {
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are selected in list " + componentType);
        return ItemListComponent(componentType).areSelected(itemText);
    }

    @Step("Checking if item {itemText} is selected in list {componentType}")
    @Override
    public boolean isSelected(ItemListComponentType componentType, String itemText) {
        LogUI.step("Checking if item " + itemText + " is selected in list " + componentType);
        return areSelected(componentType, itemText);
    }

    @Step("Checking if items are selected in list {componentType} using locator {itemListLocator}")
    @Override
    public boolean areSelected(final ItemListComponentType componentType, final By... itemListLocator) {
        LogUI.step("Checking if items are selected in list " + componentType + " using locator " + Arrays.toString(itemListLocator));
        return ItemListComponent(componentType).areSelected(itemListLocator);
    }

    @Step("Checking if item is selected in list {componentType} using locator {itemListLocator}")
    @Override
    public boolean isSelected(ItemListComponentType componentType, By itemListLocator) {
        LogUI.step("Checking if item is selected in list " + componentType + " using locator " + itemListLocator);
        return areSelected(componentType, Collections.singletonList(itemListLocator).toArray(new By[0]));
    }

    @Step("Checking if items {itemText} are enabled in list {componentType}")
    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final SmartWebElement container,
                              final String... itemText) {
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are enabled in list " + componentType);
        return ItemListComponent(componentType).areEnabled(container, itemText);
    }

    @Step("Checking if item {itemText} is enabled in list {componentType}")
    @Override
    public boolean isEnabled(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        LogUI.step("Checking if item " + itemText + " is enabled in list " + componentType);
        return areEnabled(componentType, container, itemText);
    }

    @Step("Checking if items {itemText} are enabled in list {componentType} using locator {containerLocator}")
    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final By containerLocator,
                              final String... itemText) {
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are enabled in list " + componentType + " using locator " + containerLocator);
        return ItemListComponent(componentType).areEnabled(containerLocator, itemText);
    }

    @Step("Checking if item {itemText} is enabled in list {componentType} using locator {containerLocator}")
    @Override
    public boolean isEnabled(ItemListComponentType componentType, By containerLocator, String itemText) {
        LogUI.step("Checking if item " + itemText + " is enabled in list " + componentType + " using locator " + containerLocator);
        return areEnabled(componentType, containerLocator, itemText);
    }

    @Step("Checking if items {itemText} are enabled in list {componentType}")
    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final String... itemText) {
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are enabled in list " + componentType);
        return ItemListComponent(componentType).areEnabled(itemText);
    }

    @Step("Checking if item {itemText} is enabled in list {componentType}")
    @Override
    public boolean isEnabled(ItemListComponentType componentType, String itemText) {
        LogUI.step("Checking if item " + itemText + " is enabled in list " + componentType);
        return areEnabled(componentType, itemText);
    }

    @Step("Checking if items are enabled in list {componentType} using locator {itemLocator}")
    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final By... itemLocator) {
        LogUI.step("Checking if items are enabled in list " + componentType + " using locator " + Arrays.toString(itemLocator));
        return ItemListComponent(componentType).areEnabled(itemLocator);
    }

    @Step("Checking if item is enabled in list {componentType} using locator {itemLocator}")
    @Override
    public boolean isEnabled(ItemListComponentType componentType, By itemLocator) {
        LogUI.step("Checking if item is enabled in list " + componentType + " using locator " + itemLocator);
        return areEnabled(componentType, Collections.singletonList(itemLocator).toArray(new By[0]));
    }

    @Step("Checking if items {itemText} are visible in list {componentType}")
    @Override
    public boolean areVisible(final ItemListComponentType componentType, final SmartWebElement container,
                              final String... itemText) {
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are visible in list " + componentType);
        return ItemListComponent(componentType).areVisible(container, itemText);
    }

    @Step("Checking if item {itemText} is visible in list {componentType}")
    @Override
    public boolean isVisible(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        LogUI.step("Checking if item " + itemText + " is visible in list " + componentType);
        return areVisible(componentType, container, itemText);
    }

    @Step("Checking if items {itemText} are visible in list {componentType} using locator {containerLocator}")
    @Override
    public boolean areVisible(final ItemListComponentType componentType, final By containerLocator,
                              final String... itemText) {
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are visible in list " + componentType + " using locator " + containerLocator);
        return ItemListComponent(componentType).areVisible(containerLocator, itemText);
    }

    @Step("Checking if item {itemText} is visible in list {componentType} using locator {containerLocator}")
    @Override
    public boolean isVisible(ItemListComponentType componentType, By containerLocator, String itemText) {
        LogUI.step("Checking if item " + itemText + " is visible in list " + componentType + " using locator " + containerLocator);
        return areVisible(componentType, containerLocator, itemText);
    }

    @Step("Checking if items {itemText} are visible in list {componentType}")
    @Override
    public boolean areVisible(final ItemListComponentType componentType, final String... itemText) {
        LogUI.step("Checking if items " + String.join(", ", itemText) + " are visible in list " + componentType);
        return ItemListComponent(componentType).areVisible(itemText);
    }

    @Step("Checking if item {itemText} is visible in list {componentType}")
    @Override
    public boolean isVisible(ItemListComponentType componentType, String itemText) {
        LogUI.step("Checking if item " + itemText + " is visible in list " + componentType);
        return areVisible(componentType, itemText);
    }

    @Step("Checking if items are visible in list {componentType} using locator {itemLocator}")
    @Override
    public boolean areVisible(final ItemListComponentType componentType, final By... itemLocator) {
        LogUI.step("Checking if items are visible in list " + componentType + " using locator " + Arrays.toString(itemLocator));
        return ItemListComponent(componentType).areVisible(itemLocator);
    }

    @Step("Checking if item is visible in list {componentType} using locator {itemLocator}")
    @Override
    public boolean isVisible(ItemListComponentType componentType, By itemLocator) {
        LogUI.step("Checking if item is visible in list " + componentType + " using locator " + itemLocator);
        return areVisible(componentType, Collections.singletonList(itemLocator).toArray(new By[0]));
    }

    @Step("Getting selected items from list {componentType} using container")
    @Override
    public List<String> getSelected(final ItemListComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting selected items from list " + componentType + " using container");
        return ItemListComponent(componentType).getSelected(container);
    }

    @Step("Getting selected items from list {componentType} using locator {containerLocator}")
    @Override
    public List<String> getSelected(final ItemListComponentType componentType, final By containerLocator) {
        LogUI.step("Getting selected items from list " + componentType + " using locator " + containerLocator);
        return ItemListComponent(componentType).getSelected(containerLocator);
    }

    @Step("Getting all items from list {componentType} using container")
    @Override
    public List<String> getAll(final ItemListComponentType componentType, final SmartWebElement container) {
        LogUI.step("Getting all items from list " + componentType + " using container");
        return ItemListComponent(componentType).getAll(container);
    }

    @Step("Getting all items from list {componentType} using locator {containerLocator}")
    @Override
    public List<String> getAll(final ItemListComponentType componentType, final By containerLocator) {
        LogUI.step("Getting all items from list " + componentType + " using locator " + containerLocator);
        return ItemListComponent(componentType).getAll(containerLocator);
    }

    @Step("Inserting values {values} into component {componentType} using locator {locator}")
    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        String[] stringValues = Arrays.stream(values)
                .map(String::valueOf)
                .toArray(String[]::new);
        LogUI.step("Inserting values " + Arrays.toString(stringValues) + " into component " + componentType + " using locator " + locator);
        select((ItemListComponentType) componentType, locator, stringValues);
    }

    private ItemList ItemListComponent(final ItemListComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}