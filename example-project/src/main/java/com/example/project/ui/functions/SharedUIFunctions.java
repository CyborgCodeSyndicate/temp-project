package com.example.project.ui.functions;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SharedUIFunctions {

    public static void waitForTimeout(SmartWebDriver smartWebDriver) {
        try {
            //smartWebDriver.getWait().wait(1000);
            Thread.sleep(1000);
            System.out.println("waitForTimeout");
        } catch (Exception e) {

        }
    }

    public static void waitForLoading(SmartWebDriver smartWebDriver) {
        smartWebDriver.getWait().until(
                ExpectedConditions.invisibilityOfElementLocated(By.className("loader"))
        );
    }

    public static void waitForPresence(SmartWebDriver smartWebDriver, By locator) {
        smartWebDriver.getWait().until(visibilityOfElementLocatedCustom(locator));
    }

    public static void waitToBeClickable(SmartWebDriver smartWebDriver, By locator) {
        smartWebDriver.getWait().until(elementToBeClickableCustom(locator));
    }

    public static void waitToBeRemoved(SmartWebDriver smartWebDriver, By locator) {
        System.out.println("waitToBeRemoved: " + locator.toString());
        smartWebDriver.getWait().until(invisibilityOfElementLocatedCustom(locator));
    }

    //todo: extract this to a service
    private static ExpectedCondition<Boolean> visibilityOfElementLocatedCustom(final By locator) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    SmartWebDriver smartWebDriver = new SmartWebDriver(driver);
                    return smartWebDriver.findSmartElement(locator).isDisplayed();
                } catch (NullPointerException | NoSuchElementException | StaleElementReferenceException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "element to no longer be visible: " + locator;
            }
        };
    }

    private static ExpectedCondition<Boolean> visibilityOfElementsLocatedCustom(final By locator) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    SmartWebDriver smartWebDriver = new SmartWebDriver(driver);
                    return smartWebDriver.findSmartElement(locator).isDisplayed();
                } catch (NullPointerException | NoSuchElementException | StaleElementReferenceException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "element to no longer be visible: " + locator;
            }
        };
    }

    public static ExpectedCondition<Boolean> invisibilityOfElementLocatedCustom(final By locator) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    SmartWebDriver smartWebDriver = new SmartWebDriver(driver);
                    return !(smartWebDriver.findSmartElement(locator).isDisplayed());
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    // Returns true because the element is not present in DOM. The
                    // try block checks if the element is present but is invisible.
                    return true;
                }
            }

            @Override
            public String toString() {
                return "element to no longer be visible: " + locator;
            }
        };
    }

    private static ExpectedCondition<Boolean> elementToBeClickableCustom(final By locator) {
        return new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                SmartWebDriver smartWebDriver = new SmartWebDriver(driver);
                try {
                    SmartWebElement element = smartWebDriver.findSmartElement(locator);
                    return element.isDisplayed() && element.isEnabled();
                } catch (NullPointerException | StaleElementReferenceException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "element to no longer be visible: " + locator;
            }
        };
    }
}
