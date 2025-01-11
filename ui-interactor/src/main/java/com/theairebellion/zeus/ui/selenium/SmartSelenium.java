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
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(uiConfig.waitDuration()));
        actions = new Actions(driver);
    }


    public void smartSendKeys(WebElement element, String value) {
        LogUI.extended(String.format("Sending keys: [%s] for element: [%s]", value, element.toString()));
        try {
            element.sendKeys(value);
        } catch (Exception e) {
            TriConsumer<WebDriver, WebElement, String> exceptionHandling =
                ExceptionHandling.exceptionSendKeysMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                throw e;
            }

            exceptionHandling.accept(driver, element, value);
        }
    }


    public void smartClick(WebElement element) {
        LogUI.extended("Smart click of element: '{}' is about to be performed" , element.toString());
        try {
            element.click();
        } catch (Exception e) {
            TriConsumer<WebDriver, WebElement, Consumer<WebElement>> exceptionHandling =
                ExceptionHandling.exceptionElementActionMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                throw e;
            }

            exceptionHandling.accept(driver, element, WebElement::click);
        }
    }


    public void smartClear(WebElement element) {
        LogUI.extended(String.format("Clear element: [%s]", element.toString()));
        try {
            element.clear();
        } catch (Exception e) {
            TriConsumer<WebDriver, WebElement, Consumer<WebElement>> exceptionHandling =
                ExceptionHandling.exceptionElementActionMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                throw e;
            }

            exceptionHandling.accept(driver, element, WebElement::clear);
        }
        if (!smartGetAttribute(element, "value").isEmpty()) {
            smartSendKeys(element, Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        }
        if (!smartGetAttribute(element, "value").isEmpty()) {
            smartClick(element);
            actions.keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND).sendKeys(Keys.DELETE).perform();
        }
    }


    public void smartMove(WebElement element) {
        LogUI.extended(String.format("Move to element: [%s]", element.toString()));
        try {
            actions.moveToElement(element);
            actions.perform();
        } catch (Exception e) {
            FourConsumer<WebDriver, Actions, WebElement, Consumer<WebElement>> exceptionHandling =
                ExceptionHandling.exceptionActionMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                throw e;
            }
            exceptionHandling.accept(driver, actions, element, actions::moveToElement);
        }
    }


    public String smartGetText(WebElement element) {
        LogUI.extended(String.format("Getting text from element: [%s]", element.toString()));
        try {
            return element.getText();
        } catch (Exception e) {
            TriFunction<WebDriver, WebElement, Function<WebElement, String>, String> exceptionHandling =
                ExceptionHandling.exceptionElementActionMapReturn.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                throw e;
            }
            return exceptionHandling.apply(driver, element, WebElement::getText);
        }
    }


    public WebElement smartFindElement(WebElement element, By by) {
        LogUI.extended(String.format("Finding element from element: [%s] by locator: [%s]", element.toString(),
            by.toString()));
        try {
            return element.findElement(by);
        } catch (Exception e) {
            TriFunction<WebDriver, WebElement, By, WebElement> exceptionHandling =
                ExceptionHandling.exceptionFindElementMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                throw e;
            }
            return exceptionHandling.apply(driver, element, by);
        }
    }


    public WebElement smartFindElement(By by) {
        return driver.findElement(by);
    }


    public List<WebElement> smartFindElements(WebElement element, By by) {
        LogUI.extended(String.format("Finding elements from element: [%s] by locator: [%s]", element.toString(),
            by.toString()));
        try {
            return element.findElements(by);
        } catch (Exception e) {
            TriFunction<WebDriver, WebElement, By, List<WebElement>> exceptionHandling =
                ExceptionHandling.exceptionFindElementsMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                throw e;
            }
            return exceptionHandling.apply(driver, element, by);
        }
    }


    public List<WebElement> smartFindElements(By by) {
        return driver.findElements(by);
    }


    public String smartGetAttribute(WebElement element, String attribute) {
        LogUI.extended(String.format("Getting attribute: [%s] from element: [%s]", attribute, element.toString()));
        try {
            return element.getAttribute(attribute);
        } catch (Exception e) {
            TriFunction<WebDriver, WebElement, String, String> exceptionHandling =
                ExceptionHandling.exceptionGetAttributeMap.get(e.getClass());

            if (Objects.isNull(exceptionHandling)) {
                throw e;
            }
            return exceptionHandling.apply(driver, element, attribute);
        }
    }


    public WebElement waitAndFindElement(WebElement element, By by) {
        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(element, by));
        return smartFindElement(element, by);
    }


    public WebElement waitAndFindElement(By by) {
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        return smartFindElement(by);
    }


    public List<WebElement> waitAndFindElements(WebElement element, By by) {
        wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(element, by));
        return smartFindElements(element, by);
    }


    public List<WebElement> waitAndFindElements(By by) {
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        return smartFindElements(by);
    }


    public void waitAndClickElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        smartMove(element);
        smartClick(element);
    }


    public void waitAndClickElement(By by) {
        wait.until(ExpectedConditions.elementToBeClickable(by));
        WebElement element = smartFindElement(by);
        smartMove(element);
        smartClick(element);
    }


    public void waitAndSendKeys(WebElement element, String value) {
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        smartSendKeys(element, value);
    }


    public void waitAndSendKeys(By by, String value) {
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        wait.until(ExpectedConditions.elementToBeClickable(by));
        smartSendKeys(smartFindElement(by), value);
    }


    public void clearAndSendKeys(WebElement element, String value) {
        wait.until(ExpectedConditions.visibilityOf(element));
        smartMove(element);
        smartClear(element);
        smartSendKeys(element, value);
    }


    public boolean checkIfElementIsPresent(WebElement element, By by) {
        try {
            smartFindElement(element, by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean checkIfElementIsPresent(By by) {
        try {
            smartFindElement(by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void waitUntilElementIsPresent(By by) {
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }


    public void waitUntilElementIsShown(WebElement element, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            LogUI.error("Element wasn't displayed after: " + seconds + " seconds");
        }
    }


    public void waitUntilElementIsShown(By by, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception e) {
            LogUI.error("Element wasn't displayed after: " + seconds + " seconds");
        }
    }


    public void waitUntilElementIsRemoved(WebElement element, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception e) {
            LogUI.error("Element wasn't removed after: " + seconds + " seconds");
        }
    }


    public void waitUntilElementIsRemoved(By by, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (Exception e) {
            LogUI.error("Element wasn't removed after: " + seconds + " seconds");
        }
    }


    public void waitForElementToBeShownAndRemoved(By by, int secondsToBeShown, int secondsToBeRemoved) {
        waitUntilElementIsShown(by, secondsToBeShown);
        waitUntilElementIsRemoved(by, secondsToBeRemoved);
    }


    public void waitForElementToBeShownAndRemoved(WebElement element, int secondsToBeShown, int secondsToBeRemoved) {
        waitUntilElementIsShown(element, secondsToBeShown);
        waitUntilElementIsRemoved(element, secondsToBeRemoved);
    }


    public void waitForElementToBeRemovedAndShown(WebElement webElement, int secondsToBeShown,
                                                  int secondsToBeRemoved) {
        waitUntilElementIsRemoved(webElement, secondsToBeRemoved);
        waitUntilElementIsShown(webElement, secondsToBeShown);
    }


    public void switchToIFrame(By by) {
        driver.switchTo().frame(waitAndFindElement(by));
    }


    public void switchToParentIFrame() {
        driver.switchTo().parentFrame();
    }


    public void localWait(float sec) {
        // synchronized (this) {
        try {
            Thread.sleep((long) sec * 1000);
            // wait((long) sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // }
    }


    public boolean checkNoException(Runnable runnable) {
        try {
            runnable.run();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void waitUntilAttributeValueIsChanged(WebElement element, String attributeName,
                                                 String initialAttributeValue) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            customWait.until(attributeValueChanged(element, attributeName, initialAttributeValue));
        } catch (Exception ignore) {
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
