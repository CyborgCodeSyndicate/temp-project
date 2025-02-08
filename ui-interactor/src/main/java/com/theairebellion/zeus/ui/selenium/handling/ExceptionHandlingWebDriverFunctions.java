package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ExceptionHandlingWebDriverFunctions {

    public static Object handleNoSuchElement(WebDriver driver, WebElementAction webElementAction, Object... args) {
        if (args.length == 0 || !(args[0] instanceof By)) {
            LogUI.error("Invalid or missing locator argument for FIND_ELEMENT.");
            throw new IllegalArgumentException("FIND_ELEMENT action requires a By locator.");
        }

        WebElement foundElement = findElementInIFrames(driver, (By) args[0]);
        if (foundElement != null) {
            return webElementAction.performActionWebDriver(driver, args);
        }

        LogUI.error("Element not found in the main DOM or any iframe.");
        throw new NoSuchElementException("Element not found in any iframe.");
    }

    private static SmartWebElement findElementInIFrames(WebDriver driver, By by) {
        driver.switchTo().defaultContent();
        return searchElementInIFrames(driver, by);
    }

    private static SmartWebElement searchElementInIFrames(WebDriver driver, By by) {
        try {
            WebElement element = new SmartWebDriver(driver).findElement(by);
            return new SmartWebElement(element, driver);
        } catch (NoSuchElementException e) {
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        List<WebElement> frames = (List<WebElement>) js.executeScript(
                "return Array.from(document.getElementsByTagName('iframe'));"
        );

        for (WebElement frame : frames) {
            driver.switchTo().frame(frame);
            try {
                WebElement element = driver.findElement(by);
                return new SmartWebElement(element, driver);
            } catch (Exception e) {
                driver.switchTo().defaultContent();
            }
        }

        return null;
    }
}
