package com.example.project.ui.elements.Bakery;

import com.example.project.ui.types.InputFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.selenium.InputUIElement;
import org.openqa.selenium.By;

public enum InputFields implements InputUIElement {

    USERNAME_FIELD(By.id("vaadinLoginUsername"), InputFieldTypes.VA_INPUT_TYPE),
    PASSWORD_FIELD(By.id("vaadinLoginPassword"), InputFieldTypes.VA_INPUT_TYPE),
    ;


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
