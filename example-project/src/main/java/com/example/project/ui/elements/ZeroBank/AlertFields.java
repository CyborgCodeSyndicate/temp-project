package com.example.project.ui.elements.ZeroBank;

import com.example.project.ui.types.AlertFieldTypes;
import com.theairebellion.zeus.ui.components.alert.AlertComponentType;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.selenium.AlertUIElement;
import org.openqa.selenium.By;

public enum AlertFields implements AlertUIElement {

    SUBMITTED_TRANSACTION(By.className("alert"), AlertFieldTypes.BOOTSTRAP_ALERT_TYPE),
    FOREIGN_CURRENCY_CASH(By.id("alert_content"), AlertFieldTypes.BOOTSTRAP_ALERT_TYPE),
    PAYMENT_MESSAGE(By.id("alert_content"), AlertFieldTypes.BOOTSTRAP_ALERT_TYPE)
    ;

    private final By locator;
    private final AlertComponentType componentType;


    AlertFields(final By locator, final AlertComponentType componentType) {
        this.locator = locator;
        this.componentType = componentType;
    }


    @Override
    public By locator() {
        return locator;
    }


    @Override
    public <T extends ComponentType> T componentType() {
        return (T) componentType;
    }


    @Override
    public Enum<?> enumImpl() {
        return this;
    }

}
