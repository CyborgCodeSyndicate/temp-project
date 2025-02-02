package com.theairebellion.zeus.ui.components.list;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.lang.reflect.Array;
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
        ItemListComponent(componentType).select(container, itemText);
    }

    @Override
    public void select(final ItemListComponentType componentType, final By containerLocator, final String... itemText) {
        ItemListComponent(componentType).select(containerLocator, itemText);
    }

    @Override
    public String select(final ItemListComponentType componentType, final SmartWebElement container,
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
    public void deSelect(final ItemListComponentType componentType, final SmartWebElement container,
                         final String... itemText) {
        ItemListComponent(componentType).deSelect(container, itemText);
    }

    @Override
    public void deSelect(final ItemListComponentType componentType, final By containerLocator,
                         final String... itemText) {
        ItemListComponent(componentType).deSelect(containerLocator, itemText);
    }

    @Override
    public String deSelect(final ItemListComponentType componentType, final SmartWebElement container,
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
    public boolean areSelected(final ItemListComponentType componentType, final SmartWebElement container,
                               final String... itemText) {
        return ItemListComponent(componentType).areSelected(container, itemText);
    }

    @Override
    public boolean isSelected(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        return areSelected(componentType, container, itemText);
    }

    @Override
    public boolean areSelected(final ItemListComponentType componentType, final By containerLocator,
                               final String... itemText) {
        return ItemListComponent(componentType).areSelected(containerLocator, itemText);
    }

    @Override
    public boolean isSelected(ItemListComponentType componentType, By containerLocator, String itemText) {
        return areSelected(componentType, containerLocator, itemText);
    }

    @Override
    public boolean areSelected(final ItemListComponentType componentType, final String... itemText) {
        return ItemListComponent(componentType).areSelected(itemText);
    }

    @Override
    public boolean isSelected(ItemListComponentType componentType, String itemText) {
        return areSelected(componentType, itemText);
    }

    @Override
    public boolean areSelected(final ItemListComponentType componentType, final By... itemListLocator) {
        return ItemListComponent(componentType).areSelected(itemListLocator);
    }

    @Override
    public boolean isSelected(ItemListComponentType componentType, By itemListLocator) {
        return areSelected(componentType, Collections.singletonList(itemListLocator).toArray(new By[0]));
    }

    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final SmartWebElement container,
                              final String... itemText) {
        return ItemListComponent(componentType).areEnabled(container, itemText);
    }

    @Override
    public boolean isEnabled(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        return areEnabled(componentType, container, itemText);
    }

    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final By containerLocator,
                              final String... itemText) {
        return ItemListComponent(componentType).areEnabled(containerLocator, itemText);
    }

    @Override
    public boolean isEnabled(ItemListComponentType componentType, By containerLocator, String itemText) {
        return areEnabled(componentType, containerLocator, itemText);
    }

    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final String... itemText) {
        return ItemListComponent(componentType).areEnabled(itemText);
    }

    @Override
    public boolean isEnabled(ItemListComponentType componentType, String itemText) {
        return areEnabled(componentType, itemText);
    }

    @Override
    public boolean areEnabled(final ItemListComponentType componentType, final By... itemLocator) {
        return ItemListComponent(componentType).areEnabled(itemLocator);
    }

    @Override
    public boolean isEnabled(ItemListComponentType componentType, By itemLocator) {
        return areEnabled(componentType, Collections.singletonList(itemLocator).toArray(new By[0]));
    }

    @Override
    public boolean areVisible(final ItemListComponentType componentType, final SmartWebElement container,
                              final String... itemText) {
        return ItemListComponent(componentType).areVisible(container, itemText);
    }

    @Override
    public boolean isVisible(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        return areVisible(componentType, container, itemText);
    }

    @Override
    public boolean areVisible(final ItemListComponentType componentType, final By containerLocator,
                              final String... itemText) {
        return ItemListComponent(componentType).areVisible(containerLocator, itemText);
    }

    @Override
    public boolean isVisible(ItemListComponentType componentType, By containerLocator, String itemText) {
        return areVisible(componentType, containerLocator, itemText);
    }

    @Override
    public boolean areVisible(final ItemListComponentType componentType, final String... itemText) {
        return ItemListComponent(componentType).areVisible(itemText);
    }

    @Override
    public boolean isVisible(ItemListComponentType componentType, String itemText) {
        return areVisible(componentType, itemText);
    }

    @Override
    public boolean areVisible(final ItemListComponentType componentType, final By... itemLocator) {
        return ItemListComponent(componentType).areVisible(itemLocator);
    }

    @Override
    public boolean isVisible(ItemListComponentType componentType, By itemLocator) {
        return areVisible(componentType, Collections.singletonList(itemLocator).toArray(new By[0]));
    }

    @Override
    public List<String> getSelected(final ItemListComponentType componentType, final SmartWebElement container) {
        return ItemListComponent(componentType).getSelected(container);
    }

    @Override
    public List<String> getSelected(final ItemListComponentType componentType, final By containerLocator) {
        return ItemListComponent(componentType).getSelected(containerLocator);
    }

    @Override
    public List<String> getAll(final ItemListComponentType componentType, final SmartWebElement container) {
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
        return getOrCreateComponent(componentType);
    }
}