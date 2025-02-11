package com.example.project.ui.elements.Bakery;

import com.example.project.ui.types.SelectFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.selenium.SelectUIElement;
import org.openqa.selenium.By;

public enum SelectFields implements SelectUIElement {

    LOCATION_DDL(By.cssSelector("vaadin-form-layout#form2 vaadin-combo-box#pickupLocation"), SelectFieldTypes.VA_SELECT_TYPE);

    private final By locator;
    private final SelectComponentType componentType;


    SelectFields(final By locator, final SelectComponentType componentType) {
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
