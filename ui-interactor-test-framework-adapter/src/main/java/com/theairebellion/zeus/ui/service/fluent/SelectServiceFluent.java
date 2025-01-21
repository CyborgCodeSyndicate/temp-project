package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.select.DdlMode;
import com.theairebellion.zeus.ui.components.select.SelectService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.UIElement;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.List;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class SelectServiceFluent implements Insertion {

    private final SelectService selectService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public SelectServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, SelectService selectService) {
        this.selectService = selectService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public UIServiceFluent selectItems(DdlMode mode, final UIElement element, final String... values) {
        Allure.step(String.format("Selecting items: '%s' from select component of type: '%s'.", Arrays.toString(values),
            element.componentType().toString()));
        selectService.selectItems(element.componentType(), mode, element.locator(), values);
        return uiServiceFluent;
    }


    public UIServiceFluent getAvailableItems(final UIElement element) {
        List<String> availableItems = selectService.getAvailableItems(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), availableItems);
        return uiServiceFluent;
    }


    public UIServiceFluent getSelectedItems(final UIElement element) {
        List<String> selectedItems = selectService.getSelectedItems(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selectedItems);
        return uiServiceFluent;
    }


    public UIServiceFluent isOptionPresent(final UIElement element, final String value) {
        boolean present = selectService.isOptionPresent(element.locator(), value, element.componentType());
        storage.sub(UI).put(element.enumImpl(), present);
        return uiServiceFluent;
    }


    public UIServiceFluent isOptionEnabled(final UIElement element, final String value) {
        boolean enabled = selectService.isOptionEnabled(element.locator(), value, element.componentType());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    @Override
    public void insertion(final By locator, final ComponentType componentType, final Object... values) {
        selectService.insertion(locator, componentType, values);
    }

}
