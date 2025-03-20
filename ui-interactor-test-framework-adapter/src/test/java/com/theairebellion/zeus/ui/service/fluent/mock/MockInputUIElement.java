package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.InputUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;


public class MockInputUIElement implements InputUIElement {

    private final By locator;
    private final Object enumKey;

    public MockInputUIElement(By locator, Object enumKey) {
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
        return MockInputComponentType.DUMMY;
    }

    @Override
    public <T extends ComponentType> T componentType() {
        return (T) MockInputComponentType.DUMMY;
    }
}
