package com.theairebellion.zeus.ui.components.factory.mock;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

public class FailImpl implements MockInterface {
    private final SmartWebDriver smartWebDriver;

    FailImpl(SmartWebDriver driver) {
        smartWebDriver = driver;
        throw new IllegalStateException("Failure!");
    }

    @Override
    public Enum<?> getType() {
        return MockComponentType.FAIL;
    }
}
