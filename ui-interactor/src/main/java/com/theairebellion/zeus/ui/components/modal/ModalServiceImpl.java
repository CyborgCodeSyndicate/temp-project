package com.theairebellion.zeus.ui.components.modal;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import io.qameta.allure.Step;

public class ModalServiceImpl extends AbstractComponentService<ModalComponentType, Modal> implements ModalService {

    public ModalServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Modal createComponent(ModalComponentType componentType) {
        return ComponentFactory.getModalComponent(componentType, driver);
    }

    @Step("Checking if modal {componentType} is opened")
    @Override
    public boolean isOpened(final ModalComponentType componentType) {
        LogUI.step("Checking if modal " + componentType + " is opened");
        return modalComponent(componentType).isOpened();
    }

    @Step("Clicking button with text {modalButtonText} in modal {componentType} using container")
    @Override
    public void clickButton(final ModalComponentType componentType, final SmartWebElement container, final String modalButtonText) {
        LogUI.step("Clicking button with text " + modalButtonText + " in modal " + componentType + " using container");
        modalComponent(componentType).clickButton(container, modalButtonText);
    }

    @Step("Clicking button with text {modalButtonText} in modal {componentType}")
    @Override
    public void clickButton(final ModalComponentType componentType, final String modalButtonText) {
        LogUI.step("Clicking button with text " + modalButtonText + " in modal " + componentType);
        modalComponent(componentType).clickButton(modalButtonText);
    }

    @Step("Clicking button with locator {modalButtonLocator} in modal {componentType}")
    @Override
    public void clickButton(final ModalComponentType componentType, final By modalButtonLocator) {
        LogUI.step("Clicking button with locator " + modalButtonLocator + " in modal " + componentType);
        modalComponent(componentType).clickButton(modalButtonLocator);
    }

    @Step("Getting title of modal {componentType}")
    @Override
    public String getTitle(final ModalComponentType componentType) {
        LogUI.step("Getting title of modal " + componentType);
        return modalComponent(componentType).getTitle();
    }

    @Step("Getting body text of modal {componentType}")
    @Override
    public String getBodyText(final ModalComponentType componentType) {
        LogUI.step("Getting body text of modal " + componentType);
        return modalComponent(componentType).getBodyText();
    }

    @Step("Getting content title of modal {componentType}")
    @Override
    public String getContentTitle(final ModalComponentType componentType) {
        LogUI.step("Getting content title of modal " + componentType);
        return modalComponent(componentType).getContentTitle();
    }

    @Step("Closing modal {componentType}")
    @Override
    public void close(final ModalComponentType componentType) {
        LogUI.step("Closing modal " + componentType);
        modalComponent(componentType).close();
    }

    private Modal modalComponent(final ModalComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
