package com.example.project.ui.elements.Bakery;

import com.example.project.ui.functions.ContextConsumer;
import com.example.project.ui.functions.SharedUI;
import com.example.project.ui.types.ButtonFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.selenium.ButtonUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

public enum ButtonFields implements ButtonUIElement {

    SIGN_IN_BUTTON(By.tagName("vaadin-button"), ButtonFieldTypes.VA_BUTTON_TYPE, SharedUI.WAIT_FOR_LOADING) ,
    NEW_ORDER_BUTTON(By.cssSelector("vaadin-button#action"), ButtonFieldTypes.VA_BUTTON_TYPE),
    REVIEW_ORDER_BUTTON(By.cssSelector("vaadin-button#review"), ButtonFieldTypes.VA_BUTTON_TYPE),
    PLACE_ORDER_BUTTON(By.cssSelector("vaadin-button#save"), ButtonFieldTypes.VA_BUTTON_TYPE,
            smartWebDriver -> smartWebDriver.findSmartElement(By.cssSelector("vaadin-button#save"), 5000)),
    ;


    private final By locator;
    private final ButtonComponentType componentType;
    private final Consumer<SmartWebDriver> before;
    private final Consumer<SmartWebDriver> after;


    ButtonFields(By locator) {
        this(locator, null, smartWebDriver -> {
        }, smartWebDriver -> {
        });
    }


    ButtonFields(By locator, ButtonComponentType componentType) {
        this(locator, componentType, smartWebDriver -> {
        }, smartWebDriver -> {
        });
    }


    ButtonFields(By locator,
                 ButtonComponentType componentType,
                 Consumer<SmartWebDriver> before) {
        this(locator, componentType, before, smartWebDriver -> {
        });
    }


    ButtonFields(By locator,
                 ButtonComponentType componentType,
                 ContextConsumer before) {
        this(locator, componentType, before.asConsumer(locator), smartWebDriver -> {
        });
    }


    ButtonFields(By locator,
                 ButtonComponentType componentType,
                Consumer<SmartWebDriver> before,
                Consumer<SmartWebDriver> after) {
        this.locator = locator;
        this.componentType = componentType;
        this.before = before;
        this.after = after;
    }


    ButtonFields(By locator,
                 ButtonComponentType componentType,
                 ContextConsumer before,
                 ContextConsumer after) {
        this(locator, componentType, before.asConsumer(locator), after.asConsumer(locator));
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
