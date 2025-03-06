package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.components.base.AbstractComponentService;
import com.theairebellion.zeus.ui.components.factory.ComponentFactory;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class AlertServiceImpl extends AbstractComponentService<AlertComponentType, Alert> implements AlertService {

    public AlertServiceImpl(SmartWebDriver driver) {
        super(driver);
    }

    @Override
    protected Alert createComponent(final AlertComponentType componentType) {
        return ComponentFactory.getAlertComponent(componentType, driver);
    }

    @Step("[UI - Alert] Getting value from alert of type {componentType}")
    @Override
    public String getValue(AlertComponentType componentType, SmartWebElement container) {
        LogUI.step("Getting value from alert: " + componentType);
        return alertComponent(componentType).getValue(container);
    }

    @Step("[UI - Alert] Getting value from alert of type {componentType} located by {containerLocator}")
    @Override
    public String getValue(AlertComponentType componentType, By containerLocator) {
        LogUI.step("Getting value from alert located by: " + containerLocator);
        return alertComponent(componentType).getValue(containerLocator);
    }

    @Step("[UI - Alert] Checking visibility of alert of type {componentType}")
    @Override
    public boolean isVisible(AlertComponentType componentType, SmartWebElement container) {
        LogUI.step("Checking if alert is visible: " + componentType);
        return alertComponent(componentType).isVisible(container);
    }

    @Step("[UI - Alert] Checking visibility of alert of type {componentType} located by {containerLocator}")
    @Override
    public boolean isVisible(AlertComponentType componentType, By containerLocator) {
        LogUI.step("Checking if alert is visible at: " + containerLocator);
        return alertComponent(componentType).isVisible(containerLocator);
    }

    private Alert alertComponent(final AlertComponentType componentType) {
        return getOrCreateComponent(componentType);
    }
}
