package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.link.LinkService;
import com.theairebellion.zeus.ui.selenium.UIElement;
import io.qameta.allure.Allure;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class LinkServiceFluent {

    private final LinkService linkService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public LinkServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, LinkService linkService) {
        this.linkService = linkService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public UIServiceFluent click(final UIElement element) {
        Allure.step(String.format("Clicking link with locator: '%s' from link component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        linkService.click(element.componentType(), element.locator());
        return uiServiceFluent;
    }


    public UIServiceFluent doubleClick(final UIElement element) {
        linkService.doubleClick(element.componentType(), element.locator());
        return uiServiceFluent;
    }


    public UIServiceFluent isEnabled(final UIElement element) {
        boolean enabled = linkService.isEnabled(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), enabled);
        return uiServiceFluent;
    }


    public UIServiceFluent isPresent(final UIElement element) {
        boolean present = linkService.isPresent(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), present);
        return uiServiceFluent;
    }
}
