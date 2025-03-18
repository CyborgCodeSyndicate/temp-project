package com.theairebellion.zeus.ui.selenium.locating;

import com.theairebellion.zeus.ui.selenium.shadowroot.ShadowDomUtils;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for finding elements in the DOM, including support for Shadow DOM elements.
 * <p>
 * This class provides methods to locate elements normally and within Shadow DOMs,
 * handling different WebDriver configurations.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public final class SmartFinder {

    private SmartFinder() {
    }

    /**
     * Finds an element without applying any custom logic.
     *
     * @param driver The WebDriver instance.
     * @param by     The locator for the element.
     * @return The located SmartWebElement.
     */
    public static SmartWebElement findElementNoWrap(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        return new SmartWebElement(element, driver);
    }

    /**
     * Finds an element normally with a waiting condition.
     *
     * @param driver The WebDriver instance.
     * @param by     The locator for the element.
     * @param waitFn The waiting function to apply.
     * @return The located SmartWebElement.
     */
    public static SmartWebElement findElementNormally(
            WebDriver driver,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        waitFn.accept(ExpectedConditions.presenceOfElementLocated(by));
        return findElementNoWrap(driver, by);
    }

    /**
     * Finds an element within Shadow DOM, applying wait conditions.
     *
     * @param smartDriver  The SmartWebDriver instance.
     * @param by           The locator for the element.
     * @param waitFn       The waiting function to apply.
     * @param waitInMillis Optional wait time in milliseconds.
     * @return The located SmartWebElement.
     */
    public static SmartWebElement findElementWithShadowRootDriver(
            SmartWebDriver smartDriver,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn,
            Long waitInMillis
    ) {
        List<WebElement> elements = smartDriver.getOriginal().findElements(by);
        if (!elements.isEmpty()) {
            return new SmartWebElement(elements.get(0), smartDriver.getOriginal());
        }

        if (ShadowDomUtils.shadowRootElementsPresent(smartDriver)) {
            return (waitInMillis == null)
                    ? ShadowDomUtils.findElementInShadowRoots(smartDriver, by)
                    : ShadowDomUtils.findElementInShadowRoots(smartDriver, by, waitInMillis);
        } else {
            waitFn.accept(ExpectedConditions.presenceOfElementLocated(by));
            WebElement element = smartDriver.getOriginal().findElement(by);
            return new SmartWebElement(element, smartDriver.getOriginal());
        }
    }

    /**
     * Finds a single SmartWebElement using a shadow root-aware WebDriver.
     *
     * @param smartDriver The SmartWebDriver instance.
     * @param by          The locator to find the element.
     * @param waitFn      A function to apply waiting logic before locating the element.
     * @return A SmartWebElement located using the given parameters.
     */
    public static SmartWebElement findElementWithShadowRootDriver(
            SmartWebDriver smartDriver,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        return findElementWithShadowRootDriver(smartDriver, by, waitFn, null);
    }

    /**
     * Finds multiple SmartWebElements without wrapping them in additional waiting logic.
     *
     * @param driver The WebDriver instance.
     * @param by     The locator to find the elements.
     * @return A list of SmartWebElements found using the given locator.
     */
    public static List<SmartWebElement> findElementsNoWrap(WebDriver driver, By by) {
        return driver.findElements(by).stream()
                .map(elem -> new SmartWebElement(elem, driver))
                .collect(Collectors.toList()); // or .toList() if using Java 16+
    }

    /**
     * Finds multiple elements normally.
     *
     * @param driver The WebDriver instance.
     * @param by     The locator for the elements.
     * @param waitFn The waiting function to apply.
     * @return A list of located SmartWebElements.
     */
    public static List<SmartWebElement> findElementsNormally(
            WebDriver driver,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        waitFn.accept(ExpectedConditions.presenceOfElementLocated(by));
        List<WebElement> elements = driver.findElements(by);

        if (!elements.isEmpty()) {
            return toSmartWebElements(elements, driver);
        }
        WebElement single = driver.findElement(by);
        return List.of(new SmartWebElement(single, driver));
    }

    /**
     * Finds multiple SmartWebElements using shadow root-aware WebDriver logic.
     *
     * @param smartDriver The SmartWebDriver instance.
     * @param by          The locator to find the elements.
     * @param waitFn      A function to apply waiting logic before locating the elements.
     * @return A list of SmartWebElements found using shadow root detection.
     */
    public static List<SmartWebElement> findElementsWithShadowRootDriver(
            SmartWebDriver smartDriver,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        WebDriver driver = smartDriver.getOriginal();
        List<WebElement> elements = driver.findElements(by);

        if (!elements.isEmpty()) {
            return toSmartWebElements(elements, driver);
        }

        if (ShadowDomUtils.shadowRootElementsPresent(smartDriver)) {
            return ShadowDomUtils.findElementsInShadowRoots(smartDriver, by);
        } else {
            waitFn.accept(ExpectedConditions.presenceOfElementLocated(by));
            List<WebElement> elementsAfterWait = driver.findElements(by);
            if (!elementsAfterWait.isEmpty()) {
                return toSmartWebElements(elementsAfterWait, driver);
            } else {
                WebElement single = driver.findElement(by);
                return List.of(new SmartWebElement(single, driver));
            }
        }
    }

    /**
     * Finds a single SmartWebElement without additional wrapping or waiting logic.
     *
     * @param smartElem The SmartWebElement instance.
     * @param by        The locator to find the element.
     * @return A SmartWebElement found using the given locator.
     */
    public static SmartWebElement findElementNoWrap(SmartWebElement smartElem, By by) {
        WebElement element = smartElem.getOriginal().findElement(by);
        return new SmartWebElement(element, smartElem.getDriver());
    }

    /**
     * Finds a single SmartWebElement using standard WebDriver waiting logic.
     *
     * @param smartElem The SmartWebElement instance.
     * @param by        The locator to find the element.
     * @param waitFn    A function to apply waiting logic before locating the element.
     * @return A SmartWebElement found using the given parameters.
     */
    public static SmartWebElement findElementNormally(
            SmartWebElement smartElem,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        waitFn.accept(ExpectedConditions.presenceOfElementLocated(by));
        WebElement element = smartElem.getOriginal().findElement(by);
        return new SmartWebElement(element, smartElem.getDriver());
    }

    /**
     * Finds a single SmartWebElement using shadow root-aware logic.
     *
     * @param smartElem The SmartWebElement instance.
     * @param by        The locator to find the element.
     * @param waitFn    A function to apply waiting logic before locating the element.
     * @return A SmartWebElement located using shadow root detection.
     */
    public static SmartWebElement findElementWithShadowRootElement(
            SmartWebElement smartElem,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        List<WebElement> elements = smartElem.getOriginal().findElements(by);
        if (!elements.isEmpty()) {
            return new SmartWebElement(elements.get(0), smartElem.getDriver());
        }

        if (ShadowDomUtils.shadowRootElementsPresent(smartElem)) {
            return ShadowDomUtils.findElementInShadowRoots(smartElem, by);
        } else {
            return findElementNormally(smartElem, by, waitFn);
        }
    }

    /**
     * Finds multiple SmartWebElements without additional waiting logic from a SmartWebElement.
     *
     * @param smartElem The SmartWebElement instance.
     * @param by        The locator to find the elements.
     * @return A list of SmartWebElements found using the given locator.
     */
    public static List<SmartWebElement> findElementsNoWrap(SmartWebElement smartElem, By by) {
        return smartElem.getOriginal().findElements(by).stream()
                .map(e -> new SmartWebElement(e, smartElem.getDriver()))
                .collect(Collectors.toList());
    }

    /**
     * Finds multiple SmartWebElements using standard WebDriver waiting logic.
     *
     * @param smartElem The SmartWebElement instance.
     * @param by        The locator to find the elements.
     * @param waitFn    A function to apply waiting logic before locating the elements.
     * @return A list of SmartWebElements found using the given parameters.
     */
    public static List<SmartWebElement> findElementsNormally(
            SmartWebElement smartElem,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        waitFn.accept(ExpectedConditions.presenceOfElementLocated(by));
        List<WebElement> elements = smartElem.getOriginal().findElements(by);
        if (!elements.isEmpty()) {
            return toSmartWebElements(elements, smartElem.getDriver());
        } else {
            WebElement single = smartElem.getOriginal().findElement(by);
            return List.of(new SmartWebElement(single, smartElem.getDriver()));
        }
    }

    /**
     * Finds multiple SmartWebElements within shadow DOM-aware WebDriver logic.
     *
     * @param smartElem The SmartWebElement instance.
     * @param by        The locator to find the elements.
     * @param waitFn    A function to apply waiting logic before locating the elements.
     * @return A list of SmartWebElements found using shadow root detection.
     */
    public static List<SmartWebElement> findElementsWithShadowRootElement(
            SmartWebElement smartElem,
            By by,
            Consumer<Function<WebDriver, ?>> waitFn
    ) {
        List<WebElement> elements = smartElem.getOriginal().findElements(by);
        if (!elements.isEmpty()) {
            return toSmartWebElements(elements, smartElem.getDriver());
        }

        if (ShadowDomUtils.shadowRootElementsPresent(smartElem)) {
            return ShadowDomUtils.findElementsInShadowRoots(smartElem, by);
        } else {
            return findElementsNormally(smartElem, by, waitFn);
        }
    }


    private static List<SmartWebElement> toSmartWebElements(List<WebElement> elements, WebDriver driver) {
        return elements.stream()
                .map(e -> new SmartWebElement(e, driver))
                .collect(Collectors.toList());
    }

}
