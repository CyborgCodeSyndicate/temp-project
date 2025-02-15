package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxService;

public interface CheckboxUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) CheckboxService.DEFAULT_TYPE;
    }

}
