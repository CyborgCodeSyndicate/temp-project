package com.theairebellion.zeus.ui.components.alert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface Alert {

    String getValue(WebElement container);

    String getValue(By containerLocator);

    boolean isVisible(WebElement container);

    boolean isVisible(By containerLocator);
}
