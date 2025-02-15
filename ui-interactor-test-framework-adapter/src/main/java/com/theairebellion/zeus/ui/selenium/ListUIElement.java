package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListService;

public interface ListUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) ItemListService.DEFAULT_TYPE;
    }

}
