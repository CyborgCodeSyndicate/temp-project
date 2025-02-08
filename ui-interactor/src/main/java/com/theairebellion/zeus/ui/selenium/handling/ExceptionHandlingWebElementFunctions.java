package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.helper.LocatorParser;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
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

    public static Object handleElementClickIntercepted(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Exception exception, Object... args) {
        By blocker = By.xpath(LocatorParser.extractBlockingElementLocator(exception.getMessage()));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(blocker));
        } catch (TimeoutException e) {
            LogUI.warn("Blocking element did not disappear after waiting, attempting action anyway.");
        }

        return WebElementAction.performAction(driver, element.getOriginal(), webElementAction, args);
    }

    public static Object handleElementNotInteractable(WebDriver driver, SmartWebElement element, WebElementAction webElementAction, Object... args) {
        element = LocatorParser.updateWebElement(driver, element);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);
        actions.moveToElement(element.getOriginal());
        WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(element.getOriginal()));

        return WebElementAction.performAction(driver, clickableElement, webElementAction, args);
    }
}
