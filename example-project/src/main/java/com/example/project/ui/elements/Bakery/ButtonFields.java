package com.example.project.ui.elements.Bakery;

import com.example.project.ui.types.ButtonFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.selenium.ButtonUIElement;
import org.openqa.selenium.By;

public enum ButtonFields implements ButtonUIElement {

    SIGN_IN_BUTTON(By.tagName("vaadin-button"), ButtonFieldTypes.VA_BUTTON_TYPE),
    NEW_ORDER_BUTTON(By.cssSelector("vaadin-button#action"), ButtonFieldTypes.VA_BUTTON_TYPE),
    ;


    private final By locator;
    private final ButtonComponentType componentType;


    ButtonFields(final By locator, final ButtonComponentType componentType) {
        this.locator = locator;
        this.componentType = componentType;
    }


    @Override
    public By locator() {
        return locator;
    }


    @Override
    public <T extends ComponentType> T componentType() {
        return (T) componentType;
    }


    @Override
    public Enum<?> enumImpl() {
        return this;
    }

}
