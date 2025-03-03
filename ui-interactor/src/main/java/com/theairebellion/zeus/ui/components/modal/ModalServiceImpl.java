package com.theairebellion.zeus.ui.components.modal;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

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
        return modalComponent(componentType).isOpened();
    }

    @Override
    public void clickButton(final ModalComponentType componentType, final SmartWebElement container, final String modalButtonText) {
        modalComponent(componentType).clickButton(container, modalButtonText);
    }

    @Override
    public void clickButton(final ModalComponentType componentType, final String modalButtonText) {
        modalComponent(componentType).clickButton(modalButtonText);
    }

    @Override
    public void clickButton(final ModalComponentType componentType, final By modalButtonLocator) {
        modalComponent(componentType).clickButton(modalButtonLocator);
    }

    @Override
    public String getTitle(final ModalComponentType componentType) {
        return modalComponent(componentType).getTitle();
    }

    @Override
    public String getBodyText(final ModalComponentType componentType) {
        return modalComponent(componentType).getBodyText();
    }

    @Override
    public String getContentTitle(final ModalComponentType componentType) {
        return modalComponent(componentType).getContentTitle();
    }

    @Override
    public void close(final ModalComponentType componentType) {
        modalComponent(componentType).close();
    }

    private Modal modalComponent(final ModalComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
