package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.alert.AlertService;
import com.theairebellion.zeus.ui.selenium.UIElement;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class AlertServiceFluent {

    private final AlertService alertService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public AlertServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, AlertService alertService) {
        this.alertService = alertService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public UIServiceFluent getValue(final UIElement element) {
        String value = alertService.getValue(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), value);
        return uiServiceFluent;
    }


    public UIServiceFluent isVisible(final UIElement element) {
        boolean enabled = alertService.isVisible(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }
}
