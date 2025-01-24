package com.example.project.ui.elements.bootstrap;

import com.example.project.ui.types.ButtonFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.selenium.UIElement;
import org.openqa.selenium.By;

public enum ButtonFields implements UIElement {

    SIGN_IN_BUTTON(By.id("signin_button"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
    SIGN_IN_FORM_BUTTON(By.cssSelector("input[value='Sign in']"), ButtonFieldTypes.BOOTSTRAP_INPUT_TYPE),
    ;

    public static final String SIGN_IN = "SIGN_IN_BUTTON";
    public static final String SIGN_IN_FORM = "SIGN_IN_FORM_BUTTON";


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
