package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.insertion.Insertion;
import com.theairebellion.zeus.ui.selenium.UIElement;
import com.theairebellion.zeus.framework.storage.Storage;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class InputServiceFluent implements Insertion {

    private final InputService inputService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public InputServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, InputService inputService) {
        this.inputService = inputService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public UIServiceFluent insert(final UIElement element, final String value) {
        Allure.step(String.format("Inserting value: '%s' into input component of type: '%s'.", value,
            element.componentType().toString()));
        inputService.insert(element.locator(), value, element.componentType());
        return uiServiceFluent;
    }


    public UIServiceFluent clear(final UIElement element) {
        inputService.clear(element.locator(), element.componentType());
        return uiServiceFluent;
    }


    public UIServiceFluent getValue(final UIElement element) {
        String value = inputService.getValue(element.locator(), element.componentType());
        storage.sub(UI).put(element.enumImpl(), value);
        return uiServiceFluent;
    }


    public UIServiceFluent isEnabled(final UIElement element) {
        boolean enabled = inputService.isEnabled(element.locator(), element.componentType());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public UIServiceFluent getErrorMessage(final UIElement element) {
        String errorMessage = inputService.getErrorMessage(element.locator(), element.componentType());
        storage.sub(UI).put(element.enumImpl(), errorMessage);
        return uiServiceFluent;
    }


    @Override
    public void insertion(final By locator, final ComponentType componentType, final Object... values) {
        inputService.insertion(locator, componentType, values);
    }

}
