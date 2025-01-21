package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.button.ButtonService;
import com.theairebellion.zeus.ui.selenium.UIElement;
import io.qameta.allure.Allure;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class ButtonServiceFluent {

    private final ButtonService buttonService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public ButtonServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, ButtonService buttonService) {
        this.buttonService = buttonService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public UIServiceFluent click(final UIElement element) {
        Allure.step(String.format("Clicking button with locator: '%s' from button component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        buttonService.click(element.componentType(), element.locator());
        return uiServiceFluent;
    }


    public UIServiceFluent isEnabled(final UIElement element) {
        boolean enabled = buttonService.isEnabled(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public UIServiceFluent isPresent(final UIElement element) {
        boolean present = buttonService.isPresent(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), present);
        return uiServiceFluent;
    }
}
