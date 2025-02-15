package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.select.SelectService;

public interface SelectUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) SelectService.DEFAULT_TYPE;
    }

}
