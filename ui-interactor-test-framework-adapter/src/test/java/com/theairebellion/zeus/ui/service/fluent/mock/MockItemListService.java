package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MockItemListService implements ItemListService {

    public ItemListComponentType lastComponentType;
    public SmartWebElement lastContainer;
    public By[] lastLocators;
    public String[] lastText;
    public Strategy lastStrategy;
    public boolean returnBool = true;
    public List<String> returnSelectedList = Collections.emptyList();
    public List<String> returnAllList = Collections.emptyList();

    public void reset() {
        lastComponentType = null;
        lastContainer = null;
        lastLocators = null;
        lastText = null;
        lastStrategy = null;
        returnBool = true;
        returnSelectedList = new ArrayList<>();
        returnAllList = new ArrayList<>();
    }

    @Override
    public final void select(ItemListComponentType componentType, SmartWebElement container, String... itemText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastText = itemText;
    }

    @Override
    public final void select(ItemListComponentType componentType, By containerLocator, String... itemText) {
        lastComponentType = componentType;
        lastLocators = new By[]{containerLocator};
        lastText = itemText;
    }

    @Override
    public final String select(ItemListComponentType componentType, SmartWebElement container, Strategy strategy) {
        lastComponentType = componentType;
        lastContainer = container;
        lastStrategy = strategy;
        return "mockSelectStrategy";
    }

    @Override
    public final String select(ItemListComponentType componentType, By containerLocator, Strategy strategy) {
        lastComponentType = componentType;
        lastLocators = new By[]{containerLocator};
        lastStrategy = strategy;
        return "mockSelectStrategy";
    }

    @Override
    public final void select(ItemListComponentType componentType, String... itemText) {
        lastComponentType = componentType;
        lastText = itemText;
    }

    @Override
    public final void select(ItemListComponentType componentType, By... itemListLocator) {
        lastComponentType = componentType;
        lastLocators = itemListLocator;
    }

    @Override
    public final void deSelect(ItemListComponentType componentType, SmartWebElement container, String... itemText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastText = itemText;
    }

    @Override
    public final void deSelect(ItemListComponentType componentType, By containerLocator, String... itemText) {
        lastComponentType = componentType;
        lastLocators = new By[]{containerLocator};
        lastText = itemText;
    }

    @Override
    public final String deSelect(ItemListComponentType componentType, SmartWebElement container, Strategy strategy) {
        lastComponentType = componentType;
        lastContainer = container;
        lastStrategy = strategy;
        return "mockDeSelectStrategy";
    }

    @Override
    public final String deSelect(ItemListComponentType componentType, By containerLocator, Strategy strategy) {
        lastComponentType = componentType;
        lastLocators = new By[]{containerLocator};
        lastStrategy = strategy;
        return "mockDeSelectStrategy";
    }

    @Override
    public final void deSelect(ItemListComponentType componentType, String... itemText) {
        lastComponentType = componentType;
        lastText = itemText;
    }

    @Override
    public final void deSelect(ItemListComponentType componentType, By... itemListLocator) {
        lastComponentType = componentType;
        lastLocators = itemListLocator;
    }

    @Override
    public final boolean areSelected(ItemListComponentType componentType, SmartWebElement container, String... itemText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastText = itemText;
        return returnBool;
    }

    @Override
    public final boolean isSelected(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastText = new String[]{itemText};
        return returnBool;
    }

    @Override
    public final boolean areSelected(ItemListComponentType componentType, By containerLocator, String... itemText) {
        lastComponentType = componentType;
        lastLocators = new By[]{containerLocator};
        lastText = itemText;
        return returnBool;
    }

    @Override
    public final boolean isSelected(ItemListComponentType componentType, By containerLocator, String itemText) {
        lastComponentType = componentType;
        lastLocators = new By[]{containerLocator};
        lastText = new String[]{itemText};
        return returnBool;
    }

    @Override
    public final boolean areSelected(ItemListComponentType componentType, String... itemText) {
        lastComponentType = componentType;
        lastText = itemText;
        return returnBool;
    }

    @Override
    public final boolean isSelected(ItemListComponentType componentType, String itemText) {
        lastComponentType = componentType;
        lastText = new String[]{itemText};
        return returnBool;
    }

    @Override
    public final boolean areSelected(ItemListComponentType componentType, By... itemListLocator) {
        lastComponentType = componentType;
        lastLocators = itemListLocator;
        return returnBool;
    }

    @Override
    public final boolean isSelected(ItemListComponentType componentType, By itemListLocator) {
        lastComponentType = componentType;
        lastLocators = new By[]{itemListLocator};
        return returnBool;
    }

    @Override
    public final boolean areEnabled(ItemListComponentType componentType, SmartWebElement container, String... itemText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastText = itemText;
        return returnBool;
    }

    @Override
    public final boolean isEnabled(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastText = new String[]{itemText};
        return returnBool;
    }

    @Override
    public final boolean areEnabled(ItemListComponentType componentType, By containerLocator, String... itemText) {
        lastComponentType = componentType;
        lastLocators = new By[]{containerLocator};
        lastText = itemText;
        return returnBool;
    }

    @Override
    public final boolean isEnabled(ItemListComponentType componentType, By containerLocator, String itemText) {
        lastComponentType = componentType;
        lastLocators = new By[]{containerLocator};
        lastText = new String[]{itemText};
        return returnBool;
    }

    @Override
    public final boolean areEnabled(ItemListComponentType componentType, String... itemText) {
        lastComponentType = componentType;
        lastText = itemText;
        return returnBool;
    }

    @Override
    public final boolean isEnabled(ItemListComponentType componentType, String itemText) {
        lastComponentType = componentType;
        lastText = new String[]{itemText};
        return returnBool;
    }

    @Override
    public final boolean areEnabled(ItemListComponentType componentType, By... itemLocator) {
        lastComponentType = componentType;
        lastLocators = itemLocator;
        return returnBool;
    }

    @Override
    public final boolean isEnabled(ItemListComponentType componentType, By itemLocator) {
        lastComponentType = componentType;
        lastLocators = new By[]{itemLocator};
        return returnBool;
    }

    @Override
    public final boolean areVisible(ItemListComponentType componentType, SmartWebElement container, String... itemText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastText = itemText;
        return returnBool;
    }

    @Override
    public final boolean isVisible(ItemListComponentType componentType, SmartWebElement container, String itemText) {
        lastComponentType = componentType;
        lastContainer = container;
        lastText = new String[]{itemText};
        return returnBool;
    }

    @Override
    public final boolean areVisible(ItemListComponentType componentType, By containerLocator, String... itemText) {
        lastComponentType = componentType;
        lastLocators = new By[]{containerLocator};
        lastText = itemText;
        return returnBool;
    }

    @Override
    public final boolean isVisible(ItemListComponentType componentType, By containerLocator, String itemText) {
        lastComponentType = componentType;
        lastLocators = new By[]{containerLocator};
        lastText = new String[]{itemText};
        return returnBool;
    }

    @Override
    public final boolean areVisible(ItemListComponentType componentType, String... itemText) {
        lastComponentType = componentType;
        lastText = itemText;
        return returnBool;
    }

    @Override
    public final boolean isVisible(ItemListComponentType componentType, String itemText) {
        lastComponentType = componentType;
        lastText = new String[]{itemText};
        return returnBool;
    }

    @Override
    public final boolean areVisible(ItemListComponentType componentType, By... itemLocator) {
        lastComponentType = componentType;
        lastLocators = itemLocator;
        return returnBool;
    }

    @Override
    public final boolean isVisible(ItemListComponentType componentType, By itemLocator) {
        lastComponentType = componentType;
        lastLocators = new By[]{itemLocator};
        return returnBool;
    }

    @Override
    public final List<String> getSelected(ItemListComponentType componentType, SmartWebElement container) {
        lastComponentType = componentType;
        lastContainer = container;
        return returnSelectedList;
    }

    @Override
    public final List<String> getSelected(ItemListComponentType componentType, By containerLocator) {
        lastComponentType = componentType;
        lastLocators = new By[]{containerLocator};
        return returnSelectedList;
    }

    @Override
    public final List<String> getAll(ItemListComponentType componentType, SmartWebElement container) {
        lastComponentType = componentType;
        lastContainer = container;
        return returnAllList;
    }

    @Override
    public final List<String> getAll(ItemListComponentType componentType, By containerLocator) {
        lastComponentType = componentType;
        lastLocators = new By[]{containerLocator};
        return returnAllList;
    }

    @Override
    public final void insertion(ComponentType type, By locator, Object... values) {
        lastComponentType = (ItemListComponentType) type;
        lastLocators = new By[]{locator};
        if (values != null) {
            lastText = Arrays.stream(values).map(String::valueOf).toArray(String[]::new);
        }
    }
}