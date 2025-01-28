package com.theairebellion.zeus.ui.components.toggle;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface Toggle {

    void activate(WebElement container, String toggleText);

    void activate(String toggleText);

    void activate(By toggleLocator);

    void deactivate(WebElement container, String toggleText);

    void deactivate(String toggleText);

    void deactivate(By toggleLocator);

    boolean isEnabled(WebElement container, String toggleText);

    boolean isEnabled(String toggleText);

    boolean isEnabled(By toggleLocator);

    boolean isActivated(WebElement container, String toggleText);

    boolean isActivated(String toggleText);

    boolean isActivated(By toggleLocator);
}
