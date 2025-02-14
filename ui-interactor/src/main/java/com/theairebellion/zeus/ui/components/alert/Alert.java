package com.theairebellion.zeus.ui.components.alert;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public interface Alert {

    String getValue(SmartWebElement container);

    String getValue(By containerLocator);

    boolean isVisible(SmartWebElement container);

    boolean isVisible(By containerLocator);
}
