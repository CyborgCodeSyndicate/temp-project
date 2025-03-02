package com.example.project.ui.elements.Bakery;

import com.example.project.ui.functions.ContextConsumer;
import com.example.project.ui.functions.SharedUI;
import com.example.project.ui.types.SelectFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.selenium.SelectUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

public enum SelectFields implements SelectUIElement {

    LOCATION_DDL(By.cssSelector("vaadin-combo-box#pickupLocation"), SelectFieldTypes.VA_SELECT_TYPE,
            SharedUI.WAIT_FOR_PRESENCE),
    PRODUCTS_DDL(By.cssSelector("vaadin-combo-box#products"), SelectFieldTypes.VA_SELECT_TYPE,
            SharedUI.WAIT_FOR_PRESENCE);


    private final By locator;
    private final SelectComponentType componentType;
    private final Consumer<SmartWebDriver> before;
    private final Consumer<SmartWebDriver> after;


    public static final class Data {

        public static final String LOCATION_DDL = "LOCATION_DDL";
        public static final String PRODUCTS_DDL = "PRODUCTS_DDL";

        private Data() {
        }

    }


    SelectFields(By locator) {
        this(locator, null, smartWebDriver -> {
        }, smartWebDriver -> {
        });
    }


    SelectFields(By locator, SelectComponentType componentType) {
        this(locator, componentType, smartWebDriver -> {
        }, smartWebDriver -> {
        });
    }


    SelectFields(By locator,
                 SelectComponentType componentType,
                Consumer<SmartWebDriver> before) {
        this(locator, componentType, before, smartWebDriver -> {
        });
    }


    SelectFields(By locator,
                 SelectComponentType componentType,
                 ContextConsumer before) {
        this(locator, componentType, before.asConsumer(locator), smartWebDriver -> {
        });
    }


    SelectFields(By locator,
                 SelectComponentType componentType,
                Consumer<SmartWebDriver> before,
                Consumer<SmartWebDriver> after) {
        this.locator = locator;
        this.componentType = componentType;
        this.before = before;
        this.after = after;
    }


    SelectFields(By locator,
                 SelectComponentType componentType,
                 ContextConsumer before,
                 ContextConsumer after) {
        this(locator, componentType, before.asConsumer(locator), after.asConsumer(locator));
    }


    SelectFields(By locator,
                 SelectComponentType componentType,
                 ContextConsumer before,
                 Consumer<SmartWebDriver> after) {
        this(locator, componentType, before.asConsumer(locator), after);
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
