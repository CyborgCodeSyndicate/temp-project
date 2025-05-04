package com.theairebellion.zeus.ui.components.modal.mock;

import com.theairebellion.zeus.ui.components.modal.ModalComponentType;
import com.theairebellion.zeus.ui.components.modal.ModalService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public class MockModalService implements ModalService {

    public ModalComponentType lastComponentTypeUsed;
    public ModalComponentType explicitComponentType;
    public SmartWebElement lastContainer;
    public String lastButtonText;
    public By lastButtonLocator;
    public boolean returnOpened;
    public String returnTitle;
    public String returnBodyText;
    public String returnContentTitle;

    public MockModalService() {
        reset();
    }

    private void setLastType(ModalComponentType type) {
        this.explicitComponentType = type;
        if (MockModalComponentType.DUMMY_MODAL.equals(type)) {
            this.lastComponentTypeUsed = MockModalComponentType.DUMMY_MODAL;
        } else {
            this.lastComponentTypeUsed = null;
        }
    }

    public void reset() {
        lastComponentTypeUsed = null;
        explicitComponentType = MockModalComponentType.DUMMY_MODAL;
        lastContainer = null;
        lastButtonText = null;
        lastButtonLocator = null;
        returnOpened = false;
        returnTitle = "";
        returnBodyText = "";
        returnContentTitle = "";
    }

    @Override
    public boolean isOpened(ModalComponentType componentType) {
        setLastType(componentType);
        return returnOpened;
    }

    @Override
    public void clickButton(ModalComponentType componentType, SmartWebElement container, String modalButtonText) {
        setLastType(componentType);
        lastContainer = container;
        lastButtonText = modalButtonText;
    }

    @Override
    public void clickButton(ModalComponentType componentType, String modalButtonText) {
        setLastType(componentType);
        lastButtonText = modalButtonText;
        lastContainer = null;
        lastButtonLocator = null;
    }

    @Override
    public void clickButton(ModalComponentType componentType, By modalButtonLocator) {
        setLastType(componentType);
        lastButtonLocator = modalButtonLocator;
        lastContainer = null;
        lastButtonText = null;
    }

    @Override
    public String getTitle(ModalComponentType componentType) {
        setLastType(componentType);
        return returnTitle;
    }

    @Override
    public String getBodyText(ModalComponentType componentType) {
        setLastType(componentType);
        return returnBodyText;
    }

    @Override
    public String getContentTitle(ModalComponentType componentType) {
        setLastType(componentType);
        return returnContentTitle;
    }

    @Override
    public void close(ModalComponentType componentType) {
        setLastType(componentType);
    }
}