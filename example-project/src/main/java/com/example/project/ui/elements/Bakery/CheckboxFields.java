package com.example.project.ui.elements.Bakery;

import com.example.project.ui.types.CheckboxFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;
import com.theairebellion.zeus.ui.selenium.CheckboxUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

public enum CheckboxFields implements CheckboxUIElement {

    PAST_ORDERS_CHECKBOX(By.tagName("vaadin-checkbox"), CheckboxFieldTypes.VA_CHECKBOX_TYPE),
    ;


    private final By locator;
    private final CheckboxComponentType componentType;
    private final Consumer<SmartWebDriver> before;
    private final Consumer<SmartWebDriver> after;


    CheckboxFields(By locator) {
        this(locator, null, smartWebDriver -> {
        }, smartWebDriver -> {
        });
    }


    CheckboxFields(By locator, CheckboxComponentType componentType) {
        this(locator, componentType, smartWebDriver -> {
        }, smartWebDriver -> {
        });
    }


    CheckboxFields(By locator,
                   CheckboxComponentType componentType,
                   Consumer<SmartWebDriver> before) {
        this(locator, componentType, before, smartWebDriver -> {
        });
    }


    CheckboxFields(By locator,
                   CheckboxComponentType componentType,
                   Consumer<SmartWebDriver> before,
                   Consumer<SmartWebDriver> after) {
        this.locator = locator;
        this.componentType = componentType;
        this.before = before;
        this.after = after;
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


    @Override
    public Consumer<SmartWebDriver> before() {
        return before;
    }


    @Override
    public Consumer<SmartWebDriver> after() {
        return after;
    }

}
