package com.theairebellion.zeus.ui.components.base;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

public class BaseComponent {

    protected SmartWebDriver driver;


    public BaseComponent(final SmartWebDriver smartWebDriver) {
        this.driver = smartWebDriver;
    }


}
