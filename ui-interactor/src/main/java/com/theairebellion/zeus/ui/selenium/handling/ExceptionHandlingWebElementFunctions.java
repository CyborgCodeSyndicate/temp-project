package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.helper.FrameHelper;
import com.theairebellion.zeus.ui.selenium.helper.LocatorParser;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ExceptionHandlingWebElementFunctions {

    public static Object handleStaleElement(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Object... args) {
        element = LocatorParser.updateWebElement(driver, element);
        return WebElementAction.performAction(driver, element.getOriginal(), webElementAction, args);
    }

    public static Object handleNoSuchElement(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Object... args) {
        if (args.length == 0 || !(args[0] instanceof By)) {
            LogUI.error("Invalid or missing locator argument for FIND_ELEMENT.");
            throw new IllegalArgumentException("FIND_ELEMENT action requires a By locator.");
        }

        WebElement foundElement = FrameHelper.findElementInIFrames(driver, element.getOriginal());
        if (foundElement != null) {
            return webElementAction.performActionWebElement(driver, foundElement, args);
        }

        LogUI.error("Element not found in the main DOM or any iframe.");
        throw new NoSuchElementException("Element not found in any iframe.");
    }

    public static Object handleElementClickIntercepted(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Exception exception, Object... args) {
        if (exception != null && exception.getMessage() != null) {
            String locatorString = LocatorParser.extractBlockingElementLocator(exception.getMessage());
            if (locatorString != null) {
                By blocker = By.xpath(locatorString);
                try {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(blocker));
                } catch (TimeoutException e) {
                    LogUI.warn("Blocking element did not disappear after waiting, attempting action anyway.");
                } catch (Exception e) {
                    LogUI.warn("Exception occurred while waiting for blocking element: " + e.getMessage());
                }
            }
        }

        return WebElementAction.performAction(driver, element.getOriginal(), webElementAction, args);
    }

    public static Object handleElementNotInteractable(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Object... args) {
        element = LocatorParser.updateWebElement(driver, element);

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            Actions actions = new Actions(driver);
            actions.moveToElement(element.getOriginal()).perform(); // Ensure action is performed
            WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(element.getOriginal()));
            return WebElementAction.performAction(driver, clickableElement, webElementAction, args);
        } catch (TimeoutException e) {
            LogUI.warn("Element not clickable after waiting, attempting action anyway.");
            return WebElementAction.performAction(driver, element.getOriginal(), webElementAction, args);
        } catch (Exception e) {
            LogUI.warn("Exception occurred while trying to make element interactable: " + e.getMessage());
            return WebElementAction.performAction(driver, element.getOriginal(), webElementAction, args);
        }
    }
}