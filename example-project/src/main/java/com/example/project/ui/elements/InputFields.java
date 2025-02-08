package com.example.project.ui.elements;

import com.example.project.ui.functions.SharedUI;
import com.example.project.ui.types.InputFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.selenium.InputUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

public enum InputFields implements InputUIElement {

    USERNAME(
        By.cssSelector("locator_username"),
        InputFieldTypes.MD_INPUT,
        SharedUI.WAIT_FOR_LOADING,
        SharedUI.WAIT_FOR_LOADING
    ),
    PASSWORD(
        By.cssSelector("locator_password")
    );

    public static final class Data {

        public static final String USERNAME = "USERNAME";
        public static final String PASSWORD = "PASSWORD";


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
