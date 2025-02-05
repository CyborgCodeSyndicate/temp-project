package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AlertServiceImpl extends AbstractComponentService<AlertComponentType, Alert> implements AlertService {

    public AlertServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Alert createComponent(final AlertComponentType componentType) {
        return ComponentFactory.getAlertComponent(componentType, driver);
    }

    @Override
    public String getValue(AlertComponentType componentType, SmartWebElement container) {
        return alertComponent(componentType).getValue(container);
    }

    @Override
    public String getValue(AlertComponentType componentType, By containerLocator) {
        return alertComponent(componentType).getValue(containerLocator);
    }

    @Override
    public boolean isVisible(AlertComponentType componentType, SmartWebElement container) {
        return alertComponent(componentType).isVisible(container);
    }

    @Override
    public boolean isVisible(AlertComponentType componentType, By containerLocator) {
        return alertComponent(componentType).isVisible(containerLocator);
    }

    private Alert alertComponent(final AlertComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
