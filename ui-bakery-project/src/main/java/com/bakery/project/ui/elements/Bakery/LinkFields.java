package com.bakery.project.ui.elements.Bakery;

import com.bakery.project.ui.functions.ContextConsumer;
import com.bakery.project.ui.types.LinkFieldTypes;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.button.ButtonComponentType;
import com.theairebellion.zeus.ui.components.link.LinkComponentType;
import com.theairebellion.zeus.ui.selenium.LinkUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

public enum LinkFields implements LinkUIElement {

    STOREFRONT_LINK(By.cssSelector("a[href='dashboard']"), LinkFieldTypes.VA_LINK),
    DASHBOARD_LINK(By.cssSelector("a[href='dashboard']"), LinkFieldTypes.VA_LINK),
    LOGOUT_LINK(By.cssSelector("a[href='/logout']"), LinkFieldTypes.VA_LINK),
    ;

    private final By locator;
    private final LinkComponentType componentType;
    private final Consumer<SmartWebDriver> before;
    private final Consumer<SmartWebDriver> after;


    LinkFields(By locator) {
        this(locator, null, smartWebDriver -> {
        }, smartWebDriver -> {
        });
    }


    LinkFields(By locator, LinkComponentType componentType) {
        this(locator, componentType, smartWebDriver -> {
        }, smartWebDriver -> {
        });
    }


    LinkFields(By locator,
               LinkComponentType componentType,
               Consumer<SmartWebDriver> before) {
        this(locator, componentType, before, smartWebDriver -> {
        });
    }


    LinkFields(By locator,
               LinkComponentType componentType,
               ContextConsumer before) {
        this(locator, componentType, before.asConsumer(locator), smartWebDriver -> {
        });
    }


    LinkFields(By locator,
               LinkComponentType componentType,
               Consumer<SmartWebDriver> before,
               Consumer<SmartWebDriver> after) {
        this.locator = locator;
        this.componentType = componentType;
        this.before = before;
        this.after = after;
    }


    LinkFields(By locator,
               LinkComponentType componentType,
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
