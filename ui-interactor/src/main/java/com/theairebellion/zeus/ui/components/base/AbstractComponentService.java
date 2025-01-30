package com.theairebellion.zeus.ui.components.base;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractComponentService<T extends ComponentType, C> {

    protected final SmartWebDriver driver;
    private final Map<T, C> components;


    protected AbstractComponentService(SmartWebDriver driver) {
        this.driver = Objects.requireNonNull(driver, "Driver must not be null");
        this.components = new HashMap<>();
    }


    protected C getOrCreateComponent(T componentType) {
        return components.computeIfAbsent(componentType, this::createComponent);
    }


    protected abstract C createComponent(T componentType);

}