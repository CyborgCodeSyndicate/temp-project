package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;

public interface InputUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) InputService.DEFAULT_TYPE;
    }

}
