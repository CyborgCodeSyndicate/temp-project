package com.theairebellion.zeus.ui.selenium.helper;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Utility class for handling iframe-related operations within a Selenium WebDriver instance.
 * <p>
 * This class provides methods to search for elements inside iframes when they are not found in the main DOM.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class FrameHelper {

    private FrameHelper() {
    }

    /**
     * Searches for a WebElement inside iframes using a given locator.
     *
     * @param driver The WebDriver instance.
     * @param by     The locator of the target element.
     * @return A {@link SmartWebElement} if found, otherwise {@code null}.
     */
    public static SmartWebElement findElementInIFrames(WebDriver driver, By by) {
        driver.switchTo().defaultContent();
        return searchElementInIFrames(driver, by);
    }

    /**
     * Searches for a WebElement inside iframes using an existing WebElement reference.
     *
     * @param driver  The WebDriver instance.
     * @param element The reference WebElement to find inside an iframe.
     * @return A {@link SmartWebElement} if found, otherwise {@code null}.
     */
    public static SmartWebElement findElementInIFrames(WebDriver driver, WebElement element) {
        driver.switchTo().defaultContent();
        return searchElementInIFramesByElement(driver, element);
    }

    /**
     * Searches for a WebElement inside available iframes using a locator.
     *
     * @param driver The WebDriver instance.
     * @param by     The locator of the target element.
     * @return A {@link SmartWebElement} if found, otherwise {@code null}.
     */
    private static SmartWebElement searchElementInIFrames(WebDriver driver, By by) {
        List<WebElement> elements = driver.findElements(by);
        if (!elements.isEmpty()) {
            return new SmartWebElement(elements.get(0), driver);
        }

        List<WebElement> frames = getAllIFrames(driver);
        for (WebElement frame : frames) {
            driver.switchTo().frame(frame);
            elements = driver.findElements(by);
            if (!elements.isEmpty()) {
                return new SmartWebElement(elements.get(0), driver);
            } else {
                driver.switchTo().defaultContent();
            }
        }

        return null;
    }

    /**
     * Searches for an existing WebElement inside available iframes.
     *
     * @param driver  The WebDriver instance.
     * @param element The reference WebElement to locate inside an iframe.
     * @return A {@link SmartWebElement} if found, otherwise {@code null}.
     */
    private static SmartWebElement searchElementInIFramesByElement(WebDriver driver, WebElement element) {
        List<WebElement> frames = getAllIFrames(driver);
        for (WebElement frame : frames) {
            driver.switchTo().frame(frame);
            try {
                WebElement foundElement = locateElementByAttributes(driver, element);
                if (foundElement != null) {
                    return new SmartWebElement(foundElement, driver);
                }
            } catch (Exception e) {
                driver.switchTo().defaultContent();
            }
        }

        return null;
    }

    /**
     * Attempts to locate an element by matching its tag name and unique attributes.
     *
     * @param driver          The WebDriver instance.
     * @param originalElement The reference WebElement to match.
     * @return A WebElement if found, otherwise {@code null}.
     */
    private static WebElement locateElementByAttributes(WebDriver driver, WebElement originalElement) {
        String tagName = originalElement.getTagName();
        String attributes = getUniqueAttributes(driver, originalElement);

        @SuppressWarnings("java:S1075")
        String xpathExpression = "//" + tagName + attributes;
        List<WebElement> matchingElements = driver.findElements(By.xpath(xpathExpression));

        for (WebElement el : matchingElements) {
            if (el.getText().equals(originalElement.getText())) {
                return el;
            }
        }
        return null;
    }

    /**
     * Extracts unique attributes from a WebElement for use in locating it later.
     *
     * @param driver  The WebDriver instance.
     * @param element The target WebElement.
     * @return A string representation of the element's attributes in XPath format.
     */
    private static String getUniqueAttributes(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script =
                "var attrs = arguments[0].attributes, result = ''; " +
                        "for (var i = 0; i < attrs.length; i++) { " +
                        "    if (attrs[i].name !== 'style') { " +
                        "        result += '[@' + attrs[i].name + '=\"' + attrs[i].value + '\"]'; " +
                        "    } " +
                        "} return result;";

        return (String) js.executeScript(script, element);
    }

    /**
     * Retrieves all iframe elements from the current page.
     *
     * @param driver The WebDriver instance.
     * @return A list of iframe WebElements.
     */
    private static List<WebElement> getAllIFrames(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (List<WebElement>) js.executeScript(
                "return Array.from(document.getElementsByTagName('iframe'));"
        );
    }
}