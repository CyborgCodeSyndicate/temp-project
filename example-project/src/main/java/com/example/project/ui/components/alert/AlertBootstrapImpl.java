package com.example.project.ui.components.alert;

import com.example.project.ui.types.AlertFieldTypes;
import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.alert.Alert;
import com.theairebellion.zeus.ui.components.base.BaseComponent;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


@ImplementationOfType(AlertFieldTypes.BOOTSTRAP_ALERT)
public class AlertBootstrapImpl extends BaseComponent implements Alert {

    private static final By ALERT_CONTAINER_LOCATOR = By.className("alert");


    public AlertBootstrapImpl(SmartSelenium smartSelenium) {
        super(smartSelenium);
    }


    @Override
    public String getValue(WebElement container) {
        return smartSelenium.waitAndFindElement(container, ALERT_CONTAINER_LOCATOR).getText();
    }

    @Override
    public String getValue(By containerLocator) {
        return smartSelenium.waitAndFindElement(containerLocator).getText();
    }

    @Override
    public boolean isVisible(WebElement container) {
        return smartSelenium.checkNoException(() -> smartSelenium.waitAndFindElement(container, ALERT_CONTAINER_LOCATOR));
    }

    @Override
    public boolean isVisible(By containerLocator) {
        return smartSelenium.checkNoException(() -> smartSelenium.waitAndFindElement(containerLocator));
    }
}