package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

public class ItemListServiceImpl implements ItemListService {

    protected SmartSelenium smartSelenium;
    private static Map<ItemListComponentType, ItemList> components;

    public ItemListServiceImpl(WebDriver driver) {
        smartSelenium = new SmartSelenium(driver);
        components = new HashMap<>();
    }

    public ItemListServiceImpl(SmartSelenium smartSelenium) {
        this.smartSelenium = smartSelenium;
        components = new HashMap<>();
    }

    @Override
    public void select(WebElement container, ItemListComponentType componentType, String... itemText) {
        ItemListComponent(componentType).select(container, itemText);
    }

    @Override
    public void select(By containerLocator, ItemListComponentType componentType, String... itemText) {
        ItemListComponent(componentType).select(containerLocator, itemText);
    }

    @Override
    public String select(WebElement container, Strategy strategy, ItemListComponentType componentType) {
        return ItemListComponent(componentType).select(container, strategy);
    }

    @Override
    public String select(By containerLocator, Strategy strategy, ItemListComponentType componentType) {
        return ItemListComponent(componentType).select(containerLocator, strategy);
    }

    @Override
    public void select(ItemListComponentType componentType, String... itemText) {
        ItemListComponent(componentType).select(itemText);
    }

    @Override
    public void select(ItemListComponentType componentType, By... itemListLocator) {
        ItemListComponent(componentType).select(itemListLocator);
    }

    @Override
    public void deSelect(WebElement container, ItemListComponentType componentType, String... itemText) {
        ItemListComponent(componentType).deSelect(container, itemText);
    }

    @Override
    public void deSelect(By containerLocator, ItemListComponentType componentType, String... itemText) {
        ItemListComponent(componentType).deSelect(containerLocator, itemText);
    }

    @Override
    public String deSelect(WebElement container, Strategy strategy, ItemListComponentType componentType) {
        return ItemListComponent(componentType).deSelect(container, strategy);
    }

    @Override
    public String deSelect(By containerLocator, Strategy strategy, ItemListComponentType componentType) {
        return ItemListComponent(componentType).deSelect(containerLocator, strategy);
    }

    @Override
    public void deSelect(ItemListComponentType componentType, String... itemText) {
        ItemListComponent(componentType).deSelect(itemText);
    }

    @Override
    public void deSelect(ItemListComponentType componentType, By... itemListLocator) {
        ItemListComponent(componentType).deSelect(itemListLocator);
    }

    @Override
    public boolean areSelected(WebElement container, ItemListComponentType componentType, String... itemText) {
        return ItemListComponent(componentType).areSelected(container, itemText);
    }

    @Override
    public boolean areSelected(By containerLocator, ItemListComponentType componentType, String... itemText) {
        return ItemListComponent(componentType).areSelected(containerLocator, itemText);
    }

    @Override
    public boolean areSelected(ItemListComponentType componentType, String... itemText) {
        return ItemListComponent(componentType).areSelected(itemText);
    }

    @Override
    public boolean areSelected(ItemListComponentType componentType, By... itemListLocator) {
        return ItemListComponent(componentType).areSelected(itemListLocator);
    }

    @Override
    public boolean areEnabled(WebElement container, ItemListComponentType componentType, String... itemText) {
        return ItemListComponent(componentType).areEnabled(container, itemText);
    }

    @Override
    public boolean areEnabled(By containerLocator, ItemListComponentType componentType, String... itemText) {
        return ItemListComponent(componentType).areEnabled(containerLocator, itemText);
    }

    @Override
    public boolean areEnabled(ItemListComponentType componentType, String... itemText) {
        return ItemListComponent(componentType).areEnabled(itemText);
    }

    @Override
    public boolean areEnabled(ItemListComponentType componentType, By... itemLocator) {
        return ItemListComponent(componentType).areEnabled(itemLocator);
    }

    @Override
    public boolean arePresent(WebElement container, ItemListComponentType componentType, String... itemText) {
        return ItemListComponent(componentType).arePresent(container, itemText);
    }

    @Override
    public boolean arePresent(By containerLocator, ItemListComponentType componentType, String... itemText) {
        return ItemListComponent(componentType).arePresent(containerLocator, itemText);
    }

    @Override
    public boolean arePresent(ItemListComponentType componentType, String... itemText) {
        return ItemListComponent(componentType).arePresent(itemText);
    }

    @Override
    public boolean arePresent(ItemListComponentType componentType, By... itemLocator) {
        return ItemListComponent(componentType).arePresent(itemLocator);
    }

    @Override
    public List<String> getSelected(WebElement container, ItemListComponentType componentType) {
        return ItemListComponent(componentType).getSelected(container);
    }

    @Override
    public List<String> getSelected(By containerLocator, ItemListComponentType componentType) {
        return ItemListComponent(componentType).getSelected(containerLocator);
    }

    @Override
    public List<String> getAll(WebElement container, ItemListComponentType componentType) {
        return ItemListComponent(componentType).getAll(container);
    }

    @Override
    public List<String> getAll(By containerLocator, ItemListComponentType componentType) {
        return ItemListComponent(componentType).getAll(containerLocator);
    }

    @Override
    public void insertion(By locator, ComponentType componentType, Object... values) {
        String[] stringValues = Arrays.stream(values)
                .map(String::valueOf)
                .toArray(String[]::new);
        select(locator, (ItemListComponentType) componentType, stringValues);
    }

    private ItemList ItemListComponent(ItemListComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getListComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}