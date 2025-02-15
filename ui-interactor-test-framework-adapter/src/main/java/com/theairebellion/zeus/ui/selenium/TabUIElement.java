package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.tab.TabService;

public interface TabUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) TabService.DEFAULT_TYPE;
    }

}
