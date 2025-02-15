package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.alert.AlertService;
import com.theairebellion.zeus.ui.components.base.ComponentType;

public interface AlertUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) AlertService.DEFAULT_TYPE;
    }

}
