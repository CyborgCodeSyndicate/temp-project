package com.theairebellion.zeus.ui.components.modal;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public interface Modal {

    boolean isOpened();

    void clickButton(SmartWebElement container, String buttonText);

    void clickButton(String buttonText);

    void clickButton(By buttonLocator);

    String getTitle();

    String getBodyText();

    String getContentTitle();

    void close();
}
