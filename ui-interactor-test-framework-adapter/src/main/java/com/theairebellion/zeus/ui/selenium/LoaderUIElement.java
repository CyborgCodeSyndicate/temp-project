package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.loader.LoaderService;

public interface LoaderUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) LoaderService.DEFAULT_TYPE;
    }

}
