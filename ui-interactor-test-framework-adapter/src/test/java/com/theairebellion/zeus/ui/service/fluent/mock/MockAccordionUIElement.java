package com.theairebellion.zeus.ui.service.fluent.mock;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.AccordionUIElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;

import java.util.function.Consumer;

public class MockAccordionUIElement implements AccordionUIElement {

    private final By locator;
    private final Object enumKey;

    public MockAccordionUIElement(By locator, Object enumKey) {
        this.locator = locator;
        this.enumKey = enumKey;
    }

    @Override
    public By locator() {
        return locator;
    }

    @Override
    public Consumer<SmartWebDriver> before() {
        return w -> {};
    }

    @Override
    public Consumer<SmartWebDriver> after() {
        return w -> {};
    }

    @Override
    public Enum<?> enumImpl() {
        return MockAccordionComponentType.DUMMY;
    }

    @Override
    public <T extends ComponentType> T componentType() {
        return (T) MockAccordionComponentType.DUMMY;
    }
}
