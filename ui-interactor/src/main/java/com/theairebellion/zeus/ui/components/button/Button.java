package com.theairebellion.zeus.ui.components.button;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface Button {

    void click(WebElement container, String buttonText);

    void click(WebElement container);

    void click(String buttonText);

    void click(By buttonLocator);

    boolean isEnabled(WebElement container, String buttonText);

    boolean isEnabled(WebElement container);

    boolean isEnabled(String buttonText);

    boolean isEnabled(By buttonLocator);

    boolean isPresent(WebElement container, String buttonText);

    boolean isPresent(WebElement container);

    boolean isPresent(String buttonText);

    boolean isPresent(By buttonLocator);
}
