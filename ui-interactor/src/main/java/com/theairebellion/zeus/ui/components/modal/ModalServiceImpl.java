package com.theairebellion.zeus.ui.components.modal;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import io.qameta.allure.Allure;

public class ModalServiceImpl extends AbstractComponentService<ModalComponentType, Modal> implements ModalService {

    public ModalServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Modal createComponent(ModalComponentType componentType) {
        return ComponentFactory.getModalComponent(componentType, driver);
    }

    @Override
    public boolean isOpened(final ModalComponentType componentType) {
        Allure.step(String.format("[UI - Modal] Checking if modal %s is opened", componentType));
        LogUI.step("Checking if modal " + componentType + " is opened");
        return modalComponent(componentType).isOpened();
    }

    @Override
    public void clickButton(final ModalComponentType componentType, final SmartWebElement container, final String modalButtonText) {
        Allure.step(String.format("[UI - Modal] Clicking button with text %s in modal %s using container", modalButtonText, componentType));
        LogUI.step("Clicking button with text " + modalButtonText + " in modal " + componentType + " using container");
        modalComponent(componentType).clickButton(container, modalButtonText);
    }

    @Override
    public void clickButton(final ModalComponentType componentType, final String modalButtonText) {
        Allure.step(String.format("[UI - Modal] Clicking button with text %s in modal %s", modalButtonText, componentType));
        LogUI.step("Clicking button with text " + modalButtonText + " in modal " + componentType);
        modalComponent(componentType).clickButton(modalButtonText);
    }

    @Override
    public void clickButton(final ModalComponentType componentType, final By modalButtonLocator) {
        Allure.step(String.format("[UI - Modal] Clicking button with locator %s in modal %s", modalButtonLocator, componentType));
        LogUI.step("Clicking button with locator " + modalButtonLocator + " in modal " + componentType);
        modalComponent(componentType).clickButton(modalButtonLocator);
    }

    @Override
    public String getTitle(final ModalComponentType componentType) {
        Allure.step(String.format("[UI - Modal] Getting title of modal %s", componentType));
        LogUI.step("Getting title of modal " + componentType);
        return modalComponent(componentType).getTitle();
    }

    @Override
    public String getBodyText(final ModalComponentType componentType) {
        Allure.step(String.format("[UI - Modal] Getting body text of modal %s", componentType));
        LogUI.step("Getting body text of modal " + componentType);
        return modalComponent(componentType).getBodyText();
    }

    @Override
    public String getContentTitle(final ModalComponentType componentType) {
        Allure.step(String.format("[UI - Modal] Getting content title of modal %s", componentType));
        LogUI.step("Getting content title of modal " + componentType);
        return modalComponent(componentType).getContentTitle();
    }

    @Override
    public void close(final ModalComponentType componentType) {
        Allure.step(String.format("[UI - Modal] Closing modal %s", componentType));
        LogUI.step("Closing modal " + componentType);
        modalComponent(componentType).close();
    }

    private Modal modalComponent(final ModalComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
