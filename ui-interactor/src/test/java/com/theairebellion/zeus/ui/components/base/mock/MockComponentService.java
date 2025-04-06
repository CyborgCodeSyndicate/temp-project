package com.theairebellion.zeus.ui.components.base.mock;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

public class MockComponentService extends AbstractComponentService<MockType, MockComponent> {

    public MockComponentService(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected MockComponent createComponent(MockType componentType) {
        return new MockComponent(componentType.name());
    }
}
