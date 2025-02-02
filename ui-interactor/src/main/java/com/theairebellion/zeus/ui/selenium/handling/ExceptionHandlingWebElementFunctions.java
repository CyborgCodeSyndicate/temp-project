package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExceptionHandlingWebElementFunctions {

    public static SmartWebElement findElementStaleElementExceptionHandling(WebDriver driver, SmartWebElement element, By by) {
        element = updateWebElement(driver, element);
        WebElement updatedElement = element.getOriginal().findElement(by);
        return new SmartWebElement(updatedElement, driver);
    }

    public static SmartWebElement findElementNoSuchElementExceptionHandling(WebDriver driver, SmartWebElement element) {
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        for (WebElement iframe : iframes) {
            driver.switchTo().frame(iframe);
            if (element.isDisplayed()) {
                return new SmartWebElement(element, driver);
            }
            driver.switchTo().defaultContent();
        }
        LogUI.error("Element not found in the main DOM or any iframe.");
        throw new NoSuchElementException("Element not found in any iframe.");
    }

    public static Void clickStaleElementExceptionHandling(WebDriver driver, SmartWebElement element) {
        element = updateWebElement(driver, element);
        element.getOriginal().click();
        return null;
    }

    public static Void clickElementClickInterceptedExceptionHandling(WebDriver driver, SmartWebElement element, Exception exception) {
        By blocker = By.xpath(extractBlockingElementLocator(exception.getMessage()));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(blocker));
        element.getOriginal().click();
        return null;
    }

    public static Void clickElementNotInteractableExceptionHandling(WebDriver driver, SmartWebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);
        actions.moveToElement(element.getOriginal());
        wait.until(ExpectedConditions.elementToBeClickable(element.getOriginal())).click();
        return null;
    }

    public static Void clickNoSuchElementExceptionHandling(WebDriver driver, SmartWebElement element) {
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        for (WebElement iframe : iframes) {
            driver.switchTo().frame(iframe);
            if (element.isDisplayed()) {
                element.getOriginal().click();
                return null;
            }
            driver.switchTo().defaultContent();
        }
        LogUI.error("Element not found in the main DOM or any iframe.");
        throw new NoSuchElementException("Element not found in any iframe.");
    }

    public static Void sendKeysStaleElementExceptionHandling(WebDriver driver, String keysToSend, SmartWebElement element) {
        element = updateWebElement(driver, element);
        element.getOriginal().sendKeys(keysToSend);
        return null;
    }

    public static Void sendKeysElementClickInterceptedExceptionHandling(WebDriver driver, String keysToSend, SmartWebElement element, Exception exception) {
        By blocker = By.xpath(extractBlockingElementLocator(exception.getMessage()));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(blocker));
        element.getOriginal().sendKeys(keysToSend);
        return null;
    }

    public static Void sendKeysElementNotInteractableExceptionHandling(WebDriver driver, String keysToSend, SmartWebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);
        actions.moveToElement(element.getOriginal());
        wait.until(ExpectedConditions.elementToBeClickable(element.getOriginal())).sendKeys(keysToSend);
        return null;
    }

    public static Void sendKeysNoSuchElementExceptionHandling(WebDriver driver, String keysToSend, SmartWebElement element) {
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        for (WebElement iframe : iframes) {
            driver.switchTo().frame(iframe);
            if (element.isDisplayed()) {
                element.getOriginal().sendKeys(keysToSend);
                return null;
            }
            driver.switchTo().defaultContent();
        }
        LogUI.error("Element not found in the main DOM or any iframe.");
        throw new NoSuchElementException("Element not found in any iframe.");
    }

    public static Void submitStaleElementExceptionHandling(WebDriver driver, SmartWebElement element) {
        element = updateWebElement(driver, element);
        element.getOriginal().submit();
        return null;
    }

    public static Void submitElementNotInteractableExceptionHandling(WebDriver driver, SmartWebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);
        actions.moveToElement(element.getOriginal());
        wait.until(ExpectedConditions.elementToBeClickable(element.getOriginal())).submit();
        return null;
    }

    public static Void submitNoSuchElementExceptionHandling(WebDriver driver, SmartWebElement element) {
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        for (WebElement iframe : iframes) {
            driver.switchTo().frame(iframe);
            if (element.isDisplayed()) {
                element.getOriginal().submit();
                return null;
            }
            driver.switchTo().defaultContent();
        }
        LogUI.error("Element not found in the main DOM or any iframe.");
        throw new NoSuchElementException("Element not found in any iframe.");
    }

    private static SmartWebElement updateWebElement(WebDriver driver, SmartWebElement element) {
        LogUI.extended("Element: '{}' is being relocated.", element.toString());

        final List<String> locators = Arrays.asList(element.toString().split("->"));
        final List<String> trimmedLocators = locators.stream()
                .map(String::trim)
                .toList();

        List<By> locatorsList = new ArrayList<>();

        for (int i = 1; i < trimmedLocators.size(); i++) {
            createLocator(locatorsList, trimmedLocators.get(i), "tag name", By::tagName);
            createLocator(locatorsList, trimmedLocators.get(i), "css selector", By::cssSelector);
            createLocator(locatorsList, trimmedLocators.get(i), "xpath", By::xpath);
            createLocator(locatorsList, trimmedLocators.get(i), "id", By::id);
            createLocator(locatorsList, trimmedLocators.get(i), "class name", By::className);
            createLocator(locatorsList, trimmedLocators.get(i), "name", By::name);
            createLocator(locatorsList, trimmedLocators.get(i), "link text", By::linkText);
            createLocator(locatorsList, trimmedLocators.get(i), "partial link text", By::partialLinkText);
        }

        WebElement updatedElement = driver.findElement(new ByChained(locatorsList.toArray(new By[0])));

        return new SmartWebElement(updatedElement, driver);
    }


    private static void createLocator(List<By> locatorList, String locatorText, String tagName,
                                      Function<String, By> locatorFunction) {
        if (locatorText.split(":")[0].equals(tagName)) {
            String locator = findLocator(locatorText);
            locatorList.add(locatorFunction.apply(locator));
        }
    }

    private static String findLocator(String locator) {
        String loc = locator.split(":")[1];
        String trimLocked = loc.trim();
        return trimLocked.substring(0, trimLocked.length() - 2);
    }

    private static String extractBlockingElementLocator(String exceptionMessage) {
        String pattern = "Other element would receive the click: <(\\w+)([^>]*)>";
        Matcher matcher = Pattern.compile(pattern).matcher(exceptionMessage);
        if (matcher.find()) {
            String tag = matcher.group(1);
            String attributes = matcher.group(2).trim();
            if (attributes.isEmpty()) {
                return "//" + tag;
            }
            String[] attrPairs = attributes.split("\\s+");
            StringBuilder xpath = new StringBuilder("//" + tag + "[");
            for (int i = 0; i < attrPairs.length; i++) {
                String[] keyValue = attrPairs[i].split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1].replace("\"", "").replace("'", "");
                    xpath.append("@").append(key).append("='").append(value).append("'");
                    if (i < attrPairs.length - 1) {
                        xpath.append(" and ");
                    }
                }
            }
            xpath.append("]");
            return xpath.toString();
        }
        return null;
    }


}
