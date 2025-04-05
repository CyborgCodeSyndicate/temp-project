package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.SelectUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;


public class MockSelectUIElement implements SelectUIElement {

    private final By locator;
    private final Object enumKey;

    public MockSelectUIElement(By locator, Object enumKey) {
        this.locator = locator;
        this.enumKey = enumKey;
    }

    @Override
    public By locator() {
        return locator;
    }

    @Override
    public Consumer<SmartWebDriver> before() {
        return w -> {
        };
    }

    @Override
    public Consumer<SmartWebDriver> after() {
        return w -> {
        };
    }

    @Override
    public Enum<?> enumImpl() {
        return MockSelectComponentType.DUMMY;
    }

    @Override
    public <T extends ComponentType> T componentType() {
        return (T) MockSelectComponentType.DUMMY;
    }
}
