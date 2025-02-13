package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.toggle.ToggleService;

public interface ToggleUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) ToggleService.DEFAULT_TYPE;
    }

}
