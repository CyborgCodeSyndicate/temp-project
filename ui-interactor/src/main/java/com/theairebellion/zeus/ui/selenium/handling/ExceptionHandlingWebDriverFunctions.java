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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExceptionHandlingWebDriverFunctions {

    public static Object handleNoSuchElement(WebDriver driver, WebElementAction webElementAction, Object... args) {
        if (args.length == 0 || !(args[0] instanceof By)) {
            LogUI.error("Invalid or missing locator argument for FIND_ELEMENT.");
            throw new IllegalArgumentException("FIND_ELEMENT action requires a By locator.");
        }

        WebElement foundElement = tryToFindElementInIFrame(driver, (By) args[0]);
        if (foundElement != null) {
            return webElementAction.performActionWebDriver(driver, args);
        }

        LogUI.error("Element not found in the main DOM or any iframe.");
        throw new NoSuchElementException("Element not found in any iframe.");
    }

//    private static SmartWebElement tryToFindElementInIFrame(WebDriver driver, By by) {
//        driver.switchTo().defaultContent();
//        return findElementRecursively(driver, by);
//
//    }

//    private static SmartWebElement findElementRecursively(WebDriver driver, By by) {
//        try {
//            WebElement element = new SmartWebDriver(driver).findElement(by);
//            return new SmartWebElement(element, driver);
//        } catch (NoSuchElementException e) {
//        }
//
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        List<WebElement> frames = (List<WebElement>) js.executeScript(
//                "return Array.from(document.getElementsByTagName('iframe'));"
//        );
//
//        for (WebElement frame : frames) {
//            driver.switchTo().frame(frame);
//
//            SmartWebElement elementInChild = findElementRecursively(driver, by);
//            if (elementInChild != null) {
//                return new SmartWebElement(elementInChild, driver);
//            }
//
//            driver.switchTo().parentFrame();
//        }
//
//        return null;
//    }

    private static SmartWebElement tryToFindElementInIFrame(WebDriver driver, By by) {
        driver.switchTo().defaultContent();
        return findElementRecursively(driver, by, new HashSet<>());
    }

    private static SmartWebElement findElementRecursively(WebDriver driver, By by, Set<WebElement> visitedFrames) {
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
            if (visitedFrames.contains(frame)) {
                continue;
            }

            visitedFrames.add(frame);
            driver.switchTo().frame(frame);
            SmartWebElement elementInChild = findElementRecursively(driver, by, visitedFrames);
            if (elementInChild != null) {
                return elementInChild;
            }

            driver.switchTo().parentFrame();
        }

        return null;
    }
}
