package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface UIElement {

    By locator();

    <T extends ComponentType> T componentType();

    Enum<?> enumImpl();

    default Consumer<SmartWebDriver> before(){
        return smartWebDriver -> {};
    }

    default Consumer<SmartWebDriver> after(){
        return smartWebDriver -> {};
    }

}
