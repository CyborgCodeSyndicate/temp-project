package com.theairebellion.zeus.ui.selenium;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.util.FourConsumer;
import com.theairebellion.zeus.ui.util.TriConsumer;
import com.theairebellion.zeus.ui.util.TriFunction;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


public class ExceptionHandling {


    public static final Map<Class<? extends Exception>, TriConsumer<WebDriver, WebElement, String>>
        exceptionSendKeysMap =
        Map.of(StaleElementReferenceException.class, ExceptionHandling::staleElement,
            InvalidElementStateException.class, ExceptionHandling::invalidElementStateSendKeys);

    public static final Map<Class<? extends Exception>, TriConsumer<WebDriver, WebElement, Consumer<WebElement>>>
        exceptionElementActionMap =
        Map.of(StaleElementReferenceException.class, ExceptionHandling::invalidElementStateElementAction);

    public static final Map<Class<? extends Exception>, TriFunction<WebDriver, WebElement, Function<WebElement, String>, String>>
        exceptionElementActionMapReturn =
        Map.of(StaleElementReferenceException.class, ExceptionHandling::invalidElementStateElementActionReturn);

    public static final Map<Class<? extends Exception>, FourConsumer<WebDriver, Actions, WebElement, Consumer<WebElement>>>
        exceptionActionMap =
        Map.of(StaleElementReferenceException.class, ExceptionHandling::invalidElementStateAction);

    public static final Map<Class<? extends Exception>, TriFunction<WebDriver, WebElement, By, WebElement>>
        exceptionFindElementMap =
        Map.of(StaleElementReferenceException.class, ExceptionHandling::invalidElementStateFindElement);

    public static final Map<Class<? extends Exception>, TriFunction<WebDriver, WebElement, By, List<WebElement>>>
        exceptionFindElementsMap =
        Map.of(StaleElementReferenceException.class, ExceptionHandling::invalidElementStateFindElements);

    public static final Map<Class<? extends Exception>, TriFunction<WebDriver, WebElement, String, String>>
        exceptionGetAttributeMap =
        Map.of(StaleElementReferenceException.class, ExceptionHandling::invalidElementStateGetAttribute);

    public static void staleElement(WebDriver driver, WebElement element, String value) {
        updateWebElement(driver, element);
        element.sendKeys(value);
    }

    public static void invalidElementStateSendKeys(WebDriver driver, WebElement element, String value) {
        element = updateWebElement(driver, element);
        element.sendKeys(value);
    }

    public static void invalidElementStateElementAction(WebDriver driver, WebElement element, Consumer<WebElement> function) {
        LogUI.extended("Stale element exception was thrown for element: '{}'.", element);
        element = updateWebElement(driver, element);
        function.accept(element);
    }

    public static String  invalidElementStateElementActionReturn(WebDriver driver, WebElement element, Function<WebElement, String> function) {
        element = updateWebElement(driver, element);
        return function.apply(element);
    }

    public static void invalidElementStateAction(WebDriver driver, Actions actions, WebElement element, Consumer<WebElement> function) {
        actions.moveToElement(element);
        element = updateWebElement(driver, element);
        function.accept(element);
        actions.perform();
    }

    public static WebElement invalidElementStateFindElement(WebDriver driver, WebElement element, By by) {
        element = updateWebElement(driver, element);
        return element.findElement(by);
    }

    public static List<WebElement> invalidElementStateFindElements(WebDriver driver, WebElement element, By by) {
        element = updateWebElement(driver, element);
        return element.findElements(by);
    }

    public static String invalidElementStateGetAttribute(WebDriver driver, WebElement element, String attribute) {
        element = updateWebElement(driver, element);
        return element.getAttribute(attribute);
    }

    public static WebElement updateWebElement(WebDriver driver, WebElement element) {
        LogUI.extended("Element: '{}' is being relocated.", element.toString());
        String s = element.toString();
        List<String> locators = Arrays.asList(s.split("->"));
        List<String> trimmedLocators = locators.stream()
            .map(String::trim)
            .collect(Collectors.toList());

        WebElement result = null;
        String s1 = trimmedLocators.get(trimmedLocators.size() - 1);
        s1 = s1 + "]";
        trimmedLocators.remove(trimmedLocators.size() - 1);
        trimmedLocators.add(s1);

        //        By.linkText()
        //        By.partialLinkText()
        //        By.className()

        for (int i = 1; i < trimmedLocators.size(); i++) {
            if (trimmedLocators.get(i).contains("tag name:")) {
                String locator = findLocator(trimmedLocators.get(i));
                if (result == null) {
                    result = driver.findElement(By.tagName(locator));
                } else {
                    result = result.findElement(By.tagName(locator));
                }
            }
            if (trimmedLocators.get(i).contains("css selector:")) {
                String locator = findLocator(trimmedLocators.get(i));
                if (result == null) {
                    result = driver.findElement(By.cssSelector(locator));
                } else {
                    result = result.findElement(By.cssSelector(locator));
                }
            }
            if (trimmedLocators.get(i).contains("xpath:")) {
                String locator = findLocator(trimmedLocators.get(i));
                if (result == null) {
                    result = driver.findElement(By.xpath(locator));
                } else {
                    result = result.findElement(By.xpath(locator));
                }
            }
            if (trimmedLocators.get(i).contains("id:")) {
                String locator = findLocator(trimmedLocators.get(i));
                if (result == null) {
                    result = driver.findElement(By.id(locator));
                } else {
                    result = result.findElement(By.id(locator));
                }
            }
            if (trimmedLocators.get(i).contains("class name:")) {
                String locator = findLocator(trimmedLocators.get(i));
                if (result == null) {
                    result = driver.findElement(By.className(locator));
                } else {
                    result = result.findElement(By.className(locator));
                }
            }
        }
        return result;
    }


    private static String findLocator(String locator) {
        String loc = locator.split(":")[1];
        String trimLocked = loc.trim();
        String finalLocator = trimLocked.substring(0, trimLocked.length() - 2);
        // info("Final Locator: " + finalLocator);
        return finalLocator;
    }

}
