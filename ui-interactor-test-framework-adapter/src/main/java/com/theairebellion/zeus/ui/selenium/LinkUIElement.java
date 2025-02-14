package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.link.LinkService;

public interface LinkUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) LinkService.DEFAULT_TYPE;
    }

}
