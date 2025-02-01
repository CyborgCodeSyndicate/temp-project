package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.tab.TabService;
import com.theairebellion.zeus.ui.selenium.UIElement;
import io.qameta.allure.Allure;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class TabServiceFluent {

    private final TabService tabService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public TabServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, TabService tabService) {
        this.tabService = tabService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public UIServiceFluent click(final UIElement element) {
        Allure.step(String.format("Clicking tab with locator: '%s' from tab component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        tabService.click(element.componentType(), element.locator());
        return uiServiceFluent;
    }


    public UIServiceFluent isEnabled(final UIElement element) {
        boolean enabled = tabService.isEnabled(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public UIServiceFluent isVisible(final UIElement element) {
        boolean visible = tabService.isVisible(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }


    public UIServiceFluent isSelected(final UIElement element) {
        boolean selected = tabService.isSelected(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), selected);
        return uiServiceFluent;
    }
}
