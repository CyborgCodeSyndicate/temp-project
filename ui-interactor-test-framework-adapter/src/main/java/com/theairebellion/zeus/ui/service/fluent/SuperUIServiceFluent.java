package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import lombok.experimental.Delegate;

public class SuperUIServiceFluent<T extends UIServiceFluent<?>> extends UIServiceFluent<T> {

    @Delegate
    private final UIServiceFluent<T> original;

    public SuperUIServiceFluent(UIServiceFluent<T> uiServiceFluent) {
        super(uiServiceFluent.getDriver());
        this.original = uiServiceFluent;
    }

    public SmartWebDriver getDriver() {
        return original.getDriver();
    }



}
