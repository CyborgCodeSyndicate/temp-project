package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ExceptionHandlingWebElementFunctions {

    public static SmartWebElement findElementHandling(WebDriver driver, SmartWebElement element, By by) {
        element = updateWebElement(driver, element);
        WebElement updatedElement = element.findSmartElement(by);
        return new SmartWebElement(updatedElement, driver);
    }

    public static SmartWebElement handleNoSuchElement(WebDriver driver, SmartWebElement element, By by) {
        return tryToFindElementInIFrame(driver, by);
    }


    public static Void clickElementHandling(WebDriver driver, SmartWebElement element) {
        element = updateWebElement(driver, element);
        element.getOriginal().click();
        return null;
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

        return new SmartWebDriver(driver).findSmartElement(
            new ByChained(locatorsList.toArray(new By[0])), 10);
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


    private static SmartWebElement tryToFindElementInIFrame(WebDriver driver, By by) {
        driver.switchTo().defaultContent();
        return findElementRecursively(driver, by);

    }


    private static SmartWebElement findElementRecursively(WebDriver driver, By by) {
        try {
            SmartWebElement element = new SmartWebDriver(driver).findSmartElement(by,10);
            return element;
        } catch (NoSuchElementException e) {
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        List<WebElement> frames = (List<WebElement>) js.executeScript(
            "return Array.from(document.getElementsByTagName('iframe'));"
        );

        for (WebElement frame : frames) {
            driver.switchTo().frame(frame);

            WebElement elementInChild = findElementRecursively(driver, by);
            if (elementInChild != null) {
                return new SmartWebElement(elementInChild, driver);
            }

            driver.switchTo().parentFrame();
        }

        return null;
    }

}
