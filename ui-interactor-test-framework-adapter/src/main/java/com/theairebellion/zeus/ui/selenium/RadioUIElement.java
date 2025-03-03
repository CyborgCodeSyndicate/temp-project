package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioService;

public interface RadioUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) RadioService.DEFAULT_TYPE;
    }

}
