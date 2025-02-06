package com.theairebellion.zeus.ui.components.toggle;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public interface Toggle {

    void activate(SmartWebElement container, String toggleText);

    void activate(String toggleText);

    void activate(By toggleLocator);

    void deactivate(SmartWebElement container, String toggleText);

    void deactivate(String toggleText);

    void deactivate(By toggleLocator);

    boolean isEnabled(SmartWebElement container, String toggleText);

    boolean isEnabled(String toggleText);

    boolean isEnabled(By toggleLocator);

    boolean isActivated(SmartWebElement container, String toggleText);

    boolean isActivated(String toggleText);

    boolean isActivated(By toggleLocator);
}
