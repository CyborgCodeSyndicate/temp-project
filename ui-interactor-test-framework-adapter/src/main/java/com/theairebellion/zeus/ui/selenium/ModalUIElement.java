package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.modal.ModalService;

public interface ModalUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) ModalService.DEFAULT_TYPE;
    }

}
