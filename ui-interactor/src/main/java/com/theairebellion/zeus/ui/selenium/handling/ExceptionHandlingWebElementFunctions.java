package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.helper.FrameHelper;
import com.theairebellion.zeus.ui.selenium.helper.LocatorParser;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Provides utility functions for handling WebElement exceptions.
 * <p>
 * This class defines methods to recover from common Selenium exceptions
 * such as {@link NoSuchElementException}, {@link TimeoutException}, and
 * {@link org.openqa.selenium.StaleElementReferenceException}. It aims to
 * improve test stability by implementing fallback strategies for locating and
 * interacting with elements.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class ExceptionHandlingWebElementFunctions {

    /**
     * Handles a stale element reference by attempting to re-locate the element.
     *
     * @param driver           The WebDriver instance.
     * @param element          The stale {@link SmartWebElement}.
     * @param webElementAction The action to be performed on the element.
     * @param args             Additional arguments required for the action.
     * @return The result of the attempted action.
     */
    public static Object handleStaleElement(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Object... args) {
        element = LocatorParser.updateWebElement(driver, element);
        return WebElementAction.performAction(driver, element.getOriginal(), webElementAction, args);
    }

    /**
     * Handles the case where an element is not found by searching within iframes.
     *
     * @param driver           The WebDriver instance.
     * @param element          The {@link SmartWebElement} that was not found.
     * @param webElementAction The action to be performed if the element is found.
     * @param args             Additional arguments required for the action.
     * @return The result of the attempted action if the element is found.
     * @throws NoSuchElementException If the element cannot be found in any iframe.
     */
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

    /**
     * Handles the case where an element is intercepted by another element when clicked.
     *
     * @param driver           The WebDriver instance.
     * @param element          The {@link SmartWebElement} that was intercepted.
     * @param webElementAction The action to be retried after waiting for the blocker to disappear.
     * @param exception        The exception that occurred during the click action.
     * @param args             Additional arguments required for the action.
     * @return The result of the attempted action.
     */
    public static Object handleElementClickIntercepted(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Exception exception, Object... args) {
        By blocker = By.xpath(LocatorParser.extractBlockingElementLocator(exception.getMessage()));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(blocker));
        } catch (TimeoutException e) {
            LogUI.warn("Blocking element did not disappear after waiting, attempting action anyway.");
        }

        return WebElementAction.performAction(driver, element.getOriginal(), webElementAction, args);
    }

    /**
     * Handles the case where an element is not interactable by scrolling to it and retrying the action.
     *
     * @param driver           The WebDriver instance.
     * @param element          The {@link SmartWebElement} that was not interactable.
     * @param webElementAction The action to be retried.
     * @param args             Additional arguments required for the action.
     * @return The result of the attempted action.
     */
    public static Object handleElementNotInteractable(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Object... args) {
        element = LocatorParser.updateWebElement(driver, element);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        Actions actions = new Actions(driver);
        actions.moveToElement(element.getOriginal()).perform();
        WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(element.getOriginal()));

        return WebElementAction.performAction(driver, clickableElement, webElementAction, args);
    }

}
