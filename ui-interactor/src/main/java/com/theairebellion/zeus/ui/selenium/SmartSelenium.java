package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.config.UIConfig;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.util.FourConsumer;
import com.theairebellion.zeus.ui.util.TriConsumer;
import com.theairebellion.zeus.ui.util.TriFunction;
import lombok.Getter;
import org.aeonbits.owner.ConfigCache;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;


@Getter
public class SmartSelenium {

    protected static final UIConfig uiConfig = ConfigCache.getOrCreate(UIConfig.class);

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;


    public SmartSelenium(WebDriver driver) {
        LogUI.info("UI Config => waitDuration: {}, browserType: {}, headless: {}, inputDefaultType: {}",
                uiConfig.waitDuration(), uiConfig.browserType(),
                uiConfig.headless(), uiConfig.inputDefaultType());
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(uiConfig.waitDuration()));
        actions = new Actions(driver);
    }


    public void smartSendKeys(WebElement element, String value) {
        LogUI.extended("Attempting to send keys: [{}] into element: [{}]", value, element);
        try {
            element.sendKeys(value);
            LogUI.debug("Successfully sent keys to element. Current input value is: [{}]",
                    element.getAttribute("value"));
        } catch (Exception e) {
            LogUI.warn("Exception occurred while sending keys to element: [{}]. Exception: {}",
                    element, e.getMessage());
            TriConsumer<WebDriver, WebElement, String> exceptionHandling =
                    ExceptionHandling.exceptionSendKeysMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                LogUI.error("No fallback logic for exception: [{}]. Rethrowing.", e.getClass().getSimpleName());
                throw e;
            }
            LogUI.info("Using fallback logic for exception: [{}]. Retrying sendKeys(...) with re-located element.",
                    e.getClass().getSimpleName());
            exceptionHandling.accept(driver, element, value);
        }
    }


    public void smartClick(WebElement element) {
        LogUI.extended("Initiating a smartClick on element: [{}]", element);
        try {
            element.click();
            LogUI.debug("Click succeeded on element: [{}]", element);
        } catch (Exception e) {
            LogUI.warn("Click failed on element: [{}], exception: [{}]", element, e.getMessage());
            TriConsumer<WebDriver, WebElement, Consumer<WebElement>> exceptionHandling =
                    ExceptionHandling.exceptionElementActionMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                LogUI.error("No fallback logic for exception: [{}]. Rethrowing.", e.getClass().getSimpleName());
                throw e;
            }
            LogUI.info("Executing fallback logic for exception: [{}]. Attempting re-locate and re-click.",
                    e.getClass().getSimpleName());
            exceptionHandling.accept(driver, element, WebElement::click);
        }
    }


    public void smartClear(WebElement element) {
        LogUI.extended("Attempting to clear element: [{}]", element);
        try {
            element.clear();
            LogUI.debug("After clear(), element value is: [{}]", element.getAttribute("value"));
        } catch (Exception e) {
            TriConsumer<WebDriver, WebElement, Consumer<WebElement>> exceptionHandling =
                    ExceptionHandling.exceptionElementActionMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                throw e;
            }
            LogUI.warn("Exception occurred clearing element: [{}]. Attempting fallback logic...", element.toString());
            exceptionHandling.accept(driver, element, WebElement::clear);
        }
        if (!smartGetAttribute(element, "value").isEmpty()) {
            LogUI.debug("Value is not empty after clear(). Trying the Ctrl+A+Delete approach.");
            smartSendKeys(element, Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        }
        if (!smartGetAttribute(element, "value").isEmpty()) {
            smartClick(element);
            actions.keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND).sendKeys(Keys.DELETE).perform();
        }
        LogUI.debug("Final value after multiple attempts: [{}]", element.getAttribute("value"));
    }


    public void smartMove(WebElement element) {
        LogUI.extended("Attempting to move to element: [{}]", element);

        try {
            actions.moveToElement(element);
            actions.perform();

            LogUI.debug("Successfully moved to element: [{}]", element);
        } catch (Exception e) {
            LogUI.warn("Failed to move to element [{}], exception: [{}]", element, e.getMessage());

            FourConsumer<WebDriver, Actions, WebElement, Consumer<WebElement>> exceptionHandling =
                    ExceptionHandling.exceptionActionMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                LogUI.error("No fallback logic for exception: [{}]. Rethrowing.", e.getClass().getSimpleName());
                throw e;
            }

            LogUI.info("Executing fallback logic for exception: [{}]. Re-locating or re-trying move.",
                    e.getClass().getSimpleName());
            exceptionHandling.accept(driver, actions, element, actions::moveToElement);
        }
    }


    public String smartGetText(WebElement element) {
        LogUI.extended("Retrieving text from element: [{}]", element);

        try {
            String text = element.getText();
            LogUI.debug("Retrieved text: [{}] from element: [{}]", text, element);
            return text;
        } catch (Exception e) {
            LogUI.warn("Failed to get text from element [{}], exception: [{}]", element, e.getMessage());

            TriFunction<WebDriver, WebElement, Function<WebElement, String>, String> exceptionHandling =
                    ExceptionHandling.exceptionElementActionMapReturn.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                LogUI.error("No fallback logic for exception: [{}]. Rethrowing.", e.getClass().getSimpleName());
                throw e;
            }

            LogUI.info("Using fallback logic for exception: [{}]. Attempting re-locate or re-try getText().",
                    e.getClass().getSimpleName());
            return exceptionHandling.apply(driver, element, WebElement::getText);
        }
    }


    public WebElement smartFindElement(WebElement element, By by) {
        LogUI.extended("Finding element within parent element: [{}] using locator: [{}]", element, by);

        try {
            WebElement found = element.findElement(by);
            LogUI.debug("Successfully found child element with locator [{}] under parent [{}]", by, element);
            return found;
        } catch (Exception e) {
            LogUI.warn("Failed to find child element for locator [{}] under parent [{}]. Exception: {}",
                    by, element, e.getMessage());

            TriFunction<WebDriver, WebElement, By, WebElement> exceptionHandling =
                    ExceptionHandling.exceptionFindElementMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                LogUI.error("No fallback logic for exception: [{}]. Rethrowing.", e.getClass().getSimpleName());
                throw e;
            }

            LogUI.info("Using fallback logic for exception: [{}]. Attempting re-locate or re-try findElement().",
                    e.getClass().getSimpleName());
            return exceptionHandling.apply(driver, element, by);
        }
    }


    public WebElement smartFindElement(By by) {
        LogUI.extended("Finding element using locator: [{}]", by);

        try {
            WebElement found = driver.findElement(by);
            LogUI.debug("Element found with locator: [{}]", by);
            return found;
        } catch (Exception e) {
            LogUI.warn("Failed to find element by [{}], exception: {}", by, e.getMessage());
            throw e;
        }
    }


    public List<WebElement> smartFindElements(WebElement element, By by) {
        LogUI.extended("Finding elements within parent: [{}] using locator: [{}]", element, by);

        try {
            List<WebElement> list = element.findElements(by);
            LogUI.debug("Found [{}] child elements for locator [{}] under parent [{}]",
                    list.size(), by, element);
            return list;
        } catch (Exception e) {
            LogUI.warn("Failed to find elements for locator [{}] under parent [{}]. Exception: {}",
                    by, element, e.getMessage());

            TriFunction<WebDriver, WebElement, By, List<WebElement>> exceptionHandling =
                    ExceptionHandling.exceptionFindElementsMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                LogUI.error("No fallback logic for exception: [{}]. Rethrowing.", e.getClass().getSimpleName());
                throw e;
            }

            LogUI.info("Using fallback logic for exception: [{}]. Attempting re-locate or re-try findElements().",
                    e.getClass().getSimpleName());
            return exceptionHandling.apply(driver, element, by);
        }
    }


    public List<WebElement> smartFindElements(By by) {
        LogUI.extended("Finding elements using locator: [{}]", by);

        try {
            List<WebElement> list = driver.findElements(by);
            LogUI.debug("Found [{}] elements by locator: [{}]", list.size(), by);
            return list;
        } catch (Exception e) {
            LogUI.warn("Failed to find elements by [{}], exception: {}", by, e.getMessage());
            throw e;
        }
    }


    public String smartGetAttribute(WebElement element, String attribute) {
        LogUI.extended("Retrieving attribute [{}] from element: [{}]", attribute, element);

        try {
            String attrValue = element.getAttribute(attribute);
            LogUI.debug("Attribute [{}] value is: [{}]", attribute, attrValue);
            return attrValue;
        } catch (Exception e) {
            LogUI.warn("Failed to get attribute [{}] from element [{}]. Exception: {}", attribute, element, e.getMessage());

            TriFunction<WebDriver, WebElement, String, String> exceptionHandling =
                    ExceptionHandling.exceptionGetAttributeMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                LogUI.error("No fallback logic for exception: [{}]. Rethrowing.", e.getClass().getSimpleName());
                throw e;
            }

            LogUI.info("Using fallback logic for exception: [{}]. Attempting re-locate or re-try getAttribute().",
                    e.getClass().getSimpleName());
            return exceptionHandling.apply(driver, element, attribute);
        }
    }


    public WebElement waitAndFindElement(WebElement element, By by) {
        LogUI.debug("Waiting for presence of nested element located by [{}] under [{}].", by, element);

        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(element, by));
        WebElement found = smartFindElement(element, by);

        LogUI.info("Successfully found nested element with locator [{}] under [{}].", by, element);
        return found;
    }


    public WebElement waitAndFindElement(By by) {
        LogUI.debug("Waiting for presence of element located by: [{}]", by);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        WebElement element = smartFindElement(by);
        LogUI.info("Found element [{}] after waiting for presence.", element.toString());
        return element;
    }


    public List<WebElement> waitAndFindElements(WebElement element, By by) {
        LogUI.debug("Waiting for nested element(s) located by [{}] under element [{}].", by, element);
        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(element, by));
        List<WebElement> results = smartFindElements(element, by);
        LogUI.info("Found [{}] element(s) after waiting. By: [{}], under: [{}]",
                results.size(), by, element);
        return results;
    }


    public List<WebElement> waitAndFindElements(By by) {
        LogUI.debug("Waiting for elements located by [{}].", by);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        List<WebElement> results = smartFindElements(by);
        LogUI.info("Found [{}] element(s) after waiting. By: [{}]",
                results.size(), by);
        return results;
    }


    public void waitAndClickElement(WebElement element) {
        LogUI.debug("Waiting for element [{}] to be clickable.", element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        LogUI.debug("Element [{}] is clickable. Now moving & clicking.", element);
        smartMove(element);
        smartClick(element);
        LogUI.info("Click action completed on element [{}].", element);
    }


    public void waitAndClickElement(By by) {
        LogUI.debug("Waiting for element located by [{}] to be clickable.", by);
        wait.until(ExpectedConditions.elementToBeClickable(by));

        LogUI.debug("Element by [{}] is clickable. Finding element & clicking.", by);
        WebElement element = smartFindElement(by);
        smartMove(element);
        smartClick(element);
        LogUI.info("Click action completed on element located by [{}].", by);
    }


    public void waitAndSendKeys(WebElement element, String value) {
        LogUI.debug("Waiting for element [{}] to be visible & clickable before sending keys.", element);
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));

        LogUI.debug("Sending keys: '{}' to element [{}].", value, element);
        smartSendKeys(element, value);
        LogUI.info("Sent keys '{}' to element [{}].", value, element);
    }


    public void waitAndSendKeys(By by, String value) {
        LogUI.debug("Waiting for element by [{}] to be present & clickable before sending keys.", by);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        wait.until(ExpectedConditions.elementToBeClickable(by));

        WebElement element = smartFindElement(by);
        LogUI.debug("Sending keys: '{}' to element by [{}].", value, by);
        smartSendKeys(element, value);
        LogUI.info("Sent keys '{}' to element by [{}].", value, by);
    }


    public void clearAndSendKeys(WebElement element, String value) {
        LogUI.debug("Waiting for element [{}] to be visible before clearing & sending keys.", element);
        wait.until(ExpectedConditions.visibilityOf(element));

        LogUI.debug("Moving, clearing, and then sending keys '{}' to element [{}].", value, element);
        smartMove(element);
        smartClear(element);
        smartSendKeys(element, value);
        LogUI.info("Cleared & sent keys '{}' to element [{}].", value, element);
    }


    public boolean checkIfElementIsPresent(WebElement element, By by) {
        LogUI.debug("Checking if element(s) by [{}] is present under [{}].", by, element);
        try {
            smartFindElement(element, by);
            LogUI.debug("Element(s) by [{}] found under [{}].", by, element);
            return true;
        } catch (Exception e) {
            LogUI.debug("Element(s) by [{}] NOT found under [{}]. Exception: {}", by, element, e.getMessage());
            return false;
        }
    }


    public boolean checkIfElementIsPresent(By by) {
        LogUI.debug("Checking if element(s) located by [{}] is present.", by);
        try {
            smartFindElement(by);
            LogUI.debug("Element(s) by [{}] found.", by);
            return true;
        } catch (Exception e) {
            LogUI.debug("Element(s) by [{}] not found. Exception: {}", by, e.getMessage());
            return false;
        }
    }


    public void waitUntilElementIsPresent(By by) {
        LogUI.debug("Waiting until element(s) by [{}] is present.", by);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        LogUI.info("Element(s) by [{}] is present now.", by);
    }


    public void waitUntilElementIsShown(WebElement element, int seconds) {
        LogUI.debug("Waiting up to [{}s] for element [{}] to be visible.", seconds, element);
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            customWait.until(ExpectedConditions.visibilityOf(element));
            LogUI.info("Element [{}] became visible within [{}] seconds.", element, seconds);
        } catch (Exception e) {
            LogUI.error("Element [{}] wasn't displayed after [{}] seconds. Exception: {}", element, seconds, e.getMessage());
        }
    }


    public void waitUntilElementIsShown(By by, int seconds) {
        LogUI.debug("Waiting up to [{}s] for element by [{}] to be present.", seconds, by);
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            customWait.until(ExpectedConditions.presenceOfElementLocated(by));
            LogUI.info("Element by [{}] is present within [{}] seconds.", by, seconds);
        } catch (Exception e) {
            LogUI.error("Element by [{}] wasn't displayed after [{}] seconds. Exception: {}", by, seconds, e.getMessage());
        }
    }


    public void waitUntilElementIsRemoved(WebElement element, int seconds) {
        LogUI.debug("Waiting up to [{}s] for element [{}] to be removed (invisible).", seconds, element);
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            customWait.until(ExpectedConditions.invisibilityOf(element));
            LogUI.info("Element [{}] was removed/invisible within [{}] seconds.", element, seconds);
        } catch (Exception e) {
            LogUI.error("Element [{}] wasn't removed after [{}] seconds. Exception: {}", element, seconds, e.getMessage());
        }
    }


    public void waitUntilElementIsRemoved(By by, int seconds) {
        LogUI.debug("Waiting up to [{}s] for element by [{}] to be removed (invisible).", seconds, by);
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            customWait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            LogUI.info("Element by [{}] was removed/invisible within [{}] seconds.", by, seconds);
        } catch (Exception e) {
            LogUI.error("Element by [{}] wasn't removed after [{}] seconds. Exception: {}", by, seconds, e.getMessage());
        }
    }


    public void waitForElementToBeShownAndRemoved(By by, int secondsToBeShown, int secondsToBeRemoved) {
        LogUI.debug("Waiting for element by [{}] to be shown ({}s) and then removed ({}s).",
                by, secondsToBeShown, secondsToBeRemoved);
        waitUntilElementIsShown(by, secondsToBeShown);
        waitUntilElementIsRemoved(by, secondsToBeRemoved);
        LogUI.info("Element by [{}] was shown and removed successfully.", by);
    }


    public void waitForElementToBeShownAndRemoved(WebElement element, int secondsToBeShown, int secondsToBeRemoved) {
        LogUI.debug("Waiting for element [{}] to be shown ({}s) and then removed ({}s).",
                element, secondsToBeShown, secondsToBeRemoved);
        waitUntilElementIsShown(element, secondsToBeShown);
        waitUntilElementIsRemoved(element, secondsToBeRemoved);
        LogUI.info("Element [{}] was shown and removed successfully.", element);
    }


    public void waitForElementToBeRemovedAndShown(WebElement webElement, int secondsToBeShown, int secondsToBeRemoved) {
        LogUI.debug("Waiting for element [{}] to be removed ({}s) then shown ({}s).",
                webElement, secondsToBeRemoved, secondsToBeShown);
        waitUntilElementIsRemoved(webElement, secondsToBeRemoved);
        waitUntilElementIsShown(webElement, secondsToBeShown);
        LogUI.info("Element [{}] was removed then re-shown successfully.", webElement);
    }


    public void switchToIFrame(By by) {
        LogUI.debug("Switching to iframe located by [{}].", by);
        WebElement frameElement = waitAndFindElement(by);
        driver.switchTo().frame(frameElement);
        LogUI.info("Switched to iframe by [{}].", by);
    }

    public void switchToParentIFrame() {
        LogUI.debug("Switching to parent frame.");
        driver.switchTo().parentFrame();
        LogUI.info("Switched to parent frame.");
    }

    public void localWait(float sec) {
        LogUI.debug("Pausing execution for [{}] second(s).", sec);
        try {
            Thread.sleep((long) sec * 1000);
            LogUI.info("Resuming execution after local wait of [{}] second(s).", sec);
        } catch (InterruptedException e) {
            LogUI.error("Interrupted during localWait of [{}] second(s): {}", sec, e.getMessage());
        }
    }


    public boolean checkNoException(Runnable runnable) {
        LogUI.debug("Checking if runnable can execute without exception.");
        try {
            runnable.run();
            LogUI.info("Runnable executed successfully without exception.");
            return true;
        } catch (Exception e) {
            LogUI.warn("Runnable threw an exception: {}", e.getMessage(), e);
            return false;
        }
    }


    public void waitUntilAttributeValueIsChanged(WebElement element,
                                                 String attributeName,
                                                 String initialAttributeValue) {
        LogUI.debug("Waiting for attribute [{}] of element [{}] to change from '{}'.",
                attributeName, element, initialAttributeValue);
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            customWait.until(attributeValueChanged(element, attributeName, initialAttributeValue));
            LogUI.info("Attribute [{}] of element [{}] changed from '{}'.",
                    attributeName, element, initialAttributeValue);
        } catch (Exception e) {
            LogUI.warn("Attribute [{}] of element [{}] did NOT change within 2s. Current exception: {}",
                    attributeName, element, e.getMessage());
        }
    }


    private ExpectedCondition<Boolean> attributeValueChanged(final WebElement element, final String attributeName,
                                                             final String initialValue) {
        return driver -> {
            String currentValue = smartGetAttribute(element, attributeName);
            boolean res = !initialValue.equals(currentValue);
            return res;
        };
    }

}
