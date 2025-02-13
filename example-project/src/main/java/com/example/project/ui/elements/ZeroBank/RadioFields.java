package com.example.project.ui.elements.ZeroBank;

import com.example.project.ui.types.RadioFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioComponentType;
import com.theairebellion.zeus.ui.selenium.RadioUIElement;
import org.openqa.selenium.By;

public enum RadioFields implements RadioUIElement {

    DOLLARS_RADIO_FIELD(By.id("pc_inDollars_true"), RadioFieldTypes.BOOTSTRAP_RADIO_TYPE),
    ;

    private final By locator;
    private final RadioComponentType componentType;


    RadioFields(final By locator, final RadioComponentType componentType) {
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
