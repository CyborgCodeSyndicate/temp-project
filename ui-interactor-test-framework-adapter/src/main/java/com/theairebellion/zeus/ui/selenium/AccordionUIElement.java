package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.accordion.AccordionService;
import com.theairebellion.zeus.ui.components.base.ComponentType;

public interface AccordionUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) AccordionService.DEFAULT_TYPE;
    }

}
