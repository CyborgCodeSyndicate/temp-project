package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonService;

public interface ButtonUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) ButtonService.DEFAULT_TYPE;
    }

}
