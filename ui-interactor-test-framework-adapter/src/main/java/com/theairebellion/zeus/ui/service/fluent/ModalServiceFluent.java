package com.theairebellion.zeus.ui.service.fluent;


import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.ui.components.modal.ModalService;
import com.theairebellion.zeus.ui.selenium.ModalUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import io.qameta.allure.Allure;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class ModalServiceFluent<T extends UIServiceFluent<?>> {

    private final ModalService modalService;
    private final T uiServiceFluent;
    private final Storage storage;
    private final SmartWebDriver driver;


    public ModalServiceFluent(T uiServiceFluent, Storage storage, ModalService modalService,
                              SmartWebDriver webDriver) {
        this.modalService = modalService;
        this.uiServiceFluent = uiServiceFluent;
        this.storage = storage;
        driver = webDriver;
    }


    public T click(final ModalUIElement element) {
        Allure.step(String.format("Clicking button with locator: '%s' from button component of type: '%s'.",
                element.locator().toString(),
                element.componentType().toString()));
        element.before().accept(driver);
        modalService.clickButton(element.componentType(), element.locator());
        element.after().accept(driver);
        return uiServiceFluent;
    }


    public T getTitle(final ModalUIElement element) {
        element.before().accept(driver);
        String modalTitle = modalService.getTitle(element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), modalTitle);
        return uiServiceFluent;
    }


    public T getContentTitle(final ModalUIElement element) {
        element.before().accept(driver);
        String modalContentTitle = modalService.getContentTitle(element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), modalContentTitle);
        return uiServiceFluent;
    }


    public T getBodyText(final ModalUIElement element) {
        element.before().accept(driver);
        String modalBodyText = modalService.getBodyText(element.componentType());
        element.after().accept(driver);
        storage.sub(UI).put(element.enumImpl(), modalBodyText);
        return uiServiceFluent;
    }


    public T close(final ModalUIElement element) {
        element.before().accept(driver);
        modalService.close(element.componentType());
        element.after().accept(driver);
        return uiServiceFluent;
    }


//todo: Implement validation functions

}
