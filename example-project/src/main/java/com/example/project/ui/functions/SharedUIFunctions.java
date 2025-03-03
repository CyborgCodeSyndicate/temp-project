package com.example.project.ui.functions;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SharedUIFunctions {

    public static void waitForLoading(SmartWebDriver smartWebDriver) {
        smartWebDriver.getWait().until(
                ExpectedConditions.invisibilityOfElementLocated(By.className("loader"))
        );
    }

    public static void waitForPresence(SmartWebDriver smartWebDriver, By locator) {
        smartWebDriver.getWait().until(visibilityOfElementLocatedCustom(locator));
    }

    //todo: extract this to a service
    public static ExpectedCondition<Boolean> visibilityOfElementLocatedCustom(final By locator) {
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
}
