package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public enum WebElementAction {
    FIND_ELEMENT,
    CLICK,
    SEND_KEYS,
    SUBMIT;

    public static Object performAction(WebDriver driver, WebElement element, WebElementAction action, Object... args) {
        switch (action) {
            case FIND_ELEMENT:
                return new SmartWebElement(element.findElement((By) args[0]), driver);
            case CLICK:
                element.click();
                break;
            case SEND_KEYS:
                element.sendKeys((String) args[0]);
                break;
            case SUBMIT:
                element.submit();
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation.");
        }
        return null;
    }
}
