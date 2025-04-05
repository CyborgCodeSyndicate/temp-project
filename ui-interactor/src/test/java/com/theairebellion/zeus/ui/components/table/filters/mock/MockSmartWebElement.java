package com.theairebellion.zeus.ui.components.table.filters.mock;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MockSmartWebElement extends SmartWebElement {

    public MockSmartWebElement(WebElement original, WebDriver driver) {
        super(original, driver);
    }

    @Override
    public String getText() {
        return "dummy";
    }

    @Override
    public String getAttribute(String name) {
        return "dummy";
    }

    @Override
    public void click() {
    }

    @Override
    public void submit() {
    }

    @Override
    public void clear() {
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
    }

    @Override
    public boolean isDisplayed() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isSelected() {
        return true;
    }
}
