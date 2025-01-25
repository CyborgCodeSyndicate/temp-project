package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.UIElement;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class ItemListServiceFluent implements Insertion {

    private final ItemListService itemListService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public ItemListServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, ItemListService itemListService) {
        this.itemListService = itemListService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public UIServiceFluent select(final UIElement element, final String... values) {
        Allure.step(String.format("Selecting items: '%s' from list component of type: '%s'.", Arrays.toString(values),
                element.componentType().toString()));
        itemListService.select(element.componentType(), element.locator(), values);
        return uiServiceFluent;
    }


    public UIServiceFluent deSelect(final UIElement element, final String... values) {
        itemListService.deSelect(element.componentType(), element.locator(), values);
        return uiServiceFluent;
    }


    public UIServiceFluent areSelected(final UIElement element, final String... values) {
        boolean selected = itemListService.areSelected(element.componentType(), element.locator(), values);
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }


    public UIServiceFluent areEnabled(final UIElement element, final String... values) {
        boolean enabled = itemListService.areEnabled(element.componentType(), element.locator(), values);
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public UIServiceFluent areVisible(final UIElement element, final String... values) {
        boolean visible = itemListService.areVisible(element.componentType(), element.locator(), values);
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }


    public UIServiceFluent getSelected(final UIElement element) {
        List<String> selectedItems = itemListService.getSelected(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selectedItems);
        return uiServiceFluent;
    }


    public UIServiceFluent getAll(final UIElement element) {
        List<String> allItems = itemListService.getAll(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), allItems);
        return uiServiceFluent;
    }


    @Override
    public void insertion(final ComponentType componentType, final By locator, final Object... values) {
        itemListService.insertion(componentType, locator, values);
    }

}
