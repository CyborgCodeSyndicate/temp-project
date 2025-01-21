package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.UIElement;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class RadioServiceFluent implements Insertion {

    private final RadioService radioService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public RadioServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, RadioService radioService) {
        this.radioService = radioService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public UIServiceFluent select(final UIElement element, final String value) {
        Allure.step(String.format("Selecting value: '%s' from radio component of type: '%s'.", value,
            element.componentType().toString()));
        radioService.select(value, element.componentType());
        return uiServiceFluent;
    }


    public UIServiceFluent isEnabled(final UIElement element) {
        boolean enabled = radioService.isEnabled(element.locator(), element.componentType());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public UIServiceFluent isSelected(final UIElement element) {
        boolean selected = radioService.isSelected(element.locator(), element.componentType());
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }


    public UIServiceFluent isVisible(final UIElement element) {
        boolean visible = radioService.isVisible(element.locator(), element.componentType());
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }


    public UIServiceFluent getSelected(final UIElement element) {
        String selectedValue = radioService.getSelected(element.locator(), element.componentType());
        storage.sub(UI).put(element.enumImpl(), selectedValue);
        return uiServiceFluent;
    }


    public UIServiceFluent getAll(final UIElement element) {
        radioService.getAll(element.locator(), element.componentType());
        return uiServiceFluent;
    }


    @Override
    public void insertion(final By locator, final ComponentType componentType, final Object... values) {
        radioService.insertion(locator, componentType, values);
    }

}
