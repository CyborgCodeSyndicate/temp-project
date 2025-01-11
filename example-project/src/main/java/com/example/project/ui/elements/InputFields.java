package com.example.project.ui.elements;

import com.example.project.ui.types.InputFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.selenium.UIElement;
import org.openqa.selenium.By;

public enum InputFields implements UIElement {

    USERNAME_FIELD(By.cssSelector("locator_username"), InputFieldTypes.MD_INPUT_TYPE),
    PASSWORD_FIELD(By.cssSelector("locator_password"), InputFieldTypes.MD_INPUT_TYPE),
    ;

    public static final String USERNAME = "USERNAME_FIELD";
    public static final String PASSWORD = "PASSWORD_FIELD";


    private final By locator;
    private final InputComponentType componentType;


    InputFields(final By locator, final InputComponentType componentType) {
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
