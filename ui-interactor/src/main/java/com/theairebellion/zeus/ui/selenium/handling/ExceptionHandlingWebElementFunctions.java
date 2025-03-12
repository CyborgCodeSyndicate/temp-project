package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.helper.FrameHelper;
import com.theairebellion.zeus.ui.selenium.helper.LocatorParser;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
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
        try {
            element = LocatorParser.updateWebElement(driver, element);
            return WebElementAction.performAction(driver, element.getOriginal(), webElementAction, args);
        } catch (Exception e) {
            String errorMessage = String.format(
                    "[BROKEN] WebElement action '%s' failed for stale element exception",
                    webElementAction.getMethodName());
            Allure.step(errorMessage, Status.BROKEN);
            LogUI.error(errorMessage);
            throw e;
        }
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

        String errorMessage = String.format(
                "[BROKEN] WebElement action '%s' could not be executed - Element with locator '%s' not found.",
                webElementAction.getMethodName(), args[0]
        );

        Allure.step(errorMessage, Status.BROKEN);
        LogUI.error(errorMessage);
        throw new NoSuchElementException("Element not found in any iframe.");
    }

    public static Object handleElementClickIntercepted(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Exception exception, Object... args) {
        try {
            By blocker = By.xpath(LocatorParser.extractBlockingElementLocator(exception.getMessage()));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(blocker));
            } catch (TimeoutException e) {
                LogUI.warn("Blocking element did not disappear after waiting, attempting action anyway.");
            }

            return WebElementAction.performAction(driver, element.getOriginal(), webElementAction, args);
        } catch (Exception e) {
            String errorMessage = String.format(
                    "[BROKEN] WebElement action '%s' failed due to intercepted click with locator '%s'. Exception: '%s'",
                    webElementAction.getMethodName(), LocatorParser.extractBlockingElementLocator(exception.getMessage()), e.getClass().getSimpleName()
            );
            Allure.step(errorMessage, Status.BROKEN);
            LogUI.error(errorMessage);
            throw e;
        }
    }

    public static Object handleElementNotInteractable(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Object... args) {
        try {
            element = LocatorParser.updateWebElement(driver, element);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            Actions actions = new Actions(driver);
            actions.moveToElement(element.getOriginal());
            WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(element.getOriginal()));

            return WebElementAction.performAction(driver, clickableElement, webElementAction, args);
        } catch (Exception e) {
            String errorMessage = String.format(
                    "[BROKEN] WebElement action '%s' failed because element was not interactable. Exception: '%s'",
                    webElementAction.getMethodName(), e.getClass().getSimpleName()
            );
            Allure.step(errorMessage, Status.BROKEN);
            LogUI.error(errorMessage);
            throw e;
        }
    }
}
