package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.modal.ModalService;
import com.theairebellion.zeus.ui.selenium.UIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;
import org.assertj.core.api.Assertions;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class ModalServiceFluent {

    private final ModalService modalService;
    private final UIServiceFluent uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public ModalServiceFluent(UIServiceFluent uiServiceFluent, Storage storage, ModalService modalService,
                              SmartWebDriver webDriver) {
        this.modalService = modalService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public UIServiceFluent click(final UIElement element) {
        Allure.step(String.format("Clicking button with locator: '%s' from button component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        element.before().accept(driver);
        modalService.clickButton(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public UIServiceFluent getTitle(final UIElement element) {
        element.before().accept(driver);
        String modalTitle = modalService.getTitle(element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), modalTitle);
        return uiServiceFluent;
    }


    public UIServiceFluent getContentTitle(final UIElement element) {
        element.before().accept(driver);
        String modalContentTitle = modalService.getContentTitle(element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), modalContentTitle);
        return uiServiceFluent;
    }


    public UIServiceFluent getBodyText(final UIElement element) {
        element.before().accept(driver);
        String modalBodyText = modalService.getBodyText(element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), modalBodyText);
        return uiServiceFluent;
    }


    public UIServiceFluent close(final UIElement element) {
        element.before().accept(driver);
        modalService.close(element.componentType());
        element.after().accept(driver);
        return uiServiceFluent;
    }


//todo: Implement validation functions

}
