package com.theairebellion.zeus.ui.components.factory.mock;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

@ImplementationOfType("DUMMY")
public record MockImpl(SmartWebDriver driver) implements MockInterface {
    @Override
    public Enum<?> getType() {
        return MockComponentType.DUMMY;
    }
}
