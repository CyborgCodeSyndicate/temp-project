package com.theairebellion.zeus.ui.selenium.helper;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FrameHelper {

    public static SmartWebElement findElementInIFrames(WebDriver driver, By by) {
        driver.switchTo().defaultContent();
        return searchElementInIFrames(driver, by);
    }

    public static SmartWebElement findElementInIFrames(WebDriver driver, WebElement element) {
        driver.switchTo().defaultContent();
        return searchElementInIFramesByElement(driver, element);
    }

    private static SmartWebElement searchElementInIFrames(WebDriver driver, By by) {
        try {
            WebElement foundElement = new SmartWebDriver(driver).findElement(by);
            return new SmartWebElement(foundElement, driver);
        } catch (NoSuchElementException e) {
        }

        List<WebElement> frames = getAllIFrames(driver);
        for (WebElement frame : frames) {
            driver.switchTo().frame(frame);
            try {
                WebElement foundElement = driver.findElement(by);
                return new SmartWebElement(foundElement, driver);
            } catch (Exception e) {
                driver.switchTo().defaultContent();
            }
        }

        return null;
    }

    private static SmartWebElement searchElementInIFramesByElement(WebDriver driver, WebElement element) {
        List<WebElement> frames = getAllIFrames(driver);
        for (WebElement frame : frames) {
            driver.switchTo().frame(frame);
            try {
                WebElement foundElement = locateElementByAttributes(driver, element);
                if (foundElement != null) {
                    return new SmartWebElement(foundElement, driver);
                }
            } catch (Exception e) {
                driver.switchTo().defaultContent();
            }
        }

        return null;
    }

    private static WebElement locateElementByAttributes(WebDriver driver, WebElement originalElement) {
        String tagName = originalElement.getTagName();
        String attributes = getUniqueAttributes(driver, originalElement);

        String xpathExpression = "//" + tagName + attributes;
        List<WebElement> matchingElements = driver.findElements(By.xpath(xpathExpression));

        for (WebElement el : matchingElements) {
            if (el.getText().equals(originalElement.getText())) {
                return el;
            }
        }
        return null;
    }

    private static String getUniqueAttributes(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script =
                "var attrs = arguments[0].attributes, result = ''; " +
                        "for (var i = 0; i < attrs.length; i++) { " +
                        "    if (attrs[i].name !== 'style') { " +
                        "        result += '[@' + attrs[i].name + '=\"' + attrs[i].value + '\"]'; " +
                        "    } " +
                        "} return result;";

        return (String) js.executeScript(script, element);
    }

    private static List<WebElement> getAllIFrames(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (List<WebElement>) js.executeScript(
                "return Array.from(document.getElementsByTagName('iframe'));"
        );
    }
}