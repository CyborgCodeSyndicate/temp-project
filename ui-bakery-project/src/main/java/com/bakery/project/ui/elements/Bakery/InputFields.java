package com.bakery.project.ui.elements.Bakery;

import com.bakery.project.ui.functions.ContextConsumer;
import com.bakery.project.ui.functions.SharedUI;
import com.bakery.project.ui.types.InputFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.selenium.InputUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

public enum InputFields implements InputUIElement {

    USERNAME_FIELD(By.id("vaadinLoginUsername"), InputFieldTypes.VA_INPUT_TYPE),
    PASSWORD_FIELD(By.id("vaadinLoginPassword"), InputFieldTypes.VA_INPUT_TYPE),
    SEARCH_BAR_FIELD(By.cssSelector("search-bar#search"), InputFieldTypes.VA_INPUT_TYPE,
            SharedUI.WAIT_FOR_PRESENCE,
            SharedUI.WAIT_FOR_LOADING), //todo: test the loading
    CUSTOMER_FIELD(By.id("customerName"), InputFieldTypes.VA_INPUT_TYPE,
            SharedUI.WAIT_FOR_PRESENCE),
    DETAILS_FIELD(By.id("customerDetails"), InputFieldTypes.VA_INPUT_TYPE,
            SharedUI.WAIT_FOR_PRESENCE),
    NUMBER_FIELD(By.id("customerNumber"), InputFieldTypes.VA_INPUT_TYPE,
            SharedUI.WAIT_FOR_PRESENCE),
    ;


    public static final class Data {

        public static final String USERNAME_FIELD = "USERNAME_FIELD";
        public static final String PASSWORD_FIELD = "PASSWORD_FIELD";
        public static final String CUSTOMER_FIELD = "CUSTOMER_FIELD";
        public static final String DETAILS_FIELD = "DETAILS_FIELD";
        public static final String NUMBER_FIELD = "NUMBER_FIELD";


        private Data() {
        }

    }



    private final By locator;
    private final InputComponentType componentType;
    private final Consumer<SmartWebDriver> before;
    private final Consumer<SmartWebDriver> after;


    InputFields(By locator) {
        this(locator, null, smartWebDriver -> {
        }, smartWebDriver -> {
        });
    }

    InputFields(By locator, InputComponentType componentType) {
        this(locator, componentType, smartWebDriver -> {
        }, smartWebDriver -> {
        });
    }


    InputFields(By locator,
                InputComponentType componentType,
                 Consumer<SmartWebDriver> before) {
        this(locator, componentType, before, smartWebDriver -> {
        });
    }


    InputFields(By locator,
                InputComponentType componentType,
                 ContextConsumer before) {
        this(locator, componentType, before.asConsumer(locator), smartWebDriver -> {
        });
    }


    InputFields(By locator,
                InputComponentType componentType,
                 Consumer<SmartWebDriver> before,
                 Consumer<SmartWebDriver> after) {
        this.locator = locator;
        this.componentType = componentType;
        this.before = before;
        this.after = after;
    }


    InputFields(By locator,
                InputComponentType componentType,
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
        if (componentType == null) {
            return InputUIElement.super.componentType();
        }
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
