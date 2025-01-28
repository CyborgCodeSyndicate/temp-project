package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.toggle.ToggleService;
import com.theairebellion.zeus.ui.selenium.UIElement;
import io.qameta.allure.Allure;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class ToggleServiceFluent {

    private final ToggleService toggleService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;

    public ToggleServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, ToggleService toggleService) {
        this.uiServiceFluent = uiServiceFluent;
        this.toggleService = toggleService;
        this.storage = storage;
    }

    public UIServiceFluent activate(final UIElement element) {
        Allure.step(String.format("Activating toggle with locator: '%s' from toggle component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        toggleService.activate(element.componentType(), element.locator());
        return uiServiceFluent;
    }

    public UIServiceFluent deactivate(final UIElement element) {
        Allure.step(String.format("Deactivating toggle with locator: '%s' from toggle component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        toggleService.deactivate(element.componentType(), element.locator());
        return uiServiceFluent;
    }

    public UIServiceFluent isActivated(final UIElement element) {
        boolean enabled = toggleService.isActivated(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }

    public UIServiceFluent isEnabled(final UIElement element) {
        boolean enabled = toggleService.isEnabled(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }
}
