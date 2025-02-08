package com.theairebellion.zeus.ui.selenium.helper;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.util.List;

public class FrameHelper {

    public static SmartWebElement findElementInIFrames(WebDriver driver, By by) {
        driver.switchTo().defaultContent();
        return searchElementInIFrames(driver, by);
    }

    private static SmartWebElement searchElementInIFrames(WebDriver driver, By by) {
        try {
            WebElement element = new SmartWebDriver(driver).findElement(by);
            return new SmartWebElement(element, driver);
        } catch (NoSuchElementException e) {
        }

        List<WebElement> frames = getAllIFrames(driver);
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

    private static List<WebElement> getAllIFrames(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (List<WebElement>) js.executeScript(
                "return Array.from(document.getElementsByTagName('iframe'));"
        );
    }
}