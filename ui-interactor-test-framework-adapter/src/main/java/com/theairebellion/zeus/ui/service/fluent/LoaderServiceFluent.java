package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.loader.LoaderService;
import com.theairebellion.zeus.ui.selenium.UIElement;
import io.qameta.allure.Allure;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class LoaderServiceFluent {

    private final LoaderService loaderService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;


    public LoaderServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, LoaderService loaderService) {
        this.loaderService = loaderService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
    }


    public UIServiceFluent isVisible(final UIElement element) {
        Allure.step(String.format("Checking if loader is visible for loader component of type: '%s'.",
                element.componentType().toString()));
        boolean visible = loaderService.isVisible(element.componentType(), element.locator());
        storage.sub(UI).put(element.enumImpl(), visible);
        return uiServiceFluent;
    }


    public UIServiceFluent waitToBeShown(final UIElement element, int secondsShown) {
        loaderService.waitToBeShown(element.componentType(), element.locator(), secondsShown);
        return uiServiceFluent;
    }


    public UIServiceFluent waitToBeRemoved(final UIElement element, int secondsRemoved) {
        loaderService.waitToBeRemoved(element.componentType(), element.locator(), secondsRemoved);
        return uiServiceFluent;
    }


    public UIServiceFluent waitToBeShownAndRemoved(final UIElement element, int secondsShown, int secondsRemoved) {
        loaderService.waitToBeShownAndRemoved(element.componentType(), element.locator(), secondsShown, secondsRemoved);
        return uiServiceFluent;
    }
}
