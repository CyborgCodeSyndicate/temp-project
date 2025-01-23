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
    public void select(final ItemListComponentType componentType, final WebElement container,
                       final String... itemText) {
        ItemListComponent(componentType).select(container, itemText);
    }

    @Override
    public void select(final ItemListComponentType componentType, final By containerLocator, final String... itemText) {
        ItemListComponent(componentType).select(containerLocator, itemText);
    }

    @Override
    public String select(final ItemListComponentType componentType, final WebElement container,
                         final Strategy strategy) {
        return ItemListComponent(componentType).select(container, strategy);
    }

    @Override
    public String select(final ItemListComponentType componentType, final By containerLocator,
                         final Strategy strategy) {
        return ItemListComponent(componentType).select(containerLocator, strategy);
    }

    @Override
    public void select(final ItemListComponentType componentType, final String... itemText) {
        ItemListComponent(componentType).select(itemText);
    }

    @Override
    public void select(final ItemListComponentType componentType, final By... itemListLocator) {
        ItemListComponent(componentType).select(itemListLocator);
    }

    @Override
    public void deSelect(final ItemListComponentType componentType, final WebElement container,
                         final String... itemText) {
        ItemListComponent(componentType).deSelect(container, itemText);
    }

    @Override
    public void deSelect(final ItemListComponentType componentType, final By containerLocator,
                         final String... itemText) {
        ItemListComponent(componentType).deSelect(containerLocator, itemText);
    }

    @Override
    public String deSelect(final ItemListComponentType componentType, final WebElement container,
                           final Strategy strategy) {
        return ItemListComponent(componentType).deSelect(container, strategy);
    }

    @Override
    public String deSelect(final ItemListComponentType componentType, final By containerLocator,
                           final Strategy strategy) {
        return ItemListComponent(componentType).deSelect(containerLocator, strategy);
    }

    @Override
    public void deSelect(final ItemListComponentType componentType, final String... itemText) {
        ItemListComponent(componentType).deSelect(itemText);
    }

    @Override
    public void deSelect(final ItemListComponentType componentType, final By... itemListLocator) {
        ItemListComponent(componentType).deSelect(itemListLocator);
    }

    @Override
    public boolean areSelected(final ItemListComponentType componentType, final WebElement container,
                               final String... itemText) {
        return ItemListComponent(componentType).areSelected(container, itemText);
    }

    @Override
    public boolean areSelected(final ItemListComponentType componentType, final By containerLocator,
                               final String... itemText) {
        return ItemListComponent(componentType).areSelected(containerLocator, itemText);
    }

    @Override
    public boolean areSelected(final ItemListComponentType componentType, final String... itemText) {
        return ItemListComponent(componentType).areSelected(itemText);
    }

    @Override
    public boolean areSelected(final ItemListComponentType componentType, final By... itemListLocator) {
        return ItemListComponent(componentType).areSelected(itemListLocator);
    }

    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final WebElement container,
                              final String... itemText) {
        return ItemListComponent(componentType).areEnabled(container, itemText);
    }

    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final By containerLocator,
                              final String... itemText) {
        return ItemListComponent(componentType).areEnabled(containerLocator, itemText);
    }

    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final String... itemText) {
        return ItemListComponent(componentType).areEnabled(itemText);
    }

    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final By... itemLocator) {
        return ItemListComponent(componentType).areEnabled(itemLocator);
    }

    @Override
    public boolean arePresent(final ItemListComponentType componentType, final WebElement container,
                              final String... itemText) {
        return ItemListComponent(componentType).arePresent(container, itemText);
    }

    @Override
    public boolean arePresent(final ItemListComponentType componentType, final By containerLocator,
                              final String... itemText) {
        return ItemListComponent(componentType).arePresent(containerLocator, itemText);
    }

    @Override
    public boolean arePresent(final ItemListComponentType componentType, final String... itemText) {
        return ItemListComponent(componentType).arePresent(itemText);
    }

    @Override
    public boolean arePresent(final ItemListComponentType componentType, final By... itemLocator) {
        return ItemListComponent(componentType).arePresent(itemLocator);
    }

    @Override
    public List<String> getSelected(final ItemListComponentType componentType, final WebElement container) {
        return ItemListComponent(componentType).getSelected(container);
    }

    @Override
    public List<String> getSelected(final ItemListComponentType componentType, final By containerLocator) {
        return ItemListComponent(componentType).getSelected(containerLocator);
    }

    @Override
    public List<String> getAll(final ItemListComponentType componentType, final WebElement container) {
        return ItemListComponent(componentType).getAll(container);
    }

    @Override
    public List<String> getAll(final ItemListComponentType componentType, final By containerLocator) {
        return ItemListComponent(componentType).getAll(containerLocator);
    }

    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        String[] stringValues = Arrays.stream(values)
                .map(String::valueOf)
                .toArray(String[]::new);
        select((ItemListComponentType) componentType, locator, stringValues);
    }

    private ItemList ItemListComponent(final ItemListComponentType componentType) {
        if (Objects.isNull(components.get(componentType))) {
            components.put(componentType, ComponentFactory.getListComponent(componentType, smartSelenium));
        }
        return components.get(componentType);
    }
}