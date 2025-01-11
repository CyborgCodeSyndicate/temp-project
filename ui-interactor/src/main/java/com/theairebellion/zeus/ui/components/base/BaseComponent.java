package com.theairebellion.zeus.ui.components.base;

import com.theairebellion.zeus.ui.selenium.SmartSelenium;

public class BaseComponent {

    protected SmartSelenium smartSelenium;


    public BaseComponent(final SmartSelenium smartSelenium) {
        this.smartSelenium = smartSelenium;
    }


}
