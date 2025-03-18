package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ai.metadata.model.CreationType;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;

@InfoAIClass(creationType = CreationType.ENUM,
    description = "Interface representing UI Elements that needs to be implemented in enum and specify the locator and the type of the input component")
public interface InputUIElement extends UIElement {

    @Override
    default <T extends ComponentType> T componentType() {
        return (T) InputService.DEFAULT_TYPE;
    }

}
