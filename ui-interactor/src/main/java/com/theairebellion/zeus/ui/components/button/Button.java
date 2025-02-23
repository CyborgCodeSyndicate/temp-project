package com.theairebellion.zeus.ui.components.button;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public interface Button {

    void click(SmartWebElement container, String buttonText);

    void click(SmartWebElement container);

    void click(String buttonText);

    void click(By buttonLocator);

    boolean isEnabled(SmartWebElement container, String buttonText);

    boolean isEnabled(SmartWebElement container);

    boolean isEnabled(String buttonText);

    boolean isEnabled(By buttonLocator);

    boolean isVisible(SmartWebElement container, String buttonText);

    boolean isVisible(SmartWebElement container);

    boolean isVisible(String buttonText);

    boolean isVisible(By buttonLocator);

    default void clickElementInCell(SmartWebElement cell) {

    }

}
